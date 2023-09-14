import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ColorPanel extends JPanel {
	private Map map;
	private Player player;
	private int xDim, yDim;
	private boolean inGame;
	private ArrayList<Enemy> enemies;
	private ArrayList<Texture> textures;
	private int FOV;
	private int round;
	private JFrame frame;
	private int pause;

	public ColorPanel(Map map, Player player, int xDim, int yDim, JFrame frame) {
		this.player = player;
		this.map = map;
		this.xDim = xDim;
		this.yDim = yDim;
		inGame = false;
		enemies = new ArrayList<Enemy>();
		FOV = 60;
		round = 0;
		this.frame = frame;
		pause = 100;

		textures = new ArrayList<Texture>();
		textures.add(new Texture(new File("Enemy.png"), 100));
		textures.add(new Texture(new File("Stone.png"), 100));
		textures.add(new Texture(new File("Wood.jpg"), 100));
		textures.add(new Texture(new File("Retro.png"), 100));

		addKeyListener(new Controls());
		addMouseListener(new PanelListener());
		setFocusable(true);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (player.getHealth() <= 0) {
			int round2 = round;
			gameEnd();
			JOptionPane.showMessageDialog(frame, "You Survived " + round2 + " Rounds", "Game Over", 
					JOptionPane.INFORMATION_MESSAGE);
		}
		if (inGame) {
			player.turn();
			player.move(map);
			for (Enemy enemy : enemies) {
				enemy.updateDir(player);
				enemy.move(map, player);
			}
			populate();
			paintBackdrop(g);
			paintWalls(g);
			paintEnemies(g);
			enemyShoot(g);
			paintPlayer(g);
			paintHealth(g);
			paintMap(g);
			if (enemies.size() == round) {

			}
		} else {
			paintTitleScreen(g);
		}
	}

	public void populate() {
		if (enemies.size() == 0) {
			if (pause == 0) {
				pause = 100;
				round++;
				while (enemies.size() < round) {
					int x = (int) (Math.random() * map.getBoard()[0].length);
					int y = (int) (Math.random() * map.getBoard().length);
					if (map.getBoard(player)[x][y] == 0) {
						enemies.add(new Enemy((x * 100) + 50, (y * 100 + 50), player));
					}
				}
			} else {
				pause--;
			}
		}
	}

	public void paintBackdrop(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, 1920, 540);
		g.setColor(Color.darkGray);
		g.fillRect(0, 540, 1920, 540);

	}

	public void paintWalls(Graphics g) {
		double[] pixels = Raycast.getDistances(map, player, FOV, xDim);
		int[] points = Raycast.getPoints(map, player, FOV, xDim);
		int[] types = Raycast.getTypes(map, player, FOV, xDim);
		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < 99; j++) {
				if (!player.isInScope()) {
					g.setColor(new Color(textures.get(types[i]).getRe()[j][points[i] % 100],
							textures.get(types[i]).getGr()[j][points[i] % 100], textures.get(types[i]).getBl()[j][points[i] % 100]));
					g.drawLine(i, yDim / 2 - (int) (50000 / pixels[i]) + (int) (100000 / pixels[i] * j / 100), i,
							yDim / 2 - (int) (50000 / pixels[i]) + (int) (100000 / pixels[i] * (j + 1) / 100));
				}
			}
			/*
			 * if(!player.isInScope()) { g.drawLine(i, yDim/2-(int)(50000/pixels[i]), i,
			 * yDim/2+(int)(50000/pixels[i])); } else { g.drawLine(i,
			 * yDim/2-(int)(100000/pixels[i]), i, yDim/2+(int)(100000/pixels[i])); }
			 */
		}
	}

	public void paintEnemies(Graphics g) {
		double[] pixels = Raycast.getEnemies(map, player, FOV, xDim, enemies, player.isInScope());
		int[] points = Raycast.getEnemyPoints(map, player, FOV, xDim, enemies, player.isInScope());
		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < 99; j++) {
				if (points[i] >= 0) {
					if (textures.get(0).getRe()[j][(points[i] % 100) + 50]
							+ textures.get(0).getGr()[j][(points[i] % 100) + 50]
							+ textures.get(0).getBl()[j][(points[i] % 100) + 50] > 0) {
						if (!player.isInScope()) {
							g.setColor(new Color(textures.get(0).getRe()[j][(points[i] % 100) + 50],
									textures.get(0).getGr()[j][(points[i] % 100) + 50],
									textures.get(0).getBl()[j][(points[i] % 100) + 50]));
							g.drawLine(i, yDim / 2 - (int) (100000 / pixels[i]) + (int) (200000 / pixels[i] * j / 100),
									i,
									yDim / 2 - (int) (100000 / pixels[i]) + (int) (200000 / pixels[i] * (j + 1) / 100));
						} else {
							g.setColor(new Color(textures.get(0).getRe()[j][(points[i] % 100) + 50],
									textures.get(0).getGr()[j][(points[i] % 100) + 50],
									textures.get(0).getBl()[j][(points[i] % 100) + 50]));
							g.drawLine(i, yDim / 2 - (int) (200000 / pixels[i]) + (int) (400000 / pixels[i] * j / 100),
									i,
									yDim / 2 - (int) (200000 / pixels[i]) + (int) (400000 / pixels[i] * (j + 1) / 100));
						}
					}
				}
			}
			/*
			 * if(pixels[i]>0) { g.setColor(Color.BLUE); if(!player.isInScope()) {
			 * g.drawLine(i, yDim/2-(int)(50000/pixels[i]), i,
			 * yDim/2+(int)(50000/pixels[i])); } else { g.drawLine(i,
			 * yDim/2-(int)(100000/pixels[i]), i, yDim/2+(int)(100000/pixels[i])); } }
			 */
		}
	}
	
	public void enemyShoot(Graphics g) {
		for(Enemy enemy:enemies) {
			if(Math.random()>.99&&Raycast.isHitting(map, enemy, player)) {
				player.setHealth(player.getHealth()-1);
			}
		}
	}

	public void paintPlayer(Graphics g) {
		try {
			if (!player.isInScope()) {
				g.drawImage(ImageIO.read(new File("Crosshair.png")), 734, 314, null);
				g.drawImage(ImageIO.read(new File("Tank.png")), 560, 630, null);
			} else {
				g.drawImage(ImageIO.read(new File("Scope.png")), 480, 60, null);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void paintHealth(Graphics g) {
		for (int i = 0; i < player.getHealth(); i++) {
			g.setColor(Color.red);
			g.fillRect(xDim - 110 - (100 * i), 10, 90, 50);
		}
	}

	public void paintMap(Graphics g) {
		for(int a=0; a<map.getBoard().length; a++) {
			for(int b=0; b<map.getBoard()[a].length; b++) {
				if(map.getBoard()[a][b]>0) {
					g.setColor(Color.BLACK);
				} else {
					g.setColor(Color.WHITE);
				}
				g.fillRect(10+(10*b), 10+(10*a), 10, 10);
			}
		}
		g.setColor(Color.RED);
		g.fillOval(8 + (player.getxPos()/10), 8 + (player.getyPos()/10), 4, 4);
	}
	
	public void paintTitleScreen(Graphics g) {
		try {
			g.drawImage(ImageIO.read(new File("Title0.png")), 0, 0, null);
			g.drawImage(ImageIO.read(new File("Title1.png")), -50, -350, null);
			g.drawImage(ImageIO.read(new File("Title2.png")), 0, 0, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void gameEnd() {
		player.setHealth(5);
		enemies.clear();
		round = 0;
		inGame = false;
	}

	public class Controls implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub

			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				player.setTurn(-4);
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				player.setTurn(4);
			}
			if (e.getKeyCode() == KeyEvent.VK_W) {
				player.setForward(true);
				double y = Math.sin(0.0174533 * player.getDirection());
				double x = Math.cos(0.0174533 * player.getDirection());
				player.setxVel((int) (20 * x));
				player.setyVel((int) (20 * y));
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				player.setBackward(true);
				double y = Math.sin(0.0174533 * player.getDirection());
				double x = Math.cos(0.0174533 * player.getDirection());
				player.setxVel(0 - (int) (20 * x));
				player.setyVel(0 - (int) (20 * y));
			}
			if(e.getKeyCode() == KeyEvent.VK_A) {
				player.setLeft(true);
				player.setDirection(player.getDirection()+90);
				double y = Math.sin(0.0174533 * player.getDirection());
				double x = Math.cos(0.0174533 * player.getDirection());
				player.setxVel((int) (10 * x));
				player.setyVel((int) (10 * y));
				player.setDirection(player.getDirection()-90);
			}
			if(e.getKeyCode() == KeyEvent.VK_D) {
				player.setRight(true);
				player.setDirection(player.getDirection()-90);
				double y = Math.sin(0.0174533 * player.getDirection());
				double x = Math.cos(0.0174533 * player.getDirection());
				player.setxVel((int) (10 * x));
				player.setyVel((int) (10 * y));
				player.setDirection(player.getDirection()+90);
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (Raycast.isEnemy(map, player, enemies) >= 0) {
					enemies.remove(Raycast.isEnemy(map, player, enemies));
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				player.setInScope(true);
				FOV = 30;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				player.setTurn(0);
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				player.setTurn(0);
			}
			if (e.getKeyCode() == KeyEvent.VK_W) {
				player.setForward(false);
				player.setxVel(0);
				player.setyVel(0);
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
				player.setBackward(false);
				player.setxVel(0);
				player.setyVel(0);
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				player.setLeft(false);
				player.setxVel(0);
				player.setyVel(0);
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				player.setRight(false);
				player.setxVel(0);
				player.setyVel(0);
			}
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				player.setInScope(false);
				FOV = 60;
			}
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

	private class PanelListener extends MouseAdapter {
		public void mousePressed(MouseEvent me) {
			int clickX = me.getX();
			int clickY = me.getY();
			System.out.println(clickX + ", " + clickY);
			if (!inGame) {
				if (clickX > 820 && clickX < 1820 && clickY > 430 && clickY < 680) {
					String[] options = {"Dungeon", "DeathBox", "Maze", "Colosseum"};
					String file = (String)JOptionPane.showInputDialog(null, "Choose Map:", "Menu", 
							JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
					try {
						map = new Map(new File(file+".txt"));
						if(file.equals("Colosseum")) {
							player.setxPos(250);
							player.setyPos(250);
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					player.setHealth(5);
					inGame = true;
				}
			}
		}

		public void mouseReleased(MouseEvent me) {

		}
	}
}