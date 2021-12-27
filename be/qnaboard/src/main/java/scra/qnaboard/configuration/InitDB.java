package scra.qnaboard.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.MemberRole;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.QuestionRepository;
import scra.qnaboard.domain.repository.TagRepository;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * 로컬에서 개발할 때 데이터를 초기화 하기 위한 클래스.
 */
@Slf4j
@Profile("local")
@Configuration
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    @Transactional
    public void init() {
        initService.initDB();
    }

    /**
     * PostConstruct메서드에서 바로 초기화하면 더티체킹이 안되는 것 같음 <br>
     * QuestionTag를 CascadeType.ALL로 설정했는데도 자꾸 저장이 안되서 내부클래스로 옮기니까 됨
     */
    @Slf4j
    @Profile("local")
    @Configuration
    @RequiredArgsConstructor
    static class InitService {

        private final MemberRepository memberRepository;
        private final QuestionRepository questionRepository;
        private final TagRepository tagRepository;

        @Transactional
        public void initDB() {
            log.info("데이터베이스 초기화 시작");
            Member[] members = {
                    new Member("member1", MemberRole.NORMAL),
                    new Member("member2", MemberRole.ADMIN),
                    new Member("member3", MemberRole.ADMIN),
                    new Member("member4", MemberRole.ADMIN)
            };
            memberRepository.saveAll(Arrays.asList(members));

            Tag[] tags = {
                    new Tag(members[2], "Angular"),
                    new Tag(members[1], "Web"),
                    new Tag(members[1], "JQuery"),
                    new Tag(members[3], "ReactJS"),
                    new Tag(members[3], "VueJS"),
                    new Tag(members[2], "SpringBoot")
            };
            tagRepository.saveAll(Arrays.asList(tags));

            Question[] questions = {
                    new Question(members[0], "content-1", "title-1"),
                    new Question(members[1], "content-2", "title-2aaaaaaaaaaaaaaaaaa2aaaaaaaaaaaaaaaaaa2aaaaaaaaaaaaaaaaaa2aaaaaaaaaaaaaaaaaa2aaaaaaaaaaaaaaaaaa2aaaaaaaaaaaaaaaaaa2aaaaaaaaaaaaaaaaaa2aaaaaaaaaaaaaaaaaa2aaaaaaaaaaaaaaaaaa2aaaaaaaaaaaaaaaaaa2aaaaaaaaaaaaaaaaaa2aaaaaaaaaaaaaaaaaa"),
                    new Question(members[1], "content-3", "title-3"),
                    new Question(members[2], "content-4", "title-4"),
                    new Question(members[2], "content-5", "title-5"),
                    new Question(members[2], "content-6", "title-6"),
                    new Question(members[3], "content-7", "title-7"),
                    new Question(members[3], "content-8", "title-8"),
                    new Question(members[0], "content-9", "title-9"),
                    new Question(members[0], "content-10", "title-10")
            };
            questionRepository.saveAll(Arrays.asList(questions));

            Arrays.stream(questions)
                    .filter(question -> question.getId() >= 4)
                    .forEach(question -> Arrays.asList(tags).forEach(question::addTag));
            questions[0].addTag(tags[0]);
            questions[0].addTag(tags[1]);
            questions[0].addTag(tags[2]);

            questions[1].addTag(tags[3]);
            questions[1].addTag(tags[4]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);
            questions[1].addTag(tags[5]);

            log.info("데이터베이스 초기화 완료");
        }
    }
}
