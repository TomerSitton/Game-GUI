package Game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectStreamException;
import java.net.Socket;
import java.util.ArrayList;

//TODO - move the communication stuff to Main and move the index variable to there as well
public class Player extends Sprite2 {

	// client's socket and I/O streams
	private Socket socket;
	private BufferedReader inputStreamFromServer;
	private DataOutputStream outputStreamToServer;

	private int index = -1;

	public static final String URL = "img/player1.png";
	public static final int ROWS = 2;
	public static final int COLUMNS = 8;
	public static final int WIDTH = 110;
	public static final int HEIGHT = 150;
	public static final int SPEED_X = 25;
	public static final int SPEED_Y = 20;
	private int health = 3;

	public Player(int x, int y, boolean isMyPlayer) {
		super(x, y, URL, ROWS, COLUMNS, WIDTH, HEIGHT, SPEED_X, SPEED_Y);
		if (isMyPlayer)
			initSocketStreams();
	}

	public int getHealth() {
		return health;
	}

	/**
	 * initialize the client's sockets and I/O streams
	 */
	private void initSocketStreams() {
		try {
			socket = new Socket(Network.SERVER_IP, Network.SERVER_PORT);
			inputStreamFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputStreamToServer = new DataOutputStream(socket.getOutputStream());
			index = Integer.parseInt(inputStreamFromServer.readLine());
			System.out.println("the player's index is" + index);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return the index of the player in the game
	 * @throws ObjectStreamException
	 */
	public int getIndex() throws ObjectStreamException {
		if (index == -1) {
			throw new ObjectStreamException("the 'index' variable of that player has not yet been initialized") {
			};
		}
		return index;
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

	public void attack() {
		new FireAttack(this);
	}

	public void looseHealth() {
		health--;
	}

	public class FireAttack extends Sprite2 implements Runnable {

		public static final String URL = "img/fireAttack.png";
		public static final int ROWS = 2;
		public static final int COLUMNS = 6;
		public static final int WIDTH = 50;
		public static final int HEIGHT = 50;
		public static final int SPEED_X = 15;
		public static final int SPEED_Y = 0;
		private Player myPlayer;
		private boolean isLookingRight;

		public FireAttack(Player player) {
			super(player.getX(), player.getY(), URL, ROWS, COLUMNS, WIDTH, HEIGHT, SPEED_X, SPEED_Y);
			this.myPlayer = player;
			isLookingRight = myPlayer.isLookingRight();
			new Thread(this).start();

		}

		@Override
		public void run() {
			while (this.x < WorldConstants.Frame.WIDTH && this.x + WIDTH > WorldConstants.Frame.X) {
				for (Sprite2 sprite : this.spritesTouching()) {
					if (sprite instanceof Player && sprite != this.myPlayer) {
						((Player) sprite).looseHealth();
						System.out.println(((Player) sprite).getHealth());
					}
				}
				if (isLookingRight)
					x = x + speedX;
				else
					x = x - speedX;
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.removeSprite();
		}
	}
}