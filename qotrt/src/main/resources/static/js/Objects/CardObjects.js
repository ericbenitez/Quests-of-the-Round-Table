class AdventureCard {
  constructor(name) {
    this.name = name;
  }
}

class Ally extends AdventureCard {
  constructor(name, battlePoints, bids) {
    super(name)
    this.battlePoints = battlePoints;
    this.bids = bids;
    
    this.cardType = "Ally";
  }
}

class Weapon extends AdventureCard {
  constructor(name, battlePoints) {
    super(name)
    this.battlePoints = battlePoints;
    
    this.cardType = "Weapon";
  }
}

class Foe extends AdventureCard {
  constructor(name, minBattlePoints, maxBattlePoints) {
    super(name)
    this.minBattlePoints = minBattlePoints;
    this.maxBattlePoints = maxBattlePoints;
    
    this.cardType = "Foe";
  }
}

class Amour extends AdventureCard {
  constructor() {
    super("Amour")
    this.battlePoints = 10;
    this.bids = 1;
    
    this.cardType = "Amour"
  }
}

const CardObjects = {
  "Sir Gawain": new Ally("Sir Gawain", 10, -1),
  "King Pellinore": new Ally("King Pellinore", 10, -1),
  "Sir Percival": new Ally("Sir Percival", 5, -1),
  "Sir Tristan": new Ally("Sir Tristan", 10, -1),
  "King Arthur": new Ally("King Arthur", 10, 2),
  "Queen Guinevere": new Ally("Queen Guinevere", -1, 3),
  "Queen Iseult": new Ally("Queen Iseult", -1, 2),
  "Sir Lancelot": new Ally("Sir Lancelot", 15, -1),
  "Sir Galahad": new Ally("Sir Galahad", 15, -1),
  
  "Horse": new Weapon("Horse", 10),
  "Sword": new Weapon("Sword", 10),
  "Dagger": new Weapon("Dagger", 5),
  "Excalibur": new Weapon("Excalibur", 30),
  "Lance": new Weapon("Lance", 20),
  "Battle-ax": new Weapon("Battle-ax", 15),
  
  "Thieves": new Foe("Thieves", 5),
  "Saxon Knight": new Foe("Saxon Knight", 15, 25),
  "Robber Knight": new Foe("Robber Knight", 15),
  "Evil Knight": new Foe("Evil Knight", 20, 30),
  "Saxons": new Foe("Saxons", 10, 20),
  "Mordred": new Foe("Mordred", 30),
  "Boar": new Foe("Boar", 5, 15),
  "Black Knight": new Foe("Black Knight", 25, 35),
  "Giant": new Foe("Giant", 40),
  "Green Knight": new Foe("Green Knight", 25, 40),
  "Dragon": new Weapon("Dragon", 50, 70),
  
  "Amour": new Amour(),
}

