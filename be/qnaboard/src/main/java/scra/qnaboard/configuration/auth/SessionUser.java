package scra.qnaboard.configuration.auth;

import lombok.Getter;
import scra.qnaboard.domain.entity.member.Member;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private String name;
    private String email;

    public SessionUser(Member member) {
        this.name = member.getNickname();
        this.email = member.getEmail();
    }
}
