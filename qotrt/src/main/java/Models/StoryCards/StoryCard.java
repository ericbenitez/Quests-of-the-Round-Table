package Models.StoryCards;

import Models.General.*;

public abstract class StoryCard extends Card {

    protected Player drawer;

    public void setDrawer(Player player){

        this.drawer = player;

    }
}
