package app.Service;

import app.Models.General.Game;
import app.Models.General.Player;
import app.Objects.CardObjects;
//import Models.General.GamePlay;
import app.Models.General.GameStatus;

import java.util.UUID; //for game ID

import org.springframework.stereotype.Service;

@Service
public class GameService {
    private Game currentGame;

    public String createGame(Player player, int numOfPlayers) {
        this.currentGame = new Game();
        this.currentGame.setGameID(UUID.randomUUID().toString());
        this.currentGame.setNumOfPlayers(numOfPlayers); // first initialize the array of players
        this.currentGame.registerPlayer(player); // register the player
        this.currentGame.setGameStatus(GameStatus.NEW);
        this.currentGame.setGame(this.currentGame); // storing the game
        return this.currentGame.getGameID();
    }

    // Connect other players to the current Game
    public String joinGame(Player anotherOne, String gameID) { // passes in the current game( a global variable in js)
        // why aren't we using current game?
        if (this.currentGame.getCurrentGame() == null) {
            return null; // when game doesnt exist
        }
        if (this.currentGame.registerPlayer(anotherOne) != null) { // it means there was some room in the game to join
            if (this.currentGame.getPlayers().size() == this.currentGame.getNumOfPlayers()) { // last player to join
                this.currentGame.setGameStatus(GameStatus.IN_PROGRESS); // game start, status = in progress
                // initialize the cards here?
                CardObjects cards = new CardObjects();
                this.currentGame.setAdventureCards(cards.getAdventureCards());
                this.currentGame.setStoryCards(cards.getStoryCards());
                return this.currentGame.getGameID();
            }
            
            // added by Donna
            return this.currentGame.getGameID();
        }
        return null; // there wasnt any room for the new player to join...

    }

    public Game getCurrentGame() {
        return this.currentGame; // current game taking place
    }

    public Game gamePlay() {
        // the turn class --> in javascript file
        //Check if current game is not initialized..
        if(currentGame.getGameID()==null){
            System.out.println("Game does not exist");
            return null;
        }

        //Check if the status is finished
        if(currentGame.getGameStatus().equals(GameStatus.FINISHED)){
            //exception
            System.out.println("Game ended");
            return null;
        }
        checkWinner(currentGame);


        // end the current game...
        return currentGame;
    }

    /*
     * The goal is to become the 1st player to become a knight.
     */
    public Player checkWinner(Game currGame) {
        // return the winning player
        // Player winningPlayer;
        for (Player p : currGame.getPlayers()) {
            if (p.getNumShields() >= 7) {
                return p; // it has enough shields to be a knight
            }
        }
        return null;
    }

}