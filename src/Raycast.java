import java.util.ArrayList;

public class Raycast {
	
	public static double getDistance(Player player, Map map, double offset) {
		
		int[][] board = map.getBoard();
		double xPos = player.getxPos();
		double xPos2 = xPos;
		double yPos = player.getyPos();
		double yPos2 = yPos;
		double angle = player.getDirection() + offset;
		double rise = Math.sin(0.0174533*angle);
		double run = Math.cos(0.0174533*angle);
		boolean hit = false;
		while(!hit) {
			xPos += run;
			yPos += rise;
			if(board[(int)(yPos/100)][(int)(xPos/100)] >= 1) {
				hit = true;
			}
		}
		
		double diff = Math.sqrt(Math.pow((xPos2-xPos), 2) + Math.pow((yPos2-yPos), 2));
		diff = diff*Math.cos(0.0174533*offset);
		return diff;
	}
	
	public static int getPoint(Player player, Map map, double offset) {
		int[][] board = map.getBoard();
		double xPos = player.getxPos();
		double yPos = player.getyPos();
		double angle = player.getDirection() + offset;
		double rise = Math.sin(0.0174533*angle);
		double run = Math.cos(0.0174533*angle);
		boolean hit = false;
		while(!hit) {
			xPos += run;
			yPos += rise;
			if(board[(int)(yPos/100)][(int)(xPos/100)] >= 1) {
				hit = true;
				if((int)(xPos)%100==0||(int)(xPos)%100==99) {
					return (int)(yPos%100)+(board[(int)(yPos/100)][(int)(xPos/100)]*100);
				} else {
					return (int)(xPos%100)+(board[(int)(yPos/100)][(int)(xPos/100)]*100);
				}
			}
		}
		
		return -1;
	}
	
	public static int[] getPoints(Map map, Player player, double FOV, double num) {
		int[] points = new int[(int) num];
		for(int i=0; i<num; i++) {
			double offset = (FOV/2) - i*(FOV/(num-1));
			points[i] = getPoint(player, map, offset);
		}
		return points;
	}
	
	public static double getDistance(Player player, Map map) {
		int[][] board = map.getBoard();
		double xPos = player.getxPos();
		double xPos2 = xPos;
		double yPos = player.getyPos();
		double yPos2 = yPos;
		double rise = player.getyVel()/10.0;
		double run = player.getxVel()/10.0;
		boolean hit = false;
		while(!hit) {
			xPos += run;
			yPos += rise;
			if(board[(int)(yPos/100)][(int)(xPos/100)] >= 1) {
				hit = true;
			}
		}
		
		double diff = Math.sqrt(Math.pow((xPos2-xPos), 2) + Math.pow((yPos2-yPos), 2));
		return diff;
	}
	
	public static double[] getDistances(Map map, Player player, double FOV, double num) {
		double[] distances = new double[(int) num];
		for(int i=0; i<num; i++) {
			double offset = (FOV/2) - i*(FOV/(num-1));
			distances[i] = getDistance(player, map, offset);
		}
		return distances;
	}
	
	public static int getType(Player player, Map map, double offset) {
		int[][] board = map.getBoard();
		double xPos = player.getxPos();
		double yPos = player.getyPos();
		double angle = player.getDirection() + offset;
		double rise = Math.sin(0.0174533*angle);
		double run = Math.cos(0.0174533*angle);
		boolean hit = false;
		while(!hit) {
			xPos += run;
			yPos += rise;
			if(board[(int)(yPos/100)][(int)(xPos/100)] >= 1) {
				hit = true;
				return board[(int)(yPos/100)][(int)(xPos/100)];
			}
		}
		
		return -1;
	}
	
	public static int[] getTypes(Map map, Player player, double FOV, double num) {
		int[] types = new int[(int) num];
		for(int i=0; i<num; i++) {
			double offset = (FOV/2) - i*(FOV/(num-1));
			types[i] = getType(player, map, offset);
		}
		return types;
	}
	
