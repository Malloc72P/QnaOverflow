package scra.qnaboard.configuration.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import scra.qnaboard.domain.entity.member.MemberRole;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOauth2UserService customOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(
                        "/",
                        "/css/**",
                        "/js/**",
                        "/questions",
                        "/tags",
                        "/tags/form",
                        "/tags/{tagId}/edit-form",
                        "/api/tags",
                        "/questions/{questionId}",
                        "/questions/{questionId}/form",
                        "/questions/form",
                        "/notify"
                ).permitAll()
                .antMatchers(HttpMethod.POST,
                        "/questions",
                        "/questions/{questionId}/edit",
                        "/questions/{questionId}/delete"
                ).hasAnyRole(MemberRole.ADMIN.name(), MemberRole.USER.name())
                .anyRequest().authenticated()
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .oauth2Login().userInfoEndpoint().userService(customOauth2UserService);
    }
}
