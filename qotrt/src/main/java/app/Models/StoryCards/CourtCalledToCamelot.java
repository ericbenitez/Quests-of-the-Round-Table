package app.Models.StoryCards;

import java.util.ArrayList;

import app.Models.AdventureCards.Ally;
import app.Models.General.Player;

public class CourtCalledToCamelot implements EventBehaviour {

  public String playEvent(ArrayList<Player> players, Player Drawer) {
    for (Player player : players) {
      for (int i = 0; i < player.activeAllies.size(); i++) {
        Ally ally = player.activeAllies.get(i);
        if (ally != null) {
          player.discardCard(ally.getName());
          player.activeAllies.remove(i);
        }
      }
    }
    
    return "All Allies in play were discarded";
  }
}