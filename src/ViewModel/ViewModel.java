package ViewModel;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import Model.Model;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ViewModel extends Observable implements Observer  {
	
	Model m;
	public StringProperty ip,port,path,script;

	
	public ViewModel(Model m) {
		this.m=m;
		port=new SimpleStringProperty();
		ip=new SimpleStringProperty();
		path= new SimpleStringProperty();
		script=new SimpleStringProperty();
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
		else // Calc path
			path.set(Arrays.toString(m.getPath()));
	
	}

}
