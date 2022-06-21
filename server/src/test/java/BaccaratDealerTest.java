import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BaccaratDealerTest {

    BaccaratDealer s = new BaccaratDealer();
    ArrayList<Card> testDeck;

    @BeforeEach
    void setup(){
        s.generateDeck();
        testDeck = new ArrayList<>();
        String [] suites = { "Club", "Heart", "Spade","Diamond"};
        for ( int i=0; i < 4 ; i++){
            for ( int j=1; j < 14; j++) {
                Card card = new Card(suites[i], j);
                testDeck.add(card);
            }
        }
    }

    @Test
    void generateDeckTest(){
        s.generateDeck();
        assertEquals(52,s.deck.size());
        for ( int i =0; i <52;i++){
            assertEquals(testDeck.get(i).suite, s.deck.get(i).suite);
            assertEquals(testDeck.get(i).value, s.deck.get(i).value);
        }

    }

    @Test
    void generateDeck2Test(){
        s.generateDeck();
        String [] sSuite = new String [52];
        int [] sValue = new int [52];
        String [] tSuite = new String [52];
        int [] tValue = new int [52];
        for ( int i =0; i <52;i++){
            tSuite[i] = (testDeck.get(i).suite);
            sSuite[i] = (s.deck.get(i).suite);
            tValue[i] = (testDeck.get(i).value);
            sValue[i] = (s.deck.get(i).value);
        }
        assertArrayEquals(tSuite,sSuite);
        assertArrayEquals(tValue,sValue);
    }

    @Test
    void dealHandTest(){
        ArrayList<Card> currHand =  s.dealHand();

        assertEquals("Club",currHand.get(0).suite);
        assertEquals(1,currHand.get(0).value);
        assertEquals("Club",currHand.get(1).suite);
        assertEquals(2,currHand.get(1).value);

        assertEquals(2,currHand.size());
        assertEquals(50,s.deck.size());
        assertFalse(s.deck.contains(currHand.get(0)));
        assertFalse(s.deck.contains(currHand.get(1)));
    }

    @Test
    void dealHand2Test(){
        ArrayList<Card> currHand =  s.dealHand();
        ArrayList<Card> nextHand =  s.dealHand();
        assertEquals("Club",currHand.get(0).suite);
        assertEquals(1,currHand.get(0).value);
        assertEquals("Club",currHand.get(1).suite);
        assertEquals(2,currHand.get(1).value);
        assertEquals("Club",nextHand.get(0).suite);
        assertEquals(3,nextHand.get(0).value);
        assertEquals("Club",nextHand.get(1).suite);
        assertEquals(4,nextHand.get(1).value);
        assertEquals(2,currHand.size());
        assertEquals(2,nextHand.size());
        assertEquals(48,s.deck.size());
        assertNotEquals(currHand.get(0),nextHand.get(0));
        assertNotEquals(currHand.get(1),nextHand.get(1));
    }

    @Test
    void drawOneTest(){
        Card currCard =  s.drawOne();
        assertEquals("Club",currCard.suite);
        assertEquals(1,currCard.value);
        assertEquals(51,s.deck.size());
        assertFalse(s.deck.contains(currCard));
    }

    @Test
    void drawOne2Test(){
        Card currCard =  s.drawOne();
        Card nextCard =  s.drawOne();
        assertEquals("Club",currCard.suite);
        assertEquals(1,currCard.value);
        assertEquals("Club",nextCard.suite);
        assertEquals(2,nextCard.value);
        assertEquals(50,s.deck.size());
        assertFalse(s.deck.contains(currCard));
        assertFalse(s.deck.contains(nextCard));
    }

    @Test
    void shuffleDeckTest(){
        ArrayList<Card> oldDeck = s.deck;
        s.shuffleDeck();

        assertFalse(oldDeck.equals(s.deck));
        assertEquals(52,s.deck.size());
    }
    @Test
    void shuffleDeck2Test(){
        s.drawOne();
        s.dealHand();
        assertEquals(49,s.deck.size());
        ArrayList<Card> oldDeck = s.deck;
        s.shuffleDeck();
        assertFalse(oldDeck.equals(s.deck));
        assertEquals(52,s.deck.size());

    }

    @Test
    void deckSizeTest(){
        assertEquals(52,s.deckSize());
    }
    @Test
    void deckSize2Test(){
        s.drawOne();
        s.dealHand();
        assertEquals(49,s.deckSize());

    }
}