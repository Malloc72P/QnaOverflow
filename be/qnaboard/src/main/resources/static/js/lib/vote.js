const VOTE_UP = "UP";
const VOTE_DOWN = "DOWN";

const vote = async (event) => {
    event.preventDefault();
    const voteButton = event.target;
    const voteWrapper = voteButton.closest(".vote-wrapper");
    const voteScore = voteWrapper.querySelector(".vote-score");

    const prevScore = Number.parseInt(voteScore.innerText);
    const postId = voteButton.closest(".post").dataset.postid;
    const voteType = voteButton.dataset.votetype;
    const url = `http://localhost:8080/posts/${postId}/votes?voteType=${voteType}`;

    try {
        await request(url, PUT, null, MODE_NONE);
        voteScore.innerText = prevScore + valueByVoteType(voteType);
    } catch (error) {
        const errorMessage = errorMessageByVoteType(voteType);
        alert(errorMessage);
    }
};

const valueByVoteType = (voteType) => {
    switch (voteType) {
        case VOTE_UP:
            return 1;
        case VOTE_DOWN:
            return -1;
    }
    throw new Error("알 수 없는 투표유형입니다");
}

const errorMessageByVoteType = (voteType) => {
    let message = ""
    switch (voteType) {
        case VOTE_UP:
            message = "도움이 돼요";
            break;
        case VOTE_DOWN:
            message = "별로예요";
            break;
        default :
            throw new Error("알 수 없는 투표유형입니다");
    }
    return `이미 ${message}로 투표하셨습니다`
}


const upVoteButtons = document.querySelectorAll(".up-vote-button");
const downVoteButtons = document.querySelectorAll(".down-vote-button");

for (const upVoteButton of upVoteButtons) {
    upVoteButton.addEventListener("pointerdown", vote);
}
for (const downVoteButton of downVoteButtons) {
    downVoteButton.addEventListener("pointerdown", vote);
}
