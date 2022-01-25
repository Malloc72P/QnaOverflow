import {ApiHelper} from "./apiHelper.js";

export class Tag {
    //태그 파라미터(게시글에 이미 등록된 태그처리를 위해 존재함)
    #tagParam
    //태그 처리에 필요한 HTML 엘리먼트
    #tagInput;
    #tagSuggestions;
    #selectedTags;
    #selectedTagIdList;
    #tagSearchButton;
    //검색하기 이전의 태그검색필드의값
    #prevSearchTagValue;
    //현재 선택된 태그에 대한 정보를 가지고 있는 맵
    #selectedTagMap;

    constructor(tagParam) {
        this.#tagInput = document.getElementById("tag-input");
        this.#tagSuggestions = document.getElementById("tag-suggestions");
        this.#selectedTags = document.getElementById("selected-tag-wrapper");
        this.#selectedTagIdList = document.getElementById("selected-tag-id-list");
        this.#tagSearchButton = document.getElementById("tag-search-button");
        this.#prevSearchTagValue = "";
        this.#selectedTagMap = new Map();
        this.#tagParam = tagParam;
    }

    /**
     * 태그입력기능 초기화 메서드
     */
    init = () => {
        //엔터키누르면 태그검색을 할 수 있도록 키입력 이벤트에 바인딩함
        this.#tagInput.addEventListener("keydown", this.#onTagInputKeyDown);
        //태그검색버튼에 바인딩
        this.#tagSearchButton.addEventListener("click", this.#onTagSearchButton);
        //tag-inputer.html에서 타임리프를 사용해서 넣어준 태그 정보를 불러와서 초기화함
        if (this.#tagParam && this.#tagParam.length > 0) {
            for (const tag of this.#tagParam) {
                //이미 선택된 태그를 등록함
                this.#selectTag(tag.id, tag.name);
            }
        }
    };

