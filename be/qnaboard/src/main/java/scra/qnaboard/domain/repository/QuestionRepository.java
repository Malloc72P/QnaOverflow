package scra.qnaboard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scra.qnaboard.domain.entity.post.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
