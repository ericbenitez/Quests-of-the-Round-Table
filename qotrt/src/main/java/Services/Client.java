package Services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

import Models.AdventureCards.AdventureCard;
import Models.General.*;

public class Client {
    private final int PORT = 6667;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Scanner scanner;
    int playerId = -1;
    

    private static boolean gameStarted = false;
    // Game game;

    Client() {
        try {
            socket = new Socket("localhost", PORT);

            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.scanner = new Scanner(System.in);

            // 1. send name to server
            System.out.println("[Game]:Enter your name ");
            String name = scanner.nextLine();
            this.outputStream.writeUTF(name);
            outputStream.flush();
            outputStream.reset();

            // 4. get player id and game object
            this.playerId = this.inputStream.readInt();
        } catch (IOException ioexception) {
            System.out.println(ioexception.getMessage());
        }

    }

    public void playGame() {
        try {
            while (true) {
                String serverMessage = this.inputStream.readUTF();

                if(serverMessage.equals("game-started")) {
                    System.out.println("[GAME]: Sorry the game has started, try again later...");
                    gameStarted = true;
                    break;
                }
                

                if (serverMessage.equals("determine-amount-players")) {
                    System.out.println("[GAME]: How many players do you want in this game? (2-4)");
                    while (true) {
                        String input = scanner.nextLine();
                        if (Integer.parseInt(input) <= 4 && Integer.parseInt(input) >= 2) {
                            int amount = Integer.parseInt(input);
                            this.outputStream.writeInt(amount);
                            this.outputStream.flush();
                            this.outputStream.reset();
                            break;
                        } else {
                            System.out.println("[GAME]: Invalid amount");
                        }
                    }
                }

                if (serverMessage.equals("start-turn")) {
                    System.out.println("Your turn has started");

                    // let player choose to see their current cards
                    displayChoices(); // the prompts
                    // Scanner
                    String choice = scanner.nextLine();
                    String cardNameToDiscard;

                    if (Integer.parseInt(choice) == 1) {
                        this.outputStream.writeUTF(choice);// passing in the player ID so we know
                                                                                 // which
                        outputStream.flush();
                        outputStream.reset();
                        // Sending to the server
                        System.out.println("[CLIENT]: sending choice " + choice + " to the server");
                        
                        String cards = this.inputStream.readUTF();
                        System.out.println("Your cards: \n" + cards);
                    }

                    if (Integer.parseInt(choice) == 2){
                        System.out.println("Enter the Name of Card to Discard");
                        cardNameToDiscard = scanner.nextLine();
                        this.outputStream.writeUTF(choice +  " " + cardNameToDiscard);// passing in
                                                                                                           // the
                        // player ID so we
                        // know which
                        outputStream.flush();
                        outputStream.reset();
                        // Sending to the server
                        System.out.println("[CLIENT]: sending choice " + choice + " to the server");

                    }

                    if (Integer.parseInt(choice) == 3) {
                        System.out.println("Enter the number of cards you wish to draw: ");
                        String amountCards = scanner.nextLine();

                        this.outputStream.writeUTF(choice + " " + amountCards);// passing in the
                                                                                                     // player
                        // ID so we know which
                        outputStream.flush();
                        outputStream.reset();
                        // Sending to the server
                        System.out.println("[CLIENT]: sending choice " + choice + " to the server");

                        // server response
                        Boolean success = inputStream.readBoolean();
                        if (success) {
                            System.out.println("[CLIENT]: Cards are successfully drawn.");
                        } else {
                            System.out.println("[CLIENT]: Cards could not be drawn.");
                        }

                    }

                    if (Integer.parseInt(choice) == 4) {
                        this.outputStream.writeUTF(choice);// passing in the player ID so we know
                                                                                 // which
                        outputStream.flush();
                        outputStream.reset();
                        // Sending to the server
                        System.out.println("[CLIENT]: sending choice " + choice + " to the server");

                    }
                    
                    if (Integer.parseInt(choice) == 5) {
                        this.outputStream.writeUTF("the choice is 5");
                        this.outputStream.flush();
                        this.outputStream.reset();
                    }

                }

            }
        } catch (IOException ioexception) {
            System.out.println(ioexception.getMessage());
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        if (!gameStarted) {
            client.playGame();
        } else {
            System.out.println("Game already started");
        }
        client.closeConnection();
    }

    private void closeConnection() {

        try {
            scanner.close(); //
            outputStream.close();
            inputStream.close();
            socket.close();
            System.out.println("Client connection closed.");
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }

    }

    public void displayChoices() {
        System.out.println("Choose which you would like to do: ");
        System.out.println("1. See your current cards");
        System.out.println("2. Select to discard");
        System.out.println("3. Draw new cards");
        System.out.println("4. See discarded cards");

    }

}
