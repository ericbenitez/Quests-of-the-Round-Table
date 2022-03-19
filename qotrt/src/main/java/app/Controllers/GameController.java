package app.Controllers;

import app.Models.AdventureCards.AdventureCard;
import app.Models.General.ProgressStatus;
import app.Models.StoryCards.Quest;
import app.Models.StoryCards.StoryCard;
import app.Service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
@Controller

public class GameController {
  private GameService gameService;

  @Autowired
  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  // we need an initializing player and the number of player to call createGame()
  // From TTT
  @PostMapping("/start")
  @MessageMapping("/game/start") // server
  @SendTo("/topic/game/started") // client
  public ResponseEntity<String> start(int numPlayers) throws Exception {
    gameService.createGame(numPlayers);

    // wait for players
    while (this.gameService.getCurrentGame().getPlayers().size() < numPlayers) {
      Thread.sleep(1000);
    }

    this.gameService.nextStep();

    return ResponseEntity.ok(gameService.createGame(numPlayers).getGameID());
  }

  // After starting, allow other players to connect
  @MessageMapping("/playerJoining")
  @SendToUser("/topic/joinGame")
  public ResponseEntity<Integer> joinGame(String playerName) throws Exception {
    return ResponseEntity.ok(this.gameService.joinGame(playerName));
  }

  @MessageMapping("/getAdvCard")
  @SendToUser("/topic/getAdvCard")
  public AdventureCard getAdvCard() {
    if (this.gameService.getCurrentGame().getProgressStatus() != ProgressStatus.IN_PROGRESS)
      return null;
    return gameService.getAdventureCard();
  }

  @MessageMapping("/pickCard")
  @SendToUser("/topic/pickCard")
  public ResponseEntity<StoryCard> pickCard() throws Exception {
    if (this.gameService.getCurrentGame().getProgressStatus() != ProgressStatus.IN_PROGRESS)
      return null;

    StoryCard storyCard = this.gameService.getCurrentGame().pickCard();
    return ResponseEntity.ok(storyCard);
  }

  @SendTo("/topic/doYouWantToSponsor")
  public ResponseEntity<String> askForSponsor() throws Exception {
    // StoryCard storyCard = this.gameService.getCurrentGame().pickCard();
    // sponsor id
    return ResponseEntity.ok("sponsor id");
  }
}