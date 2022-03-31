/**
 * The current active story card type
 */
let activeStoryCardType; 

/**
 * Displays a story card onto the UI
 * @param {StoryCard} storyCard 
 */
//~~~~~~~~~~Pick Card Function calls this ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 /**
       * {"currentActivePlayer":2,
       * "currentStoryCard":{"name":"Slay the Dragon","drawer":null,"storyCardType":"Quest","totalStages":"3","foeName":"Dragon","sponsor":1,"stages":[["Evil Knight","Saxons"],["Horse","Thieves"],["Mordred","Horse"]],"participantsId":[]},
       * "sponsorId":1,
       * "participantsId":[],
       * "questInPlay":false}
       * 
       * 
       */
function displayStoryCard(data) {
  const storyCardDiv = document.getElementById("activeStoryCard")
  storyCardDiv.style.display = "block";
  storyCardDiv.innerText = "";
  
  const name = data.currentStoryCard.name;
  const storyCardType = data.currentStoryCard.storyCardType;

  
  activeStoryCard = data.currentStoryCard.name;; //setting the global variable to info from the pickCard
  activeStoryCardType = data.currentStoryCard.storyCardType;
  
  storyCardDiv.appendChild(document.createTextNode(name));
  storyCardDiv.appendChild(document.createElement("br"));
  storyCardDiv.appendChild(document.createTextNode(storyCardType));

  if(storyCardType==="Quest"){
    if (playerId === data.currentActivePlayer) {
      const isSponsoring = confirm("Do you want to sponsor?");
      if (isSponsoring) {
          alert("Cool, you can press on the button Sponsor Quest button!")
      }
      if (!isSponsoring) {
          alert("I see that you don't want to sponsor, press the Transfer Quest button")
      }
    
  }
  }else if (storyCardType === "Tournament"){
    storyCardDiv.appendChild(document.createTextNode(storyCard.currentStory));

    // we can immediately start asking if the drawer would like to join
    drawerTournament = playerId;
    if (playerId === data.currentActivePlayer){
      startTournament(storyCard);
      askPlayerJoinTournament();
    }
    
  }


  else if (storyCardType === "Event"){
    alert("Click on Perform Event!")
  }
}
