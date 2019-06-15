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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
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
	@FXML
	Slider ThrottleSlider,RudderSlider;
	@FXML
	Circle SmallCircle,BigCircle;
	@FXML
	MapLogic mapLogic;
	
	DoubleProperty aileron,elvator;
	
	IntegerProperty planePostionX,planePostionY,markPostionX,markPostionY;
	
	StringProperty path;
	
	public MainWindowLogic() {
		aileron=new SimpleDoubleProperty();
		elvator=new SimpleDoubleProperty();
		planePostionX=new SimpleIntegerProperty();
		planePostionY=new SimpleIntegerProperty();
		markPostionX=new SimpleIntegerProperty();
		markPostionY=new SimpleIntegerProperty();
	}
	public void setViewModel(ViewModel vm) {
		this.vm=vm;
		vm.script.bind(script.textProperty());
		
		vm.throttle.bind(ThrottleSlider.valueProperty());
		vm.rudder.bind(RudderSlider.valueProperty());
		vm.aileron.bind(aileron);
		vm.elvator.bind(elvator);
		vm.planePostionX.bind(planePostionX);
		vm.planePostionY.bind(planePostionY);
		vm.markPostionX.bind(markPostionX);
		vm.markPostionY.bind(markPostionY);
		path.bind(vm.path);

	}
	public void setMapLogic(MapLogic ml) {
		mapLogic=ml;
	}
	
	public void HomeShower() {
		HomePane.setVisible(false);
		MapPane.setVisible(false);
		ManualPane.setVisible(false);
		AutoPilotPane.setVisible(false);
	}
	
	
	public void CalcShower() {
		HomePane.setVisible(true);
		MapPane.setVisible(true);
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
	
public void MarkMap(MouseEvent e) { //mark the X on the Map
		
		markPostionX.set((int)e.getX());
		markPostionY.set((int)e.getY());
		//System.out.println(markPostionX.get()+""+markPostionY.get());
		
		mapLogic.drawMap(planePostionX.get(), planePostionY.get(), markPostionX.get(), markPostionY.get());
		
		markPostionX.set((int)((float)markPostionX.get()/451*16));
		markPostionY.set((int)((float)markPostionY.get()/383*14));
		
	}
	
	
	public void MoveJoyStick() {
	
		SmallCircle.setOnMouseDragged((MouseEvent e)->{
			
			if(e.getX()<(BigCircle.getRadius()-SmallCircle.getRadius())
			&& e.getX()> -(BigCircle.getRadius()-SmallCircle.getRadius())
			&& e.getY()<(BigCircle.getRadius()-SmallCircle.getRadius())
			&& e.getY()> -(BigCircle.getRadius()-SmallCircle.getRadius())
			 ){
				aileron.set(e.getX()/BigCircle.getRadius());
				elvator.set(e.getY()/BigCircle.getRadius());

				SmallCircle.setCenterX(e.getX());
				SmallCircle.setCenterY(e.getY());
				vm.moveAileron();
				vm.moveElvator();
				}
			});
		
		SmallCircle.setOnMouseReleased((MouseEvent e)->{
			SmallCircle.setCenterX(0);
			SmallCircle.setCenterY(0);
		});
	}
	
	public void MoveThrottleSlider() {
		vm.MoveThrottleSlider();
	}
	public void MoveRudderSlider() {
		vm.MoveRudderSlider();
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
	public void PopupMapHandler() throws IOException {
		
		FXMLLoader fxl = new FXMLLoader(getClass().getResource("MapPopUp.fxml"));
		AnchorPane root = (AnchorPane)fxl.load();
		Scene scene = new Scene(root,400,400);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage primaryStage = new Stage();
		primaryStage.setScene(scene);
		primaryStage.show();
	
		MapPopUpLogic mpul =fxl.getController(); //view
		mpul.setViewModel(vm);
		vm.addObserver(this);
		
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
		primaryStage.setTitle("open Map");
		fc.setInitialDirectory(new File("./resources"));
		File chosen = fc.showOpenDialog(null);
		
		mapLogic.setMapLogic(vm.ConvertFileToArray(chosen));
		path=new SimpleStringProperty();
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
			if(o==vm) {
				System.out.println("hello");
				mapLogic.drawPath(arg.toString());
			}

	}

}