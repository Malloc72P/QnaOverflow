package scra.qnaboard.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import scra.qnaboard.configuration.auth.LoginUserArgumentResolver;

import java.util.List;

/**
 * 따로 만든 ArgumentResolver를 등록하는 설정클래스
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    //@Component가 붙어있어서 빈으로 등록된 상태이다. 따라서 의존성주입이 가능하다.
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    /**
     * 따로 만든 ArgumentResolver를 등록한다.
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }
}
