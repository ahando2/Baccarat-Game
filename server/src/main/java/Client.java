import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;



public class Client extends Thread{

	// initialize variables
	Socket socketClient = null;
	BaccaratInfo baccaratInfo= new BaccaratInfo();
	ObjectOutputStream out;
	ObjectInputStream in;
	private Consumer<Serializable> callback;
	String host;
	int port;


	Client(Consumer<Serializable> call){
		callback = call;
	}

	public void run() {

		// set up the client socket
		try {
			socketClient = new Socket(this.host,this.port);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		}
		// if failed then terminate program
		catch(ConnectException l){

		} catch (SocketException t) {
			System.out.println("server is out");
			BaccaratInfo temp = new BaccaratInfo();
			temp.setMessage("server down");
			callback.accept(this.baccaratInfo);
			t.printStackTrace();
		} catch(Exception e) {

			BaccaratInfo temp = new BaccaratInfo();
			temp.setMessage("server down");
			callback.accept(this.baccaratInfo);
			System.out.println("server is out");

		}

		// getting input from server
		while(true) {

			try {
				this.baccaratInfo = (BaccaratInfo) in.readObject();

				callback.accept(this.baccaratInfo); // throw to gui
			}catch (SocketException t) {}
			catch(Exception e) {}
		}

	}

	// send info the server
	public void send(BaccaratInfo baccaratInfo) {

		try {
			out.writeObject(baccaratInfo); // throw the baccaratInfo to server

		}
		catch (SocketException t) { // if failed most likely server is down, terminate program
			BaccaratInfo temp = new BaccaratInfo();
			temp.setMessage("server down");
			callback.accept(temp);

		}catch( NullPointerException t){
			System.out.println("server is out");
			BaccaratInfo temp = new BaccaratInfo();
			temp.setMessage("server down");
			callback.accept(temp);
		}
		catch ( IOException e){
		}


		// clean out for next sending
		try {
			out.reset();
		} catch( NullPointerException l){ }
		catch (SocketException t) { }
		catch ( IOException e){

		}
	}

}
