import java.awt.*;
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
    private Image fUp[] = new Image[3];
    private Image fRight[] = new Image[3];
    private Image fDown[] = new Image[3];
    private Image fLeft[] = new Image[3];
    private Image fExplosion[] = new Image[7];

    public Ghost(String n, int x, int y) {
        super(n, x, y, GHOST_WIDTH, GHOST_HEIGHT, GHOST_MASS);
        colour = Colour.RED;
        state = GhostState.PASSIVE;
        initializeImages();
        seesPlayer = false;
        startingPoint=x;
    }

    public Ghost(String n, int x, int y, Colour c, GhostState s) {
        super(n, x, y, GHOST_WIDTH, GHOST_HEIGHT, GHOST_MASS);
        colour = c;
        state = s;
        initializeImages();
        seesPlayer = false;
        startingPoint=x;
    }

    public void setDimension(double x, double y) {
        maxX = x;
        maxY = y;
    }

    public void setColour(Colour c) {this.colour = c;}
    public Colour getColour() {return this.colour;}
    public void setGhostState(GhostState s) {this.state = s;}
    public GhostState getGhostState() {return this.state;}
    public boolean getSeesPlayer() {return this.seesPlayer;}

    /**
     * initialize the image frames and duration
     */
    public void initializeImages() {
        setDuration(0.25);
        switch(colour) {
            case RED:
                for (int i=0;i<3;i++) fUp[i] = new Image(".//Images/ghosts/red_ghost/red_ghost_up_"+i+".png");
                for (int i=0;i<3;i++) fRight[i] = new Image(".//Images/ghosts/red_ghost/red_ghost_right_"+i+".png");
                for (int i=0;i<3;i++) fDown[i] = new Image(".//Images/ghosts/red_ghost/red_ghost_down_"+i+".png");
                for (int i=0;i<3;i++) fLeft[i] = new Image(".//Images/ghosts/red_ghost/red_ghost_left_"+i+".png");
                break;
            case BLUE:
                for (int i=0;i<3;i++) fUp[i] = new Image(".//Images/ghosts/blue_ghost/blue_ghost_up_"+i+".png");
                for (int i=0;i<3;i++) fRight[i] = new Image(".//Images/ghosts/blue_ghost/blue_ghost_right_"+i+".png");
                for (int i=0;i<3;i++) fDown[i] = new Image(".//Images/ghosts/blue_ghost/blue_ghost_down_"+i+".png");
                for (int i=0;i<3;i++) fLeft[i] = new Image(".//Images/ghosts/blue_ghost/blue_ghost_left_"+i+".png");
                break;
            case YELLOW:
                for (int i=0;i<3;i++) fUp[i] = new Image(".//Images/ghosts/yellow_ghost/yellow_ghost_up_"+i+".png");
                for (int i=0;i<3;i++) fRight[i] = new Image(".//Images/ghosts/yellow_ghost/yellow_ghost_right_"+i+".png");
                for (int i=0;i<3;i++) fDown[i] = new Image(".//Images/ghosts/yellow_ghost/yellow_ghost_down_"+i+".png");
                for (int i=0;i<3;i++) fLeft[i] = new Image(".//Images/ghosts/yellow_ghost/yellow_ghost_left_"+i+".png");
                break;
            case GREEN:
                for (int i=0;i<3;i++) fUp[i] = new Image(".//Images/ghosts/green_ghost/green_ghost_up_"+i+".png");
                for (int i=0;i<3;i++) fRight[i] = new Image(".//Images/ghosts/green_ghost/green_ghost_right_"+i+".png");
                for (int i=0;i<3;i++) fDown[i] = new Image(".//Images/ghosts/green_ghost/green_ghost_down_"+i+".png");
                for (int i=0;i<3;i++) fLeft[i] = new Image(".//Images/ghosts/green_ghost/green_ghost_left_"+i+".png");
                break;
            default:
                for (int i=0;i<3;i++) fUp[i] = new Image(".//Images/ghosts/red_ghost/red_ghost_up_"+i+".png");
                for (int i=0;i<3;i++) fRight[i] = new Image(".//Images/ghosts/red_ghost/red_ghost_right_"+i+".png");
                for (int i=0;i<3;i++) fDown[i] = new Image(".//Images/ghosts/red_ghost/red_ghost_down_"+i+".png");
                for (int i=0;i<3;i++) fLeft[i] = new Image(".//Images/ghosts/red_ghost/red_ghost_left_"+i+".png");
                break;
        }
        for (int i=0;i<7;i++) fExplosion[i]=new Image(".//Images/explosion/test_explosion/explosion_"+i+".png");
        setFrames(fUp);
    }

    public void updateImages(String s) {
        switch (s) {
            case "up" :
                setFrames(fUp);
                break;
            case "right" :
                setFrames(fRight);
                break;
            case "down" :
                setFrames(fDown);
                break;
            case "left" :
                setFrames(fLeft);
                break;
            case "explosion" :
                setFrames(fExplosion);
                break;
        }
    }

    /**
     * updates the state of the ghost
     * @param time
     * @param player
     */
    //@Override
    public void update(double time, Player player, ArrayList<Asteroid> asteroids) {
        double timePassed = 0;
        setSeesPlayer(player, asteroids);

        if (this.intersects(player)) {
            state = GhostState.EXPLOSIVE;
            timeStamp = System.currentTimeMillis();
        }

        if (this.colour == Colour.RED) {
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
                        startingPoint=positionX;
                        velocityX=+2;
                    }
                    break;
                case ACTIVE:
                    if (!seesPlayer) {
                        state = GhostState.SUSPICIOUS;
                        timeStamp = System.currentTimeMillis();
                    }
                    // chase player in its patrolling area
                    break;
                case EXPLOSIVE:
                    timePassed = (System.currentTimeMillis() - timeStamp) / 1000;
                    if (timePassed > 4) {   // leave time for the explosion animation
                        state = GhostState.PASSIVE;
                    }
                    break;
            }        
                /*    
                default:
                    state = GhostState.PASSIVE;
                */
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
            }
            positionX += velocityX ;
            positionY += velocityY ;
            return;
        }

        if (this.colour == Colour.BLUE) {
            switch (state) {
                case PASSIVE:
                    if (seesPlayer) {
                        state = GhostState.ACTIVE;
                    }
                    break;
                // case SUSPICIOUS:       // blue doesn't go in this state
                //    break;
                case ACTIVE:
                    if (!seesPlayer) {
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
                    if (timePassed > 4) {   // leave time for the explosion animation
                        state = GhostState.PASSIVE;
                    }
                    break;
                default:
                    state = GhostState.PASSIVE;
            }
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
                        state = GhostState.PASSIVE;
                        timeStamp = System.currentTimeMillis();
                    }
                    if (this.intersects(player)) {
                        state = GhostState.EXPLOSIVE;
                        timeStamp = System.currentTimeMillis();
                    }
                    // chases player in an area thrice the size of the patrolling area
                    break;
                case EXPLOSIVE:
                    timePassed = (System.currentTimeMillis() - timeStamp) / 1000;
                    if (timePassed > 4) {   // leave time for the explosion animation
                        state = GhostState.PASSIVE;
                    }
                    break;
                default:
                    state = GhostState.PASSIVE;
            }
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
                    if (timePassed > 4) {   // leave time for the explosion animation
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
     * @param player
     * @param obstacles all the walls/asteorids that could hide the player to the ghosts
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

    /* public boolean canSeePlayer(Player player, ArrayList<Asteroid> asteroids) {
        Rectangle2D analysis = new Rectangle2D(	Math.min(player.getPositionX(), positionX),
                Math.min(player.getPositionY(), positionY),
                Math.abs(positionX-player.getPositionX()),
                Math.abs(positionY-player.getPositionY()));

        for (Asteroid a : asteroids) {
            if (analysis.intersects(a.getBoundary())) {
                return false;
            }
        }
        return true;
    } */

    /**
     * updates the value of seesPlayer according to the colour of the ghost
     * @param player
     * @param obstacles all the walls/asteorids that could hide the player to the ghosts
     */
    public void setSeesPlayer(Player player, ArrayList<Asteroid> obstacles) {
        if (canSeePlayer(player, obstacles)) {
            switch (colour) {
                case RED:       // can see the player up to 8 cells ahead of him
                    if (calculateDistance(player.getPositionX(), player.getPositionY()) < RED_SIGHT) {
                       seesPlayer = true;
                    } else {
                        seesPlayer = false;
                    }
                    break;
                    /*
                    if ((Math.abs(positionX - player.getPositionX()) < 512) ||
                        (Math.abs(positionY - player.getPositionY()) < 512)) {
                        switch(this.direction_) {
                            case UP :
                                if (positionY > player.getPositionY()) {seesPlayer = true;}
                                else {seesPlayer = false;}
                                break;
                            case RIGHT :
                                if (positionX < player.getPositionX()) {seesPlayer = true;}
                                else {seesPlayer = false;}
                                break;
                            case DOWN :
                                if (positionY < player.getPositionY()) {seesPlayer = true;}
                                else {seesPlayer = false;}
                                break;
                            case LEFT :
                                if (positionX > player.getPositionX()) {seesPlayer = true;}
                                else {seesPlayer = false;}
                                break;
                            default :
                                seesPlayer = false;
                        }
                    } else {
                        seesPlayer = false;
                    }
                    break;
                    */
                case BLUE:      // can see the player only when he's less than a cell around him
                    /*
                    if ((Math.abs(positionX - player.getPositionX()) < 64) ||
                        (Math.abs(positionY - player.getPositionY()) < 64)) {
                        seesPlayer = true;
                    } else {
                        seesPlayer = false;
                    }
                    */
                    if (calculateDistance(player.getPositionX(), player.getPositionY()) < BLUE_SIGHT) {
                        seesPlayer = true;
                    } else {
                        seesPlayer = false;
                    }
                    break;
                case YELLOW:    // can see the player up to 8 cells ahead of him
                    if ((Math.abs(positionX - player.getPositionX()) < 512) ||
                        (Math.abs(positionY - player.getPositionY()) < 512)) {
                        switch(this.direction_) {
                            case UP :
                                if (positionY > player.getPositionY()) {seesPlayer = true;}
                                else {seesPlayer = false;}
                                break;
                            case RIGHT :
                                if (positionX < player.getPositionX()) {seesPlayer = true;}
                                else {seesPlayer = false;}
                                break;
                            case DOWN :
                                if (positionY < player.getPositionY()) {seesPlayer = true;}
                                else {seesPlayer = false;}
                                break;
                            case LEFT :
                                if (positionX > player.getPositionX()) {seesPlayer = true;}
                                else {seesPlayer = false;}
                                break;
                            default :
                                seesPlayer = false;
                        }
                    } else {
                        seesPlayer = false;
                    }
                    break;
                case GREEN:     // knows the player is here when he's less than 4 cells away, even if hidden
                    if ((Math.abs(positionX - player.getPositionX()) < 256) ||
                        (Math.abs(positionY - player.getPositionY()) < 256)) {
                        seesPlayer = true;
                    } else {
                        seesPlayer = false;
                    }
                    break;
                default:
                    seesPlayer = false;
            }
        } else {
            seesPlayer = false;

            //set the direction of the ghost to the direction to the player
            //if it can see the player
            /*
            if (this.colour == Colour.GREEN) {
                // same block as in the switch case
                // maybe find a prettier way to put it
                if ((Math.abs(positionX - player.getPositionX()) < 256) ||
                    (Math.abs(positionY - player.getPositionY()) < 256)) {
                    seesPlayer = true;
                } else {
                    seesPlayer = false;
                }
            }
            */
        }
        if (seesPlayer) {
            directionGhostX=(player.getPositionX()-positionX>0)?1:-1;
            directionGhostY=(player.getPositionY()-positionY>0)?1:-1;
        }
    }

}
