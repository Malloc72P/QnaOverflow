package scra.qnaboard.domain.repository.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.dto.comment.CommentDTO;
import scra.qnaboard.dto.comment.QCommentDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static scra.qnaboard.domain.entity.QComment.comment;
import static scra.qnaboard.domain.entity.member.QMember.member;

/**
 * 댓글 관련 약간 복잡한 쿼리를 위한 리포지토리
 */
@Repository
@RequiredArgsConstructor
public class CommentSimpleQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 댓글 아이디로 댓글과 작성자를 패치조인으로 가져와서 옵셔널로 감싸서 반환한다
     */
    public Optional<Comment> commentWithAuthor(long commentId) {
        Comment findComment = queryFactory.select(comment)
                .from(comment)
                .innerJoin(comment.author, member).fetchJoin()
                .where(comment.id.eq(commentId))
                .fetchOne();
        return Optional.ofNullable(findComment);
    }

    /**
     * 게시글 아이디로 대댓글을 전부 조회해서 정리한 다음, 맵에 저장해서 반환하는 메서드 <br>
     * 맵의 키는 게시글의 아이디이고, 값은 대댓글 List이다.
     *
     * @param postIds 댓글을 조회할 게시글의 아이디 리스트
     * @return 댓글 맵
     */
    public Map<Long, List<CommentDTO>> commentMap(List<Long> postIds) {
        List<CommentDTO> commentDTOS = commentDtosByPostId(postIds);
        //6. 대댓글 조립 1 - 부모 게시글 아이디로 그룹화
        //originalCommentMap : 디비에서 가져온 댓글 목록을 게시글 ID로 그룹화해서 모아놓은 맵
        Map<Long, List<CommentDTO>> originalCommentMap = commentDTOS.stream()
                .collect(Collectors.groupingBy(CommentDTO::getParentPostId));

        //7. 대댓글 조립 3 - 계층관계 조립
        //newCommentMap : 조립된 댓글목록을 적절한 게시글에 넣어주기 위한 맵 자료구조
        Map<Long, List<CommentDTO>> newCommentMap = new HashMap<>();
        for (Map.Entry<Long, List<CommentDTO>> entry : originalCommentMap.entrySet()) {
            Long postId = entry.getKey();
            List<CommentDTO> originalComments = entry.getValue();
            List<CommentDTO> assembledComments = CommentDTO.assemble(originalComments);
            newCommentMap.put(postId, assembledComments);
        }

        return newCommentMap;
    }

    /**
     * 대댓글 DTO 리스트를 게시글 id로 조회
     *
     * @param postIds 게시글 id
     * @return 대댓글 DTO 리스트
     */
    private List<CommentDTO> commentDtosByPostId(List<Long> postIds) {
        List<CommentDTO> commentDTOS = queryFactory
                .select(new QCommentDTO(
                        comment.id,
                        comment.author.id,
                        comment.author.nickname,
                        comment.createdDate,
                        comment.content,
                        comment.parentComment.id,
                        comment.parentPost.id,
                        comment.deleted
                )).from(comment)
                .innerJoin(comment.author, member)
                .where(comment.parentPost.id.in(postIds))
                .fetch();

        //삭제된 코멘트는 블러 처리한다
        for (CommentDTO commentDTO : commentDTOS) {
            commentDTO.blur();
        }

        return commentDTOS;
    }

}
