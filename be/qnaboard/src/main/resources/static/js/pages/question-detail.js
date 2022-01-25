//답변게시글 처리
import {Answer} from "../lib/answer.js";
import {Comment} from "../lib/comment.js";

class QuestionDetail {

    #comment;
    #answer;

    constructor() {
        this.#comment = new Comment();
        this.#answer = new Answer(this.#comment);
    }

    init = () => {
        this.#answer.init();
        this.#comment.init();
    }
}

const questionDetail = new QuestionDetail();
questionDetail.init();
