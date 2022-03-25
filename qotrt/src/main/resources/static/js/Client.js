// ----------------------------------------------------------------------------
// VARIABLES
const url = "http://localhost:8080";
const socket = new SockJS("/gameplay");
const stompClient = Stomp.over(socket);
stompClient.connect()

let game = null;
let gameId = null;
let playerId = 0; //player.java also has an id it assigns
let numOfPlayer = 0;
let playerName = "";
let activeStoryCard = "";
let participant = false;
let sponsor = false;
let isTurn = false;
let playerHand = []; //the adventure cards go here...because we can set a limit 
let stageCards = [];

let shields = 0;
let totalPoints = 0;
let canDraw = 0;

/**
 * The current stage you are on
 */
let currentStage = 0;

// temp fake number of stages, battle points
let numOfStages = 5;
let stageBattlePts = 10;
let currentActivePlayer = 1;
// let questHappening = false;
// let tournamentHappening = false;

// ----------------------------------------------------------------------------
// PROGRAM
setupWindow()

// ----------------------------------------------------------------------------
// FUNCTIONS

// creating a new game
function createGame() {
  playerName = document.getElementById("creator-name").value.trim();
  numOfPlayer = document.getElementById("num-players").value;

  // doesn't allow to create more than one game (for now)
  if (game != null) {
    alert("There is already a game in progress.")
    return;
  }

  // ensures a valid input for num of players
  if (numOfPlayer < 2 || numOfPlayer > 4) {
    alert("Please enter a valid number of players.");
    return;
  }

  //here the game is successfully created...hide the info container
  // let container = document.getElementById("new-game-container");
  // // container.style.display = "hidden";
  // //also make the board visible...#

  subscriptions();

  stompClient.send("/app/game/start", {}, numOfPlayer * 1);
  setTimeout(() => { stompClient.send("/app/playerJoining", {}, playerName); }, 1000)
}

/**
 * Send request to pick a story card
 */
function pickCard() {
  stompClient.send("/app/pickCard",{});
}

function joinGame() {
  if (playerName !== "") return
  playerName = document.getElementById("player-name").value.trim();

  if (!validInputString(playerName)) {
    alert("Please enter a name.");
    return;
  }
  subscriptions();

  // send request to server to join
  stompClient.send("/app/playerJoining", {}, playerName);
}

