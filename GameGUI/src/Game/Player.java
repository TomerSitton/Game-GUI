package Game;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Player extends Sprite2 {

	// client's socket and I/O streams
	private Socket socket;
	private BufferedReader inputStreamFromServer;
	private DataOutputStream outputStreamToServer;

	public static final String URL = "img/sprite-boy-running.png";
	public static final int ROWS = 2;
	public static final int COLUMNS = 8;
	public static final int WIDTH = 110;
	public static final int HEIGHT = 150;
	public static final int SPEED_X = 15;
	public static final int SPEED_Y = 10;

	public Player(int x, int y) {
		super(x, y, URL, ROWS, COLUMNS, WIDTH, HEIGHT, SPEED_X, SPEED_Y);
		initSocketStreams();
	}

	/**
	 * initialize the client's sockets and I/O streams
	 */
	private void initSocketStreams() {
		try {
			socket = new Socket(Network.SERVER_IP, Network.SERVER_PORT);
			inputStreamFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputStreamToServer = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * send data to the server. </br>
	 * In the game, the data sent to the the server is represented like this:
	 * </br>
	 * </br>
	 * "(newX,newY)\n" </br>
	 * </br>
	 * so if for example my character moved from (0,0) to (10,12), the data to
	 * be sent to the server will look like this:</br>
	 * "(10,12)\n"
	 * 
	 * @param data
	 *            - the data to be sent to the server
	 */
	public void sendData(String data) {
		Network.sendDataToServer(this.outputStreamToServer, data);
	}

	/**
	 * Receive data from the server.</br>
	 * The data should look like this: "(newX1,newY1) : (newX2,newY2) :
	 * (newX3,newY3) : (newX4,newY4)\n" </br>
	 * </br>
	 * These numbers represent the locations of the 4 players at the given time
	 * 
	 * @return - a string representing the locations of all players
	 */
	public String recieveData() {
		return Network.recieveDataFromServer(this.inputStreamFromServer);
	}

	@Override
	public void oneCycle(Surface[] surfaces) {
		super.oneCycle(surfaces);
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
	}
}
