import java.io.Serializable;
import java.util.ArrayList;

public class BaccaratInfo  implements Serializable {

    // initialize variable
    private String message;

    private double currentBet;
    private double totalWinnings;
    private String bid;
    private String clientID;
    private String clientHost;
    private int clientPort;
    private ArrayList<String> clientsList ;

    private  ArrayList<Card> playerHand;
    private  ArrayList<Card> bankerHand;

    // default constructor
    public BaccaratInfo() {
        this.message = "Start";
        this.playerHand = new ArrayList<>();
        this.bankerHand = new ArrayList<>();
        this.currentBet = -1;
        this.totalWinnings = 0;
        this.bid = "none";
        this.clientID = "00";
        this.clientsList = new ArrayList<>();
        this.clientHost = "";
        this.clientPort = 0;
    }

    // default constructor2
    public BaccaratInfo(String message, double currentBet, double totalWinnings, String bid, String clientID, String clientHost, int clientPort, ArrayList<String> clientsList, ArrayList<Card> playerHand, ArrayList<Card> bankerHand) {
        this.message = message;
        this.currentBet = currentBet;
        this.totalWinnings = totalWinnings;
        this.bid = bid;
        this.clientID = clientID;

        this.clientsList = clientsList;

        this.clientHost = clientHost;
        this.clientPort = clientPort;
        this.playerHand = playerHand;
        this.bankerHand = bankerHand;
    }

    // getting and setting the private variable functions
    public double getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(double currentBet) {
        this.currentBet = currentBet;
    }

    public double getTotalWinnings() {
        return totalWinnings;
    }

    public void setTotalWinnings(double totalWinnings) {
        this.totalWinnings = totalWinnings;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public ArrayList<String> getClientsList() {
        return clientsList;
    }

    public void setClientsList(ArrayList<String> clientsList) {
        this.clientsList = clientsList;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(ArrayList<Card> playerHand) {
        this.playerHand = playerHand;
    }

    public ArrayList<Card> getBankerHand() {
        return bankerHand;
    }

    public void setBankerHand(ArrayList<Card> bankerHand) {
        this.bankerHand = bankerHand;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
