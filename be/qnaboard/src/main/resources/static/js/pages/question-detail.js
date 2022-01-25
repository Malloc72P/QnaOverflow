//답변게시글 처리
import {Answer} from "../lib/answer.js";

class QuestionDetail {

    #answer = new Answer();

    init = () => {
        this.#answer.init();
    }
}

const questionDetail = new QuestionDetail();

questionDetail.init()


//댓글 처리
const createCommentForms = document.querySelectorAll(".create-comment-form");
for (const commentForm of createCommentForms) {
    commentForm.addEventListener("submit", createComment);
}

const toggleCommentWriterButtons = document.querySelectorAll(".toggle-comment-writer");
for (const toggleCommentWriterButton of toggleCommentWriterButtons) {
    toggleCommentWriterButton.addEventListener("click", toggleCommentWriter);
}

const replyCommentForms = document.querySelectorAll(".reply-comment-form");
for (const replyCommentForm of replyCommentForms) {
    replyCommentForm.addEventListener("submit", replyComment);
}

const deleteCommentButtons = document.querySelectorAll(".delete-comment-button");
for (const deleteCommentButton of deleteCommentButtons) {
    deleteCommentButton.addEventListener("click", deleteComment);
}

const toggleCommentEditorButtons = document.querySelectorAll(".toggle-comment-editor");
for (const toggleCommentEditorButton of toggleCommentEditorButtons) {
    toggleCommentEditorButton.addEventListener("click", toggleCommentEditor);
}

const editCommentForms = document.querySelectorAll(".edit-comment-form");
for (const editCommentForm of editCommentForms) {
    editCommentForm.addEventListener("submit", editComment);
}