    /**
     * 태그검색창에 엔터키가 눌리면 태그를 검색하는 메서드
     * @param event 키 입력 이벤트
     * @returns {Promise<void>}
     */
    #onTagInputKeyDown = async (event) => {
        if (event.keyCode !== 13) {
            return;
        }
        await this.#searchTag(event);
    }

    /**
     * 태그검색버튼이 눌리면 태그를 검색하는 메서드
     * @param event
     * @returns {Promise<void>}
     */
    #onTagSearchButton = async (event) => {
        event.preventDefault();
        await this.#searchTag(event);
    };

    /**
     * API로 태그를 검색하는 메서드.
     * 검색된 태그를 화면에 보여주기 위해 addSuggestion()메서드를 사용한다
     * @param event
     * @returns {Promise<void>}
     */
    #searchTag = async (event) => {
        event.preventDefault();
        const keyword = this.#tagInput.value;
        //빈 문자열이거나 이전의 검색키워드에서 바뀐게 없다면 API를 호출하지 않는다
        if (keyword === '' || keyword === this.#prevSearchTagValue) {
            return;
        }
        const url = `/api/tags?keyword=${keyword}`;
        try {
            let response = await ApiHelper.request(url, ApiHelper.GET, null);
            //태그 추천 목록을 제거한다
            this.#tagSuggestions.textContent = "";
            //새로 검색된 태그목록을 추천 목록에 등록한다
            for (const tag of response.tags) {
                this.#addSuggestion(tag.id, tag.name);
            }
            //이번 검색에 사용된 키워드를 prevSearchTagValue에 저장한다(이전검색키워드 업데이트)
            this.#prevSearchTagValue = keyword;
        } catch (error) {
            ApiHelper.alertError(error);
        }
    };

    /**
     * 추천태그를 등록한다.
     * API의 검색결과를 화면에 뿌리기 위해서 호출한다
     * @param id 태그 아이디
     * @param name 태그 이름
     */
    #addSuggestion = (id, name) => {
        const domParser = new DOMParser();
        const htmlButtonElement = domParser.parseFromString(
            `<a class="btn btn-sm btn-secondary me-1 mb-1" href="javascript:" data-tagid="${id}">${name}</a>`,
            "text/html").querySelector("a");
        htmlButtonElement.addEventListener("click", this.#onSelectTag);
        this.#tagSuggestions.appendChild(htmlButtonElement);
    }

    /**
     * 추천태그의 클릭 이벤트에 등록되는 메서드.
     * selectTag()메서드를 호출해서 클릭된 추천태그를 선택된 태그목록에 등록한다
     * @param event 클릭 이벤트
     */
    #onSelectTag = (event) => {
        const tagId = event.target.dataset.tagid;
        const tagName = event.target.innerText;
        //추천태그를 선택된 태그 목록에 등록함
        this.#selectTag(tagId, tagName);
    }


    /**
     * 태그 아이디 목록을 문자열로 변환해서 반환한다.
     * 1,2,3,4 이런 식으로 만들어서 반환한다.
     * 선택된 태그목록을 서버에 전송하기 위해서 사용한다.
     * @returns {string|unknown}
     */
    #getTagIdListAsString = () => {
        const keys = this.#selectedTagMap.keys();
        const tagIdArray = Array.from(keys);
        if (tagIdArray.length === 0) {
            return "";
        }
        return tagIdArray
            .reduce((prev, curr) => prev + "," + curr);
    }

    /**
     * 선택된 태그목록에 대한 데이터를 업데이트하는 메서드.
     * 태그를 선택할때마다 호출해주면 된다
     */
    #refreshTagIds = () => {
        this.#selectedTagIdList.value = this.#getTagIdListAsString();
    }

    /**
     * 태그를 선택된 태그 목록에 등록하는 메서드.
     * 게시글을 등록하거나 수정할 때 선택된 태그목록도 전송된다.
     * @param tagId 선택된 태그의 아이디
     * @param tagName 태그 이름
     */
    #selectTag = (tagId, tagName) => {
        //혹시나 문자열 아이디라서 맵에서 못찾을 수 있으니 숫자로 변환한다
        tagId = Number.parseInt(tagId);
        //이미 선택된 태그라면 여기서 리턴해서 추가동작을 하지 못하게 한다
        if (this.#selectedTagMap.has(tagId)) {
            return;
        }
        //선택된 태그 목록에 태그를 추가하기 위해서 domParser로 태그의 엘리먼트를 만들고 붙인다(append)
        const domParser = new DOMParser();
        const tagElement = domParser.parseFromString(
            `<span class="btn btn-secondary btn-sm disable-btn-secondary-hover me-1 mb-1" 
                         data-tagid="${tagId}" data-tagname=${tagName}>
                        <a class="text-decoration-none text-white tag-name" href="javascript:" role="button">${tagName}</a>
                        <a class="text-decoration-none text-white hover-bold tag-delete" href="javascript:" role="button">❌</a>
                   </span>`, "text/html").querySelector("span");
        tagElement.querySelector(".tag-delete").addEventListener("click", this.#removeTag);
        this.#selectedTags.appendChild(tagElement);
        //해당 태그가 중복해서 선택되는 문제를 방지하기 위해 맵에 추가한다
        this.#selectedTagMap.set(tagId, {"name": tagName, "tagElement": tagElement});
        //현재 선택된 태그정보를 최신화한다
        this.#refreshTagIds();
    }

    /**
     * 선택된 태그 목록에서 지우는 메서드
     * @param event
     */
    #removeTag = (event) => {
        const container = event.target.closest("span");
        const tagId = Number.parseInt(container.dataset.tagid);
        //선택된 태그목록에 존재하는지 확인한다. 없으면 제거작업을 중단한다
        if (!this.#selectedTagMap.has(tagId)) {
            return
        }
        //지우려는 태그를 맵에서 찾고, 그 안에 저장된 엘리먼트를 꺼낸 다음, 태그를 지운다.
        const tagDataSet = this.#selectedTagMap.get(tagId);
        tagDataSet.tagElement.remove();
        //맵에서도 지워준다
        this.#selectedTagMap.delete(tagId);
        //선택된 태그목록정보를 업데이트한다.
        this.#refreshTagIds();
    }
}
