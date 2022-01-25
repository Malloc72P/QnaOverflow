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
    const url = `/api/posts/${postId}/votes?voteType=${voteType}`;

    try {
        await request(url, PUT, null);
        voteScore.innerText = prevScore + valueByVoteType(voteType);
    } catch (error) {
        alertError(error);
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

const upVoteButtons = document.querySelectorAll(".up-vote-button");
const downVoteButtons = document.querySelectorAll(".down-vote-button");

for (const upVoteButton of upVoteButtons) {
    upVoteButton.addEventListener("click", vote);
}
for (const downVoteButton of downVoteButtons) {
    downVoteButton.addEventListener("click", vote);
}
