package Services;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import Models.General.*;

public class Client {
    private final int PORT = 6667;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Scanner scanner;
    int playerId = -1;

    Game game;

    Client() {
        try {
            socket = new Socket("localhost", PORT);

            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.scanner = new Scanner(System.in);

            // 1. send name to server
            String name = scanner.nextLine();
            this.outputStream.writeUTF(name);
            outputStream.flush();
            outputStream.reset();
            System.out.println("[CLIENT]: sending " + name + " to the server");

            // 4. get player id and game object
            this.playerId = this.inputStream.readInt();
            System.out.println("[CLIENT]:  received player id " + this.playerId);
            // this.game = (Game) this.inputStream.readObject();
            // System.out.println("[CLIENT]: game object");

        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void playGame() {
        try {
            while (true) {
                Game game = (Game) inputStream.readObject();
                System.out.println();

                // let player choose to see their current cards
                displayChoices();
                // Scanner

            }
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.playGame();
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
