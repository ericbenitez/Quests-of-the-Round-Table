package Models.StoryCards;

public class Quest extends StoryCard{

    private int totalStages;
    private Foe foeCard;
    private Player sponsor;


    public Quest(String name, String description, int stages, Foe foe = null, Player sponsor = null ){
        this.name = name;
        this.description = description;
        this.stages = stages;
        this.foeCard = foe;
        this.sponsor = sponsor;
    }

    public Quest(String name, String description, int stages ){
        this.name = name;
        this.description = description;
        this.stages = stages;
        this.foeCard = null;
        this.sponsor = null;
    }


    public setSponsor(Player player){
        this.sponsor = player;

    }
  
}
