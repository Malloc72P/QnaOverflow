import {Tag} from "../lib/tag.js";

class TagInputer {
    #tag;

    constructor(tagParam) {
        this.#tag = new Tag(tagParam);
    }

    init = () => {
        this.#tag.init();
    };
}

const tagInputer = new TagInputer(tagParam);
tagInputer.init();
