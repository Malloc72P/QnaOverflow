package scra.qnaboard.domain.entity.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QuestionTest {

    @Autowired
    private EntityManager em;

    /**
     * 저장된 엔티티는 영속성 컨택스트를 초기화 한 이후에 식별자를 가지고 찾을 수 있어야 하니까 이를 찾는 코드도 추가함. 또한 Auditing 기능이 잘 적용되고 있는지도 테스트해야함
     */
    @Test
    @DisplayName("질문글을 생성할 수 있어야 함")
    void testSaveQuestion() {
        Member member1 = new Member("member1", MemberRole.USER);
        em.persist(member1);

        Question question = new Question(member1, "content1", "title");
        em.persist(question);

        em.flush();
        em.clear();

        Question findQuestion = em.find(Question.class, question.getId());
        assertThat(findQuestion).isEqualTo(question);
        assertThat(findQuestion.getCreatedDate()).isNotNull();
        assertThat(findQuestion.getLastModifiedDate())
                .isNotNull()
                .isEqualTo(findQuestion.getCreatedDate());
    }

    @Test
    @DisplayName("질문글로 답변글을 가지고 올 수 있어야 함")
    void testGetAnswers() {
        Member member1 = new Member("member1", MemberRole.USER);
        em.persist(member1);

        Question question = new Question(member1, "content1", "title");
        em.persist(question);

        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Answer answer = new Answer(member1, "content" + i, question);
            answers.add(answer);
            em.persist(answer);
        }

        em.flush();
        em.clear();

        int size = em.createQuery("select count(a) from Answer a where a.question.id = :id", Long.class)
                .setParameter("id", question.getId()).getSingleResult().intValue();
        assertThat(size).isEqualTo(answers.size());
    }
}
