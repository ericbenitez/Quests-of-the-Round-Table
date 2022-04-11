package app.Service;
import java.util.ArrayList;
import java.util.UUID; //for game ID

import org.springframework.stereotype.Service;

import app.Models.AdventureCards.AdventureCard;
import app.Models.AdventureCards.Ally;
import app.Models.AdventureCards.Test;
import app.Models.Enums.AdventureCardType;
import app.Models.AdventureCards.Amour;
import app.Models.General.Card;
import app.Models.General.Game;
import app.Models.General.Player;
import app.Models.General.ProgressStatus;
import app.Models.RankCards.Rank;
import app.Models.StoryCards.EventCard;
import app.Models.StoryCards.StoryCard;
import app.Models.StoryCards.Tournament;
import app.Objects.CardObjects;

@Service
public class GameService {
    private Game currentGame;
    private int currentActivePlayer = 1; //id starts from 1
    private boolean questInPlay = false;
    private boolean tournamentInPlay = false;
    private boolean eventInPlay = false;
    private StoryCard currentStoryCard ;
    private CardObjects cards; 

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
        cards = new CardObjects();
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
        /*for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUniqueId() == playerId) {
                players.get(i).updateShields(shields);
                Player haha = players.get(i);
                System.out.println(haha);
                return true;
            }
        }*/
        this.currentGame.getPlayerById(playerId).updateShields(shields);
        Player p = this.currentGame.getPlayerById(playerId);
        return true;

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
       
