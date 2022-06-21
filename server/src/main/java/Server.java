import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server{

	// initialize Variables
	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<>();
	TheServer server;
	int port;
	private Consumer<Serializable> callback;
	boolean on = true;

	// initialize server
	Server(Consumer<Serializable> call){
	
		callback = call;
		server = new TheServer();
		server.start();
	}

	public class TheServer extends Thread{
		
		public void run() {

			// create the server socket
			try(ServerSocket mysocket = new ServerSocket(port);){
				System.out.println("Server is starting");
				callback.accept("this server port "+ port);
				while(true) {

					if (!on){ // server is turned off the close the serversocket
						mysocket.close();
					}if ( mysocket.isClosed()){
						mysocket.bind(mysocket.getLocalSocketAddress(),mysocket.getLocalPort());
					}
					else {// accept new clients and add them to list
						ClientThread c = new ClientThread(mysocket.accept(), count);
						callback.accept("client has connected to server: " + "client #" + count);
						callback.accept("total active client " + (clients.size() + 1));
						clients.add(c);
						c.start();

						count++;
					}



				}

			}
			 catch (SocketException t) {
				 callback.accept("Server socket did not launch");
			}
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}
		}
	

		class ClientThread extends Thread{
			
			// inirialize variables
			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			BaccaratGame game = null;
			BaccaratInfo baccaratInfo = null;
			SocketAddress s;

			// initialize the client sockets
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
			}

			// update the clients from server
			public void updateClients(BaccaratInfo baccaratInfo) {

					try {
					 this.out.writeObject(baccaratInfo); // this refer to current client
					 this.out.reset();
					}
					catch(Exception e) {
					}
//				}

			}


			
			public void run(){


					try { // initialize sockets
						in = new ObjectInputStream(connection.getInputStream());
						out = new ObjectOutputStream(connection.getOutputStream());
						connection.setTcpNoDelay(true);
						this.game = new BaccaratGame();
						this.baccaratInfo = new BaccaratInfo();
					} catch (Exception e) {
						System.out.println("Streams not open");
					}

					updateClients(this.baccaratInfo);

					while (true) {
						if (!on) { // if server is off then, notify clients the close all input and output socket
							try {
								BaccaratInfo temp = new BaccaratInfo();
								temp.setMessage("server down");
								updateClients(baccaratInfo);
								this.s = connection.getLocalSocketAddress(); // store current addres to reopen
								in.close();
								out.close();
								connection.shutdownInput();
								connection.shutdownOutput();
								connection.close();
							} catch (SocketException t) {}
							catch (IOException e) {
								e.printStackTrace();
							}

						}
						else if (connection.isClosed() && on) { // reopen socket

							try {
								connection.connect(this.s,this.count);
								in = new ObjectInputStream(connection.getInputStream());
								out = new ObjectOutputStream(connection.getOutputStream());
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
						if(on && connection.isConnected()) { // server is on and socket is open
							try {

								this.baccaratInfo = (BaccaratInfo) in.readObject(); // get input



								if (this.baccaratInfo.getMessage().equals("new round")) { // if new round then get new hands and clients bid and current bet
									this.game.bid = this.baccaratInfo.getBid();
									this.game.currentBet = this.baccaratInfo.getCurrentBet();
									this.baccaratInfo.setPlayerHand(this.game.playerHand);
									this.baccaratInfo.setBankerHand(this.game.bankerHand);

									this.baccaratInfo.setMessage(BaccaratGameLogic.handTotal(this.game.playerHand) + " " + BaccaratGameLogic.handTotal(this.game.bankerHand));

									callback.accept("client#" + count + " is starting a new round, bet on " + this.baccaratInfo.getBid() + " , $" + this.baccaratInfo.getCurrentBet());
								} else if (this.baccaratInfo.getMessage().equals("evaluate")) { // if evaluate the cards and tell who won, and how much client lost
									double currWin = this.game.evaluateWinnings();
									this.game.totalWinnings = currWin;
									this.baccaratInfo.setPlayerHand(this.game.playerHand);
									this.baccaratInfo.setBankerHand(this.game.bankerHand);
									this.baccaratInfo.setTotalWinnings(this.game.totalWinnings);

									this.baccaratInfo.setMessage(String.valueOf(currWin));

									callback.accept("client#" + count + " round ends,  bet result:" + this.baccaratInfo.getTotalWinnings());

								} else if (this.baccaratInfo.getMessage().equals("rebid")) { // if rebid then 'reset' the necessary variable, get new cards
									game.gameRebid();

									this.baccaratInfo.setCurrentBet(game.currentBet);
									this.baccaratInfo.setPlayerHand(game.playerHand);
									this.baccaratInfo.setBankerHand(game.bankerHand);
									this.baccaratInfo.setBid(game.bid);
									this.baccaratInfo.setMessage("");


								}
//

								updateClients(this.baccaratInfo); // update the clients


							} catch (Exception e) { // if client is down then terminate client

								callback.accept("client#" + count + "has disconnected");
								callback.accept("total active client " + (clients.size() - 1));
								updateClients(this.baccaratInfo);
								clients.remove(this);
								break;
							}
						}
					}

				}//end of run
			
			
		}//end of client thread
}


	
	

	
