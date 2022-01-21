package scra.qnaboard.domain.repository.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.post.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    /**
     * soft delete를 위한 메서드. 반드시 어걸로만 지워야 함
     *
     * @param answerId 삭제할 답변게시글의 아이디
     */
    @Modifying
    @Transactional
    @Query("update Answer a set a.deleted = true where a.id = :answerId")
    void deleteById(@Param("answerId") Long answerId);

}
