package app.Models.StoryCards;

import java.util.ArrayList;
import app.Models.General.*;

public abstract class StoryCard extends Card {

    protected Player drawer;

    public void setDrawer(Player player){
        this.drawer = player;
    }

    public Player getDrawer() { return this.drawer; }

    public void playEvent(ArrayList<Player> players) {
    }
    /**
     * Returns the card's name
     * @return String
     */
    public String getName() {
        return this.name;
    }

}
