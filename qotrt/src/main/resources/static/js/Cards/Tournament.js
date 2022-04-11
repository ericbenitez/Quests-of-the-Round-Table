
let tournamentCards = [];
firstTournamentParticipantID = -1;



// have pop up appear to get player's answer for joining tournament
function askPlayerJoinTournament() {
    document.getElementById("popUpContainer").style.display = "block";
}

function hidePopUp() {
    document.getElementById("popUpContainer").style.display = "none";
}



// if the player clicks no, move to next player
// if yes, add player to participant or something and move to next player
function joinTournament(answer) {
    if (answer === "yes") {
        enableBidding();
        addParticipantTournament();

    }
    if (answer == "no") {
        scrollDiv("You have decided not to participate in the tournament :( click Finish Turn");
        disableBidding();
    }


    hidePopUp();
}


// ------------------- Tournament Funtionality (New Stuff) -------------------

function addParticipantTournament() {
    stompClient.send("/app/addParticipantTournament", {}, JSON.stringify({ "message": playerId }));
    getAdventureCards()
    scrollDiv("You received an adventure card!")
    scrollDiv("Choose cards for bidding in this tournament")
}


// checks if there is only one player in the tournament
// returns true/false
function singlePlayerTournament() {
    stompClient.send("/app/isSinglePlayerTournament", {});
    const singlePLayerSub = stompClient.subscribe('/user/queue/isSinglePlayerTournament', function (response) {
        let data = JSON.parse(response.body);
        if (data != -1) {
            if (playerId == data) {
                awardSingleTournament(playerId);
            }

        }
        singlePLayerSub.unsubscribe();
    });
}


// checks if the tournament has no participants
function emptyTournament() {
    stompClient.send("/app/emptyTournament", {});
    const emptyTournSub = stompClient.subscribe('/user/queue/emptyTournament', function (response) {
        let data = JSON.stringify(response);
        scrollDiv(data);
        emptyTournSub.unsubscribe();
    });
}


// gets award amount if only one player joins tournament
function autoAwardSingleParticipant() {
    stompClient.send("/app/autoAwardSingleTourn", {});
}


// checks if there is only one amour (which is max amount allowed) or less
// takes in array of card names
function containsMaxOneAmour(cards) {
    amourCount = 0;
    for (let i = 0; i < cards.length; i++) {
        if (cards[i] == "Amour") {
            amourCount++;
            if (amourCount >= 2) { return false; }
        }
    }
    return true;
}


function isTestCard(cardName) {
    let testCards = ["Test of the Questing Beast", "Test of Temptation", "Test of Valor", "Test of Morgan Le Fey"]
    for (let i = 0; i < testCards.length; i++) {
        if (testCards[i] == cardName) {
            return true;
        }
    }
    return false;
}

function hasTests(cardNames) {
    for (let i = 0; i < cardNames.length; i++) {
        if (isTestCard(cardNames[i])) {
            return true;
        }
    }
    return false;
}

function containsFoeOrTest(checked) {
    if (hasFoes(checked) || hasTests(checked)) {
        return true;
    }
    return false;
}



function placeCardsTournament() {
    // get the checked cards
    let checked = getAllChecked();
    let checkedObjs = getActualCards(checked);

    if (!allWeaponCardsUnique(checkedObjs)) {
        scrollDiv("You may not play two Weapon cards of the same type.");
        return;
    }
    if (!containsMaxOneAmour(checked)) {
        scrollDiv("Your may play a maximum of 1 Amour card.");
        return;
    }

    if (containsFoeOrTest(checked)) {

        scrollDiv("You may not play a foe or test in a tournament.");
        return;
    }
    tournamentCards = checkedObjs;

    removeAllCheckedCards(checked);


    // add it to the tournament hashmap playerCards, but don't display them until ALL players are ready
    // the server send something to display when all players ready
    stompClient.send("/app/addPlayerCardsTourn", {}, JSON.stringify({ "playerId": playerId, "cards": checked }));

    //alert them to click finish turn
    scrollDiv("Thank you for placing your bid, click Finish Turn");
}



// it will also display the winner, and award the shields etc
function displayAllCardsAtOnce() {
    stompClient.send("/app/getAllTournPlayerCards", {});

}



function hideTournamentDisplay() {
    clearTournamentDataDisplay();
    document.getElementById("tournament").style.display = "none";
}


// given a dictionary of playerId and total pts, calc the winner(s) or tie
function calcTournamentResult(playerPts) {
    maxPts = 0;
    winners = [];
    for (let player in playerPts) {
        if (playerPts[player] > maxPts) {
            maxPts = playerPts[player];
            winners = [player];
        } else if (playerPts[player] == maxPts) {
            winners.push(player);
        }
    }
    // display winner(s) or tie result
    displayTournResult(winners, maxPts);
    return winners;

}


function displayTournResult(winners, maxPts) {
    let resultDiv = document.getElementById("tournResults");
    if (winners.length > 1) {
        resultDiv.appendChild(document.createTextNode("TIE"));
        resultDiv.appendChild(document.createElement("br"));
    }
    for (let i = 0; i < winners.length; i++) {
        resultDiv.appendChild(document.createTextNode("Player " + winners[i] + " with " + maxPts + " battle points!"));
        resultDiv.appendChild(document.createElement("br"));
    }
}


function awardSingleWinner(winnerId) {
    stompClient.send("/app/awardSingleWinner", {}, JSON.stringify({ "message": winnerId }));
    const awardSub = stompClient.subscribe('/user/queue/awardSingleWinner', function (response) {
        let data = JSON.parse(response.body);
        scrollDiv("You now have: " + data + " shields");
        shields = data;
        updateShieldDisplay();
        awardSub.unsubscribe();

    });
}


function awardTiedWinner(winnerId) {
    stompClient.send("/app/awardTiedWinner", {}, JSON.stringify({ "message": winnerId }));
    const awardSub = stompClient.subscribe('/user/queue/awardTiedWinner', function (response) {
        let data = JSON.parse(response.body);
        scrollDiv("You (player " + winnerId + ") now have: " + data + " shields");
        shields = data;
        updateShieldDisplay();
        awardSub.unsubscribe();

    });
}


// if there is only one player who joined the tournament
function awardSingleTournament() {
    stompClient.send("/app/awardSingleGameWinner", {}/*, JSON.stringify({"message": playerId})*/);
    const awardSub = stompClient.subscribe('/user/queue/awardSingleGameWinner', function (response) {
        let data = JSON.parse(response.body);
        scrollDiv("You were the only player to join the tournament. You get " + data + " shields!");
        shields = data;
        updateShieldDisplay();
        // update the cards so they get the bidded cards back
        getPlayerHand();
        awardSub.unsubscribe();

    });
}


function discardCardsAfterTournament() {
    stompClient.send("/app/discardCardsAfterTournament", {});
}

function discardCardsAfterTie() {
    stompClient.send("/app/discardCardsAfterTie", {});
}


// clears the tournament board data
function clearTournamentDataDisplay() {
    for (let i = 0; i < 4; i++) {
        let id = i + 1;
        let playerCardDiv = document.getElementById("player" + id + "Cards");
        let playerTotal = document.getElementById("player" + id + "Total");


        while (playerCardDiv.firstChild) {
            playerCardDiv.removeChild(playerCardDiv.firstChild);
        }
        while (playerTotal.firstChild) {
            playerTotal.removeChild(playerTotal.firstChild);
        }
    }

    let resultDiv = document.getElementById("tournResults");

    while (resultDiv.firstChild) {
        resultDiv.removeChild(resultDiv.firstChild);
    }
}


