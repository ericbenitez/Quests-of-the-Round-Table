// ----------------------------------------------------------------------------
// VARIABLES
const url = "http://localhost:8080";
const socket = new SockJS("/gameplay");
const stompClient = Stomp.over(socket);
stompClient.connect()

let game = null;
let gameId = null;
let playerId = null;
let numOfPlayer = 0;
let playerName = "";

let participant = false;
let sponsor = false;
// the player's 12 cards
let playerHand = [];
let stageCards = [];
let shields = 0;
let totalPoints = 0;

// temp fake number of stages, battle points
let numOfStages = 5;
let stageBattlePts = 10;

// ----------------------------------------------------------------------------
// PROGRAM
setupWindow()


// ----------------------------------------------------------------------------
// FUNCTIONS

// creating a new game
function createGame() {
  playerName = document.getElementById("creator-name").value.trim();
  const numPlayers = document.getElementById("num-players").value;

  // doesn't allow to create more than one game (for now)
  if (game != null) {
    alert("There is already a game in progress.")
    return;
  }

  // ensures a valid input for num of players
  if (numPlayers < 2 || numPlayers > 4) {
    alert("Please enter a valid number of players.");
    return;
  }

  //here the game is successfully created...hide the info container
  // let container = document.getElementById("new-game-container");
  // // container.style.display = "hidden";
  // //also make the board visible...#

  subscriptions()

  stompClient.send("/app/game/start", {}, numPlayers * 1);
  setTimeout(() => { stompClient.send("/app/playerJoining", {}, playerName); }, 1000)
}

/**
 * Send request to pick a story card
 */
function pickCard() {
  stompClient.send("/app/pickCard", {}, "");
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
  const joinGameSubscription = stompClient.subscribe("/topic/joinGame", (response) => {
    const data = JSON.parse(response.body);
    playerId = data.body + 1;
    showResponse(data, playerName);

    stompClient.subscribe("/topic/pickCard", (response) => {
      const data = JSON.parse(response.body).body;
      displayStoryCard(data);
    })
    
    joinGameSubscription.unsubscribe();
  })

  const gameStartedSubscription = stompClient.subscribe('/topic/game/started', function (response) {
    let data = JSON.parse(response.body);
    if (response != null) game = response;

    gameId = game.gameID

    displayCreateGameResponse(response.gameID, playerName, parseInt(numPlayers))

    gameStartedSubscription.unsubscribe();
    startGame()
  });
}

function setupWindow() {
  //window.addEventListener("load", displayAllCards(theCards));
}

function startGame() {
  // subscribe to "wait for server to tell client to start"
  stompClient.subscribe("/topic/startTurn", (response) => {
    if (response === playerId) {
      // ungreys out buttons
      // start turn
      // wait for player input, request server to pick storyCard
    }
  })

  // server sends cards to client whenever .. for 12 cards in the beginning(or anytime else they need a card)
  stompClient.subscribe("/topic/receiveCards", (cards) => {

  })
  // subscribe to "show active player"
  stompClient.subscribe("/topic/showActivePlayer", (activePlayerId) => {
    if (activePlayerId !== playerId) {
      // disable buttons
    }
    // given 12 adventure cards each --> in server go over every player and give them the cards
  })
  //active player click on pickCard button 


  //if it's a quest card ...pickCard returns a name -> 
  // client does pickCard
  // server decides what to do with the card

  //alert do you want to sponser?? --> 
  // do you wnat to sponsor
  // do you want participate
  // startQuest/startTournament broadcast

  //broadcast other players to join
  stompClient.subscribe("/topic/doYouWantToSponsor", (sponsor) => {
    const isSponsoring = confirm("Do you want to sponsor?")
    // do alert that asks for yes/no <--- confirm 
    //if no(another alert telling them to click on pass quest)
    //pass quest has an onclick that moves the turn to the next player.. 
    //if (yes).. alert tells the player to click on sponsor quest button which has an onlclick function
    //that sets the curr player to sponser...calls quest.setSponsor on the server side
    //... 
  })
  //the sponsor has to set all the stages and cards before asking other players to particiapte...
  stompClient.subscribe("/topic/setStages", (sponsor) => {
    //set the quest stages
    //DYNAMIC BUTTON
  });


  stompClient.subscribe("/topic/doYouWantToParticipate", (sponsor) => {
    const isParticipating = confirm("Do you want to participate in the event?")
    //calls other players to check if they are pariticpating in the quest
    //alert them to ask to click on paticipate quest 
    //joinQuest onClick() , Quest 
  })

  stompClient.subscribe("/topic/startQuest", (participants) => {
    // if your id is inside participants, you are part of the quest
    // 
  })

  stompClient.subscribe("/topic/startTournament", (participants) => {

  })





  //later details..
  //greys out the button if it's not the player's turn. 
  //message indicating player's turn.. traffic light
}




// ----------------------------------- Player participating quest ---------------------------------


