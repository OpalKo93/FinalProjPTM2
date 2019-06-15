package Model;

import java.io.File;

public interface Model {

	public void connect(String ip,int port);
	public void calcPath(File f);
	public String getPath();
	public void interpret(String[] lines);
	public void setThrottle(double d);
	public void setRudder(double d);
	public void setAileron(double d);
	public void setElvator(double d);
	public void connectToMapServer(String ip, int port, String mapCor, int planeX, int planeY, int destX,
			int destY);
}
