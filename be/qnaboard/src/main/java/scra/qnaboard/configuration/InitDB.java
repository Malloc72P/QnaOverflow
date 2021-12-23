package scra.qnaboard.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.MemberRole;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.QuestionRepository;

import javax.annotation.PostConstruct;

@Slf4j
@Profile("local")
@Configuration
@RequiredArgsConstructor
public class InitDB {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;

    @PostConstruct
    public void initDB() {
        log.info("데이터베이스 초기화 시작");
        Member member1 = new Member("member1", MemberRole.NORMAL);
        memberRepository.save(member1);
        memberRepository.save(new Member("member2", MemberRole.NORMAL));
        memberRepository.save(new Member("member3", MemberRole.NORMAL));

        for (int i = 0; i < 10; i++) {
            questionRepository.save(new Question(member1, "content" + i, "title" + i));
        }
        log.info("데이터베이스 초기화 완료");
    }
}
