package scra.qnaboard.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.answer.AnswerRepository;
import scra.qnaboard.domain.repository.answer.AnswerSimpleQueryRepository;
import scra.qnaboard.service.exception.answer.delete.AnswerDeleteFailedException;
import scra.qnaboard.service.exception.answer.edit.AnswerEditFailedException;
import scra.qnaboard.service.exception.answer.edit.UnauthorizedAnswerEditException;
import scra.qnaboard.service.exception.member.MemberNotFoundException;
import scra.qnaboard.web.dto.answer.AnswerDetailDTO;
import scra.qnaboard.web.dto.answer.edit.EditAnswerResultDTO;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

    @InjectMocks
    private AnswerService answerService;

    @Mock
    private MemberService memberService;
    @Mock
    private QuestionService questionService;
    @Mock
    private AnswerRepository answerRepository;
    @Mock
    private AnswerSimpleQueryRepository answerSimpleQueryRepository;

    @Test
    void 답변글_생성_테스트() {
        //given
        long authorId = 1L;
        long questionId = 2L;
        long answerId = 3L;
        //given
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);
        //given
        String title = "title-1";
        String content = "content-1";
        //given
        Question question = new Question(author, content, title);
        ReflectionTestUtils.setField(question, "id", questionId);
        //given
        Answer answerNoId = new Answer(author, content, question);
        Answer answer = new Answer(author, content, question);
        ReflectionTestUtils.setField(answer, "id", answerId);
        //given
        given(memberService.findMember(authorId)).willReturn(author);
        given(questionService.findQuestion(questionId)).willReturn(question);
        given(answerRepository.save(answerNoId)).willReturn(answer);

        //when
        AnswerDetailDTO answerDetailDTO = answerService.createAnswer(authorId, questionId, content);

        //then
        assertThat(answerDetailDTO.getAnswerId()).isEqualTo(answer.getId());
        assertThat(answerDetailDTO.getContent()).isEqualTo(answer.getContent());
    }

    @Test
    void 답변글생성_실패_테스트() {
        //given
        long authorId = 1L;
        given(memberService.findMember(authorId)).willThrow(new MemberNotFoundException(authorId));

        //when & then
        assertThatThrownBy(() -> answerService.createAnswer(authorId, 2L, "content-1"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 답변글_삭제_테스트() {
        //given
        long authorId = 1L;
        long questionId = 2L;
        long answerId = 3L;
        //given
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);
        //given
        String title = "title-1";
        String content = "content-1";
        //given
        Question question = new Question(author, content, title);
        ReflectionTestUtils.setField(question, "id", questionId);
        //given
        Answer answer = new Answer(author, content, question);
        ReflectionTestUtils.setField(answer, "id", answerId);
        //given
        given(memberService.findMember(authorId)).willReturn(author);
        given(answerSimpleQueryRepository.answerWithAuthor(answerId)).willReturn(Optional.of(answer));

        //when & then
        answerService.deleteAnswer(authorId, answerId);
    }

    @Test
    void 답변글_삭제_실패_테스트_작성자및관리자아님() {
        //given
        long authorId = 1L;
        long questionId = 2L;
        long answerId = 3L;
        long anotherAuthorId = 4L;
        //given
        Member anotherAuthor = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(anotherAuthor, "id", anotherAuthorId);
        //given
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);
        //given
        String title = "title-1";
        String content = "content-1";
        //given
        Question question = new Question(author, content, title);
        ReflectionTestUtils.setField(question, "id", questionId);
        //given
        Answer answer = new Answer(author, content, question);
        ReflectionTestUtils.setField(answer, "id", answerId);
        //given
        given(memberService.findMember(anotherAuthorId)).willReturn(anotherAuthor);
        given(answerSimpleQueryRepository.answerWithAuthor(answerId)).willReturn(Optional.of(answer));

        //when & then
        assertThatThrownBy(() -> answerService.deleteAnswer(anotherAuthorId, answerId))
                .isInstanceOf(AnswerDeleteFailedException.class);
    }

    @Test
    void 답변글_삭제_테스트_관리자는_성공해야함() {
        //given
        long authorId = 1L;
        long questionId = 2L;
        long answerId = 3L;
        long anotherAuthorId = 4L;
        //given
        Member anotherAuthor = new Member("nickname", "email", MemberRole.ADMIN);
        ReflectionTestUtils.setField(anotherAuthor, "id", anotherAuthorId);
        //given
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);
        //given
        String title = "title-1";
        String content = "content-1";
        //given
        Question question = new Question(author, content, title);
        ReflectionTestUtils.setField(question, "id", questionId);
        //given
        Answer answer = new Answer(author, content, question);
        ReflectionTestUtils.setField(answer, "id", answerId);
        //given
        given(memberService.findMember(anotherAuthorId)).willReturn(anotherAuthor);
        given(answerSimpleQueryRepository.answerWithAuthor(answerId)).willReturn(Optional.of(answer));

        //when & then
        answerService.deleteAnswer(anotherAuthorId, answerId);
    }

    @Test
    void 답변글_수정_테스트() {
        //given
        long authorId = 1L;
        long questionId = 2L;
        long answerId = 3L;
        //given
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);
        //given
        String title = "title-1";
        String content = "content-1";
        String newContent = "new-content-1";
        //given
        Question question = new Question(author, content, title);
        ReflectionTestUtils.setField(question, "id", questionId);
        //given
        Answer answer = new Answer(author, content, question);
        ReflectionTestUtils.setField(answer, "id", answerId);
        //given
        given(answerSimpleQueryRepository.answerWithAuthor(answerId)).willReturn(Optional.of(answer));
        given(memberService.findMember(authorId)).willReturn(author);

        //when
        EditAnswerResultDTO editAnswerResultDTO = answerService.editAnswer(authorId, answerId, newContent);

        //then
        assertThat(editAnswerResultDTO.getContent()).isEqualTo(newContent);
    }

    @Test
    void 답변글_수정_실패_테스트_작성자및관리자아님() {
        //given
        long authorId = 1L;
        long questionId = 2L;
        long answerId = 3L;
        long anotherAuthorId = 4L;
        //given
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);
        //given
        Member anotherAuthor = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(anotherAuthor, "id", anotherAuthorId);
        //given
        String title = "title-1";
        String content = "content-1";
        String newContent = "new-content-1";
        //given
        Question question = new Question(author, content, title);
        ReflectionTestUtils.setField(question, "id", questionId);
        //given
        Answer answer = new Answer(author, content, question);
        ReflectionTestUtils.setField(answer, "id", answerId);
        //given
        given(answerSimpleQueryRepository.answerWithAuthor(answerId)).willReturn(Optional.of(answer));
        given(memberService.findMember(anotherAuthorId)).willReturn(anotherAuthor);

        //when & then
        assertThatThrownBy(() -> answerService.editAnswer(anotherAuthorId, answerId, newContent))
                .isInstanceOf(AnswerEditFailedException.class)
                .isInstanceOf(UnauthorizedAnswerEditException.class);
    }

    @Test
    void 답변글_수정_테스트_관리자는성공해야함() {
        //given
        long authorId = 1L;
        long questionId = 2L;
        long answerId = 3L;
        long anotherAuthorId = 4L;
        //given
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);
        //given
        Member anotherAuthor = new Member("nickname", "email", MemberRole.ADMIN);
        ReflectionTestUtils.setField(anotherAuthor, "id", anotherAuthorId);
        //given
        String title = "title-1";
        String content = "content-1";
        String newContent = "new-content-1";
        //given
        Question question = new Question(author, content, title);
        ReflectionTestUtils.setField(question, "id", questionId);
        //given
        Answer answer = new Answer(author, content, question);
        ReflectionTestUtils.setField(answer, "id", answerId);
        //given
        given(answerSimpleQueryRepository.answerWithAuthor(answerId)).willReturn(Optional.of(answer));
        given(memberService.findMember(anotherAuthorId)).willReturn(anotherAuthor);

        //when
        EditAnswerResultDTO editAnswerResultDTO = answerService.editAnswer(anotherAuthorId, answerId, newContent);

        //then
        assertThat(editAnswerResultDTO.getContent()).isEqualTo(newContent);
    }
}
