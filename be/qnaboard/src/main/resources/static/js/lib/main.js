//맨 아래에서 Main 클래스 생성해서 호출함
class Main {

    init = () => {
        //버튼의 애니메이션을 개선하는 로직
        const elements = document.querySelectorAll(".btn");
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
