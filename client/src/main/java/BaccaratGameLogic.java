import java.util.ArrayList;

public class BaccaratGameLogic {
    // get whowon based on hand value
    // assuming hand 1 is player and hand 2 is banker
    public static String whoWon(ArrayList<Card> hand1, ArrayList<Card> hand2){
        int hand1_value = handTotal(hand1);
        int hand2_value = handTotal(hand2);
        if ( hand1_value > hand2_value) return "Player";
        if ( hand1_value < hand2_value) return "Banker";
        return "Draw";

    }

    // get the hand value, only counts 0 to 10 and get only the first digit.
    public static int handTotal(ArrayList<Card> hand){
        int total = 0;
        for (Card card : hand) {
            if ( card.value < 11){
                total += card.value;
            }
        }

        if (total >= 10) total %= 10;

        return total;
    }

    // evaluate if banker needs to draw or not based of player draw
    public static boolean evaluateBankerDraw(ArrayList<Card> hand, Card playerCard){
        int bankerValue = handTotal(hand);
        if (bankerValue >= 7) return false;
        else if (bankerValue >= 0 && bankerValue <= 3 )return true;
        else if (bankerValue == 4 && ((playerCard.value >= 2 && playerCard.value <= 7) || playerCard.value == -1)) return true;
        else if (bankerValue == 5 && ((playerCard.value >= 4 && playerCard.value <= 7) || playerCard.value == -1)) return true;
        else if (bankerValue == 6 && playerCard.value >= 6 && playerCard.value <= 7) return true;
        return false;
    }

    // evaluate if playe needs to draw or not
    public static boolean evaluatePlayerDraw(ArrayList<Card> hand){
        if (handTotal(hand) >= 6) return false;
        return true;
    }
}
