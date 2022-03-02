package Models.StoryCards;
import Models.AdventureCards.*;
import Models.General.Player;

public class Quest extends StoryCard {

    private int totalStages;
    private Foe foeCard;
    private Player sponsor;


    public Quest(String name, String description, int stages, Foe foe, Player sponsor){
        this.name = name;
        this.description = description;
        this.totalStages = stages;
        this.foeCard = foe;
        this.sponsor = sponsor;
   
    }

    public Quest(String name, String description, int stages ){
        this.name = name;
        this.description = description;
        this.totalStages = stages;
        this.foeCard = null;
        this.sponsor = null;
    }


    public void setSponsor(Player player){
        this.sponsor = player;
    }

    @Override
    protected void draw(Player player) {
        // TODO Auto-generated method stub
        
    }
  
}
