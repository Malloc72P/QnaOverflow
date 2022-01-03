package scra.qnaboard.utils;

import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Question;

import java.util.Arrays;

public class TestDataDTO {
    Tag[] tags;
    Question[] questions;
    Member[] members;
    Answer[] answers;

    public TestDataDTO(Tag[] tags,
                       Question[] questions,
                       Member[] members,
                       Answer[] answers) {
        this.tags = tags;
        this.questions = questions;
        this.members = members;
        this.answers = answers;
    }

    public Member anotherMember(Member author) {
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
}
