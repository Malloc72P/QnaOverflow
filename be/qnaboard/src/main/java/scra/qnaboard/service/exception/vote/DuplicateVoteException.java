package scra.qnaboard.service.exception.vote;

public class DuplicateVoteException extends RuntimeException {
    private static final String MESSAGE = "중복투표입니다";

    public DuplicateVoteException() {
        super(MESSAGE);
    }
}
