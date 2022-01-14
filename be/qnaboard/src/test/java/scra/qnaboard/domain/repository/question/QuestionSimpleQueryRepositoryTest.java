package scra.qnaboard.domain.repository.question;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.service.QuestionService;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * QueryDSL을 사용하는 리포지토리 테스트
 */
@SpringBootTest
@Transactional
class QuestionSimpleQueryRepositoryTest {

    @Autowired
    private QuestionSimpleQueryRepository questionSimpleQueryRepository;

    @Autowired
    private QuestionService questionService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 질문글과_작성자_함께조회하는기능_테스트() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));

        //given
        String title = "title-1";
        String content = "content-1";

        //given
        long questionId = questionService.createQuestion(author.getId(), title, content, new ArrayList<>());

        //when
        Optional<Question> findQuestionOptional = questionSimpleQueryRepository.questionWithAuthor(questionId);

        //then
        Question findQuestion = findQuestionOptional.orElse(null);
        assertThat(findQuestion).isNotNull();
        assertThat(findQuestion.getTitle()).isEqualTo(title);
        assertThat(findQuestion.getContent()).isEqualTo(content);
        assertThat(findQuestion.getAuthor().getId()).isEqualTo(author.getId());

    }
}
