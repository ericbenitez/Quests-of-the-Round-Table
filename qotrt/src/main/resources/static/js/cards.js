let theCards = ["card1", "card2", "card3", "card4", "card5", "card6", "card7", "card8", "card9", "card10", "card11", "card12"];
let tempPlayerNum = 1;
let currCardsFacedDown = []

window.addEventListener("load", function () {
  document.getElementById("faceUpBut").addEventListener("click", addFaceUp);
});
window.addEventListener("load", function () {
  document.getElementById("faceDownBut").addEventListener("click", addFaceDown);
});

// automatically display cards to test the functionality
//window.addEventListener("load", displayAllCards(theCards));


function addFaceUp(){
    console.log("face up");
    checked = getAllChecked();
    removeAllCheckedCards(checked);
    placeCards(checked);
    disableCardButtons();
}

function addFaceDown(){
    console.log("face down");
    checked = getAllChecked();
    removeAllCheckedCards(checked);
    currCardsFacedDown = checked;
    if (checked == []){
        disableCardButtons();
        return;
    }

    let cardAtPlay = document.getElementById("playerCardAtPlay");
    let div = document.createElement("div");
    //div.id = "cardsDown";
    div.setAttribute('id', 'cardsDown');
    div.appendChild(document.createTextNode("P" + tempPlayerNum));
    cardAtPlay.appendChild(div);
    document.getElementById("cardsDown").addEventListener("click", turnCardsOver);

    disableCardButtons();
}


// Display the cards
function displayAllCards(cards){
    // loop through the cards and call the
    // function addACardDisplay()
   // alert("cards to display: " + theCards);
    //alert("cards total: " + theCards.length);
    for (let i = 0; i < cards.length; i++){
        addACardDisplay(cards[i], "playerCards");
    }
}



// create and add a checkbox "card"
// should take in a card name or something
function addACardDisplay(cardName, divName){
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
function updateButtonDisplay(){

       let checkBoxDiv = document.getElementById("playerCards").children;
       // loop through all checkboxes and check if any are checked
       for (let i = 0; i < checkBoxDiv.length; i++){
           if (checkBoxDiv[i].firstChild.checked){
                enableCardButtons();
                return;
           }
       }
       disableCardButtons();
}


function enableCardButtons(){
    document.getElementById("faceUpBut").disabled = false;
    document.getElementById("faceUpBut").className = "enabledButton";
    document.getElementById("faceDownBut").disabled = false;
    document.getElementById("faceDownBut").className = "enabledButton";
}


function disableCardButtons(){
    document.getElementById("faceUpBut").disabled = true;
    document.getElementById("faceUpBut").className = "disabledButton";
    document.getElementById("faceDownBut").disabled = true;
    document.getElementById("faceDownBut").className = "disabledButton";
}



// loop through all the cards in the DOM, and get the cards that are checked
function getAllChecked(){
    let checked = [];
    // loop through each child and look at the checkBox to see if it's checked
    let cards = document.getElementById("playerCards").children;

    // start at 1 since the first child is just text "Your Cards" (nvm for now)
    for (let i = 0; i < cards.length; i++){
        if (cards[i].firstChild.checked){
            checked.push(cards[i].firstChild.name);
        }
    }
    return checked;

}



function removeAllCheckedCards(checked){
    for (let i = 0; i < checked.length; i++){
        removeCardFromDisplay(checked[i]);
    }
}


function removeCardFromDisplay(cardName){

    let cards = document.getElementById("playerCards").children;

    for (let i = 0; i < cards.length; i++){
        if (cards[i].firstChild.name === cardName){
            cards[i].remove();
            return;
        }
    }

}



function placeCards(cardsPlaced){
    // this is in the blue area where the chosen cards are placed
    // let cardAtPlay = document.getElementById("player-card-atPlay");
    for (let i = 0; i < cardsPlaced.length; i++){
        addACardDisplay(cardsPlaced[i], "playerCardAtPlay");
    }

}



// when click on the face down cards, it reveals the cards
function turnCardsOver(){
    let currCards = document.getElementById("playerCardAtPlay");

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