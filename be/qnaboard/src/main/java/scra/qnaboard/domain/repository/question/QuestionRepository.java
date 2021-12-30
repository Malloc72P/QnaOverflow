package scra.qnaboard.domain.repository.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.post.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    /**
     * soft delete를 위한 메서드. 반드시 어걸로만 지워야 함
     * @param questionId 삭제할 질문게시글의 아이디
     */
    @Modifying
    @Transactional
    @Query("update Question q set q.deleted = true where q.id = :questionId")
    void deleteById(@Param("questionId") Long questionId);
}
