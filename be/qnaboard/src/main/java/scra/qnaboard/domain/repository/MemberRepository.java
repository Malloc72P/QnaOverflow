package scra.qnaboard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scra.qnaboard.domain.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
