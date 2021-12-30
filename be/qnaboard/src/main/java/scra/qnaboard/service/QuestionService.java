package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.QuestionRepository;
import scra.qnaboard.domain.repository.QuestionSearchRepository;
import scra.qnaboard.service.exception.MemberNotFoundException;
import scra.qnaboard.web.dto.question.detail.QuestionDetailDTO;
import scra.qnaboard.web.dto.question.list.QuestionListDTO;
import scra.qnaboard.web.dto.question.list.QuestionSummaryDTO;

import java.util.List;

/**
 * 질문 엔티티에 대한 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final QuestionSearchRepository questionSearchRepository;

    /**
     * 질문목록조회 로직을 처리하는 메서드 <br>
     * 복잡한 검색로직을 담당하는 QuestionSearchRepository를 사용해서 로직을 처리한다
     *
     * @return 질문목록조회를 위한 DTO
     */
    public QuestionListDTO questionList() {
        List<QuestionSummaryDTO> questionSummaryDTOS = questionSearchRepository.search();
        return new QuestionListDTO(questionSummaryDTOS);
    }

    /**
     * 질문글 상세조회 로직을 처리하는 메서드
     * @param questionId 조회할 질문글의 아이디
     * @return 질문글 상세조회 DTO
     */
    public QuestionDetailDTO questionDetail(long questionId) {
        return questionSearchRepository.questionDetailV2(questionId);
    }

    @Transactional
    public long createQuestion(long authorId, String title, String content) {
        Member author = memberRepository.findById(authorId).orElseThrow(() -> new MemberNotFoundException(authorId));
        Question question = new Question(author, content, title);
        questionRepository.save(question);
        return question.getId();
    }

}
