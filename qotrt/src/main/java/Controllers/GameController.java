package Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import Models.General.*;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import java.util.ArrayList;

@Controller
@RequestMapping("/start")
public class GameController {
  ArrayList<Player> players = new ArrayList<>();

  // we need an initializing player and the number of player to call createGame()
  // From TTT
  @MessageMapping("/game/start")
  @SendTo("/topic/game/started")
  public ResponseEntity<Game> start(@RequestBody Player player, int numOfPlayers) {
    return ResponseEntity.ok()
  }

  // trying
  @MessageMapping("/playerJoining")
  @SendTo("/topic/joinGame")
  public String joinGame(String name) throws Exception {
    System.out.println("here here here tryna joing: " + name);

    if (players.size() >= 4) {
      return "Sorry " + name + " the game is full.";
    } else {

      players.add(new Player(name));
      return "Welcome to the game " + name + "! You are player number " + players.size();

    }
  }

}