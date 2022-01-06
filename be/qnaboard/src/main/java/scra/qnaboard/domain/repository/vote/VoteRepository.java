package scra.qnaboard.domain.repository.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import scra.qnaboard.domain.entity.vote.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
