let tempPlayerNum = 1;
let currCardsFacedDown = []

window.addEventListener("load", function () {
    document.getElementById("faceUpButton").addEventListener("click", addFaceUp);
});
window.addEventListener("load", function () {
    document.getElementById("faceDownButton").addEventListener("click", addFaceDown);
});

// automatically display cards to test the functionality
// window.addEventListener("load", displayAllCards(theCards));


function addFaceUp() {
    checked = getAllChecked();
    removeAllCheckedCards(checked);
    placeCards(checked);
    disableCardButtons();
}

function addFaceDown() {
    checked = getAllChecked();
    removeAllCheckedCards(checked);
    currCardsFacedDown = checked;
    if (checked == []) {
        disableCardButtons();
        return;
    }

    let cardAtPlay = document.getElementById("stages");
    let div = document.createElement("div");
    //div.id = "cardsDown";
    div.setAttribute('id', 'cardsDown');
    div.appendChild(document.createTextNode("P" + tempPlayerNum));
    cardAtPlay.appendChild(div);
    document.getElementById("cardsDown").addEventListener("click", turnCardsOver);

    disableCardButtons();
}


// Display the cards
function displayAllCards(cards) {
    // loop through the cards and call the
    // function addACardDisplay()
    // alert("cards to display: " + theCards);
    //alert("cards total: " + theCards.length);
    for (let i = 0; i < cards.length; i++) {
        console.log("adding card to display: " + cards[i].name);
        addACardDisplay(cards[i].name, "playerHand", true);
    }
}



// create and add a checkbox "card"
// should take in a card name or something
function addACardDisplay(cardName, divName, haveCheckBox) {
    let cards = document.getElementById(divName);

    // create div to contain checkbox and label
    let div = document.createElement("div");

    // make checkbox
    let checkBox = document.createElement("INPUT");
    checkBox.setAttribute("type", "checkbox");
    checkBox.setAttribute("name", cardName);
    checkBox.setAttribute('class', 'cardCheckBox')
    checkBox.setAttribute('onclick', "updateButtonDisplay()");

    // make label
    let label = document.createElement("LABEL");
    label.setAttribute("for", cardName);
    let text = document.createTextNode(cardName);
    label.appendChild(text);

    // update div and add it

    if (haveCheckBox){
        div.appendChild(checkBox);
    }
    div.appendChild(label);
    if (!haveCheckBox){div.appendChild(document.createElement("br"));}
    cards.appendChild(div);

}


// enables or disables buttons based on whether any checkboxes are checked
function updateButtonDisplay() {

    let checkBoxDiv = document.getElementById("playerHand").children;
    // loop through all checkboxes and check if any are checked
    for (let i = 0; i < checkBoxDiv.length; i++) {
        if (checkBoxDiv[i].firstChild.checked) {
            enableCardButtons();
            return;
        }
    }
    disableCardButtons();
}


function enableCardButtons() {
    document.getElementById("faceUpButton").disabled = false;
    document.getElementById("faceUpButton").className = "enabledButton";
    document.getElementById("faceDownButton").disabled = false;
    document.getElementById("faceDownButton").className = "enabledButton";
}


function disableCardButtons() {
    document.getElementById("faceUpButton").disabled = true;
    document.getElementById("faceUpButton").className = "disabledButton";
    document.getElementById("faceDownButton").disabled = true;
    document.getElementById("faceDownButton").className = "disabledButton";
}



// loop through all the cards in the DOM, and get the cards that are checked
function getAllChecked() {
    let checked = [];
    // loop through each child and look at the checkBox to see if it's checked
    let cards = document.getElementById("playerHand").children;

    // start at 1 since the first child is just text "Your Cards" (nvm for now)
    for (let i = 0; i < cards.length; i++) {
        if (cards[i].firstChild.checked) {
            checked.push(cards[i].firstChild.name);
        }
    }
    return checked;

}

function getAllUnchecked() {
    let unChecked = [];
    let cards = document.getElementById("playerHand").children;

    // start at 1 since the first child is just text "Your Cards" (nvm for now)
    for (let i = 0; i < cards.length; i++) {
        if (!cards[i].firstChild.checked) {
            unChecked.push(cards[i].firstChild.name);
        }
    }
    return unChecked;

}


function removeAllCheckedCards(checked) {
    for (let i = 0; i < checked.length; i++) {
        removeCardFromDisplay(checked[i]);

    }
}

// ensures checked boxes are unchecked
function resetCardsDisplay(){
    let cards = document.getElementById("playerHand").children;

    for (let i = 0; i < cards.length; i++) {
        cards[i].firstChild.checked = false;
    }
}


function removeCardFromDisplay(cardName) {

    let cards = document.getElementById("playerHand").children;

    for (let i = 0; i < cards.length; i++) {
        // deletes the checked one, not the first one to occur in hand
        if (cards[i].firstChild.name === cardName && cards[i].firstChild.checked) {
            cards[i].remove();
            return;
        }
    }

}


