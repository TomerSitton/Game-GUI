package Game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

//TODO - move the communication stuff to Main and move the index variable to there as well
/**
 * This class represents a player in the game.<br>
 * <br>
 * It handles its connection and communication to the server, its movement, its
 * state (such as the amount of health left) and its actions (such as attacking
 * the other characters)
 * 
 * @author Sitton
 */
public class Player extends Sprite {

	/////////////////// fields /////////////////

	// serial version
	private static final long serialVersionUID = 1L;

	/**
	 * the client's socket
	 */
	private Socket socket;

	/**
	 * the client's input stream
	 */
	private BufferedReader inputStreamFromServer;

	/**
	 * the client's output stream
	 */
	private DataOutputStream outputStreamToServer;

	/**
	 * the index of the player (received by the server)
	 */
	private int index = -1;

	/**
	 * The constants of the player
	 */
	public static final String URL = "img/player1-2.0.png";
	public static final int ROWS = 4;
	public static final int COLUMNS = 8;
	public static final int WIDTH = 110;
	public static final int HEIGHT = 150;
	public static final int SPEED_X = 25;
	public static final int SPEED_Y = 20;
	/**
	 * the health of the player
	 */
	private int health;

	// TODO - make this enum..?
	/**
	 * the attacking char of the player:<br>
	 * N - not attacking<br>
	 * F - attacking
	 */
	private char attackingChar = 'N';

	/**
	 * an {@link ArrayList} containing the attacks of the {@link Player}.
	 * 
	 * @see FireAttack
	 */
	private ArrayList<FireAttack> attacks = new ArrayList<>();

	/////////////////// constructors /////////////////

	/**
	 * This constructs a new {@link Player} with the given arguments
	 * 
	 * @param x
	 *            - the starting x position of the {@link Player}
	 * @param y
	 *            - the starting Y position of the {@link Player}
	 * @param isMyPlayer
	 *            - true if the player is the local player (so a connetion to the
	 *            server needs to be set up)
	 */
	public Player(int x, int y, boolean isMyPlayer) {
		super(x, y, URL, ROWS, COLUMNS, WIDTH, HEIGHT, SPEED_X, SPEED_Y);
		health = 3;
		if (isMyPlayer)
			initSocketStreams();
	}

	/**
	 * This constructs a new {@link Player} with the given arguments
	 * 
	 * @param x
	 *            - the starting x position of the {@link Player}
	 * @param y
	 *            - the starting Y position of the {@link Player}
	 * @param index
	 *            - the index of the {@link Player}
	 */
	public Player(int x, int y, int index) {
		this(x, y, false);
		this.index = index;
	}

	/////////////////// getters /////////////////
	/**
	 * returns the health of the {@link Player}
	 * 
	 * @return the health of the {@link Player}
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * returns the attacks of the player
	 * 
	 * @return the attacks of the player
	 */
	public ArrayList<FireAttack> getAttacks() {
		return attacks;
	}

	/////////////////// other methods /////////////////

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
	 * returns the index of the player in the game
	 * 
	 * @return the index of the player in the game
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * send data to the server. <br>
	 * In the game, the data sent to the the server is represented like this: <br>
	 * <br>
	 * "[newX,newY]_attackingChar\n" <br>
	 * <br>
	 * So if for example my character moved from (0,0) to (10,12),and did not make
	 * an attempt to attack, the data to be sent to the server will look like
	 * this:<br>
	 * "[10,12]_N\n"
	 * 
	 * @param newX
	 *            - the new x coordinate of the player.
	 * @param newY
	 *            - the new y coordinate of the player.
	 * 
	 */
	public void sendLocationData(int newX, int newY) {
		String state = "[" + newX + "," + newY + "]_" + this.attackingChar + "\n";
		Network.sendDataToServer(this.outputStreamToServer, state);
		if (attackingChar == 'F')
			attackingChar = 'N';
	}

	/**
	 * This method sends a string to the server using the
	 * {@link Network#sendDataToServer(DataOutputStream, String)} method
	 * 
	 * @param msg
	 *            - the message to send to the server
	 */
	public void sendString(String msg) {
		Network.sendDataToServer(this.outputStreamToServer, msg);
	}

	/**
	 * Receive data from the server.<br>
	 * The data should look like this: "[newX1,newY1]_attk ~ [newX2,newY2]_attk ~
	 * [newX3,newY3]_attk ~ [newX4,newY4]_attk ~\n" <br>
	 * <br>
	 * 
	 * These numbers represent the locations of the 4 players at the given time, and
	 * the "attk" represents the {@link #attackingChar}s field of the players.<br>
	 * <br>
	 * 
	 * An example input for 4 players could look like this:<br>
	 * [100,350]_N ~ [529,350]_F ~ [958,350]_N ~ [1290,350]_N ~\n
	 * 
	 * @return - a string representing the locations of all players
	 */
	public String recieveData() {
		return Network.recieveDataFromServer(this.inputStreamFromServer);
	}

	/**
	 * Add a {@link FireAttack} to the {@link #attacks} fields
	 * 
	 * @see FireAttack
	 */
	public void attack() {
		if (health > 0)
			attacks.add(new FireAttack(this));
	}

	/**
	 * set the {@link #attackingChar}
	 * 
	 * @param attackingChar
	 *            - the new {@link #attackingChar}
	 */
	public void setAttackingChar(char attackingChar) {
		this.attackingChar = attackingChar;
	}

	/**
	 * Reduce the value of {@link #health} by 1 and set the
	 * {@link Sprite#costumeConst costume constant} to 2 for a short time in order
	 * to use the correct type of costume
	 * 
	 * @see Sprite#costumeConst
	 */
	public void looseHealth() {
		if (costumeConst != 0)
			return;
		health--;
		new Thread(new Runnable() {

			@Override
			public void run() {
				costumeConst = 2;
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				costumeConst = 0;
			}
		}).start();
	}

	/**
	 * {@inheritDoc}<br>
	 * <br>
	 * 
	 * In addition to that, this method moves the attacks of the {@link Player}
	 * 
	 * note - in case the {@link #getHealth() player's health} is 0, this method
	 * will move him to his death position
	 */
	@Override
	public void moveToLocation(int newX, int newY) {
		for (int i = 0; i < attacks.size(); i++) {
			attacks.get(i).move();
		}

		if (health <= 0) {
			this.x = Heart.determineXPosition(this, 1);
			this.y = WorldConstants.HEARTS.Y;
		} else {
			super.moveToLocation(newX, newY);
		}

	}

}