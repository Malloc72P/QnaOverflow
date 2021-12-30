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

// Example starter JavaScript for disabling form submissions if there are invalid fields
(function () {
    'use strict'

    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.querySelectorAll('.needs-validation')

    // Loop over them and prevent submission
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }

                form.classList.add('was-validated')
            }, false)
        })
})()
