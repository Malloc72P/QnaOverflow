package scra.qnaboard;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity, Long>, TestRepositoryQuerydsl {
}
