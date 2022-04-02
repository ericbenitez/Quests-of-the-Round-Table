package app.Models.StoryCards;
import java.util.ArrayList;

import app.Models.General.*;

public class Plague implements EventBehaviour {
   
   
    public String playEvent(ArrayList<Player> players, Player drawer) {
        
        drawer.updateShields(-2);    
        return "Drawer loses 2 shields if possible";
    }
}