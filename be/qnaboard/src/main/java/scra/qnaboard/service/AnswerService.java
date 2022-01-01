package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.AnswerRepository;
import scra.qnaboard.web.dto.answer.AnswerDetailDTO;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final MemberService memberService;
    private final QuestionService questionService;
    private final AnswerRepository answerRepository;

    public AnswerDetailDTO createAnswer(long authorId, long questionId, String content) {
        Member author = memberService.findMember(authorId);
        Question question = questionService.findQuestion(questionId);
        Answer answer = new Answer(author, content, question);
        answerRepository.save(answer);

        return AnswerDetailDTO.from(answer);
    }
}
