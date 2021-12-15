package scra.qnaboard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TestRepositoryTest {
    @Autowired
    private TestRepository testRepository;

    @Test
    void testSpringDataJpaRepository() {
        TestEntity entity = new TestEntity("asdf");
        testRepository.save(entity);

        Optional<TestEntity> findEntityOptional = testRepository.findById(entity.getId());
        TestEntity findEntity = findEntityOptional.orElse(null);

        assertThat(findEntity)
                .isNotNull()
                .isEqualTo(entity);
    }

    @Test
    void testQuerydsl() {
        TestEntity entity = new TestEntity("asdf");
        testRepository.save(entity);

        List<TestEntity> testEntities = testRepository.search();

        assertThat(testEntities).contains(entity);
    }
}
