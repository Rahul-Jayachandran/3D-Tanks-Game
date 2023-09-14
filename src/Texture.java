import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {
	
	private int[][] re;
	private int[][] gr;
	private int[][] bl;
	private int size;
	private File file;
	
	public Texture(File file, int size) {
		this.file = file;
		this.size = size;
		re = new int[size][size];
		gr = new int[size][size];
		bl = new int[size][size];
		load();
	}
	
	private void load() {
		
		BufferedImage image;
		try {
			int[][] pixels = new int[size][size];
			image = ImageIO.read(file);
			int w = image.getWidth();
			int h = image.getHeight();
			int[] temp = new int[size*size];
			image.getRGB(0, 0, w, h, temp, 0, w);
			for(int a=0; a<size; a++) {
				for(int b=0; b<size; b++) {
					pixels[a][b] = temp[(size*a)+b];
					re[a][b] = (pixels[a][b] & 0x00ff0000) >> 16;
					gr[a][b] = (pixels[a][b] & 0x0000ff00) >> 8;
					bl[a][b] =  pixels[a][b] & 0x000000ff;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public int[][] getRe() {
		return re;
	}

	public void setRe(int[][] re) {
		this.re = re;
	}

	public int[][] getGr() {
		return gr;
	}

	public void setGr(int[][] gr) {
		this.gr = gr;
	}

	public int[][] getBl() {
		return bl;
	}

	public void setBl(int[][] bl) {
		this.bl = bl;
	}
}
