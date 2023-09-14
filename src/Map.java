import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Map {
	
	private int[][] board;
	
	public Map() {
		board = new int[10][10];
		for(int i=0; i<board.length; i++) {
			board[i][0] = 1;
			board[0][i] = 1;
			board[i][board.length-1] = 1;
			board[board.length-1][i] = 1;
		}
	}
	
	public Map(int[][] board) {
		this.board = board;
	}
	
	public Map(File file) throws FileNotFoundException {
		Scanner input = new Scanner(file);
		String inputStr = input.nextLine();
		this.board = new int[Integer.parseInt(inputStr.substring(0, inputStr.indexOf(' ')))]
				[Integer.parseInt(inputStr.substring(inputStr.indexOf(' ')+1))];
		for(int a=0; a<board.length; a++) {
			inputStr = input.nextLine();
			for(int b=0; b<board[a].length; b++) {
				board[a][b] = Character.getNumericValue(inputStr.charAt(b));
			}
		}
	}

	public int[][] getBoard() {
		return board;
	}
	
	public int[][] getBoard(Player player){
		int xPos = player.getxPos()/100;
		int yPos = player.getyPos()/100;
		int[][] board2 = new int[board.length][board.length];
		for(int a=0; a<board.length; a++) {
			for(int b=0; b<board.length; b++) {
				board2[a][b] = board[a][b];
			}
		}
		if(yPos<board2.length && xPos<board2[yPos].length)
			board2[yPos][xPos] = 2;
		return board2;
	}

	public void setBoard(int[][] board) {
		this.board = board;
	}
	
	public String toString() {
		String output = "";
		for(int a=board.length-1; a>=0; a++) {
			for(int b=0; b<board[a].length; b++) {
				output+=board[a][b] + " ";
			}
			output+="\n";
		}
		return output;
	}
	
	public String toString(Player player) {
		int xPos = player.getxPos()/100;
		int yPos = player.getyPos()/100;
		if(yPos<board.length && xPos<board[yPos].length)
			board[yPos][xPos] = 2;
		String output = "";
		for(int a=0; a<board.length; a++) {
			for(int b=0; b<board[a].length; b++) {
				output+=board[a][b] + " ";
			}
			output+="\n";
		}
		if(yPos<board.length && xPos<board[yPos].length)
			board[yPos][xPos] = 0;
		return output;
	}
}