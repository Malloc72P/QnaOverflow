export class ApiHelper {
    static GET = "GET";
    static POST = "POST";
    static PATCH = "PATCH";
    static PUT = "PUT";
    static DELETE = "DELETE";

    static #commonHeader = {
        "Content-Type": "application/json",
        "Accept": "*/*",
        "Accept-Encoding": "gzip, deflate, br",
    };

    /**
     * API 요청을 단순화하기 위한 함수
     * @param uri 요청 URL
     * @param method 요청메서드
     * @param body 메세지 바디
     * @returns {Promise<string|any>} 프로미스, await하면 메세지 바디가 text 또는 json으로 파싱되어 반환된다
     */
    static request = async (uri, method, body) => {
        //요청메세지 생성
        let requestMessage = {
            method: method,
            headers: this.#commonHeader
        };
        //바디에 실어 보낼 내용이 있으면 JSON 포맷으로 실어넣는다
        if (body !== null) {
            requestMessage["body"] = JSON.stringify(body);
        }
        //fetch함수를 사용해서 API요청 실시
        let response = await fetch(serverAddress + uri, requestMessage);
        //응답메세지의 바디를 담을 변수
        let content = null;
        //응답메세지의 컨텐츠 타입에 따라서 어떤 방식으로 파싱할지 결정함
        switch (response.headers.get("content-type")) {
            case "application/json":
                content = await response.json();
                break;
            case "text/html;charset=UTF-8":
                content = await response.text();
                break;
        }
        //만약 응답메세지의 상태코드가 200이 아니라면 예외를 발생시킨다
        if (!response.ok) {
            throw content;
        }
        //200이라면 응답메세지의 바디를 반환한다
        return content;
    };

    /**
     * 에러가 발생한 경우 서버에서 보내준 에러메세지를 화면에 보여주는 메서드
     * @param error 화면에 보여줄 에러메세지를 담고있는 객체
     */
    static alertError = (error) => {
        if (error != null && error.description != null && error.description !== "") {
            //서버에서 응답해준 ErrorDTO 안의 description 메세지를 alert을 사용해서 보여줌
            alert(error.description);
        } else {
            alert("unknown error");
        }
    };
}

