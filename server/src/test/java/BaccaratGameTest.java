import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BaccaratGameTest {

    static BaccaratGame game;

    @BeforeAll
    static void setup(){
        game = new BaccaratGame();
    }

    @Test
    void constructorTest(){
        assertNotNull(game);
    }

    @Test
    void evaluateWinningTest(){
        game.currentBet = 50;
        game.bid = "Player";
        game.playerHand = new ArrayList<>();
        game.playerHand.add(new Card("Diamond",5));
        game.playerHand.add(new Card("Club",4));

        game.bankerHand = new ArrayList<>();
        game.bankerHand.add(new Card("Diamond",3));
        game.bankerHand.add(new Card("Spade",4));

        assertEquals(50,game.evaluateWinnings());
    }

    @Test
    void evaluateWinning2Test(){
        game.currentBet = 50;
        game.bid = "Draw";
        game.playerHand = new ArrayList<>();
        game.playerHand.add(new Card("Diamond",5));
        game.playerHand.add(new Card("Club",4));

        game.bankerHand = new ArrayList<>();
        game.bankerHand.add(new Card("Club",5));
        game.bankerHand.add(new Card("Spade",4));

        assertEquals(400,game.evaluateWinnings());
    }

    @Test
    void rebidTest(){
        game.gameRebid();
        assertEquals(-1, game.currentBet);
        assertEquals("none", game.bid);

    }

    @Test
    void rebid2Test(){
        ArrayList<Card> oldPlayer = game.playerHand;
        ArrayList<Card> oldBanker = game.bankerHand;
        game.gameRebid();
        assertFalse(oldBanker.equals( game.bankerHand));
        assertFalse(oldPlayer.equals(game.playerHand));

    }
}