function clearPlayerHandDisplay(){
    console.log("here");
    let playerHandDiv = document.getElementById("playerHand");
    while (playerHandDiv.firstChild) {
    playerHandDiv.removeChild(playerHandDiv.firstChild);
    console.log("removed");
  }
}


function placeCards(cardsPlaced) {
    // this is in the blue area where the chosen cards are placed
    let playerStageCards = document.createElement("div");
    playerStageCards.setAttribute("class", "placeCardsDiv");
    playerStageCards.setAttribute("id", "placeCardsDiv");
    document.getElementById("stages").appendChild(playerStageCards);

    for (let i = 0; i < cardsPlaced.length; i++) {
        if (i < cardsPlaced.length -1 ){
        addACardDisplay(cardsPlaced[i].name + "," , "placeCardsDiv", false);}
        else{
        addACardDisplay(cardsPlaced[i].name, "placeCardsDiv", false);}

    }


}

// Clears the cards that the players placed (for a stage)
// this doesn't remove sponsor stuff, just in case it's needed
// so maybe call this function every time a stage is over
function clearPlayerStageCards(){
    document.getElementById("placeCardsDiv").remove();
}



// when click on the face down cards, it reveals the cards
function turnCardsOver() {
    let currCards = document.getElementById("stages");

    // remove the square for the player cards (what's currently in that div)

    const cardsDown = document.getElementById("cardsDown-" + playerId);
    cardsDown.remove();

    // display the cards
    placeCards(stageCards);
    //currCardsFacedDown = [];

    // when cards are turned over, display the total points
    winStage();

}




//it would be better if we had player ids..
function getAdventureCards() {
    stompClient.send("/app/getAdvCard", {}, JSON.stringify(playerId));
    //stompClient.subscribe("/topic/getAdvCard", callback);
    const advCardSubscription = stompClient.subscribe('/user/queue/getAdvCard', function (response) {
        let data = JSON.parse(response.body);

        // for testing purposes
       /* if (playerHand.size < 12) {
            playerHand.push(data); //{name: "blab", ba..}
        }
        if (playerHand.size >= 12) {
            alert("You already have 12 adventure cards in your hand, discard some to get more!");
        }*/

        //console.log(data);
        let hand = document.getElementById("playerHand");
        /*all for creating a label :))*/
        let newLabel = document.createElement("label");
        newLabel.setAttribute("for", 'checkbox');
        newLabel.innerHTML = data.name; //name of the card

        let newCheckbox = document.createElement("input");
        newCheckbox.setAttribute("type", 'checkbox');
        newCheckbox.setAttribute("id", 'checkbox');
        newCheckbox.setAttribute("name", data.name);

        // added to div first for easier cleanup (removing the cards)
        let div = document.createElement("div");
        div.appendChild(newCheckbox);
        div.appendChild(newLabel);
        hand.appendChild(div);

        getPlayerHand();

        // playerHand 12 cards - edit 2
        // adding to the hand even if > 12 to allow player to pick and choose
        playerHand.push(data);

        if (playerHand.length > 12){
            let difference = playerHand.length - 12;
            alert("You may only have a max of 12 cards. Please discard at least " + difference + " card(s).");
            // disable all buttons until player has valid amount of cards
            disableGameButtons();
        }

        advCardSubscription.unsubscribe();
    });
    //stompClient.unsubscribe('/topic/getAdvCard');
}


// disbles everything except for discard card button
// intended to be used to force player to discard cards so that they have <= 12 cards
function disableGameButtons(){
    document.getElementById("adventureCards").style.pointerEvents = "none";
    document.getElementById("storyCards").style.pointerEvents = "none";

    document.getElementById("hand").disabled = true;
    document.getElementById("sponserQuest").disabled = true;
    document.getElementById("joinQuest").disabled = true;
    document.getElementById("withdrawQuest").disabled = true;
    document.getElementById("transferQuest").disabled = true;
    document.getElementById("dealAdventureCard").disabled = true;
    document.getElementById("finishTurn").disabled = true;
    document.getElementById("placeCardsQuest").disabled = true;
}


// enable the game buttons (used after player has the valid number of cards)
function enableGameButtons(){
    document.getElementById("adventureCards").style.pointerEvents = "auto";
    document.getElementById("storyCards").style.pointerEvents = "auto";

    document.getElementById("hand").disabled = false;
    document.getElementById("sponserQuest").disabled = false;
    document.getElementById("joinQuest").disabled = false;
    document.getElementById("withdrawQuest").disabled = false;
    document.getElementById("transferQuest").disabled = false;
    document.getElementById("dealAdventureCard").disabled = false;
    document.getElementById("finishTurn").disabled = false;
    document.getElementById("placeCardsQuest").disabled = false;
}
