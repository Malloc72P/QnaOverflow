package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.answer.AnswerRepository;
import scra.qnaboard.domain.repository.answer.AnswerSimpleQueryRepository;
import scra.qnaboard.service.exception.answer.delete.AnswerDeleteFailedException;
import scra.qnaboard.service.exception.answer.edit.UnauthorizedAnswerEditException;
import scra.qnaboard.service.exception.answer.search.AnswerNotFoundException;
import scra.qnaboard.web.dto.answer.AnswerDetailDTO;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final MemberService memberService;
    private final QuestionService questionService;
    private final AnswerRepository answerRepository;
    private final AnswerSimpleQueryRepository answerSimpleQueryRepository;

    public AnswerDetailDTO createAnswer(long authorId, long questionId, String content) {
        Member author = memberService.findMember(authorId);
        Question question = questionService.findQuestion(questionId);
        Answer answer = new Answer(author, content, question);
        answerRepository.save(answer);

        return AnswerDetailDTO.from(answer);
    }

    /**
     * 답변게시글 삭제로직을 처리하는 메서드.
     * soft delete라서 데이터가 물리적으로 지워지지 않는다.
     *
     * @param requesterId 삭제요청을 한 사용자의 아이디
     * @param answerId    삭제할 질문게시글의 아이디
     */
    @Transactional
    public void deleteQuestion(long requesterId, long answerId) {
        Answer answer = answerWithAuthor(answerId);
        Member requester = memberService.findMember(requesterId);

        //관리자가 아니면서 소유자도 아니면 실패해야함
        if (requester.isNotAdmin() && answer.isNotOwner(requester)) {
            throw new AnswerDeleteFailedException(AnswerDeleteFailedException.UNAUTHORIZED, answerId, requesterId);
        }

        //관리자이거나 질문게시글의 소유자면 질문게시글 삭제함
        //관리자는 다른 관리자의 게시글을 지울 수 있음
        answerRepository.deleteById(answerId);
    }

    /**
     * 답변게시글을 수정하는 메서드
     *
     * @param requesterId 요청한 유저의 아이디
     * @param answerId    수정할 답변게시글의 아이디
     * @param content     답변게시글의 새로운 내용
     */
    @Transactional
    public void editQuestion(long requesterId, long answerId, String content) {
        Answer answer = answerWithAuthor(answerId);
        Member requester = memberService.findMember(requesterId);

        //관리자가 아니면서 소유자도 아니면 실패해야함
        if (requester.isNotAdmin() && answer.isNotOwner(requester)) {
            throw new UnauthorizedAnswerEditException(answerId, requesterId);
        }

        answer.update(content);
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
