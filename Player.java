public class Player extends MovingAnimatedImage {

    private final double width = 10;
    private final double height = 30;
    private final double mass = 40;

    /* number of lives of the player, from 0 to 3 at the beginning, up to 10 at the end
    * whenever the player respawns, is set to 3 (no matter the number max of lives) */
    private double lives;
    private double currMaxLives;
    private final double maxLives = 10;

    /* level of energy for the defensive force field, from 0 to 30 at the beginning, up to 100 at the end
     * whenever the player respawns, is set to currMaxEnergy */
    private double fieldEnergy;
    private double currMaxEnergy;
    private final double maxEnergy = 100;

    enum State {IDLE, MOVING, HURT, DEAD, DOOR, RESPAWN};
    private State state;
    public State getState() {return this.state;}

    public void Player(String n, int x, int y) {
        super(n, x, y, width, height, mass);
        lives = 3;
        fieldEnergy = currMaxEnergy;
        state = State.IDLE;
    }

    public void Player(String n, int x, int y, int l, int e, State s) {
        super(n, x, y, width, height, mass);
        if (l <= currMaxLives) {lives = l;}
        else {lives = currMaxLives;}
        if (e <= currMaxEnergy) {fieldEnergy = e;}
        else {energy = currMaxEnergy;}
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
    }

}