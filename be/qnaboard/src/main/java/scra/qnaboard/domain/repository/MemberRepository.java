package scra.qnaboard.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import scra.qnaboard.domain.entity.member.Member;

import java.util.Optional;

/**
 * 회원 리포지토리
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 삭제되지 않은 모든 회원을 조회하는 메서드.
     * 생성일 기준으로 내림차순 정렬해서 반환한다
     * 파라미터로 받는 Pageable을 토대로 페이징 처리해서 일부분만 반환한다
     */
    Page<Member> findAllByDeletedFalseOrderByCreatedDateDesc(Pageable pageable);

    /**
     * 이메일로 회원 엔티티를 조회하고, 옵셔널로 감싸서 반환함
     */
    Optional<Member> findByEmail(String email);

    /**
     * 아이디로 삭제되지 않은 회원 엔티티를 조회하고 옵셔널로 감싸서 반환함
     */
    Optional<Member> findByIdAndDeletedFalse(long memberId);

    /**
     * 회원 아이디를 가지고 회원 엔티티를 삭제함
     */
    @Modifying
    @Query("update Member m set m.deleted = true where m.id = :memberId")
    void deleteMemberById(@Param("memberId") long memberId);
}
