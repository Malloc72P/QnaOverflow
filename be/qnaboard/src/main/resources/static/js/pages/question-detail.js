//답변게시글 처리
const deleteAnswerButtons = document.querySelectorAll(".answer-delete");
for (const deleteAnswerButton of deleteAnswerButtons) {
    deleteAnswerButton.addEventListener("pointerdown", deleteAnswer);
}

const submitAnswerButton = document.getElementById("submit-answer-button");
submitAnswerButton.addEventListener("click", createAnswer);

const editAnswerButtons = document.querySelectorAll(".answer-edit");
for (const button of editAnswerButtons) {
    button.addEventListener("pointerdown", toggleEditAnswerForm);
}

const editAnswerForms = document.querySelectorAll(".answer-edit-form");
for (const form of editAnswerForms) {
    form.addEventListener("submit", editAnswer);
}

//댓글 처리

const createCommentForms = document.querySelectorAll(".create-comment-form");
for (const commentForm of createCommentForms) {
    commentForm.addEventListener("submit", createComment);
}

const toggleCommentWriterButtons = document.querySelectorAll(".toggle-comment-writer");
for (const toggleCommentWriterButton of toggleCommentWriterButtons) {
    toggleCommentWriterButton.addEventListener("pointerdown", toggleCommentWriter);
}

const replyCommentForms = document.querySelectorAll(".reply-comment-form");
for (const replyCommentForm of replyCommentForms) {
    replyCommentForm.addEventListener("submit", replyComment);
}

const deleteCommentButtons = document.querySelectorAll(".delete-comment-button");
for (const deleteCommentButton of deleteCommentButtons) {
    deleteCommentButton.addEventListener("pointerdown", deleteComment);
}

const toggleCommentEditorButtons = document.querySelectorAll(".toggle-comment-editor");
for (const toggleCommentEditorButton of toggleCommentEditorButtons) {
    toggleCommentEditorButton.addEventListener("pointerdown", toggleCommentEditor);
}

const editCommentForms = document.querySelectorAll(".edit-comment-form");
for (const editCommentForm of editCommentForms) {
    editCommentForm.addEventListener("submit", editComment);
}

