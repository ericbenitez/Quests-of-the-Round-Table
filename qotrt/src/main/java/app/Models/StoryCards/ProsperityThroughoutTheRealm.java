package app.Models.StoryCards;

import java.util.ArrayList;

import app.Models.General.Player;

public class ProsperityThroughoutTheRealm implements EventBehaviour {

  @Override
  public String playEvent(ArrayList<Player> players, Player drawer) {
    for (Player player : players) {
      player.drawCards(2);
    }
    
    return "All players drew 2 Adventure cards";
  }
}
