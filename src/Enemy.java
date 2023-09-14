public class Enemy {
	
	private int xPos, yPos;
	private int direction;
	private int xVel, yVel;
	private int turn;
	private boolean forward;
	
	public Enemy(){
		xPos = 0;
		yPos = 0;
		direction = 0;
		xVel = 0;
		yVel = 0;
		forward = true;
	}
	
	public Enemy(int xPos, int yPos, Player player) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.direction = (int)(Math.atan((player.getyPos()-yPos)/(player.getxPos()-xPos+0.0))*57.2958);
		if(player.getxPos()-xPos<0) {
			direction+=180;
		} else if(player.getyPos()-yPos<0) {
			direction+=360;
		}
		xVel = 0;
		yVel = 0;
		turn = 0;
		forward = false;
	}
	
	public void move(Map map, Player player) {
		double y = Math.sin(0.0174533*direction);
		double x = Math.cos(0.0174533*direction);
		xVel = ((int)(5*x));
		yVel = ((int)(5*y));
		double distance = Math.sqrt(Math.pow(xPos-player.getxPos(), 2)+Math.pow(yPos-player.getyPos(), 2));
		if(Raycast.getDistance(this, map, 0)>Math.sqrt((xVel*xVel)+(yVel*yVel))&&distance>50&&Math.random()>.9) {
			xPos += xVel;
			yPos += yVel;
		}
	}
	
	public void updateDir(Player player) {
		this.direction = (int)(Math.atan((player.getyPos()-yPos)/(player.getxPos()-xPos+0.0))*57.2958);
		if(player.getxPos()-xPos<0) {
			direction+=180;
		} else if(player.getyPos()-yPos<0) {
			direction+=360;
		}
	}
	
	public void turn() {
		direction += turn;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getxVel() {
		return xVel;
	}

	public void setxVel(int xVel) {
		this.xVel = xVel;
	}

	public int getyVel() {
		return yVel;
	}

	public void setyVel(int yVel) {
		this.yVel = yVel;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public boolean isForward() {
		return forward;
	}

	public void setForward(boolean forward) {
		this.forward = forward;
	}
}