let theCards = ["card1", "card2", "card3", "card4", "card5", "card6", "card7", "card8", "card9", "card10", "card11", "card12"];
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
        addACardDisplay(cards[i], "playerHand");
    }
}



// create and add a checkbox "card"
// should take in a card name or something
function addACardDisplay(cardName, divName) {
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
    div.appendChild(checkBox);
    div.appendChild(label);
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



function removeAllCheckedCards(checked) {
    for (let i = 0; i < checked.length; i++) {
        removeCardFromDisplay(checked[i]);
    }
}


function removeCardFromDisplay(cardName) {

    let cards = document.getElementById("playerHand").children;

    for (let i = 0; i < cards.length; i++) {
        if (cards[i].firstChild.name === cardName) {
            cards[i].remove();
            return;
        }
    }

}



function placeCards(cardsPlaced) {
    // this is in the blue area where the chosen cards are placed
    for (let i = 0; i < cardsPlaced.length; i++) {
        addACardDisplay(cardsPlaced[i], "stages");
    }

}



// when click on the face down cards, it reveals the cards
function turnCardsOver() {
    let currCards = document.getElementById("stages");

    // remove the square for the player cards (what's currently in that div)

    while (currCards.firstChild) {
        currCards.removeChild(currCards.firstChild);
    }

    // display the cards
    placeCards(currCardsFacedDown);
    currCardsFacedDown = [];

}



/*
Thinking:
I need a way to get the player's cards
I need a way to get the player's number (name?)


So.....

When a player joins/connects, we also want them to subscribe to their player num.
When in the service, we should return the player num instead of the game id?

Then we can use the player num to get their cards, and display them etc

okay how do we connect the index page data (socket and stuff) to the board page?...
*/


/*
To do now:
- work on disabling buttons
- search up how to take info from the index page to the board page
- try to implement that so that we get the player's cards (on the board page, so in the cards.js)
*/


/*
- only one html page now
- subscribe to different nums... but why are the other pages udpated??? because of same function?
- need to work on disable buttons

*/

//it would be better if we had player ids..
function getAdventureCards() {
    stompClient.send("/app/getAdvCard");
    //stompClient.subscribe("/topic/getAdvCard", callback);
    stompClient.subscribe('/topic/getAdvCard', function (response) {
        let data = JSON.parse(response.body);
        //console.log(data);
        let hand = document.getElementById("playerHand");
        /*all for creating a label :))*/
        let newLabel = document.createElement("label");
        newLabel.setAttribute("for", 'checkbox');
        newLabel.innerHTML = data.name; //name of the card

        let newCheckbox = document.createElement("input");
        newCheckbox.setAttribute("type", 'checkbox');
        newCheckbox.setAttribute("id", 'checkbox');
        hand.appendChild(newCheckbox);
        hand.appendChild(newLabel);
    });
    stompClient.unsubscribe('/topic/getAdvCard');
}