	public static double getEnemy(Player player, Map map, double offset, ArrayList<Enemy> enemies, boolean inScope) {
		int[][] board = map.getBoard();
		double xPos = player.getxPos();
		double yPos = player.getyPos();
		double angle = player.getDirection() + offset;
		double rise = Math.sin(0.0174533*angle);
		double run = Math.cos(0.0174533*angle);
		boolean hit = false;
		
		Enemy seen = null;
		double distance = 0.0;
		for(Enemy enemy:enemies) {
			double rise2 = Math.sin(0.0174533*(enemy.getDirection()+90));
			double run2 = Math.cos(0.0174533*(enemy.getDirection()+90));
			double xPos2 = enemy.getxPos();
			double yPos2 = enemy.getyPos();
			double xInt = (((rise/run)*(xPos))-((rise2/run2)*(xPos2))-yPos+yPos2)/((rise/run)-(rise2/run2));
			double yInt = ((rise/run)*(xInt-xPos))+yPos;
			if(Math.sqrt(Math.pow(xInt-xPos2, 2)+Math.pow(yInt-yPos2, 2))<50) {
				if((run>0&&xPos-xInt<0)||(run<0&&xPos-xInt>0)) {
					if(seen == null) {
						seen = enemy;
						distance = Math.sqrt(Math.pow(xInt-xPos, 2)+Math.pow(yInt-yPos, 2));
					} else {
						if(Math.sqrt(Math.pow(xInt-xPos, 2)+Math.pow(yInt-yPos, 2))<distance) {
							seen = enemy;
							distance = Math.sqrt(Math.pow(xInt-xPos, 2)+Math.pow(yInt-yPos, 2));
						}
					}
				}
			}
		}
		
		if(seen == null) {
			return -1;
		}
		
		double xPos2 = xPos;
		double yPos2 = yPos;
		
		while(Math.sqrt(Math.pow(xPos2-xPos, 2)+Math.pow(yPos2-yPos, 2))<distance&&!inScope) {
			xPos += run;
			yPos += rise;
			if(board[(int)(yPos/100)][(int)(xPos/100)] >= 1) {
				return -1.0;
			}
		}
		
		return distance*Math.cos(0.0174533*offset);
	}
	
	public static double[] getEnemies(Map map, Player player, double FOV, double num, ArrayList<Enemy> enemies, 
			boolean inScope) {
		double[] distances = new double[(int) num];
		for(int i=0; i<num; i++) {
			double offset = (FOV/2) - i*(FOV/(num-1));
			distances[i] = getEnemy(player, map, offset, enemies, inScope);
		}
		return distances;
	}
	
	public static int getEnemyPoint(Map map, Player player, double offset, ArrayList<Enemy> enemies, boolean inScope) {
		int[][] board = map.getBoard();
		double xPos = player.getxPos();
		double yPos = player.getyPos();
		double angle = player.getDirection() + offset;
		double rise = Math.sin(0.0174533*angle);
		double run = Math.cos(0.0174533*angle);
		boolean hit = false;
		double xHit = 0.0;
		double yHit = 0.0;
		double eXPos = 0.0;
		double eYPos = 0.0;
		
		Enemy seen = null;
		double distance = 0.0;
		for(Enemy enemy:enemies) {
			double rise2 = Math.sin(0.0174533*(enemy.getDirection()+90));
			double run2 = Math.cos(0.0174533*(enemy.getDirection()+90));
			double xPos2 = enemy.getxPos();
			double yPos2 = enemy.getyPos();
			double xInt = (((rise/run)*(xPos))-((rise2/run2)*(xPos2))-yPos+yPos2)/((rise/run)-(rise2/run2));
			double yInt = ((rise/run)*(xInt-xPos))+yPos;
			if(Math.sqrt(Math.pow(xInt-xPos2, 2)+Math.pow(yInt-yPos2, 2))<50) {
				if((run>0&&xPos-xInt<0)||(run<0&&xPos-xInt>0)) {
					if(seen == null) {
						seen = enemy;
						distance = Math.sqrt(Math.pow(xInt-xPos, 2)+Math.pow(yInt-yPos, 2));
						xHit = xInt;
						yHit = yInt;
						eXPos = xPos2;
						eYPos = yPos2;
					} else {
						if(Math.sqrt(Math.pow(xInt-xPos, 2)+Math.pow(yInt-yPos, 2))<distance) {
							seen = enemy;
							distance = Math.sqrt(Math.pow(xInt-xPos, 2)+Math.pow(yInt-yPos, 2));
							xHit = xInt;
							yHit = yInt;
							eXPos = xPos2;
							eYPos = yPos2;
						}
					}
				}
			}
		}
		
		if(seen == null) {
			return -1;
		}
		
		double xPos2 = xPos;
		double yPos2 = yPos;
		
		while(Math.sqrt(Math.pow(xPos2-xPos, 2)+Math.pow(yPos2-yPos, 2))<distance&&!inScope) {
			xPos += run;
			yPos += rise;
			if(board[(int)(yPos/100)][(int)(xPos/100)] >= 1) {
				return -1;
			}
		}
		
		return (int)(Math.sqrt(Math.pow(xHit-eXPos, 2)+Math.pow(yHit-eYPos, 2)));
	}
	
	public static int[] getEnemyPoints(Map map, Player player, double FOV, double num, ArrayList<Enemy> enemies, 
			boolean inScope) {
		int[] points = new int[(int) num];
		for(int i=0; i<num; i++) {
			double offset = (FOV/2) - i*(FOV/(num-1));
			points[i] = getEnemyPoint(map, player, offset, enemies, inScope);
		}
		return points;
	}
	
