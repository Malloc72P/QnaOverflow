package scra.qnaboard.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.utils.TestDataInit;
import scra.qnaboard.web.dto.question.detail.CommentDTO;
import scra.qnaboard.web.dto.question.detail.QuestionDetailDTO;

import javax.persistence.EntityManager;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QuestionSearchRepositoryTest {

    @Autowired
    private QuestionSearchRepository repository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("질문 상세보기를 할 수 있어야 함")
    void questionDetail() {
        Question[] questions = TestDataInit.init(em).getQuestions();

        for (Question question : questions) {
            Question findQuestion = repository.questionDetail(question.getId()).orElse(null);
            testFoundEntityIsEqualToExpected(question, findQuestion);
        }
    }

    @Test
    @DisplayName("질문 상세보기를 v2메서드로 할 수 있어야 함")
    void questionDetailV2() {
        Question[] questions = TestDataInit.init(em).getQuestions();

        for (Question question : questions) {
            QuestionDetailDTO detailDTO = repository.questionDetailV2(question.getId());
            testDetailDTO(detailDTO, question);
        }
    }

    /**
     * 예상되는 값을 가지고 엔티티를 테스트.
     *
     * @param question 예상되는 엔티티의 상태값
     */
    private void testFoundEntityIsEqualToExpected(Question question, Question findQuestion) {
        //찾은 question 엔티티는 null 이면 안됨
        assertThat(findQuestion).isNotNull();

        //태그와 답변글 개수가 같아야 함
        assertThat(findQuestion.getQuestionTags().size()).isEqualTo(question.getQuestionTags().size());
        assertThat(findQuestion.getAnswers().size()).isEqualTo(question.getAnswers().size());

        //제목, 내용, 작성자가 같아야 함
        assertThat(findQuestion)
                .extracting(
                        Question::getTitle,
                        Question::getContent,
                        Question::getAuthor
                ).containsExactly(
                        question.getTitle(),
                        question.getContent(),
                        question.getAuthor()
                );

        //태그와 답변글 개수가 같아야 함
        assertThat(findQuestion.getQuestionTags().size()).isEqualTo(question.getQuestionTags().size());
        assertThat(findQuestion.getAnswers().size()).isEqualTo(question.getAnswers().size());
    }

    /**
     * DTO가 엔티티의 값을 잘 가지고 있는지 테스트함
     */
    private void testDetailDTO(QuestionDetailDTO detailDTO, Question question) {
        assertThat(detailDTO).extracting(
                QuestionDetailDTO::getQuestionId,
                QuestionDetailDTO::getTitle,
                QuestionDetailDTO::getContent,
                QuestionDetailDTO::getVoteScore,
                QuestionDetailDTO::getViewCount,
                QuestionDetailDTO::getAuthorName
        ).containsExactly(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getUpVoteCount() - question.getDownVoteCount(),
                question.getViewCount(),
                question.getAuthor().getNickname()
        );

        assertThat(detailDTO.getTags().size()).isEqualTo(question.getQuestionTags().size());
        assertThat(detailDTO.getAnswers().size()).isEqualTo(question.getAnswers().size());
    }

    @Test
    @DisplayName("코멘트의 계층구조를 조립해서 가지고 올 수 있어야 함")
    void commentsByParentPostId() {
        Question[] questions = TestDataInit.init(em).getQuestions();

        for (Question question : questions) {
            //질문 상세조회
            QuestionDetailDTO detailDTO = repository.questionDetailV2(question.getId());

            //대댓글 전체  테스트
            testComments(commentsByParentPostId(question.getId()), detailDTO.getComments());

            //게시글에 딸려있는 답변게시글도 테스트
            detailDTO.getAnswers()
                    .forEach(answerDTO -> {
                        List<Comment> comments = commentsByParentPostId(answerDTO.getAnswerId());
                        List<CommentDTO> commentDTOS = answerDTO.getComments();
                        testComments(comments, commentDTOS);
                    });
        }
    }

    /**
     * 질문상세보기 DTO 안에 있는 댓글에 대한 테스트 메서드. <br>
     * 최상위 댓글 및 그 하위에 있는 모든 댓글을 테스트함
     *
     * @param comments    댓글 엔티티
     * @param commentDTOS 댓글 DTO
     */
    private void testComments(List<Comment> comments, List<CommentDTO> commentDTOS) {
        //최상위에 있는 댓글의 개수와 순서를 비교
        compareCommentAndDTO(comments, commentDTOS);

        //최상위 댓글의 아래에 있는 댓글 비교
        testCommentChildren(commentDTOS);
    }

    /**
     * 넓이 우선탐색을 하면서 자식 댓글의 순서와 개수를 비교
     *
     * @param commentDTOS 테스트 대상 질문상세보기 DTO(안에 댓글있음)
     */
    private void testCommentChildren(List<CommentDTO> commentDTOS) {
        Queue<CommentDTO> queue = new ArrayDeque<>(commentDTOS);
        while (!queue.isEmpty()) {
            CommentDTO poll = queue.poll();
            List<CommentDTO> dtoChildren = poll.getChildren();
            queue.addAll(poll.getChildren());

            List<Comment> comments = commentsByParentCommentId(poll.getCommentId());
            compareCommentAndDTO(comments, dtoChildren);
        }
    }

    /**
     * 댓글 엔티티와 DTO List를 비교함(개수 + 내용물)
     *
     * @param comments    엔티티 list
     * @param dtoComments dto list
     */
    private void compareCommentAndDTO(List<Comment> comments, List<CommentDTO> dtoComments) {
        assertThat(comments.size()).isEqualTo(dtoComments.size());
        for (int i = 0; i < comments.size(); i++) {
            Comment expected = comments.get(i);
            CommentDTO actual = dtoComments.get(i);
            assertThat(actual.getCommentId()).isEqualTo(expected.getId());
        }
    }

    /**
     * 게시글의 최상위 댓글을 생성일로 오름차순 정렬해서 가져옴
     *
     * @param parentPostId 게시글 아이디
     * @return 댓글 목록
     */
    private List<Comment> commentsByParentPostId(long parentPostId) {
        return em.createQuery(
                        "select c from Comment c " +
                                "where c.parentPost.id = :parentPostId and c.parentComment.id is null " +
                                "order by c.createdDate",
                        Comment.class)
                .setParameter("parentPostId", parentPostId)
                .getResultList();
    }

    /**
     * 댓글의 자식 댓글을 생성일로 오름차순 정렬해서 가져옴
     *
     * @param parentCommentId 부모댓글의 아이디
     * @return 댓글 목록
     */
    private List<Comment> commentsByParentCommentId(long parentCommentId) {
        return em.createQuery(
                        "select c from Comment c " +
                                "where c.parentComment.id = :parentCommentId " +
                                "order by c.createdDate",
                        Comment.class)
                .setParameter("parentCommentId", parentCommentId)
                .getResultList();
    }

}
