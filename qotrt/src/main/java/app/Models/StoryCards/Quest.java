package app.Models.StoryCards;

import java.util.ArrayList;

import app.Models.Enums.StoryCardType;
import app.Models.General.Player;
import java.util.HashMap;
import app.Models.AdventureCards.*;
import app.Objects.CardObjects;

public class Quest extends StoryCard {
    protected String name;
    protected int totalStages;
    protected String foeName;
    protected int sponsor; // [[[player 1], [player2]],[]]
    protected ArrayList<ArrayList<String>> stages; // sponsor stages
    // client stages is overwritten for a new stage
    protected HashMap<Integer, ArrayList<String>> clientStage;
    // protected ArrayList<ArrayList<String>> clientStages; //client = player
    // {key:value,key:value} playerId : stage cards
    protected ArrayList<Integer> participants; // for quests
    private int currentStage = 1;
    protected int sponsorAttempts = 0; // the amount of times a player was asked to sponsor
    protected boolean questIncludesTest;  //we figure this out from the stages the sponsor sets
    protected int testInStage;
    protected Test testCard;
    //CardObjects cardObjects;
    ArrayList<String> testNames;
    protected boolean isActive = true;
    protected Integer currentActivePlayer;


    // if all the foes pass 'all' to foeName
    public Quest(String name, int totalStages, String foe) {

        this.name = name;
        this.totalStages = totalStages;
        this.foeName = foe;
        this.stages = new ArrayList<>();
        this.storyCardType = StoryCardType.Quest;
        this.clientStage = new HashMap<>();
        this.participants = new ArrayList<>(3);
        //this.cardObjects = new CardObjects();
        testNames = new ArrayList<>();
        testNames.add("Test of the Questing Beast");
        testNames.add("Test of Temptation");
        testNames.add("Test of Valor");
        testNames.add("Test of Morgan Le Fey");

    }

    public int getCurrentStageNumber() {
        return currentStage;
    }

    public void incrementCurrentStage() {
        if(currentStage < totalStages ){
            currentStage+=1;
        }
        else if(currentStage==totalStages){
            currentStage+=1;
        }
        
    }
    
    /**
     * Returns the amount of sponsor attempts
     * @return int
     */
    public int getSponsorAttempts() {
        return this.sponsorAttempts;
    }
    
    /**
     * Increments the sponsor attempts by 1 and returns it
     * @return int
     */
    public int incrementSponsorAttempts() {
        return ++this.sponsorAttempts;
    }
    
    public boolean isActive() {
        return this.isActive;
    }
    
    public boolean setActive(Boolean active) {
        this.isActive = active;
        return active;
    }

    public void setSponsorStages(ArrayList<ArrayList<String>> stages) {
        //here it should go through the cards and set the tests
        this.stages = stages;

        for (int i = 0; i < stages.size(); i++)
        {
            for (int j = 0; j < stages.get(i).size(); j++)
            {
                String nameOfCard = stages.get(i).get(j);
                for(String x:testNames ){
                    if(x.equals(nameOfCard)){
                        questIncludesTest = true;  //we figure this out from the stages the sponsor sets
                        testInStage = i+1;
                    }
                }
               
            } 
        }
        
     
    }
    
    public void setCurrentActivePlayer(Integer currentActivePlayer) {
        this.currentActivePlayer = currentActivePlayer;
    }
    
    public Integer getCurrentActivePlayer() {
        return this.currentActivePlayer;
    }
    
    //from the game service...
    public void setTestCard(Test tc){
        this.testCard = tc;
    }
  
    public void setClientStage(int playerId, ArrayList<String> cards) {
        clientStage.put(playerId, cards);
    }


    public void setSponsor(int sponsorId) {
        this.sponsor = sponsorId;
    }

    public int getSponsor() {
        return this.sponsor;
    }

    public void addParticipant(int playerId) {
        participants.add(playerId);
    }

    // get participants id
    public ArrayList<Integer> getParticipantsId() {
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

    public ArrayList<ArrayList<String>> getStages() {
        return this.stages;
    }

    public HashMap<Integer, ArrayList<String>> getClientStage() {
        return this.clientStage;
    }

    public boolean canPlayerSponsor(Player player) {

        return false;
    }

    /**
     * Returns the card's name
     * 
     * @return String
     */
    public String getName() {
        return name;
    }

    public String getTotalStages() {
        return String.valueOf(totalStages);
    }

    public boolean getQuestIncludesTest(){
        return questIncludesTest;
    }  //we figure this out from the stages the sponsor sets
   
    public int getTestInStage(){
        return testInStage;
    }


    public Test getTestCard(){
        return this.testCard;
    }
}

// class JourneyThruForest extends Quest {
//     public JourneyThruForest() {
//         super("Journey Through the Enchanted Forest", 3, "Evil Knight");
//     };

// }

// class SlayTheDragon extends Quest {
//     public SlayTheDragon(String name, int stages, String foe) {
//         super("Slay the Dragon", 3, "Dragon");
//     };

// }

// class VanquishKingArthursEnemies extends Quest {
//     public VanquishKingArthursEnemies() {
//         super("Vanquish King Arthur's Enemies", 3, null);
//     }
// }

// class RepelTheSaxonRaiders extends Quest {
//     public RepelTheSaxonRaiders() {
//         super("Repel the Saxon Raiders", 2, "All");
//     };
// }

// class BoarHunt extends Quest {

//     public BoarHunt() {
//         super("Boar Hunt", 2, "All Sacons");
//     }
// }

// class SearchForTheQuestingBeast extends Quest {

//     public SearchForTheQuestingBeast() {
//         super("Search for the Questing Beast", 4, null);
//     }
// }

// class DefendTheQueensHonor extends Quest {

//     public DefendTheQueensHonor() {
//         super("Defend the Queen's Honor", 4, "All");
//     }
// }

// class RescueTheFairMaiden extends Quest {

//     public RescueTheFairMaiden() {
//         super("Rescue the Fair Maiden", 3, "Black Knight");
//     }
// }

// class SearchForTheHolyGrail extends Quest {
//     public SearchForTheHolyGrail() {
//         super("Search for the Holy Grail", 5, "All");
//     }
// }

// class TestOfTheGreenKnight extends Quest {
//     public TestOfTheGreenKnight() {
//         super("Test of the Green Knight", 4, "Green Knight");
//     }
// }