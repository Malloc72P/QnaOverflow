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

    public Member update(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public Member activate() {
        this.deleted = false;
        return this;
    }

    public boolean isAdmin() {
        return role.equals(MemberRole.ADMIN);
    }

    public boolean isNotAdmin() {
        return !role.equals(MemberRole.ADMIN);
    }

    public boolean isNotSame(Member another) {
        return !id.equals(another.id);
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
