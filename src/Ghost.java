import java.util.ArrayList;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import java.lang.*;

public class Ghost extends MovingAnimatedImage {

    private static final double GHOST_WIDTH = 32;
    private static final double GHOST_HEIGHT = 32;
    private static final double GHOST_MASS = 30;

    private static final double RED_SIGHT = 320;
    private static final double BLUE_SIGHT = 64;
    private static final double YELLOW_SIGHT = 512;
    private static final double GREEN_SIGHT = 256;

    /* the colour of the ghost determines its behaviour */
    enum Colour {RED, BLUE, YELLOW, GREEN};
    enum GhostState {PASSIVE, SUSPICIOUS, ACTIVE, EXPLOSIVE};

    private Colour colour;
    private GhostState state;

    private long timeStamp;
    private boolean seesPlayer;

    private double maxX;
    private double maxY;

    private double directionGhostX;
    private double directionGhostY;

    private double startingPoint;

    /* sets of frames */
    private final Image[] fUp = new Image[3];
    private final Image[] fRight = new Image[3];
    private final Image[] fDown = new Image[3];
    private final Image[] fLeft = new Image[3];
    private final Image[] fExplosion = new Image[7];

    public Ghost(String n, int x, int y) {
        this(n,x,y,Colour.RED,GhostState.PASSIVE);
    }

    public Ghost(String n, int x, int y, Colour c, GhostState s) {
        super(n, x, y, GHOST_WIDTH, GHOST_HEIGHT, GHOST_MASS);
        colour = c;
        state = s;
        initializeImages();
        seesPlayer = false;
        startingPoint=x;
    }

    //getter functions
    public Colour getColour() {return this.colour;}
    public GhostState getGhostState() {return this.state;}
    public boolean getSeesPlayer() {return this.seesPlayer;}

    //setter functions
    public void setDimension(double x, double y) {
        maxX = x;
        maxY = y;
    }
    public void setColour(Colour c) {this.colour = c;}
    public void setGhostState(GhostState s) {this.state = s;}

    /**
     * initialize the image frames and duration
     */
    public void initializeImages() {
        setDuration(0.25);
        String ghost_colour = "red";
        switch(colour) {
            case RED:
                ghost_colour = "red";
                break;
            case BLUE:
                ghost_colour = "blue";
                break;
            case YELLOW:
                ghost_colour = "yellow";
                break;
            case GREEN:
                ghost_colour = "green";  
                break;     
        }
        for (int i=0;i<3;i++) fUp[i] = new Image(".//Images/ghosts/" + ghost_colour + "_ghost/"+ ghost_colour+ "_ghost_up_"+i+".png");
        for (int i=0;i<3;i++) fRight[i] = new Image(".//Images/ghosts/" + ghost_colour + "_ghost/"+ ghost_colour+ "_ghost_right_"+i+".png");
        for (int i=0;i<3;i++) fDown[i] = new Image(".//Images/ghosts/" + ghost_colour + "_ghost/"+ ghost_colour+ "_ghost_down_"+i+".png");
        for (int i=0;i<3;i++) fLeft[i] = new Image(".//Images/ghosts/" + ghost_colour + "_ghost/"+ ghost_colour+ "_ghost_left_"+i+".png");

        for (int i=0;i<7;i++) fExplosion[i]=new Image(".//Images/explosion/test_explosion/explosion_"+i+".png");
        setFrames(fUp);
    }

    public void updateImages(String s) {
        switch (s) {
            case "up":
                setFrames(fUp);
                break;
            case "right":
                setFrames(fRight);
                break;
            case "down":
                setFrames(fDown);
                break;    
            case "left":
                setFrames(fLeft);
                break;
            case "explosion":
                setFrames(fExplosion);    
                break;
            
        }
    }

