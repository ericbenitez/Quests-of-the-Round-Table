package app.Objects;

import java.util.ArrayList;
import java.util.Collections;
import app.Models.AdventureCards.*;
import app.Models.StoryCards.*;

public class CardObjects {
  ArrayList<AdventureCard> adventureCards = new ArrayList<AdventureCard>();
  ArrayList<StoryCard> storyCards = new ArrayList<StoryCard>();
  
  public CardObjects() {
    this.adventureCards.add(new Ally("Sir Gawain", 10, -1));
    this.adventureCards.add(new Ally("King Pellinore", 10, -1));
    this.adventureCards.add(new Ally("Sir Percival", 5, -1));
    this.adventureCards.add(new Ally("Sir Tristan", 10, -1));
    this.adventureCards.add(new Ally("King Arthur", 10, 2));
    this.adventureCards.add(new Ally("Queen Guinevere", 0, 3));
    this.adventureCards.add(new Ally("Queen Iseult", 0, 2));
    this.adventureCards.add(new Ally("Sir Lancelot", 15, -1));
    this.adventureCards.add(new Ally("Sir Galahad", 15, -1));

    for (int i = 0; i < 11; i++) {
      this.adventureCards.add(new Weapon("Horse", 10));
    }
    for (int i = 0; i < 16; i++) {
      this.adventureCards.add(new Weapon("Sword", 10));
    }
    for (int i = 0; i < 15; i++) {
      this.adventureCards.add(new Weapon("Dagger", 50));
    }
    for (int i = 0; i < 2; i++) {
      this.adventureCards.add(new Weapon("Excalibur", 30));
    }
    for (int i = 0; i < 6; i++) {
      this.adventureCards.add(new Weapon("Lance", 20));
    }
    for (int i = 0; i < 8; i++) {
      this.adventureCards.add(new Weapon("Battle-ax", 15));
    }

    //TODO: should be 2
    for (int i = 0; i < 8; i++) {
      this.adventureCards.add(new Test("Test of the Questing Beast"));
      this.adventureCards.add(new Test("Test of Temptation"));
      this.adventureCards.add(new Test("Test of Valor"));
      this.adventureCards.add(new Test("Test of Morgan Le Fey", 3));
    }
    for (int i = 0; i < 8; i++) {
      this.adventureCards.add(new Foe("Thieves", 1));
      this.adventureCards.add(new Foe("Saxon Knight", 2, 25));
      if (i < 7) {
        this.adventureCards.add(new Foe("Robber Knight", 15));
      }
      if (i < 6) {
        this.adventureCards.add(new Foe("Evil Knight", 20, 30));
      }
      if (i < 5) {
        this.adventureCards.add(new Foe("Saxons", 10, 20));
      }
      if (i < 4) {
        this.adventureCards.add(new Foe("Mordred", 30));
        this.adventureCards.add(new Foe("Boar", 5, 15));
      }
      if (i < 3) {
        this.adventureCards.add(new Foe("Black Knight", 25, 35));
      }
      if (i < 2) {
        this.adventureCards.add(new Foe("Giant", 40));
        this.adventureCards.add(new Foe("Green Knight", 25, 40));
      }
    }
    this.adventureCards.add(new Foe("Dragon", 50, 70));

    for (int i = 0; i < 8; i++) {
      this.adventureCards.add(new Amour("Amour", "", 10, 1));
    }

    this.storyCards.add(new EventCard(new KingsRecognition(), "King's Recognition",
        "The next player(s) to complete a Quest will receive 2 extra shields."));
    for (int i = 0; i < 2; i++) {
      this.storyCards.add(new EventCard(new QueensFavor(), "Queens Favour",
          "The lowest ranked player(s) immediately receives 2 Adventure Cards."));
      this.storyCards.add(new EventCard(new CourtCalledToCamelot(), "Court Called To Camelot",
          "All Allies in play must be discarded."));
    }

    this.storyCards
        .add(new EventCard(new Pox(), "Pox", "All players except the player drawing this card lose 1 shield."));

    this.storyCards.add(new EventCard(new Plague(), "Plague",
        "Drawer loses 2 shields if possible."));

    this.storyCards.add(new EventCard(new ChivalrousDeed(), "Chivalrous Deed",
        "Player(s) with both lowest rank and least amount of shields, receives 3 shields"));

    this.storyCards.add(new EventCard(new ProsperityThroughoutTheRealm(), "Prosperity Throughout the Realm",
        "All players may immediately draw 2 Adventure Cards."));
    this.storyCards.add(new EventCard(new KingsCallToArms(), "King's Call To Arms",
        "The highest ranked player(s) must place 1 weapon in the discard pile. If unable to do so, 2 Foe Cards must be discarded."));

    this.storyCards
        .add(new EventCard(new Pox(), "Pox", "All players except the player drawing this card lose 1 shield."));

    this.storyCards.add(new EventCard(new Plague(), "Plague",
        "Drawer loses 2 shields if possible."));

    this.storyCards.add(new EventCard(new ChivalrousDeed(), "Chivalrous Deed",
        "Player(s) with both lowest rank and least amount of shields, receives 3 shields"));


    // initialize quest cards
    
    this.storyCards.add(new Quest("Defend the Queen's Honor", 4, "All"));
    this.storyCards.add(new Quest("Slay the Dragon", 3, "Dragon"));
    this.storyCards.add(new Quest("Rescue the Fair Maiden", 3, "Black Knight"));
    this.storyCards.add(new Quest("Search for the Holy Grail", 5, "All"));
    this.storyCards.add(new Quest("Test of the Green Knight", 4, "Green Knight"));
    this.storyCards.add(new Quest("Journey Through the Enchanted Forest",3,"Evil Knight"));

    for (int i = 0; i < 2; ++i) {
      this.storyCards.add(new Quest("Vanquish King Arthur's Enemies", 3, "All"));
      this.storyCards.add(new Quest("Boar Hunt", 2, "Boar"));
      this.storyCards.add(new Quest("Repel the Saxon Raiders", 2, "All"));
    }

    this.storyCards.add(new Tournament("some tournament", 1));
    this.storyCards.add(new Tournament("At Camelot", 3));
    this.storyCards.add(new Tournament("At Orkney", 2));
    this.storyCards.add(new Tournament("At Tintagel", 1));
    this.storyCards.add(new Tournament("At York", 0));


    
    Collections.shuffle(this.adventureCards);
    Collections.shuffle(this.storyCards);
            // --------------------------------------------------------------------------
    // PLAYER 4
    this.adventureCards.add(new Weapon("Sword", 10));
    this.adventureCards.add(new Weapon("Dagger", 5));
    this.adventureCards.add(new Weapon("Excalibur", 30));
    this.adventureCards.add(new Ally("Sir Gawain", 10, -1));
    this.adventureCards.add(new Ally("Sir Tristan", 10, -1));
    this.adventureCards.add(new Ally("Sir Galahad", 15, -1));
    this.adventureCards.add(new Test("Test of the Questing Beast"));
    this.adventureCards.add(new Amour("Amour", "", 10, 1));
    this.adventureCards.add(new Foe("Thieves", 5));
    this.adventureCards.add(new Foe("Saxon Knight", 15, 25));
    this.adventureCards.add(new Foe("Robber Knight", 15));
    this.adventureCards.add(new Foe("Boar", 5, 15));

    
        // --------------------------------------------------------------------------
    // PLAYER 3
    this.adventureCards.add(new Weapon("Sword", 10));
    this.adventureCards.add(new Weapon("Dagger", 5));
    this.adventureCards.add(new Weapon("Excalibur", 30));
    this.adventureCards.add(new Ally("Sir Gawain", 10, -1));
    this.adventureCards.add(new Ally("Sir Tristan", 10, -1));
    this.adventureCards.add(new Ally("Sir Galahad", 15, -1));
    this.adventureCards.add(new Test("Test of the Questing Beast"));
    this.adventureCards.add(new Amour("Amour", "", 10, 1));
    this.adventureCards.add(new Foe("Thieves", 5));
    this.adventureCards.add(new Foe("Saxon Knight", 15, 25));
    this.adventureCards.add(new Foe("Robber Knight", 15));
    this.adventureCards.add(new Foe("Boar", 5, 15));
    
    // --------------------------------------------------------------------------
    // PLAYER 2
    this.adventureCards.add(new Weapon("Sword", 10));
    this.adventureCards.add(new Weapon("Dagger", 5));
    this.adventureCards.add(new Weapon("Excalibur", 30));
    this.adventureCards.add(new Ally("Sir Gawain", 10, -1));
    this.adventureCards.add(new Ally("Sir Tristan", 10, -1));
    this.adventureCards.add(new Ally("Sir Galahad", 15, -1));
    this.adventureCards.add(new Test("Test of the Questing Beast"));
    this.adventureCards.add(new Amour("Amour", "", 10, 1));
    this.adventureCards.add(new Foe("Thieves", 5));
    this.adventureCards.add(new Foe("Saxon Knight", 15, 25));
    this.adventureCards.add(new Foe("Robber Knight", 15));
    this.adventureCards.add(new Foe("Boar", 5, 15));
    
    // --------------------------------------------------------------------------
    // PLAYER 1
    this.adventureCards.add(new Weapon("Sword", 10));
    this.adventureCards.add(new Weapon("Dagger", 5));
    this.adventureCards.add(new Weapon("Excalibur", 30));
    this.adventureCards.add(new Ally("Sir Gawain", 10, -1));
    this.adventureCards.add(new Ally("Sir Tristan", 10, -1));
    this.adventureCards.add(new Ally("Sir Galahad", 15, -1));
    this.adventureCards.add(new Test("Test of Morgan Le Fey", 3));
    this.adventureCards.add(new Test("Test of the Questing Beast"));
    this.adventureCards.add(new Amour("Amour", "", 10, 1));
    this.adventureCards.add(new Foe("Thieves", 5));
    this.adventureCards.add(new Foe("Saxon Knight", 15, 25));
    this.adventureCards.add(new Foe("Robber Knight", 15));
    this.adventureCards.add(new Foe("Boar", 5, 15));
    
    // --------------------------------------------------------------------------

    this.storyCards.add(new EventCard(new ProsperityThroughoutTheRealm(), "Prosperity Throughout the Realm", "All players may immediately draw 2 Adventure Cards."));
    this.storyCards.add(new Tournament("At Orkney", 0)); // 2 shields
    this.storyCards.add(new Quest("Boar Hunt", 2, "Boar")); 
    
    
  }
  
  //getter
  public  ArrayList<AdventureCard> getAdventureCards(){
    return adventureCards;
  }
  
  public ArrayList<StoryCard> getStoryCards(){
    return storyCards;
  }
  
  public AdventureCard getCardByName(String name) {
    for (AdventureCard adventureCard : adventureCards) {
      if (adventureCard.getName().equals(name)) {
        return adventureCard;
      }
    }
    return null;
  }

  public int getBattlePtsByName(String name){
    for (AdventureCard adventureCard : adventureCards){
      if (adventureCard.getName().equals(name)){
        return adventureCard.getBattlePoints();
      }
    }
    return 0;
  }
}