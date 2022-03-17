// ----------------------------------------------------------------------------
// VARIABLES
const url = "http://localhost:8080";
const socket = new SockJS("/gameplay");
const stompClient = Stomp.over(socket);
stompClient.connect()

let game = null;
let playerId = null;
let numOfPlayer = 0;
// ----------------------------------------------------------------------------
// PROGRAM
setupWindow()

// ----------------------------------------------------------------------------
// FUNCTIONS

/**
 * Tries to connect a player to the game
 * @returns void
 */
function connect(playerName) {
  //const playerName = document.getElementById("player-name").value.trim();
  if (!validInputString(playerName)) {
    alert("Please enter a name.");
    return;
  }

  // send request to server to join
  stompClient.send("/app/playerJoining", {}, JSON.stringify({ 'player': $("#player-name").val(), 'game': game }));

  stompClient.subscribe("/topic/joinGame", (response) => {
    const data = JSON.parse(response.body);
    playerId = data.body;
    showResponse(data, playerName);

    stompClient.subscribe("/topic/pickCard", (response) => {
      const data = JSON.parse(response.body).body;
      displayStoryCard(data);
    })
  })

}

// creating a new game
function createGame() {

  const playerName = document.getElementById("creator-name").value.trim();
  const numPlayers = document.getElementById("num-players").value;

  // doesn't allow to create more than one game (for now)
  if (game != null) {
    alert("There is already a game in progress.")
    return;
  }


  // ensures a valid input for player name
  if (!validInputString(playerName)) {
    alert("Please enter a valid name.");
    return;
  }

  // ensures a valid input for num of players
  if (numPlayers < 2 || numPlayers > 4) {
    alert("Please enter a valid number of players.");
    return;
  }

  attemptCreate(playerName, numPlayers);
  //here the game is successfully created...hide the info container
  // let container = document.getElementById("new-game-container");
  // // container.style.display = "hidden";
  // //also make the board visible...#


  stompClient.subscribe('/topic/game/started', function (response) {
    let data = JSON.parse(response.body);


    if (response != null) {
      game = response;
      // game is initialized, creator can now join the game
      connect(playerName);
    }

    displayCreateGameResponse(response, playerName, parseInt(numPlayers));
  });
}

function attemptCreate(player, numPlayers) {
  console.log("working")
  stompClient.send("/app/game/start", {},
    JSON.stringify({
      "player": player,
      "numOfPlayers": numPlayers * 1
    }));

  stompClient.subscribe("/topic/pickCard", function (response) {
    const data = JSON.parse(response.body).body
    displayStoryCard(data)
  })

}


/**
 * Send request to pick a story card
 */
function pickCard() {
  stompClient.send("/app/pickCard", {}, "");
}



function joinGame() {

  let playerName = document.getElementById("player-name").value.trim;
  connect(playerName);
}



function setupWindow() {
  window.addEventListener("load", displayAllCards(theCards));
}
