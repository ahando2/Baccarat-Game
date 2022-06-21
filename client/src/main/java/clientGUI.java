
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class clientGUI extends Application{

	
	TextField tHost,tPort, tCurrBet;
	Text text = new Text("");
	Button startButton,loginButton;
	HashMap<String, Scene> sceneMap;
	VBox clientBox;
	Client clientConnection;
	int show = 0;
	int stat = 0;
	boolean inputValid = true;
	int sequence = 0;
	boolean serverDown = false;
	double totalWin = 0;



	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Baccarat Game");
		text.setText("");//  reset the text value to empty string

		tHost = new TextField(); // text field for ip address
		tPort = makeIntegerTextField();// 'integer' field for port

		// login button declaration
		loginButton = makeButton("login",15,white);
		loginButton.setTextFill(black);
		loginButton.setPrefSize(100,20);
		loginButton.setAlignment(Pos.CENTER);

		loginButton.setOnAction(e->{
			this.inputValid = true;
			// checkin if the host and port inputs are valid if not, go back to start scene
			if ( this.inputValid) {

				// check if port is numeric
				try {
					int num = Integer.parseInt(tPort.getText());
				} catch (NumberFormatException nfe) {
					this.text.setText("invalid Input");
					this.text.setFill(Color.rgb(186, 26, 26));
					inputValid = false;
					primaryStage.setScene(sceneMap.get("clientStart"));
					primaryStage.setTitle("Baccarat Game");

				}

				// check if host is in 0000.0000.0000.0000 format
				String[] hostTemp = tHost.getText().split("\\.");
				if (hostTemp.length != 4) {
					this.text.setText("invalid Input");
					this.text.setFill(Color.rgb(186, 26, 26));
					inputValid = false;
				primaryStage.setScene(sceneMap.get("clientStart"));
				primaryStage.setTitle("Baccarat Game");
				}

				// check if host is actually numeric excluding the "."
				for (String digits : hostTemp) {
					try {
						int num = Integer.parseInt(digits);
					} catch (NumberFormatException nfe) {
						this.text.setText("invalid Input");
						this.text.setFill(Color.rgb(186, 26, 26));
						inputValid = false;
					primaryStage.setScene(sceneMap.get("clientStart"));
					primaryStage.setTitle("Baccarat Game");

					}
				}
			}

			// if input valid then create the client sockets
			if ( inputValid) {

				this.clientConnection = new Client(data -> {

					// get updates from client and server
					Platform.runLater(() -> {
						BaccaratInfo temp = (BaccaratInfo) data;
						// if message says server down then terminate program immediately
						if (temp.getMessage().equals("server down")) {
							this.serverDown = true;

							primaryStage.setScene(sceneMap.get("error"));
							primaryStage.setTitle("server Down");
							try {
								errorStage(primaryStage);
							} catch (Exception exception) {
								exception.printStackTrace();
							}

						}
						// else update bacacarat info
						this.clientConnection.baccaratInfo = (BaccaratInfo) data;
					});
				});
				// set the host and port
				this.clientConnection.host = tHost.getText();
				this.clientConnection.port = Integer.parseInt(tPort.getText());
				this.clientConnection.baccaratInfo.setClientHost(this.clientConnection.host);
				this.clientConnection.baccaratInfo.setClientPort(this.clientConnection.port);

				// run the sockets
				this.clientConnection.start();

				this.serverDown = false;

				this.clientConnection.baccaratInfo = new BaccaratInfo();

				// clear text fields
				tHost.clear();
				tPort.clear();

				// go to gampelay scene
				try {
					gamePlay(primaryStage);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

		});



		// create scenes hashmap
		sceneMap = new HashMap<String, Scene>();

		sceneMap.put("clientStart",  createClientStart());
		sceneMap.put("error",  createClientServerDownGui());

		// close event handler
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		
		 
		
		primaryStage.setScene(sceneMap.get("clientStart"));
		primaryStage.show();
		
	}

	// scene for server down, show error and will self quit
	public void errorStage(Stage primaryStage) throws Exception {
			this.serverDown = true;

			primaryStage.setScene(sceneMap.get("error"));
			primaryStage.setTitle("server Down");
			PauseTransition errorPause = new PauseTransition(Duration.seconds(8));
			errorPause.setOnFinished(t->{
				primaryStage.setScene(null);
				primaryStage.close();
				System.exit(0);

			});
			errorPause.play();


	}


	// function for game play
	public void gamePlay(Stage primaryStage) throws Exception {

		text.setText("");// reset text value
		sceneMap.put("clientGamePlayNoNatural", createClientGamePlayNoNaturalGui()); // initialize nonNatural scene

		// show card eventhandler
		EventHandler<ActionEvent> showCard = (e-> {
			sceneMap.replace("clientGamePlay", createClientGamePlayGui(this.show));

			primaryStage.setScene(sceneMap.get("clientGamePlay"));
			primaryStage.setTitle("Play! ");
		});

		// natural endscene eventhandler
		EventHandler<ActionEvent> endScene = (e-> {
			primaryStage.setScene(sceneMap.get("clientGamePlayEnd"));
			primaryStage.setTitle("Natural Win");
			this.clientConnection.send(this.clientConnection.baccaratInfo);


		});

		// noNatural eventhandler
		EventHandler<ActionEvent> noNaturalScene = e-> {
			primaryStage.setScene(sceneMap.get("clientGamePlayNoNatural"));
			primaryStage.setTitle("No Natural");
		};

		// noNatural endscene eventhandler
		EventHandler<ActionEvent> endScene2 = (e-> {
			if ( !sceneMap.containsKey("clientGamePlayEnd"))sceneMap.put("clientGamePlayEnd", createClientGamePlayEndGui(primaryStage));
			sceneMap.replace("clientGamePlayEnd", createClientGamePlayEndGui(primaryStage));

			primaryStage.setScene(sceneMap.get("clientGamePlayEnd"));
			primaryStage.setTitle("No Natural Win");

			this.clientConnection.send(this.clientConnection.baccaratInfo);
		});

		tCurrBet = makeIntegerTextField();// current bet 'integer' text field
		EventHandler<ActionEvent> statUp = (e-> {this.stat = 1;}); // eventhandler that updates stat to 1
		EventHandler<ActionEvent> seqUp1 = (e-> {this.sequence = 1;});// eventhandler that updates seq to 1
		EventHandler<ActionEvent> seqUp0 = (e-> {this.sequence = 0;});// eventhandler that updates seq to 1
		EventHandler<ActionEvent> showUp1 = (e-> {this.show = 1;});// eventhandler that updates show to 1
		EventHandler<ActionEvent> showUp2 = (e-> {this.show = 2;});// eventhandler that updates show to 2
		this.stat = 0;
		this.sequence = 0;

		// create start button
		startButton = makeButton("Start >>",15, white);
		startButton.setPrefSize(100,50);
		startButton.setOnAction(e->{
			if ( this.serverDown){ // if server down terminate
				System.exit(-1);

			}else if (this.clientConnection.baccaratInfo.getBid().equals("none")){ // if bid is invalid then rerun the scene
				this.text.setText("Invalid input");
				this.text.setFill(Color.rgb(186,26,26));

				sceneMap.replace("clientChoose", createClientChooseGui(primaryStage));
				primaryStage.setScene(sceneMap.get("clientChoose"));
				primaryStage.setTitle("This is a clientChoose");
			}else { // every input is valid

				// updates clients
				this.clientConnection.baccaratInfo.setCurrentBet(Double.parseDouble(tCurrBet.getText()));
				this.clientConnection.baccaratInfo.setMessage("new round");
				this.clientConnection.send(this.clientConnection.baccaratInfo);
				tCurrBet.clear();// clear currbet text field

				// update gameplay scene
				if (!sceneMap.containsKey("clientGamePlay")) sceneMap.put("clientGamePlay", createClientGamePlayGui(0));
				sceneMap.replace("clientGamePlay", createClientGamePlayGui(0));
				// if server down terminate
				if (this.serverDown) System.exit(-1);

				// go to gameplay, show first round back card
				primaryStage.setScene(sceneMap.get("clientGamePlay"));
				primaryStage.setTitle("Play! ");

				// set pause
				PauseTransition pause = new PauseTransition(Duration.seconds(0));

				//show first round front card
				pause.setOnFinished(showUp1);
				pause.play();
				pause = new PauseTransition(Duration.seconds(5));
				pause.setOnFinished(showCard);
				pause.play();

				if (this.serverDown) System.exit(-1);
				// if natural
				if (BaccaratGameLogic.handTotal(this.clientConnection.baccaratInfo.getPlayerHand()) >= 8 || BaccaratGameLogic.handTotal(this.clientConnection.baccaratInfo.getBankerHand()) >= 8) {
					// update client to server to evaluate the cards
					this.clientConnection.baccaratInfo.setMessage("evaluate");
					this.clientConnection.send(this.clientConnection.baccaratInfo);
					// go to end scene
					if (!sceneMap.containsKey("clientGamePlayEnd"))
						sceneMap.put("clientGamePlayEnd", createClientGamePlayEndGui(primaryStage));
					sceneMap.replace("clientGamePlayEnd", createClientGamePlayEndGui(primaryStage));
					pause = new PauseTransition(Duration.seconds(10));
					pause.setOnFinished(endScene);
					pause.play();
				} else { // not natural;

					// update client to server to evaluate the cards
					this.clientConnection.baccaratInfo.setMessage("evaluate");
					this.clientConnection.send(this.clientConnection.baccaratInfo);
					// show no natural message scene
					pause = new PauseTransition(Duration.seconds(9));
					pause.setOnFinished(statUp);
					pause.play();

					PauseTransition noNaturePause = new PauseTransition(Duration.seconds(10));
					noNaturePause.setOnFinished(noNaturalScene);
					noNaturePause.play();

					//show second round player back card, if new card exist
					pause = new PauseTransition(Duration.seconds(14));
					pause.setOnFinished(showUp2);
					pause.play();
					pause = new PauseTransition(Duration.seconds(14));
					pause.setOnFinished(seqUp1);
					pause.play();

					PauseTransition showPause = new PauseTransition(Duration.seconds(15));
					showPause.setOnFinished(showCard);
					showPause.play();

					//show second round player front card, if new card exist
					pause = new PauseTransition(Duration.seconds(19));
					pause.setOnFinished(statUp);
					pause.play();
					pause = new PauseTransition(Duration.seconds(19));
					pause.setOnFinished(showUp1);
					pause.play();
					PauseTransition showPause2 = new PauseTransition(Duration.seconds(20));
					showPause2.setOnFinished(showCard);
					showPause2.play();


					//show second round banker back card, if new card exist
					pause = new PauseTransition(Duration.seconds(24));
					pause.setOnFinished(seqUp0);
					pause.play();
					pause = new PauseTransition(Duration.seconds(24));
					pause.setOnFinished(statUp);
					pause.play();
					pause = new PauseTransition(Duration.seconds(24));
					pause.setOnFinished(showUp2);
					pause.play();
					showPause = new PauseTransition(Duration.seconds(25));
					showPause.setOnFinished(showCard);
					showPause.play();

					//show second round banker front card, if new card exist
					pause = new PauseTransition(Duration.seconds(29));
					pause.setOnFinished(showUp1);
					pause.play();
					showPause2 = new PauseTransition(Duration.seconds(30));
					showPause2.setOnFinished(showCard);
					showPause2.play();


					// show end scene
					PauseTransition end2 = new PauseTransition(Duration.seconds(35));
					end2.setOnFinished(endScene2);
					end2.play();

				}
			}


		});

		// go to client choose bid side and bet
		if ( !sceneMap.containsKey("clientChoose"))	sceneMap.put("clientChoose", createClientChooseGui(primaryStage));
		sceneMap.replace("clientChoose", createClientChooseGui(primaryStage));

		primaryStage.setScene(sceneMap.get("clientChoose"));
		primaryStage.setTitle("This is a clientChoose");


	}


	// scene create client start , login
	public Scene createClientStart() {
		// welcome message
		Label ltitle = makeLabel("Welcome to Baccarat!",50, Color.TRANSPARENT) ;
		ltitle.setTextFill(white);
		ltitle.setAlignment(Pos.CENTER);
		ltitle.setPadding(paddingDefault);

		// ip section
		Label lgetip = makeLabel("ip ",15, Color.TRANSPARENT) ;
		lgetip.setPrefSize(50,20);
		lgetip.setTextFill(white);
		HBox ipBox = new HBox(5, lgetip,tHost);
		hBoxDefault(ipBox);
		ipBox.setAlignment(Pos.CENTER);

		// port section
		Label lgetport = makeLabel("port ",15, Color.TRANSPARENT) ;
		lgetport.setTextFill(white);
		lgetport.setPrefSize(50,20);
		HBox portBox = new HBox(5,lgetport,tPort);
		hBoxDefault(portBox);
		portBox.setAlignment(Pos.CENTER);

		// exit button
		Button exit = makeButton("exit",15,red);
		exit.setTextFill(black);
		exit.setPrefSize(100,50);
		exit.setOnAction(e-> System.exit(0));
		HBox exitBox = new HBox(10,exit);
		exitBox.setAlignment(Pos.CENTER_LEFT);

		VBox a = new VBox( 50,ltitle,ipBox,portBox,loginButton,text,exitBox);
		a.setAlignment(Pos.CENTER);
		a.setBackground(new Background(new BackgroundFill(black,
				CornerRadii.EMPTY, Insets.EMPTY)));
		return new Scene(a, 800, 600);

	}

	// scene where client choose bid and bet
	public Scene createClientChooseGui(Stage primaryStage) {

		// bid section
		GridPane bidGrid = makeGridpaneBid(primaryStage);
		Label lgetBid = makeLabel("Pick a Bid ",15, Color.TRANSPARENT) ;
		HBox bidBox = new HBox(10, lgetBid, bidGrid);
		hBoxDefault(bidBox);
		bidBox.setBackground(new Background(new BackgroundFill(red,
				CornerRadii.EMPTY,  new Insets(-25,0,-25,0))));

		// bet section
		Label lgetBet = makeLabel("Bet amount ",15, Color.TRANSPARENT) ;
		HBox betBox = new HBox(10, lgetBet, tCurrBet);
		hBoxDefault(betBox);
		betBox.setBackground(new Background(new BackgroundFill(blue,
				CornerRadii.EMPTY,  new Insets(-25,0,-25,0))));


		// start button section
		HBox startBox = new HBox( 10, this.text,startButton);
		hBoxDefault(startBox);
		startBox.setAlignment(Pos.TOP_RIGHT);
		startBox.setPadding(new Insets(25,0,25,25));

		// exit button section
		Button exit = makeButton("exit",15,red);
		exit.setTextFill(black);
		exit.setPrefSize(100,50);
		exit.setOnAction(e-> System.exit(0));
		HBox exitBox = new HBox(10,exit);
		exitBox.setAlignment(Pos.CENTER_LEFT);

		clientBox = new VBox(50, bidBox,betBox,startBox,exitBox);
		clientBox.setAlignment(Pos.CENTER);
		clientBox.setBackground(new Background(new BackgroundFill(black,
				CornerRadii.EMPTY, Insets.EMPTY)));
		return new Scene(clientBox, 800, 600);

	}

	public Scene createClientGamePlayGui( int show) {

		// store current show
		int currShow = show;

		// player section
		// for second round showing if it's banker show turn make sure player card is all shown
		if ( this.sequence == 0 && this.stat == 1) {
			show = 1;
		}
		GridPane playerGrid = makeGridpaneCard(1, show);
		Label lplayerCards = makeLabel("  Player ",15, Color.TRANSPARENT) ;
		lplayerCards.setTextFill(white);
		lplayerCards.setPrefSize(100,20);
		HBox playerCardBox= new HBox(10, lplayerCards, playerGrid);
		playerCardBox.setBackground(new Background(new BackgroundFill(green,
				CornerRadii.EMPTY, new Insets(-25,0,-25,0))));

		// banker section
		show = currShow;
		// for second round showing if it's player show turn make sure banker new card is hidden
		if ( this.sequence == 1) {
			show = 1;
			this.stat = 0;
		}
		GridPane bankerGrid = makeGridpaneCard(2, show);
		Label lbankerCards = makeLabel("  Banker ",15, Color.TRANSPARENT) ;
		lbankerCards.setTextFill(white);
		lbankerCards.setPrefSize(100,20);

		HBox bankerCardBox = new HBox( 10, lbankerCards,bankerGrid);
		bankerCardBox.setBackground(new Background(new BackgroundFill(red,
				CornerRadii.EMPTY,  new Insets(-25,0,-25,0))));

		// exit section
		Button exit = makeButton("exit",15,red);
		exit.setTextFill(black);
		exit.setPrefSize(100,50);
		exit.setOnAction(e-> System.exit(0));
		HBox exitBox = new HBox(10,exit);
		exitBox.setAlignment(Pos.CENTER_LEFT);

		clientBox = new VBox(50, playerCardBox,bankerCardBox,exitBox);
		clientBox.setAlignment(Pos.CENTER);
		clientBox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
				CornerRadii.EMPTY, Insets.EMPTY)));

		return new Scene(clientBox, 800, 600, black);

	}

	// no natural message scene
	public Scene createClientGamePlayNoNaturalGui() {
		Label lmessage1 = makeLabel("No Natural Win ",15,Color.TRANSPARENT);
		Label lmessage2 = makeLabel("Drawing New Cards ",15,Color.TRANSPARENT);


		// exit button section
		Button exit = makeButton("exit",15,red);
		exit.setTextFill(black);
		exit.setPrefSize(100,50);
		exit.setOnAction(e-> System.exit(0));
		HBox exitBox = new HBox(10,exit);
		exitBox.setAlignment(Pos.CENTER_LEFT);
		clientBox = new VBox(30, lmessage1,lmessage2,exitBox);
		clientBox.setAlignment(Pos.CENTER);
		clientBox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
				CornerRadii.EMPTY, Insets.EMPTY)));

		return new Scene(clientBox, 800, 600, black);

	}

	// end game scene
	public Scene createClientGamePlayEndGui(Stage primaryStage) {
		// header of player and banker total
		Label lplayerTotal = makeLabel("   Player Total : "+BaccaratGameLogic.handTotal(this.clientConnection.baccaratInfo.getPlayerHand()),15,Color.TRANSPARENT);
		Label lbankerTotal = makeLabel("   Banker Total : "+BaccaratGameLogic.handTotal(this.clientConnection.baccaratInfo.getBankerHand()),15,Color.TRANSPARENT);
		HBox header= new HBox(25, lplayerTotal,lbankerTotal);
		hBoxDefault(header);

		// get the winner text and adjust the winning message according to client situation
		String winnerText = BaccaratGameLogic.whoWon(this.clientConnection.baccaratInfo.getPlayerHand(),this.clientConnection.baccaratInfo.getBankerHand());
		String clientMessageText;
		if (winnerText.equals(this.clientConnection.baccaratInfo.getBid())) {

			clientMessageText = "Congrats, you bet on " +this.clientConnection.baccaratInfo.getBid() +" you win!";
		} else if (winnerText.equals("Draw")) {

			clientMessageText = "You get your money back, you bet on " + this.clientConnection.baccaratInfo.getBid() + " it's a draw";
		} else {

			clientMessageText = "Sorry, you bet on " + this.clientConnection.baccaratInfo.getBid() + " you lose..";
		}

		if(!winnerText.equals("Draw")) winnerText += " Wins !";
		else  winnerText = "It's a Draw!";

		// winner, client message, and total win of current round
		Label lwinner = makeLabel(winnerText,15, Color.TRANSPARENT);
		Label lclientMessage = makeLabel(clientMessageText,15, Color.TRANSPARENT);
		Label ltotalWin = makeLabel("Total Win : $"+ Math.round(this.clientConnection.baccaratInfo.getTotalWinnings()*100.0)/100.0,15, Color.TRANSPARENT);

		// rebid button , will reset necessar variable and create a fresh start
		Button rebid = makeButton("rebid",15,green);
		rebid.setOnAction(e-> {
			this.totalWin = 0;
			this.text.setText("");
			this.show = 0;
			this.stat = 0;
			this.sequence = 0;

			this.clientConnection.baccaratInfo.setMessage("rebid");
			this.clientConnection.send(this.clientConnection.baccaratInfo);
			this.clientConnection.baccaratInfo.setBid("none");

			try {
				gamePlay(primaryStage);
			} catch (Exception exception) {
				exception.printStackTrace();
			}

		});

		// exit button
		Button exit = makeButton("exit",15,red);
		exit.setOnAction(e-> {
			try {
				primaryStage.setScene(null);
				primaryStage.close();
				primaryStage.getOnCloseRequest();
			} catch (Exception exception) {
				exception.printStackTrace();
			}

		});
		HBox buttons= new HBox(25, rebid,exit);
		hBoxDefault(buttons);
		buttons.setAlignment(Pos.BOTTOM_RIGHT);

		clientBox = new VBox(40, header,lwinner,lclientMessage,ltotalWin,buttons);
		clientBox.setAlignment(Pos.CENTER);
		clientBox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
				CornerRadii.EMPTY, Insets.EMPTY)));

		return new Scene(clientBox, 800, 600, black);

	}

	// server down message scene
	public Scene createClientServerDownGui() {
		Label lmessage1 = makeLabel("Server Down :( ",15,Color.TRANSPARENT);
		Label lmessage2 = makeLabel("please try again later \nquitting... ",15,Color.TRANSPARENT);


		// exit button section
		Button exit = makeButton("exit",15,red);
		exit.setTextFill(black);
		exit.setPrefSize(100,50);
		exit.setOnAction(e-> System.exit(0));
		HBox exitBox = new HBox(10,exit);
		exitBox.setAlignment(Pos.CENTER_LEFT);
		clientBox = new VBox(25, lmessage1,lmessage2,exitBox);
		clientBox.setAlignment(Pos.CENTER);
		clientBox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
				CornerRadii.EMPTY, Insets.EMPTY)));

		return new Scene(clientBox, 800, 600, black);

	}


	// color palette
	Paint white = Color.rgb(239, 239, 239);
	Paint black = Color.rgb(46,46,46);
	Paint blue = Color.rgb(20, 40, 85);
	Paint red = Color.rgb(135, 33, 33);
	Paint green = Color.rgb(13, 91, 8);
	Paint hover = Color.rgb(168,168,168,0.7);
	// default padding
	Insets paddingDefault = new Insets(25,25,0,25);

	// function to make button
	Button makeButton (String string, int fSize, Paint paint){
		Button button = new Button(string);
		button.setAlignment(Pos.CENTER);
		button.setTextFill(black);
		button.setFont(Font.font("Helvetica", FontWeight.BOLD,fSize));
		button.setBackground(new Background(new BackgroundFill(paint,
				CornerRadii.EMPTY, Insets.EMPTY)));
		button.setMinSize(40,40);
		button.setPrefSize(200,50);

		// for hover
		button.setOnMouseEntered(e->{
			button.setBackground(new Background(new BackgroundFill(hover,
					CornerRadii.EMPTY, Insets.EMPTY)));
		});
		button.setOnMouseExited(e->{
			button.setBackground(new Background(new BackgroundFill(paint,
					CornerRadii.EMPTY, Insets.EMPTY)));
		});
		return button;
	}

	//create text field that accepts only integer
	TextField makeIntegerTextField(){
		TextField finalTextField = new TextField();
		finalTextField.setPromptText("type number only");
		finalTextField.setOnKeyReleased(t->{
			try {
				int num = Integer.parseInt(finalTextField.getText());
				if ( num < 1) finalTextField.clear();
			} catch (NumberFormatException nfe) {
				if (finalTextField.getText().length() <= 1) finalTextField.clear();
				else finalTextField.deletePreviousChar();
			}
		});
		return finalTextField;
	}


	// function to make label
	Label makeLabel (String string, int fSize, Paint paint){
		Label label = new Label(string);
		label.setAlignment(Pos.CENTER_LEFT);
		label.setTextFill(white);
		label.setFont(Font.font("Helvetica", FontWeight.BOLD,fSize));
		label.setBackground(new Background(new BackgroundFill(paint,
				CornerRadii.EMPTY, Insets.EMPTY)));
		label.setPrefSize(575,60);
		return label;
	}

	// function to utilize hbox aligment and padding
	void hBoxDefault (HBox hBox){
		hBox.setPadding(paddingDefault);
		hBox.setAlignment(Pos.TOP_LEFT);
	}

	// function to create gridpane for bid
	GridPane makeGridpaneBid(Stage primaryStage){
		GridPane gridPane = new GridPane();
		gridPane.setHgap(100);
		gridPane.setVgap(10);
		ArrayList <String> bidType = new ArrayList<>();
		bidType.add("Player");
		bidType.add("Banker");
		bidType.add("Draw");
		for ( int i=0; i<3; i++) {
			Button button = makeButton(bidType.get(i), 15, white);
			String bid = bidType.get(i);
			button.setOnAction(t -> {
					this.clientConnection.baccaratInfo.setBid(bid);

					button.setBackground(new Background(new BackgroundFill(green,
							CornerRadii.EMPTY, Insets.EMPTY)));
					sceneMap.replace("clientChoose", createClientChooseGui(primaryStage));
					primaryStage.setScene(sceneMap.get("clientChoose"));
					primaryStage.setTitle("This is a clientChoose");

			});


			if (bidType.contains(this.clientConnection.baccaratInfo.getBid())) {
				if (bid.equals(this.clientConnection.baccaratInfo.getBid())) {
					button.setDisable(false);
					button.setBackground(new Background(new BackgroundFill(green,
							CornerRadii.EMPTY, Insets.EMPTY)));
					button.setOnAction(t -> {
						this.clientConnection.baccaratInfo.setBid("none");
						button.setBackground(new Background(new BackgroundFill(white,
								CornerRadii.EMPTY, Insets.EMPTY)));
						sceneMap.replace("clientChoose", createClientChooseGui(primaryStage));
						primaryStage.setScene(sceneMap.get("clientChoose"));
						primaryStage.setTitle("This is a clientChoose");
					});
				} else button.setDisable(true);
			}

				GridPane.setRowIndex(button, 0);
				GridPane.setColumnIndex(button, i);
				gridPane.getChildren().addAll(button);


		}
		return gridPane;
	}

	// function to create gridpane for card showing
	GridPane makeGridpaneCard(int which, int show){
		GridPane gridPane = new GridPane();
		gridPane.setHgap(100);
		gridPane.setVgap(10);

		// get current hand cards
		ArrayList<Card> cards = new ArrayList<>();
		if (which == 1) cards = this.clientConnection.baccaratInfo.getPlayerHand();
		else if (which == 2){
			cards = this.clientConnection.baccaratInfo.getBankerHand();
		}

		int length = cards.size();
		if ( show == 0){ // if show 0 then it's the first showing where only 2 cards
			length = 2;
		}
		if (this.stat == 0 && cards.size() >= 3) length = 2; // if stat 0 then it's the second showing where only 2 cards showing

		for ( int i=0; i<length; i++) {

			Image cardImage = null;
			if ( show == 0) { // if 0 then hide all cards
				try {
					cardImage = new Image(new FileInputStream(System.getProperty("user.dir")
							+ "/Src/ImgSrc/CardBack.jpg"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}else {
				String suite = cards.get(i).suite;
				String value = String.valueOf(cards.get(i).value);

				if (show == 1) {// if 0 then show all cards
					try {
						cardImage = new Image(new FileInputStream(System.getProperty("user.dir")
								+ "/Src/ImgSrc/" + suite + "s/" + value + suite + ".png"));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				} else if (show == 2) {// if 2 then hide the last card
					if (i == 2) {
						try {
							cardImage = new Image(new FileInputStream(System.getProperty("user.dir")
									+ "/Src/ImgSrc/CardBack.jpg"));
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}

					} else {
						try {
							cardImage = new Image(new FileInputStream(System.getProperty("user.dir")
									+ "/Src/ImgSrc/" + suite + "s/" + value + suite + ".png"));
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}

				}
			}




			ImageView cardImageView = new ImageView(cardImage);

			cardImageView.setFitHeight(217/1.5);
			cardImageView.setFitWidth(153/1.5);

			cardImageView.setPreserveRatio(true);


			GridPane.setRowIndex(cardImageView, 0);
			GridPane.setColumnIndex(cardImageView, i);
			gridPane.getChildren().addAll(cardImageView);


		}
		gridPane.setAlignment(Pos.CENTER);
		return  gridPane;
	}



}
