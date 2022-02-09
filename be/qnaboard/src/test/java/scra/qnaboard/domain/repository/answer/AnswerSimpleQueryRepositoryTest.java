package scra.qnaboard.domain.repository.answer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.question.QuestionRepository;
import scra.qnaboard.dto.answer.AnswerDetailDTO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AnswerSimpleQueryRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AnswerSimpleQueryRepository answerSimpleQueryRepository;

    @Test
    void 질문글_아이디로_답변글을_조회할수있어야함_하나도없어도_조회할수있어야함() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));

        //when
        List<AnswerDetailDTO> answerDetailDTOS = answerSimpleQueryRepository.answerDtosByQuestionId(question.getId());

        //then
        assertThat(answerDetailDTOS.size()).isEqualTo(0);
    }

    @Test
    void 질문글_아이디로_답변글을_조회할수있어야함() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        List<Answer> answers = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> answerRepository.save(new Answer(author, "answerContent" + i, question)))
                .collect(Collectors.toList());

        //when
        List<AnswerDetailDTO> answerDetailDTOS = answerSimpleQueryRepository.answerDtosByQuestionId(question.getId());

        //then
        assertThat(answerDetailDTOS.size()).isEqualTo(answers.size());
    }
}
