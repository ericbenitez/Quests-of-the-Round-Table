package app.Models.StoryCards;

import java.util.ArrayList;

import app.Models.AdventureCards.Ally;
import app.Models.General.Player;

public class CourtCalledToCamelot implements EventBehaviour {

  public String playEvent(ArrayList<Player> players, Player Drawer) {
    // loop through each player
    // check and delete for all allies in the player's hand

    for (Player player : players) {
      //add them to the discarded pile?
      if(player.activeAllies.size()==0){
        return "All Allies in play must be discarded";
      }
      while(player.activeAllies.size()!=0){
        int index = player.activeAllies.size()-1;
        Ally card = player.activeAllies.get(index);
        player.discardCard(card.getName()); //add it to discard pile
        player.activeAllies.remove(index); //remove it from the array 
      }
      
    }
    
    return "All Allies in play must be discarded";
  }
}