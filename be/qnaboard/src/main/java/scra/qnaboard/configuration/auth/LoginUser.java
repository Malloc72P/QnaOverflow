package scra.qnaboard.configuration.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * LoginUserArgumentResolver를 통해 세션에서 유저DTO를 꺼내오기 위해 사용하는 애너테이션.
 * 컨트롤러 메서드의 파라미터 앞에 이 애너테이션을 붙이면 LoginUserArgumentResolver가 작동한다
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
}
