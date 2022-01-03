const deleteAnswerButtons = document.querySelectorAll(".answer-delete");

for (const deleteAnswerButton of deleteAnswerButtons) {
    deleteAnswerButton.addEventListener("pointerdown", deleteAnswer);
}

const editAnswerButtons = document.querySelectorAll(".answer-edit");
const editAnswerForms = document.querySelectorAll(".answer-edit-form");

for (const button of editAnswerButtons) {
    button.addEventListener("pointerdown", toggleEditAnswerForm);
}

for (const form of editAnswerForms) {
    form.addEventListener("submit", editAnswer);
}

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