    /**
     * updates the state of the ghost
     * @param time the time
     * @param player the player
     */
    public void update(double time, Player player, ArrayList<Asteroid> asteroids) {
        double timePassed = 0;
        setSeesPlayer(player, asteroids);

        if (this.intersects(player)) {
            state = GhostState.EXPLOSIVE;
            timeStamp = System.currentTimeMillis();
        }

        if (state == GhostState.EXPLOSIVE) {
            setFrames(fExplosion);
        } else {
            setFrames(fUp);
        }

        if (this.colour == Colour.RED) {
            switch (state) {
                case PASSIVE:
                    if (seesPlayer) {
                        state = GhostState.ACTIVE;
                    }
                    break;
                case SUSPICIOUS:
                    timePassed = (System.currentTimeMillis() - timeStamp) / 1000;
                    if (seesPlayer) state = GhostState.ACTIVE;
                    if (timePassed > 4) {
                        state = GhostState.PASSIVE;
                        startingPoint=positionX;
                        velocityX=+2;
                    }
                    break;
                case ACTIVE:
                    if (!seesPlayer) {
                        state = GhostState.SUSPICIOUS;
                        timeStamp = System.currentTimeMillis();
                    }
                    break;
                case EXPLOSIVE:
                    timePassed = (System.currentTimeMillis() - timeStamp) / 1000;
                    if (timePassed > 2) {   // leave time for the explosion animation
                        state = GhostState.PASSIVE;
                    }
                    break;
            }
            switch (state) {
                case PASSIVE :
                    velocityX=(positionX-startingPoint>100)?-2:velocityX;
                    velocityX=(positionX-startingPoint<0)?2:velocityX;
                    velocityY=0;
                    break;
                case ACTIVE :
                    velocityX=directionGhostX*2;
                    velocityY=directionGhostY*2;
                    break;
                case SUSPICIOUS :
                    velocityX=0;
                    velocityY=0;
                    break;
                case EXPLOSIVE:
                    velocityX=0;
                    velocityY=0;
                    break;
            }
            moveGhost(asteroids);
            return;      
        }

        if (this.colour == Colour.BLUE) {
            switch (state) {
                case PASSIVE:
                    if (seesPlayer) {
                        state = GhostState.ACTIVE;
                    }
                    break;
                case ACTIVE:
                    if (!seesPlayer) {
                        state = GhostState.PASSIVE;
                    }
                    if (this.intersects(player)) {
                        state = GhostState.EXPLOSIVE;
                        timeStamp = System.currentTimeMillis();
                    }
                    break;
                case EXPLOSIVE:
                    timePassed = (System.currentTimeMillis() - timeStamp) / 1000;
                    if (timePassed > 2) {   // leave time for the explosion animation
                        state = GhostState.PASSIVE;
                    }
                    break;
            }
            if (state == GhostState.ACTIVE) {
                velocityX=directionGhostX*2;
                velocityY=directionGhostY*2;
            } else {
                return;
            }
            positionX += velocityX ;
            positionY += velocityY ;
            return;
        }
        
        if (this.colour == Colour.YELLOW) {
            switch (state) {
                case PASSIVE:
                    if (seesPlayer) {
                        state = GhostState.ACTIVE;
                    }
                    // patrols in its area
                    break;
                case SUSPICIOUS:
                    timePassed = (System.currentTimeMillis() - timeStamp) / 1000;
                    if (seesPlayer) state = GhostState.ACTIVE;
                    if (timePassed > 4) {
                        state = GhostState.PASSIVE;
                    }
                    break;
                case ACTIVE:
                    if (!seesPlayer) {
                        startingPoint = positionX;
                        velocityX = 0.002;
                        state = GhostState.PASSIVE;
                        timeStamp = System.currentTimeMillis();
                    }
                    if (this.intersects(player)) {
                        state = GhostState.EXPLOSIVE;
                        timeStamp = System.currentTimeMillis();
                    }
                    break;
                case EXPLOSIVE:
                    timePassed = (System.currentTimeMillis() - timeStamp) / 1000;
                    if (timePassed > 2) {   // leave time for the explosion animation
                        startingPoint = positionX;
                        velocityX = 0.002;
                        state = GhostState.PASSIVE;
                    }
                    break;
                default:
                    state = GhostState.PASSIVE;
            }
            switch (state) {
                case PASSIVE :
                    velocityX=(positionX-startingPoint>100)?-0.002:velocityX;
                    velocityX=(positionX-startingPoint<0)?0.002:velocityX;
                    velocityY=0;
                    break;
                case ACTIVE :
                    velocityX=directionGhostX * 0.001;
                    velocityY=directionGhostY * 0.001;
                    // chases player in an area thrice the size of the patrolling area
                    break;
                case SUSPICIOUS :
                    velocityX=0;
                    velocityY=0;
                    break;
                case EXPLOSIVE:
                    velocityX=0;
                    velocityY=0;
                    break;
            }
            moveGhost(asteroids);
            return;    
        }

        if (this.colour == Colour.GREEN) {
            switch (state) {
                case PASSIVE:
                    timePassed = (System.currentTimeMillis() - timeStamp) / 1000;
                    if ((seesPlayer)&&(timePassed > 4)) {
                        state = GhostState.ACTIVE;
                    }
                    // stays hidden behind a rock
                    break;
                case SUSPICIOUS:
                    break;
                case ACTIVE:
                    if (!seesPlayer) {
                        state = GhostState.PASSIVE;
                        timeStamp = System.currentTimeMillis();
                    }
                    if (this.intersects(player)) {
                        state = GhostState.EXPLOSIVE;
                        timeStamp = System.currentTimeMillis();
                    }
                    // gets in the path of the player
                    break;
                case EXPLOSIVE:
                    timePassed = (System.currentTimeMillis() - timeStamp) / 1000;
                    if (timePassed > 2) {   // leave time for the explosion animation
                        state = GhostState.PASSIVE;
                        timeStamp = System.currentTimeMillis();
                    }
                    break;
                default:
                    state = GhostState.PASSIVE;
            }
        }
    }
    /**
     * determines if the player is in the line of sight of the ghost
     * will be used in setSeesPlayer
     * @param player the player
     * @param obstacles all the walls/asteroids that could hide the player to the ghosts
     * @return true if the ghost can see the player else false
     */
    public boolean canSeePlayer(Player player, ArrayList<Asteroid> obstacles) {
        Rectangle2D analysis = new Rectangle2D(	Math.min(player.getPositionX(), positionX),
                                                Math.min(player.getPositionY(), positionY),
                                                Math.abs(positionX-player.getPositionX()),
                                                Math.abs(positionY-player.getPositionY()));

        for (Asteroid obstacle : obstacles) {
            if (analysis.intersects(obstacle.getBoundary())) {
                return false;
            }
        }
        return true;
    }

