import java.util.ArrayList;
import javafx.scene.image.Image;

public class Player extends MovingAnimatedImage {

    private static final double PLAYER_WIDTH = 23;
    private static final double PLAYER_HEIGHT = 39;
    private static final double PLAYER_MASS = 80;
    private static final double maxLives = 10;
    private static final double maxEnergy = 100;

    private static final double WIDTH_MAX = 1430;
    private static final double HEIGHT_MAX = 800;

    /* number of lives of the player, from 0 to 3 at the beginning, up to 10 at the end
    * whenever the player respawns, is set to 3 (no matter the number max of lives) */
    private double lives;
    private double currMaxLives;

    private double initialX_;
    private double initialY_;

    /* level of energy for the defensive force field, from 0 to 30 at the beginning, up to 100 at the end
     * whenever the player respawns, is set to currMaxEnergy */
    private double fieldEnergy;
    private double currMaxEnergy;

    private long timeStamp;
    private double timePassed;

    enum PlayerState {ALIVE, HURT, DEAD, DOOR};
    private PlayerState state;
    public PlayerState getState() {return this.state;}

    /* sets of frames */
    private Image fIdleLeft[] = new Image[5];
    private Image fIdleRight[] = new Image[5];
    private Image fThrustUp[] = new Image[5];
    private Image fThrustRight[] = new Image[5];
    private Image fThrustDown[] = new Image[5];
    private Image fThrustLeft[] = new Image[5];


    public Player(String n, int x, int y) {
        super(n, x, y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_MASS);
        lives = 3;
        fieldEnergy = currMaxEnergy;
        state = PlayerState.ALIVE;
        initialX_ = x;
        initialY_ = y;
        setPosition(x, y);
        initializeImages();
        setDuration(0.1);
    }

    public Player(String n, int x, int y, int l, int e, PlayerState s) {
        super(n, x, y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_MASS);
        if (l <= currMaxLives) {lives = l;}
        else {lives = currMaxLives;}
        if (e <= currMaxEnergy) {fieldEnergy = e;}
        else {fieldEnergy = currMaxEnergy;}
        state = s;
        initialX_ = 40 * x;
        initialY_ = 40 * y;
        setPosition(40 * x, 40 * y);
        initializeImages();
        setDuration(0.1);
    }
    /**
     * initialize the sets of frames of the player
     */
    public void initializeImages() {
        setDuration(0.2);
        for (int i=0;i<5;i++) fIdleLeft[i] = new Image(".//Images/spaceman/SpacemanIdleLeft/SpacemanIdleLeft_"+i+".png");
        for (int i=0;i<5;i++) fIdleRight[i] = new Image(".//Images/spaceman/SpacemanIdleRight/SpacemanIdleRight_"+i+".png");
        for (int i=0;i<5;i++) fThrustUp[i] = new Image(".//Images/spaceman/SpacemanThrustUp/SpacemanThrustUp_"+i+".png");
        for (int i=0;i<5;i++) fThrustRight[i] = new Image(".//Images/spaceman/SpacemanThrustRight/SpacemanThrustRight_"+i+".png");
        for (int i=0;i<5;i++) fThrustDown[i] = new Image(".//Images/spaceman/SpacemanThrustDown/SpacemanThrustDown_"+i+".png");
        for (int i=0;i<5;i++) fThrustLeft[i] = new Image(".//Images/spaceman/SpacemanThrustLeft/SpacemanThrustLeft_"+i+".png");
        setFrames(fIdleRight);
    }

    /** update which set of frames is used accordng to the key typed
     * @param s a string representing the movement of the player
     */
    public void updateImages(String s) {
        switch (s) {
            case "idle right" :
                setFrames(fIdleRight);
                break;
            case "idle left" :
                setFrames(fIdleLeft);
                break;
            case "thrust up" :
                setFrames(fThrustUp);
                break;
            case "thrust right" :
                setFrames(fThrustRight);
                break;
            case "thrust down" :
                setFrames(fThrustDown);
                break;
            case "thrust left" :
                setFrames(fThrustLeft);
                break;
        }
    }

    /** updates the state of the player
     * @param time
     * @param asteroids
     */
    public void update(double time, ArrayList<Asteroid> asteroids) {

        switch (state) {
            case ALIVE :
                /* if (the player has circa the same coordinates as a door) {
                *   this.state = PlayerState.DOOR;
                *   this.timeStamp = System.currentTimeMillis();
                * }
                * if (the player is around an explosive ghost) {
                *   this.state = PlayerState.HURT;
                *   this.timeStamp = System.currentTimeMillis();
                * } */
                /*
                accelerationX = forceX/PLAYER_MASS;
                accelerationY = forceY/PLAYER_MASS;
                */
                /*
                velocityX = velocityX+accelerationX;
                velocityY = velocityY+accelerationY;
                */

                //save the original x and y
                //just in case if the future position intersect with an asteroid
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

                //if the future position intersects with an asteroid
                //dont move the player
                if (isIntersect) {
                    positionX = originalX;
                    positionY = originalY;
                    break;
                }

                if (positionX<0) {
                    positionX=0;
                    //velocityX=-velocityX;
                }
                if (positionY<0) {
                    positionY=0;
                    //velocityY=-velocityY;
                }
                if (positionX>WIDTH_MAX-PLAYER_WIDTH) {
                    positionX=WIDTH_MAX-PLAYER_WIDTH;
                    //velocityX=-velocityX*0.8;

                }
                if (positionY>HEIGHT_MAX-PLAYER_HEIGHT) {
                    positionY=HEIGHT_MAX-PLAYER_HEIGHT;
                    //velocityY=-velocityY*0.8;
                }

                break;
                
            case HURT :
                this.lives -= 1;
                if (this.lives == 0) {
                    this.state = PlayerState.DEAD;
                    this.timeStamp = System.currentTimeMillis();
                }
                break;
            case DEAD : // wait for some time before respawning at the beginning of the level
                timePassed = (System.currentTimeMillis()-timeStamp)/1000;
                // sets velocity of zero
                this.addVelocity(-this.getVelocityX(), -this.getVelocityY());
                if (timePassed > 10) {
                    this.setPosition(initialX_, initialY_);
                    this.state = PlayerState.ALIVE;
                    this.lives = 3;
                }
                break;
            case DOOR : // wait for the time of the animation of going through the door
                timePassed = (System.currentTimeMillis()-timeStamp)/1000;
                // sets velocity of zero
                this.addVelocity(-this.getVelocityX(), -this.getVelocityY());
                if (timePassed > 10) {
                    // this.setPosition(PuzzleLevel.getStartingX(), PuzzleLevel.getStartingY());
                    this.state = PlayerState.ALIVE;
                }
                break;
            default:
                state = PlayerState.ALIVE;
                break;
        }        
    }

    //getter function
    public double getInitialX() {
        return initialX_;
    }

    public double getInitialY() {
        return initialY_;
    }

}