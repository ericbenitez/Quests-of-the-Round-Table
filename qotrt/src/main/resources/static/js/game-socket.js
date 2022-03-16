// Global variable game
let game = null;
const url = "http://localhost:8080";
let stompClient = null;


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

    let currPlayersNum = 0;

    stompClient.subscribe('/topic/joinGame', function (response) {
      let data = JSON.parse(response.body);
      currPlayersNum = data.body;

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


  var socket = new SockJS('/gameplay');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {

    console.log('Connected: ' + frame);
    attemptCreate(playerName, numPlayers);

    stompClient.subscribe('/topic/game/started', function (response) {
      let data = JSON.parse(response.body);


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

}




function joinGame(){
    let playerName = document.getElementById("player-name").value;
    connect();
}
/it would be better if we had player ids..
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



