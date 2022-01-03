const deleteAnswer = async (event) => {
    const section = event.target.closest("section");
    const questionId = section.querySelector(".question").id.substring(2);
    const answer = event.target.closest(".answer");
    const answerId = answer.id.substring(2);

    const url = `http://localhost:8080/${questionId}/answers/${answerId}`;

    let body = {};

    try {
        const response = await request(url, DELETE, body, MODE_NONE);
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

    const url = `http://localhost:8080/${questionId}/answers/${answerId}`;

    let body = {
        "content": content
    };

    try {
        const response = await request(url, PATCH, body, MODE_JSON);
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
