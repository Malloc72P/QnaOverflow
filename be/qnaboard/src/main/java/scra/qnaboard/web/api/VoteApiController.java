package scra.qnaboard.web.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import scra.qnaboard.configuration.auth.LoginUser;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.domain.entity.vote.VoteType;
import scra.qnaboard.service.VoteService;

/**
 * 투표 API 컨트롤러
 * 게시글에 대한 추천, 비추천 요청을 처리한다
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/votes")
public class VoteApiController {

    private final VoteService voteService;

    /**
     * 추천, 비추천 요청을 처리하는 핸들러
     *
     * @param postId      게시글 아이디
     * @param voteType    투표 유형(추천, 비추천)
     * @param sessionUser 요청을 한 회원정보를 담은 DTO
     */
    @PutMapping
    public void vote(@PathVariable("postId") Long postId,
                     @RequestParam VoteType voteType,
                     @LoginUser SessionUser sessionUser) {
        voteService.vote(sessionUser.getId(), postId, voteType);
    }
}
