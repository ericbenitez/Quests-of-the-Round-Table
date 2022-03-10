package app.Models.StoryCards;

import java.util.ArrayList;

import Models.AdventureCards.*;
import app.Models.General.Player;
import app.Models.General.Game;
import app.Models.AdventureCards.AdventureCard;

public class Quest extends StoryCard {

    protected String name;
    protected int totalStages;
    protected String foeName;
    protected Player sponsor;
    protected ArrayList<ArrayList<AdventureCard>> stages;
    protected Game game;
    // public QuestBehaviour QuestBehaviour; //??

    // if all the foes pass 'all' to foeName

    public Quest(String name, int stages, String foe, Game game) {
        this.game = game;
        this.name = name;
        this.totalStages = stages;
        this.foeName = foe;
        this.stages = new ArrayList<>();
    }

    public void setSponsor(Player player) {
        this.sponsor = player;
    }

    public String getFoe() {
        return foeName;
    }

    public boolean canPlayerSponsor(Player player) {

        return false;
    }

    @Override
    protected void draw(Player player) {

        // TODO Auto-generated method stub
        // while something ask player input to find sponsor
    }
}

class JourneyThruForest extends Quest {
    public JourneyThruForest(Game game) {
        super("Journey Through the Forest", 3, "Evil Knight", game);
    };

}

class SlayTheDragon extends Quest {
    public SlayTheDragon(String name, int stages, String foe, Game game) {
        super("Slay the Dragon", 3, "Dragon", game);
    };

}

class VanquishKingArthursEnemies extends Quest {
    public VanquishKingArthursEnemies(Game game) {
        super("Vanquish King Arthur's Enemies", 3, null, game);
    }
}

class RepelTheSaxonRaiders extends Quest {
    public RepelTheSaxonRaiders(Game game) {
        super("Repel the Saxon Raiders", 2, "All", game);
    };
}

class BoarHunt extends Quest {

    public BoarHunt(Game game) {
        super("Boar Hunt", 2, "All Sacons", game);
    }
}

class SearchForTheQuestingBeast extends Quest {

    public SearchForTheQuestingBeast(Game game) {
        super("Search for the Questing Beast", 4, null, game);
    }
}

class DefendTheQueensHonor extends Quest {

    public DefendTheQueensHonor(Game game) {
        super("Defend the Queen's Honor", 4, "All", game);
    }
}

class RescueTheFairMaiden extends Quest {

    public RescueTheFairMaiden(Game game) {
        super("Rescue the Fair Maiden", 3, "Black Knight", game);
    }
}

class SearchForTheHolyGrail extends Quest {
    public SearchForTheHolyGrail(Game game) {
        super("Search for the Holy Grail", 5, "All", game);
    }
}

class TestOfTheGreenKnight extends Quest {
    public TestOfTheGreenKnight(Game game) {
        super("Test of the Green Knight", 4, "Green Knight", game);
    }
}