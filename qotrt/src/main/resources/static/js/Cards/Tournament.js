/* 
Thinking/Planning:

Winner = person with the most BATTLE POINTS

1) Tournament card is drawn
2) Each player decides whether to join the tournament (start at the drawer, then go left)
3) 1 person only = auto awarded one shield + bonus shields (on the card)

4) Each participant draws one card from adventure deck
5) Each participant chooses their cards: 
    5a) May choose: Ally, Weapon, Amour cards
        - can't play more than one weapon card of same type, max 1 amour card
        - they can choose no cards (only calc the rank+ally pts)

6) IN UNISON, all participants place cards FACE UP
7) Player with the most battle points (rank + ally + weapon + amour) wins tournament

8) End tournament: discard weapon and amour cards discarded
    (ally cards remain)

Winner prize = # of participants + bonus shields      (prize is basically shields)


---------------------------------------------------------------------------


TIE: 
(participants are now the players that were tied)

1) participants discard all weapon cards that were in play
    (ally, amour cards can stay)

2) Play again like in the first round (choose cards, calc winner, blah blah blah) (basically steps 4-7)
3) if TIE AGAIN: WINNER PRIZE = # of participants in the original tournament (shields)


*/


/*
Variables:
let tourParticipant = false;
let drawerTournament = 0;
*/
// array of actual cards (name, battlepts) that player placed
let tournamentCards = [];
firstTournamentParticipantID = -1;


// just an alert, probably wont need it once turn is incorporated
function startTournament(sessionData){
    alert(sessionData);
}


// have pop up appear to get player's answer for joining tournament
function askPlayerJoinTournament(){
    document.getElementById("popUpContainer").style.display = "block";
}

function hidePopUp(){
    document.getElementById("popUpContainer").style.display = "none";
}



// if the player clicks no, move to next player
// if yes, add player to participant or something and move to next player
function joinTournament(answer){
    if (answer === "yes"){
        addParticipantTournament();
    }
    if(answer=="no"){
        alert(" You have decided not to participate in the tournament :( click Finish Turn")
    }
    
    // move to next player
    // finishTurn();
    hidePopUp();
}


// ------------------- Tournament Funtionality (New Stuff) -------------------

function addParticipantTournament(){
    stompClient.send("/app/addParticipantTournament", {}, JSON.stringify({"message": playerId}));
    // we dont need to set participant to true cause it's in the session right?
    alert("Choose cards for bidding in this tournament")
}


// checks if there is only one player in the tournament
// returns true/false
function singlePlayerTournament(){
    stompClient.send("/app/isSinglePlayerTournament", {});
    const singlePLayerSub = stompClient.subscribe('/user/queue/isSinglePlayerTournament', function (response) {
        let data = JSON.parse(response.body);
        alert(data);
        if (data == true){
            // once we merge with the turns, we will obv have to check for the playerid before awarding
            awardSingleTournament(playerId);
        }
        singlePLayerSub.unsubscribe();
    });
}


// checks if the tournament has no participants
function emptyTournament(){
    stompClient.send("/app/emptyTournament", {});
    const emptyTournSub = stompClient.subscribe('/user/queue/emptyTournament', function (response) {
        let data = JSON.stringify(response);
        alert(data);
        emptyTournSub.unsubscribe();
    });
}


// gets award amount if only one player joins tournament
function autoAwardSingleParticipant(){
    stompClient.send("/app/autoAwardSingleTourn", {});
}


// checks if there is only one amour (which is max amount allowed) or less
// takes in array of card names
function containsMaxOneAmour(cards){
    amourCount = 0;
    for (let i = 0; i < cards.length; i++){
        if (cards[i] == "Amour"){
            amourCount++;
            if (amourCount >= 2){return false;}
        }
    }
    return true;
}



