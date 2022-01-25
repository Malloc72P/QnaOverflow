//맨 아래에서 Main 클래스 생성해서 호출함
class Main {

    init = () => {
        //버튼의 애니메이션을 개선하는 로직
        const elements = document.querySelectorAll(".navbar-toggler, .btn");
        for (const element of elements) {
            element.addEventListener("click", () => this.#handleBtnClickEffect(element));
        }
        //사이드바 active 처리
        this.#initSidebar();
        //검색창 힌트 패널 처리
        this.#initSearchBarHint();
    };

    /**
     * 검색창 힌트 패널 처리
     */
    #initSearchBarHint = () => {
        //검색창에 focus가 생기면 검색 힌트를 보여주도록 설정
        const searchInput = document.getElementById("question-search-input");
        const searchHint = document.getElementById("search-question-hint");
        //검색힌트를 눌러도 힌트가 사라지지 않도록 설정
        searchHint.addEventListener("mousedown", (event) => event.preventDefault());
        searchHint.addEventListener("pointerdown", (event) => event.preventDefault());
        //검색힌트를 보여주거나 숨기는 이벤트리스너 등록
        searchInput.addEventListener("focusin", () => {
            searchHint.classList.remove("hide-search-hint");
        });
        searchInput.addEventListener("focusout", () => {
            searchHint.classList.add("hide-search-hint");
        });
    };

    /**
     * 사이드바 active 처리
     */
    #initSidebar = () => {
        const sidebarLinks = document.querySelectorAll(".sidebar-link");
        const pathname = document.location.pathname;
        let isSelected = false;
        //링크의 주소와 페이지의 현재주소가 같으면 active클래스를 부여함
        for (const sidebarLink of sidebarLinks) {
            const elementPathname = sidebarLink.pathname;
            if (pathname === elementPathname) {
                sidebarLink.classList.add("active");
                isSelected = true;
                break;
            }
        }
        //경로가 루트면 첫번째 아이템에 active를 부여함
        if (pathname === "/") {
            sidebarLinks[0].classList.add("active");
        }
    };

    /**
     * 부트스트랩 버튼의 애니메이션을 고치기 위한 함수. 포커스를 대상 엘리먼트에 부여하고, 150초 후에 풀어버린다.
     * @param element 대상 엘리먼트
     */
    #handleBtnClickEffect = (element) => {
        element.focus();
        setTimeout(() => {
            element.blur();
        }, 150);
    };
}

const main = new Main();
main.init();

export class Toggler {

    currentlyOpenedForm = null;

    /**
     * 폼을 열고 닫는 토글 기능을 수행하는 메서드
     * 만약 기존에 열려있던 엘리먼트가 있는 상태에서 새로운 폼을 열려고 시도하면,
     * 기존 폼을 닫고나서 새로운 폼을 열어준다.
     * @param newForm 토글 기능을 수행할 새로운 폼
     */
    toggleForm = (newForm) => {
        //닫혀있는 상태라면...
        if (this.isClosed(newForm)) {
            //기존에 열려있던 폼을 닫은 다음,
            this.closeCurrentlyOpenedForm();
            //새로운 폼을 열어준다
            this.openForm(newForm);
            //currentlyOpenedForm을 새로운 폼으로 업데이트한다
            this.currentlyOpenedForm = newForm;
        } else {//열려있는 상태라면...
            //닫아준다
            this.closeForm(newForm);
        }
    };

    openForm = (element) => {
        element.classList.remove("hide-form");
    };

    closeForm = (element) => {
        element.classList.add("hide-form");
    };

    isClosed = (element) => {
        return element.classList.contains("hide-form");
    }

    /**
     * 현재 열려있는
     */
    closeCurrentlyOpenedForm = () => {
        //현재 열려있는 폼이 없으면 바로 리턴한다
        if (!this.currentlyOpenedForm) {
            return;
        }
        //현재 열려있는 폼을 닫고 상태값을 null로 초기화한다
        this.closeForm(this.currentlyOpenedForm);
        this.currentlyOpenedForm = null;
    };
}
