import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BaccaratGameLogicTest {
    ArrayList<Card> hand1 = new ArrayList<>(); // player
    ArrayList<Card> hand2 = new ArrayList<>(); // banker

    @BeforeEach
    void setup(){
        hand1.add(new Card("Spade",5));
        hand1.add(new Card("Spade",3));

        hand2.add(new Card("Spade",6));
        hand2.add(new Card("Spade",1));

    }

    @Test
    void whoWonTest(){
        assertEquals("Player",BaccaratGameLogic.whoWon(hand1,hand2));
    }

    @Test
    void whoWon2Test(){
        hand1.clear();
        hand2.clear();

        hand1.add(new Card("Spade",4));
        hand1.add(new Card("Spade",1));

        hand2.add(new Card("Spade",6));
        hand2.add(new Card("Spade",3));
        assertEquals("Banker",BaccaratGameLogic.whoWon(hand1,hand2));
    }

    @Test
    void whoWon3Test(){
        hand1.clear();
        hand2.clear();

        hand1.add(new Card("Clubs",5));
        hand1.add(new Card("Clubs",4));

        hand2.add(new Card("Spade",6));
        hand2.add(new Card("Spade",3));
        assertEquals("Draw",BaccaratGameLogic.whoWon(hand1,hand2));
    }

    @Test
    void handTotalTest(){
        assertEquals(8,BaccaratGameLogic.handTotal(hand1));
    }

    @Test
    void handTotal2Test(){
        hand2.clear();

        hand2.add(new Card("Spade",7));
        hand2.add(new Card("Spade",6));
        assertEquals(3,BaccaratGameLogic.handTotal(hand2));
    }

    @Test
    void evaluateBankerDrawTest(){
        assertFalse(BaccaratGameLogic.evaluateBankerDraw(hand2,new Card("Diamond",7)));
    }

    @Test
    void evaluateBankerDraw2Test(){
        hand2.clear();

        hand2.add(new Card("Spade",3));
        hand2.add(new Card("Spade",2));
        assertTrue(BaccaratGameLogic.evaluateBankerDraw(hand2,new Card("Diamond",5)));
    }

    @Test
    void evaluatePlayerDrawTest(){
        assertFalse(BaccaratGameLogic.evaluatePlayerDraw(hand1));
    }

    @Test
    void evaluatePlayerDraw2Test(){
        hand1.clear();

        hand1.add(new Card("Spade",7));
        hand1.add(new Card("Spade",6));
        assertTrue(BaccaratGameLogic.evaluatePlayerDraw(hand1));
    }
}