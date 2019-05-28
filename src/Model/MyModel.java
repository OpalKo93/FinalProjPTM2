package Model;

import java.io.File;
import java.util.Observable;

import ServerClient.Client;
import interpreter.MyInterpreter;

public class MyModel extends Observable implements Model  {

	
	String[] path;
	Client c;
	MyInterpreter interpreter;
	
	public MyModel(MyInterpreter interpreter,Client c) {
		this.c=c;
		this.interpreter=interpreter;
	}
	@Override
	public void connect(String ip, int port) {
		c.connect(ip, port);
		System.out.println("Connected to the server");
		setChanged();
		notifyObservers("Connected");
	}

	@Override
	public String[] getPath() {
		return path;
		
	}

	@Override
	public void interpret(String[] lines) {
		
		interpreter.interpret(lines);
		setChanged();
		notifyObservers("Interpreted");
	}

	@Override
	public void calcPath(File f) {
		
		setChanged();
		notifyObservers("Calculated");
		//Hello
	}

}
