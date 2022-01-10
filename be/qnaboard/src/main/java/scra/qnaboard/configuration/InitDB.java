package scra.qnaboard.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;
import scra.qnaboard.domain.entity.vote.Vote;
import scra.qnaboard.domain.entity.vote.VoteType;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

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
            List<Member> members = new ArrayList<>();
            members.add(new Member("member1", MemberRole.NORMAL));
            members.add(new Member("Admin1", MemberRole.ADMIN));
            members.add(new Member("Admin2", MemberRole.ADMIN));
            members.add(new Member("member2", MemberRole.NORMAL));
            members.add(new Member("member3", MemberRole.NORMAL));
            members.add(new Member("member4", MemberRole.NORMAL));
            members.add(new Member("member5", MemberRole.NORMAL));
            members.add(new Member("member6", MemberRole.NORMAL));
            members.add(new Member("member7", MemberRole.NORMAL));

            members.forEach(em::persist);

            //2. 태그 생성
            List<Tag> tags = new ArrayList<>();
            tags.add(new Tag(members.get(1), "Angular", "description-Angular"));
            tags.add(new Tag(members.get(1), "Web", "description-Web"));
            tags.add(new Tag(members.get(1), "JQuery", "description-JQuery"));
            tags.add(new Tag(members.get(2), "ReactJS", "description-ReactJS"));
            tags.add(new Tag(members.get(2), "VueJS", "description-VueJS"));
            tags.add(new Tag(members.get(1), "SpringBoot", "description-SpringBoot"));
            tags.forEach(em::persist);

            //3. 질문글 생성
            List<Question> questions = new ArrayList<>();
            questions.add(new Question(members.get(1), "target-content", "target-title"));
            questions.add(new Question(members.get(1), "no-tag-no-answer", "title-2"));
            questions.add(new Question(members.get(0), "content-3", "title-3"));
            questions.add(new Question(members.get(3), "content-4", "title-4"));
            questions.add(new Question(members.get(0), "content-5", "title-5"));
            questions.forEach(em::persist);
            Question testTargetQuestion = questions.get(0);

            //4. 답변글 생성
            List<Answer> answers = new ArrayList<>();
            answers.add(new Answer(members.get(4), "content1", testTargetQuestion));
            answers.add(new Answer(members.get(4), "content2", testTargetQuestion));
            answers.add(new Answer(members.get(2), "content3", testTargetQuestion));
            answers.add(new Answer(members.get(1), "content4", testTargetQuestion));
            answers.add(new Answer(members.get(1), "content5", testTargetQuestion));
            answers.add(new Answer(members.get(0), "content6", testTargetQuestion));
            answers.add(new Answer(members.get(0), "content7", testTargetQuestion));
            answers.forEach(em::persist);

            Answer testTargetAnswer = answers.get(0);

            //5. 질문글에 태그 등록
            tags.stream()
                    .map(tag -> new QuestionTag(tag, testTargetQuestion))
                    .forEach(em::persist);

            //6. 질문글에 대댓글 등록
            Comment c7 = createComment(em, members.get(0), testTargetQuestion, "content-7", null);
            Comment c8 = createComment(em, members.get(1), testTargetQuestion, "content-8", null);
            Comment c1 = createComment(em, members.get(1), testTargetQuestion, "content-1", null);
            Comment c2 = createComment(em, members.get(1), testTargetQuestion, "content-2", c1);
            Comment c3 = createComment(em, members.get(0), testTargetQuestion, "content-3", c2);
            Comment c4 = createComment(em, members.get(3), testTargetQuestion, "content-4", c3);
            Comment c5 = createComment(em, members.get(3), testTargetQuestion, "content-5", c4);
            Comment c6 = createComment(em, members.get(0), testTargetQuestion, "content-6", c4);

            List<Comment> commentList = new ArrayList<>();
            commentList.add(c1);
            commentList.add(c2);
            commentList.add(c3);
            commentList.add(c4);
            commentList.add(c5);
            commentList.add(c6);
            commentList.add(c7);
            commentList.add(c8);

            //7. 답변글에 대댓글 등록
            answers.forEach(answer -> {
                Comment ac4 = createComment(em, members.get(0), answer, answer.getId() + "content-4", null);
                Comment ac5 = createComment(em, members.get(1), answer, answer.getId() + "content-5", null);
                Comment ac6 = createComment(em, members.get(2), answer, answer.getId() + "content-6", ac5);
                Comment ac1 = createComment(em, members.get(0), answer, answer.getId() + "content-1", null);
                Comment ac2 = createComment(em, members.get(1), answer, answer.getId() + "content-2", ac1);
                Comment ac3 = createComment(em, members.get(0), answer, answer.getId() + "content-3", ac1);
                commentList.add(ac1);
                commentList.add(ac2);
                commentList.add(ac3);
                commentList.add(ac4);
                commentList.add(ac5);
                commentList.add(ac6);
            });

            members.stream()
                    .map(member -> new Vote(member, testTargetQuestion, VoteType.UP))
                    .peek(em::persist)
                    .forEach(vote -> testTargetQuestion.increaseScore());

            members.stream()
                    .map(member -> new Vote(member, testTargetAnswer, VoteType.UP))
                    .peek(em::persist)
                    .forEach(vote -> testTargetAnswer.increaseScore());

            em.persist(new Vote(members.get(1), testTargetQuestion, VoteType.DOWN));
            testTargetQuestion.decreaseScore();
            em.persist(new Vote(members.get(2), testTargetQuestion, VoteType.DOWN));
            testTargetQuestion.decreaseScore();
            em.persist(new Vote(members.get(3), testTargetQuestion, VoteType.DOWN));
            testTargetQuestion.decreaseScore();
            em.persist(new Vote(members.get(4), testTargetQuestion, VoteType.DOWN));
            testTargetQuestion.decreaseScore();

            log.info("데이터베이스 초기화 완료");
        }
    }
}
