package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.QuestionRepository;
import scra.qnaboard.web.dto.question.list.QuestionListDTO;
import scra.qnaboard.web.dto.question.list.QuestionSummaryDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionListDTO questionList() {
        List<Question> questions = questionRepository.findAll();
        List<QuestionSummaryDTO> summaryDTOS = questions.stream()
                .map(QuestionSummaryDTO::from)
                .collect(Collectors.toList());

        return QuestionListDTO.from(summaryDTOS);
    }

}
