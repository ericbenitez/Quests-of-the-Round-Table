/**
 * Displays a story card onto the UI
 * @param {StoryCard} storyCard 
 */
 function displayStoryCard(storyCardName) {
    const storyCardDiv = document.getElementById("activeStoryCard")
    storyCardDiv.style.display = "block";
    const message = storyCardName
    storyCardDiv.appendChild(document.createTextNode(message))
  }