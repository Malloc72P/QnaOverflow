export class Vote {
    static #VOTE_UP = "UP";
    static #VOTE_DOWN = "DOWN";

    static vote = async (event) => {
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
            voteScore.innerText = prevScore + Vote.#valueByVoteType(voteType);
        } catch (error) {
            alertError(error);
        }
    };

    static #valueByVoteType = (voteType) => {
        switch (voteType) {
            case Vote.#VOTE_UP:
                return 1;
            case Vote.#VOTE_DOWN:
                return -1;
        }
        throw new Error("알 수 없는 투표유형입니다");
    }

    init = () => {
        const upVoteButtons = document.querySelectorAll(".up-vote-button");
        const downVoteButtons = document.querySelectorAll(".down-vote-button");

        //모든 추천버튼에 바인딩
        for (const upVoteButton of upVoteButtons) {
            upVoteButton.addEventListener("click", Vote.vote);
        }
        //모든 비추천버튼에 바인딩
        for (const downVoteButton of downVoteButtons) {
            downVoteButton.addEventListener("click", Vote.vote);
        }
    };
}
