package app.Models.StoryCards;
import java.util.ArrayList;

import Models.General.*;
import app.Models.General.Player;

public class Plague implements EventBehaviour {
   
   
    public void playEvent(ArrayList<Player> players, Player drawer) {
        
        drawer.updateShields(-2);    
        
    }

  
}