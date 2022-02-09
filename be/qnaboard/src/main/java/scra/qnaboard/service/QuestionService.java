package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.question.QuestionRepository;
import scra.qnaboard.domain.repository.question.QuestionSearchDetailRepository;
import scra.qnaboard.domain.repository.question.QuestionSearchListRepository;
import scra.qnaboard.domain.repository.question.QuestionSimpleQueryRepository;
import scra.qnaboard.dto.question.edit.QuestionWithTagDTO;
import scra.qnaboard.service.exception.question.AlreadyDeletedQuestionException;
import scra.qnaboard.service.exception.question.delete.UnauthorizedQuestionDeletionException;
import scra.qnaboard.service.exception.question.edit.UnauthorizedQuestionEditException;
import scra.qnaboard.service.exception.question.search.QuestionNotFoundException;
import scra.qnaboard.dto.question.detail.QuestionDetailDTO;
import scra.qnaboard.dto.question.list.QuestionSummaryDTO;
import scra.qnaboard.dto.question.search.ParsedSearchQuestionDTO;

import java.util.List;

/**
 * 질문 엔티티에 대한 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final MemberService memberService;
    private final QuestionTagService questionTagService;
    private final QuestionRepository questionRepository;
    private final QuestionSimpleQueryRepository questionSimpleQueryRepository;
    private final QuestionSearchDetailRepository questionSearchDetailRepository;
    private final QuestionSearchListRepository questionSearchListRepository;

    /**
     * 질문목록조회 로직을 처리하는 메서드 <br>
     * 복잡한 검색로직을 담당하는 QuestionSearchRepository를 사용해서 로직을 처리한다
     *
     * @return 질문목록조회를 위한 DTO
     */
    public Page<QuestionSummaryDTO> searchQuestions(ParsedSearchQuestionDTO searchQuestionDTO, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return questionSearchListRepository.search(searchQuestionDTO, pageRequest);
    }

    /**
     * 질문글 상세조회 로직을 처리하는 메서드
     *
     * @param questionId 조회할 질문글의 아이디
     * @return 질문글 상세조회 DTO
     */
    @Transactional
    public QuestionDetailDTO questionDetail(long questionId) {
        return questionSearchDetailRepository.questionDetail(questionId);
    }

    /**
     * 질문게시글을 생성로직을 처리하는 메서드
     *
     * @param authorId 게시글 작성자의 아이디
     * @param title    게시글 제목
     * @param content  게시글 내용
     * @return 생성된 게시글의 아이디
     */
    @Transactional
    public long createQuestion(long authorId, String title, String content, List<Long> tagIds) {
        //작성자를 조회한다
        Member author = memberService.findMember(authorId);

        //질문글 생성 및 저장
        Question question = new Question(author, content, title);
        question = questionRepository.save(question);

        //필요한 태그를 전부 조회하고, 질문글에 추가한다
        questionTagService.createQuestionTags(question, tagIds);

        //아이디를 반환한다
        return question.getId();
    }

    /**
     * 게시글 삭제로직을 처리하는 메서드.
     * soft delete라서 데이터가 물리적으로 지워지지 않는다.
     *
     * @param requesterId 삭제요청을 한 사용자의 아이디
     * @param questionId  삭제할 질문게시글의 아이디
     */
    @Transactional
    public void deleteQuestion(long requesterId, long questionId) {
        Question question = questionWithAuthor(questionId);
        Member requester = memberService.findMember(requesterId);

        //관리자가 아니면서 소유자도 아니면 실패해야함
        if (requester.isNotAdmin() && question.isNotOwner(requester)) {
            throw new UnauthorizedQuestionDeletionException(questionId, requesterId);
        }

        //관리자이거나 질문게시글의 소유자면 질문게시글 삭제함
        //관리자는 다른 관리자의 게시글을 지울 수 있음
        question.delete();
    }

    public QuestionWithTagDTO questionWithTag(long questionId) {
        Question question = findQuestion(questionId);

        return QuestionWithTagDTO.from(question);
    }

    @Transactional
    public void editQuestion(long requesterId, long questionId, String title, String content, List<Long> tagIds) {
        Question question = questionWithAuthor(questionId);
        Member requester = memberService.findMember(requesterId);

        //관리자가 아니면서 소유자도 아니면 실패해야함
        if (requester.isNotAdmin() && question.isNotOwner(requester)) {
            throw new UnauthorizedQuestionEditException(questionId, requesterId);
        }

        //질문게시글 수정
        question.update(title, content);

        //태그 정보 수정
        questionTagService.updateQuestionTags(question, tagIds);
    }

    public Question findQuestion(long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));

        if (question.isDeleted()) {
            throw new AlreadyDeletedQuestionException();
        }

        return question;
    }

    /**
     * 질문게시글과 작성자를 패치조인으로 함께 조회하는 로직을 처리하는 메서드.
     *
     * @param questionId 조회할 게시글의 아이디
     * @return 질문게시글 엔티티(작성자 포함)
     */
    private Question questionWithAuthor(long questionId) {
        return questionSimpleQueryRepository.questionWithAuthor(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(questionId));
    }
}
