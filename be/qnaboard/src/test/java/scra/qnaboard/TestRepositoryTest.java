package scra.qnaboard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TestRepositoryTest {
    @Autowired
    private TestRepository testRepository;

    @Test
    void testSpringDataJpaRepository() {
        TestEntity entity = new TestEntity("asdf");
        testRepository.save(entity);

        TestEntity findEntity = testRepository.findById(entity.getId()).get();
        assertThat(findEntity).isEqualTo(entity);
    }

    @Test
    void testQuerydsl() {
        TestEntity entity = new TestEntity("asdf");
        testRepository.save(entity);

        List<TestEntity> testEntities = testRepository.search();

        assertThat(testEntities).contains(entity);
    }
}
