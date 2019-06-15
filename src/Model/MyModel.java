package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import ServerClient.Client;
import algorithms.BestFirstSearch;
import algorithms.MatrixProblem;
import interpreter.MyInterpreter;
import server_side.FileCacheManager;
import server_side.MySerialServer;
import server_side.SearchableClientHandler;
import server_side.SearcherSolver;
import algorithms.Position;

public class MyModel extends Observable implements Model  {

	
	String path;
	Client c;
	MyInterpreter interpreter;
	Boolean connectStatus;
	MySerialServer mapServer;
	String mapServerIp;
	int mapServerPort;
	
	public MyModel(MyInterpreter interpreter,Client c) {
		this.c=c;
		this.interpreter=interpreter;
		connectStatus=false;
	}
	@Override
	public void connect(String ip, int port) {
		c.connect(ip, port);
		connectStatus=true;
		System.out.println("Connected to the server");
		setChanged();
		notifyObservers("Connected");
	}

	@Override
	public String getPath() {
		return path;
		
	}

	@Override
	public void interpret(String[] lines) {
		new Thread(()->{
			interpreter.interpret(lines);
			connectStatus=true;
			setChanged();
			notifyObservers("Interpreted");
		}).start();;
	}

	@Override
	public void calcPath(File f) {
		
		setChanged();
		notifyObservers("Calculated");
	}
	@Override
	public void setThrottle(double d) {
		if(connectStatus)
			c.setPathValue("controls/engines/current-engine/throttle", d);
		else
		{
			this.connect("127.0.0.1", 5402);
			c.setPathValue("controls/engines/current-engine/throttle", d);
		}
	}
	@Override
	public void setRudder(double d) {
		if(connectStatus)
			c.setPathValue("/controls/flight/rudder", d);
		else
		{
			this.connect("127.0.0.1", 5402);
			c.setPathValue("/controls/flight/rudder", d);
		}
	}
	@Override
	public void setAileron(double d) {
		if(connectStatus) {
			c.setPathValue("/controls/flight/aileron", d);
			System.out.println(d);
		}
		else
		{
			this.connect("127.0.0.1", 5402);
			c.setPathValue("/controls/flight/aileron", d);
			System.out.println(d);
		}
		
	}
	@Override
	public void setElvator(double d) {
		if(connectStatus) {
			c.setPathValue("/controls/flight/elevator", d);
			System.out.println(d);
		}
		else
		{
			this.connect("127.0.0.1", 5402);
			c.setPathValue("/controls/flight/elevator", d);
			System.out.println(d);
		}
		
	}
	
	@Override
	public void connectToMapServer(String ip, int port, String mapToText, int planeX, int planeY, int destX,
			int destY) {
		
		mapServerIp = ip;
		mapServerPort = (int)port;
		
		mapServer = new MySerialServer(6420); //PORT 6420 is the port to connect MapSolver 

		new Thread(()-> {
			try {
				SearchableClientHandler<String, Position> ch;
				ch = new SearchableClientHandler<>(
						new SearcherSolver<MatrixProblem, String, Position>(new BestFirstSearch<Position>()),
						new FileCacheManager<MatrixProblem, String>("./maze.xml")
				);
				
				mapServer.start(ch, "end"); // running the server
			} catch (Exception e) {
				System.out.println("Cannot login to map Server");
			}
			
		}).start();
		
		
		calculateMap(mapToText, planeX, planeY, destX, destY);
	}

	
	public void calculateMap(String mapToText, int planeX, int planeY, int destX, int destY) {
		
		try {
			Socket s = new Socket(mapServerIp, mapServerPort);
			
			s.setSoTimeout(3000);
			PrintWriter out=new PrintWriter(s.getOutputStream());
			BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
			
			String[] lines = mapToText.split("\n");
			
			for(int i = 0; i < lines.length; i++) {
				out.println(lines[i].trim());
				out.flush();
			}
			out.println("end");
			out.flush();
			
			out.println(planeX + "," + planeY);
			out.flush();
			out.println(destX + "," + destY);
			out.flush();
			
			path = in.readLine();
			System.out.println(path);
			setChanged();
			notifyObservers("calculate Map");
			
			out.close();
			in.close();
			s.close();
			
			
		} catch (UnknownHostException e) {

		} catch (IOException e) {
		}
		
	}
}
	

