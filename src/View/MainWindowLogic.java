package View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import com.sun.glass.ui.Window;

import ViewModel.ViewModel;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainWindowLogic implements Observer{
	
	ViewModel vm;
	@FXML
	TextArea script;
	@FXML
	Button CalcButton;
	@FXML
	AnchorPane ManualPane,HomePane,MapPane,AutoPilotPane;
	
	
	
	public void setViewModel(ViewModel vm) {
		this.vm=vm;
		vm.script.bind(script.textProperty());
		
		//script.textProperty().bind(vm.script);
		//calc path needed
		
	}
	public void HomeShower() {
		HomePane.setVisible(false);
		MapPane.setVisible(false);
		ManualPane.setVisible(false);
		AutoPilotPane.setVisible(false);
	}
	
	
	public void CalcShower() {
		HomePane.setVisible(true);
		MapPane.setVisible(false);
		ManualPane.setVisible(false);
		AutoPilotPane.setVisible(false);
	}
	
	public void ManualShower() {
		HomePane.setVisible(false);
		MapPane.setVisible(false);
		ManualPane.setVisible(true);
		AutoPilotPane.setVisible(false);
	}
	public void AutoPilotShower() {
		HomePane.setVisible(false);
		MapPane.setVisible(false);
		ManualPane.setVisible(false);
		AutoPilotPane.setVisible(true);
	}
	
	public void PopupHandler() throws IOException {
	
		FXMLLoader fxl = new FXMLLoader(getClass().getResource("PopUpWindow.fxml"));
		AnchorPane root = (AnchorPane)fxl.load();
		Scene scene = new Scene(root,400,400);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setScene(scene);
		primaryStage.show();
	
		PopUpLogic pul =fxl.getController(); //view
		pul.setViewModel(vm);
		vm.addObserver(pul);
		
		
	}
	
	
	
	
public void loadScript() {
		
		FileChooser fc = new FileChooser();
		Stage primaryStage = new Stage();
		fc.setTitle("open Script");
		fc.setInitialDirectory(new File("./resources"));
		File chosen = fc.showOpenDialog(null);
		
		showScript(chosen);
		
		
		
	}
	
	
	public void loadMap() {
		
		FileChooser fc = new FileChooser();
		Stage primaryStage = new Stage();
		primaryStage.setTitle("file");
		fc.setTitle("open CSV file");
		fc.setInitialDirectory(new File("./resources"));
		File chosen = fc.showOpenDialog(primaryStage);
		primaryStage.show();
		
	}
	
	public void showScript(File f) {
		script.setWrapText(true);
		try {
		
			Scanner s = new Scanner(f);
			while(s.hasNextLine()) {
				script.appendText(s.nextLine()+"\n");
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void interpret() {
		vm.interpret();
	}


	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}