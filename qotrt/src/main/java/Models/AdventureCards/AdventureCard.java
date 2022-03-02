package Models.AdventureCards;

public abstract class AdventureCard extends Card {
  Player owner;
  
  @Override
  void draw(Player player) {
    this.owner = player;
  	owner.cards.add(this);
  }
  
  public abstract int getBattlePoints(); //not all classes have it but need it for getPersonalBattleScore function in player.
}
