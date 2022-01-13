package scra.qnaboard.configuration.auth;

import lombok.Getter;
import scra.qnaboard.domain.entity.member.Member;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private Long id;
    private String name;
    private String email;

    public SessionUser(Member member) {
        this.id = member.getId();
        this.name = member.getNickname();
        this.email = member.getEmail();
    }
}
