let currentQuest;
let currentStages;
let foe;
function sponsorQuest(){
    //console.log("here");
    stompClient.send("/app/sponsorQuest", {}, "Journey Through the Enchanted Forest");
     stompClient.subscribe('/topic/sponsorQuest', function (response) {
            var data = JSON.parse(response.body);
            //console.log("here is the data", data);
            currentQuest = data.name;
            currentStages = data.stages*1;
            foe = data.foe;
            console.log(currentQuest,currentStages, foe);
            setStages(currentQuest, currentStages, foe);

        });
}

let count = 1;
let selectedCards =0;
let totalAdventureCardsWon = (count-1)+selectedCards;
function setStages(currentQuest,currentStages,foe){
    let stagesArea = document.getElementById("stages");
    if(count <= currentStages ){
        alert("pick cards for your "+count+"/3 stages");
        let stage = document.createElement("div");
        let dynamicButton = document.createElement("button");
        var t = document.createTextNode("Create Stage "+count);
        dynamicButton.appendChild(t);
        dynamicButton.setAttribute("onclick", "stageNumCards()" );
        dynamicButton.setAttribute("id","stageButton")
        document.getElementById("placeCardButtons").appendChild(dynamicButton)

        }
        else{
        alert("you have no more stages to set");
        }

}
function stageNumCards(){
        checked = getAllChecked(); //returns checked cards
        //send the cards to server to place in array of stages.
        //place cards
        removeAllCheckedCards(checked);//remove from hand
        let stageSpecificDiv = document.createElement("div");
        stageSpecificDiv.append("Cards for Stage"+count);
        for (let i = 0; i < checked.length; i++){
            stageSpecificDiv.append(checked[i]);
            selectedCards++;

        }
        document.getElementById("stages").appendChild(stageSpecificDiv);

        count++;
         var elem = document.getElementById('stageButton');
         elem.parentNode.removeChild(elem);
        setStages(currentQuest,currentStages,foe);

}
function advCardsForSponsor(){
    alert("pick this many adventure cards"+selectedCards);
}
