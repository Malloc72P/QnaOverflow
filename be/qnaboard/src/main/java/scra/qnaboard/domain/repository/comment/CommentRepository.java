package scra.qnaboard.domain.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
