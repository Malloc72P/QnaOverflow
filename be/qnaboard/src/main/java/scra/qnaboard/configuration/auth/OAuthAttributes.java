package scra.qnaboard.configuration.auth;

import lombok.Builder;
import lombok.Getter;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;

import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        switch (registrationId) {
            case "google":
                return ofGoogle(userNameAttributeName, attributes);
            case "naver":
                return ofNaver("id", attributes);
            default:
                throw new IllegalStateException("지원하지 않는 로그인 유형이 감지됨");
        }
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /**
     * 네이버 로그인은 userNameAttributeName이 id임에 주의할 것 <br>
     * 또한 네이버에서 응답해주는 json은 response객체 안에 필요한 데이터가 들어있다 <br>
     * 그래서 attributes 대신, 그 안에 있는 response를 사용해야 한다 <br>
     * .attributes(response) 이 부분에서 attribute가 아니라 response임에 주의할 것
     */
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toMember() {
        return new Member(name, email, MemberRole.USER);
    }
}
