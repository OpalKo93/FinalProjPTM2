package Model;

import java.io.File;

public interface Model {

	public void connect(String ip,int port);
	public void calcPath(File f);
	public String[] getPath();
	public void interpret(String[] lines);
}
