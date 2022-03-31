
function subscriptions() {

    stompClient.subscribe("/topic/testWinner", function(response){
        let data = JSON.parse(response.body);
        alert("The test was won by "+ data);
    })
    
  
  // subscribe to get tournamnet cards displayed, as well as tournament results
  const singlePLayerSub = stompClient.subscribe('/topic/getAllTournPlayerCards', function (response) {
    // clear it first = need to do this later
    let data = JSON.parse(response.body);
    let playerPts = {};
    for (let i = 0; i < data.length; i++){
        let id = i+ 1;
        console.log("id: " + id);
        let playerCardDiv = document.getElementById("player" + id + "Cards");
        if (data[i] == null){continue;}
        let totalPts = 0;
        for (let j = 0; j < data[i].length; j++){
            playerCardDiv.appendChild(document.createTextNode(data[i][j].name + ": " + data[i][j].battlePoints + " battle points"));
            playerCardDiv.appendChild(document.createElement("br"));
            totalPts += data[i][j].battlePoints;
        }
        // YOU NEED TO ADD ALLY PTS TOO
        document.getElementById("player" + id + "Total").appendChild(document.createTextNode(" " + totalPts + " battle points"));
        playerPts[id] = totalPts;
    }
    // calc winner(s) or result (tie)
    let allWinners = calcTournamentResult(playerPts);
    
    alert("allWinners: " + allWinners);
    if (allWinners.length == 1){
      alert("winner length = 1");
      if (playerId == allWinners[0]){
        awardSingleWinner(allWinners[0]);
        alert("YOU WON SOME SHIELDS BRUH");
      }
    }else {
      if (!tieBreakerPlayed){
        alert("There's a tie -- play the tie breaker round!");
        tieBreakerPlayed = true;
      }else {
        for (let i = 0; i < allWinners.length; i++){
          if (allWinners[i] == playerId){
            awardTiedWinner(playerId);
          }
        }
      }
      
    }
    
  });

  // tournament award/winner alert for single participant
  const autoAwardSingleSub = stompClient.subscribe('/topic/autoAwardSingleTourn', function (response) {
    let data = JSON.stringify(response);
    alert(data);
  });


    // subscribe to calculate stage winners
    stompClient.subscribe("/topic/calculateStage", function (response) {
      let data = JSON.parse(response.body);
      
      alert("Congratulations to: " + data);
    });
    
    //the response after setting the sponsor stages.
    stompClient.subscribe("/topic/setStages", function (response) { //need all players subscribe to this
      let data = JSON.parse(response.body); //should be an aray
  
      let stageSpecificDiv = document.createElement("div");
      let stageNumOfCardsDiv = document.createElement("div");
      let stages = data.stages;
      console.log("stages:" + stages);
      for (let i = 0; i < stages.length; i++) {
        stageSpecificDiv.append("Cards for Stages " + (i + 1));
        stageSpecificDiv.append(document.createElement("br"));
  
        stageNumOfCardsDiv.append("Cards for Stages " + (i + 1) + ": ");
        stageNumOfCardsDiv.append(stages[i].length + " cards");
        stageNumOfCardsDiv.append(document.createElement("br"));
        let hidden = document.createElement("div");
        hidden.setAttribute("id", "stage" + (i + 1));
        hidden.style.display = "none";
        stageNumOfCardsDiv.append(hidden);
        stageNumOfCardsDiv.append(document.createElement("br"));
  
        for (let j = 0; j < stages[i].length; j++) {
          stageSpecificDiv.append(stages[i][j]);
          stageSpecificDiv.append(document.createElement("br"));
  
          hidden.appendChild(document.createTextNode(stages[i][j]));
          hidden.appendChild(document.createElement("br"));
  
        }
      }
      if (data.sponsor == playerId) {
        document.getElementById("stages").appendChild(stageSpecificDiv);
      } else {
        stageSpecificDiv.style.display = "none";
        document.getElementById("stages").appendChild(stageNumOfCardsDiv);
      }
  
  
    });
    console.log("subscribed")
  
  
    const gameStartedSubscription = stompClient.subscribe('/topic/game/started', function (response) {
      let data = JSON.parse(response.body);
      if (response) game = response;
  
      gameId = game.gameID
  
      displayCreateGameResponse(data.body, playerName, parseInt(numOfPlayer));
  
      gameStartedSubscription.unsubscribe();
    });
  
    console.log("game started")
  
  
    const joinGameSubscription = stompClient.subscribe("/user/queue/joinGame", (response) => {
      const data = JSON.parse(response.body);
      playerId = data.body;
      showResponse(data, playerName);
  
      // setTimeout(() => { alert("Click on initialize cards to begin the game"); }, 2000);
      setTimeout(() => { stompClient.send("/app/ready", {}, ""); }, 2000);
      initializeAdv();
  
      joinGameSubscription.unsubscribe();
    })
  
    // subscribe to "wait for server to tell client to start"
    stompClient.subscribe("/topic/startTurn", (response) => { // does not get called
      console.log("This is after initilizing", response.body);
      if (response.body * 1 !== 0 && response.body * 1 === playerId) {
        alert("Pick a Story Card");
      }
    })
  
    //This function should also use session data and send it back.
    stompClient.subscribe("/topic/pickCard", function (response) {
      const data = JSON.parse(response.body);
      //console.log("From pick Card",data); //name: 'Slay the Dragon', drawer: null, storyCardType: 'Quest', totalStages: '3', foeName: 'Dragon', …}
      displayStoryCard(data);
    })

     //This function should also use session data and send it back.
     stompClient.subscribe("/topic/nextStageIsTest", function (response) {
        const data = JSON.parse(response.body);
        console.log(data);
      })
  
    //~~~~~~~~~~~~~~~~~~~~~~~~From finish Turn~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
  
  
    stompClient.subscribe("/topic/finishTurn", function (response) { //response = currentActiveplayer 
      let data = JSON.parse(response.body); //the id of the next active player..
      console.log(data);
      serverData = data;
  
      /**
       * {"currentActivePlayer":2,
       * "currentStoryCard":{"name":"Slay the Dragon","drawer":null,"storyCardType":"Quest","totalStages":"3","foeName":"Dragon","sponsor":1,"stages":[["Evil Knight","Saxons"],["Horse","Thieves"],["Mordred","Horse"]],"participantsId":[]},
       * "sponsorId":1,
       * "participantsId":[],
       * "questInPlay":false}
       * 
       * 
       */
      if (data.currentActivePlayer === playerId) {
        //activate their buttons
  
        // this needs work vv
        enableGameButtons();
        let currentStoryCard = data.currentStoryCard;
        if (currentStoryCard.storyCardType === "Quest") {
          //If the current story card type is quest, it could mean a few things
          //they're the sponsor, and it has looped back to them, the stage is complete
          if (currentStoryCard.sponsor === playerId && (currentStoryCard.currentStageNumber <= (currentStoryCard.totalStages * 1))) {
            //check winner for data.currentstages
            //CLEAR THE HASHMAP FOR CLIENT STAGE
            //checkWinner(currentStoryCard.clientStages, currentStoryCard.stages); //this function does the functionality of sending the appropriate reward
            //and moving the turn
            // request for winner
            stompClient.send("/app/calculateStage"); //the response to this will be subscriptions so that everybody gets to see the dying player
            //after this nothing happens so we need the sponsor to click finish quest
            //the surviving player are rewarded with an extra adventure card
            
            if(data.testInPlay){
                alert("The upcoming stage is a test");
                //send to server and broadcast it to all players
                stompClient.send("/app/nextStageIsTest");
                alert("click finish Turn");
            }
            alert("Hey Sponsor, click finish quest!");


          }
          //they're the sponsor and this is the last and total stage
          if (currentStoryCard.sponsor === playerId && currentStoryCard.currentStageNumber > currentStoryCard.totalStages) {
            //check the winners again and reward them
            //for the sponsor, it should check how many total cards they used in all of the stages + total stages
            //for example, alert(pick 6 cards for sponsoring the quest);
            //send something to the server, stomp.client(/app/setStoryCardToNull );
            alert("The Quest is complete!");
            //send some server things to clear the current quest
          }
          //another scenario is that the player is not the sponsor.
          if (currentStoryCard.sponsor != playerId) {
            if (!currentStoryCard.participantsId.includes(playerId) && currentStoryCard.currentStageNumber === 1) {
            //ask them to join
                alert("click join quest");
            }
            if (currentStoryCard.participantsId.includes(playerId) && currentStoryCard.currentStageNumber <= currentStoryCard.totalStages) {
                //pick cards for this stage
                //they've already joined the quset, they have to pick cards for the next stage or withdraw
                alert("Pick cards for stage # " ,currentStoryCard.currentStageNumber);
                if(data.testInPlay){
                    alert("This is a test");
                    let placeBidButton = document.createElement("button");
                    var t = document.createTextNode("Place Bid (Test)");
                    placeBidButton.appendChild(t);
                    placeBidButton.setAttribute("onclick", "placeTestBid()");
                    placeBidButton.setAttribute("id", "placeTestBid")
                    document.getElementById("placeCardButtons").appendChild(placeBidButton);
                }
                //place bid function through a button which takes in the server Data;
          }
          if (!currentStoryCard.participantsId.includes(playerId) && currentStoryCard.currentStageNumber != 1) {
            //this player refused to join the quest;
            //finishTurn();//increment the currentActivePlayer and move to the next player
            alert("here 3")
             }
         }
        }
        if (currentStoryCard.storyCardType === "Event") {
            
    
        }
        if (currentStoryCard.storyCardType === "Tournament") {

    
        }
        if (currentStoryCard.storyCardType === null) {
            //if the story card type is numm it means they might be the first player
            alert("pick a story card");
        }
  
    }
    else if (data.currentActivePlayer != playerId) {
        //disable their buttons!
        //disableButtons();
    }
  
    });
}
  
  //~~~~~~~~~~~~~Subscription ends here~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~