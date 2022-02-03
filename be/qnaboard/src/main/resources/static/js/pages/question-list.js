import {Question} from "../lib/question.js";
import {ApiHelper} from "../lib/apiHelper.js";

class QuestionList {

    #moreQuestionButton = null;
    #questionListSection = null;
    #question;

    constructor() {
        this.#question = new Question();
    }

    init = () => {
        this.#moreQuestionButton = document.getElementById("more-question-btn");
        this.#questionListSection = document.getElementById("question-list-section");

        //더보기 버튼이 있는 경우에만 리스너 바인딩
        if (this.#moreQuestionButton) {
            this.#moreQuestionButton.addEventListener("click", this.#getMoreQuestions);
        }
    }

    #getMoreQuestions = async () => {
        const pageNumber = this.#moreQuestionButton.dataset.pageno;
        const url = `/questions?pageNumber=${pageNumber}`
        const response = await ApiHelper.request(url, ApiHelper.GET, null);
        const domParser = new DOMParser();
        const newDocument = domParser.parseFromString(response, "text/html");
        const questionContainer = newDocument.querySelector(".question-container");

        this.#moreQuestionButton.remove()
        this.#moreQuestionButton = questionContainer.querySelector("#more-question-btn");

        if (this.#moreQuestionButton) {
            this.#moreQuestionButton.addEventListener("click", this.#getMoreQuestions);
        }

        this.#questionListSection.appendChild(questionContainer);
    }
}

const questionList = new QuestionList();
questionList.init();
