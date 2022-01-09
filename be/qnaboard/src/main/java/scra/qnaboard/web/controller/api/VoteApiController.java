package scra.qnaboard.web.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import scra.qnaboard.domain.entity.vote.VoteType;
import scra.qnaboard.service.VoteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/votes")
public class VoteApiController {

    private final VoteService voteService;

    @PutMapping
    public void vote(@PathVariable("postId") Long postId,
                     @RequestParam VoteType voteType) {
        voteService.vote(1L, postId, voteType);
    }
}
