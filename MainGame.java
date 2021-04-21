import java.util.ArrayList;
import javafx.scene.input.KeyEvent;

public class MainGame {

    /* the width of each array showing on the screen*/
    public static final int CELL_WIDTH = 64;

    private static final int EMPTY_CELL = 0;
    private static final int ROCK_CELL = 1;
    private static final int PLAYER_CELL = 2;
    private static final int RED_GHOST_CELL = 3;
    private static final int BLUE_GHOST_CELL = 4;
    private static final int YELLOW_GHOST_CELL = 5;
    private static final int GREEN_GHOST_CELL = 6;
    private static final int TREASURE_CELL = 7;
    private static final int DOOR_CELL = 8;

    private static final int X_MAX = 36;
    private static final int Y_MAX = 20;

    /* the array that represents each location on the game page */
    //private int[][] game_array_ = new int[40][30];
    private int[][] game_array_ = new int[X_MAX][Y_MAX];
    private AnimatedImage[][] display_array_ = new AnimatedImage[36][20];

    /* the level of the current game */
    private int level_;

    /* lists that contain all the Ghosts of given level */
    private ArrayList<Ghost> redGhosts = new ArrayList<Ghost>();
    private ArrayList<Ghost> blueGhosts = new ArrayList<Ghost>();
    private ArrayList<Ghost> yellowGhosts = new ArrayList<Ghost>();
    private ArrayList<Ghost> greenGhosts = new ArrayList<Ghost>();
    private ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();

    private Player player_;
    private Treasure treasure_;

    public MainGame() {
        level_ = 1;
        FileReader fr = new FileReader(".//game_environment_files/game_level_" + "1" + ".txt");
        initializeArray(fr.allLines());
    }

    public MainGame(int level) {
        level_ = level;
        FileReader fr = new FileReader(".//game_environment_files/game_level_" + level_ + ".txt");
        initializeArray(fr.allLines());
    }

    /**
     * helper function
     * help initialize the game_array with the according
     * arraylist of string that was read in the file
     * @param s an arraylist of string
     */
    private void initializeArray(ArrayList<String> s) {
        for (int i = 0; i < s.size(); i++) {
            //make sure it does not go out of bound
            if (i < 20) {
                for (int c = 0; c < s.get(i).length(); c++) {
                    if (c < 36) {
                        String cell = s.get(i).substring(c, c + 1);
                        int cell_int = 0;
                        try {
                            cell_int = Integer.parseInt(cell);
                        } catch (NumberFormatException e) {
                            System.out.println("Number Format Exception, using default value");
                            cell_int = 0;
                        }
                        game_array_[c][i] = cell_int;
                        
                        switch(cell_int) {
                            case EMPTY_CELL:
                                display_array_[c][i] = null;
                                break;
                            case ROCK_CELL:
                                display_array_[c][i] = new Asteroid(c, i);
                                asteroids.add(new Asteroid(c, i));
                                break;
                            case PLAYER_CELL:
                                player_ = new Player("player", c, i, 3, 30, Player.PlayerState.ALIVE);
                                display_array_[c][i] = player_;
                                break;
                            case RED_GHOST_CELL:
                                redGhosts.add(new Ghost("red_ghost_"+c+"_"+i, c, i, Ghost.Colour.RED, Ghost.GhostState.PASSIVE));
                                display_array_[c][i] = redGhosts.get(redGhosts.size()-1);
                                break;
                            case BLUE_GHOST_CELL:
                                blueGhosts.add(new Ghost("blue_ghost_"+c+"_"+i, c, i, Ghost.Colour.BLUE, Ghost.GhostState.PASSIVE));
                                display_array_[c][i] = blueGhosts.get(blueGhosts.size()-1);
                                break;
                            case YELLOW_GHOST_CELL:
                                yellowGhosts.add(new Ghost("yellow_ghost_"+c+"_"+i, c, i, Ghost.Colour.YELLOW, Ghost.GhostState.PASSIVE));
                                display_array_[c][i] = yellowGhosts.get(yellowGhosts.size()-1);
                                break;
                            case GREEN_GHOST_CELL:
                                greenGhosts.add(new Ghost("green_ghost_"+c+"_"+i, c, i, Ghost.Colour.GREEN, Ghost.GhostState.PASSIVE));
                                display_array_[c][i] = greenGhosts.get(greenGhosts.size()-1);
                                break;    
                            case TREASURE_CELL:
                                treasure_ = new Treasure(c, i);
                                display_array_[c][i] = new Treasure(c, i);
                                break;
                            case DOOR_CELL:
                                display_array_[c][i] = new Door(c, i);
                                break;
                            default:
                                display_array_[c][i] = null;
                                break;
                        }
                    }
                }
            }
        }
    }
    /**
     * find the type of the object in the given cell
     * or null if the cell is empty
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @return a string that represents the type of the object
     */
    public String cellType(int x, int y) {
        if (x > 36 || y > 20) {
            return "null";
        }
        if (display_array_[x][y] == null) {
            return "null";
        }
        AnimatedImage aImage = display_array_[x][y];
        if (aImage instanceof Asteroid) {
            return "asteroid";
        }
        if (aImage instanceof Player) {
            return "player";
        }
        if (aImage instanceof Ghost) {
            return "ghost";
        }
        if (aImage instanceof Treasure) {
            return "treasure";
        }
        if (aImage instanceof Door) {
            return "door";
        }
        return "null";
    }

