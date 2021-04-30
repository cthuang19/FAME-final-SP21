import java.util.ArrayList;
import javafx.scene.input.KeyEvent;

public class Puzzle {

    /* the width of each array showing on the screen*/
    public static final int CELL_WIDTH = 64;

    protected static final int EMPTY_CELL = 0;
    protected static final int ROCK_CELL = 1;
    protected static final int PLAYER_CELL = 2;
    protected static final int TREASURE_CELL = 7;

    /* the type and "level" of the current puzzle */
    protected int type;
    protected int level;
    protected double dimensionX;
    protected double dimensionY;

    protected ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();

    protected Player player_;
    protected Treasure treasure_;

    protected boolean isCompleted;

    public Puzzle() {
        type = 1;
        level = 1;
        FileReader fr = new FileReader(".//game_environment_files/puzzle_level_1_1.txt");
        initializeArray(fr.allLines());
        isCompleted = false;
        dimensionX = 1430;
        dimensionY = 800;
    }

    public Puzzle(int tp, int lvl) {
        type = tp;
        level = lvl;
        FileReader fr = new FileReader(".//game_environment_files/puzzle_level_"+type+"_"+level+".txt");
        initializeArray(fr.allLines());
        isCompleted = false;
        dimensionX = 1430;
        dimensionY = 800;
    }

    /**
     * helper function
     * help initialize the game_array with the according
     * arraylist of string that was read in the file
     * @param s an arraylist of string
     */
    private void initializeArray(ArrayList<String> s) {
        dimensionX = s.get(0).length() * CELL_WIDTH;
        dimensionY = s.size() * CELL_WIDTH;
        for (int i = 0; i < s.size(); i++) {
            for (int c = 0; c < s.get(i).length(); c++) {
                String cell = s.get(i).substring(c, c + 1);
                int cell_int = 0;
                try {
                    cell_int = Integer.parseInt(cell);
                } catch (NumberFormatException e) {
                    System.out.println("Number Format Exception, using default value");
                    cell_int = 0;
                }

                //initialize each array according to the document
                switch(cell_int) {
                    case ROCK_CELL:
                        asteroids.add(new Asteroid(c * CELL_WIDTH, i * CELL_WIDTH));
                        break;
                    case PLAYER_CELL:
                        player_ = new Player("player", c * CELL_WIDTH, i * CELL_WIDTH, 3, 30, Player.PlayerState.ALIVE);
                        break;
                    case TREASURE_CELL:
                        treasure_ = new Treasure(c * CELL_WIDTH, i * CELL_WIDTH);
                        break;
                }
            }
        }
        player_.setDimension(dimensionX, dimensionY);
    }

    /**
     * move the player according to the keycode
     * @param code the keycode that represents which key was pressed
     */
    public void movePlayer(ArrayList<String> input) {
        // basic movement of the player
        if (input.contains("W")) {      // Z on AZERTY keyboard
            //player_.addForces(0, -5);   // UP
            player_.setVelocity(0, -5);
            player_.setCharacterDirection(Player.CharacterDirection.UP);
            player_.updateImages("thrust up");
        }
        if (input.contains("D")) {
            //player_.addForces(5, 0);    // RIGHT
            player_.setVelocity(5, 0);
            player_.setCharacterDirection(Player.CharacterDirection.RIGHT);
            player_.updateImages("thrust right");
        }
        if (input.contains("S")) {
            //player_.addForces(0, 5);    // DOWN
            player_.setVelocity(0, 5);
            player_.setCharacterDirection(Player.CharacterDirection.DOWN);
            player_.updateImages("thrust down");
        }
        if (input.contains("A")) {      // Q on AZERTY keyboard
            //player_.addForces(-5, 0);   // LEFT
            player_.setVelocity(-5, 0);
            player_.setCharacterDirection(Player.CharacterDirection.LEFT);
            player_.updateImages("thrust left");
        }

        //add or remove shield to the player
        if (input.contains("K")) {
            player_.setShieldOn(true);
            if (player_.getShieldOn()) {
                player_.updateImages("shield");
            } else {
                player_.updateImages("idle right");
            }
        } else {
            player_.setShieldOn(false);
            player_.updateImages("idle right");
        }

        if (input.contains(null)) {
            if (player_.getDirection() == Player.CharacterDirection.UP) {
                player_.updateImages("idle right");
            }
            if (player_.getDirection() == Player.CharacterDirection.RIGHT) {
                player_.updateImages("idle right");
            }
            if (player_.getDirection() == Player.CharacterDirection.DOWN) {
                player_.updateImages("idle left");
            }
            if (player_.getDirection() == Player.CharacterDirection.LEFT) {
                player_.updateImages("idle left");
            }
            player_.setShieldOn(false);
        }
    }
    /**
     * update each moving element in the game
     * @param t the time
     */
    public void update_time(double t) {
        // empty list to use for the update
        ArrayList<Ghost> empty_ghosts = new ArrayList<Ghost>();
        player_.update(t, asteroids, empty_ghosts, treasure_);
    }

    //getter function
    public ArrayList<Asteroid> getAsteroids() { return asteroids;}

    public Player getPlayer() { return player_;}

    public Treasure getTreasure() { return treasure_;}

    public double getDimensionX() {return dimensionX;}

    public double getDimensionY() {return dimensionY;}

    public double getPlayerLives() {return player_.getLives();}

    public double getPlayerFieldEnergy() {return player_.getFieldEnergy();}

    public boolean getIsCompleted() {return isCompleted;}

    public int getType() {return type;}

}