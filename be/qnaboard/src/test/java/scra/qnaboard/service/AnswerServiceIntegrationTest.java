package scra.qnaboard.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.answer.AnswerRepository;
import scra.qnaboard.domain.repository.question.QuestionRepository;
import scra.qnaboard.service.exception.answer.delete.AnswerDeleteFailedException;
import scra.qnaboard.service.exception.answer.edit.AnswerEditFailedException;
import scra.qnaboard.service.exception.answer.edit.UnauthorizedAnswerEditException;
import scra.qnaboard.service.exception.member.MemberNotFoundException;
import scra.qnaboard.web.dto.answer.AnswerDetailDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class AnswerServiceIntegrationTest {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void 답변글_생성_테스트() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        String answerContent = "answer-content-1";

        //when
        AnswerDetailDTO answerDetailDTO = answerService.createAnswer(author.getId(), question.getId(), answerContent);

        //then
        assertThat(answerDetailDTO.getContent()).isEqualTo(answerContent);
    }

    @Test
    void 답변글생성_실패_테스트() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //when & then
        assertThatThrownBy(() -> answerService.createAnswer(-1L, question.getId(), "content-1"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 답변글_삭제_테스트() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        String answerContent = "answer-content-1";
        //given
        Answer answer = answerRepository.save(new Answer(author, answerContent, question));

        //when
        answerService.deleteAnswer(author.getId(), answer.getId());

        //then
        Answer findAnswer = answerRepository.findById(answer.getId()).orElse(null);
        assertThat(findAnswer).isNotNull();
        assertThat(findAnswer.isDeleted()).isTrue();
    }

    @Test
    void 답변글_삭제_실패_테스트_작성자및관리자아님() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        Member anotherAuthor = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        String answerContent = "answer-content-1";
        //given
        Answer answer = answerRepository.save(new Answer(author, answerContent, question));

        //when
        assertThatThrownBy(() -> answerService.deleteAnswer(anotherAuthor.getId(), answer.getId()))
                .isInstanceOf(AnswerDeleteFailedException.class);

        //then
        Answer findAnswer = answerRepository.findById(answer.getId()).orElse(null);
        assertThat(findAnswer).isNotNull();
        assertThat(findAnswer.isDeleted()).isFalse();
    }

    @Test
    void 답변글_삭제_테스트_관리자는성공해야함() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        Member anotherAuthor = memberRepository.save(new Member("nickname", "email", MemberRole.ADMIN));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        String answerContent = "answer-content-1";
        //given
        Answer answer = answerRepository.save(new Answer(author, answerContent, question));

        //when
        answerService.deleteAnswer(anotherAuthor.getId(), answer.getId());

        //then
        Answer findAnswer = answerRepository.findById(answer.getId()).orElse(null);
        assertThat(findAnswer).isNotNull();
        assertThat(findAnswer.isDeleted()).isTrue();
    }

    @Test
    void 답변글_수정_테스트() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        String answerContent = "answer-content-1";
        String newAnswerContent = "new-answer-content-1";
        //given
        Answer answer = answerRepository.save(new Answer(author, answerContent, question));

        //when
        answerService.editAnswer(author.getId(), answer.getId(), newAnswerContent);

        //then
        Answer findAnswer = answerRepository.findById(answer.getId()).orElse(null);
        assertThat(findAnswer).isNotNull();
        assertThat(findAnswer.getContent()).isEqualTo(newAnswerContent);
    }

    @Test
    void 답변글_수정_실패_테스트_관리자및작성자아님() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        Member anotherAuthor = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        String answerContent = "answer-content-1";
        String newAnswerContent = "new-answer-content-1";
        //given
        Answer answer = answerRepository.save(new Answer(author, answerContent, question));

        //when
        assertThatThrownBy(() -> answerService.editAnswer(anotherAuthor.getId(), answer.getId(), newAnswerContent))
                .isInstanceOf(AnswerEditFailedException.class)
                .isInstanceOf(UnauthorizedAnswerEditException.class);

        //then
        Answer findAnswer = answerRepository.findById(answer.getId()).orElse(null);
        assertThat(findAnswer).isNotNull();
        assertThat(findAnswer.getContent()).isEqualTo(answerContent);
    }

    @Test
    void 답변글_수정_테스트_관리자는성공해야함() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        Member anotherAuthor = memberRepository.save(new Member("nickname", "email", MemberRole.ADMIN));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        String answerContent = "answer-content-1";
        String newAnswerContent = "new-answer-content-1";
        //given
        Answer answer = answerRepository.save(new Answer(author, answerContent, question));

        //when
        answerService.editAnswer(anotherAuthor.getId(), answer.getId(), newAnswerContent);

        //then
        Answer findAnswer = answerRepository.findById(answer.getId()).orElse(null);
        assertThat(findAnswer).isNotNull();
        assertThat(findAnswer.getContent()).isEqualTo(newAnswerContent);
    }
}
