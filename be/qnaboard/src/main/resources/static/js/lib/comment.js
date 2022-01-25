export class Comment {

    static #parser = new DOMParser();

    static create = async (event) => {
        event.preventDefault();
        //필요한 엘리먼트 찾기
        const commentTextArea = event.target[0];
        const content = commentTextArea.value;
        const commentWrapper = commentTextArea.closest(".comment-wrapper");
        const commentList = commentWrapper.querySelector(".comment-list");
        //게시글 아이디 찾기
        const postId = Comment.#extractPostId(commentTextArea);
        //api url 생성
        const url = `/posts/${postId}/comments`;
        //서버에 전송할 body 생성
        let body = {
            "parentCommentId": null,
            "content": content
        };
        try {
            //api 요청
            const response = await request(url, PUT, body);
            //domParser를 가지고 응답메세지를 엘리먼트로 파싱
            const comment = Comment.#parser.parseFromString(response, "text/html").querySelector(".comment");
            //이벤트리스너 바인딩
            this.#setCommentEventListener(comment);
            //엘리먼트를 dom에 추가
            commentList.appendChild(comment);
            commentTextArea.value = "";
        } catch (error) {
            alertError(error);
        }
    };

    static reply = async (event) => {
        event.preventDefault();
        const commentTextArea = event.target[0];
        const commentWriter = event.target.closest(".comment-writer");
        const content = commentTextArea.value;
        const parentComment = event.target.closest(".comment");
        const parentCommentId = parentComment.id.substring(2);
        const commentList = parentComment.querySelector(".child-comment-wrapper");

        const postId = Comment.#extractPostId(commentTextArea);

        const url = `/posts/${postId}/comments`;

        let body = {
            "parentCommentId": parentCommentId,
            "content": content
        };

        try {
            const response = await request(url, PUT, body);
            const comment = Comment.#parser.parseFromString(response, "text/html").querySelector(".comment");
            this.#setCommentEventListener(comment);
            commentList.appendChild(comment);
            commentTextArea.value = "";
            commentWriter.classList.toggle("d-none");
        } catch (error) {
            alertError(error);
        }
    };

    static delete = async (event) => {
        const comment = event.target.closest(".comment");
        const commentId = comment.id.substr(2);
        const postId = Comment.#extractPostId(comment);

        const url = `/posts/${postId}/comments/${commentId}`;
        try {
            const response = await request(url, DELETE, null);
            comment.querySelector(".comment-author").innerText = response.deletedAuthorName;
            comment.querySelector(".comment-content").innerText = response.deletedContentName;
        } catch (error) {
            alertError(error);
        }
    }

    static edit = async (event) => {
        event.preventDefault();
        //textarea 찾고 안의 내용 꺼내기
        const commentTextArea = event.target[0];
        const content = commentTextArea.value;
        //wrapper 태그 찾고 거기 들어있는 postId꺼내기
        const postId = Comment.#extractPostId(commentTextArea);
        //댓글 컴포넌트 찾고 아이디 꺼내기
        const comment = event.target.closest(".comment");
        const commentId = Comment.#extractCommentId(comment);
        //댓글 수정 폼을 감싸고 있는 태그 찾기
        const commentEditor = event.target.closest(".comment-editor");
        //이전 content 값을 가지고 있는 태그와 이전값을 꺼내기
        const prevContentContainer = commentEditor.querySelector(".prev-comment-content");
        const prevContent = prevContentContainer.value;

        //바뀐게 없다면 API 호출 안함
        if (prevContent === content) {
            //댓글 수정 폼 숨기기
            commentEditor.classList.toggle("d-none");
            return;
        }
        const url = `/posts/${postId}/comments/${commentId}`;
        let body = {
            "content": content
        };
        try {
            const response = await request(url, PATCH, body);

            //댓글 내용 바꾸고 입력폼의 내용도 최신화하기
            comment.querySelector(".comment-content").innerText = response.content;
            commentTextArea.value = response.content;
            prevContentContainer.value = response.content;
            //댓글 수정 폼 숨기기
            commentEditor.classList.toggle("d-none");
        } catch (error) {
            alertError(error);
        }
    };

    static #extractCommentId = (comment) => {
        return comment.id.substr(2);
    };

    static #extractPostId = (childOfPostElement) => {
        return childOfPostElement.closest(".post").dataset.postid;
    };

    static #setCommentEventListener = (comment) => {
        comment.querySelector(".edit-comment-form").addEventListener("submit", Comment.edit);
        comment.querySelector(".delete-comment-button").addEventListener("click", Comment.delete);
        comment.querySelector(".reply-comment-form").addEventListener("submit", Comment.reply);
        comment.querySelector(".toggle-comment-writer").addEventListener("click", Comment.#toggleCreateComment);
        comment.querySelector(".toggle-comment-editor").addEventListener("click", Comment.#toggleEditComment);
    };

    static #toggleCreateComment = (event) => {
        event.preventDefault();
        const comment = event.target.closest(".comment");
        const commentWriter = comment.querySelector(".comment-writer");
        commentWriter.classList.toggle("d-none")
    };

    static #toggleEditComment = (event) => {
        event.preventDefault();
        const comment = event.target.closest(".comment");
        const commentWriter = comment.querySelector(".comment-editor");
        commentWriter.classList.toggle("d-none")
    };

    /**
     * 댓글기능 초기화 메서드
     */
    init = () => {
        //댓글생성폼 바인딩
        const createCommentForms = document.querySelectorAll(".create-comment-form");
        for (const commentForm of createCommentForms) {
            commentForm.addEventListener("submit", Comment.create);
        }
        //대댓글 생성폼 토글기능 바인딩
        const toggleCommentWriterButtons = document.querySelectorAll(".toggle-comment-writer");
        for (const toggleCommentWriterButton of toggleCommentWriterButtons) {
            toggleCommentWriterButton.addEventListener("click", Comment.#toggleCreateComment);
        }
        //대댓글 생성폼 바인딩
        const replyCommentForms = document.querySelectorAll(".reply-comment-form");
        for (const replyCommentForm of replyCommentForms) {
            replyCommentForm.addEventListener("submit", Comment.reply);
        }
        //댓글삭제버튼 바인딩
        const deleteCommentButtons = document.querySelectorAll(".delete-comment-button");
        for (const deleteCommentButton of deleteCommentButtons) {
            deleteCommentButton.addEventListener("click", Comment.delete);
        }
        //댓글 수정폼 토글기능 바인딩
        const toggleCommentEditorButtons = document.querySelectorAll(".toggle-comment-editor");
        for (const toggleCommentEditorButton of toggleCommentEditorButtons) {
            toggleCommentEditorButton.addEventListener("click", Comment.#toggleEditComment);
        }
        //댓글 수정폼 바인딩
        const editCommentForms = document.querySelectorAll(".edit-comment-form");
        for (const editCommentForm of editCommentForms) {
            editCommentForm.addEventListener("submit", Comment.edit);
        }
    };
}