// player joining a quest, local and server 
function joinQuest() {
  if (sponsor){return;}
  participant = true;

  // change joinQuest button to withdraw button
  document.getElementById("joinQuest").style.display = "none";
  document.getElementById("withdrawQuest").style.display = "inline";

  // add participant to server side
 stompClient.send("/app/joinQuest", {}, JSON.stringify({
         'message': playerId + ""
 }));

}



function withdrawQuest(){
 participant = false;
 document.getElementById("joinQuest").style.display = "inline";
 document.getElementById("withdrawQuest").style.display = "none";
 // should I disable the joinQuest button until the quest is over?

 stageCards = [];

 // withdraw on server side
 stompClient.send("/app/withdrawQuest", {}, JSON.stringify({
             'message': playerId + ""
 }));
}


// test function
// shows whether current player is in the quest or not
function inQuest(){
 alert("player " + playerId + " quest: "  + participant);
}


// this is for the sponsor (disable the button after the player clicks on sponsor quest)
function disableJoinQuest(){
 document.getElementById("joinQuest").disabled = true;
}



// check if the cards are unique
// since players cannot play two Weapon cards of the same type in a stage
function allWeaponCardsUnique(cards){
 let tempArr = [];
 for (let i = 0; i < cards.length; i++){
     if (tempArr.includes(cards[i].name)){
         return false;
     }
     // only keep tracks of the weapon cards
     if (isWeaponCard(cards[i].name)){
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
function winStage(){
 // rank + card battle points
  stompClient.send("/app/getRankPts", {}, JSON.stringify({
     'message': playerId + ""
  }));
  stompClient.subscribe('/topic/getRankPts', function (response) {
     let points = JSON.parse(response.body).body;

     // store total points now, display total points when card face up
     totalPoints = calcTotalBattlePts(points);

     if (calcTotalBattlePts(points) >= stageBattlePts){
         displayBattlePoint(totalPoints, stageBattlePts, "won");
         return true;
     }else {
         displayBattlePoint(totalPoints, stageBattlePts, "lost");
         participant = false;
         return false;
     }
  });
}



// displays the total battle points for a stage
// winMessage = "won" or "lost"
function displayBattlePoint(totBattlePts, stagePts, winMessage){
 let div = document.getElementById("battlePoints");
 div.appendChild(document.createTextNode("Total player battle points: " + totalPoints));
 div.appendChild(document.createElement("br"));
 div.appendChild(document.createTextNode("Stage's battle points: " + stagePts));
 div.appendChild(document.createElement("br"));
 div.appendChild(document.createTextNode("You have " + winMessage + " this stage!"));
}


function clearBattlePointDisplay(){
 let div = document.getElementById("battlePoints");
 while (div.firstChild) {
         div.removeChild(div.firstChild);
 }
}


// get total battle points (rank + cards)
function calcTotalBattlePts(rankPts){
 let total = rankPts;
 // get the card points
 for (let i = 0; i < stageCards.length; i++){
     if (stageCards[i].battlePoints != -1){
         total += stageCards[i].battlePoints;
     }
 }
 return total;
}

// checking if the card is a weapon card
// if there's a better way let me know lol
function isWeaponCard(cardName){
 let weapons = ["Horse", "Sword", "Dagger", "Excalibur", "Lance", "Battle-ax"];
 if (weapons.includes(cardName)){
     return true;
 }
 return false;
}


// update winner's shields (when player wins the quest/tournament)
// they win the entire game when shields >= 5
function updateShields(numOfShields){

 stompClient.send("/app/updateShields", {}, JSON.stringify({
         'playerId': playerId + "",
         'shields': numOfShields + ""
 }));

 // update shields in global var (local)
 shields += numOfShields;

}




function placeCardsQuest(){
 // players are allowed to choose no cards, so we don't disable the button
 checked = getAllChecked();

 // check if weapon cards unique
 if (!allWeaponCardsUnique(getActualCards(checked))){
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
 div.setAttribute('id', 'cardsDown');
 div.appendChild(document.createTextNode("P" + playerId));
 cardAtPlay.appendChild(div);
 document.getElementById("cardsDown").addEventListener("click", turnCardsOver);

}



// input: list of card names
// output: list of object objects with the corresponding given names
function getActualCards(cardNames){
 let cards = [];
 for (let i = 0; i < cardNames.length; i++){
     for (let j = 0; j < playerHand.length; j++){
         if (playerHand[j].name == cardNames[i]){
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
function removeUsedCardsLocal(){
 let temp = stageCards;
 let newPlayerHand = [];

 let cardFound = false;
 let toBeDeleted = []

 for (let i = 0; i < stageCards.length; i++){
     toBeDeleted.push(stageCards[i].name);
 }

 for (let i = 0; i < playerHand.length; i++){
     for (let j = 0; j < stageCards.length; j++){
         if (playerHand[i].name == stageCards[j].name){
             cardFound = true;
             break;
         }
     }
     if (!cardFound){
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

function removeUsedCardsServer(cards){
 stompClient.send("/app/discardCards", {}, JSON.stringify({
             'playerId': playerId,
             'cards': cards
     }));
}