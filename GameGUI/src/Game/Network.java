package Game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This is an interface with basic methods to be used for players to send data
 * to the server and receive data from it
 */
public interface Network {
	// server's address constants
	/**
	 * The IP address of the server
	 */
	public static final String SERVER_IP = "192.168.172.107";
	/**
	 * The port on which the server runs
	 */
	public static final int SERVER_PORT = 2212;

	/**
	 * send data to the server. <br>
	 * In the game, the data sent to the the server is represented like this: <br>
	 * <br>
	 * "[newX,newY]_attackingChar#health\n" <br>
	 * <br>
	 * So if for example my character moved from (0,0) to (10,12), and did not make
	 * an attempt to attack, the data to be sent to the server will look like
	 * this:<br>
	 * "[10,12]_N#2\n"
	 * 
	 * @param outputStreamToServer
	 *            - the output stream of the player's socket
	 *
	 * @param data
	 *            - the data to be sent to the server
	 * @see Player#setAttackingChar(char)
	 *
	 *
	 */
	public static void sendDataToServer(DataOutputStream outputStreamToServer, String data) {
		try {
			outputStreamToServer.write(data.getBytes());
		} catch (IOException e) {
			System.out.println("IO exeption catched while trying to send data to the server");
			e.printStackTrace();
		}
	}

	/**
	 * Receive data from the server.<br>
	 * The data should look like this: "[newX1,newY1]_attk#hlth ~ [newX2,newY2]_attk#hlth ~
	 * [newX3,newY3]_attk#hlth ~ [newX4,newY4]_attk#hlth ~\n" <br>
	 * <br>
	 * 
	 * These numbers represent the locations of the 4 players at the given time, and
	 * the "attk" represents the players' attacking char.
	 * 
	 * An example input for 4 players could look like this:<br>
	 * [100,350]_N#hlth ~ [529,350]_F#hlth ~ [958,350]_N#hlth ~ [1290,350]_N#hlth ~\n
	 * 
	 * @param inputStreamFromServer
	 *            - the input stream of the player's socket
	 * 
	 * 
	 * @return - a string representing the locations of all players
	 * 
	 * @see Player#setAttackingChar(char)
	 */
	public static String recieveDataFromServer(BufferedReader inputStreamFromServer) {
		String msg = null;
		try {
			msg = inputStreamFromServer.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("received "+msg);
		return msg;
	}
}
