package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ObjectStreamException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Game.DirectionsTuple.DirectionX;

//TODO - make the movement of my character only via the server (and not locally), just like the movement of the other players
// TODO - make a client class that handles commu with server (like the index and number of players etc.)
public class Main extends JPanel implements Runnable, KeyListener {
	private Player[] players;
	private Player myPlayer;
	private JFrame frame;
	private Surface[] surfaces = new Surface[1];

	public Main() {

		initFrame();
		initComponents();
		Thread t = new Thread(this);
		t.start();

		frame.setVisible(true);
	}

	private void initFrame() {
		this.setBounds(10, 10, 1000, 700);
		frame = new JFrame();
		frame.addKeyListener(this);
		frame.setBounds(10, 10, 1000, 700);
		frame.add(this, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.gray);
	}

	private void initComponents() {
		myPlayer = new Player(100, 200, true);
		this.add(myPlayer);

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

		surfaces[0] = new Surface(WorldConstants.GROUND.X, WorldConstants.GROUND.Y, WorldConstants.GROUND.WIDTH,
				WorldConstants.GROUND.HEIGHT);
		this.add(surfaces[0]);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Sprite2.getExistingSprites().forEach((s) -> s.draw(g));
		Arrays.asList(surfaces).forEach(surface -> surface.paint(g));
	}

	@Override
	public void run() {
		while (true) {
			cycle();
			myPlayer.sendData("(" + myPlayer.getX() + "," + myPlayer.getY() + ")\n");
			updatePlayersLocations();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void cycle() {
		Sprite2.getExistingSprites().forEach((s) -> s.oneCycle(surfaces));
		repaint();
	}

	public static void main(String[] args) {
		new Main();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			myPlayer.setDirectionX(DirectionX.MOVE_RIGHT);
			break;
		case KeyEvent.VK_LEFT:
			myPlayer.setDirectionX(DirectionX.MOVE_LEFT);
			break;
		case KeyEvent.VK_SPACE:
			myPlayer.TryToJump();
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			myPlayer.setDirectionX(DirectionX.LOOK_LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			myPlayer.setDirectionX(DirectionX.LOOK_RIGHT);
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void updatePlayersLocations() {
		Point[] locations = getPlayersLocations();
		for (int i = 0; i < players.length; i++) {
			players[i].moveToLocation((int) locations[i].getX(), (int) locations[i].getY());
		}
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
			System.out.println("location for " + i + " - " + location);
			// cutting the last location from the string
			data = data.substring(data.indexOf(']') + 1);

			/** handle points **/
			int x = Integer.parseInt(location.substring(location.indexOf('[') + 1, location.indexOf(',')));
			int y = Integer.parseInt(location.substring(location.indexOf(',') + 1, location.indexOf(']')));
			playersLocations[i] = new Point(x, y);
		}
		return playersLocations;
	}

}
