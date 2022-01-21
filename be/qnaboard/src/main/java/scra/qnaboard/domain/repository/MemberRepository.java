package scra.qnaboard.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import scra.qnaboard.domain.entity.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByDeletedFalse();

    Optional<Member> findByEmail(String email);

    Optional<Member> findByIdAndDeletedFalse(long memberId);

    @Modifying
    @Query("update Member m set m.deleted = true where m.id = :memberId")
    void deleteMemberById(@Param("memberId") long memberId);
}