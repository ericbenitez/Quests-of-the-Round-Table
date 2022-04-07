
function disableAdvCard() {
    document.getElementById("adventureCards").style.pointerEvents = "none";
}

function enableAdvCard() {
    document.getElementById("adventureCards").style.pointerEvents = "auto";
}

function disableTransferQuestButton() {
    document.getElementById("transferQuest").disabled = true;
}

function enableTransferQuestButton() {
    document.getElementById("transferQuest").disabled = false;
}

function disableBidding() {
    let bidButton = document.getElementById("bidReadyButton");
    bidButton.disabled = true;
    bidButton.className = "disabledButton";
}

function enableBidding() {
    if (playerHand.length < 12) {
        getAdventureCards()
    }
    
    let bidButton = document.getElementById("bidReadyButton");
    bidButton.disabled = false;
    bidButton.className = "";
}
function disableFinishTurnAfterClick(btn) {
    document.getElementById(btn.id).disabled = true;
}

function disableFinishTurn() {
    document.getElementById("finishTurn").disabled = true;
}

function enableFinishTurn() {
    document.getElementById("finishTurn").disabled = false;
}

//Sponsor Quest

function disableSponsorQuestAfterClick(btn) {
    document.getElementById(btn.id).disabled = true;
}

function disableSponsorQuest() {
    document.getElementById("sponserQuest").disabled = true;
}

function enableSponsorQuest() {
    document.getElementById("sponserQuest").disabled = false;
}

//Place cards for stage

function disableStageCardsAfterClick(btn) {
    document.getElementById(btn.id).disabled = true;
}

function disableStageCards() {
    document.getElementById("placeCardsQuest").disabled = true;
}

function enableStageCards() {
    document.getElementById("placeCardsQuest").disabled = false;
}

function disablePickStoryCard() {
    document.getElementById("storyCards").style.pointerEvents = "none";
}

function enablePickStoryCard() {
    document.getElementById("storyCards").style.pointerEvents = "auto";
}

//Join Quest
function disableJoinQuest() {
    document.getElementById("joinQuest").disabled = true;
}

function enableJoinQuest() {
    document.getElementById("joinQuest").disabled = false;
}

function disableButtons() {
    disableBidding();
    disableFinishTurn();
    disableJoinQuest();
    disablePickStoryCard();
    disableSponsorQuest();
    disableStageCards();
    disableTransferQuestButton();
    disableAdvCard();
}

function newRound() {
    enableFinishTurn();
    enablePickStoryCard();
    disableJoinQuest();
    disableStageCards();
    disableBidding()
    disableAdvCard();
    disableTransferQuestButton();
    disableSponsorQuest();
}

function tournamentParticBtns() {
    enableFinishTurn();
    disablePickStoryCard();
    disableJoinQuest();
    disableStageCards();
    enableBidding();
    disableTransferQuestButton();
    disableSponsorQuest();
    enableAdvCard();
}

//these people have already joined the quest, n
function questParticBtns() {
    enableJoinQuest();
    enableFinishTurn();
    enableStageCards();
    disableSponsorQuest();
    disableTransferQuestButton();
    disablePickStoryCard();
    enableAdvCard();
}

function eventParticBtns() {

}

function newQuestJoiners() {
    enableJoinQuest();
    enableFinishTurn();
    enableStageCards();
    disableSponsorQuest();
    disableTransferQuestButton();
    disablePickStoryCard();
    enableAdvCard();
    disableBidding();
}

function alreadyASponsor() {
    enableFinishTurn();
    disablePickStoryCard();
    disableJoinQuest();
    disableStageCards();
    disableBidding()
    disableTransferQuestButton();
    disableSponsorQuest();
    enableAdvCard();
}


function hideJoinGame(){
    document.getElementById("new-player-join").style.display = "none";
}

function showJoinGame(){
    document.getElementById("new-player-join").style.display = "block";
}