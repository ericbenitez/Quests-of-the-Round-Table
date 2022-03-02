package Models.StoryCards;

import java.util.ArrayList;

import Models.General.Player;

public class KingsRecognition implements EventBehaviour {

  @Override
  public void playEvent(ArrayList<Player> players, Player drawer) {
    // TODO Auto-generated method stub
    drawer.getGame().requests.add("King's Recognition");
  }
}
