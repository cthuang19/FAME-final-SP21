import java.util.ArrayList;
import javafx.geometry.Rectangle2D;
import java.lang.*;

public class Ghost extends MovingAnimatedImage {

    private final double GHOST_WIDTH = 7;
    private final double GHOST_HEIGHT = 20;
    private final double GHOST_MASS = 30;

    /* the colour of the ghost determine its behaviour */
    enum Colour {RED, BLUE, ORANGE, GREEN};
    enum GhostState {PASSIVE, SUSPICIOUS, ACTIVE, EXPLOSIVE};

    private Colour colour;
    private GhostState state;

    private long timeStamp;
    private boolean seesPlayer;

    public Ghost(String n, int x, int y) {
        super(n, x, y, GHOST_WIDTH, GHOST_HEIGHT, GHOST_MASS);
        colour = Colour.RED;
        state = GhostState.PASSIVE;
    }

    public Ghost(String n, int x, int y, Colour c, GhostState s) {
        super(n, x, y, GHOST_WIDTH, GHOST_HEIGHT, GHOST_MASS);
        colour = c;
        state = s;
    }

    public void setColour(Colour c) {this.colour = c;}
    public Colour getColour() {return this.colour;}
    public void setGhostState(GhostState s) {this.state = s;}
    public GhostState getGhostState() {return this.state;}


    /**
     * updates the state of the ghost
     * @param time
     */
    @Override
    public void update(double time) {

        if (this.colour == Colour.RED) {
            switch (state) {
                case PASSIVE:
                    if (seesPlayer) {
                        state = GhostState.ACTIVE;
                    }
                    break;
                case SUSPICIOUS:
                    double timePassed = (System.currentTimeMillis() - timeStamp) / 1000;
                    if (seesPlayer) state = GhostState.ACTIVE;
                    if (timePassed > 4) {
                        state = GhostState.PASSIVE;
                        //startingPoint = positionX;
                        velocityX = +2;
                    }
                    break;
                case ACTIVE:
                    if (!seesPlayer) {
                        state = GhostState.SUSPICIOUS;
                        timeStamp = System.currentTimeMillis();
                    }
                    break;
                case EXPLOSIVE:
                    break;
                default:
                    state = GhostState.PASSIVE;
            }
        }

        if (this.colour == Colour.BLUE) {
            switch (state) {
                case PASSIVE:
                    if (seesPlayer) {
                        state = GhostState.ACTIVE;
                    }
                    break;
                case SUSPICIOUS:
                    break;
                case ACTIVE:
                    break;
                case EXPLOSIVE:
                    break;
                default:
                    state = GhostState.PASSIVE;
            }
        }

        if (this.colour == Colour.ORANGE) {
            switch (state) {
                case PASSIVE:
                    if (seesPlayer) {
                        state = GhostState.ACTIVE;
                    }
                    break;
                case SUSPICIOUS:
                    break;
                case ACTIVE:
                    break;
                case EXPLOSIVE:
                    break;
                default:
                    state = GhostState.PASSIVE;
            }
        }

        if (this.colour == Colour.GREEN) {
            switch (state) {
                case PASSIVE:
                    if (seesPlayer) {
                        state = GhostState.ACTIVE;
                    }
                    break;
                case SUSPICIOUS:
                    break;
                case ACTIVE:
                    break;
                case EXPLOSIVE:
                    break;
                default:
                    state = GhostState.PASSIVE;
            }
        }
    }
    /**
     * determines if the player is in the line of sight of the ghost
     * will be used in setSeePlayer
     * @param player
     * @param obstacles all the walls/asteorids that could hide the player to the ghosts
     * @return true if the ghost can see the player else false
     */
    public boolean canSeePlayer(MovingAnimatedImage player, ArrayList<MovingAnimatedImage> obstacles) {
        Rectangle2D analysis = new Rectangle2D(	Math.min(player.getPositionX(), positionX),
                                                Math.min(player.getPositionY(), positionY),
                                                Math.abs(positionX-player.getPositionX()),
                                                Math.abs(positionY-player.getPositionY()));

        for (MovingAnimatedImage obstacle : obstacles)
        {
            if (analysis.intersects(obstacle.getBoundary())) {
                return false;
            }
        }
        return true;
    }

    /**
     * updates the value of seesPlayer
     * @param player
     * @param obstacles all the walls/asteorids that could hide the player to the ghosts
     */
    public void setSeesPlayer(MovingAnimatedImage player, ArrayList<MovingAnimatedImage> obstacles) {

        if (canSeePlayer(player, obstacles))
            switch (colour) {
                case RED:
                    if ((Math.abs(positionX - player.getPositionX()) > 200) &&
                        (Math.abs(positionY - player.getPositionY()) > 200)) {
                        seesPlayer = false;
                    } else {
                        seesPlayer = true;
                    }
                    break;
                case BLUE:
                    if ((Math.abs(positionX - player.getPositionX()) > 50) &&
                        (Math.abs(positionY - player.getPositionY()) > 50)) {
                        seesPlayer = false;
                    } else {
                        seesPlayer = true;
                    }
                    break;
                case ORANGE:
                    if ((Math.abs(positionX - player.getPositionX()) > 200) &&
                        (Math.abs(positionY - player.getPositionY()) > 200)) {
                        seesPlayer = false;
                    } else {
                        seesPlayer = true;
                    }
                    break;
                case GREEN:
                    if ((Math.abs(positionX - player.getPositionX()) > 100) &&
                        (Math.abs(positionY - player.getPositionY()) > 100)) {
                        seesPlayer = false;
                    } else {
                        seesPlayer = true;
                    }
                    break;
                default:
                    seesPlayer = false;
            }
    }

}