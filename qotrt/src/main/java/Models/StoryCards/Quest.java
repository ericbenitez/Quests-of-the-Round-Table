package Models.StoryCards;

import java.util.ArrayList;

import Models.AdventureCards.*;
import Models.General.Player;
import Models.General.Game;

public class Quest extends StoryCard {
    
    private String name;
    private int totalStages;
    private String foeName;
    private Player sponsor;
    private ArrayList<ArrayList<AdventureCard>> stages;
    private Game game;

    // if all the foes pass 'all' to foeName
    public Quest(Game game, String name, int stages, String foe) {
        this.game = game;
        this.name = name;
        this.totalStages = stages;
        this.foeName = foe;
        this.stages = new ArrayList<>();
    }

    public Quest(Game game, String name, int stages) {
        this.game = game;
        this.name = name;
        this.totalStages = stages;
        this.foeName = null; //when the quest doesnt have a foe
    }

    public void setSponsor(Player player) {
        this.sponsor = player;
    }

    public String getFoe(){
        return foeName;
    }
    public boolean canPlayerSponsor(Player player){
        
        return false;
    }


    @Override
    protected void draw(Player player) {
        // TODO Auto-generated method stub
        // while something ask player input to find sponsor
    }
}
