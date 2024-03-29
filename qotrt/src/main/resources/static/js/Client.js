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
let serverData = {}; //this is data saved from finish Turn (Session Data)
// let activeStoryCard = "";
// let participant = false;
// let sponsor = false;
// let isTurn = false;
let playerHand = []; //the adventure cards go here...because we can set a limit 
let stageCards = [];

let shields = 0;
let totalPoints = 0;
let canDraw = 0;

let tourParticipant = false;
let drawerTournament = 0;
let tieBreakerPlayed = false;  // false when the tie breaker round has not yet been played
let tieOccurred = false;


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
    scrollDiv("There is already a game in progress.")
    return;
  }

  // ensures a valid input for num of players
  if (numOfPlayer < 2 || numOfPlayer > 4) {
    scrollDiv("Please enter a valid number of players.");
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
  stompClient.send("/app/pickCard", {});
}

function joinGame() {
  if (playerName !== "") return
  playerName = document.getElementById("player-name").value.trim();

  if (!validInputString(playerName)) {
    scrollDiv("Please enter a name.");
    return;
  }
  subscriptions();

  // send request to server to join
  stompClient.send("/app/playerJoining", {}, playerName);
}


function setupWindow() {
  //window.addEventListener("load", displayAllCards(theCards));
  
  setTimeout(() => { checkGameForJoinDisplay() }, 600);
}

function checkGameForJoinDisplay() {
    stompClient.send("/app/checkGameExist", {});
    const checkGame = stompClient.subscribe("/user/queue/checkGameExist", function (response) {
        let data = JSON.parse(response.body);
        if (data == 1){
          showJoinGame();
          checkGame.unsubscribe();
        }
        
        
    });  
  
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

const foes = ["Robber Knight", "Saxons", "Boar", "Thieves", "Green Knight", "Black Knight", "Evil Knight", "Saxon Knight", "Dragon", "Giant", "Mordred"]
function hasFoes(cards) {
  for (const card of cards) {
    if (foes.includes(card)) return true
  }

  return false
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
      updateShieldDisplay();
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


//~~~~~~~~~~~~~~this dunction should be in quest.js~~~~~~~~~~~~~~~

function placeCardsQuest(btn) {
  // players are allowed to choose no cards, so we don't disable the button
  checked = getAllChecked();

  // check if weapon cards unique
  if (!allWeaponCardsUnique(getActualCards(checked))) {
    scrollDiv("You may not play two Weapon cards of the same type.");
    return;
  }

  if (hasFoes(checked)) {
    scrollDiv("You cannot play foes for a quest")
    return
  }

  for (const cardName of checked) {
    const card = CardObjects[cardName]

    if (card) {
      if (card.cardType == "Test") {
        scrollDiv("Participants cannot select Test cards for a quest")
        return
      }
    }
  }


  stompClient.send("/app/setClientStage", {}, JSON.stringify({ "playerId": playerId, "cards": checked }));
  scrollDiv("Click Finish Turn!");


  // stores the cards for this stage
  // we need to change this so that stageCards contains the actual cards (so we have data)
  // but what about like, simple printing... nah, we need the actual cards
  //stageCards = checked;
  stageCards = getActualCards(checked);

  // remove from the cards display
  removeAllCheckedCards(checked);
  removeUsedCardsServer(checked);
  let currentStageNumber = serverData.currentStoryCard.currentStageNumber;
  

  let cardAtPlay = document.getElementById("stages");
  let div = document.createElement("div");
  div.setAttribute("class", "placeCardsDiv");
  //div.id = "cardsDown";
  div.setAttribute('id', 'cardsDown-' + playerId + currentStageNumber);
  div.appendChild(document.createElement("br"));
  div.appendChild(document.createTextNode("---------- Player " + playerId + " cards for stage " + currentStageNumber + " ----------"));
  // div.appendChild(document.createTextNode("P" + playerId));
  div.appendChild(document.createElement("br"));
  cardAtPlay.appendChild(div);
  //document.getElementById("cardsDown-" + playerId).addEventListener("click", turnCardsOver);
  scrollDiv("Click complete turn if you're done setting your cards for stage " + currentStage);
  disableStageCardsAfterClick(btn);
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
  if (playerHand.length <= 12) {
    enableGameButtons();
  }

}


// remove cards checked from hand
function removeCardsFromHand(checked) {
  // get the checked, and unchecked cards
  //let checked = getAllChecked();
  let unChecked = getAllUnchecked();

  // update the player hand so that it only has the unchecked cards
  playerHand = getActualCards(unChecked);
  removeAllCheckedCards(checked);
  // reset the display so that checked is now unchecked
  resetCardsDisplay();

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
    clearPlayerHandDisplay();
    displayAllCards(data);
  });
}


// updates the player's hand (from server)
// same as initializeAdv, but without drawing 12 cards
function getPlayerHand() {
  stompClient.send("/app/getCards", {}, `${playerId}`);

  const connection = stompClient.subscribe("/user/queue/getCards", (response) => {
    const data = JSON.parse(response.body);
    playerHand = data;
    clearPlayerHandDisplay();
    displayAllCards(data);

    if (playerHand.length > 12) {
      let difference = playerHand.length - 12;
      scrollDiv("You may only have a max of 12 cards. Please discard at least " + difference + " card(s).");
      // disable all buttons until player has valid amount of cards
      disableGameButtons();
  }
  });
}

function showCurrentStage(stageNumber) {
  // the first one is 0?
  //let stageNumber = currentStage + 1;
  //alert("stage: " + stageNumber);
  document.getElementById("stage" + stageNumber).style.display = "inline";
}


// shields should be updated after winning quests, tournamnets, and for certain events
// updates the shields display
// (Note: I thought we didn't update the shields on server side, but we actually did, so the global var shields should be updated)
function updateShieldDisplay() {
  shieldDisplay = document.getElementById("shields").innerHTML = "You have " + shields + " shields.";

}

function displayTurnIndicator(isMyTurn) {
  const turnIndicator = document.getElementById("turnIndicator")
  
  if (isMyTurn) {
    turnIndicator.style.background = "#547c2f"
    turnIndicator.innerHTML = "It is your turn!"
  } else {
    
    turnIndicator.style.background = "#8a382e"
    turnIndicator.innerHTML = "Waiting..."
  }
}


function updateRankToKnight(){
    document.getElementById("squire").style.display = "none";
    document.getElementById("knight").style.display = "block";
    let rankInfo = document.getElementById("playerInfoRank");
    rankInfo.removeChild(rankInfo.firstChild);
    rankInfo.appendChild(document.createTextNode("Rank: Knight = 10 Battle Points"));
}

function showPlayerInfoDisplay(){
    let prettyName = playerName[0].toUpperCase();
    prettyName += playerName.slice(1);
    document.getElementById("playerInfoName").appendChild(document.createTextNode(prettyName));
    document.getElementById("playerInfoNumber").appendChild(document.createTextNode("Player Number: " + playerId));
    document.getElementById("playerInfoRank").appendChild(document.createTextNode("Rank: Squire = 5 Battle Points"));
}
