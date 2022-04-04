
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