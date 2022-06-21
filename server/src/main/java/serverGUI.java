import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.HashMap;

public class serverGUI extends Application{

	// initialize variables
	TextField fserverPort;
	Label lTotalClient,lserverPort,lstartTitle;
	Button serverPort, serverOnOff;
	HashMap<String, Scene> sceneMap;

	Server serverConnection;
	Server oldserver;

	ListView<String> listItems;
	VBox vbox;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Server");


		// label for total active clients
		lTotalClient = makeLabel("total active Client : 0",15, Color.TRANSPARENT);

		// label for start title
		this.lstartTitle = makeLabel("Baccarat Server",20, Color.TRANSPARENT);
		this.lstartTitle.setTextFill(white);
		this.lstartTitle.setPrefSize( 200,25);
		this.lstartTitle.setAlignment(Pos.CENTER);

		// port label
		this.lserverPort = makeLabel("port number : ",15, Color.TRANSPARENT);
		this.lserverPort.setTextFill(white);
		this.lserverPort.setPrefSize( 120,20);
		this.lserverPort.setAlignment(Pos.CENTER);

		// port 'integer' text field
		this.fserverPort = makeIntegerTextField();
		this.fserverPort.setAlignment(Pos.CENTER);
		this.serverOnOff = makeButton( "Turn OFF server", 15, white);

		// port submit button
		this.serverPort = makeButton("enter",15,white);
		this.serverPort.setFont(Font.font("Helvetica", FontWeight.BOLD,15));
		this.serverPort.setAlignment(Pos.CENTER);
		this.serverPort.setPrefSize(100,20);

		this.serverPort.setOnAction(e->{
			primaryStage.setScene(sceneMap.get("server")); // go to server of port scene
			primaryStage.setTitle("Baccarat Server");

			// established new server
			this.serverConnection = new Server(data -> {
				Platform.runLater(()->{ // use to update the list view and total clients
					String message = data.toString();
					if (message.contains("total active client")) {
						lTotalClient = makeLabel(message,15, Color.TRANSPARENT);
						sceneMap.replace("server",  createServerGui(primaryStage));
						primaryStage.setScene(sceneMap.get("server"));
						primaryStage.setTitle("This is the Server");
					}else listItems.getItems().add(message);
				});

			});

			// button turn on or off server
			this.serverOnOff.setOnAction( t->{
					if (!this.serverConnection.on) {
						this.serverConnection = oldserver;
						this.serverConnection.on = true;
						this.serverOnOff.setText("Turn OFF server");

					}else {
						this.oldserver = this.serverConnection;
						this.serverConnection.on = false;
						this.serverOnOff.setText("Turn ON server");
					}

					sceneMap.replace("server",  createServerGui(primaryStage));
					primaryStage.setScene(sceneMap.get("server"));
					primaryStage.setTitle("This is the Server");
				});
			this.serverConnection.port = Integer.parseInt(fserverPort.getText()); // get port number
			fserverPort.clear();// clear port text field
		});

		listItems = new ListView<String>(); // declare list view

		sceneMap = new HashMap<String, Scene>();//hashmap of scenes
		
		sceneMap.put("server",  createServerGui(primaryStage));
		sceneMap.put("serverStart",  createServerStart());

		// close event handler
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		primaryStage.setScene(sceneMap.get("serverStart"));
		primaryStage.show();
		
	}


	// scene to get port number ( port textfield, enter button)
	public Scene createServerStart() {

		HBox hbox = new HBox( 10 , lserverPort,fserverPort);
		hBoxDefault(hbox);
		hbox.setAlignment(Pos.CENTER);


		HBox hbox2 = new HBox( 10 , serverPort);
		hBoxDefault(hbox2);
		hbox2.setAlignment(Pos.CENTER);

		vbox = new VBox(10 ,lstartTitle,hbox,hbox2);
		vbox.setAlignment(Pos.CENTER);
		vbox.setBackground(new Background(new BackgroundFill(black,
				CornerRadii.EMPTY, Insets.EMPTY)));

		return new Scene(vbox, 800, 600);


	}

	// main server scene ( list view, total client)
	public Scene createServerGui(Stage primaryStage) {


		lTotalClient.setPadding(paddingDefault);

		BorderPane pane = new BorderPane();
		pane.setBackground(new Background(new BackgroundFill(black,
				CornerRadii.EMPTY,Insets.EMPTY)));
		pane.setPadding(new Insets(70));
		pane.setCenter(listItems);

		vbox = new VBox( 10 , lTotalClient,pane,serverOnOff);
		vbox.setBackground(new Background(new BackgroundFill(black,
				CornerRadii.EMPTY,Insets.EMPTY)));
		return new Scene(vbox, 800, 600);
		
		
	}


	// color palette
	Paint white = Color.rgb(239, 239, 239);
	Paint black = Color.rgb(46,46,46);
	Insets paddingDefault = new Insets(25,25,0,25);

	// function to create button
	Button makeButton (String string, int fSize, Paint paint){
		Button button = new Button(string);
		button.setAlignment(Pos.CENTER);
		button.setTextFill(black);
		button.setFont(Font.font("Helvetica", fSize));
		button.setBackground(new Background(new BackgroundFill(paint,
				CornerRadii.EMPTY, Insets.EMPTY)));
		button.setMinSize(40,40);
		button.setPrefSize(200,50);
		return button;
	}

	// function to create 'integer' text field
	TextField makeIntegerTextField(){
		TextField finalTextField = new TextField();
		finalTextField.setPromptText("type number only");
		finalTextField.setOnKeyReleased(t->{
			try {
				int num = Integer.parseInt(finalTextField.getText());
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





}
