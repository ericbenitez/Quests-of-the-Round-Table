package Models.StoryCards;

import java.util.ArrayList;

import Models.General.*;

public abstract class StoryCard extends Card {

    protected Player drawer;

    public void setDrawer(Player player){

        this.drawer = player;

    }

    public void playEvent(ArrayList<Player> players) {
    }
}
