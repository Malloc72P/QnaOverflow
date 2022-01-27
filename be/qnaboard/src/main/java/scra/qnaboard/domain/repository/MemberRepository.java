package scra.qnaboard.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import scra.qnaboard.domain.entity.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Page<Member> findAllByDeletedFalse(Pageable pageable);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByIdAndDeletedFalse(long memberId);

    @Modifying
    @Query("update Member m set m.deleted = true where m.id = :memberId")
    void deleteMemberById(@Param("memberId") long memberId);
}
