const toggleCommentWriter = (event) => {
    event.preventDefault();
    const comment = event.target.closest(".comment");
    const commentWriter = comment.querySelector(".comment-writer");
    commentWriter.classList.toggle("d-none")
};

const alertWhenCreateCommentIsFailed = () => {
    alert("댓글은 6자 이상이어야 합니다");
};

const createComment = async (event) => {
    event.preventDefault();
    const commentTextArea = event.target[0];
    const content = commentTextArea.value;
    const commentWrapper = commentTextArea.closest(".comment-wrapper");
    const commentList = commentWrapper.querySelector(".comment-list");

    const postId = commentWrapper.id.substring(2);

    const url = `http://localhost:8080/posts/${postId}/comments`;

    let body = {
        "parentCommentId": null,
        "content": content
    };

    try {
        const response = await request(url, PUT, body, MODE_TEXT);
        const domParser = new DOMParser();
        const comment = domParser.parseFromString(response, "text/html").querySelector(".comment");
        setCommentEventListener(comment);
        commentList.appendChild(comment);
        commentTextArea.value = "";

    } catch (error) {
        alertWhenCreateCommentIsFailed();
    }
};

const replyComment = async (event) => {
    event.preventDefault();
    const commentTextArea = event.target[0];
    const commentWriter = event.target.closest(".comment-writer");
    const content = commentTextArea.value;
    const parentComment = event.target.closest(".comment");
    const parentCommentId = parentComment.id.substring(2);
    const commentWrapper = commentTextArea.closest(".comment-wrapper");
    const commentList = parentComment.querySelector(".child-comment-wrapper");

    const postId = commentWrapper.id.substring(2);

    const url = `http://localhost:8080/posts/${postId}/comments`;

    let body = {
        "parentCommentId": parentCommentId,
        "content": content
    };

    try {
        const response = await request(url, PUT, body, MODE_TEXT);
        const domParser = new DOMParser();
        const comment = domParser.parseFromString(response, "text/html").querySelector(".comment");
        setCommentEventListener(comment);
        commentList.appendChild(comment);
        commentTextArea.value = "";
        commentWriter.classList.toggle("d-none");
    } catch (error) {
        alertWhenCreateCommentIsFailed();
    }
};

const deleteComment = async (event) => {
    const comment = event.target.closest(".comment");
    const commentId = comment.id.substr(2);
    const commentWrapper = event.target.closest(".comment-wrapper");
    const postId = commentWrapper.id.substring(2);

    const url = `http://localhost:8080/posts/${postId}/comments/${commentId}`;
    try {
        const response = await request(url, DELETE, {}, MODE_JSON);
        comment.querySelector(".comment-author").innerText = response.deletedAuthorName;
        comment.querySelector(".comment-content").innerText = response.deletedContentName;
    } catch (error) {
        alertWhenCreateCommentIsFailed();
    }
}

const setCommentEventListener = (comment) => {
    comment.querySelector(".toggle-comment-writer").addEventListener("pointerdown", toggleCommentWriter);
    comment.querySelector(".reply-comment-form").addEventListener("submit", replyComment);
}
