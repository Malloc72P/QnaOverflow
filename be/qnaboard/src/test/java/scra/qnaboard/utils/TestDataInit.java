package scra.qnaboard.utils;

import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.MemberRole;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Question;

import javax.persistence.EntityManager;
import java.util.Arrays;

public class TestDataInit {

    /**
     * 데이터 초기화
     * @param em 초기화에 사용할 엔티티 매니저
     */
    public static Question[] init(EntityManager em) {
        Member author = new Member("member1", MemberRole.NORMAL);
        em.persist(author);

        Tag[] tags = {
                new Tag(author, "Angular"),
                new Tag(author, "Web"),
                new Tag(author, "JQuery"),
                new Tag(author, "ReactJS"),
                new Tag(author, "VueJS"),
                new Tag(author, "SpringBoot")
        };
        Arrays.stream(tags).forEach(em::persist);

        Question[] questions = {
                new Question(author, "target-content", "target-title"),
                new Question(author, "no-tag-no-answer", "title-2"),
                new Question(author, "content-3", "title-3"),
                new Question(author, "content-4", "title-4"),
                new Question(author, "content-5", "title-5"),
        };
        Arrays.stream(questions).forEach(em::persist);
        Question testTargetQuestion = questions[0];

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

        Arrays.stream(tags).forEach(testTargetQuestion::addTag);
        return questions;
    }

}
