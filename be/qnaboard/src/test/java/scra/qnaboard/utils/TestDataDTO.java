package scra.qnaboard.utils;

import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Question;

import java.util.Arrays;
import java.util.stream.Stream;

public class TestDataDTO {
    Tag[] tags;
    Question[] questions;
    Member[] members;
    Answer[] answers;
    Comment[] comments;

    public TestDataDTO(Tag[] tags,
                       Question[] questions,
                       Member[] members,
                       Answer[] answers,
                       Comment[] comments) {
        this.tags = tags;
        this.questions = questions;
        this.members = members;
        this.answers = answers;
        this.comments = comments;
    }

    public Stream<Comment> commentStream() {
        return Arrays.stream(comments);
    }

    public Stream<Question> questionStream() {
        return Arrays.stream(questions);
    }

    public Stream<Answer> answerStream() {
        return Arrays.stream(answers);
    }

    public Question question() {
        return Arrays.stream(questions)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 실패! 질문 엔티티를 찾지 못함!"));
    }

    public Answer answer() {
        return Arrays.stream(answers)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 실패! 답변 엔티티를 찾지 못함!"));
    }

    public Comment comment() {
        return Arrays.stream(comments)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 실패! 댓글 엔티티를 찾지 못함!"));
    }

    public Member noneAdminMember() {
        return Arrays.stream(members)
                .filter(Member::isNotAdmin)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 실패! 멤버 엔티티를 찾지 못함!"));
    }

    public Member anotherMemberAndNotAdmin(Member author) {
        return Arrays.stream(members)
                .filter(member -> member.isNotSame(author) && member.isNotAdmin())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 실패! 멤버 엔티티를 찾지 못함!"));
    }

    public Member adminMember() {
        return Arrays.stream(members)
                .filter(member -> !member.isNotAdmin())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 실패! 관리자 엔티티를 찾지 못함!"));
    }

    public Tag[] getTags() {
        return tags;
    }

    public Question[] getQuestions() {
        return questions;
    }

    public Member[] getMembers() {
        return members;
    }

    public Answer[] getAnswers() {
        return answers;
    }

    public Comment[] getComments() {
        return comments;
    }
}
