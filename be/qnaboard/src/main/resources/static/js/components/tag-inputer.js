const tagInput = document.getElementById("tag-input");
const tagSuggestions = document.getElementById("tag-suggestions");
const selectedTags = document.getElementById("selected-tag-wrapper");
const selectedTagIdList = document.getElementById("selected-tag-id-list");
const tagSearchButton = document.getElementById("tag-search-button");

let prevSearchTagValue = "";
const selectedTagMap = new Map();

const getTagIdListAsString = () => {
    const keys = selectedTagMap.keys();
    const tagIdArray = Array.from(keys);
    if (tagIdArray.length === 0) {
        return "";
    }
    return tagIdArray
        .reduce((prev, curr) => prev + "," + curr);
}

const refreshTagIds = () => {
    selectedTagIdList.value = getTagIdListAsString();
}

const addSuggestion = (id, name) => {
    const domParser = new DOMParser();
    const htmlButtonElement = domParser.parseFromString(
        `<a class="btn btn-sm btn-secondary me-1 mb-1" href="javascript:" data-tagid="${id}">${name}</a>`,
        "text/html").querySelector("a");
    htmlButtonElement.addEventListener("pointerdown", onSelectTag);
    tagSuggestions.appendChild(htmlButtonElement);
}

const selectTag = (tagId, tagName) => {
    tagId = Number.parseInt(tagId);
    if (selectedTagMap.has(tagId)) {
        return;
    }

    const domParser = new DOMParser();
    const tagElement = domParser.parseFromString(
        `<span class="btn btn-secondary btn-sm disable-btn-secondary-hover me-1 mb-1" 
                     data-tagid="${tagId}" data-tagname=${tagName}>
                    <a class="text-decoration-none text-white tag-name" href="javascript:" role="button">${tagName}</a>
                    <a class="text-decoration-none text-white hover-bold tag-delete" href="javascript:" role="button">❌</a>
               </span>`, "text/html").querySelector("span");
    tagElement.querySelector(".tag-delete").addEventListener("pointerdown", removeTag);
    selectedTags.appendChild(tagElement);

    selectedTagMap.set(tagId, {"name": tagName, "tagElement": tagElement});
    refreshTagIds();
}

const onSelectTag = (event) => {
    const tagId = event.target.dataset.tagid;
    const tagName = event.target.innerText;

    selectTag(tagId, tagName);
}

const removeTag = (event) => {
    const container = event.target.closest("span");
    const tagId = Number.parseInt(container.dataset.tagid);

    if (!selectedTagMap.has(tagId)) {
        return
    }

    const tagDataSet = selectedTagMap.get(tagId);
    tagDataSet.tagElement.remove();
    selectedTagMap.delete(tagId);
    refreshTagIds();
}

const searchTag = async (event) => {
    event.preventDefault();
    const keyword = tagInput.value;

    if (keyword === '' || keyword === prevSearchTagValue) {
        return;
    }

    const url = `http://localhost:8080/api/tags?keyword=${keyword}`;

    try {
        let response = await request(url, GET, null);
        tagSuggestions.textContent = "";
        for (const tag of response.tags) {
            addSuggestion(tag.id, tag.name);
        }
        prevSearchTagValue = keyword;
    } catch (error) {
        alertError(error);
    }


};

const onTagInputKeyDown = (event) => {
    if (event.keyCode !== 13) {
        return;
    }
    searchTag(event);
}

const onTagSearchButton = (event) => {
    event.preventDefault();
    searchTag(event);
};

tagInput.addEventListener("keydown", onTagInputKeyDown);
tagSearchButton.addEventListener("pointerdown", onTagSearchButton);

if (tagParam && tagParam.length > 0) {
    for (const tag of tagParam) {
        selectTag(tag.id, tag.name);
    }
}
