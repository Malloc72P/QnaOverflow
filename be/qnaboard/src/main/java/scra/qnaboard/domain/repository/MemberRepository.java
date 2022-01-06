package scra.qnaboard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scra.qnaboard.domain.entity.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
