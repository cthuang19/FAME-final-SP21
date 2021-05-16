import javafx.geometry.Rectangle2D;
import java.lang.*;

public class MovingAnimatedImage extends AnimatedImage implements java.io.Serializable{
	enum CharacterDirection {LEFT, RIGHT, UP, DOWN};
	
	protected double positionX;
    protected double positionY;    
    protected double velocityX;
    protected double velocityY;
    protected double accelerationX;
    protected double accelerationY;
    protected double forceX;
	protected double forceY;
	protected String name;

	protected double width;
    protected double height;
	protected double mass;
	
	protected CharacterDirection direction;
    
	public void setState(MovingAnimatedImage x){
		positionX=x.getPositionX();
		positionY=x.getPositionY();    
		velocityX=x.getVelocityX();
		velocityY=x.getVelocityY();
		accelerationX=0;
		accelerationY=0;
	}	
	
	public MovingAnimatedImage(String n,double x, double y, double w, double h, double m){
		super();
		this.name=n;
		positionX=x;
		positionY=y;
		width=w;
		height=h;
		mass=m;
		velocityX=0;
		velocityY=0;
		direction = CharacterDirection.UP;
	}

	public double getWidth(){
		return width;
	}
	public String getName(){
		return name;
	}
	public double getHeight(){
		return height;
	}
	public double getVelocityX(){
		return velocityX;
	}
	public double getVelocityY(){
		return velocityY;
	}
	public double getPositionX(){
		return positionX;
	}
	public double getPositionY(){
		return positionY;
	}
	public CharacterDirection getDirection() {
		return direction;
	}
	/*
	public String getStrDirection() {
		return switch (direction) {
			case UP -> "up";
			case RIGHT -> "right";
			case DOWN -> "down";
			case LEFT -> "left";
			default -> "up";
		};
	}
	*/
	public void setDirection(CharacterDirection cd) {
		direction = cd;
	}
	public void setPosition(double x, double y)
    {
        positionX = x;
        positionY = y;
    }

    public void setForces(double x, double y)
    {
        forceX = x;
        forceY = y;
    }
	public void addForces(double x, double y)
    {
        forceX += x;
        forceY += y;
    }

    public void addVelocity(double x, double y)
    {
        velocityX += x;
        velocityY += y;
    }

    public void update(double time)
    {
		/*
		accelerationX = forceX/mass;
		accelerationY = forceY/mass;
		
		velocityX = velocityX+accelerationX;
		velocityY = velocityY+accelerationY;
		
		positionX += velocityX ;
		positionY += velocityY ;
        
        if (positionX<0) {
			positionX=0;
			 velocityX=-velocityX;
        }
        if (positionY<0) {
			positionY=0;
			velocityY=-velocityY;
        }
        if (positionX>1600) {
			positionX=1600;
			velocityX=-velocityX;
        }
		if (positionY>1200) {
			positionY=1200;
			velocityY=-velocityY;
		}
		*/
	}
    
    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(positionX,positionY,width,height);
    }

    // MovingAnimatedImage intersects MovingAnimatedImage
    public boolean intersects(MovingAnimatedImage s)
    {
        return s.getBoundary().intersects(this.getBoundary());
    }

    // MovingAnimatedImage intersects AnimatedImage
	public boolean intersects(AnimatedImage a)
	{
		if (a instanceof Asteroid || a instanceof Treasure || a instanceof Door) {
			if (a instanceof Asteroid) {
				Asteroid temp = (Asteroid)(a);
				return temp.getBoundary().intersects(this.getBoundary());
			} else if (a instanceof Door) {
				Door temp = (Door)(a);
				return temp.getBoundary().intersects(this.getBoundary());
			} else {
				Treasure temp = (Treasure)(a);
				return temp.getBoundary().intersects(this.getBoundary());
			}
		}
		return false;
	}

	/**
     * calculate the distance between this object and a given x and y
     * @param x2 the x coordinate of the other position
     * @param y2 the y coordinate of the other position
     * @return the distance between the 2 objects
     */
    public double calculateDistance(double x2, double y2) {
		double result = 0;
		result = Math.pow(this.positionX - x2, 2) + Math.pow(this.positionY - y2, 2); // result = x^2 + y^2
		result = Math.sqrt(result);
        return result;
    }

	//setter functions
	public void setVelocity(double x, double y) {
		velocityX = x;
		velocityY = y;
	}

	public void setAcceleration(double x, double y) {
		accelerationX = x;
		accelerationY = y;
	}

}
