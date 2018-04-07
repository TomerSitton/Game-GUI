package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Game.DirectionsTuple.DirectionX;

public class Main extends JPanel implements Runnable, KeyListener {
	private Player player;
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
		player = new Player(100, 200);
		this.add(player);

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
			player.sendData("(" + player.getX() + "," + player.getY() + ")\n");
			System.out.println("data recieved from server: "+player.recieveData());
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
			player.setDirectionX(DirectionX.MOVE_RIGHT);
			break;
		case KeyEvent.VK_LEFT:
			player.setDirectionX(DirectionX.MOVE_LEFT);
			break;
		case KeyEvent.VK_SPACE:
			player.TryToJump();
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			player.setDirectionX(DirectionX.LOOK_LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			player.setDirectionX(DirectionX.LOOK_RIGHT);
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
