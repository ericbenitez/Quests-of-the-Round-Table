/**
 * The current active story card type
 */
let activeStoryCardType; 

/**
 * Displays a story card onto the UI
 * @param {StoryCard} storyCard 
 */

function displayStoryCard(storyCard) {
  const storyCardDiv = document.getElementById("activeStoryCard")
  storyCardDiv.style.display = "block";
  
  storyCardDiv.innerText = "";
  
  const name = storyCard.name;
  const storyCardType = storyCard.storyCardType;

  
  activeStoryCard = storyCard.name; //setting the global variable to info from the pickCard
  activeStoryCardType = storyCard.storyCardType;
  
  storyCardDiv.appendChild(document.createTextNode(name));
  storyCardDiv.appendChild(document.createElement("br"));
  storyCardDiv.appendChild(document.createTextNode(storyCardType));

  if(storyCardType==="Quest"){
    if (playerId === currentActivePlayer) {
      const isSponsoring = confirm("Do you want to sponsor?");
      if (isSponsoring) {
          alert("Cool, you can press on the button Sponsor Quest button!")
      }
      if (!isSponsoring) {
          alert("I see that you don't want to sponsor, press the Transfer Quest button")
      }
    
  }
  }
}
