import {ApiHelper} from "../lib/apiHelper.js";

class QuestionList {

    #moreQuestionButton = null;
    #questionListSection = null;
    #domParser = new DOMParser();

    init = () => {
        this.#moreQuestionButton = document.getElementById("more-question-btn");
        this.#questionListSection = document.getElementById("question-list-section");

        //더보기 버튼이 있는 경우에만 리스너 바인딩
        this.#addEventListenerToMoreButton();
    }

    #getMoreQuestions = async () => {
        //페이지번호를 얻어서 질문목록조회의 URL을 생성
        const urlParams = new URLSearchParams(document.location.search)
        const searchInput = encodeURI(urlParams.get("searchInput"));
        const pageNumber = this.#extractPageNumber();
        const url = `/questions?searchInput=${searchInput}&pageNumber=${pageNumber}`
        //서버에 요청메세지 전송
        const response = await ApiHelper.request(url, ApiHelper.GET, null);
        //응답받은 데이터를 domParser로 파싱
        const newDocument = this.#domParser.parseFromString(response, "text/html");
        const questionContainer = newDocument.querySelector(".question-container");
        //새로운 더보기 버튼을 변수에 저장하고 이벤트 리스너 등록
        this.#setMoreButton(questionContainer);
        this.#addEventListenerToMoreButton();
        //DOM에 새로운 엘리먼트(새로운 질문목록)를 추가
        this.#questionListSection.appendChild(questionContainer);
    }

    //페이지 번호 추출
    #extractPageNumber = () => {
        return  this.#moreQuestionButton.dataset.pageno;
    };

    //새로운 더보기 버튼을 찾아서 변수에 저장
    #setMoreButton = (documentParam) => {
        if (this.#moreQuestionButton) {
            this.#moreQuestionButton.remove();
        }
        this.#moreQuestionButton = documentParam.querySelector("#more-question-btn");
    };

    //더보기 버튼에 이벤트리스너 등록
    #addEventListenerToMoreButton = () => {
        if (this.#moreQuestionButton) {
            this.#moreQuestionButton.addEventListener("click", this.#getMoreQuestions);
        }
    };
}

const questionList = new QuestionList();
questionList.init();
