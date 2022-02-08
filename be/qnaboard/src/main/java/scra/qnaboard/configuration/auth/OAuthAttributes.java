package scra.qnaboard.configuration.auth;

import lombok.Builder;
import lombok.Getter;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;

import java.util.Map;

/**
 * OAuthAttributes를 Map이 아닌 DTO로 처리하기 위해 만든 DTO이다.
 * 회원정보 생성에 필요한 필수값을 name, email등의 필드에 저장한다.
 * 혹시 모르니 원본에 해당하는 Map을 attributes라는 필드에 저장한다
 */
@Getter
@Builder
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;

    /**
     * OAuthAttributes가 저장된 Map을 DTO로 변환하는 메서드.
     * 로그인 서비스 제공자(구글,네이버)에 따라 다른 방식으로 객체를 생성하여 반환함.
     * @param registrationId 로그인 서비스 제공자를 구분하는 값
     * @param userNameAttributeName DefaultOAuth2User의 nameAttributeKey를 위한 값
     * @param attributes Map 자료구조로 되어있는 OAuth 애트리뷰트
     * @return OAuthAttribute DTO
     */
    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        //registrationId에 따라 알맞은 메서드를 호출해서 DTO를 반환함
        switch (registrationId) {
            case "google":
                return ofGoogle(userNameAttributeName, attributes);
            case "naver":
                return ofNaver("id", attributes);
            default:
                throw new IllegalStateException("지원하지 않는 로그인 유형이 감지됨");
        }
    }

    /**
     * 소셜로그인 서비스 제공자가 구글인 경우에 OAuthAttributes 객체를 생성해서 반환하는 메서드
     */
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /**
     * 소셜로그인 서비스 제공자가 네이버인 경우에 OAuthAttributes 객체를 생성해서 반환하는 메서드
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

    /**
     * attributes를 가지고 회원 엔티티를 생성해서 반환하는 메서드
     * @return 회원 엔티티
     */
    public Member toMember() {
        return new Member(name, email, MemberRole.USER);
    }
}
