package scra.qnaboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scra.qnaboard.domain.entity.post.Post;
import scra.qnaboard.domain.entity.vote.VoteType;
import scra.qnaboard.domain.repository.PostRepository;
import scra.qnaboard.service.exception.post.PostNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    /**
     * 게시글을 찾아서 반환함
     * 게시글은 질문글 또는 답변글일 수 있음
     */
    public Post findPostById(long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId));
    }

    @Transactional
    public void updateScore(long postId, VoteType voteType) {
        if (voteType == VoteType.UP) {
            postRepository.increaseScore(postId);
        } else {
            postRepository.decreaseScore(postId);
        }
    }
}
