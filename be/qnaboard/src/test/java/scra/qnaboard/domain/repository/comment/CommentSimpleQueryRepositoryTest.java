package scra.qnaboard.domain.repository.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.question.QuestionRepository;
import scra.qnaboard.service.CommentService;
import scra.qnaboard.web.dto.comment.CommentDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentSimpleQueryRepositoryTest {

    @Autowired
    private CommentSimpleQueryRepository commentSimpleQueryRepository;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;


    @Test
    void 댓글이랑_작성자_정보를_한번에조회할수있어야함() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        String commentContent = "comment-content-1";
        //given
        Comment comment = commentRepository.save(new Comment(author, commentContent, question, null));

        //when
        Comment findComment = commentSimpleQueryRepository.commentWithAuthor(comment.getId()).orElse(null);

        //then
        assertThat(findComment).isNotNull();
        assertThat(findComment.getContent()).isEqualTo(commentContent);
        assertThat(findComment.getAuthor().getId()).isEqualTo(author.getId());
        assertThat(findComment.getAuthor().getNickname()).isEqualTo(author.getNickname());
    }

    @Test
    void 게시글_아이디로_댓글을_맵으로_가져올수있어야함() throws Exception {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));
        //given
        Question question1 = questionRepository.save(new Question(author, "content-1", "title-1"));
        Question question2 = questionRepository.save(new Question(author, "content-1", "title-1"));
        //given
        String commentContent = "comment-content-1";
        //given
        List<Comment> comments = new ArrayList<>();
        comments.add(commentRepository.save(new Comment(author, commentContent, question1, null)));
        comments.add(commentRepository.save(new Comment(author, commentContent, question2, null)));
        comments.add(commentRepository.save(new Comment(author, commentContent, question2, null)));
        comments.add(commentRepository.save(new Comment(author, commentContent, question2, null)));
        //given
        List<Long> questionIds = new ArrayList<>();
        questionIds.add(question1.getId());
        questionIds.add(question2.getId());

        //when
        Map<Long, List<CommentDTO>> commentMap = commentSimpleQueryRepository.commentMap(questionIds);

        //then
        assertThat(commentMap.get(question1.getId()).size()).isEqualTo(1);
        assertThat(commentMap.get(question2.getId()).size()).isEqualTo(3);
    }
}
