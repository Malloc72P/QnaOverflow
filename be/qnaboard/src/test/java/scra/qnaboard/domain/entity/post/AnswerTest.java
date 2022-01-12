package scra.qnaboard.domain.entity.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AnswerTest {

    @Autowired
    private EntityManager em;

    /**
     * 저장된 엔티티는 영속성 컨택스트를 초기화 한 이후에 식별자를 가지고 찾을 수 있어야 하니까 이를 찾는 코드도 추가함. 또한 Auditing 기능이 잘 적용되고 있는지도 테스트해야함
     */
    @Test
    @DisplayName("답변글을 생성할 수 있어야 함")
    void testSaveAnswer() {
        Member member1 = new Member("member1", MemberRole.USER);
        em.persist(member1);

        Question question = new Question(member1, "content1", "title");
        em.persist(question);

        Answer answer = new Answer(member1, "content1", question);
        em.persist(answer);

        em.flush();
        em.clear();

        Answer findAnswer = em.find(Answer.class, answer.getId());
        assertThat(findAnswer).isEqualTo(answer);
        assertThat(findAnswer.getCreatedDate()).isNotNull();
        assertThat(findAnswer.getLastModifiedDate())
                .isNotNull()
                .isEqualTo(findAnswer.getCreatedDate());
        assertThat(findAnswer.getQuestion()).isEqualTo(question);
    }
}
