package scra.qnaboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.service.exception.question.delete.QuestionDeleteFailedException;
import scra.qnaboard.service.exception.question.edit.QuestionEditFailedException;
import scra.qnaboard.service.exception.question.edit.QuestionPropertyIsEmptyException;
import scra.qnaboard.service.exception.question.edit.UnauthorizedQuestionEditException;
import scra.qnaboard.utils.EntityConverter;
import scra.qnaboard.utils.QueryUtils;
import scra.qnaboard.utils.TestDataDTO;
import scra.qnaboard.utils.TestDataInit;
import scra.qnaboard.web.dto.question.detail.QuestionDetailDTO;
import scra.qnaboard.web.dto.question.detail.QuestionDetailDTOTestUtil;
import scra.qnaboard.web.dto.question.list.QuestionListDTO;
import scra.qnaboard.web.dto.question.search.ParsedSearchQuestionDTO;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static scra.qnaboard.utils.QueryUtils.isDeletedPost;

@SpringBootTest
@Transactional
class QuestionServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private VoteService voteService;

    @Test
    @DisplayName("검색 기능 테스트")
    void testSearchQuestion() {
        testTemplate(
                (question, testDataDTO) -> {
                    ParsedSearchQuestionDTO searchQuestionDTO = new ParsedSearchQuestionDTO();
                    searchQuestionDTO.setAnswers(3);
                    searchQuestionDTO.setScore(3);
                    searchQuestionDTO.setTitle("target");
                    searchQuestionDTO.setAuthorId(2);
                    searchQuestionDTO.addTag("Angular");
                    searchQuestionDTO.addTag("Web");
                    QuestionListDTO questionListDTO = questionService.searchQuestions(searchQuestionDTO);
                }
        );
    }

    @Test
    @DisplayName("아이디로 질문글 엔티티를 찾고 DTO로 변환할 수 있어야 함")
    void testQuestionDetail() {
        testTemplate(
                (question, testDataDTO) -> {
                    //질문글 상세조회를 하고, 실제 엔티티와 데이터가 같은지 검사함
                    QuestionDetailDTO detailDTO = questionService.questionDetail(question.getId());
                    QuestionDetailDTOTestUtil.testQuestionDetailDTO(em, question, detailDTO);
                }
        );
    }

    @Test
    @DisplayName("상세조회 기능의 투표점수 테스트")
    void testQuestionDetailVoteScore() {
        TestDataDTO testDataDTO = TestDataInit.init(em);

        Question question = testDataDTO.question();
        List<Member> members = testDataDTO.getMembers();
        members.forEach(member -> voteService.voteUp(member.getId(), question.getId()));
        int numberOfMembers = members.size();

        QuestionDetailDTO detailDTO = questionService.questionDetail(question.getId());
        assertThat(detailDTO.getVoteScore()).isEqualTo(numberOfMembers);
    }

    @Test
    @DisplayName("작성자는 질문글을 삭제할 수 있어야 함")
    void authorCanDeleteOwnQuestion() {
        testTemplate(
                (question, testDataDTO) -> {//삭제 기능 테스트
                    questionService.deleteQuestion(question.getAuthor().getId(), question.getId());
                    testDeleteSuccess(question);
                }
        );
    }

    @Test
    @DisplayName("관리자는 모든 질문글을 삭제할 수 있어야 함")
    void adminCanDeleteAllQuestion() {
        testTemplate(
                (question, testDataDTO) -> {
                    //관리자 권한으로 질문글 삭제
                    Member admin = testDataDTO.adminMember();
                    questionService.deleteQuestion(admin.getId(), question.getId());
                    //삭제완료되었는지 확인
                    testDeleteSuccess(question);
                }
        );
    }

    @Test
    @DisplayName("관리자가 아닌 사용자는 다른 사용자의 질문글을 지울 수 없어야 함")
    void memberCanNotDeleteOtherMembersQuestion() {
        testTemplate(
                (question, testDataDTO) -> {
                    //잘못된 삭제요청시 예외가 발생하는테 확인
                    Member author = question.getAuthor();
                    Member anotherMember = testDataDTO.anotherMemberAndNotAdmin(author);
                    assertThatThrownBy(() -> questionService.deleteQuestion(anotherMember.getId(), question.getId()))
                            .isInstanceOf(QuestionDeleteFailedException.class);
                }
        );
    }

    @Test
    @DisplayName("자기가 작성한 게시글을 수정할 수 있어야 함")
    void memberCanEditOwnQuestion() {
        testTemplate(
                (question, testDataDTO) -> {
                    editQuestion(question, question.getAuthor());
                    testEditSuccess(question);
                }
        );
    }

    @Test
    @DisplayName("자기가 작성한 게시글을 수정할 수 있어야 함")
    void adminCanEditAllQuestion() {
        testTemplate(
                (question, testDataDTO) -> {
                    editQuestion(question, testDataDTO.adminMember());
                    testEditSuccess(question);
                }
        );
    }

    @Test
    @DisplayName("일반 유저는 다른 사용자의 질문글을 수정할 수 없어야 함")
    void memberCanNotEditAnotherMembersQuestion() {
        testTemplate(
                (question, testDataDTO) -> {//다른 사용자의 질문글을 일반 사용자가 수정하려고 하면 예외 터지는지 테스트함
                    Member author = question.getAuthor();
                    Member anotherMemberAndNotAdmin = testDataDTO.anotherMemberAndNotAdmin(author);

                    //수정시도
                    assertThatThrownBy(() -> editQuestion(question, anotherMemberAndNotAdmin))
                            .isInstanceOf(QuestionEditFailedException.class)
                            .isInstanceOf(UnauthorizedQuestionEditException.class);

                    //대상 질문글을 조회해서 값이 바뀌었는지 확인(바뀌지 않아야 통과)
                    Question findQuestion = QueryUtils.questionById(em, question);
                    assertThat(findQuestion.getTitle()).isNotEqualTo(editTitle(question));
                    assertThat(findQuestion.getContent()).isNotEqualTo(editContent(question));

                }
        );
    }

    @Test
    @DisplayName("질문글의 내용을 빈 내용으로 수정할 수 없어야함")
    void canNotEditQuestionWithEmptyProperty() {
        TestDataDTO dataDTO = TestDataInit.init(em);

        em.flush();
        em.clear();

        String[][] testcases = {
                {"", ""},
                {"a", ""},
                {"", "a"}
        };

        Question question = dataDTO.question();
        Long authorId = question.getAuthor().getId();
        for (String[] testcase : testcases) {
            assertThatThrownBy(() -> questionService.editQuestion(authorId, question.getId(), testcase[0], testcase[1], new ArrayList<>()))
                    .isInstanceOf(QuestionEditFailedException.class)
                    .isInstanceOf(QuestionPropertyIsEmptyException.class);
        }
    }

    //테스트 템플릿
    private void testTemplate(BiConsumer<Question, TestDataDTO> testFunction) {
        TestDataDTO dataDTO = TestDataInit.init(em);
        List<Question> questions = dataDTO.getQuestions();

        em.flush();
        em.clear();

        for (Question question : questions) {
            testFunction.accept(question, dataDTO);
        }
    }

    private void testDeleteSuccess(Question question) {
        assertThat(isDeletedPost(em, question)).isTrue();
    }

    private void testEditSuccess(Question question) {
        Question findQuestion = QueryUtils.questionById(em, question);

        assertThat(findQuestion).extracting(
                Question::getTitle,
                Question::getContent
        ).containsExactly(
                editTitle(question),
                editContent(question)
        );
    }

    private void editQuestion(Question question, Member member) {
        String editTitle = editTitle(question);
        String editContent = editContent(question);
        List<Long> tagIds = EntityConverter.getTagIds(question);

        questionService.editQuestion(member.getId(), question.getId(), editTitle, editContent, tagIds);
    }

    private String editTitle(Question question) {
        return "edited-title-" + question.getId();
    }

    private String editContent(Question question) {
        return "edited-content-" + question.getId();
    }

}
