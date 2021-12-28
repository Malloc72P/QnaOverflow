package scra.qnaboard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scra.qnaboard.domain.entity.post.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
