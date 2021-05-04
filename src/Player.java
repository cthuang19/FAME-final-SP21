import java.util.ArrayList;
import javafx.scene.image.Image;

public class Player extends MovingAnimatedImage {

    private static final double PLAYER_WIDTH = 23;
    private static final double PLAYER_HEIGHT = 39;
    private static final double PLAYER_MASS = 80;
    private static final double maxLives = 10;
    private static final double maxEnergy = 100;

    /* number of lives of the player, from 0 to 3 at the beginning, up to 10 at the end
    * whenever the player respawns, is set to 3 (no matter the number max of lives) */
    private double lives;
    private double currMaxLives;

    private double initialX_;
    private double initialY_;

    private double maxX;
    private double maxY;

    /* level of energy for the defensive force field, from 0 to 30 at the beginning, up to 100 at the end
     * whenever the player respawns, is set to currMaxEnergy */
    private double fieldEnergy;
    private double currMaxEnergy = 30;

    private long timeStamp;
    private double timePassed;
    private long shieldTimeStamp;

    enum PlayerState {ALIVE, HURT, DEAD, DOOR};
    private PlayerState state;
    private boolean invulnerable;
    private boolean isGameCompleted;
    private boolean shieldOn;
    private boolean beforeDoor;

    /* the door before which the player was last */
    private Door currentDoor;

    /* sets of frames */
    private Image fIdleLeft[] = new Image[5];
    private Image fIdleRight[] = new Image[5];
    private Image fThrustUp[] = new Image[5];
    private Image fThrustRight[] = new Image[5];
    private Image fThrustDown[] = new Image[5];
    private Image fThrustLeft[] = new Image[5];
    private Image explosions[] = new Image[7];
    private Image shield[] = new Image[1];


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

        //just default value
        //change if needed (or change when calling setDimension)
        maxX = 1080;
        maxY = 800;
        isGameCompleted = false;

    }

    public Player(String n, int x, int y, int l, int e, PlayerState s) {
        super(n, x, y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_MASS);
        //if (l <= currMaxLives) {lives = l;}
        //else {lives = currMaxLives;}
        lives = l;
        if (e <= currMaxEnergy) {fieldEnergy = e;}
        else {fieldEnergy = currMaxEnergy;}
        state = s;
        initialX_ = x;
        initialY_ = y;
        setPosition(x, y);
        initializeImages();
        setDuration(0.1);
        //just default value
        //change if needed (or change when calling setDimension)
        maxX = 1080;
        maxY = 800;
        isGameCompleted = false;
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
        for (int i=0;i<7;i++) explosions[i] = new Image(".//Images/explosion/test_explosion/explosion_" + i + ".png");
        for (int i=0;i<1;i++) shield[i] = new Image(".//Images/shield.png");
        setFrames(fIdleRight);
    }

    public void setDimension(double x, double y) {
        maxX = x;
        maxY = y;
    }

    /** update which set of frames is used according to the key typed
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
            // TODO : create frames with shield superposed to all the already existing frames
            case "shield" :
                setFrames(shield);
                break;
        }
    }

    public void setShieldOn(boolean b) {
        if (fieldEnergy > 0) {
            shieldOn = b;
            if (shieldOn) {
                if (System.currentTimeMillis()-shieldTimeStamp > 2) {
                    shieldTimeStamp = System.currentTimeMillis();
                }
            }
        } else {
            shieldOn = false;
        }
    }

    public void updateFieldEnergy() {
        if (fieldEnergy > 0) {
            if (shieldOn) {
                fieldEnergy -= 1;
                shieldTimeStamp = System.currentTimeMillis();
            }
        }
    }

    /** updates the state of the player
     * @param time
     * @param asteroids
     */
    public void update(double time, ArrayList<Asteroid> asteroids, ArrayList<Ghost> ghosts, Treasure treasure) {

        /*if ((System.currentTimeMillis() - shieldTimeStamp) / 1000 > 1)*/
        updateFieldEnergy();
        switch (state) {
            case ALIVE :
                if (beforeDoor) {
                    this.state = PlayerState.DOOR;
                    this.timeStamp = System.currentTimeMillis();
                }

                accelerationX = forceX/PLAYER_MASS;
                accelerationY = forceY/PLAYER_MASS;

                velocityX = velocityX+accelerationX;
                velocityY = velocityY+accelerationY;

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

                // if the future position intersects with an asteroid
                // the player bounces
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
                if (positionX>maxX-PLAYER_WIDTH) {
                    positionX=maxX-PLAYER_WIDTH;
                    velocityX=-velocityX*0.5;

                }
                if (positionY>maxY-PLAYER_HEIGHT) {
                    positionY=maxY-PLAYER_HEIGHT;
                    velocityY=-velocityY*0.5;
                }

                // check if the player intersects with ghost 
                if (!(this.shieldOn)) {
                    if (this.invulnerable) {
                        timePassed = (System.currentTimeMillis() - timeStamp) / 1000;
                        if (timePassed > 5) {
                            this.invulnerable = false;
                        }
                    } else {
                        for (Ghost g : ghosts) {
                            if (this.intersects(g)) {
                                this.state = PlayerState.HURT;
                                //System.out.println("hurt");
                                this.timeStamp = System.currentTimeMillis();
                            }
                        }
                    }
                }

                if (this.intersects(treasure)) {
                    isGameCompleted = true;
                    return;
                }

                break;
                
            case HURT :
                this.lives -= 1;
                //setFrames(explosions);
                if (this.lives == 0) {
                    this.state = PlayerState.DEAD;
                    this.timeStamp = System.currentTimeMillis();
                }
                else {
                    this.state = PlayerState.ALIVE;
                    invulnerable = true;
                    this.timeStamp = System.currentTimeMillis();
                }
                break;
            case DEAD : // wait for some time before respawning at the beginning of the level
                timePassed = (System.currentTimeMillis()-timeStamp)/1000;
                this.setVelocity(0, 0);
                if (timePassed > 3) {
                    this.setPosition(initialX_, initialY_);
                    this.state = PlayerState.ALIVE;
                    this.lives = 3;
                }
                break;
/*            case DOOR : // wait for the time of the animation of going through the door
                timePassed = (System.currentTimeMillis()-timeStamp)/1000;
                this.setVelocity(0, 0);
                if (timePassed > 3) {
                    this.state = PlayerState.ALIVE;
                }
                break;*/
            default:
                state = PlayerState.ALIVE;
                break;
        }        
    }

    //getter function
    public double getInitialX() { return initialX_;}

    public double getInitialY() {return initialY_;}

    public PlayerState getState() {return state;}

    public double getLives() {return lives;}

    public double getFieldEnergy() {return fieldEnergy;}

    public boolean getShieldOn() {return shieldOn;}

    public boolean getBeforeDoor() {return beforeDoor;}

    public void setBeforeDoor(ArrayList<Door> doors) {
        for (Door d : doors) {
            if (this.intersects(d) && !(d.getIsCompleted())) {
                beforeDoor = true;
                this.currentDoor = d;
                return;
            } else {
                beforeDoor = false;
            }
        }
    }

    public Door getCurrentDoor() {return currentDoor;}
}