package ViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javax.net.ssl.SSLContext;

import Model.Model;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ViewModel extends Observable implements Observer  {
	
	Model m;
	public StringProperty ip,port,path,script,MapIp,MapPort;
	public DoubleProperty throttle,rudder,aileron,elvator;
	public IntegerProperty planePostionX,planePostionY,markPostionX,markPostionY;
	public String mapToText;
	int sizeOfMapX,sizeOfMapY;
	
	public ViewModel(Model m) {
		this.m=m;
		MapPort=new SimpleStringProperty();
		MapIp=new SimpleStringProperty();
		port=new SimpleStringProperty();
		ip=new SimpleStringProperty();
		path= new SimpleStringProperty();
		script=new SimpleStringProperty();
		throttle=new SimpleDoubleProperty();
		rudder=new SimpleDoubleProperty();
		aileron=new SimpleDoubleProperty();
		elvator=new SimpleDoubleProperty();
		planePostionX=new SimpleIntegerProperty();
		planePostionY=new SimpleIntegerProperty();
		markPostionX=new SimpleIntegerProperty();
		markPostionY=new SimpleIntegerProperty();
		sizeOfMapX=14;
		sizeOfMapY=16;
	}
	
	public void interpret() {
		m.interpret(script.get().split("\n"));
	}
	
	public void connect() {
		m.connect(ip.get(),Integer.parseInt(port.get()));
	}
	@Override
	
	public void update(Observable o, Object arg) {
		if(arg.equals("Connected"))
			System.out.println("Connected");
		else if(arg.equals("Interpreted"))
			System.out.println("Interpreted");
		else if(arg.equals("calculate Map")) {
			path.set(m.getPath());
			setChanged();
			//System.out.println(this.countObservers());
			//notifyObservers("Done calc the map");
			notifyObservers(path.get());
		}
	
	}
	
	public void MoveThrottleSlider() {
		m.setThrottle(throttle.get());
	}
	public void MoveRudderSlider() {
		m.setRudder(rudder.get());
	}

	public void moveAileron() {
		m.setAileron(aileron.get());
	}

	public void moveElvator() {
		m.setElvator(elvator.get());
	}
	
	public int[][] ConvertFileToArray(File f) {
		int[][] map= new int[sizeOfMapX][sizeOfMapY];
		int i=0;
		int j=0;
		String[] temp;
		try {
			Scanner s=new Scanner(f);
			mapToText =(s.useDelimiter("\\A").next().trim());
			
			s=new Scanner(f);
			while(s.hasNextLine()) {
				temp=s.nextLine().split(",");
				for (String string : temp) {
					map[i][j] = Integer.parseInt(string);
					j++;
				}
				i++;
				j=0;
			}
			return map;
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
		
	}


	public void connectToMapServer() {
		m.connectToMapServer(MapIp.get(),Integer.parseInt(MapPort.get()),mapToText,
				planePostionX.get(),planePostionY.get(),markPostionY.get(),markPostionX.get()
			    );
	}

	
}
