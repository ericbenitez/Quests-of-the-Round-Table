package Models.General;

public class Main {
  public static void main(String[] args) {
    Game game = new Game();
    game.start();
    //Select cards for discarding
    game.players.get(0).discardCard("Amour");
    game.players.get(1).discardCard("Thieves");


    System.out.println("Printing Discarded Cards...");

    game.displayDiscardedCards();
  }
}