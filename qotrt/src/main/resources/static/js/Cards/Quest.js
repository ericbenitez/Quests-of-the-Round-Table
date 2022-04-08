let currentQuest = ""; //name of the current quest taking place
let currentStages = 0; //the number of stages for the current quest
let foe = "";
let battlePointsLimit = 0;

function sponsorQuest(btn) {
    //console.log("here");
    displayTurnIndicator(true)
    sponsor = true;
    disableTransferQuestButton();
    stompClient.send("/app/sponsorQuest", {});
    stompClient.subscribe('/topic/sponsorQuest', function (response) {
        var data = JSON.parse(response.body);
        console.log("here is the data", data);
        currentQuest = data.name;
        currentStages = data.totalStages * 1;
        foe = data.foeName;
        console.log(currentQuest, currentStages, foe);
        setStages(currentQuest, currentStages, foe);

    });
    disableSponsorQuestAfterClick(btn);
}

let count = 1;
let selectedCards = 0;
let totalAdventureCardsWon = (count - 1) + selectedCards;
function setStages(currentQuest, currentStages, foe) {
    if (count <= currentStages) {
        scrollDiv("Pick cards for your " + count + "/" + currentStages + " stages");
        let dynamicButton = document.createElement("button");
        var t = document.createTextNode("Create Stage " + count);
        dynamicButton.appendChild(t);
        dynamicButton.setAttribute("onclick", "stageNumCards()");
        dynamicButton.setAttribute("id", "stageButton")
        document.getElementById("placeCardButtons").appendChild(dynamicButton);

    }
    else {
        scrollDiv("You have no more stages to set, click 'Finish Turn'! ");
        stompClient.send("/app/setStages", {}, JSON.stringify({ 'cards': myStages })); //sends the stages cards name to server
    }

}

let globalAmountOfFoes = 0;
let globalAmountOfTests = 0;
let globalAmountOfAmours = 0;

participants = [];
let maxBattlePoints = [];
//only one foe per stage(foe can be supplied different weapons tho), only one test per stage & quest
let myStages = [];
let testCards = ["Test of the Questing Beast", "Test of Temptation", "Test of Valor", "Test of Morgan Le Fey"];
function stageNumCards() {
    if (count === 1) {
        globalAmountOfFoes = 0;
        globalAmountOfTests = 0;
        globalAmountOfAmours = 0;
    }

    let checkedString = getAllChecked(); //returns checked cards
    let checked = getActualCards(checkedString);
    console.log("This is checked!", checked);
    //send the cards to server to place in array of stages.
    //place cards

    //check for test (it should be a single and only card for that stage)
    if ((checkedString.includes(testCards[0]) && checkedString.length > 1) ||
        (checkedString.includes(testCards[1]) && checkedString.length > 1) ||
        (checkedString.includes(testCards[2]) && checkedString.length > 1) ||
        (checkedString.includes(testCards[3]) && checkedString.length > 1)) {
        //there is a test card but the stage has more than one card
        scrollDiv("There should only be one test card!");
        return;
    }

    let amountOfBattlePoints = 0;
    let amountOfFoes = 0;
    let amountOfTests = 0;
    let amountOfAmours = 0;

    console.log(playerHand)
    const processedNames = [];

    // calculate battlepoints for stage (sponsor), and compare it to previous stage (only foes to account for)
    for (const cardName of checkedString) {
        if (processedNames.includes(cardName)) {
            scrollDiv("No repeating cards")
            return
        }

        processedNames.push(cardName)

        const card = CardObjects[cardName];

        if (card) {
            if (card.hasOwnProperty("battlePoints")) amountOfBattlePoints += card.battlePoints

            if (card.hasOwnProperty("minBattlePoints")) {
                switch (currentQuest) {
                    case "Journey Through the Enchanted Forest":
                        amountOfBattlePoints += (card.name == "Evil Knight") ? card.maxBattlePoints : card.minBattlePoints
                        break;

                    case "Repel the Saxon Raiders":
                        amountOfBattlePoints += (card.name == "Saxon Knight" || card.name == "Saxons") ? card.maxBattlePoints : card.minBattlePoints
                        break;

                    case "Boar Hunt":
                        amountOfBattlePoints += (card.name == "Boar") ? card.maxBattlePoints : card.minBattlePoints
                        break;

                    case "Slay the Dragon":
                        amountOfBattlePoints += (card.name == "Dragon") ? card.maxBattlePoints : card.minBattlePoints
                        break;

                    case "Rescue the Fair Maiden":
                        amountOfBattlePoints += (card.name == "Black Knight") ? card.maxBattlePoints : card.minBattlePoints
                        break;

                    case "Test of the Green Knight":
                        amountOfBattlePoints += (card.name == "Green Knight") ? card.maxBattlePoints : card.minBattlePoints
                        break;

                    default:
                        break;
                }
            }
            // if (card.hasOwnProperty("minBattlePoints")) amountOfBattlePoints += card.minBattlePoints
            if (card.cardType == "Foe") amountOfFoes++;

            if (card.cardType == "Test") {
                if (globalAmountOfTests >= 1) {
                    scrollDiv("You can only have 1 test when sponsoring the quest!")
                    return;
                } else {
                    amountOfTests++;
                }
            }

            if (card.cardType == "Amour") {
                if (globalAmountOfAmours >= 1) {
                    alert("You can only have 1 amour when sponsoring the quest!")
                    return;
                } else {
                    amountOfAmours++;
                }
            }
        }
    }

    if (amountOfBattlePoints <= battlePointsLimit && amountOfTests == 0) {
        scrollDiv("Not enough battle points, enter more or transfer the quest!")

        return
    }

    if (amountOfFoes == 0 && amountOfTests == 0) {
        scrollDiv("You need at least 1 foe or 1 test in this stage!")
        return
    }

    if (amountOfFoes > 1) {
        scrollDiv("You cannot have more than 1 foe per stage")
        return
    }

    globalAmountOfTests += amountOfTests;
    globalAmountOfFoes += amountOfFoes;
    globalAmountOfAmours += amountOfAmours;

    if (count + 1 > currentStages) {
        if (globalAmountOfFoes == 0) {
            scrollDiv("You need at least one foe in the quest!")
            return
        }
    }

    console.log("Battlepoints for this stage: " + amountOfBattlePoints)
    battlePointsLimit = amountOfBattlePoints;

    let battlePointsForThisStage = 0;
    //removeAllCheckedCards(checkedString);//remove from UI

    removeCardsFromHand(checkedString); //remove them from hand (server) + UI
    myStages.push(checkedString); //["hello","hi","he"]
    // myStages.push("/");
    for (let i = 0; i < checkedString.length; i++) {
        // myStages[count - 1].push(checkedString[i]);
        battlePointsForThisStage += checked[i].battlePoints; //[{"name":fs,"battlePoints":52},{gsgs}]
        selectedCards++;
    }
    //here make sure the battlepoints are greater than the previous ones... recall the function
    maxBattlePoints.push(battlePointsForThisStage);
    count++;
    //Remove things
    var elem = document.getElementById('stageButton');
    elem.parentNode.removeChild(elem);
    setStages(currentQuest, currentStages, foe);
}


