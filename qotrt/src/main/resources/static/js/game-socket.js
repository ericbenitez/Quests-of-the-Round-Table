// Global variable game
let game = null;
const url = "http://localhost:8080";
var stompClient;


window.addEventListener("load", function () {
  document.getElementById("create-game-button").addEventListener("click", createGame);
});


window.addEventListener("load", function () {
  document.getElementById("join-button").addEventListener("click", joinGame);
});




// Tries to connect a player to the game
function connect() {

  let playerName = document.getElementById("player-name").value.trim();
  if (!validInputString(playerName)) {
    alert("Please enter a name.");
    return;
  }

  var socket = new SockJS('/gameplay');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    //setConnected(true);
    console.log('Connected: ' + frame);
    attemptJoin();

    stompClient.subscribe('/topic/joinGame', function (response) {
      let data = JSON.parse(response.body);

      // this should show whether the player successfully joined or not
      showResponse(data, playerName);
    });
    
    stompClient.subscribe("/topic/pickCard", function(response) {
      const data = JSON.parse(response.body).body
      displayStoryCard(data)
    })
  });
}



// attempts to join a game
// this goes to the game controller
function attemptJoin() {
  stompClient.send("/app/playerJoining", {}, JSON.stringify({ 'player': $("#player-name").val(), 'game': game }));
}



// creating a new game
function createGame() {

  let playerName = document.getElementById("creator-name").value.trim();
  let numPlayers = document.getElementById("num-players").value;

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
  alert("type: " + typeof(numPlayers) + " numPlayers: " + numPlayers);


  var socket = new SockJS('/gameplay');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {

    console.log('Connected: ' + frame);
    attemptCreate(playerName, numPlayers);

    alert("here I am after attemp join buddy");
    stompClient.subscribe('/topic/game/started', function (response) {
        alert("in subscribe")
      let data = JSON.parse(response.body);
      alert("response full: " + response)
      alert("data repsonse.body parse: " + data)

      if (response != null){
      game = response;}

      displayCreateGameResponse(response, playerName, parseInt(numPlayers));
    });
  });



}

function attemptCreate(player, numPlayers){
    console.log("working")
    stompClient.send("/app/game/start", {},
    JSON.stringify({
                    "player": player,
                    "numOfPlayers": numPlayers *1
                }));
                
  stompClient.subscribe("/topic/pickCard", function(response) {
    const data = JSON.parse(response.body).body
    displayStoryCard(data)
  })
  
    //stompClient.send("/app/game/start",{}, JSON.stringify({"info": player + " " + numPlayers.toString()}));
   /* $.ajax({
                url: url + "/game/start",
                type: 'POST',
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({
                    "player":  player
                    ,
                    "numOfPlayers": numPlayers
                }),
                success: function (data) {
                    //game = data.gameId;
                    //playerType = 'O';
                    //reset();
                    //connectToSocket(gameId);
                   // alert("Congrats you're playing with: " + data.player1.login);
                   alert(data);
                },
                error: function (error) {
                    console.log(error);
                }
            })
*/
}

// displays if the game is successfully created
function displayCreateGameResponse(data, playerName, numPlayers) {
    alert("here in displaycreategame thing");

  let message = "The game cannot be created."
  if (data != null) {
  alert(typeof(data));
  alert("data: " + data);
  for (i in data){
    alert(i);
    }
    alert("try: " + JSON.parse(data.body).body);
    gameID = JSON.parse(data.body).body

    message = "The game " + gameID + " has successfully been created! Welcome to the game " + playerName + ". " +
      "This game will have " + numPlayers + " players."
  }
  alert(message)

  let newGameMessage = document.getElementById("game-message");
  clearMessage();
  newGameMessage.appendChild(document.createTextNode(message));
  newGameMessage.appendChild(document.createElement("br"));

}



/**
 * Send request to pick a story card
 */
function pickCard() {
  stompClient.send("/app/pickCard", {}, "");
}

/**
 * Displays a story card onto the UI
 * @param {StoryCard} storyCard 
 */
function displayStoryCard(storyCardName) {
  const storyCardDiv = document.getElementById("activeStoryCard")
  storyCardDiv.style.display = "block";
  const message = storyCardName
  storyCardDiv.appendChild(document.createTextNode(message))
}

/**
 * Will deal a card
 */
function deal() {
  
}



// clears the game message box
function clearMessage() {
  let joinMessage = document.getElementById("game-message");
  while (joinMessage.firstChild) {
    joinMessage.removeChild(joinMessage.firstChild);
  }
}






function joinGame(){
    let playerName = document.getElementById("player-name").value;
    alert("joiningthe game: " + playerName);
    connect();
    alert("after connecting");
    //attemptJoin();
}
