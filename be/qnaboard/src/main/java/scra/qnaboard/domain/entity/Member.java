package scra.qnaboard.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "members_id")
    private Long id;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    public Member(String nickname, MemberRole role) {
        this.nickname = nickname;
        this.role = role;
    }

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
