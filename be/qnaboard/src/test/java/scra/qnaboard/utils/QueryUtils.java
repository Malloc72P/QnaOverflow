package scra.qnaboard.utils;

import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.post.Post;

import javax.persistence.EntityManager;
import java.util.List;

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

    public static Tag tagById(EntityManager em, long tagId) {
        return em.createQuery("select t from Tag t join fetch t.author where t.id = :id", Tag.class)
                .setParameter("id", tagId)
                .getSingleResult();
    }

    public static List<Tag> tagByNameLike(EntityManager em, String keyword) {
        return em.createQuery("select t from Tag t join fetch t.author where t.name like :keyword", Tag.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }
}
