
function disabledAdvCard(){
    document.getElementById("adventureCards").style.pointerEvents = "none";
}

function enableAdvCard(){
    document.getElementById("adventureCards").style.pointerEvents = "auto";
}

function disableTransferQuestButton(){
    document.getElementById("transferQuest").disabled = true;
}

function enableTransferQuestButton(){
    document.getElementById("transferQuest").disabled = false;
}

function disableBidding(){
    let bidButton = document.getElementById("bidReadyButton");
    bidButton.disabled = true;
    bidButton.className = "disabledButton";
}

function enableBidding(){
    let bidButton = document.getElementById("bidReadyButton");
    bidButton.disabled = false;
    bidButton.className = "";
}
function disableFinishTurnAfterClick(btn) {
    document.getElementById(btn.id).disabled = true;
}

function disableFinishTurn(){
    document.getElementById("finishTurn").disabled = true;
}

function enableFinishTurn(){
    document.getElementById("finishTurn").disabled = false;
}

//Sponsor Quest

function disableSponsorQuestAfterClick(btn) {
    document.getElementById(btn.id).disabled = true;
}

function disableSponsorQuest(){
    document.getElementById("sponserQuest").disabled = true;
}

function enableSponsorQuest(){
    document.getElementById("sponserQuest").disabled = false;
}

//Place cards for stage

function disableStageCardsAfterClick(btn) {
    document.getElementById(btn.id).disabled = true;
}

function disableStageCards(){
    document.getElementById("placeCardsQuest").disabled = true;
}

function enableStageCards(){
    document.getElementById("placeCardsQuest").disabled = false;
}