function placeCardsTournament(){
    // get the checked cards
    let checked = getAllChecked();
    let checkedObjs = getActualCards(checked);

    if (!allWeaponCardsUnique(checkedObjs)){
        alert("You may not play two Weapon cards of the same type.");
        return;
    }
    if (!containsMaxOneAmour(checked)){
        alert("Your may play a maximum of 1 Amour card.");
        return;
    }
    tournamentCards = checkedObjs;

    removeAllCheckedCards(checked);
    
    
    // add it to the tournament hashmap playerCards, but don't display them until ALL players are ready
    // the server send something to display when all players ready
    stompClient.send("/app/addPlayerCardsTourn", {}, JSON.stringify({"playerId": playerId, "cards": checked}));

    //alert them to click finish turn
    alert("Thank you for placing your bid, click Finish Turn");
}



// it will also display the winner, and award the shields etc
function displayAllCardsAtOnce(){
    stompClient.send("/app/getAllTournPlayerCards", {});
    
}

// will probably add more stuff to disable/enable
function disableBidding(){
    let bidButton = document.getElementById("bidReadyButton");
    bidButton.disabled = true;
    bidButton.className = "disabledButton";
}

function enableBidding(){
    let bidButton = document.getElementById("bidReadyButton");
    bidButton.disabled = false;
    bidButton.className = "";
}

function hideTournamentDisplay(){
    document.getElementById("tournament").style.display = "none";
}


// given a dictionary of playerId and total pts, calc the winner(s) or tie
function calcTournamentResult(playerPts){
    maxPts = 0;
    winners = [];
    for (let player in playerPts){
        if (playerPts[player] > maxPts){
            maxPts = playerPts[player];
            winners = [player];
        }else if (playerPts[player] == maxPts){
            winners.push(player);
        }
    }
    // display winner(s) or tie result
    displayTournResult(winners, maxPts);
    return winners;

}


function displayTournResult(winners, maxPts){
    let resultDiv = document.getElementById("tournResults");
    if (winners.length > 1){
        resultDiv.appendChild(document.createTextNode("TIE"));
        resultDiv.appendChild(document.createElement("br"));
    }
    for (let i = 0; i < winners.length; i++){
        resultDiv.appendChild(document.createTextNode("Player " + winners[i] + " with " + maxPts + " battle points!"));
        resultDiv.appendChild(document.createElement("br"));
    }
}


function awardSingleWinner(winnerId){
    stompClient.send("/app/awardSingleWinner", {}, JSON.stringify({"message": winnerId}));
    const awardSub = stompClient.subscribe('/user/queue/awardSingleWinner', function (response) {
        let data = JSON.parse(response.body);
        alert("You now have: " + data + " shields");
        shields = data;
        updateShieldDisplay();
        awardSub.unsubscribe();

    });
}


function awardTiedWinner(winnerId){
    stompClient.send("/app/awardTiedWinner", {}, JSON.stringify({"message": winnerId}));
    const awardSub = stompClient.subscribe('/user/queue/awardTiedWinner', function (response) {
        let data = JSON.parse(response.body);
        alert("You (player " + winnerId + ") now have: " + data + " shields");
        shields = data;
        updateShieldDisplay();
        awardSub.unsubscribe();
   
    });
}


// if there is only one player who joined the tournament
function awardSingleTournament(playerId){
    stompClient.send("/app/awardSingleGameWinner", {}, JSON.stringify({"message": playerId}));
    const awardSub = stompClient.subscribe('/user/queue/awardSingleGameWinner', function (response) {
        let data = JSON.parse(response.body);
        alert("You were the only player to join the tournament. You get " + data + " shields!");
        shields = data;
        updateShieldDisplay();
        awardSub.unsubscribe();

    });
}


function discardCardsAfterTournament(){
    stompClient.send("/app/discardCardsAfterTournament", {});
}

function discardCardsAfterTie(){
    stompClient.send("/app/discardCardsAfterTie", {});
}

// Note: need to add ally stuff to tournaments