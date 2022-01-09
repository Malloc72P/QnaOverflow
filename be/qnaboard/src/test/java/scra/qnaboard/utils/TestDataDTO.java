package scra.qnaboard.utils;

import scra.qnaboard.domain.entity.Comment;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.member.Member;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Question;

import java.util.List;

public class TestDataDTO {
    List<Tag> tags;
    List<Question> questions;
    List<Member> members;
    List<Answer> answers;
    List<Comment> comments;

    public TestDataDTO(List<Tag> tags,
                       List<Question> questions,
                       List<Member> members,
                       List<Answer> answers,
                       List<Comment> comments) {
        this.tags = tags;
        this.questions = questions;
        this.members = members;
        this.answers = answers;
        this.comments = comments;
    }

    public Question question() {
        return questions.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 실패! 질문 엔티티를 찾지 못함!"));
    }

    public Answer answer() {
        return answers.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 실패! 답변 엔티티를 찾지 못함!"));
    }

    public Comment comment() {
        return comments.stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 실패! 댓글 엔티티를 찾지 못함!"));
    }

    public Member noneAdminMember() {
        return members.stream()
                .filter(Member::isNotAdmin)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 실패! 멤버 엔티티를 찾지 못함!"));
    }

    public Member anotherMemberAndNotAdmin(Member author) {
        return members.stream()
                .filter(member -> member.isNotSame(author) && member.isNotAdmin())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 실패! 멤버 엔티티를 찾지 못함!"));
    }

    public Member adminMember() {
        return members.stream()
                .filter(member -> !member.isNotAdmin())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("테스트 실패! 관리자 엔티티를 찾지 못함!"));
    }

    public List<Tag> getTags() {
        return tags;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
