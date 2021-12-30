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

//main =========================================
let elements = document.querySelectorAll(".btn");
for (let element of elements) {
    element.addEventListener("pointerdown", () => handleBtnClickEffect(element));
}


