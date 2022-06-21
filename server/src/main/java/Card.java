import java.io.Serializable;

public class Card implements Serializable {
    String suite; // store suite
    int value ; // store card value, joker and above counts as 11,12,..

    Card(String theSuite, int theValue){
        this.suite = theSuite;
        this.value = theValue;
    }

}
