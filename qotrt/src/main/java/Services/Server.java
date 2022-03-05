package Services;

import java.io.DataInput;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import Models.AdventureCards.*;
import Models.General.*;
import Models.StoryCards.*;

@SpringBootApplication
public class Server {

  @Autowired
  private Game game;

  private final int SERVER_PORT = 6667;
  private ServerSocket serverSocket;
  private final HashMap<Integer, Socket> sockets = new HashMap<Integer, Socket>();
  private final HashMap<Integer, ObjectInputStream> inputStreams = new HashMap<Integer, ObjectInputStream>();
  private final HashMap<Integer, ObjectOutputStream> outputStreams = new HashMap<Integer, ObjectOutputStream>();

  public void initialize() { // similar to initialize in black jack
    try {
      serverSocket = new ServerSocket(SERVER_PORT);
      this.game = new Game();

      System.out.println("Server intialized. Waiting for players");

      int maxAmountPlayers = 2;
      while (game.getPlayers().size() < maxAmountPlayers) {
        Socket socket = this.serverSocket.accept();

        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

        // 2. wait for player name input
        String name = inputStream.readUTF();
        Player player = new Player(name); // goes inside game object
        this.game.registerPlayer(player);
        System.out.println("[SERVER]: Player " + name + " has joined!");

        player.drawCards(3);

        // 3. after creating player object, send to client the player id + game object
        this.sockets.put(player.getId(), socket);
        this.inputStreams.put(player.getId(), inputStream);
        this.outputStreams.put(player.getId(), outputStream);

        outputStream.writeInt(player.getId());
        outputStream.flush();
        outputStream.reset();

        if (game.getPlayers().size() >= 2 && game.getPlayers().size() == maxAmountPlayers - 1) {
          outputStream.writeUTF("game-started");
          outputStream.flush();
          outputStream.reset();
        }

        if (game.getPlayers().size() == 1) {
          outputStream.writeUTF("determine-amount-players");
          outputStream.flush();
          outputStream.reset();
          maxAmountPlayers = inputStream.readInt();
        }
      }

    } catch (IOException ioException) {
      System.out.println(ioException.getMessage());
    }
  }

  public String connectToGame(Player player) {
    if (this.game.getPlayers().size() == 4) {
      return "the game already has max players";
    }

    return null;
  }

  private void closeConnection() {
    try {
      serverSocket.close();
      for (int i = 0; i < sockets.size(); i++) {
        sockets.get(i).getInputStream().close();
        sockets.get(i).getOutputStream().close();
        sockets.get(i).close();
      }
      System.out.println("Server connection closed.");
    } catch (IOException ioException) {
      System.out.println(ioException.getMessage());
    }
  }

  private void playGame() {
    System.out.println("[SERVER]: Game started");

    try {
      int currentPlayer = 0;

      while (true) {
        Player player = this.game.getPlayers().get(currentPlayer);
        // Socket socket = this.sockets.get(player.getId());

        ObjectInputStream inputStream = this.inputStreams.get(player.getId());
        ObjectOutputStream outputStream = this.outputStreams.get(player.getId());

        outputStream.writeUTF("start-turn");
        outputStream.flush();
        outputStream.reset();

        // sending the card to the client every turn
        ArrayList<AdventureCard> cards = player.getCards();
        String cardString = "";
        for (int i = 0; i < cards.size(); i++) {
          cardString += cards.get(i).toString();
        }
        outputStream.writeUTF(cardString);
        outputStream.flush();
        outputStream.reset();

        // Object object = inputStream.readObject();
        String[] clientMsg = (String[]) inputStream.readObject(); // has the choice

        String choice = clientMsg[0];
        String value = clientMsg[1]; // will be parsed in the actionForChoice()

        System.out.println("[SERVER]: Player's choice is " + choice + "!");
        // calling a function that takes in the choice
        actionForChoice(choice, player, value, inputStream, outputStream);

        currentPlayer++;
        if (currentPlayer == game.getPlayers().size()) {
          currentPlayer = 0;
        }

      }
    } catch (Exception exception) {
      System.out.println("An exception");
      System.out.println(exception.getMessage());
    }
  }

  public void actionForChoice(String choice, Player player, String input, ObjectInputStream inputStream,
      ObjectOutputStream outputStream) {
    try {
      if (choice.equals("1")) {
        // select to discard

        AdventureCard card = player.discardCard(input);

        System.out.println("Helloo");
        try {
          outputStream.writeUTF("Card was deleted " + card.getName());
          outputStream.flush();
          outputStream.reset();


        } catch (IOException ioException) {
          System.out.println("Choice 1 execption:" + ioException.getMessage());
        }
      }
      if (choice.equals("2")) {
        // Draw new cards
        Boolean amount = player.drawCards(Integer.parseInt(input));

        // try {

        outputStream.writeBoolean(amount);
        outputStream.flush();
        outputStream.reset();
        System.out.println("[SERVER: Player " + player.getId() + " has successfully drawn " + input + " cards.");
        // } catch (IOException ioException) {
        // System.out.println(ioException.getMessage());
        // }

      }
      if (choice.equals("3")) {
        // See discarded cards
        ArrayList<String> discardedCards = game.getDiscardedCards();
        try {

          outputStream.writeUTF(discardedCards.toString());
          outputStream.flush();
          outputStream.reset();
          System.out.println("[SERVER: Sending the discarded cards.");
        } catch (IOException ioException) {
          System.out.println("Choice 3 execption:" + ioException.getMessage());
        }

      }
    } catch (IOException ioException) {
      System.out.println(ioException.getMessage());
    }
  }

  public static void main(String[] args) {
    // ConfigurableApplicationContext context = SpringApplication.run(Server.class,
    // args);
    // Game game = context.getBean(Game.class);

    Server server = new Server();
    server.initialize();
    server.playGame();
    server.closeConnection();

    // Game game = new Game();

    // Player player1 = new Player("player1");
    // Player player2 = new Player("player2");
    // Player player3 = new Player("player3");
    // Player player4 = new Player("player4");

    // game.registerPlayer(player1);
    // game.registerPlayer(player2);
    // game.registerPlayer(player3);
    // game.registerPlayer(player4);

    // StoryCard storyCard = game.getLastStoryCard();
    // storyCard.setDrawer(player1);
    // player1.pickedCard = storyCard;
    // player1.printPickedCard();

    // player1.updateShields(5);
    // player2.updateShields(4);
    // player3.updateShields(1);

    // if (player1.pickedCard instanceof EventCard) {
    // EventCard eventCard = (EventCard) player1.pickedCard;

    // System.out.println("player1 before");
    // player1.printCards();

    // eventCard.eventBehaviour.playEvent(game.getPlayers(), player1);

    // // call some function to start the event.. and allow other players to join
    // the
    // // event

    // }
  }
}
