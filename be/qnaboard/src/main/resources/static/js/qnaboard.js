let elements = document.querySelectorAll(".btn");

const handleBtnClickEffect = (element) => {
    element.focus();
    setTimeout(() => {
        console.log("fired")
        element.blur();
        element.classList.remove("hasactive");
    }, 150);
};

for (let element of elements) {
    element.addEventListener("pointerdown", () => handleBtnClickEffect(element));
}
