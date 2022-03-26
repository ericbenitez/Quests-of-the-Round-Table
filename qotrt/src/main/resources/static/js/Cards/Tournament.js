/* 
Thinking/Planning:

Winner = person with the most BATTLE POINTS

1) Tournament card is drawn
2) Each player decides whether to join the tournament (start at the drawer, then go left)
3) 1 person only = auto awarded one shield + bonus shields (on the card)

4) Each participant draws one card from adventure deck
5) Each participant chooses their cards: 
    5a) May choose: Ally, Weapon, Amour cards
        - can't play more than one weapon card of same type, max 1 amour card
        - they can choose no cards (only calc the rank+ally pts)

6) IN UNISON, all participants place cards FACE UP
7) Player with the most battle points (rank + ally + weapon + amour) wins tournament

8) End tournament: discard weapon and amour cards discarded
    (ally cards remain)

Winner prize = # of participants + bonus shields      (prize is basically shields)


---------------------------------------------------------------------------


TIE: 
(participants are now the players that were tied)

1) participants discard all weapon cards that were in play
    (ally, amour cards can stay)

2) Play again like in the first round (choose cards, calc winner, blah blah blah) (basically steps 4-7)
3) if TIE AGAIN: WINNER PRIZE = # of participants in the original tournament (shields)


*/


/*
Variables:
let tourParticipant = false;
let drawerTournament = 0;
*/



function startTournament(sessionData){
    // we need players to join the tournament first to get all the participants
    // I need to see the session data stuff
    alert(sessionData);
}


// have pop up appear to get player's answer for joining tournament
function askPlayerJoinTournament(){
    document.getElementById("popUpContainer").style.display = "block";
}

function hidePopUp(){
    document.getElementById("popUpContainer").style.display = "none";
}



// if the player clicks no, move to next player
// if yes, add player to participant or something and move to next player
function joinTournament(answer){
    if (answer === "no"){
        // do nothing
    }else {
        // add participant 
    }
    // move to next player
    finishTurn();
    hidePopUp();
}