    /**
     * updates the value of seesPlayer according to the colour of the ghost
     * @param player the player
     * @param obstacles all the walls/asteroids that could hide the player to the ghosts
     */
    public void setSeesPlayer(Player player, ArrayList<Asteroid> obstacles) {
        if (canSeePlayer(player, obstacles)) {
            switch (colour) {
                case RED:       // can see the player up to 8 cells ahead of him
                    seesPlayer = calculateDistance(player.getPositionX(), player.getPositionY()) < RED_SIGHT;
                    break;
                case BLUE:      // can see the player only when he's less than a cell around him
                    seesPlayer = calculateDistance(player.getPositionX(), player.getPositionY()) < BLUE_SIGHT;
                    break;
                case YELLOW:    // can see the player up to 8 cells ahead of him
                    seesPlayer = calculateDistance(player.getPositionX(), player.getPositionY()) < YELLOW_SIGHT;
                    break;
                case GREEN:     // knows the player is here when he's less than 4 cells away, even if hidden
                    seesPlayer = calculateDistance(player.getPositionX(), player.getPositionY()) < GREEN_SIGHT;
                    break;
                default:
                    seesPlayer = false;
            }
        } else {
            seesPlayer = false;
        }
        if (seesPlayer) {
            directionGhostX=(player.getPositionX()-positionX>0)?1:-1;
            directionGhostY=(player.getPositionY()-positionY>0)?1:-1;
        }
    }

    /**
     * move the ghost and prevent it from
     * moving on the top of asteroid
     * @param asteroids the arraylist of asteroids
     */
    private void moveGhost(ArrayList<Asteroid> asteroids) {
        double originalX = positionX;
        double originalY = positionY;
        positionX += velocityX ;
        positionY += velocityY ;
        boolean isIntersect = false;
        for (Asteroid a : asteroids) {
            if (this.intersects(a)) {
                isIntersect = true;
            }
        }

        // if the future position intersects with an asteroid
        // the player bounces
        //TODO: probably move this to MovingAnimatedImage since it is the same
        // as in player
        if (isIntersect) {
            positionX = originalX;
            positionY = originalY;
            velocityX=-velocityX*0.5;
            velocityY=-velocityY*0.5;
            return;
        }

        if (positionX<0) {
            positionX=0;
            velocityX=-velocityX*0.5;
        }
        if (positionY<0) {
            positionY=0;
            velocityY=-velocityY*0.5;
        }
        if (positionX>maxX-GHOST_WIDTH) {
            positionX=maxX-GHOST_WIDTH;
            velocityX=-velocityX*0.5;
        }
        if (positionY>maxY-GHOST_HEIGHT){
            positionY=maxY-GHOST_HEIGHT;
            velocityY=-velocityY*0.5;
        }
    }

}
