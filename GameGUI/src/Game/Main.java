package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
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
	private int dx;

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
				if (i + 1 == myPlayer.getIndex())
					players[i] = myPlayer;
				else
					players[i] = new Player(i * 100, 200, i+1);
		}

		// initialize the surfaces
		surfaces[0] = new Surface(WorldConstants.GROUND.X, WorldConstants.GROUND.Y, WorldConstants.GROUND.WIDTH,
				WorldConstants.GROUND.HEIGHT);
		this.add(surfaces[0]);

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
		Sprite2.getExistingSprites().forEach((s) -> s.draw(g));
		Arrays.asList(surfaces).forEach(surface -> surface.paint(g));
	}

	/**
	 * this is the thread's function handling the cycles of the game. In each run
	 * the function updates calculates the X and Y values of the player, sends it to
	 * the server, and then receives the positions of the rest of the players and
	 * updates them on the frame
	 */
	@Override
	public void run() {
		while (true) {
			// calculate the new positions
			int newX = myPlayer.getX() + dx;
			int newY = myPlayer.getY() + myPlayer.getCurrentYSpeed(surfaces);
			// send player's state to the server
			myPlayer.sendData(newX, newY);
			// receive states of all the players and update the frame
			updateFrame();
			repaint();
			try {
				Thread.sleep(15);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

		}
	}

	/**
	 * this method handles the keyboard requests. it changes the direction of the
	 * character accordingly
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
	 * change the direction back when the released
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

	public void updateFrame() {
		/*
		 * Receive data from server - looking like this: "[22,32]_N ~ [100,110]_F ~ \n"
		 */
		String data = myPlayer.recieveData();
		/*
		 * the state of each player: "[22,32]_N" for player one and "[100,110]_F" for
		 * player two
		 */
		String states[] = data.split(" ~ ");

		for (int i = 0; i < players.length; i++) {
			/*
			 * splitting each state to the wanted values: values[0] = location (list)
			 * values[1] = attackingChar (char)
			 */
			String[] values = states[i].split("_");

			/*
			 * the location of the player, represented by an array which its values are "x"
			 * and "y"
			 */
			String[] location = values[0].replace("]", "").replace("[", "").split(",");
			players[i].moveToLocation(Integer.parseInt(location[0]), Integer.parseInt(location[1]));

			if (values[1].equals("F"))
				players[i].attack();

		}
	}

	public static void main(String[] args) {
		new Main();
	}

}
