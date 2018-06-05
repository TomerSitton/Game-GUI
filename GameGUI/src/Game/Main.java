package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Supplier;

import javax.lang.model.type.ArrayType;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//TODO - make the movement of my character only via the server (and not locally), just like the movement of the other players
// TODO - make a client class that handles commu with server (like the index and number of players etc.)
/**
 * This is the main class of the project.<br>
 * <br>
 * 
 * It handles the creation of the frame and the movement and actions of the
 * characters
 * 
 * @author Sitton
 *
 */
public class Main extends JPanel implements Runnable, KeyListener {

	/////////////////// fields /////////////////

	// serial number
	private static final long serialVersionUID = 1L;

	/**
	 * an {@link ArrayType array} containing all the {@link Player players} in
	 * the game
	 */
	private Player[] players;

	/**
	 * the local player
	 */
	private Player myPlayer;

	/**
	 * the game's frame
	 */
	private JFrame frame;

	/**
	 * an {@link ArrayType array} containing the {@link Surface surfaces} in the
	 * map
	 */
	private Surface[] surfaces = new Surface[1];

	// movement-helpers fields
	private HashMap<String, Boolean> keys = new HashMap<String, Boolean>() {
		// serial number
		private static final long serialVersionUID = 1L;

		{
			put("RIGHT", false);
			put("LEFT", false);
		}
	};
	private int dx;

	/////////////////// constructors /////////////////

	/**
	 * This constructs a new {@link Main} by calling the {@link #initFrame()}
	 * and {@link #initComponents()} methods and starting the main thread
	 * {@link #run()}
	 * 
	 * @see #initFrame()
	 * @see #initComponents()
	 * @see #run()
	 */
	public Main() {

		initFrame();
		initComponents();
		new Thread(this).start();

		frame.setVisible(true);
	}

	/////////////////// initialization methods /////////////////

	/**
	 * Handles the initialization of the JFrame: size, color, keyListener etc.
	 */
	private void initFrame() {
		this.setBounds(10, 10, 1000, 700);
		frame = new JFrame();
		frame.addKeyListener(this);
		frame.setBounds(10, 10, 1000, 700);
		frame.add(this, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.gray);
	}

	/**
	 * returns an {@link ArrayType array} containing the starting x locations of
	 * the players according to the number of players in the game
	 * 
	 * @return - an {@link ArrayType array} containing the starting x locations
	 *         of the players
	 */
	private int[] getPlayersStartingXPositions() {
		int[] locations = new int[players.length];
		final int LEFT = WorldConstants.GROUND.X;
		final int MIDDLE = (int) (WorldConstants.GROUND.X + 0.5 * WorldConstants.GROUND.WIDTH);
		final int RIGHT = WorldConstants.GROUND.X + WorldConstants.GROUND.WIDTH - Player.WIDTH;
		switch (locations.length) {
		case 1:
			locations[0] = MIDDLE;
			break;
		case 2:
			locations[0] = LEFT;
			locations[1] = RIGHT;
			break;
		case 3:
			locations[0] = LEFT;
			locations[1] = MIDDLE;
			locations[2] = RIGHT;
			break;
		case 4:
			locations[0] = LEFT;
			locations[1] = (int) (WorldConstants.GROUND.X + 0.33 * WorldConstants.GROUND.WIDTH);
			locations[2] = (int) (WorldConstants.GROUND.X + 0.66 * WorldConstants.GROUND.WIDTH);
			locations[3] = RIGHT;
		}

		return locations;
	}

	/**
	 * Creates the components of the JFrame and adds them to the frame. The
	 * components include players, surfaces etc.
	 */
	private void initComponents() {
		// initialize my player
		myPlayer = new Player(0, 0, true);
		this.add(myPlayer);

		// initialize other players
		String serverMsg = myPlayer.recieveData();

		while (!serverMsg.equals("start game")) {

			// no msg received
			if (serverMsg == null || serverMsg == "")
				continue;

			// the msg is "we are %d players. more players?"
			if (serverMsg.contains("more players?")) {
				int numberOfPlayers = Integer.parseInt(Character.toString(serverMsg.charAt(7)));
				switch (JOptionPane.showConfirmDialog(null,
						"we are " + numberOfPlayers + ". are there more players wishing to join the game?",
						"other players?", JOptionPane.YES_NO_OPTION)) {
				case JOptionPane.YES_OPTION:
					myPlayer.sendString("yes");
					break;
				default:
					myPlayer.sendString("no");
					break;
				}

			}

			// the msg is "the final number of players is %d"
			else if (serverMsg.contains("the final number of players is"))
				players = new Player[Integer.parseInt(serverMsg.substring(serverMsg.length() - 1))];

			// receiving data again
			serverMsg = myPlayer.recieveData();
		}

		for (int i = 0; i < players.length; i++) {
			int startingX = getPlayersStartingXPositions()[i];
			int startingY = WorldConstants.GROUND.Y - WorldConstants.GROUND.HEIGHT - Player.HEIGHT;

			if (i + 1 == myPlayer.getIndex()) {
				players[i] = myPlayer;
				players[i].moveToLocation(startingX, startingY);
			} else
				players[i] = new Player(startingX, startingY, i + 1);
		}

		// initialize the surfaces
		surfaces[0] = new Surface(WorldConstants.GROUND.X, WorldConstants.GROUND.Y, WorldConstants.GROUND.WIDTH,
				WorldConstants.GROUND.HEIGHT);
		this.add(surfaces[0]);

		// initialize hearts
		for (Player player : players) {
			for (int j = 0; j < player.getHealth(); j++) {
				new Heart(player, j);
			}
		}
	}

