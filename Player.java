import java.util.ArrayList;

public class Player extends MovingAnimatedImage {

    private static final double PLAYER_WIDTH = 35;
    private static final double PLAYER_HEIGHT = 52;
    private static final double PLAYER_MASS = 80;
    private static final double maxLives = 10;
    private static final double maxEnergy = 100;

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

    public Player(String n, int x, int y) {
        super(n, x, y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_MASS);
        lives = 3;
        fieldEnergy = currMaxEnergy;
        state = PlayerState.ALIVE;
        initialX_ = x;
        initialY_ = y;
    }

    public Player(String n, int x, int y, int l, int e, PlayerState s) {
        super(n, x, y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_MASS);
        if (l <= currMaxLives) {lives = l;}
        else {lives = currMaxLives;}
        if (e <= currMaxEnergy) {fieldEnergy = e;}
        else {fieldEnergy = currMaxEnergy;}
        state = s;
        initialX_ = x;
        initialY_ = y;
    }

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
                accelerationX = forceX/PLAYER_MASS;
                accelerationY = forceY/PLAYER_MASS;

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
                for (Asteroid a : asteroids) {
                    if (this.intersects(a)) {
                        velocityX = -velocityX;
                        velocityY = -velocityY;
                    }
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