package scra.qnaboard.domain.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.answer.AnswerRepository;
import scra.qnaboard.domain.repository.comment.CommentRepository;
import scra.qnaboard.domain.repository.question.QuestionRepository;
import scra.qnaboard.domain.repository.question.QuestionSearchDetailRepository;
import scra.qnaboard.domain.repository.tag.TagRepository;
import scra.qnaboard.web.dto.answer.AnswerDetailDTO;
import scra.qnaboard.web.dto.comment.CommentDTO;
import scra.qnaboard.web.dto.question.detail.QuestionDetailDTO;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QuestionSearchRepositoryTest {

    @Autowired
    private QuestionSearchDetailRepository repository;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    void 질문_상세보기를_할수있어야함() {
        //given
        Member member = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(member, "q-content", "q-title"));
        long prevViewCount = question.getViewCount();
        //given
        Tag tag1 = tagRepository.save(new Tag(member, "tag-name1", "tag-desc2"));
        Tag tag2 = tagRepository.save(new Tag(member, "tag-name1", "tag-desc2"));
        //given
        int answerCount = 2;
        Answer answer1 = answerRepository.save(new Answer(member, "a-content1", question));
        Answer answer2 = answerRepository.save(new Answer(member, "a-content2", question));
        //given
        Comment comment1 = commentRepository.save(new Comment(member, "c-content1", question, null));
        Comment comment2 = commentRepository.save(new Comment(member, "c-content2", question, comment1));
        Comment comment3 = commentRepository.save(new Comment(member, "c-content3", question, comment1));
        Comment comment4 = commentRepository.save(new Comment(member, "c-content4", question, null));
        //given
        Comment comment5 = commentRepository.save(new Comment(member, "c-content5", answer1, null));
        Comment comment6 = commentRepository.save(new Comment(member, "c-content6", answer1, null));

        //when
        QuestionDetailDTO detailDTO = repository.questionDetail(question.getId());
        question = questionRepository.findById(question.getId()).orElseThrow(IllegalStateException::new);

        //then 질문게시글 데이터 검사
        assertThat(detailDTO).extracting(
                QuestionDetailDTO::getQuestionId,
                QuestionDetailDTO::getTitle,
                QuestionDetailDTO::getContent,
                QuestionDetailDTO::getViewCount,
                QuestionDetailDTO::getAuthorName
        ).containsExactly(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getViewCount(),
                question.getAuthor().getNickname()
        );
        //then 조회수 검사(1 만큼 증가해야함)
        assertThat(detailDTO.getViewCount()).isEqualTo(prevViewCount + 1);
        //then 질문게시글에 달린 답변게시글 개수 검사
        assertThat(detailDTO.getAnswers().size()).isEqualTo(answerCount);
        //then 댓글 검사
        assertThat(detailDTO.getComments().size()).isEqualTo(2);
        //then 대댓글 검사
        CommentDTO firstQuestionComment = detailDTO.getComments().stream()
                .filter(commentDTO -> commentDTO.getCommentId() == comment1.getId())
                .findFirst()
                .orElse(null);
        assertThat(firstQuestionComment).isNotNull();
        assertThat(firstQuestionComment.getChildren().size()).isEqualTo(2);
        //then 답변게시글에 달린 댓글 검사
        AnswerDetailDTO firstAnswerDTO = detailDTO.getAnswers().stream()
                .filter(answerDetailDTO -> answerDetailDTO.getAnswerId() == answer1.getId())
                .findFirst()
                .orElse(null);
        assertThat(firstAnswerDTO).isNotNull();
        assertThat(firstAnswerDTO.getComments().size()).isEqualTo(2);
    }
}
