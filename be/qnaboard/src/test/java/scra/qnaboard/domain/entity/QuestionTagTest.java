package scra.qnaboard.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.post.Question;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QuestionTagTest {

    @Autowired
    private EntityManager em;

    /**
     * 저장된 엔티티는 영속성 컨택스트를 초기화 한 이후에 식별자를 가지고 찾을 수 있어야 하니까 이를 찾는 코드도 추가함. 또한 Auditing 기능이 잘 적용되고 있는지도 테스트해야함
     */
    @Test
    @DisplayName("태그를 생성할 수 있어야 함")
    void testSaveQuestionTag() {
        Member member = new Member("member1", MemberRole.NORMAL);
        em.persist(member);

        Tag tag = new Tag(member, "tag1", "FF5733");
        em.persist(tag);

        Question question = new Question(member, "content", "title");
        em.persist(question);

        QuestionTag questionTag = new QuestionTag(tag, question);
        em.persist(questionTag);

        em.flush();
        em.clear();

        QuestionTag foundQuestionTag = em.createQuery("select qt from QuestionTag qt " +
                        "inner join fetch qt.tag t " +
                        "where qt.question.id = :question_id", QuestionTag.class)
                .setParameter("question_id", question.getId())
                .getSingleResult();

        assertThat(foundQuestionTag.getTag()).isEqualTo(tag);
    }

}
