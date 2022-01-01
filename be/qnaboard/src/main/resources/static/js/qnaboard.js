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
        default:
            throw new Error("unknown response type")
    }
};

const commonHeader = {
    "Content-Type": "application/json",
    "Accept": "*/*",
    "Accept-Encoding": "gzip, deflate, br",
};

const MODE_JSON = "JSON";
const MODE_TEXT = "TEXT";

const GET = "GET";
const POST = "POST";
const PATCH = "PATCH";
const PUT = "PUT";
const DELETE = "DELETE";

//main =========================================
let elements = document.querySelectorAll(".btn");
for (let element of elements) {
    element.addEventListener("pointerdown", () => handleBtnClickEffect(element));
}



