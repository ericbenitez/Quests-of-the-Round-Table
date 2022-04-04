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

