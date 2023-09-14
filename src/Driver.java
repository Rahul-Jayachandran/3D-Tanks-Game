import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFrame;

public class Driver {
	
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		System.out.println("Hello World");
		File mapText = new File("DeathBox.txt");
		Map map = new Map(mapText);
		Player player = new Player(150, 150, 0);
		//System.out.println(map.toString(player));
		//System.out.println(Raycast.getDistance(player, map, 0));
		//System.out.println(Arrays.toString(Raycast.getDistances(map, player, 90, 3)));
		JFrame myFrame = new JFrame();
		myFrame.setSize(1920, 1080);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ColorPanel myPanel = new ColorPanel(map, player, 1920, 1080, myFrame);
		myFrame.getContentPane().add(myPanel);
		myFrame.setVisible(true);
		while(true) {
			Thread.sleep(10);
			myPanel.repaint();
		}
	}
}