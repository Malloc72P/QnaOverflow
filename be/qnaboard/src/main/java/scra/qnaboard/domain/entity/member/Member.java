package scra.qnaboard.domain.entity.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import scra.qnaboard.domain.entity.BaseTimeEntity;

import javax.persistence.*;
import java.util.Objects;

/**
 * 회원에 대한 Member 엔티티.
 */
@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    private static final String NO_EMAIL = "no-email";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "members_id")
    private Long id;

    private String nickname;

    private String email;

    private boolean deleted = false;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder
    public Member(String nickname, String email, MemberRole role) {
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }

    /**
     * 회원정보를 수정하는 메서드
     */
    public Member update(String nickname) {
        this.nickname = nickname;
        return this;
    }

    /**
     * 회원을 다시 활성화 상태로 바꾸는 메서드
     */
    public Member activate() {
        this.deleted = false;
        return this;
    }

    /**
     * 관리자인지 여부를 반환함
     */
    public boolean isAdmin() {
        return role.equals(MemberRole.ADMIN);
    }

    /**
     * 관리자가 아닌지에 대한 여부를 반환함
     */
    public boolean isNotAdmin() {
        return !role.equals(MemberRole.ADMIN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(getId(), member.getId()) &&
                Objects.equals(getNickname(), member.getNickname()) &&
                getRole() == member.getRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNickname(), getRole());
    }
}
