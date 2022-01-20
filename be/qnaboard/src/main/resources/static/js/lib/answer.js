const createAnswer = async (event) => {
    event.preventDefault();
    let textAreaElement = document.getElementById("textarea-submit-answer");
    let content = textAreaElement.value;
    let questionId = textAreaElement.closest(".content-root").dataset.questionid;
    let url = `http://localhost:8080/questions/${questionId}/answers`;
    let body = {"content": content};

    try {
        let response = await request(url, POST, body);

        let parser = new DOMParser();
        let answerElementWrapper = parser.parseFromString(response, "text/html");
        let answerElement = answerElementWrapper.querySelector(".answer");

        answerElement.querySelector(".answer-delete").addEventListener("click", deleteAnswer);
        answerElement.querySelector(".answer-edit").addEventListener("click", toggleEditAnswerForm);
        answerElement.querySelector(".answer-edit-form").addEventListener("submit", editAnswer);
        answerElement.querySelector(".up-vote-button").addEventListener("pointerdown", vote);
        answerElement.querySelector(".down-vote-button").addEventListener("pointerdown", vote);

        let answerWrapper = document.getElementById("answer-wrapper");
        answerWrapper.appendChild(answerElement);

        increaseAnswerCount();
        textAreaElement.value = "";
    } catch (error) {
        alert("답변의 내용은 6자보다 길어야 합니다");
    }

};

const deleteAnswer = async (event) => {
    const section = event.target.closest("section");
    const questionId = section.querySelector(".question").id.substring(2);
    const answer = event.target.closest(".answer");
    const answerId = answer.id.substring(2);

    const url = `http://localhost:8080/questions/${questionId}/answers/${answerId}`;

    let body = {};

    try {
        const response = await request(url, DELETE, body);
        answer.remove();
        decreaseAnswerCount();

    } catch (error) {
        alert("자신이 작성한 답변만 삭제할 수 있습니다. 만약 당신이 관리자라면 서버 관리자에게 문의해주세요");
    }

};

const editAnswer = async (event) => {
    event.preventDefault();
    const section = event.target.closest("section");
    const questionId = section.querySelector(".question").id.substring(2);
    const answer = event.target.closest(".answer");
    const answerId = answer.id.substring(2);
    const content = answer.querySelector(".answer-edit-form")[0].value;

    const url = `http://localhost:8080/questions/${questionId}/answers/${answerId}`;

    let body = {
        "content": content
    };

    try {
        const response = await request(url, PATCH, body);
        const answerContent = answer.querySelector(".post-content-wrapper");
        answerContent.innerText = response.content;

        const lastModifiedDate = answer.querySelector(".post-controller .last-modified-date");
        lastModifiedDate.innerText = response.lastModifiedDate;

        answer.querySelector(".answer-edit").dispatchEvent(new Event("pointerdown"));
    } catch (error) {
        alert("자신이 작성한 답변만 삭제할 수 있습니다. 만약 당신이 관리자라면 서버 관리자에게 문의해주세요");
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
