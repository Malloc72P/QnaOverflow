package scra.qnaboard.domain.repository.question;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.member.MemberRole;
import scra.qnaboard.domain.entity.post.Question;
import scra.qnaboard.domain.repository.MemberRepository;
import scra.qnaboard.domain.repository.tag.TagRepository;
import scra.qnaboard.service.QuestionService;
import scra.qnaboard.web.dto.question.detail.QuestionDetailDTO;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 질문글 상세조회 리포지토리 테스트
 */
@SpringBootTest
@Transactional
class QuestionSearchDetailRepositoryTest {

    @Autowired
    private QuestionSearchDetailRepository questionSearchDetailRepository;

    @Autowired
    private QuestionService questionService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("질문글_상세조회_기능_테스트")
    void testQuestionDetail() {
        //given
        Member author = memberRepository.save(new Member("nickname", "email", MemberRole.USER));

        //given
        String title = "title-1";
        String content = "content-1";

        //given
        Tag tag = new Tag(author, "tag", "tag-description");
        tagRepository.save(tag);

        //given
        List<Long> tagIds = new ArrayList<>();
        tagIds.add(tag.getId());

        //given
        long questionId = questionService.createQuestion(author.getId(), title, content, tagIds);
        Question findQuestion = questionService.findQuestion(questionId);

        //when
        QuestionDetailDTO detailDTO = questionSearchDetailRepository.questionDetail(questionId);

        //then
        assertThat(detailDTO.getTitle()).isEqualTo(title);
        assertThat(detailDTO.getContent()).isEqualTo(content);
        assertThat(detailDTO.getTags().size()).isEqualTo(tagIds.size());
        assertThat(detailDTO.getAuthorId()).isEqualTo(author.getId());
        assertThat(detailDTO.getAuthorName()).isEqualTo(author.getNickname());
        assertThat(detailDTO.getVoteScore()).isEqualTo(findQuestion.getScore());
    }

}
