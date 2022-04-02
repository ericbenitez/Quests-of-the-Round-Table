package app.Models.StoryCards;

import java.util.ArrayList;

import app.Models.Enums.StoryCardType;
import app.Models.General.*;

public abstract class StoryCard extends Card {

    protected Player drawer;
    protected StoryCardType storyCardType;

    public void setDrawer(Player player){
        this.drawer = player;
    }

    public Player getDrawer() { return this.drawer; }

    // public void playEvent(ArrayList<Player> players) {
        
    // }
    
    /**
     * Returns the card's name
     * @return String
     */
    public String getName() {
        return this.name;
    }

    public StoryCardType getStoryCardType() {
        return this.storyCardType;
    }
}
