package app.Models.StoryCards;

import java.util.ArrayList;

import Models.General.*;
import app.Models.General.Card;
import app.Models.General.Player;

public abstract class StoryCard extends Card {

    protected Player drawer;

    public void setDrawer(Player player){

        this.drawer = player;

    }

    public void playEvent(ArrayList<Player> players) {
    }
}
