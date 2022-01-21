package scra.qnaboard.service.exception.vote;

import scra.qnaboard.domain.entity.vote.VoteType;
import scra.qnaboard.service.exception.DescriptionMessageCodeSupplier;

public class DuplicateVoteException extends RuntimeException implements DescriptionMessageCodeSupplier {
    private static final String MESSAGE = "중복투표입니다";
    private final VoteType voteType;

    public DuplicateVoteException(VoteType voteType) {
        super(MESSAGE);
        this.voteType = voteType;
    }

    @Override
    public String descriptionMessageCode() {
        switch (voteType) {
            case UP:
                return "vote.duplicate.up";
            case DOWN:
                return "vote.duplicate.down";
        }
        return "unknown";
    }
}
