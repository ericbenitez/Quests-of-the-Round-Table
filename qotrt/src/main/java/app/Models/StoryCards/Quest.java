package app.Models.StoryCards;

import java.util.ArrayList;

import app.Models.AdventureCards.AdventureCard;
import app.Models.Enums.StoryCardType;
import app.Models.General.Player;

public class Quest extends StoryCard {
    protected String name;
    protected int totalStages;
    protected String foeName;
    protected Player sponsor;
    protected ArrayList<ArrayList<AdventureCard>> stages;

    // if all the foes pass 'all' to foeName
    public Quest(String name, int totalStages, String foe) {
        this.name = name;
        this.totalStages = totalStages;
        this.foeName = foe;
        this.stages = new ArrayList<>();
        this.storyCardType = StoryCardType.Quest;
    }

    public void setSponsor(Player player) {
        this.sponsor = player;
    }
    
    public Player getSponsor() {
        return this.sponsor;
    }
    
    
    public String getFoeName() {
        return foeName;
    }

    public ArrayList<ArrayList<AdventureCard>> getStages() { return this.stages; }

    public boolean canPlayerSponsor(Player player) {

        return false;
    }
    /**
     * Returns the card's name
     * @return String
     */
    public String getName() {
        return name;
    }
    
    public String getTotalStages() { return String.valueOf(totalStages); }

    @Override
    protected void draw(Player player) {

        // TODO Auto-generated method stub
        // while something ask player input to find sponsor
    }
}

class JourneyThruForest extends Quest {
    public JourneyThruForest() {
        super("Journey Through the Enchanted Forest", 3, "Evil Knight");
    };

}

class SlayTheDragon extends Quest {
    public SlayTheDragon(String name, int stages, String foe) {
        super("Slay the Dragon", 3, "Dragon");
    };

}

class VanquishKingArthursEnemies extends Quest {
    public VanquishKingArthursEnemies() {
        super("Vanquish King Arthur's Enemies", 3, null);
    }
}

class RepelTheSaxonRaiders extends Quest {
    public RepelTheSaxonRaiders() {
        super("Repel the Saxon Raiders", 2, "All");
    };
}

class BoarHunt extends Quest {

    public BoarHunt() {
        super("Boar Hunt", 2, "All Sacons");
    }
}

class SearchForTheQuestingBeast extends Quest {

    public SearchForTheQuestingBeast() {
        super("Search for the Questing Beast", 4, null);
    }
}

class DefendTheQueensHonor extends Quest {

    public DefendTheQueensHonor() {
        super("Defend the Queen's Honor", 4, "All");
    }
}

class RescueTheFairMaiden extends Quest {

    public RescueTheFairMaiden() {
        super("Rescue the Fair Maiden", 3, "Black Knight");
    }
}

class SearchForTheHolyGrail extends Quest {
    public SearchForTheHolyGrail() {
        super("Search for the Holy Grail", 5, "All");
    }
}

class TestOfTheGreenKnight extends Quest {
    public TestOfTheGreenKnight() {
        super("Test of the Green Knight", 4, "Green Knight");
    }
}