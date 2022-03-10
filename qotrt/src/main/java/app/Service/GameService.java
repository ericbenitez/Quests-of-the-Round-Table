package app.Service;
import app.Models.General.Game;
import app.Models.General.Player;
//import Models.General.GamePlay;
import app.Models.General.GameStatus;

import java.util.UUID; //for game ID


public class GameService{
    public Game createGame(Player player, int numOfPlayers){
        Game game = new Game();
        game.setGameID(UUID.randomUUID().toString());
        game.setNumOfPlayers(numOfPlayers); //first initialize the array of players
        game.registerPlayer(player) ; //register the player
        game.setGameStatus(GameStatus.NEW);
        game.setGame(game); //storing the game 
        return game;
    }
    //

}