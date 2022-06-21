import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BaccaratInfoTest {
    BaccaratInfo info;
    double currentBet;
    double totalWinnings;
    String bid;
    String clientID;
    String host = "123.0.0.4";
    int port = 1234;
    ArrayList<String> clientsList ;

    String message ="";
    ArrayList<Card> playerHand = new ArrayList<>(); // player
    ArrayList<Card> bankerHand = new ArrayList<>(); // banker


    @BeforeEach
    void setup() {
        currentBet = 50;
        bid = "Player";
        totalWinnings = 0;
        clientID = "01";
        clientsList = new ArrayList<>();
        clientsList.add(clientID);
        clientsList.add(bid);
        clientsList.add(String.valueOf(currentBet));

        clientsList.add("02");
        clientsList.add("Banker");
        clientsList.add("10");

        playerHand.add(new Card("Spade",6));
        playerHand.add(new Card("Spade",1));
        bankerHand.add(new Card("Spade",5));
        bankerHand.add(new Card("Spade",3));
        info = new BaccaratInfo(message, currentBet,totalWinnings,bid,clientID,host,port,clientsList, playerHand, bankerHand);
    }

    @Test
    void getCurrentBetTest() {
        assertEquals(50,info.getCurrentBet());
    }

    @Test
    void setCurrentBetTest() {
        info.setCurrentBet(2);
        assertEquals(2,info.getCurrentBet());
    }

    @Test
    void getTotalWinningsTest() {
        assertEquals(0,info.getTotalWinnings());
    }

    @Test
    void setTotalWinningsTest() {
        info.setTotalWinnings(2);
        assertEquals(2,info.getTotalWinnings());
    }

    @Test
    void getBidTest() {
        assertEquals("Player",info.getBid());
    }

    @Test
    void setBidTest() {
        info.setBid("Draw");
        assertEquals("Draw",info.getBid());
    }

    @Test
    void getClientIDTest() {
        assertEquals("01",info.getClientID());
    }

    @Test
    void getClientID2Test() {
        clientID = "05";
        info = new BaccaratInfo(message, currentBet,totalWinnings,bid,clientID,host,port,clientsList, playerHand, bankerHand);
        assertEquals("05",info.getClientID());
    }

    @Test
    void setClientIDTest() {
        info.setClientID("02");
        assertEquals("02",info.getClientID());
    }

    @Test
    void setClientID2Test() {
        info.setClientID("06");
        assertEquals("06",info.getClientID());
    }

    @Test
    void getClientsListTest() {
        assertTrue(clientsList.equals(info.getClientsList()));
    }

    @Test
    void getClientsList2Test() {
        int i = 0;
        for ( String items : clientsList){
            assertEquals( items, info.getClientsList().get(i));
            i++;
        }
    }

    @Test
    void setClientsListTest() {
        clientsList.add("03");
        clientsList.add("Draw");
        clientsList.add("25");
        info.setClientsList(clientsList);
        assertTrue(clientsList.equals(info.getClientsList()));
    }

    @Test
    void setClientsList2Test() {
        clientsList.remove(5);
        clientsList.remove(4);
        clientsList.remove(3);
        info.setClientsList(clientsList);
        assertTrue(clientsList.equals(info.getClientsList()));
    }

    @Test
    void getClientPortTest() {
        assertEquals(port, info.getClientPort());
    }

    @Test
    void getClientPort2Test() {
        port = 4321;
        info = new BaccaratInfo(message, currentBet,totalWinnings,bid,clientID,host,port,clientsList, playerHand, bankerHand);
        assertEquals(port, info.getClientPort());
    }

    @Test
    void setClientPortTest() {
        info.setClientPort(4321);
        assertEquals(4321, info.getClientPort());
    }

    @Test
    void setClientPort2Test() {
        info.setClientPort(1111);
        assertEquals(1111, info.getClientPort());
    }

    @Test
    void getClientHostTest() {
        assertEquals(host, info.getClientHost());
    }

    @Test
    void getClientHost2Test() {
        host = "127.0.0.1";
        info = new BaccaratInfo(message, currentBet,totalWinnings,bid,clientID,host,port,clientsList, playerHand, bankerHand);
        assertEquals(host, info.getClientHost());
    }

    @Test
    void setClientHostTest() {
        info.setClientHost("127.0.0.1");
        assertEquals("127.0.0.1", info.getClientHost());
    }

    @Test
    void setClientHost2Test() {
        info.setClientHost("111.0.0.1");
        assertEquals("111.0.0.1", info.getClientHost());
    }

    @Test
    void getPlayerHandTest() {
        assertTrue(info.getPlayerHand().equals(playerHand));
    }

    @Test
    void getPlayerHand2Test() {
        playerHand.add(new Card("Heart",1));
        info = new BaccaratInfo(message, currentBet,totalWinnings,bid,clientID,host,port,clientsList, playerHand, bankerHand);
        assertTrue(info.getPlayerHand().equals(playerHand));
    }

    @Test
    void setPlayerHandTest() {
        playerHand.clear();

        playerHand.add(new Card("Spade",5));
        playerHand.add(new Card("Spade",3));
        info.setPlayerHand(playerHand);
        assertTrue(info.getPlayerHand().equals(playerHand));
    }

    @Test
    void setPlayerHand2Test() {
        playerHand.clear();
        info.setPlayerHand(playerHand);
        assertTrue(info.getPlayerHand().equals(playerHand));
    }

    @Test
    void getBankerHandTest() {

        assertTrue(info.getBankerHand().equals(bankerHand));
    }

    @Test
    void getBankerHand2Test() {
        bankerHand.add(new Card("Spade",10));
        info = new BaccaratInfo(message, currentBet,totalWinnings,bid,clientID,host,port,clientsList, playerHand, bankerHand);
        assertTrue(info.getBankerHand().equals(bankerHand));
    }

    @Test
    void setBankerHandTest() {
        bankerHand.clear();
        bankerHand.add(new Card("Spade",6));
        bankerHand.add(new Card("Spade",1));
        info.setBankerHand(bankerHand);
        assertTrue(info.getBankerHand().equals(bankerHand));
    }

    @Test
    void setBankerHand2Test() {
        bankerHand.clear();
        info.setBankerHand(bankerHand);
        assertTrue(info.getBankerHand().equals(bankerHand));
    }

    @Test
    void getMessageTest(){
        assertTrue(info.getMessage() =="");
    }

    @Test
    void getMessage2Test(){
        info = new BaccaratInfo("Second", currentBet,totalWinnings,bid,clientID,host,port,clientsList, playerHand, bankerHand);
        assertTrue(info.getMessage() =="Second");
    }

    @Test
    void setMessageTest(){
        info.setMessage("Hi");
        assertTrue(info.getMessage() =="Hi");
    }


    @Test
    void setMessage2Test(){
        info.setMessage("Hi2");
        assertTrue(info.getMessage() =="Hi2");
    }
}