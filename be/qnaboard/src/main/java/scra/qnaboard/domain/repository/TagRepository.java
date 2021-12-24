package scra.qnaboard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scra.qnaboard.domain.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
