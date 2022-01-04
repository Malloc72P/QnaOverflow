package scra.qnaboard.utils;

import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.post.Post;

import javax.persistence.EntityManager;

public class QueryUtils {
    public static int sizeOfAnswerByQuestionId(EntityManager em, long questionId) {
        return em.createQuery("select count(a) from Answer a where a.question.id = :id", Long.class)
                .setParameter("id", questionId).getSingleResult().intValue();
    }

    public static int sizeOfQuestionTagsByQuestionId(EntityManager em, long questionId) {
        return em.createQuery("select count(qt) from QuestionTag qt where qt.question.id = :id", Long.class)
                .setParameter("id", questionId).getSingleResult().intValue();
    }

    public static boolean isDeletedPost(EntityManager em, Post post) {
        //삭제되지 않은 엔티티가 0개인 경우 참, 1개면 거짓.
        return em.createQuery("select (count(p.id) = 0) from Post p where p.id = :id and p.deleted = false ",
                        Boolean.class)
                .setParameter("id", post.getId())
                .getSingleResult();
    }

    public static boolean isDeletedTag(EntityManager em, Tag tag) {
        //삭제되지 않은 엔티티가 0개인 경우 참, 1개면 거짓.
        return em.createQuery("select (count(t.id) = 0) from Tag t where t.id = :id and t.deleted = false ",
                        Boolean.class)
                .setParameter("id", tag.getId())
                .getSingleResult();
    }
}
