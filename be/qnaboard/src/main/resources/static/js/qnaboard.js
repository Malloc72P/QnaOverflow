const MODE_JSON = "JSON";
const MODE_TEXT = "TEXT";
const MODE_NONE = "NONE";

const GET = "GET";
const POST = "POST";
const PATCH = "PATCH";
const PUT = "PUT";
const DELETE = "DELETE";

const commonHeader = {
    "Content-Type": "application/json",
    "Accept": "*/*",
    "Accept-Encoding": "gzip, deflate, br",
};


/**
 * 부트스트랩 버튼의 애니메이션을 고치기 위한 함수. 포커스를 대상 엘리먼트에 부여하고, 150초 후에 풀어버린다.
 *
 * @param element 대상 엘리먼트
 */
const handleBtnClickEffect = (element) => {
    element.focus();
    setTimeout(() => {
        element.blur();
    }, 150);
};

/**
 * API 요청을 단순화하기 위한 함수
 * @param url 요청 URL
 * @param method 요청메서드
 * @param body 메세지 바디
 * @param responseType 응답 타입(html은 MODE_TEXT, json은 MODE_JSON)
 * @returns {Promise<string|any>} 프로미스, await하면 메세지 바디가 text 또는 json으로 파싱되어 반환된다
 */
const request = async (url, method, body, responseType) => {
    let response = await fetch(url, {
        method: method,
        headers: commonHeader,
        body: JSON.stringify(body)
    });
    if (!response.ok) {
        throw new Error(response.statusText);

    }
    switch (responseType) {
        case MODE_JSON:
            return await response.json();
        case MODE_TEXT:
            return await response.text()
        case MODE_NONE:
            return response;
        default:
            throw new Error("unknown response type")
    }

};

/**
 * 질문삭제요청
 * @param event 버튼 이벤트
 * @returns {Promise<void>} 삭제요청의 완료에 대한 프로미스
 */
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
        "content" : content
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


//main =========================================
const elements = document.querySelectorAll(".btn");
for (const element of elements) {
    element.addEventListener("pointerdown", () => handleBtnClickEffect(element));
}

const deleteAnswerButtons = document.querySelectorAll(".answer-delete");

for (const deleteAnswerButton of deleteAnswerButtons) {
    deleteAnswerButton.addEventListener("pointerdown", deleteAnswer);
}

const editAnswerButtons = document.querySelectorAll(".answer-edit");
const editAnswerForms = document.querySelectorAll(".answer-edit-form");

for (const button of editAnswerButtons) {
    button.addEventListener("pointerdown", toggleEditAnswerForm);
}

for (const form of editAnswerForms) {
    form.addEventListener("submit", editAnswer);
}