function subscriptions() {

  stompClient.subscribe("/topic/setStages", function (response) { //need all players subscribe to this
    let data = JSON.parse(response.body); //should be an aray
    let stageSpecificDiv = document.createElement("div");
    for (let i = 0; i < data.length; i++) {
      stageSpecificDiv.append("Cards for Stages " + (i + 1));
      for (let j = 0; j < data[i].length; j++) {
        stageSpecificDiv.append(data[i][j]);
        stageSpecificDiv.append(document.createElement("br"));
      }
    }
    document.getElementById("stages").appendChild(stageSpecificDiv);
  });
  console.log("subscribed")
  //From finish Turn...
  stompClient.subscribe("/topic/finishTurn", function (response) { //response = currentActiveplayer 
    let data = JSON.parse(response.body); //the id of the next active player..
    currentActivePlayer = data["player-id"];
    canDraw = data["can-draw"];

    if (playerId == data) {
      if (activeStoryCardType === "Quest") {
        // joinQuest();
        if (sponsor) {
          winStage(); //checking the stage cards from each player and deciding who won that specific stage
          stompClient.send("/app/incrementStage", {}, currentStage);
          stompClient.subscribe("/topic/incrementStage", function (response) {
            let data = JSON.parse(response.body); //returns a boolean
            if (data) { currentStage += 1 };//increment the stage if true.
            if (!data) {
              alert("Hey the quest is complete, grab this many adventure cards " + selectedCards);
              // sponsor = false; 
              currentQuest = "";
              activeStoryCardType = "";
            }
          })
          finishTurn(); //move to the next player
        }
        if (participant) {
          alert("set stages for " + currentStage);

        }
        alert("If you'd like to participate in the quest, click Join Quest"); //if not sponsor/if not pariticpant
      }

      else { //this is if the current active story card is empty!
        alert("Pick a story Card!");
      }
    }
  })



  const joinGameSubscription = stompClient.subscribe("/user/queue/joinGame", (response) => {
    const data = JSON.parse(response.body);
    playerId = data.body;
    showResponse(data, playerName);


function subscriptions() {

  const gameStartedSubscription = stompClient.subscribe('/topic/game/started', function (response) {
    let data = JSON.parse(response.body);
    if (response) game = response;

    gameId = game.gameID

    displayCreateGameResponse(data.body, playerName, parseInt(numOfPlayer));

    gameStartedSubscription.unsubscribe();
  });

  console.log("game started")


  const joinGameSubscription = stompClient.subscribe("/user/queue/joinGame", (response) => {
    const data = JSON.parse(response.body);
    playerId = data.body;
    showResponse(data, playerName);
    
    setTimeout(() => {  alert("Click on initialize cards to begin the game"); }, 2000);
    setTimeout(() => {  stompClient.send("/app/ready",{},""); }, 2000);

     
    joinGameSubscription.unsubscribe();
  })

   // subscribe to "wait for server to tell client to start"
   stompClient.subscribe("/topic/startTurn", (response) => { // does not get called
    console.log("This is after initilizing", response.body);
    if (response.body * 1 !== 0 && response.body * 1 === playerId) {
      alert("Pick a Story Card");
    }
  })


  stompClient.subscribe("/topic/pickCard", function(response){
    const data = JSON.parse(response.body);
    //console.log("From pick Card",data); //name: 'Slay the Dragon', drawer: null, storyCardType: 'Quest', totalStages: '3', foeName: 'Dragon', …}
    displayStoryCard(data);
  })


  //This is so that all players can see the stages for a quest that the sponsor sets
  stompClient.subscribe("/topic/setStages", function (response) { //need all players subscribe to this
    let data = JSON.parse(response.body); //should be an aray
    let stageSpecificDiv = document.createElement("div");
    for (let i = 0; i < data.length; i++) {
      stageSpecificDiv.append("Cards for Stages " + (i + 1));
      for (let j = 0; j < data[i].length; j++) {
        stageSpecificDiv.append(data[i][j]);
        stageSpecificDiv.append(document.createElement("br"));
      }
    }
    document.getElementById("stages").appendChild(stageSpecificDiv);
  });
  
  
  //From finish Turn...
  stompClient.subscribe("/topic/finishTurn", function (response) { //response = currentActiveplayer 
    let data = JSON.parse(response.body); //the id of the next active player..
    console.log(data);
    
  })


}

//~~~~~~~~~~~~~Subscription ends here~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

function setupWindow() {
  //window.addEventListener("load", displayAllCards(theCards));
}



// check if the cards are unique
// since players cannot play two Weapon cards of the same type in a stage
function allWeaponCardsUnique(cards) {
  let tempArr = [];
  for (let i = 0; i < cards.length; i++) {
    if (tempArr.includes(cards[i].name)) {
      return false;
    }
    // only keep tracks of the weapon cards
    if (isWeaponCard(cards[i].name)) {
      tempArr.push(cards[i].name);
    }

  }
  return true;
}



// if the total battle points >= stage battle points, return true
// otherwise false
// remove the player from the quest if they lose
// note: I tried to put the stompClient stuff in a separate function so that it gets the rank points first,
// but it didn't worked
function winStage() {
  // rank + card battle points
  stompClient.send("/app/getRankPts", {}, JSON.stringify({
    'message': playerId + ""
  }));
  stompClient.subscribe('/user/queue/getRankPts', function (response) {
    let points = JSON.parse(response.body).body;

    // store total points now, display total points when card face up
    totalPoints = calcTotalBattlePts(points);
    stageBattlePts = maxBattlePoints[currentStage - 1];
    if (calcTotalBattlePts(points) >= stageBattlePts) {
      displayBattlePoint(totalPoints, stageBattlePts, "won");
      updateShields(currentStages);
      return true;
    } else {
      displayBattlePoint(totalPoints, stageBattlePts, "lost");
      participant = false;
      return false;
    }
  });
}



// displays the total battle points for a stage
// winMessage = "won" or "lost"
function displayBattlePoint(totBattlePts, stagePts, winMessage) {
  let div = document.getElementById("battlePoints");
  div.appendChild(document.createTextNode("Total player battle points: " + totalPoints));
  div.appendChild(document.createElement("br"));
  div.appendChild(document.createTextNode("Stage's battle points: " + stagePts));
  div.appendChild(document.createElement("br"));
  div.appendChild(document.createTextNode("You have " + winMessage + " this stage!"));
}


function clearBattlePointDisplay() {
  let div = document.getElementById("battlePoints");
  while (div.firstChild) {
    div.removeChild(div.firstChild);
  }
}


// get total battle points (rank + cards)
function calcTotalBattlePts(rankPts) {
  let total = rankPts;
  // get the card points
  for (let i = 0; i < stageCards.length; i++) {
    if (stageCards[i].battlePoints != -1) {
      total += stageCards[i].battlePoints;
    }
  }
  return total;
}

// checking if the card is a weapon card
// if there's a better way let me know lol
function isWeaponCard(cardName) {
  let weapons = ["Horse", "Sword", "Dagger", "Excalibur", "Lance", "Battle-ax"];
  if (weapons.includes(cardName)) {
    return true;
  }
  return false;
}


// update winner's shields (when player wins the quest/tournament)
// they win the entire game when shields >= 5
function updateShields(numOfShields) {

  stompClient.send("/app/updateShields", {}, JSON.stringify({
    'playerId': playerId + "",
    'shields': numOfShields + ""
  }));

  // update shields in global var (local)
  shields += numOfShields;

}




function placeCardsQuest() {
  // players are allowed to choose no cards, so we don't disable the button
  checked = getAllChecked();

  // check if weapon cards unique
  if (!allWeaponCardsUnique(getActualCards(checked))) {
    alert("You may not play two Weapon cards of the same type.");
    return;
  }

  // stores the cards for this stage
  // we need to change this so that stageCards contains the actual cards (so we have data)
  // but what about like, simple printing... nah, we need the actual cards
  //stageCards = checked;
  stageCards = getActualCards(checked);

  // remove from the cards display
  removeAllCheckedCards(checked);

  let cardAtPlay = document.getElementById("stages");
  let div = document.createElement("div");
  //div.id = "cardsDown";
  div.setAttribute('id', 'cardsDown-' + playerId);
  div.appendChild(document.createElement("br"));
  div.appendChild(document.createTextNode("---------- Player " + playerId + " cards for stage " + currentStage + " ----------"));
  div.appendChild(document.createTextNode("P" + playerId));
  div.appendChild(document.createTextNode("br"));
  cardAtPlay.appendChild(div);
  document.getElementById("cardsDown-" + playerId).addEventListener("click", turnCardsOver);
  alert("Click complete turn if you're done setting your cards for stage " + currentStage);

}



// input: list of card names
// output: list of object objects with the corresponding given names
function getActualCards(cardNames) {
  let cards = [];
  for (let i = 0; i < cardNames.length; i++) {
    for (let j = 0; j < playerHand.length; j++) {
      if (playerHand[j].name == cardNames[i]) {
        console.log("card name: " + cardNames[i]);
        console.log("playerHand[j].name " + playerHand[j].name);
        console.log(" ");
        cards.push(playerHand[j]);
        break;
      }
    }
  }
  return cards;
}



// need a function for removing cards from player after stage is done
// this would be server side, I believe the local storage too
// cards to remove: stageCards, also amour would be removed only after quest is done (not stage)
function removeUsedCardsLocal() {
  let temp = stageCards;
  let newPlayerHand = [];

  let cardFound = false;
  let toBeDeleted = []

  for (let i = 0; i < stageCards.length; i++) {
    toBeDeleted.push(stageCards[i].name);
  }

  for (let i = 0; i < playerHand.length; i++) {
    for (let j = 0; j < stageCards.length; j++) {
      if (playerHand[i].name == stageCards[j].name) {
        cardFound = true;
        break;
      }
    }
    if (!cardFound) {
      newPlayerHand.push(playerHand[i]);
    }
  }
  stageCards = [];
  playerHand = newPlayerHand;
  // testing
  // console.log("new hand after removal: ");
  // for (let i = 0; i < newPlayerHand.length; i++){
  //    console.log(newPlayerHand[i].name);
  //}

  removeUsedCardsServer(toBeDeleted);

  return newPlayerHand;

}

function removeUsedCardsServer(cards) {
  stompClient.send("/app/discardCards", {}, JSON.stringify({
    'playerId': playerId,
    'cards': cards
  }));
}



function removeSelectedCards() {
  let checked = getAllChecked();
  removeCardsFromHand(checked);

}


// remove cards checked from hand
function removeCardsFromHand(checked) {
  // get the checked, and unchecked cards
  //let checked = getAllChecked();
  let unChecked = getAllUnchecked();

  // update the player hand so that it only has the unchecked cards
  playerHand = getActualCards(unChecked);
  removeAllCheckedCards(checked);

  // remove from server
  // basically it removes all checked cards
  removeUsedCardsServer(checked);

}


function initializeAdv() {
  stompClient.send("/app/giveCards", {}, `${playerId}`);

  const connection = stompClient.subscribe("/user/queue/giveCards", (response) => {
    const data = JSON.parse(response.body);
    console.log(data);
    playerHand = data;
    displayAllCards(data);
  });

}
