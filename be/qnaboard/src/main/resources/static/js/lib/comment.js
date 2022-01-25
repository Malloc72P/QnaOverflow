import {ApiHelper} from "./apiHelper.js";

export class Comment {

    static #parser = new DOMParser();
    #currentlyOpenedForm = null;

    create = async (event) => {
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
            const response = await ApiHelper.request(url, ApiHelper.PUT, body);
            //domParser를 가지고 응답메세지를 엘리먼트로 파싱
            const comment = Comment.#parser.parseFromString(response, "text/html").querySelector(".comment");
            //이벤트리스너 바인딩
            this.#setCommentEventListener(comment);
            //엘리먼트를 dom에 추가
            commentList.appendChild(comment);
            commentTextArea.value = "";
        } catch (error) {
            ApiHelper.alertError(error);
        }
    };

    reply = async (event) => {
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
            const response = await ApiHelper.request(url, ApiHelper.PUT, body);
            const comment = Comment.#parser.parseFromString(response, "text/html").querySelector(".comment");
            this.#setCommentEventListener(comment);
            commentList.appendChild(comment);
            commentTextArea.value = "";
            this.#closeForm(commentWriter);
        } catch (error) {
            ApiHelper.alertError(error);
        }
    };

    delete = async (event) => {
        const comment = event.target.closest(".comment");
        const commentId = comment.id.substr(2);
        const postId = Comment.#extractPostId(comment);

        const url = `/posts/${postId}/comments/${commentId}`;
        try {
            const response = await ApiHelper.request(url, ApiHelper.DELETE, null);
            comment.querySelector(".comment-author").innerText = response.deletedAuthorName;
            comment.querySelector(".comment-content").innerText = response.deletedContentName;
            comment.querySelector(".comment-buttons").remove();
        } catch (error) {
            ApiHelper.alertError(error);
        }
    }

    edit = async (event) => {
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
            const response = await ApiHelper.request(url, ApiHelper.PATCH, body);

            //댓글 내용 바꾸고 입력폼의 내용도 최신화하기
            comment.querySelector(".comment-content").innerText = response.content;
            commentTextArea.value = response.content;
            prevContentContainer.value = response.content;
            //댓글 수정 폼 숨기기
            commentEditor.classList.toggle("d-none");
        } catch (error) {
            ApiHelper.alertError(error);
        }
    };

    /**
     * 댓글 엘리먼트로부터 댓글 아이디를 추출한다
     * @param comment
     * @returns {string}
     */
    static #extractCommentId = (comment) => {
        return comment.id.substr(2);
    };

    /**
     * 게시글의 하위 엘리먼트를 파라미터로 받아서 게시글 아이디를 추출한 다음 반환한다
     * @param childOfPostElement
     * @returns {string}
     */
    static #extractPostId = (childOfPostElement) => {
        return childOfPostElement.closest(".post").dataset.postid;
    };

    /**
     * 댓글 엘리먼트에 이벤트 리스너를 바인딩해준다
     * @param comment
     */
    #setCommentEventListener = (comment) => {
        comment.querySelector(".edit-comment-form").addEventListener("submit", this.edit);
        comment.querySelector(".delete-comment-button").addEventListener("click", this.delete);
        comment.querySelector(".reply-comment-form").addEventListener("submit", this.reply);
        comment.querySelector(".toggle-comment-writer").addEventListener("click", this.#toggleReplyComment);
        comment.querySelector(".toggle-comment-editor").addEventListener("click", this.#toggleEditComment);
    };

    /**
     * 답글 폼을 열고 닫는 기능을 수행하는 메서드.
     * 이벤트를 발생시킨 버튼을 가지고 있는 댓글 엘리먼트를 찾은 다음, 그 안에 있는 답글 폼을 찾고 토글한다
     * @param event
     */
    #toggleReplyComment = (event) => {
        event.preventDefault();
        const comment = event.target.closest(".comment");
        const commentWriter = comment.querySelector(".comment-writer");
        this.#toggleForm(commentWriter);
    };

    /**
     * 댓글 수정 폼을 열고 닫는 기능을 수행하는 메서드
     * 이벤트를 발생시킨 버튼을 가지고 있는 댓글 엘리먼트를 찾은 다음, 그 안에 있는 댓글 수정폼을 찾고 토글한다
     * @param event
     */
    #toggleEditComment = (event) => {
        event.preventDefault();
        const comment = event.target.closest(".comment");
        const commentWriter = comment.querySelector(".comment-editor");
        this.#toggleForm(commentWriter);
    };

    /**
     * 폼을 열고 닫는 토글 기능을 수행하는 메서드
     * 만약 기존에 열려있던 엘리먼트가 있는 상태에서 새로운 폼을 열려고 시도하면,
     * 기존 폼을 닫고나서 새로운 폼을 열어준다.
     * @param newForm 토글 기능을 수행할 새로운 폼
     */
    #toggleForm = (newForm) => {
        //닫혀있는 상태라면...
        if (this.#isClosed(newForm)) {
            //기존에 열려있던 폼을 닫은 다음,
            this.#closeCurrentlyOpenedForm();
            //새로운 폼을 열어준다
            this.#openForm(newForm);
            //currentlyOpenedForm을 새로운 폼으로 업데이트한다
            this.#currentlyOpenedForm = newForm;
        } else {//열려있는 상태라면...
            //닫아준다
            this.#closeForm(newForm);
        }
    };

    /**
     * 현재 열려있는
     */
    #closeCurrentlyOpenedForm = () => {
        //현재 열려있는 폼이 없으면 바로 리턴한다
        if (!this.#currentlyOpenedForm) {
            return;
        }
        //현재 열려있는 폼을 닫고 상태값을 null로 초기화한다
        this.#closeForm(this.#currentlyOpenedForm);
        this.#currentlyOpenedForm = null;
    };

    #openForm = (element) => {
        element.classList.remove("hide-comment-form");
    };

    #closeForm = (element) => {
        element.classList.add("hide-comment-form");
    };

    #isClosed = (element) => {
        return element.classList.contains("hide-comment-form");
    }


    /**
     * 댓글기능 초기화 메서드
     */
    init = () => {
        //댓글생성폼 바인딩
        const createCommentForms = document.querySelectorAll(".create-comment-form");
        for (const commentForm of createCommentForms) {
            commentForm.addEventListener("submit", this.create);
        }
        //대댓글 생성폼 토글기능 바인딩
        const toggleCommentWriterButtons = document.querySelectorAll(".toggle-comment-writer");
        for (const toggleCommentWriterButton of toggleCommentWriterButtons) {
            toggleCommentWriterButton.addEventListener("click", this.#toggleReplyComment);
        }
        //대댓글 생성폼 바인딩
        const replyCommentForms = document.querySelectorAll(".reply-comment-form");
        for (const replyCommentForm of replyCommentForms) {
            replyCommentForm.addEventListener("submit", this.reply);
        }
        //댓글삭제버튼 바인딩
        const deleteCommentButtons = document.querySelectorAll(".delete-comment-button");
        for (const deleteCommentButton of deleteCommentButtons) {
            deleteCommentButton.addEventListener("click", this.delete);
        }
        //댓글 수정폼 토글기능 바인딩
        const toggleCommentEditorButtons = document.querySelectorAll(".toggle-comment-editor");
        for (const toggleCommentEditorButton of toggleCommentEditorButtons) {
            toggleCommentEditorButton.addEventListener("click", this.#toggleEditComment);
        }
        //댓글 수정폼 바인딩
        const editCommentForms = document.querySelectorAll(".edit-comment-form");
        for (const editCommentForm of editCommentForms) {
            editCommentForm.addEventListener("submit", this.edit);
        }
    };
}
