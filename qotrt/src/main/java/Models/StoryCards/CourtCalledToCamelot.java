package Models.StoryCards;

import java.util.ArrayList;
import Models.General.Player;
import Models.AdventureCards.AdventureCard;
import Models.AdventureCards.Ally;

public class CourtCalledToCamelot implements EventBehaviour {

  public void playEvent(ArrayList<Player> players, Player Drawer) {
    // loop through each player
    // check and delete for all allies in the player's hand

    for (Player player : players) {
      for (AdventureCard card : player.hand) {
        if (card instanceof Ally) {
          player.discardCardFromHand(((Ally) card).getName());
        }
      }
    }
  }

}