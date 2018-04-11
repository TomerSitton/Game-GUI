package Game;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This is an interface which contains basic method to be used for player to
 * send data to the server and receive data from it
 */
public interface Network {
	// server's address constants
	public static final String SERVER_IP = "10.0.0.3";
	public static final int SERVER_PORT = 2212;

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
	public static void sendDataToServer(DataOutputStream outputStreamToServer, String data) {
		try {
			outputStreamToServer.write(data.getBytes());
		} catch (IOException e) {
			System.out.println("IO exeption catched while trying to send data to the server");
			e.printStackTrace();
		}
	}

	/**
	 * Receive data from the server.</br>
	 * The data should look like this:</br>
	 * "(newX1,newY1) : (newX2,newY2) : (newX3,newY3) : (newX4,newY4)\n" </br>
	 * </br>
	 * These numbers represent the locations of the 4 players at the given time
	 * 
	 * @return - a string representing the locations of all players
	 */
	public static String recieveDataFromServer(BufferedReader inputStreamFromServer) {
		String msg = null;
		try {
			msg = inputStreamFromServer.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
}