	/**
	 * calls the painting methods of all of the JFrame's components
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Sprite.getExistingSprites().forEach((s) -> s.draw(g));
		Arrays.asList(surfaces).forEach(surface -> surface.paint(g));
	}

	/**
	 * This is the thread's function handling the cycles of the game.<br>
	 * In each run this method calculates the X and Y values of the player,
	 * sends them to the server, and then receives the positions of the rest of
	 * the players and updates them on the frame using the
	 * {@link #updateFrame()} method.
	 * 
	 * @see #updateFrame()
	 */
	@Override
	public void run() {
		Supplier<Integer> livingPlayers = new Supplier<Integer>() {
			@Override
			public Integer get() {
				int sum = 0;
				for (Player p : players)
					if (p.getHealth() > 0)
						sum++;
				return sum;
			}
		};

		// update locations as long as there are more than one player alive
		while (livingPlayers.get() > 1) {
			// calculate the new positions
			int newX = myPlayer.getX() + dx;
			int newY = myPlayer.getY() + myPlayer.getCurrentYSpeed(surfaces);
			// send player's state to the server
			myPlayer.sendLocationData(newX, newY);
			// receive states of all the players and update the frame
			updateFrame();
			repaint();

			// kill if outside of the frame
			for (Player p : players) {
				if (p.getY() > WorldConstants.Frame.HEIGHT)
					while (p.getHealth() > 0)
						p.looseHealth();
			}

			try {
				Thread.sleep(15);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

		}

		// tell the server the game is over
		myPlayer.sendString("finished!");

		repaint();

		// tell the server that my player has won if that's true
		if (myPlayer.getHealth() > 0)
			myPlayer.sendString("player " + myPlayer.getIndex() + " win");

		String winningPlayerIndex = myPlayer.recieveData();
		while (winningPlayerIndex.length() > 1)
			winningPlayerIndex = myPlayer.recieveData();

		JOptionPane.showMessageDialog(this.frame, "THE WINNER IS PLAYER NUMBER " + winningPlayerIndex);
		System.exit(0);
	}

	/**
	 * This method handles the keyboard requests:<br>
	 * 1. It sets the {@link #dx} field according to the arrow key pressed (so
	 * if the left key was pressed dx will be negative, and if the right key was
	 * pressed it will be positive).<br>
	 * 2. If the space key has been pressed, it sets the attacking char of the
	 * {@link Player player} to F.<br>
	 * 3. If the up key was pressed, it makes the {@link Player player} try to
	 * jump
	 * 
	 * @param e
	 *            - the {@link KeyEvent}
	 * 
	 * @see Player#setAttackingChar(char)
	 * @see Player#TryToJump()
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			dx = myPlayer.getSpeedX();
			keys.put("RIGHT", true);
			break;
		case KeyEvent.VK_LEFT:
			dx = -myPlayer.getSpeedX();
			keys.put("LEFT", true);
			break;
		case KeyEvent.VK_SPACE:
			myPlayer.setAttackingChar('F');
			break;
		case KeyEvent.VK_UP:
			myPlayer.TryToJump();
			break;
		}

	}

	/**
	 * This method handles the directions when the key is released
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			if (keys.get("LEFT") == false)
				dx = 0;
			keys.put("RIGHT", false);
			break;
		case KeyEvent.VK_LEFT:
			if (keys.get("RIGHT") == false)
				dx = 0;
			keys.put("LEFT", false);
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	/**
	 * This method handles the update of the game's state on the frame.<br>
	 * It receives the data (location and attacking char) for each player from
	 * the server, moves the players to the correct locations (using
	 * {@link Player#moveToLocation(int, int)} and makes the attack if needed
	 * (using the {@link Player#attack()} method)
	 * 
	 * @see Player#moveToLocation(int, int)
	 * @see Player#attack()
	 */
	public void updateFrame() {
		/*
		 * Receive data from server - looking like this:
		 * "[22,32]_N ~ [100,110]_F ~ \n"
		 */
		String data = myPlayer.recieveData();
		/*
		 * the state of each player: "[22,32]_N" for player one and
		 * "[100,110]_F" for player two
		 */
		String states[] = data.split(" ~ ");

		for (int i = 0; i < players.length; i++) {
			/*
			 * splitting each state to the wanted values: values[0] = location
			 * (list) values[1] = attackingChar (char)
			 */
			String[] values = states[i].split("_");

			/*
			 * the location of the player, represented by an array which its
			 * values are "x" and "y"
			 */
			String[] location = values[0].replace("]", "").replace("[", "").split(",");

			players[i].moveToLocation(Integer.parseInt(location[0]), Integer.parseInt(location[1]));

			if (values[1].equals("F") && players[i].getAttacks().size() < 3)
				players[i].attack();
		}
	}

	/**
	 * the main function. creates a new {@link Main} instance
	 * 
	 * @param args
	 *            - the arguments of the main method
	 */
	public static void main(String[] args) {
		new Main();
	}

}
