import java.util.ArrayList;

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

    /* the array that represents each location on the game page */
    //private int[][] game_array_ = new int[40][30];
    private int[][] game_array_ = new int[36][20];
    private AnimatedImage[][] display_array_ = new AnimatedImage[36][20];

    /* the level of the current game */
    private int level_;

    /* lists that contain all the Ghosts of given level */
    private ArrayList<Ghost> redGhosts = new ArrayList<Ghost>();
    private ArrayList<Ghost> blueGhosts = new ArrayList<Ghost>();
    private ArrayList<Ghost> yellowGhosts = new ArrayList<Ghost>();
    private ArrayList<Ghost> greenGhosts = new ArrayList<Ghost>();

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
                                break;
                            case PLAYER_CELL:
                                Player player = new Player("player", i, c, 3, 30, Player.PlayerState.ALIVE);
                                display_array_[c][i] = player;
                                break;
                            case RED_GHOST_CELL:
                                redGhosts.add(new Ghost("red_ghost_"+c+"_"+i, i, c, Ghost.Colour.RED, Ghost.GhostState.PASSIVE));
                                display_array_[c][i] = redGhosts.get(redGhosts.size()-1);
                                break;
                            case BLUE_GHOST_CELL:
                                blueGhosts.add(new Ghost("blue_ghost_"+c+"_"+i, i, c, Ghost.Colour.BLUE, Ghost.GhostState.PASSIVE));
                                display_array_[c][i] = blueGhosts.get(blueGhosts.size()-1);
                                break;
                            case YELLOW_GHOST_CELL:
                                yellowGhosts.add(new Ghost("yellow_ghost_"+c+"_"+i, i, c, Ghost.Colour.YELLOW, Ghost.GhostState.PASSIVE));
                                display_array_[c][i] = yellowGhosts.get(yellowGhosts.size()-1);
                                break;
                            case GREEN_GHOST_CELL:
                                greenGhosts.add(new Ghost("green_ghost_"+c+"_"+i, i, c, Ghost.Colour.GREEN, Ghost.GhostState.PASSIVE));
                                display_array_[c][i] = greenGhosts.get(greenGhosts.size()-1);
                                break;    
                            case TREASURE_CELL:
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
     * check if the cell is empty
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @return true if the cell is empty; else false
     */
    public boolean isCellEmpty(int x, int y) {
        return (display_array_[x][y] == null);
    }

    //getter functions
    public int[][] getGameArray() {
        return game_array_;
    }

    public AnimatedImage[][] getDisplayArray() {
        return display_array_;
    }


}