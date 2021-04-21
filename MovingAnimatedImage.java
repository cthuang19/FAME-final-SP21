import javafx.geometry.Rectangle2D;

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
	
	protected CharacterDirection direction_;
    
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
		direction_ = CharacterDirection.UP;
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
		return direction_;
	}
	public String getStrDirection() {
		switch (direction_) {
			case UP : return "up";
			case RIGHT : return "right";
			case DOWN : return "down";
			case LEFT : return "left";
			default : return "up";
		}
	}
	public void setCharacterDirection(CharacterDirection cd) {
		direction_ = cd;
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
    // MovingAnimatedImage intersects Asteroid
	public boolean intersects(Asteroid a)
	{
		return a.getBoundary().intersects(this.getBoundary());
	}




}
