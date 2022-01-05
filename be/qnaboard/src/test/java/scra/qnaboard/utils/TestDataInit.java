package scra.qnaboard.utils;

import scra.qnaboard.domain.entity.*;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.entity.questiontag.QuestionTag;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDataInit {

    /**
     * 데이터 초기화
     *
     * @param em 초기화에 사용할 엔티티 매니저
     */
    public static TestDataDTO init(EntityManager em) {
        //1. 멤버 생성

        Member[] members = {
                new Member("member1", MemberRole.NORMAL),
                new Member("Admin1", MemberRole.ADMIN),
                new Member("Admin2", MemberRole.ADMIN),
                new Member("member2", MemberRole.NORMAL),
                new Member("member3", MemberRole.NORMAL),
                new Member("member4", MemberRole.NORMAL),
                new Member("member5", MemberRole.NORMAL),
                new Member("member6", MemberRole.NORMAL),
                new Member("member7", MemberRole.NORMAL),
        };
        Arrays.stream(members).forEach(em::persist);

        //2. 태그 생성
        Tag[] tags = {
                new Tag(members[1], "Angular", "description-Angular"),
                new Tag(members[1], "Web", "description-Web"),
                new Tag(members[1], "JQuery", "description-JQuery"),
                new Tag(members[2], "ReactJS", "description-ReactJS"),
                new Tag(members[2], "VueJS", "description-VueJS"),
                new Tag(members[1], "SpringBoot", "description-SpringBoot")
        };
        Arrays.stream(tags).forEach(em::persist);

        //3. 질문글 생성
        Question[] questions = {
                new Question(members[1], "target-content", "target-title"),
                new Question(members[1], "no-tag-no-answer", "title-2"),
                new Question(members[0], "content-3", "title-3"),
                new Question(members[3], "content-4", "title-4"),
                new Question(members[0], "content-5", "title-5"),
        };
        Arrays.stream(questions).forEach(em::persist);
        Question testTargetQuestion = questions[0];

        //4. 답변글 생성
        Answer[] answers = {
                new Answer(members[4], "content1", testTargetQuestion),
                new Answer(members[4], "content2", testTargetQuestion),
                new Answer(members[2], "content3", testTargetQuestion),
                new Answer(members[1], "content4", testTargetQuestion),
                new Answer(members[1], "content5", testTargetQuestion),
                new Answer(members[0], "content6", testTargetQuestion),
                new Answer(members[0], "content7", testTargetQuestion),
        };
        Arrays.stream(answers).forEach(em::persist);

        //5. 질문글에 태그 등록
        Arrays.stream(tags)
                .map(tag -> new QuestionTag(tag, testTargetQuestion))
                .forEach(em::persist);

        //6. 질문글에 대댓글 등록
        Comment c7 = createComment(em, members[0], testTargetQuestion, "content-7", null);
        Comment c8 = createComment(em, members[1], testTargetQuestion, "content-8", null);
        Comment c1 = createComment(em, members[1], testTargetQuestion, "content-1", null);
        Comment c2 = createComment(em, members[1], testTargetQuestion, "content-2", c1);
        Comment c3 = createComment(em, members[0], testTargetQuestion, "content-3", c2);
        Comment c4 = createComment(em, members[3], testTargetQuestion, "content-4", c3);
        Comment c5 = createComment(em, members[3], testTargetQuestion, "content-5", c4);
        Comment c6 = createComment(em, members[0], testTargetQuestion, "content-6", c4);

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
        Arrays.stream(answers).forEach(answer -> {
            Comment ac4 = createComment(em, members[0], answer, answer.getId() + "content-4", null);
            Comment ac5 = createComment(em, members[1], answer, answer.getId() + "content-5", null);
            Comment ac6 = createComment(em, members[2], answer, answer.getId() + "content-6", ac5);
            Comment ac1 = createComment(em, members[0], answer, answer.getId() + "content-1", null);
            Comment ac2 = createComment(em, members[1], answer, answer.getId() + "content-2", ac1);
            Comment ac3 = createComment(em, members[0], answer, answer.getId() + "content-3", ac1);
            commentList.add(ac1);
            commentList.add(ac2);
            commentList.add(ac3);
            commentList.add(ac4);
            commentList.add(ac5);
            commentList.add(ac6);
        });

        return new TestDataDTO(tags, questions, members, answers, commentList.toArray(Comment[]::new));
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
