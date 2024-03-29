
function subscriptions() {

  stompClient.subscribe("/topic/getShields", function (response) {
    const players = JSON.parse(response.body)

    for (const player of players) {
      if (player.id == playerId) {
        const shieldsDiv = document.getElementById("shields")
        shieldsDiv.innerHTML = ""
        shieldsDiv.append(document.createTextNode(`You have ${player.shields} shields.`))
      }
    }


  });


  stompClient.subscribe("/topic/showStage", function (response) { //response = currentActiveplayer 
    let sponsorId = JSON.parse(response.body);
    //showCurrentStage(stageNum);
    // player cards face up
    if (playerId != sponsorId) {
      turnCardsOver();
      showCurrentStage(serverData.currentStoryCard.currentStageNumber - 1);
    }
  });

  stompClient.subscribe("/topic/clearTournament", function (response) {
    tieBreakerPlayed = false;
    tieOccurred = false;
    hideTournamentDisplay();
    clearStageCards();
  });

  const playEventSubscription = stompClient.subscribe("/topic/playEvent", (response) => {
    const data = JSON.parse(response.body)

    const eventMessage = document.getElementById("eventMessage")
    eventMessage.innerHTML = ""
    eventMessage.appendChild(document.createTextNode(`Event: ${data.message}`))

    for (const player of data.players) {
      if (player.id === playerId) {
        const shieldsDiv = document.getElementById("shields")
        shieldsDiv.innerHTML = ""
        shieldsDiv.append(document.createTextNode(`You have ${player.shields} shields.`))

        // update cards
        getPlayerHand()

        break;
      }
    }

    if (serverData.currentActivePlayer === playerId) {
      scrollDiv("An event has been played, select 'Finish Turn'!")
    }
  })

  const transferQuestSubscription = stompClient.subscribe("/topic/transferQuest", (response) => {
    if (response.body * 1 === -1 && playerId === serverData.currentActivePlayer) {
      finishTurn(document.getElementById("finishTurn"))
      return
    }

    // current active player after a transfer
    serverData.currentActivePlayer = response.body * 1

    // if its us...
    if (playerId === serverData.currentActivePlayer) {
      displayTurnIndicator(true)

      const isSponsoring = confirm("Do you want to sponsor?");
      if (isSponsoring) {
        scrollDiv("Cool, you can press on the button Sponsor Quest button!")
      }
      if (!isSponsoring) {
        scrollDiv("I see that you don't want to sponsor, press the Transfer Quest button")
      }
    }
  })


  stompClient.subscribe("/topic/testWinner", function (response) {
    let playerName = response.body;
    scrollDiv("The test was won by " + playerName);
  })


  // subscribe to get tournamnet cards displayed, as well as tournament results
  const singlePLayerSub = stompClient.subscribe('/topic/getAllTournPlayerCards', function (response) {
    // clear it first = need to do this later
    document.getElementById("tournament").style.display = "flex";

    let data = JSON.parse(response.body);
    if (data.length == 0) {
      singlePlayerTournament();
      return;
    }

    let playerPts = {};
    for (let i = 0; i < data.length; i++) {
      let id = i + 1;
      console.log("id: " + id);
      let playerCardDiv = document.getElementById("player" + id + "Cards");
      if (data[i] == null) { continue; }
      let totalPts = 0;
      for (let j = 0; j < data[i].length; j++) {
        playerCardDiv.appendChild(document.createTextNode(data[i][j].name + ": " + data[i][j].battlePoints + " battle points"));
        playerCardDiv.appendChild(document.createElement("br"));
        totalPts += data[i][j].battlePoints;
      }
      // YOU NEED TO ADD ALLY PTS TOO - update: ally should be added now, just needs testing
      document.getElementById("player" + id + "Total").appendChild(document.createTextNode(" " + totalPts + " battle points"));
      playerPts[id] = totalPts;
    }
    // calc winner(s) or result (tie)
    let allWinners = calcTournamentResult(playerPts);

    scrollDiv("allWinners: " + allWinners);
    if (allWinners.length == 1) {
      //alert("winner length = 1");
      if (playerId == allWinners[0]) {
        awardSingleWinner(allWinners[0]);
        scrollDiv("YOU WON SOME SHIELDS BRUH");
      }
    } else {
      if (!tieBreakerPlayed) {
        //alert("There's a tie -- play the tie breaker round!");
        tieOccurred = true;
        if (playerId == firstTournamentParticipantID) {
          scrollDiv("Place cards for the sigh almighty tie breaker round!");
          tieBreakerPlayed = true;
          displayTurnIndicator(true);
        }
      } else {
        for (let i = 0; i < allWinners.length; i++) {
          if (allWinners[i] == playerId) {
            awardTiedWinner(playerId);
          }
        }
      }

    }

  });

  // tournament award/winner alert for single participant
  const autoAwardSingleSub = stompClient.subscribe('/topic/autoAwardSingleTourn', function (response) {
    let data = JSON.stringify(response);
    scrollDiv(data);
  });


  // subscribe to calculate stage winners
  stompClient.subscribe("/topic/calculateStage", function (response) {
    let data = JSON.parse(response.body);

    // remove amour
    let index = 0;
    for (const cardName of playerHand) {
      const card = CardObjects[cardName];

      if (card) {
        if (card.cardType === "Amour") {
          playerHand.remove(index);
          removeUsedCardsServer([cardName]);
        }
      }

      index++;
    }

    stompClient.send("/app/getShields");


    scrollDiv("Congratulations to: " + data);
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
    //displayCreateGameResponse(data.body, playerName, parseInt(numOfPlayer));

    gameStartedSubscription.unsubscribe();
  });

  console.log("game started")


  const joinGameSubscription = stompClient.subscribe("/user/queue/joinGame", (response) => {
    const data = JSON.parse(response.body);
    playerId = data.body;
    showResponse(data, playerName);
    showPlayerInfoDisplay();

    // setTimeout(() => { alert("Click on initialize cards to begin the game"); }, 2000);
    setTimeout(() => { stompClient.send("/app/ready", {}, ""); }, 2000);
    initializeAdv();

    joinGameSubscription.unsubscribe();
  })

  // subscribe to "wait for server to tell client to start"
  stompClient.subscribe("/topic/startTurn", (response) => { // does not get called
    serverData.currentActivePlayer = response.body * 1;

    stompClient.send("/app/getShields")

    // hide new game container
    const newGameContainer = document.getElementById("new-game-container")
    newGameContainer.style.display = "none"

    const newPlayerJoin = document.getElementById("new-player-join")
    newPlayerJoin.style.display = "none"

    // enable game area
    const gameArea = document.getElementById("gameArea")
    gameArea.style.display = "flex"



    // disable buttons

    console.log("This is after initilizing", response.body);
    if (response.body * 1 !== 0 && response.body * 1 === playerId) {
      displayTurnIndicator(true);
      scrollDiv("Pick a Story Card"); // player 3
    }
  })

  //This function should also use session data and send it back.
  stompClient.subscribe("/topic/pickCard", function (response) {
    // console.log("picked card")
    const data = JSON.parse(response.body);
    console.log("From pick Card", data); //name: 'Slay the Dragon', drawer: null, storyCardType: 'Quest', totalStages: '3', foeName: 'Dragon', …}
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
    console.log("Here is the data from the server", data);
    serverData = data;
    // clear event card display
    const eventMessage = document.getElementById("eventMessage");
    eventMessage.innerHTML = "";
    // checks if there are any winners
    if (data.winners.length > 0) {
      scrollDiv("The game is over! Congratulations to the winner(s): " + data.winners);
      for (let i = 0; i < winners.length; i++) {
        updateRankToKnight();
      }
      // update the rank display
      return;
    }

    if (data.currentActivePlayer === playerId) {
      displayTurnIndicator(true);
      //activate their buttons

      // this needs work vv
      // enableGameButtons();
      let currentStoryCard = data.currentStoryCard;
      if (currentStoryCard) {
        if (currentStoryCard.storyCardType === "Quest") {
          //If the current story card type is quest, it could mean a few things
          //they're the sponsor, and it has looped back to them, the stage is complete
          if (currentStoryCard.sponsor === playerId && (currentStoryCard.currentStageNumber <= (currentStoryCard.totalStages * 1))) {
            //check winner for data.currentstages
            //CLEAR THE HASHMAP FOR CLIENT STAGE
            //checkWinner(currentStoryCard.clientStages, currentStoryCard.stages); //this function does the functionality of sending the appropriate reward
            //and moving the turn
            // request for winner

            // when a stage is over, we have to have the player cards faced up (it's currently faced down)

            // when a stage is over, the sponsor stage cards have to be visible to the participants
            currentStageNumber = currentStoryCard.currentStageNumber;
            showStage(currentStageNumber);
            alreadyASponsor(); //button
            stompClient.send("/app/calculateStage"); //the response to this will be subscriptions so that everybody gets to see the dying player
            //after this nothing happens so we need the sponsor to click finish quest
            //the surviving player are rewarded with an extra adventure card
            // clearPlayerStageCards();
            if (data.testInPlay) {
              scrollDiv("The upcoming stage is a test");
              //send to server and broadcast it to all players
              stompClient.send("/app/nextStageIsTest");
              scrollDiv("click finish Turn");
            }
            scrollDiv("Hey Sponsor, click finish Turn!");

          }
          //they're the sponsor and this is the last and total stage
          if (currentStoryCard.sponsor === playerId && currentStoryCard.currentStageNumber > currentStoryCard.totalStages) {
            //check the winners again and reward them
            //for the sponsor, it should check how many total cards they used in all of the stages + total stages
            //for example, alert(pick 6 cards for sponsoring the quest);
            //send something to the server, stomp.client(/app/setStoryCardToNull );
            currentStageNumber = currentStoryCard.currentStageNumber;
            stompClient.send("/app/calculateStage"); //the response to this will be subscriptions so that everybody gets to see the dying player
            showStage(currentStageNumber);
            alreadyASponsor();
            scrollDiv("The Quest is complete! Giving you as the sponsor adventure cards!");
            scrollDiv("Click on Finish turn");
            //send some server things to clear the current quest
            stompClient.send("/app/rewardSponsor");
            stompClient.send("/app/setQuestInPlayFalse");

            getPlayerHand();
          }
          //another scenario is that the player is not the sponsor.
          if (currentStoryCard.sponsor != playerId) {
            if (!currentStoryCard.participantsId.includes(playerId) && currentStoryCard.currentStageNumber === 1) {
              //ask them to join
              newQuestJoiners();
              scrollDiv("click join quest");

              if (data.testInPlay) {
                // draw adventure card before bidding
                if (data.currentActivePlayer === playerId) {
                  displayTurnIndicator(true)
                }
                scrollDiv("This is a test");
                let placeBidButton = document.createElement("button");
                var t = document.createTextNode("Place Bid (Test)");
                placeBidButton.appendChild(t);
                placeBidButton.setAttribute("onclick", "placeTestBid()");
                placeBidButton.setAttribute("id", "placeTestBid");
                document.getElementById("placeCardButtons").appendChild(placeBidButton);
              }
            }
            if (currentStoryCard.participantsId.includes(playerId) && currentStoryCard.currentStageNumber <= currentStoryCard.totalStages) {
              //pariticpiants of the quest
              //withdraw, place their cards, finish the turn 
              questParticBtns();

              //pick cards for this stage
              //they've already joined the quset, they have to pick cards for the next stage or withdraw
              currentStageNumber = currentStoryCard.currentStageNumber;
              displayTurnIndicator(true);

              if (playerHand.length < 12) {
                getAdventureCards();
              }

              scrollDiv("Pick cards for stage # " + currentStoryCard.currentStageNumber);

              if (data.testInPlay) {
                // draw adventure card before bidding
                scrollDiv("This is a test");
                let placeBidButton = document.createElement("button");
                var t = document.createTextNode("Place Bid (Test)");
                placeBidButton.appendChild(t);
                placeBidButton.setAttribute("onclick", "placeTestBid()");
                placeBidButton.setAttribute("id", "placeTestBid");
                document.getElementById("placeCardButtons").appendChild(placeBidButton);

                if(currentStoryCard.participantsId.length ==1){
                  scrollDiv("All players have dropped out of the test! Click Finish Turn!");
                   var buttonTestBid = document.getElementById("placeTestBid");
                  buttonTestBid.parentNode.removeChild(buttonTestBid);
                  return;
                }
          
              }
              //place bid function through a button which takes in the server Data;
            }
            if (!currentStoryCard.participantsId.includes(playerId) && currentStoryCard.currentStageNumber != 1) {
              //this player refused to join the quest;
              //finishTurn();//increment the currentActivePlayer and move to the next player
              disableButtons();
              scrollDiv("You decided not to Join the Quest Or you lost previous round! So we're skipping your turn :P");
              finishTurn(document.getElementById("finishTurn"));
              displayTurnIndicator(false);
            }
          }
        }
        if (currentStoryCard.storyCardType === "Event") {


        }
        if (currentStoryCard.storyCardType === "Tournament" && data.tournamentInPlay) {
          //pop up, yes <-- enable bidding for this otherwise we keep the bid button disabled
          tournamentParticBtns();
          //we round back to the first player who first picked the tournament card
          if (currentStoryCard.firstParticipantId === playerId) {
            //alert("the tournament has ended, click finish turn !");

            firstTournamentParticipantID = playerId

            displayAllCardsAtOnce();

          }
          //the first player clicked finish turn after placing their bids
          //if the participants is not a participant , ask them to join the tournament 
          if (!currentStoryCard.participants.includes(playerId) && !data.tieBreakerPlayed && data.tournamentInPlay) {
            askPlayerJoinTournament();

          } else if (currentStoryCard.participants.includes(playerId)) {
            if (tieOccurred && !tieBreakerPlayed) {
              tournamentParticBtns();
              scrollDiv("Place cards for the almighty tie breaker round!");
              tieBreakerPlayed = true;
              displayTurnIndicator(true)
            }
          }
        }
      }

      if (!data.questInPlay && !data.tournamentInPlay && !data.eventInPlay) {
        //if the story card type is numm it means they might be the first player
        newRound();
        console.log("The current Active player -->", data.currentActivePlayer);
        if (data.currentActivePlayer === playerId) {
          displayTurnIndicator(true)
          scrollDiv("Pick a story card"); // player 2
        }
        stompClient.send("/app/clearTournament", {});
        // disableButtons()
      }

      //the tournament
    } //the cuurent active player's stuff ends here
    
    // if not current active player...
    else {
      //disable their buttons!
      
      disableButtons();
      displayTurnIndicator(false);
    }
  });

}
//~~~~~~~~~~~~~Subscription ends here~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~