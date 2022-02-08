package scra.qnaboard.configuration.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.repository.MemberRepository;

import javax.servlet.http.HttpSession;
import java.util.Collections;

/**
 * OAUTH 로그인을 위한 유저 서비스
 */
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    /**
     * 소셜 로그인 이후 가져온 사용자의 정보를 가지고 회원가입, 회원정보 수정, 세션등록 등의 기능을 처리함
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        //registrationId는 서비스에 대한 ID임.(구글인지 네이버인지를 구분할 때 사용함)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        //userNameAttributeName은 OAuth2 로그인을 할 때 키가 되는 필드값임. 네이버와 구글 로그인 동시지원을 위해 사용함
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        //attributes는 OAuth2UserService를 통해 가져온 OAuth2User의 애트리뷰트를 담을 객체임
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        //회원등록 및 수정로직을 처리함
        Member member = saveOrUpdate(attributes);
        //세션에 저장할 유저DTO를 생성해서 세션에 저장함
        httpSession.setAttribute("user", new SessionUser(member));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    /**
     * 이메일로 사용자를 찾고, 있다면 새로운 정보로 업데이트함. 못찾으면 새로 생성함
     *
     * @param attributes 사용자 정보를 담고 있는 객체
     * @return 사용자 엔티티
     */
    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName()))
                .map(Member::activate)
                .orElse(attributes.toMember());

        return memberRepository.save(member);
    }
}
