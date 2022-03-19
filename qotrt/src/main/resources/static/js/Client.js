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

  stompClient.subscribe("/topic/pickCard", (response) => {
    const data = JSON.parse(response.body).body;
    displayStoryCard(data);
  })
  
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
  const joinGameSubscription = stompClient.subscribe("/user/topic/joinGame", (response) => {
    const data = JSON.parse(response.body);
    playerId = data.body;
    showResponse(data, playerName);

    stompClient.subscribe("/topic/pickCard", (response) => {
      const data = JSON.parse(response.body).body;
      displayStoryCard(data);
    })
    
    joinGameSubscription.unsubscribe();
  })

  const gameStartedSubscription = stompClient.subscribe('/topic/game/started', function (response) {
    let data = JSON.parse(response.body);
    if (response) game = response;

    gameId = game.gameID

    displayCreateGameResponse(response.gameID, playerName, parseInt(numPlayers))

    gameStartedSubscription.unsubscribe();
    startGame()
  });
}

function setupWindow() {
  window.addEventListener("load", displayAllCards(theCards));
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
