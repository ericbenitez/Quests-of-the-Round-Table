// Global variable game
let game = null;
const url = "http://localhost:8080";
<<<<<<< Updated upstream
let stompClient = null;
=======
let stompClient;
>>>>>>> Stashed changes


// Tries to connect a player to the game
function connect() {
  let playerName = document.getElementById("player-name").value.trim();
  if (!validInputString(playerName)) {
//    alert("Please enter a name.");
    return;
  }
  var socket = new SockJS('/gameplay');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    //setConnected(true);
    console.log('Connected: ' + frame);
    attemptJoin();
<<<<<<< Updated upstream

    let currPlayersNum = 0;

    stompClient.subscribe('/topic/joinGame', function (response) {
      let data = JSON.parse(response.body);
      currPlayersNum = data.body;

=======
    stompClient.subscribe('/topic/joinGame', function (response) {
      let data = JSON.parse(response.body);
>>>>>>> Stashed changes
      // this should show whether the player successfully joined or not
      showResponse(data, playerName);

        // subscribe to get cards if player joined successfully
        if (data.body != null){
            subscribeToCards(currPlayersNum);
        }
    });


  });
}





// attempts to join a game
// this goes to the game controller
function attemptJoin() {
  stompClient.send("/app/playerJoining", {}, JSON.stringify({ 'player': $("#player-name").val(), 'game': game }));
}

function gettingCards(playerNum){
    stompClient.send("/app/playerGetCards", {}, JSON.stringify({
        'message': playerNum + ""
    }));
}


function subscribeToCards(currPlayersNum){
    var socket = new SockJS('/gameplay');
      stompClient = Stomp.over(socket);
      stompClient.connect({}, function (frame) {


        stompClient.subscribe('/topic/current-cards/' + currPlayersNum, function (response){
            let data = JSON.parse(response.body);
            displayAllCards(data);
            stompClient.disconnect();
            });
            gettingCards(currPlayersNum);

    });}



// creating a new game
function createGame() {
  let playerName = document.getElementById("creator-name").value.trim();
  let numPlayers = document.getElementById("num-players").value;
  // doesn't allow to create more than one game (for now)
  if (game != null) {
//    alert("There is already a game in progress.")
    return;
  }
  // ensures a valid input for player name
  if (!validInputString(playerName)) {
//    alert("Please enter a valid name.");
    return;
  }
  // ensures a valid input for num of players
  if (numPlayers < 2 || numPlayers > 4) {
//    alert("Please enter a valid number of players.");
    return;
  }
<<<<<<< Updated upstream


=======
//  alert("type: " + typeof(numPlayers) + " numPlayers: " + numPlayers);
>>>>>>> Stashed changes
  var socket = new SockJS('/gameplay');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    attemptCreate(playerName, numPlayers);
<<<<<<< Updated upstream

    stompClient.subscribe('/topic/game/started', function (response) {
      let data = JSON.parse(response.body);


=======
//    alert("here I am after attemp join buddy");
    stompClient.subscribe('/topic/game/started', function (response) {
//      alert("in subscribe")
      let data = JSON.parse(response.body);
//      alert("response full: " + response)
//      alert("data repsonse.body parse: " + data)
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream

}




function joinGame(){
    let playerName = document.getElementById("player-name").value;
    connect();
}



=======
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
//                   // alert("Congrats you're playing with: " + data.player1.login);
//                   alert(data);
                },
                error: function (error) {
                    console.log(error);
                }
            })
*/
}

// displays if the game is successfully created
function displayCreateGameResponse(data, playerName, numPlayers) {
//  alert("here in displaycreategame thing");
  let message = "The game cannot be created."
  if (data != null) {
//  alert(typeof(data));
//  alert("data: " + data);
  for (i in data){
//    alert(i);
    }
//    alert("try: " + JSON.parse(data.body).body);
    gameID = JSON.parse(data.body).body
    message = "The game " + gameID + " has successfully been created! Welcome to the game " + playerName + ". " +
      "This game will have " + numPlayers + " players."
  }
//  alert(message)
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

function joinGame(){
    let playerName = document.getElementById("player-name").value;
//    alert("joiningthe game: " + playerName);
    connect();
//    alert("after connecting");
    //attemptJoin();
}

//it would be better if we had player ids..
function getAdventureCards(){
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
>>>>>>> Stashed changes
