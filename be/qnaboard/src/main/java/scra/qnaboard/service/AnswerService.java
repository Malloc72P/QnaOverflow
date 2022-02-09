package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.answer.AnswerRepository;
import scra.qnaboard.domain.repository.answer.AnswerSimpleQueryRepository;
import scra.qnaboard.service.exception.answer.delete.UnauthorizedAnswerDeletionException;
import scra.qnaboard.service.exception.answer.edit.ForbiddenAnswerEditException;
import scra.qnaboard.service.exception.answer.search.AnswerNotFoundException;
import scra.qnaboard.dto.answer.AnswerDetailDTO;
import scra.qnaboard.dto.answer.edit.EditAnswerResultDTO;

/**
 * 답변글 서비스
 */
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final MemberService memberService;
    private final QuestionService questionService;
    private final AnswerRepository answerRepository;
    private final AnswerSimpleQueryRepository answerSimpleQueryRepository;

    public AnswerDetailDTO createAnswer(long authorId, long questionId, String content) {
        //작성자 엔티티 조회
        Member author = memberService.findMember(authorId);
        //답변글의 주인이 될 질문글 엔티티 조회
        Question question = questionService.findQuestion(questionId);
        //답변글 엔티티 생성 후 저장
        Answer answer = new Answer(author, content, question);
        answer = answerRepository.save(answer);
        //생성결과 DTO를 생성해서 반환
        return AnswerDetailDTO.from(answer);
    }

    /**
     * 답변게시글 삭제로직을 처리하는 메서드.
     * soft delete라서 데이터가 물리적으로 지워지지 않는다.
     * 관리자는 다른 관리자의 게시글을 지울 수 있음
     *
     * @param requesterId 삭제요청을 한 사용자의 아이디
     * @param answerId    삭제할 질문게시글의 아이디
     */
    @Transactional
    public void deleteAnswer(long requesterId, long answerId) {
        //대상 답변글과 요청자 엔티티 조회
        Answer answer = answerWithAuthor(answerId);
        Member requester = memberService.findMember(requesterId);
        //관리자가 아니면서 소유자도 아니면 실패해야함
        if (requester.isNotAdmin() && answer.isNotOwner(requester)) {
            throw new UnauthorizedAnswerDeletionException(answerId, requesterId);
        }
        //관리자이거나 질문게시글의 소유자면 질문게시글을 삭제함
        answer.delete();
    }

    /**
     * 답변게시글을 수정하는 메서드
     *
     * @param requesterId 요청한 유저의 아이디
     * @param answerId    수정할 답변게시글의 아이디
     * @param content     답변게시글의 새로운 내용
     */
    @Transactional
    public EditAnswerResultDTO editAnswer(long requesterId, long answerId, String content) {
        //대상 답변글과 요청자 엔티티 조회
        Answer answer = answerWithAuthor(answerId);
        Member requester = memberService.findMember(requesterId);
        //관리자가 아니면서 소유자도 아니면 실패해야함
        if (requester.isNotAdmin() && answer.isNotOwner(requester)) {
            throw new ForbiddenAnswerEditException(answerId, requesterId);
        }
        //답변글 수정
        answer.update(content);
        //수정 결과 DTO를 만들어서 반환
        return new EditAnswerResultDTO(answer.getContent(), answer.getLastModifiedDate());
    }

    /**
     * 답변게시글과 작성자를 패치조인으로 함께 조회하는 로직을 처리하는 메서드.
     *
     * @param answerId 조회할 게시글의 아이디
     * @return 답변게시글 엔티티(작성자 포함)
     */
    private Answer answerWithAuthor(long answerId) {
        return answerSimpleQueryRepository.answerWithAuthor(answerId)
                .orElseThrow(() -> new AnswerNotFoundException(answerId));
    }

}
