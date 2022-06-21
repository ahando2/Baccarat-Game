import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    Card card ;

    @Test
    void ConstructorTest(){
        card = new Card("Spade",3);
        assertEquals("Spade", card.suite,"wrong value");
        assertEquals(3, card.value,"wrong value");
    }

}