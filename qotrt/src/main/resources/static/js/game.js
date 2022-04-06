

// displays if player has joined game or not
function showResponse(data, playerName) {

  let message = "The game is full or there was an issue."
  if (data.body != null) {
    message = playerName + " has successfully joined the game. They are player number " + data.body;

  }

  let joinMessage = document.getElementById("game-message");
  joinMessage.appendChild(document.createTextNode(message));
  joinMessage.appendChild(document.createElement("br"));
}



// displays if the game is successfully created
function displayCreateGameResponse(data, playerName, numPlayers) {
  let message = "The game cannot be created."
  if (data != null) {

    gameID = data

    message = "The game " + gameID + " has successfully been created! Welcome to the game " + playerName + ". " +
      "This game will have " + numPlayers + " players."
  }

  let newGameMessage = document.getElementById("game-message");
  clearMessage();
  newGameMessage.appendChild(document.createTextNode(message));
  newGameMessage.appendChild(document.createElement("br"));

}


// clears the game message box
function clearMessage() {
  let joinMessage = document.getElementById("game-message");
  while (joinMessage.firstChild) {
    joinMessage.removeChild(joinMessage.firstChild);
  }
}




// checks if the input is valid (not empty)
function validInputString(input) {
  if (input == "") {
    return false;
  }
  return true;
}

function finishTurn(btn) {
  //moves on to the next player..
  stompClient.send("/app/finishTurn");

  //after one click the finish turn disables
  disableFinishTurnAfterClick(btn);
  disablePickStoryCard();
  
}

//adding message to the scrollDiv
function scrollDiv(text){
    let ele = document.getElementById('scrollDiv');
    var txt = document.createTextNode(text);
    const current = new Date();
             // By default US English uses 12hr time with AM/PM
    const time = current.toLocaleTimeString("en-US");
    let dateNode = document.createTextNode(time);
    var breakPt = document.createElement('br');
    
    ele.appendChild(breakPt);   
    ele.appendChild(txt);
    ele.appendChild(breakPt);   
    ele.appendChild(dateNode);
    ele.appendChild(breakPt);

}