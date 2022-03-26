package app.Service;
import java.util.ArrayList;
import java.util.UUID; //for game ID
import org.springframework.stereotype.Service;
import app.Models.AdventureCards.AdventureCard;
import app.Models.StoryCards.StoryCard;
import app.Models.Enums.Rank;
import app.Models.General.Game;
import app.Models.General.Player;
import app.Models.General.ProgressStatus;
import app.Objects.CardObjects;

@Service
public class GameService {
    private Game currentGame;
    private int currentActivePlayer = 1; //id starts from 1
    private boolean questInPlay = false;
    private boolean tournamentInPlay = false;
    private boolean eventInPlay = false;
    private StoryCard currentStoryCard ;

    /***********Create Game Function **************
     * 
     * @param numOfPlayers
     * @return currentGame
     */
    public Game createGame(int numOfPlayers) {
        this.currentGame = new Game();
        this.currentGame.setGameID(UUID.randomUUID().toString());
        this.currentGame.setNumOfPlayers(numOfPlayers); // first initialize the array of players
        this.currentGame.setProgressStatus(ProgressStatus.NEW);
        // initialize cards
        CardObjects cards = new CardObjects();
        this.currentGame.setAdventureCards(cards.getAdventureCards());
        this.currentGame.setStoryCards(cards.getStoryCards());
        

        return this.currentGame;
    }

    // Connect other players to the current Game
    public Integer joinGame(String playerName) { // passes in the current game( a global variable in js)
        System.out.println("player joining");
        Player player = new Player(playerName); //when player is declared, it gets an id
        this.currentGame.registerPlayer(player); // adds the player to the array
        return player.getId();
    }

    public Game getCurrentGame() {
        return this.currentGame; // current game taking place
    }



    // gets the player's cards given the player num (unique id)
    public ArrayList<String> getPlayerCards(String playerNum) {
        if (this.currentGame == null) {
            return null;
        }

        int playerN = Integer.parseInt(playerNum);

        if (this.currentGame.getPlayers().size() <= 0) {
            System.out.println("no players in this game so cant get the cards");
            return null;
        }
        if (playerN > this.currentGame.getPlayers().size()) {
            System.out.println("invalid player num so cant get cards");
            return null;
        }
        System.out.println(this.currentGame.getPlayerById(playerN).getCards());
        ArrayList<AdventureCard> currCards = this.currentGame.getPlayerById(playerN).getCards();

        // trying to send only the name of the cards since I keep getting randomly
        // occurring errors
        ArrayList<String> cardNames = new ArrayList<String>();
        for (AdventureCard c : currCards) {
            cardNames.add(c.getName());
        }
        return cardNames;
    }

    public AdventureCard getAdventureCard(String id) {
        int playerId = Integer.parseInt(id);

        if (playerId <= 0 || playerId > this.currentGame.getPlayers().size()) {
            return null;
        }
        AdventureCard card = currentGame.getLastCard();
        this.currentGame.getPlayerById(playerId).addCard(card);
        return card;
    }

    public int getPlayerRankBattlePts(String id) {
        int playerId = Integer.parseInt(id);
        Player player = this.currentGame.getPlayerById(playerId);
        if (player == null) {
            return 0;
        }
        return player.getRankPts();
    }

    // update the players shields
    public boolean updateShields(int playerId, int shields) {
        // int playerId = Integer.parseInt(id);
        // int shields = Integer.parseInt(numOfShields);

        ArrayList<Player> players = this.currentGame.getPlayers();
        if (players.size() < playerId) {
            return false;
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUniqueId() == playerId) {
                players.get(i).updateShields(shields);
                return true;
            }
        }
        return false;

    }

    public void discardCards(String playerId, ArrayList<String> cards) {
        // loop through each card and remove it from the player's card
        int id = Integer.parseInt(playerId);
        Player player = this.currentGame.getPlayerById(id);
        for (int i = 0; i < cards.size(); i++) {
            AdventureCard removedCard = player.discardCard(cards.get(i));
            // add it to the discarded cards pile
            if (removedCard != null) {
                this.currentGame.addDiscardedCards(removedCard); 
            }
        }

    }

    public boolean joinQuest(String id) {
        int playerId = Integer.parseInt(id);
        //if (playerId > this.currentGame.getPlayers().size() || playerId - 1 < 0) {
         //   return false;
        //}
        // get player so we can use the addPlayer function in turns
        // Player player = this.currentGame.getPlayerById(playerId);
        currentGame.getCurrentQuest().addParticipant(Integer.parseInt(id));
        return true;
    }

    public void withdrawQuest(String id) {
        int playerId = Integer.parseInt(id);
        if (playerId > this.currentGame.getPlayers().size() || playerId - 1 < 0) {
            return;
        }
        this.currentGame.getCurrentQuest().withdrawParticipant(playerId);

    }


    public int getCurrentActivePlayer() {
        return this.currentActivePlayer;
    }

    public void setCurrentActivePlayer(int currentActivePlayer) {
        this.currentActivePlayer = currentActivePlayer;
    }

    //currentActivePlayer is the id of the players
    public int startNextPlayer() {
        currentActivePlayer+=1;
        if(currentActivePlayer <= currentGame.getPlayers().size()){
            return currentActivePlayer;
        }if(currentActivePlayer > currentGame.getPlayers().size()){
            currentActivePlayer = 1; //it's never 0!
            return currentActivePlayer;
        }
        return this.currentActivePlayer;
    }


    //~~~~~~~`turn~~~~~~~~~`
    public void setCurrentStoryCard(StoryCard card){
        this.currentStoryCard=card;
    }
    public StoryCard getCurrentStoryCard(){
        return this.currentStoryCard;
    }
    public void setQuestInPlay(boolean x){
        this.questInPlay=x;
    }
    public boolean getQuestInPlay(){
        return this.questInPlay;
    }

}