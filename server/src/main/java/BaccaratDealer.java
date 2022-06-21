import java.util.ArrayList;
import java.util.Random;

public class BaccaratDealer {
    ArrayList<Card> deck;
    Random random= new Random();

    // generate new deck
    public void generateDeck(){
        this.deck = new ArrayList<>();
        String [] suites = { "Club", "Heart", "Spade","Diamond"};
        for ( int i=0; i < 4 ; i++){
            for ( int j=1; j < 14; j++) {
                Card card = new Card(suites[i], j);
                this.deck.add(card);
            }
        }
    }

    // return 2 card to hand
    public ArrayList<Card> dealHand(){
       ArrayList<Card> hand = new ArrayList<>();

        for ( int i = 0; i < 2 ; i++) {
            hand.add(this.deck.get(0));
            this.deck.remove(0);
        }

        return hand;
    }

    // return a card
    public Card drawOne(){
        ArrayList<Integer> order = new ArrayList<>();
        Card card = new Card("none",-1);
        card = this.deck.get(0);
        this.deck.remove(0);

        return card;
    }

    // generate new deck and shuffle it
    public void shuffleDeck(){
        generateDeck();
        ArrayList<Integer> order = new ArrayList<>();
        ArrayList<Card> newDeck = new ArrayList<>();

        for ( int i=0; i < deckSize(); i++){
            int index = random.nextInt(deckSize());
            if (!order.contains(index)){
                order.add(index);
                newDeck.add(this.deck.get(index));
            }else i--;
        }
        this.deck = newDeck;
    }

    // return current deck size
    public int deckSize(){
        return this.deck.size();
    }

}
