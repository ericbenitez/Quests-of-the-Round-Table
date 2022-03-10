package app.Models.StoryCards;

import java.util.ArrayList;

import app.Models.Enums.Rank;
import app.Models.General.Player;

public class KingsCallToArms implements EventBehaviour {

  @Override
  public void playEvent(ArrayList<Player> players, Player drawer) {
    Rank highestRankValue = Rank.Squire;
    
    for (Player player: players) {
      if (player.getRankInt().compareTo(highestRankValue) == 1) {
        highestRankValue = player.getRankInt();
      }
    }
    
    for (Player player: players) {
      if (player.getRankInt() == highestRankValue) {
        player.drawCards(2);
        
        // TODO player input to select weapon card/foe cards
        // The highest ranked player must place 1 weapon in the discard pile, if unable discard 2 foe cards
      }
    }
  }
}
