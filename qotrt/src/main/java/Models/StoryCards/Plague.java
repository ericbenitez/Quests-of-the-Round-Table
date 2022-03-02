package Models.StoryCards;
import java.util.ArrayList;

import Models.General.*;

public class Plague implements EventBehaviour {
   
   
    public void playEvent(ArrayList<Player> players, Player drawer) {
        
        drawer.updateShields(-2);    
        
    }

  
}