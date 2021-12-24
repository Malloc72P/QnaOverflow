package scra.qnaboard.configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

/**
 * QueryDSL을 위한 빈을 등록하는 설정 클래스
 */
@Configuration
public class QuerydslConfiguration {

    /**
     * QueryDSL에서 사용할 JPAQueryFactory를 빈으로 등록하는 메서드
     *
     * @param em JPAQueryFactory는 EntityManager가 필요하다.
     * @return JpaQueryFactory
     */
    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }
}
