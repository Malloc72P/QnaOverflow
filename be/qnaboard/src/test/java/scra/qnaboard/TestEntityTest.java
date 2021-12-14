package scra.qnaboard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class TestEntityTest {

    @Autowired
    private EntityManager em;

    @Test
    void testEntity() {
        TestEntity testEntity = new TestEntity("asdf");
        em.persist(testEntity);

        em.flush();
        em.clear();
        TestEntity findEntity = em.find(TestEntity.class, testEntity.getId());

        assertThat(findEntity).isEqualTo(testEntity);
    }

}
