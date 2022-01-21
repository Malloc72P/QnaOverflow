package scra.qnaboard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scra.qnaboard.domain.entity.post.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