	public static int isEnemy(Map map, Player player, ArrayList<Enemy> enemies) {
		int[][] board = map.getBoard();
		double xPos = player.getxPos();
		double yPos = player.getyPos();
		double angle = player.getDirection();
		double rise = Math.sin(0.0174533*angle);
		double run = Math.cos(0.0174533*angle);
		boolean hit = false;
		
		int num = 0;
		int seenInd = 0;
		Enemy seen = null;
		double distance = 0.0;
		for(Enemy enemy:enemies) {
			double rise2 = Math.sin(0.0174533*(enemy.getDirection()+90));
			double run2 = Math.cos(0.0174533*(enemy.getDirection()+90));
			double xPos2 = enemy.getxPos();
			double yPos2 = enemy.getyPos();
			double xInt = (((rise/run)*(xPos))-((rise2/run2)*(xPos2))-yPos+yPos2)/((rise/run)-(rise2/run2));
			double yInt = ((rise/run)*(xInt-xPos))+yPos;
			if(Math.sqrt(Math.pow(xInt-xPos2, 2)+Math.pow(yInt-yPos2, 2))<50) {
				if((run>0&&xPos-xInt<0)||(run<0&&xPos-xInt>0)) {
					if(seen == null) {
						seen = enemy;
						seenInd = num;
						distance = Math.sqrt(Math.pow(xInt-xPos, 2)+Math.pow(yInt-yPos, 2));
					} else {
						if(Math.sqrt(Math.pow(xInt-xPos, 2)+Math.pow(yInt-yPos, 2))<distance) {
							seen = enemy;
							seenInd = num;
							distance = Math.sqrt(Math.pow(xInt-xPos, 2)+Math.pow(yInt-yPos, 2));
						}
					}
				}
			}
			num++;
		}
		
		if(seen == null) {
			return-1;
		}
		
		double xPos2 = xPos;
		double yPos2 = yPos;
		
		while(Math.sqrt(Math.pow(xPos2-xPos, 2)+Math.pow(yPos2-yPos, 2))<distance) {
			xPos += run;
			yPos += rise;
			if(board[(int)(yPos/100)][(int)(xPos/100)] >= 1) {
				return -1;
			}
		}
		
		return seenInd;
		
	}
	
	public static double getDistance(Enemy player, Map map, double offset) {
		
		int[][] board = map.getBoard();
		double xPos = player.getxPos();
		double xPos2 = xPos;
		double yPos = player.getyPos();
		double yPos2 = yPos;
		double angle = player.getDirection() + offset;
		double rise = Math.sin(0.0174533*angle);
		double run = Math.cos(0.0174533*angle);
		boolean hit = false;
		while(!hit) {
			xPos += run;
			yPos += rise;
			if(board[(int)(yPos/100)][(int)(xPos/100)] >= 1) {
				hit = true;
			}
		}
		
		double diff = Math.sqrt(Math.pow((xPos2-xPos), 2) + Math.pow((yPos2-yPos), 2));
		diff = diff*Math.cos(0.0174533*offset);
		return diff;
	}
	
	public static double[] getDistances(Map map, Enemy player, double FOV, double num) {
		double[] distances = new double[(int) num];
		for(int i=0; i<num; i++) {
			double offset = (FOV/2) - i*(FOV/(num-1));
			distances[i] = getDistance(player, map, offset);
		}
		return distances;
	}
	
	public static boolean isHitting(Map map, Enemy enemy, Player player) {
		double rise = Math.sin(0.0174533*enemy.getDirection());
		double run = Math.cos(0.0174533*enemy.getDirection());
		double distance = Math.sqrt(Math.pow((enemy.getxPos()-player.getxPos()), 2) + 
				Math.pow((enemy.getyPos()-player.getyPos()), 2));
		double xPos = enemy.getxPos();
		double yPos = enemy.getyPos();
		double xPos2 = xPos;
		double yPos2 = yPos;
		
		while(Math.sqrt(Math.pow((xPos2-xPos), 2) + Math.pow((yPos2-yPos), 2))<distance) {
			xPos2+=run;
			yPos2+=rise;
			if(map.getBoard()[(int)(yPos2/100)][(int)(xPos2/100)]>0) {
				return false;
			}
		}
		
		return true;
	}
	
	/*
	public static int raycast(Map map, Player player, double angle, double up) {
		

		double y = Math.sin(0.0174533*angle);
		double x = Math.cos(0.0174533*angle);
		double z = Math.sin(0.0174533*up);
		double xPos = player.getxPos();
		double yPos = player.getyPos();
		
		for(double i=50; i<100&&i>0; i+=z) {
			xPos+=x;
			yPos+=y;
			if(map.getBoard()[(int)(yPos/100)][(int)(xPos/100)] >= 1) {
				return map.getBoard()[(int)(yPos/100)][(int)(xPos/100)];
			}
		}
		
		return 0;
	}
	
	public static int[][] raycaster(Map map, Player player, double FOV, int width, int height){
		int[][] pixels = new int[height][width];
		double offset = FOV/(double)(width);
		
		for(int a=0; a<height; a++) {
			for(int b=0; b<width; b++) {
				pixels[a][b] = raycast(map, player, player.getDirection() + FOV/2 - b*offset, 
						(a-height/2)*offset);
			}
		}
		
		return pixels;
	}
	*/
}