package app.Models.StoryCards;
import java.util.ArrayList;

import app.Models.General.*;


public interface EventBehaviour {

    String playEvent(ArrayList<Player> players, Player drawer);
    
  
}