function advCardsForSponsor() {
    scrollDiv("Pick this many adventure cards" + selectedCards);
}



// ----------------------------------- Player participating quest ---------------------------------
// player joining a quest, local and server 
function joinQuest() {
    // if (sponsor) { return; }
    participant = true;
    participants.push(currentActivePlayer);
    // change joinQuest button to withdraw button
    document.getElementById("joinQuest").style.display = "none";
    document.getElementById("withdrawQuest").style.display = "inline";

    // add participant to server side
    stompClient.send("/app/joinQuest", {}, JSON.stringify({
        'message': playerId + ""
    }));

    stompClient.subscribe("/user/queue/joinQuest", function (response) {
        let data = response.body;
        scrollDiv(data);
        // participantSetsStages();//dynamic button type thing for sponsor
    })
}


function withdrawQuest() {
    participant = false;
    document.getElementById("joinQuest").style.display = "inline";
    document.getElementById("withdrawQuest").style.display = "none";
    // should I disable the joinQuest button until the quest is over?
    disableJoinQuest();

    stageCards = [];

    // withdraw on server side
    stompClient.send("/app/withdrawQuest", {}, JSON.stringify({
        'message': playerId + ""
    }));
}

function transferQuest() {
    if (serverData.currentActivePlayer === playerId) {
        displayTurnIndicator(false)
        stompClient.send("/app/transferQuest", {}, playerId)
    }
}


// test function
// shows whether current player is in the quest or not
function inQuest() {
    scrollDiv("player " + playerId + " quest: " + participant);
}


// this is for the sponsor (disable the button after the player clicks on sponsor quest)
function disableJoinQuest() {
    document.getElementById("joinQuest").disabled = true;
}


function placeTestBid() {
    let testCard = serverData.testCard;
    let checkedString = getAllChecked(); //returns checked cards
    if (testCard.lastBid == 0) { //meaning this player is about to make the first bid so it should be greater than min Bid
        if (checkedString.length >= testCard.minBid) {
            //send their bid to the server
            stompClient.send("/app/placeTestBid", {}, JSON.stringify({
                'bids': checkedString
            }));
            removeCardsFromHand(checkedString); //remove them from hand (server) + UI
            var buttonTestBid = document.getElementById("placeTestBid");
            buttonTestBid.parentNode.removeChild(buttonTestBid);
        }
        else {
            scrollDiv("Your bid is less than the min bid! Try again or withdraw quest!");
        }
    }
    else if (testCard.bids.length > 0) {
        const lastBid = testCard.lastBid;
        if (checkedString.length > lastBid) {
            //send to server
            var buttonTestBid = document.getElementById("placeTestBid");
            buttonTestBid.parentNode.removeChild(buttonTestBid);
            stompClient.send("/app/placeTestBid", {}, JSON.stringify({ 'bids': checkedString }))
            removeCardsFromHand(checkedString); //remove them from hand (server) + UI

        }

        else {
            scrollDiv("The last bid was " + lastBid + " you need to increase your bid! If you can't, click withdraw quest!");
        }

    }

}


function showStage(stageNum) {

    stompClient.send("/app/showStage", {});
}



function clearStageCards() {
    let playerCardsDiv = document.getElementById("stages");
    while (playerCardsDiv.firstChild) {
        playerCardsDiv.removeChild(playerCardsDiv.firstChild);
    }

}

