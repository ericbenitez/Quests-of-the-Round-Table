package app.Models.StoryCards;

import java.util.ArrayList;

import app.Models.General.Player;

public class KingsRecognition implements EventBehaviour {

  @Override
  public String playEvent(ArrayList<Player> players, Player drawer) {
    //  TODO Auto-generated method stub
     //drawer.getGame().requests.add("King's Recognition");
     
     return "The next player(s) to complete a Quest will receive 2 extra shields";
  }
}
