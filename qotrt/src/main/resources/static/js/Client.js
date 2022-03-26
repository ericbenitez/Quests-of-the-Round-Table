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
// let activeStoryCard = "";
// let participant = false;
// let sponsor = false;
// let isTurn = false;
let playerHand = []; //the adventure cards go here...because we can set a limit 
let stageCards = [];

let shields = 0;
let totalPoints = 0;

let tourParticipant = false;
let drawerTournament = 0;

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

  //the response after setting the sponsor stages.
  stompClient.subscribe("/topic/setStages", function (response) { //need all players subscribe to this
    let data = JSON.parse(response.body); //should be an aray
    let stageSpecificDiv = document.createElement("div");
    let stageNumOfCardsDiv = document.createElement("div");
    for (let i = 0; i < data.length; i++) {
      stageSpecificDiv.append("Cards for Stages " + (i + 1));
      stageSpecificDiv.append(document.createElement("br"));

      stageNumOfCardsDiv.append("Cards for Stages " + (i + 1) + ": ");
      stageNumOfCardsDiv.append(data[i].length + " cards");
      stageNumOfCardsDiv.append(document.createElement("br"));
      let hidden = document.createElement("div");
      hidden.setAttribute("id", "stage" + (i + 1));
      hidden.style.display = "none";
      stageNumOfCardsDiv.append(hidden);
      stageNumOfCardsDiv.append(document.createElement("br"));

      for (let j = 0; j < data[i].length; j++) {
        stageSpecificDiv.append(data[i][j]);
        stageSpecificDiv.append(document.createElement("br"));

        hidden.appendChild(document.createTextNode(data[i][j]));
        hidden.appendChild(document.createElement("br"));

      }
    }
    if (sponsor){
      document.getElementById("stages").appendChild(stageSpecificDiv);
    }else{
      //stageSpecificDiv.style.display = "none";
      document.getElementById("stages").appendChild(stageNumOfCardsDiv);
    }
    
    
  });
  console.log("subscribed")


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
    //console.log("From pick Card",data); //name: 'Slay the Dragon', drawer: null, storyCardType: 'Quest', totalStages: '3', foeName: 'Dragon', …}
    displayStoryCard(data);
  })
   
  //From finish Turn...


  stompClient.subscribe("/topic/finishTurn", function (response) { //response = currentActiveplayer 
    let data = JSON.parse(response.body); //the id of the next active player..
    console.log(data);

    /**
     * {"currentActivePlayer":2,
     * "currentStoryCard":{"name":"Slay the Dragon","drawer":null,"storyCardType":"Quest","totalStages":"3","foeName":"Dragon","sponsor":1,"stages":[["Evil Knight","Saxons"],["Horse","Thieves"],["Mordred","Horse"]],"participantsId":[]},
     * "sponsorId":1,
     * "participantsId":[],
     * "questInPlay":false}
     * 
     * 
     */
    if (data.currentActivePlayer = playerId){
      //activate their buttons
      enableGameButtons();
      let currentStoryCard = data.currentStoryCard;
      if(currentStoryCard.storyCardType === "Quest"){
        //If the current story card type is quest, it could mean a few things
        //they're the sponsor, and it has looped back to them, the stage is complete
        if (currentStoryCard.sponsor === playerId && currentStoryCard.currentStageNumber < currentStoryCard.totalStages){
          //check winner for data.currentstages
          checkWinner(data.stages); //this function does the functionality of sending the appropriate reward
          //and moving the turn
        }
        //they're the sponsor and this is the last and total stage
        if (currentStoryCard.sponsor === playerId && currentStoryCard.currentStageNumber === currentStoryCard.totalStages){
          //check the winners again and reward them
          //for the sponsor, it should check how many total cards they used in all of the stages + total stages
          //for example, alert(pick 6 cards for sponsoring the quest);
        }
        //another scenario is that the player is not the sponsor.
      }if(currentStoryCard.sponsor!=playerId){
          if(!currentStoryCard.participantsId.includes(playerId) && currentStoryCard.currentStageNumber ===1){
            //ask them to join
            alert("click join quest");
          }
          if(currentStoryCard.participantsId.includes(playerId) && currentStoryCard.currentStageNumber<= currentStoryCard.totalStages){
            //pick cards for this stage
            alert("here 2")
          }
          if(!currentStoryCard.participantsId.includes(playerId) &&currentStoryCard.currentStageNumber !=1){
          //this player refused to join the quest;
          //finishTurn();//increment the currentActivePlayer and move to the next player
          alert("here 3")
        }
      }
      if(currentStoryCard.storyCardType === "Event"){
        
      }
      if(currentStoryCard.storyCardType === "Tournament"){
        
      }
      if(currentStoryCard.storyCardType === null){
        //if the story card type is numm it means they might be the first player
        alert("pick a story card");
      }

    }
    else if (data.currentActivePlayer != playerId){
      //disable their buttons!
      diableButtons();
    }
    
  });
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
  div.setAttribute("class", "placeCardsDiv");
  //div.id = "cardsDown";
  div.setAttribute('id', 'cardsDown-' + playerId);
  div.appendChild(document.createElement("br"));
  div.appendChild(document.createTextNode("---------- Player " + playerId + " cards for stage " + currentStage + " ----------"));
  // div.appendChild(document.createTextNode("P" + playerId));
  div.appendChild(document.createElement("br"));
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
  alert(playerHand.length);
  if (playerHand.length <= 12){
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
function getPlayerHand(){
  stompClient.send("/app/getCards", {}, `${playerId}`);

  const connection = stompClient.subscribe("/user/queue/getCards", (response) => {
    const data = JSON.parse(response.body);
    playerHand = data;
    clearPlayerHandDisplay();
    displayAllCards(data);
  });
}

function showCurrentStage(){
    // the first one is 0?
    let stageNumber =  currentStage + 1;
    alert("stage: " + stageNumber);
    document.getElementById("stage" + stageNumber).style.display = "inline";
}


// shields should be updated after winning quests, tournamnets, and for certain events
// updates the shields display
// (Note: I thought we didn't update the shields on server side, but we actually did, so the global var shields should be updated)
function updateShieldDisplay(){
  shieldDisplay = document.getElementById("shields").innerHTML = "You have " + shields + " shields.";

}

