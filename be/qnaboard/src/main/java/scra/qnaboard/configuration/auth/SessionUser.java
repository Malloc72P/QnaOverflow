package scra.qnaboard.configuration.auth;

import lombok.Getter;
import scra.qnaboard.domain.entity.member.Member;

import java.io.Serializable;

/**
 * 세션에 유저 정보를 저장하기 위한 DTO.
 * 엔티티를 세션에 저장하지 않기 위해 만든 DTO이다.
 * 세션에 저장하려면 직렬화를 구현해야 한다. 하지만 엔티티는 직렬화를 구현하지 않는 편이 좋다.
 * 엔티티는 다른 엔티티와 관계를 형성할 수 있다. 그런데, 엔티티의 직렬화를 구현하게 되면 직렬화 대상에 자식엔티티까지 포함될 확률이 높다.
 * 이렇게 되면 성능이슈나 부수효과가 발생활 확률이 높아진다.
 * 그래서 직렬화 기능을 가진 세션DTO를 만들어서 사용하게 되었다.
 */
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

    public SessionUser(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
