package scra.qnaboard.domain.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import scra.qnaboard.domain.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
