import java.util.ArrayList;

public class BaccaratGame {

    // initialize variable
    ArrayList<Card> playerHand;
    ArrayList<Card> bankerHand;
    BaccaratDealer theDealer;
    double currentBet;
    double totalWinnings;
    String bid;

    // class contructor
    BaccaratGame(){
        currentBet = -1;
        totalWinnings = 0;
        bid = "none";
        theDealer =  new BaccaratDealer();
        theDealer.shuffleDeck();
        playerHand = theDealer.dealHand();
        bankerHand = theDealer.dealHand();
    }

    // 'rebid' is to reset the game, since new round is choosen
    public void gameRebid( ){
        if (theDealer.deckSize() < 6) theDealer.shuffleDeck();
        this.currentBet = -1;
        this.bid = "none";
        this.playerHand = theDealer.dealHand();
        this.bankerHand = theDealer.dealHand();
    }

    // get the current round winning
    public double evaluateWinnings(){
        int playerValue = BaccaratGameLogic.handTotal(playerHand);
        int bankerValue = BaccaratGameLogic.handTotal(bankerHand);

        if (playerValue < 8 && bankerValue < 8){ // if not natural, get new cards
            Card player3rdCard = new Card("none", -1);
            if ( BaccaratGameLogic.evaluatePlayerDraw(playerHand)) {
                player3rdCard = theDealer.drawOne();
                playerHand.add(player3rdCard);
            }
            if (BaccaratGameLogic.evaluateBankerDraw(bankerHand,player3rdCard)){
                Card banker3rdCard = theDealer.drawOne();
                bankerHand.add(banker3rdCard);
            }
        }

        String winner = BaccaratGameLogic.whoWon(playerHand, bankerHand);
        if (bid.equals(winner)){
            if (winner.equals("Player")){ // bet on player win 1:1
                return currentBet;
            }
            if (winner.equals("Banker")){ // bet on player win 1:1 with 5% to banker
                return Math.round((currentBet*0.95)* 100.0) / 100.0;
            }
            if (winner.equals("Draw")){ // bet on draw win 8:1
                return currentBet* 8;
            }
        }
        else if (winner.equals("Draw")){ // if draw is the winner and user didn't choose draw then they got their money back
            return 0;
        }

        return currentBet*-1; // else user lost the money they bet

    }
}
