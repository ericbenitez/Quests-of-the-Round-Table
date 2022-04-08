package app.Models.StoryCards;

import java.util.ArrayList;

import app.Models.General.Player;

public class KingsRecognition implements EventBehaviour {

  @Override
  public String playEvent(ArrayList<Player> players, Player drawer) {
    for (Player player: players) {
      player.updateShields(2);
    }
    
    return "The next player(s) to complete a Quest will receive 2 extra shields";
  }
}
