package scra.qnaboard.utils;

import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class TestDataInit {

    /**
     * 데이터 초기화
     *
     * @param em 초기화에 사용할 엔티티 매니저
     */
    public static TestDataDTO init(EntityManager em) {
        //1. 멤버 생성
        List<Member> members = new ArrayList<>();
        members.add(new Member("member1", MemberRole.USER));
        members.add(new Member("Admin1", MemberRole.ADMIN));
        members.add(new Member("Admin2", MemberRole.ADMIN));
        members.add(new Member("member2", MemberRole.USER));
        members.add(new Member("member3", MemberRole.USER));
        members.add(new Member("member4", MemberRole.USER));
        members.add(new Member("member5", MemberRole.USER));
        members.add(new Member("member6", MemberRole.USER));
        members.add(new Member("member7", MemberRole.USER));

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
        questions.add(new Question(members.get(0), "content-6", "title-6"));
        questions.add(new Question(members.get(0), "content-7", "title-7"));
        questions.add(new Question(members.get(0), "content-8", "title-8"));
        questions.add(new Question(members.get(0), "content-9", "title-9"));
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

        return new TestDataDTO(tags, questions, members, answers, commentList);
    }

    private static Comment createComment(EntityManager em,
                                         Member author,
                                         Post post,
                                         String content,
                                         Comment parentComment) {
        Comment c1 = new Comment(author, content, post, parentComment);
        em.persist(c1);
        return c1;
    }

}
