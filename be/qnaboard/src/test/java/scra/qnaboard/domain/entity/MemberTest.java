package scra.qnaboard.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;

import javax.persistence.EntityManager;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    private EntityManager em;

    /**
     * 저장된 엔티티는 영속성 컨택스트를 초기화 한 이후에 식별자를 가지고 찾을 수 있어야 하니까 이를 찾는 코드도 추가함. 또한 Auditing 기능이 잘 적용되고 있는지도 테스트해야함
     */
    @Test
    @DisplayName("맴버 엔티티를 저장할 수 있어야함")
    void testSaveMember() {
        Member[] testcases = {
                new Member("member1","email", MemberRole.ADMIN),
                new Member("member2","email", MemberRole.USER),
        };

        Arrays.stream(testcases).forEach(em::persist);

        em.flush();
        em.clear();

        Arrays.stream(testcases)
                .forEach((testcase) -> {
                    Member findMember = em.find(Member.class, testcase.getId());
                    assertThat(findMember).isEqualTo(testcase);
                    assertThat(findMember.getCreatedDate()).isNotNull();
                    assertThat(findMember.getLastModifiedDate())
                            .isNotNull()
                            .isEqualTo(findMember.getCreatedDate());
                });
    }
}
