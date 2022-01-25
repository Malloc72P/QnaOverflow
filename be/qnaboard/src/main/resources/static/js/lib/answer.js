import {Vote} from "./vote.js";
import {ApiHelper} from "./apiHelper.js";

export class Answer {
    #parser = new DOMParser();
    #createAnswerTextArea;//답변입력기의 textarea
    #questionId;//질문게시글 아이디
    #answerCount;//답변게시글 개수 엘리먼트
    #answerWrapper;//답변게시글 컨테이너
    #comment//댓글을 관리하는 객체

    constructor(comment) {
        this.#comment = comment;
    }

    /**
     * 답변 수정폼을 이벤트를 가지고 여닫는 메서드
     * @param event HTML 클릭 이벤트
     */
    static #toggleEditFormByEvent(event) {
        event.target.closest(".answer")
            .querySelector(".answer-edit-form")
            .classList
            .toggle("d-none");
    };

    static #extractAnswerId(answer) {
        return answer.id.substring(2);
    }

    /**
     * 답변기능 초기화 메서드. 여기서 질문 상세보기페이지를 위한 이벤트 바인딩을 함
     */
    init = () => {
        //모든 답변의 삭제버튼 바인딩
        const deleteAnswerButtons = document.querySelectorAll(".answer-delete");
        for (const deleteAnswerButton of deleteAnswerButtons) {
            deleteAnswerButton.addEventListener("click", this.delete);
        }
        //답변 생성버튼 바인딩
        const submitAnswerButton = document.getElementById("submit-answer-button");
        submitAnswerButton.addEventListener("click", this.create);
        //모든 답변의 수정버튼 바인딩
        const editAnswerButtons = document.querySelectorAll(".answer-edit");
        for (const button of editAnswerButtons) {
            button.addEventListener("click", Answer.#toggleEditFormByEvent);
        }
        //모든 답변의 수정폼 바인딩
        const editAnswerForms = document.querySelectorAll(".answer-edit-form");
        for (const form of editAnswerForms) {
            form.addEventListener("submit", this.edit);
        }
        //자주 찾는 엘리먼트 미리 찾기
        this.#createAnswerTextArea = document.getElementById("textarea-submit-answer");
        this.#questionId = this.#createAnswerTextArea.closest(".content-root").dataset.questionid;
        this.#answerCount = document.getElementById("answer-count");
        this.#answerWrapper = document.getElementById("answer-wrapper");
    }

    create = async (event) => {
        event.preventDefault();
        const content = this.#createAnswerTextArea.value;
        const url = `/questions/${this.#questionId}/answers`;
        const body = {"content": content};

        try {
            //필요한 엘리먼트 찾기
            const response = await ApiHelper.request(url, ApiHelper.POST, body);
            const answerElementWrapper = this.#parser.parseFromString(response, "text/html");
            const answerElement = answerElementWrapper.querySelector(".answer");
            //새로 생성된 엘리먼트에 이벤트리스너 바인딩
            this.#setAnswerEventListener(answerElement);
            //엘리먼트를 dom에 추가
            this.#appendAnswer(answerElement);
            //답변게시글 입력 필드 초기화
            this.#createAnswerTextArea.value = "";
        } catch (error) {
            ApiHelper.alertError(error);
        }
    };

    delete = async (event) => {
        const answer = Answer.#findAnswerFromEvent(event);
        const answerId = Answer.#extractAnswerId(answer)
        const url = `/questions/${this.#questionId}/answers/${answerId}`;
        try {
            await ApiHelper.request(url, ApiHelper.DELETE, {});
            answer.remove();
            this.#decreaseAnswerCount();
        } catch (error) {
            ApiHelper.alertError(error);
        }
    };

    edit = async (event) => {
        event.preventDefault();
        const answer = Answer.#findAnswerFromEvent(event);
        const answerId = Answer.#extractAnswerId(answer)
        const content = answer.querySelector(".answer-edit-form")[0].value;
        const url = `/questions/${this.#questionId}/answers/${answerId}`;
        const body = {
            "content": content
        };
        try {
            //api 요청
            const response = await ApiHelper.request(url, ApiHelper.PATCH, body);
            //답변의 내용을 담고 있는 엘리먼트 찾고 업데이트하기
            const answerContent = answer.querySelector(".post-content-wrapper");
            answerContent.innerText = response.content;
            //답변의 최근수정일 엘리먼트를 찾고 업데이트
            const lastModifiedDate = answer.querySelector(".post-controller .last-modified-date");
            lastModifiedDate.innerText = response.lastModifiedDate;
            //답변수정폼 토글(숨기기)
            Answer.#closeEditFormByAnswer(answer);
        } catch (error) {
            ApiHelper.alertError(error);
        }
    };

    #setAnswerEventListener = (answerElement) => {
        //수정 삭제기능
        answerElement.querySelector(".answer-delete").addEventListener("click", this.delete);
        answerElement.querySelector(".answer-edit-form").addEventListener("submit", this.edit);
        //답변 수정폼 토글기능
        answerElement.querySelector(".answer-edit").addEventListener("click", Answer.#toggleEditFormByEvent);
        //답변 투표기능
        answerElement.querySelector(".up-vote-button").addEventListener("click", Vote.vote);
        answerElement.querySelector(".down-vote-button").addEventListener("click", Vote.vote);
        //답변 댓글입력기능
        answerElement.querySelector(".create-comment-form").addEventListener("submit", this.#comment.create);
    }

    /**
     * 답변 수정폼을 답변게시글 HTML 엘리먼트를 가지고 닫는 메서드
     * @param answer 답변게시글 HTML 엘리먼트
     */
    static #closeEditFormByAnswer(answer) {
        answer.querySelector(".answer-edit-form").classList.add("d-none");
    }

    static #findAnswerFromEvent(event) {
        return event.target.closest(".answer");
    }

    #appendAnswer(answerElement) {
        //답변게시글 목록에 새로 생성한 답변게시글 추가
        this.#answerWrapper.appendChild(answerElement);
        //답변게시글 개수 증가
        this.#increaseAnswerCount();
    }

    #increaseAnswerCount() {
        this.#answerCount.innerText = parseInt(this.#answerCount.innerText) + 1;
    }

    #decreaseAnswerCount() {
        this.#answerCount.innerText = parseInt(this.#answerCount.innerText) - 1;
    }
}
