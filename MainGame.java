import java.util.ArrayList;

public class MainGame {

    /* the width of each array showing on the screen*/
    public static final int CELL_WIDTH = 40;

    private static final int EMPTY_CELL = 0;
    private static final int ROCK_CELL = 1;
    private static final int PLAYER_CELL = 2;
    private static final int GHOST_CELL = 3;

    /* the array that represents each location on the game page */
    //private int[][] game_array_ = new int[40][30];
    private int[][] game_array_ = new int[36][20];
    private AnimatedImage[][] display_array = new AnimatedImage[36][20];

    /* the level of the current game */
    private int level_;

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
                                display_array[c][i] = null;
                                break;
                            case ROCK_CELL:
                                display_array[c][i] = new Asteroid(c, i);
                                break;
                            case PLAYER_CELL:
                                //TODO: add a player here with given location
                                break;
                            case GHOST_CELL:
                                //TODO: add a ghost here with given location
                                // or probably separate each ghost color by more 
                                // different cell_int
                                break;    
                            default:
                                display_array[c][i] = null;
                                break;
                        }
                        
                    }
                }
            }
        }
    }

    //getter functions
    public int[][] getGameArray() {
        return game_array_;
    }


}