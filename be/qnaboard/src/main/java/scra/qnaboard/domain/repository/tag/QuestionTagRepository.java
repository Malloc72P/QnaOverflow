package scra.qnaboard.domain.repository.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;

public interface QuestionTagRepository extends JpaRepository<QuestionTag, Long> {
}
