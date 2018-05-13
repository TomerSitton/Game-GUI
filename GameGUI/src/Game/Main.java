package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ObjectStreamException;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

//TODO - make the movement of my character only via the server (and not locally), just like the movement of the other players
// TODO - make a client class that handles commu with server (like the index and number of players etc.)
public class Main extends JPanel implements Runnable, KeyListener {
	private Player[] players;
	private Player myPlayer;
	private JFrame frame;
	private Surface[] surfaces = new Surface[1];
	private HashMap<String, Boolean> keys = new HashMap<String, Boolean>() {
		{
			put("RIGHT", false);
			put("LEFT", false);
		}
	};

	public Main() {

		initFrame();
		initComponents();
		Thread t = new Thread(this);
		t.start();

		frame.setVisible(true);
	}

	/**
	 * handles the initialization of the JFrame: size, color, keyListener etc.
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
	 * creates the components of the JFrame and adds them to the frame. The
	 * components include players, surfaces etc.
	 */
	private void initComponents() {
		// initialize my player
		myPlayer = new Player(100, 200, true);
		this.add(myPlayer);

		// initialize other players
		players = new Player[Integer.parseInt(myPlayer.recieveData())];
		for (int i = 0; i < players.length; i++) {
			try {
				if (i + 1 == myPlayer.getIndex())
					players[i] = myPlayer;
				else
					players[i] = new Player(i * 100, 200, false);
			} catch (ObjectStreamException e) {
				e.printStackTrace();
			}
		}

		// initialize the surfaces
		surfaces[0] = new Surface(WorldConstants.GROUND.X, WorldConstants.GROUND.Y, WorldConstants.GROUND.WIDTH,
				WorldConstants.GROUND.HEIGHT);
		this.add(surfaces[0]);
	}

	/**
	 * calls the painting methods of all of the JFrame's components
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Sprite2.getExistingSprites().forEach((s) -> s.draw(g));
		Arrays.asList(surfaces).forEach(surface -> surface.paint(g));
	}

	/**
	 * this is the thread's function handling the cycles of the game. In each
	 * run the function changes the characters positions and updates them on the
	 * JFrame accordingly, and send the position of the player to the server
	 */
	@Override
	public void run() {
		while (true) {
			// TODO - move the repaint to the end of the while loop and remove
			// the call to repaint in the moveOneStep method
			Sprite2.getExistingSprites().forEach((s) -> s.fall(surfaces));
			Sprite2.getExistingSprites()
					.forEach((s) -> s.moveToLocation(s.getX() + s.getSpeedX(), s.getY() + s.getSpeedY()));
			repaint();
			myPlayer.sendData("[" + myPlayer.getX() + "," + myPlayer.getY() + "]\n");
			updatePlayersLocations();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

		}
	}

	/**
	 * this method handles the keyboard requests. it changes the direction of
	 * the character accordingly
	 */
	// TODO - why is it just changing the direction? why not changing the
	// direction to look to the right one and ALSO call the movement method (i.e
	// changing the x and y values)
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			keys.put("RIGHT", true);
			myPlayer.setSpeedX(Player.SPEED_X);
			break;
		case KeyEvent.VK_LEFT:
			myPlayer.setSpeedX(-Player.SPEED_X);
			keys.put("LEFT", true);
			break;
		case KeyEvent.VK_SPACE:
			myPlayer.TryToJump();
			break;
		}

	}

	/**
	 * change the direction back when the released
	 */
	// TODO - make it work with the new version of keyPressed
	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println("released");
		switch (e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			if (keys.get("LEFT") == false)
				myPlayer.setSpeedX(0);
			keys.put("RIGHT", false);
			break;
		case KeyEvent.VK_LEFT:
			if (keys.get("RIGHT") == false)
				myPlayer.setSpeedX(0);
			keys.put("RIGHT", false);
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	/**
	 * 
	 * @return - an array of points which represents the locations of the
	 *         players received from the server
	 */
	private Point[] getPlayersLocations() {
		// Receive data from server
		String data = myPlayer.recieveData();
		// the location string for each player would look like this:['x','y']
		String location;
		// the locations of the players
		Point[] playersLocations = new Point[players.length];

		for (int i = 0; i < players.length; i++) {
			/** handle strings **/
			location = data.substring(data.indexOf('['), data.indexOf(']') + 1).replaceAll("\\s", "").replace("'", "");
			// System.out.println("location for " + i + " - " + location);
			// cutting the last location from the string
			data = data.substring(data.indexOf(']') + 1);

			/** handle points **/
			int x = Integer.parseInt(location.substring(location.indexOf('[') + 1, location.indexOf(',')));
			int y = Integer.parseInt(location.substring(location.indexOf(',') + 1, location.indexOf(']')));
			playersLocations[i] = new Point(x, y);
		}
		return playersLocations;
	}

	/**
	 * changes the players positions to those received by the server
	 */
	public void updatePlayersLocations() {
		Point[] locations = getPlayersLocations();
		for (int i = 0; i < players.length; i++) {
			players[i].moveToLocation((int) locations[i].getX(), (int) locations[i].getY());
		}
	}

	public static void main(String[] args) {
		new Main();
	}

}
