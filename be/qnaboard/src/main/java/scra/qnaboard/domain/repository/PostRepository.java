package scra.qnaboard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.post.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Transactional
    @Query("update Post p set p.score = p.score + 1 where p.id = :postId")
    void increaseScore(@Param("postId") long postId);

    @Modifying
    @Transactional
    @Query("update Post p set p.score = p.score - 1 where p.id = :postId")
    void decreaseScore(@Param("postId") long postId);
}
