package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.repository.QuestionRepository;
import scra.qnaboard.domain.repository.QuestionSearchRepository;
import scra.qnaboard.web.dto.question.list.QuestionListDTO;
import scra.qnaboard.web.dto.question.list.QuestionSummaryDTO;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionSearchRepository questionSearchRepository;

    public QuestionListDTO questionList() {
        List<QuestionSummaryDTO> questionSummaryDTOS = questionSearchRepository.search();
        return new QuestionListDTO(questionSummaryDTOS);
    }

}