    /**
     * check if there is an asteroid in the cell
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @return true if there is an asteroid in the cell
     */
    public boolean isCellAsteroid(double x, double y) {
        //System.out.println(x+ "   " + y);
        for (Asteroid a: asteroids) {
            //System.out.println("ax = " + a.getPositionX_() + "   ay = " + a.getPositionY_());
            if (a.getPositionX_() == x && a.getPositionY_() == y) {
                return true;
            }
        }
        return false;
    }
    /**
     * move the player according to the keycode
     * @param code the keycode that represents which key was pressed
     */
    public void movePlayer(KeyEvent e) {
        switch(e.getCode()) {
            case UP:
                if (player_.getPositionY() > 0) {
                    if (!isCellAsteroid(player_.getPositionX(), player_.getPositionY() - 1)) {
                        player_.moveDir("up");
                    }
                }
                break;
            case DOWN:
                if (player_.getPositionY() < Y_MAX - 1) {
                    if (!isCellAsteroid(player_.getPositionX(), player_.getPositionY() + 1)) {
                        player_.moveDir("down");
                    }
                }
                break;
            case RIGHT:
                if (player_.getPositionX() < X_MAX - 1) {
                    if (!isCellAsteroid(player_.getPositionX() + 1, player_.getPositionY())) {
                        player_.moveDir("right");
                    }
                }
                break;    
            case LEFT:    
                if (player_.getPositionX() > 0) {
                    if (!isCellAsteroid(player_.getPositionX() - 1, player_.getPositionY())) {
                        player_.moveDir("left");
                    }
                }
                break;
        }
    }
    /**
     * update each moving element in the game
     * @param t the time
     */
    public void update_time(double t) {
        player_.update(t, asteroids);
        for (Ghost rg: redGhosts) {
            rg.update(t, player_);
        }
        for (Ghost bg: blueGhosts) {
            bg.update(t, player_);
        }
        for (Ghost yg: yellowGhosts) {
            yg.update(t, player_);
        }
        for (Ghost gg: greenGhosts) {
            gg.update(t, player_);
        }
    }
    //getter functions
    public int[][] getGameArray() {
        return game_array_;
    }

    public AnimatedImage[][] getDisplayArray() {
        return display_array_;
    }

    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }
    
    public Player getPlayer() {
        return player_;
    }
    
    public Treasure getTreasure() {
        return treasure_;
    }

}