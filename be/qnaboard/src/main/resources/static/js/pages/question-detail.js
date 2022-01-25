//답변게시글 처리
import {Answer} from "../lib/answer.js";
import {Comment} from "../lib/comment.js";
import {Vote} from "../lib/vote.js";

class QuestionDetail {
    #comment;
    #answer;
    #vote;

    constructor() {
        this.#comment = new Comment();
        this.#answer = new Answer();
        this.#vote = new Vote();
    }

    init = () => {
        this.#answer.init();
        this.#comment.init();
        this.#vote.init();
    }
}

const questionDetail = new QuestionDetail();
questionDetail.init();
