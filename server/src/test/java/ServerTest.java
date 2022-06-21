import javafx.application.Application;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    Server connection;
    Client client,client2;
    String message, message1,message2;
    BaccaratInfo baccaratInfo;
    BaccaratInfo baccaratInfo1 = new BaccaratInfo();
    BaccaratInfo baccaratInfo2 = new BaccaratInfo();
    Consumer<Serializable> callback;

    @BeforeEach
    void setUp() {


        connection = new Server(data -> {
                message = String.valueOf(data);
        });
        connection.port = 5555;

        

        client = new Client( data -> {
            Platform.runLater(() -> {
                Platform.runLater(() -> {
                    baccaratInfo1 = (BaccaratInfo) data;
                });
            });
        });

        client2= new Client( data ->{
            Platform.runLater(() -> {
                baccaratInfo2 = (BaccaratInfo) data;
            });
        });
        client2.port = 5555;
        client2.host = "127.0.0.1";
        client2.start();

        client.port = 5555;
        client.host = "127.0.0.1";
        client.start();


    }

    @Test
    void constructorTest(){
        assertNotNull(connection);
    }

    @Test
    void theServerTest(){
        assertEquals(connection.clients.size()+1,connection.count);
    }

    @Test
    void theServer2Test(){
        assertTrue(connection.server.isAlive());
    }

    @Test
    void clientThreadTest(){
        assertTrue(client.isAlive());
    }
    @Test
    void clientThread2Test(){
        assertTrue(client2.isAlive());
    }




}