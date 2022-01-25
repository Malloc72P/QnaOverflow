const createAnswer = async (event) => {
    event.preventDefault();
    const textAreaElement = document.getElementById("textarea-submit-answer");
    const content = textAreaElement.value;
    const questionId = textAreaElement.closest(".content-root").dataset.questionid;
    const url = `/questions/${questionId}/answers`;
    const body = {"content": content};

    try {
        let response = await request(url, POST, body);

        let parser = new DOMParser();
        let answerElementWrapper = parser.parseFromString(response, "text/html");
        let answerElement = answerElementWrapper.querySelector(".answer");

        answerElement.querySelector(".answer-delete").addEventListener("click", deleteAnswer);
        answerElement.querySelector(".answer-edit").addEventListener("click", toggleEditAnswerForm);
        answerElement.querySelector(".answer-edit-form").addEventListener("submit", editAnswer);
        answerElement.querySelector(".up-vote-button").addEventListener("click", vote);
        answerElement.querySelector(".down-vote-button").addEventListener("click", vote);

        let answerWrapper = document.getElementById("answer-wrapper");
        answerWrapper.appendChild(answerElement);

        increaseAnswerCount();
        textAreaElement.value = "";
    } catch (error) {
        alertError(error);
    }

};

const deleteAnswer = async (event) => {
    const section = event.target.closest("section");
    const questionId = section.querySelector(".question").id.substring(2);
    const answer = event.target.closest(".answer");
    const answerId = answer.id.substring(2);

    const url = `/questions/${questionId}/answers/${answerId}`;

    let body = {};

    try {
        await request(url, DELETE, body);
        answer.remove();
        decreaseAnswerCount();

    } catch (error) {
        alertError(error);
    }

};

const editAnswer = async (event) => {
    event.preventDefault();
    const section = event.target.closest("section");
    const questionId = section.querySelector(".question").id.substring(2);
    const answer = event.target.closest(".answer");
    const answerId = answer.id.substring(2);
    const content = answer.querySelector(".answer-edit-form")[0].value;

    const url = `/questions/${questionId}/answers/${answerId}`;

    let body = {
        "content": content
    };

    try {
        const response = await request(url, PATCH, body);
        const answerContent = answer.querySelector(".post-content-wrapper");
        answerContent.innerText = response.content;

        const lastModifiedDate = answer.querySelector(".post-controller .last-modified-date");
        lastModifiedDate.innerText = response.lastModifiedDate;

        // answer.querySelector(".answer-edit").dispatchEvent(new Event("click"));
        answer.querySelector(".answer-edit-form").classList.toggle("d-none");
    } catch (error) {
        alertError(error);
    }
};

const toggleEditAnswerForm = (event) => {
    event.target
        .closest(".answer")
        .querySelector(".answer-edit-form")
        .classList
        .toggle("d-none");
};

const increaseAnswerCount = () => {
    const answerCount = document.getElementById("answer-count");
    answerCount.innerText = parseInt(answerCount.innerText) + 1;
}

const decreaseAnswerCount = () => {
    const answerCount = document.getElementById("answer-count");
    answerCount.innerText = parseInt(answerCount.innerText) - 1;
}
