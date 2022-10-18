package client;
 
import javafx.concurrent.Task;

import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import shared.ClientInt;
import com.sun.prism.paint.Color;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class ClientFXGUI extends Application {
	ClientFXGUI guiReference;
	ClientInt client;
	String ID;
	String IP;
	Scene scene;
	Text scenetitle;
	Text actiontarget;
	Label serverIP,userName;
	Button ipButton,rButton,lButton;
	TextField ipTextField,userTextField;
	HBox hbBtn1,hbBtn2;
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Client GUI");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        serverIP = new Label("Server IP:");
        grid.add(serverIP, 0, 1);
        
        ipTextField = new TextField();
        grid.add(ipTextField, 1, 1);
        
        ipButton = new Button("Insert");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(ipButton);
        grid.add(ipButton, 0, 2);
        actiontarget = new Text();
        grid.add(actiontarget, 0,6,2,1);
        ipButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent e) {
        	        rButton.setDisable(true);
				lButton.setDisable(true);
				IP = ipTextField.getText();
				//client = new Client(guiReference ,IP);
				rButton.setDisable(false);
				lButton.setDisable(false);
                actiontarget.setText("IP Inserted");}
        });
        userName = new Label("User Name:");
        grid.add(userName, 0, 3);

        userTextField = new TextField();
        grid.add(userTextField, 1, 3);
       
        rButton = new Button("Register");
        hbBtn1 = new HBox(10);
        hbBtn1.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn1.getChildren().add(rButton);
        grid.add(rButton, 0, 4);
        
        lButton = new Button("Login");
        hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().add(lButton);
        grid.add(lButton, 1, 4);
        
        rButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                actiontarget.setText("Registered");
            }
        });
        
        lButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                actiontarget.setText("Logged in");
            }
        });
        
        /*Task task = new Task<Void>() {
            @Override public Void call() throws InterruptedException {
            	  
            	
               return null;
            }   
        };
        ProgressBar bar = new ProgressBar();
        grid.add(bar, 1, 6, 1, 3);
        bar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
        */
  }
}