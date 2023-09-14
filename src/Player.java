public class Player {
	
	private int xPos, yPos;
	private int direction;
	private int xVel, yVel;
	private int turn;
	private boolean forward, backward, left, right;
	private boolean inScope;
	private int health;
	
	public Player(){
		xPos = 0;
		yPos = 0;
		direction = 0;
		xVel = 0;
		yVel = 0;
		turn = 0;
		forward = true;
		inScope = false;
		health = 5;
	}
	
	public Player(int xPos, int yPos, int direction) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.direction = direction;
		xVel = 0;
		yVel = 0;
		turn = 0;
		forward = true;
		inScope = false;
		health = 5;
	}
	
	public void move(Map map) {
		if(forward) {
			if(Math.abs(xVel)+Math.abs(yVel)>0 && Raycast.getDistance(this, map)>Math.sqrt((xVel*xVel)+(yVel*yVel))+1) {
				xPos += xVel;
				yPos += yVel;
			} else {
				if(map.getBoard()[(int)(yPos/100.0)][(int)((xPos+xVel)/100.0)]==0) {
					xPos+=xVel;
				} else if(map.getBoard()[(int)((yPos+yVel)/100.0)][(int)(xPos/100.0)]==0) {
					yPos+=yVel;
				}
			}
		} 
		if(backward) {
			direction = direction+180;
			if(Math.abs(xVel)+Math.abs(yVel)>0 && Raycast.getDistance(this, map)>Math.sqrt((xVel*xVel)+(yVel*yVel))+1) {
				direction = direction-180;
				xPos += xVel;
				yPos += yVel;
			} else {
				if(map.getBoard()[(int)(yPos/100.0)][(int)((xPos+xVel)/100.0)]==0) {
					direction = direction-180;
					xPos+=xVel;
				} else if(map.getBoard()[(int)((yPos+yVel)/100.0)][(int)(xPos/100.0)]==0) {
					direction = direction-180;
					yPos+=yVel;
				} else {
					direction = direction-180;
				}
			}
		}
		if(left) {
			direction = direction+90;
			if(Math.abs(xVel)+Math.abs(yVel)>0 && Raycast.getDistance(this, map)>Math.sqrt((xVel*xVel)+(yVel*yVel))+1) {
				direction = direction-90;
				xPos += xVel;
				yPos += yVel;
			} else {
				if(map.getBoard()[(int)(yPos/100.0)][(int)((xPos+xVel)/100.0)]==0) {
					direction = direction-90;
					xPos+=xVel;
				} else if(map.getBoard()[(int)((yPos+yVel)/100.0)][(int)(xPos/100.0)]==0) {
					direction = direction-90;
					yPos+=yVel;
				} else {
					direction = direction-90;
				}
			}
		}
		if(right) {
			direction = direction-90;
			if(Math.abs(xVel)+Math.abs(yVel)>0 && Raycast.getDistance(this, map)>Math.sqrt((xVel*xVel)+(yVel*yVel))+1) {
				direction = direction+90;
				xPos += xVel;
				yPos += yVel;
			} else {
				if(map.getBoard()[(int)(yPos/100.0)][(int)((xPos+xVel)/100.0)]==0) {
					direction = direction+90;
					xPos+=xVel;
				} else if(map.getBoard()[(int)((yPos+yVel)/100.0)][(int)(xPos/100.0)]==0) {
					direction = direction+90;
					yPos+=yVel;
				} else {
					direction = direction+90;
				}
			}
		}
	}
	
	public void turn() {
		if(inScope) 
			direction += turn/2;
		else
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

	public boolean isBackward() {
		return backward;
	}

	public void setBackward(boolean backward) {
		this.backward = backward;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isInScope() {
		return inScope;
	}

	public void setInScope(boolean inScope) {
		this.inScope = inScope;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
}