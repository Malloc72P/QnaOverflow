package scra.qnaboard.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.question.QuestionRepository;
import scra.qnaboard.domain.repository.question.QuestionSearchDetailRepository;
import scra.qnaboard.domain.repository.question.QuestionSearchListRepository;
import scra.qnaboard.domain.repository.question.QuestionSimpleQueryRepository;
import scra.qnaboard.service.exception.member.MemberNotFoundException;
import scra.qnaboard.service.exception.question.delete.QuestionDeleteFailedException;
import scra.qnaboard.service.exception.question.edit.QuestionEditFailedException;
import scra.qnaboard.service.exception.question.edit.UnauthorizedQuestionEditException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

/**
 * QuestionService에 대한 단위테스트를 수행함 <br>
 * QuestionService의 비즈니스 로직이 잘 동작하는지에 대해 테스트한다 <br>
 * QuestionService의 의존관계에 있는 객체는 Mock객체를 넣어서 동작하게 함 <br>
 * 리포지토리와 연계한 테스트는 통합 테스트에서 따로 함
 */
@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private MemberService memberService;
    @Mock
    private QuestionTagService questionTagService;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private QuestionSimpleQueryRepository questionSimpleQueryRepository;
    @Mock
    private QuestionSearchDetailRepository questionSearchDetailRepository;
    @Mock
    private QuestionSearchListRepository questionSearchListRepository;

    @Test
    void 질문생성_테스트() throws Exception {
        //given
        long authorId = 1L;

        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);

        given(memberService.findMember(authorId)).willReturn(author);

        //given
        long questionId = 2L;
        String title = "title-1";
        String content = "content-1";
        List<Long> tagIds = new ArrayList<>();

        Question questionParam = new Question(author, content, title);
        Question questionResult = new Question(author, content, title);
        ReflectionTestUtils.setField(questionResult, "id", questionId);

        given(questionRepository.save(questionParam)).willReturn(questionResult);

        //when
        long result = questionService.createQuestion(authorId, title, content, tagIds);

        //then
        assertThat(result).isEqualTo(questionId);
    }

    @Test
    void 질문생성_실패_테스트() {
        //given
        long authorId = 1L;
        given(memberService.findMember(authorId)).willThrow(new MemberNotFoundException(authorId));

        //when & then
        assertThatThrownBy(() -> questionService.createQuestion(authorId, "title-1", "content-1", new ArrayList<>()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 질문삭제_테스트() throws Exception {
        //given
        long authorId = 1L;
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);

        given(memberService.findMember(authorId))
                .willReturn(author);

        //given
        long questionId = 2L;
        Question question = new Question(author, "content-1", "title-1");
        ReflectionTestUtils.setField(question, "id", questionId);

        given(questionSimpleQueryRepository.questionWithAuthor(questionId))
                .willReturn(Optional.of(question));

        //when & then
        questionService.deleteQuestion(authorId, questionId);
    }

    @Test
    void 질문삭제_실패_테스트_작성자및관리자아님() throws Exception {
        //given
        long anotherAuthorId = 3L;
        Member anotherAuthor = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(anotherAuthor, "id", anotherAuthorId);

        given(memberService.findMember(anotherAuthorId))
                .willReturn(anotherAuthor);

        //given
        long authorId = 1L;
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);

        long questionId = 2L;
        Question question = new Question(author, "content-1", "title-1");
        ReflectionTestUtils.setField(question, "id", questionId);

        given(questionSimpleQueryRepository.questionWithAuthor(questionId))
                .willReturn(Optional.of(question));

        //when & then
        assertThatThrownBy(() -> questionService.deleteQuestion(anotherAuthorId, questionId))
                .isInstanceOf(QuestionDeleteFailedException.class);
    }

    @Test
    void 질문삭제_테스트_관리자는_성공해야함() throws Exception {
        //given
        long anotherAuthorId = 3L;
        Member anotherAuthor = new Member("nickname", "email", MemberRole.ADMIN);
        ReflectionTestUtils.setField(anotherAuthor, "id", anotherAuthorId);

        given(memberService.findMember(anotherAuthorId))
                .willReturn(anotherAuthor);

        //given
        long authorId = 1L;
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);

        long questionId = 2L;
        Question question = new Question(author, "content-1", "title-1");
        ReflectionTestUtils.setField(question, "id", questionId);

        given(questionSimpleQueryRepository.questionWithAuthor(questionId))
                .willReturn(Optional.of(question));

        //when & then
        questionService.deleteQuestion(anotherAuthorId, questionId);
    }

    @Test
    void 질문수정_테스트() throws Exception {
        //given
        long authorId = 1L;
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);

        given(memberService.findMember(authorId))
                .willReturn(author);

        //given
        long questionId = 2L;
        Question question = new Question(author, "content-1", "title-1");
        ReflectionTestUtils.setField(question, "id", questionId);

        given(questionSimpleQueryRepository.questionWithAuthor(questionId))
                .willReturn(Optional.of(question));

        //when & then
        questionService.editQuestion(authorId, questionId, "new-title", "new-content", new ArrayList<>());
    }

    @Test
    void 질문수정_실패_테스트_작성자및관리자아님() throws Exception {
        //given
        long anotherAuthorId = 3L;
        Member anotherAuthor = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(anotherAuthor, "id", anotherAuthorId);

        given(memberService.findMember(anotherAuthorId))
                .willReturn(anotherAuthor);

        //given
        long authorId = 1L;
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);

        long questionId = 2L;
        Question question = new Question(author, "content-1", "title-1");
        ReflectionTestUtils.setField(question, "id", questionId);

        given(questionSimpleQueryRepository.questionWithAuthor(questionId))
                .willReturn(Optional.of(question));

        //when & then
        assertThatThrownBy(() -> questionService.editQuestion(anotherAuthorId, questionId, "new-title", "new-content", new ArrayList<>()))
                .isInstanceOf(QuestionEditFailedException.class)
                .isInstanceOf(UnauthorizedQuestionEditException.class);
    }

    @Test
    void 질문수정_테스트_관리자는_성공해야함() throws Exception {
        //given
        long anotherAuthorId = 3L;
        Member anotherAuthor = new Member("nickname", "email", MemberRole.ADMIN);
        ReflectionTestUtils.setField(anotherAuthor, "id", anotherAuthorId);

        given(memberService.findMember(anotherAuthorId))
                .willReturn(anotherAuthor);

        //given
        long authorId = 1L;
        Member author = new Member("nickname", "email", MemberRole.USER);
        ReflectionTestUtils.setField(author, "id", authorId);

        long questionId = 2L;
        Question question = new Question(author, "content-1", "title-1");
        ReflectionTestUtils.setField(question, "id", questionId);

        given(questionSimpleQueryRepository.questionWithAuthor(questionId))
                .willReturn(Optional.of(question));

        //when & then
        questionService.editQuestion(anotherAuthorId, questionId, "new-title", "new-content", new ArrayList<>());
    }
}
