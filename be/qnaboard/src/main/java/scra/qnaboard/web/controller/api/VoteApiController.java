package scra.qnaboard.web.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import scra.qnaboard.configuration.auth.LoginUser;
import scra.qnaboard.configuration.auth.SessionUser;
import scra.qnaboard.domain.entity.vote.VoteType;
import scra.qnaboard.service.VoteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/votes")
public class VoteApiController {

    private final VoteService voteService;

    @PutMapping
    public void vote(@PathVariable("postId") Long postId,
                     @RequestParam VoteType voteType,
                     @LoginUser SessionUser sessionUser) {
        voteService.vote(sessionUser.getId(), postId, voteType);
    }
}
