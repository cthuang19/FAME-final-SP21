public class Player extends MovingAnimatedImage {

    private static final double PLAYER_WIDTH = 10;
    private static final double PLAYER_HEIGHT = 30;
    private static final double PLAYER_MASS = 40;
    private static final double maxLives = 10;
    private static final double maxEnergy = 100;

    /* number of lives of the player, from 0 to 3 at the beginning, up to 10 at the end
    * whenever the player respawns, is set to 3 (no matter the number max of lives) */
    private double lives;
    private double currMaxLives;

    /* level of energy for the defensive force field, from 0 to 30 at the beginning, up to 100 at the end
     * whenever the player respawns, is set to currMaxEnergy */
    private double fieldEnergy;
    private double currMaxEnergy;

    enum PlayerState {IDLE, MOVING, HURT, DEAD, DOOR, RESPAWN};
    private PlayerState state;
    public PlayerState getState() {return this.state;}

    public Player(String n, int x, int y) {
        super(n, x, y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_MASS);
        lives = 3;
        fieldEnergy = currMaxEnergy;
        state = PlayerState.IDLE;
    }

    public Player(String n, int x, int y, int l, int e, PlayerState s) {
        super(n, x, y, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_MASS);
        if (l <= currMaxLives) {lives = l;}
        else {lives = currMaxLives;}
        if (e <= currMaxEnergy) {fieldEnergy = e;}
        else {fieldEnergy = currMaxEnergy;}
        state = s;
    }

    public void update(double time) {

        switch (state) {
            case IDLE :
                break;
            case MOVING :
                break;
            case HURT :
                break;
            case DEAD :
                break;
            case DOOR :
                break;
            case RESPAWN :
                break;
            default:
                state = PlayerState.IDLE;
        }        
    }

}