let currentQuest = ""; //name of the current quest taking place
let currentStages = 0; //the number of stages for the current quest
let foe = "";
function sponsorQuest() {
    //console.log("here");
    sponsor = true;
    questHappening = true;
    stompClient.send("/app/sponsorQuest", {}, activeStoryCard);
    stompClient.subscribe('/topic/sponsorQuest', function (response) {
        var data = JSON.parse(response.body);
        //console.log("here is the data", data);
        currentQuest = data.name;
        currentStages = data.totalStages * 1;
        foe = data.foeName;
        console.log(currentQuest, currentStages, foe);
        setStages(currentQuest, currentStages, foe);

    });
}

let count = 1;
let selectedCards = 0;
let totalAdventureCardsWon = (count - 1) + selectedCards;
function setStages(currentQuest, currentStages, foe) {
    let stagesArea = document.getElementById("stages");
    if (count <= currentStages) {
        alert("pick cards for your " + count + "/" + currentStages + " stages");
        let stage = document.createElement("div");
        let dynamicButton = document.createElement("button");
        var t = document.createTextNode("Create Stage " + count);
        dynamicButton.appendChild(t);
        dynamicButton.setAttribute("onclick", "stageNumCards()");
        dynamicButton.setAttribute("id", "stageButton")
        document.getElementById("placeCardButtons").appendChild(dynamicButton)

    }
    else {
        alert("you have no more stages to set, click Complete Turn! ");
        stompClient.send("/app/setStages", {}, JSON.stringify({ 'cards': myStages })); //sends the stages cards name to server
        alert("click Finish Turn");
    }

}






participants = [];
//loop for the quest which only goes around the participating players..
// function getParticipants(currentActivePlayer) { //id of the sponsor
//     // currentActivePlayer + 1.. 
//     //the next player should get some notification
//     if (currentActivePlayer < numOfPlayer) {
//         if (currentActivePlayer + 1) { //next person to the sponsor
//             // tell server to ask next person if they wanna participate
//             //after they click on participate in quest
//             stomp.stompClient("/app/addParticipant", {}, ...);
//             // reponse. and we add the player to participants array {
//             //tells them to choose their cards for the stages
//         }
//         let answer = confirm("Do you want to participate in the quest " + currentQuest);
//         if (answer) {
//             //add them to the participants array
//             participants.push(currentActivePlayer + 1); //push their id into the participants array

//         }
//         if (!answer) {
//             alert("ok bye")
//             getParticipants(currentActivePlayer + 1)
//         }
//     }
// }
// if (currentActivePlayer === numOfPlayer) {
//     questBegins(participants);
// }


// function questBegins(participants) {
//     count = 1;
//     while (count <= currentStages) {
//         for (participant in participants) {
//             if (participant == playerId) {
//                 alert("choose cards for stage " + count);
//             }
//         }
//         //here we want to check the winners of stage 1
//         winStage(); //returns true or false, 
//         count++;
//     }
//     placeCardsQuest();//places the cards in the blue area
// }

let maxBattlePoints = [];
let myStages = [];
function stageNumCards() {
    let checkedString = getAllChecked(); //returns checked cards
    let checked = getActualCards(checkedString);
    console.log("This is checked!", checked);
    //send the cards to server to place in array of stages.
    //place cards
    let battlePointsForThisStage = 0;
    removeAllCheckedCards(checkedString);//remove from UI
    removeCardsFromHand(checkedString); //remove them from hand
    myStages.push(checkedString); //["hello","hi","he"]
    // myStages.push("/");
    for (let i = 0; i < checkedString.length; i++) {
        // myStages[count - 1].push(checkedString[i]);
        battlePointsForThisStage += checked[i].battlePoints; //[{"name":fs,"battlePoints":52},{gsgs}]
        selectedCards++;
    }
    maxBattlePoints.push(battlePointsForThisStage);
    count++;
    //Remove things
    var elem = document.getElementById('stageButton');
    elem.parentNode.removeChild(elem);
    setStages(currentQuest, currentStages, foe);
}


function advCardsForSponsor() {
    alert("pick this many adventure cards" + selectedCards);
}






// ----------------------------------- Player participating quest ---------------------------------
// player joining a quest, local and server 
function joinQuest() {
    if (sponsor) { return; }
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
        alert(data);
        // participantSetsStages();//dynamic button type thing for sponsor
    })
}


function withdrawQuest() {
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
function inQuest() {
    alert("player " + playerId + " quest: " + participant);
}


// this is for the sponsor (disable the button after the player clicks on sponsor quest)
function disableJoinQuest() {
    document.getElementById("joinQuest").disabled = true;
}
