public class MainGame {

    /* the width of each array showing on the screen*/
    public static final int CELL_WIDTH = 25;

    private static final int EMPTY_CELL = 0;
    private static final int ROCK_CELL = 1;
    private static final int PLAYER_CELL = 2;
    private static final int GHOST_CELL = 3;

    /* the array that represents each location on the game page */
    public int[64][48] game_array_;

    /* the level of the current game */
    public int level_;

    public MainGame() {
        level_ = 1;
    }

    public MainGame(int level) {
        level_ = level;

    }


}