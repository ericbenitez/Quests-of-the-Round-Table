public abstract class AdventureCard extends Card {
  Player owner;
  
  @Override
  void draw(Player player) {
    this.owner = player;
  	owner.cards.add(this);
  }
}
