package app.Models.StoryCards;

import java.util.ArrayList;

import app.Models.Enums.StoryCardType;
import app.Models.General.Player;

public class Quest extends StoryCard {
    protected String name;
    protected int totalStages;
    protected String foeName;
    protected int sponsor;
    protected ArrayList<ArrayList<String>> stages; //sponsor stages
    protected ArrayList<ArrayList<String>> clientStages;
    protected ArrayList<Integer> participants; // for quests
    private int currentStage = 1;
    // if all the foes pass 'all' to foeName
    public Quest(String name, int totalStages, String foe) {
        this.name = name;
        this.totalStages = totalStages;
        this.foeName = foe;
        this.stages = new ArrayList<>();
        this.storyCardType = StoryCardType.Quest;
        this.clientStages = new ArrayList<>();
        this.participants = new ArrayList<>(3);
    }

    public int getCurrentStageNumber(){
        return currentStage;
    }
    public void incrementCurrentStage (){
        this.currentStage+=1;
    }
    public void setSponsorStages(ArrayList<ArrayList<String>> stages){
        this.stages = stages;
    }
    public void setStages(ArrayList<ArrayList<String>> clientStages) {
        this.clientStages = clientStages;
      }

    public void setStage(ArrayList<String> clientStage) {
    if (this.clientStages.size() >= totalStages) {
        return;
    }
    clientStages.add(clientStage);
    }

    public void setSponsor(int sponsorId) {
        this.sponsor = sponsorId;
    }
    public int getSponsor(){
        return this.sponsor;
    }
    public void addParticipant(int playerId) {
        participants.add(playerId);
      }

    //get participants id 
    public ArrayList<Integer> getParticipantsId(){
        return participants;
    }
      
    public void withdrawParticipant(int playerId) {
        for (int i = 0; i < participants.size(); i++) {
        if (participants.get(i) == playerId) {
            participants.remove(i);
            return;
        }
        }
    }
    
    
    public String getFoeName() {
        return foeName;
    }

    public ArrayList<ArrayList<String>> getStages() { return this.stages; }

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