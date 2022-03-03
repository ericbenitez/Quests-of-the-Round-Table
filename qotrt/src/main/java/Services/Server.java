package Services;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Models.General.*;
import Models.StoryCards.*;

public class Server {
  private Game game;
  
  private final int SERVER_PORT = 6667;
  private ServerSocket serverSocket;
  private final ArrayList<Socket> sockets = new ArrayList<>();
  private final ArrayList<ObjectInputStream> inputStreams = new ArrayList<>();
  private final ArrayList<ObjectOutputStream> outputStreams = new ArrayList<>();

  public void initialize() { //similar to initialize in black jack
    try {
      serverSocket = new ServerSocket(SERVER_PORT);
      this.game = new Game();
      
      System.out.println("Server intialized. Waiting for players");
      while (game.getPlayers().size() < 2) {
        Socket socket = this.serverSocket.accept();
        this.sockets.add(socket);
    
        
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        this.inputStreams.add(inputStream);
        
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.outputStreams.add(outputStream);
        
        // 2. wait for player name input
        String name = inputStream.readUTF();
        Player player = new Player(name);
        this.game.registerPlayer(player);
        System.out.println("[SERVER]: Player " +name + " has joined!");
        
        // 3. after creating player object, send to client the player id + game object
        outputStream.writeInt(player.getId());
        outputStream.flush();
        outputStream.reset();
        System.out.println("[SERVER]: sending player id " + player.getId());
        
        // outputStream.writeObject(this.game);
        // outputStream.write
        // outputStream.flush();
        // outputStream.reset();
        // System.out.println("[SERVER]: sending game object");
        
        
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
        for(int i = 0; i < sockets.size(); i++) {
            sockets.get(i).close();
            inputStreams.get(i).close();
            outputStreams.get(i).close();
        }
        System.out.println("Server connection closed.");
    } catch (IOException ioException) {
        System.out.println(ioException.getMessage());
    }
}


  public static void main(String[] args) {
    Server server = new Server();
    server.initialize();
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
    //   EventCard eventCard = (EventCard) player1.pickedCard;


    //   System.out.println("player1 before");
    //   player1.printCards();
      
      
    //   eventCard.eventBehaviour.playEvent(game.getPlayers(), player1);
      
    //   // call some function to start the event.. and allow other players to join the
    //   // event

    // }
  }
}
