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
 * API 요청을 단순화하기 위한 함수
 * @param url 요청 URL
 * @param method 요청메서드
 * @param body 메세지 바디
 * @param responseType 응답 타입(html은 MODE_TEXT, json은 MODE_JSON)
 * @returns {Promise<string|any>} 프로미스, await하면 메세지 바디가 text 또는 json으로 파싱되어 반환된다
 */
const request = async (url, method, body, responseType) => {
    let requestMessage = {
        method: method,
        headers: commonHeader
    };
    if (body !== null) {
        requestMessage["body"] = JSON.stringify(body);
    }

    let response = await fetch(url, requestMessage);
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