        // get player so we can use the addPlayer function in turns
        // Player player = this.currentGame.getPlayerById(playerId);
        currentGame.getCurrentQuest().addParticipant(playerId);
        return true;
    }

    public void withdrawQuest(String id) {
        int playerId = Integer.parseInt(id);
      
        this.currentGame.getCurrentQuest().withdrawParticipant(playerId);
    }

    
    // TODO eric: can draw function
    public void canDraw() {
        // Quest quest = this.currentGame.getCurrentQuest();
        // int amount = 0;
        
        // amount+=quest.getStages().size();
        // for (ArrayList<AdventureCard> stage: quest.getStages()) {
        //     amount += stage.size();
        // }
        
        
        // return amount;
    }
    

    /**
     * Returns the index of the current active player (not player id)
     * @return int
     */
    public int getCurrentActivePlayer() {
        return this.currentActivePlayer;
    }

    public void setCurrentActivePlayer(int currentActivePlayer) {
        this.currentActivePlayer = currentActivePlayer;
    }

    //currentActivePlayer is the index of the active player
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
    
    public int getIndexOfPlayer(int playerId) {
        int index = 0;
        for (Player player: this.currentGame.getPlayers()) {
            if (player.getId() == playerId) {
                return index;
            }
            
            index++;
        }
        
        return -1;
    }

    //~~~~~~~`turn~~~~~~~~~`
    public void setCurrentStoryCard(StoryCard card){
        this.currentStoryCard=card;
        if (card instanceof Tournament){
            tournamentInPlay = true;
        }
        
        if (card instanceof EventCard) {
            this.eventInPlay = true;
        }
    }
    
    public StoryCard getCurrentStoryCard(){
        return this.currentStoryCard;
    }
    public void setQuestInPlay(boolean x){
        this.questInPlay=x;
    }
    public boolean getQuestInPlay(){
        // if(questInPlay){
        //     if(currentGame.getCurrentQuest().getCurrentStageNumber() > Integer.parseInt(currentGame.getCurrentQuest().getTotalStages())){
        //         setQuestInPlay(false);
        //     }
        // }
        return questInPlay;
    }
    public boolean getTournamentInPlay(){
        return this.tournamentInPlay;
    }
    public void setTournamentInPlay(boolean x){
        this.tournamentInPlay =x;
    }


    // -------------- Tournament functionality --------------
    public int getNumPlayersTourn(){
        int numOfPlayers = this.currentGame.getCurrentTournament().getParticipantSize();
        return numOfPlayers;
    }

    public int getSinglePlayerIdTourn(){
        Tournament t = this.currentGame.getCurrentTournament();
        if (t.getParticipantSize() == 1){
            // return player id for single player tournament
            return t.getParticipants().get(0);
        }
        // return -1 if there are more than one player
        return -1;
    }

    public int getAutoAwardSinglePlayer(){
        return this.currentGame.getCurrentTournament().getAutoAwardSinglePlayer();
    }

    public boolean addPlayerCardsTourn(int playerId, ArrayList<String> cardsToAdd){

        ArrayList<String> weapons = new ArrayList<>();
        
        Player player = this.currentGame.getPlayerById(playerId);
        for (String cardName : cardsToAdd){
            Card currentCard = this.currentGame.getCardObjects().getCardByName(cardName);
            // sets amour card if exists
            if (cardName.equals("Amour")){
                player.setAmour((Amour)currentCard);
                //cardsToAdd.remove(cardName);
                continue;
            }

            // sets allies (add to player's ally array)
            else if (currentCard instanceof Ally){
                player.addActiveAlly((Ally)currentCard); //uncommment and test after ally array is added
                //cardsToAdd.remove(cardName);
            }
            else {
                weapons.add(cardName);
            }
            
        }
        boolean returnVal = this.currentGame.getCurrentTournament().addPlacedCards(playerId, weapons);
        if (returnVal){
            setTieResult();
        }
        return true;
    }

    // discard cards (all but ally) after the tournament is complete 
    // new: basically only discards amour
    public void discardCardsAfterTournament(){
        for (int i : this.currentGame.getCurrentTournament().getAllPlayerCards().keySet()){
            Player player = this.getCurrentGame().getPlayerById(i);

            // remove amour card
            if(player.getAmour() != null){
                player.discardCard(player.getAmour().getName());
                player.setAmour(null);
            }
            
        }
        tournamentInPlay = false;
    }


    // discard cards after a tournament tie (only weapon cards, ally and amour stay)
    public void discardCardsAfterTie(){
        for (int i : this.currentGame.getCurrentTournament().getAllPlayerCards().keySet()){
            Player player = this.getCurrentGame().getPlayerById(i);

            for (String cardName : this.currentGame.getCurrentTournament().getPlayerCards(i)){
                AdventureCard cardToRemove = this.getCurrentGame().getCardObjects().getCardByName(cardName);
                if (!(cardToRemove instanceof Ally) && !(cardToRemove instanceof Amour)){
                    cardToRemove = player.discardCard(cardName);
                } 
            }
        }
    }


    public void setTieResult(){
        ArrayList<ArrayList<Card>> temp = new ArrayList<>();
        int maxPts = 0;
        ArrayList<Integer> winners = new ArrayList<>();

        for (int i = 1; i < this.currentGame.getPlayers().size()+1; i++){
            ArrayList<Card> tempCards = new ArrayList<>();
            Player player = this.currentGame.getPlayerById(i);
            int playerTotal = 0;

            ArrayList<String> currCards = this.currentGame.getCurrentTournament().getPlayerCards(i);
            if (currCards != null){
                for (String cardName : currCards ){
                    tempCards.add(this.currentGame.getCardObjects().getCardByName(cardName));
                    playerTotal += this.currentGame.getCardObjects().getBattlePtsByName(cardName);
                }
                // add amour card
                if (player.getAmour() != null){
                    tempCards.add(player.getAmour());
                    playerTotal += player.getAmour().getBattlePoints();
                }

                // add active ally cards 
                for (Ally allyCard : player.getActiveAllies()){
                    tempCards.add(allyCard);
                    playerTotal += allyCard.getBattlePoints(this.currentGame.getCurrentTournament().getName(), player.getActiveAllies());
                }

                // add Rank pts (now we just need to deal with Ally pts)
                tempCards.add(new Rank(player.getRankString(), player.getRankPts()));
                playerTotal += player.getRankPts();
                temp.add(tempCards);    
            }else {
                temp.add(null);
            }
            if (playerTotal > maxPts){
                winners.clear();
                winners.add(i);
                maxPts = playerTotal;
            }else if (playerTotal == maxPts){
                winners.add(i);
            }

        }

        // if theres a tie
        System.out.println("winners: " + winners);
        System.out.println("winner size: " + winners.size());
        if (winners.size() > 1){
            this.currentGame.getCurrentTournament().setTieOccurred(true);
            System.out.println(this.currentGame.getCurrentTournament().getTieOccured() + " -- ");
        }else{
            this.currentGame.getCurrentTournament().setTieOccurred(false);
        }
        
        
    }


    // returns a double array list [   [real cards for player 1], [real cards for player 2], ...      ]
    // if  a player is not in the tournament, then in that array spot, there will be null
    public ArrayList<ArrayList<Card>> getAllTournamentPlayerCards(){
        ArrayList<ArrayList<Card>> temp = new ArrayList<>();
        int maxPts = 0;
        ArrayList<Integer> winners = new ArrayList<>();

        // if there is only one player, return null
        if (this.currentGame.getCurrentTournament().getParticipantSize() <= 1){
            return null;
        }

        for (int i = 1; i < this.currentGame.getPlayers().size()+1; i++){
            ArrayList<Card> tempCards = new ArrayList<>();
            Player player = this.currentGame.getPlayerById(i);
            int playerTotal = 0;

            ArrayList<String> currCards = this.currentGame.getCurrentTournament().getPlayerCards(i);
            if (currCards != null){
                for (String cardName : currCards ){
                    tempCards.add(this.currentGame.getCardObjects().getCardByName(cardName));
                    playerTotal += this.currentGame.getCardObjects().getBattlePtsByName(cardName);
                }
                // add amour card
                if (player.getAmour() != null){
                    tempCards.add(player.getAmour());
                    playerTotal += player.getAmour().getBattlePoints();
                }

                // add active ally cards 
                for (Ally allyCard : player.getActiveAllies()){
                    tempCards.add(allyCard);
                    playerTotal += allyCard.getBattlePoints(this.currentGame.getCurrentTournament().getName(), player.getActiveAllies());
                }

                // add Rank pts (now we just need to deal with Ally pts)
                tempCards.add(new Rank(player.getRankString(), player.getRankPts()));
                playerTotal += player.getRankPts();
                temp.add(tempCards);    
            }else {
                temp.add(null);
            }
            if (playerTotal > maxPts){
                winners.clear();
                winners.add(i);
                maxPts = playerTotal;
            }else if (playerTotal == maxPts){
                winners.add(i);
            }

        }
        // technically, at this point, either the tournament is done OR theres a tie, in
        // which case we will need to discard the adventure cards
        discardCardsAfterTie();

        // maybe reset for potential tiebreaker round here
        this.currentGame.getCurrentTournament().resetRound(winners);

        // if theres a tie
        System.out.println("winners: " + winners);
        System.out.println("winner size: " + winners.size());
        if (winners.size() > 1){
            this.currentGame.getCurrentTournament().setTieOccurred(true);
            System.out.println(this.currentGame.getCurrentTournament().getTieOccured() + " -- ");
        }
        
        return temp;
    }
    
    
    public int awardSingleWinner(int winnerId){
        int award = this.currentGame.getCurrentTournament().getAward();
        Player winner = this.currentGame.getPlayerById(winnerId);
        winner.updateShields(award);
        // tournament is over, discard the cards
        if (tournamentInPlay){
            discardCardsAfterTournament();
        }

        return winner.getNumShields();
    }

    public int awardTiedWinner(int winnerId){
        int award = this.currentGame.getCurrentTournament().getAwardTie();
        Player winner = this.currentGame.getPlayerById(winnerId);
        winner.updateShields(award);

        // tournament is over, discard the cards
        if (tournamentInPlay){
            discardCardsAfterTournament();
        }

        return winner.getNumShields();
    }

    // if only one person enters the tournament
    public int awardSingleGameWinner(int winnerId){
        int award = this.currentGame.getCurrentTournament().getAutoAwardSinglePlayer();
        Player winner = this.currentGame.getPlayerById(winnerId);
        winner.updateShields(award);

        // tournament is over, discard the cards
        if (tournamentInPlay){
            discardCardsAfterTournament();
        }

        return winner.getNumShields();
    }

    public int awardSingleGameWinnerAuto(){
        Tournament t = this.currentGame.getCurrentTournament();
        if (t.getParticipantSize() > 0){
            int winnerId = t.getParticipants().get(0);
            Player winner = this.currentGame.getPlayerById(winnerId);
            winner.updateShields(t.getAutoAwardSinglePlayer());
            return winner.getNumShields();
        }
        return 0;
        
    }

    //adding to the active ally for a player from its stages for a quest
    public void setActiveAlliesFromQuestStage(int playerId,ArrayList<String> stageCards){
        Player player = this.currentGame.getPlayerById(playerId);
        for(int i=0;i<stageCards.size();i++){
            AdventureCard card = this.currentGame.getCardObjects().getCardByName(stageCards.get(i));
            if(card instanceof Ally){
                player.activeAllies.add((Ally) card);
            }
        }

    }
    

    public Test setTestCard(ArrayList<ArrayList<String>> stages){
        Test testCard = null;
       for (int i = 0; i < stages.size(); i++)
        {
            for (int j = 0; j < stages.get(i).size(); j++)
            {
                String nameOfCard = stages.get(i).get(j);
                AdventureCard advCard = cards.getCardByName(nameOfCard);
                if(advCard.getAdventureCardType() == AdventureCardType.Test){
                
                    testCard = (Test) advCard;
                    //System.out.println(testCard.getName());
                    return testCard;

                }
            } 
        }
        return testCard;
    }


    // returns an arraylist of playerIds of winners (for the entire game)
    // when client gets session data, check if there are any winners
    public ArrayList<Integer> getWinners(){
        // loops through and checks shields (to win, all they need is 5+ shields)
        ArrayList<Integer> winners = new ArrayList<>();
        ArrayList<Player> players = this.currentGame.getPlayers();
        for (Player player : players){
            if (player.getNumShields() >= 5){
                winners.add(player.getId());
            }
        }
        return winners;
    }


    // ------------------------------ Tournament Turns -------------------------------
    public int incrementRound(){
        return this.currentGame.getCurrentTournament().incrementRound();
    }

    public AdventureCard getCardByName(String cardName) {
        return this.cards.getCardByName(cardName);
    }
}

