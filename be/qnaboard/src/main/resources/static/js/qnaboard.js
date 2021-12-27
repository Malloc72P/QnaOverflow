let elements = document.querySelectorAll(".btn");

/**
 * 부트스트랩 버튼의 애니메이션을 고치기 위한 함수. 포커스를 대상 엘리먼트에 부여하고, 150초 후에 풀어버린다.
 *
 * @param element 대상 엘리먼트
 */
const handleBtnClickEffect = (element) => {
    element.focus();
    setTimeout(() => {
        console.log("fired")
        element.blur();
    }, 150);
};

for (let element of elements) {
    element.addEventListener("pointerdown", () => handleBtnClickEffect(element));
}
