package scra.qnaboard.utils;

import scra.qnaboard.domain.entity.Member;
import scra.qnaboard.domain.entity.Tag;
import scra.qnaboard.domain.entity.post.Answer;
import scra.qnaboard.domain.entity.post.Question;

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
