package scra.qnaboard.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.*;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
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

        private final EntityManager em;

        private static Comment createComment(EntityManager em,
                                             Member author,
                                             Post post,
                                             String content,
                                             Comment parentComment) {
            Comment c1 = new Comment(author, content, post, parentComment);
            em.persist(c1);
            return c1;
        }

        @Transactional
        public void initDB() {
            log.info("데이터베이스 초기화 시작");

            //1. 멤버 생성
            Member author = new Member("member1", MemberRole.NORMAL);
            em.persist(author);

            //2. 태그 생성
            Tag[] tags = {
                    new Tag(author, "Angular", "구글에서 만든 웹 프론트엔드 프레임워크인 Angular"),
                    new Tag(author, "Web", "웹에 관련된 질문은 해당 태그를 사용해주세요"),
                    new Tag(author, "JQuery", "자바스크립트를 좀 더 편하게 사용할 수 있게 도와주는 라이브러리입니다"),
                    new Tag(author, "ReactJS", "페이스북에서 만든 웹 프론트엔드 프레임워크입니다. SPA를 보다 쉽게 만들 수 있도록 도와줍니다"),
                    new Tag(author, "VueJS", "프로그레시브 자바스크립트 프레임워크입니다. 접근하기 쉽고 높은 성능을 자랑합니다"),
                    new Tag(author, "SpringBoot", "스프링 프레임워크를 보다 더 쉽게 사용할 수 있게 만든 프레임워크입니다")
            };
            Arrays.stream(tags).forEach(em::persist);

            //3. 질문글 생성
            Question[] questions = {
                    new Question(author, "target-content", "target-title"),
                    new Question(author, "no-tag-no-answer", "title-2"),
                    new Question(author, "content-3", "title-3"),
                    new Question(author, "content-4", "title-4"),
                    new Question(author, "content-5", "title-5"),
            };
            Arrays.stream(questions).forEach(em::persist);
            Question testTargetQuestion = questions[0];

            //4. 답변글 생성
            Answer[] answers = {
                    new Answer(author, "content1", testTargetQuestion),
                    new Answer(author, "content2", testTargetQuestion),
                    new Answer(author, "content3", testTargetQuestion),
                    new Answer(author, "content4", testTargetQuestion),
                    new Answer(author, "content5", testTargetQuestion),
                    new Answer(author, "content6", testTargetQuestion),
                    new Answer(author, "content7", testTargetQuestion),
            };
            Arrays.stream(answers).forEach(em::persist);

            //5. 질문글에 태그 등록
            Arrays.stream(tags)
                    .map(tag -> new QuestionTag(tag, testTargetQuestion))
                    .forEach(em::persist);

            //6. 질문글에 대댓글 등록
            Comment c7 = createComment(em, author, testTargetQuestion, "q-content-7", null);
            Comment c8 = createComment(em, author, testTargetQuestion, "q-content-8", null);
            Comment c1 = createComment(em, author, testTargetQuestion, "q-content-1", null);
            Comment c2 = createComment(em, author, testTargetQuestion, "q-content-2", c1);
            Comment c3 = createComment(em, author, testTargetQuestion, "q-content-3", c2);
            Comment c4 = createComment(em, author, testTargetQuestion, "q-content-4", c3);
            Comment c5 = createComment(em, author, testTargetQuestion, "q-content-5", c4);
            Comment c6 = createComment(em, author, testTargetQuestion, "q-content-6", c4);

            //7. 답변글에 대댓글 등록
            Arrays.stream(answers).forEach(answer -> {
                Comment ac4 = createComment(em, author, answer, answer.getId() + "content-4", null);
                Comment ac5 = createComment(em, author, answer, answer.getId() + "content-5", null);
                Comment ac6 = createComment(em, author, answer, answer.getId() + "content-6", ac5);
                Comment ac1 = createComment(em, author, answer, answer.getId() + "content-1", null);
                Comment ac2 = createComment(em, author, answer, answer.getId() + "content-2", ac1);
                Comment ac3 = createComment(em, author, answer, answer.getId() + "content-3", ac1);
            });

            log.info("데이터베이스 초기화 완료");
        }
    }
}
