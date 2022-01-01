package scra.qnaboard.utils;

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
}
