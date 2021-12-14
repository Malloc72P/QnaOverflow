package scra.qnaboard;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static scra.qnaboard.QTestEntity.testEntity;

@RequiredArgsConstructor
public class TestRepositoryQuerydslImpl implements TestRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TestEntity> search() {
        return queryFactory.select(testEntity)
                .from(testEntity)
                .fetch();
    }
}
