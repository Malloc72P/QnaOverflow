package scra.qnaboard.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @WebMvcTest를 하기 위해 추가한 설정 클래스.
 * EnableJpaAuditing을 사용하려면 최소 하나의 @Entity 클래스가 필요하다.
 * 그런데 @WebMvcTest는 모든 컴포넌트를 준비하지 않고, Web관련 컴포넌트만 준비한다.
 * WebMvcTest를 수행하는데, QnaboardApplication에 @EnableJpaAuditing을 붙여놓았다면 위에서 말한 조건이 충족되지 않아서
 * 에러가 발생한다. 이러한 에러를 피하기 위해 QnaboardApplication 클래스로부터 @EnableJpaAuditing를 분리해야 했고,
 * 이로인해 만든 클래스가 바로 여기있는 JpaConfig이다.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
