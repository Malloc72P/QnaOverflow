package scra.qnaboard.configuration.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

/**
 * 로그인된 사용자 정보를 확인하려면 세션에서 꺼내봐야 알 수 있는데, 컨트롤러에서 이와 같은 코드가 계속 반복되는 일을 피하기 위해서
 * 해당 ArgumentResolver를 구현하였음. 이 ArgumentResolver를 적용하고 싶으면 @LoginUser를 컨트롤러 메서드의 파라미터에 붙이면 됨.
 * 붙이면 해당 ArgumentResolver가 세션에서 유저DTO를 꺼내서 애너테이션이 붙은 파라미터로 넘겨준다.
 */
@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    /**
     * 파라미터가 해당 ArgumentResolver를 지원하는지 확인하는 메서드.
     * LoginUser 애너테이션이 달려있고, 파라미터의 타입이 SessionUser가 맞다면, 지원하는 파라미터라고 판단한다
     *
     * @param parameter 지원여부를 검사할 대상 파라미터
     * @return 지원여부
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isUserClass;
    }

    /**
     * 컨트롤러 메서드의 파라미터에 아규먼트를 전달해주는 일을 처리함. 세션에서 유저DTO인 SessionUser를 꺼내고 반환하여
     * 아규먼트로 넘길 수 있게 도와준다.
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("user");

        if (sessionUser == null) {
            throw new NoSessionUserException();
        }

        return sessionUser;
    }
}
