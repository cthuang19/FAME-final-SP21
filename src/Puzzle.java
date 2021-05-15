import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javafx.scene.image.Image;

public class Puzzle {

    /* the width of each array showing on the screen*/
    public static final int CELL_WIDTH = 64;

    protected static final int EMPTY_CELL = 0;
    protected static final int ROCK_CELL = 1;
    protected static final int PLAYER_CELL = 2;
    protected static final int TREASURE_CELL = 7;

    private static final ArrayList<String> alphabetRocks = new ArrayList<>(Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"));
    private static final ArrayList<String> alphabetBackTiles = new ArrayList<>(Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","L"));

    /* the type and "level" of the current puzzle */
    protected int type;
    protected int level;
    protected double dimensionX;
    protected double dimensionY;

    protected ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
    protected ArrayList<BackTile> backtiles = new ArrayList<>();

    protected Player player;
    protected Treasure treasure;

    protected boolean isCompleted;

    public Puzzle() {
        this(1,1);
    }

    public Puzzle(int tp, int lvl) {
        type = tp;
        level = lvl;
        FileReader fr = new FileReader(".//game_environment_files/puzzle_level_"+type+"_"+level+".txt");
        FileReader fr_rock = new FileReader(".//game_environment_files/puzzle_level_"+type+"_"+level+"_tiles.txt");
        FileReader fr_back_tiles = new FileReader(".//game_environment_files/puzzle_level_"+type+"_"+level+"_backtiles.txt");
        initializeArray(fr.allLines(), fr_rock.allLines(), fr_back_tiles.allLines());
        isCompleted = false;
        dimensionX = 1430;
        dimensionY = 800;
    }

    public void setLevel(int lvl) {level = lvl;}

    /**
     * helper function
     * help initialize the game_array with the according
     * arraylist of string that was read in the file
     * @param s an arraylist of string
     */
    private void initializeArray(ArrayList<String> s, ArrayList<String> rock_file, ArrayList<String> back_tile_file) {
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
                initializeBackTiles(back_tile_file,c,i);
                switch(cell_int) {
                    case ROCK_CELL:
                        initializeRock(rock_file, c, i);
                        break;
                    case PLAYER_CELL:
                        player = new Player("player", c * CELL_WIDTH, i * CELL_WIDTH, 3, 300, Player.PlayerState.ALIVE);
                        break;
                    case TREASURE_CELL:
                        Random rand = new Random();
                        if (rand.nextInt(2)==0) {
                            Image treasure_image = new Image(".//Images/puzzle_treasures/heart.png");
                            treasure = new Treasure(treasure_image, c * CELL_WIDTH, i * CELL_WIDTH);
                        } else {
                            Image treasure_image = new Image(".//Images/puzzle_treasures/energy.png");
                            treasure = new Treasure(treasure_image, c * CELL_WIDTH, i * CELL_WIDTH);
                        }
                        break;
                }
            }
        }
        player.setDimension(dimensionX, dimensionY);
    }

    private void initializeRock(ArrayList<String> rock_file, int c, int i) {
        String letter = rock_file.get(i).substring(c, c + 1);
        if (alphabetRocks.contains(letter)) {
            asteroids.add(new Asteroid(letter, c * CELL_WIDTH, i * CELL_WIDTH));
        }
    }

    private void initializeBackTiles(ArrayList<String> back_tile_file, int c, int i) {
        String letter = back_tile_file.get(i).substring(c, c + 1);
        if (alphabetBackTiles.contains(letter)) {
            backtiles.add(new BackTile(letter, c * CELL_WIDTH, i * CELL_WIDTH));
        }
    }

    /**
     * move the player according to the keycode
     * @param input the keycode that represents which key was pressed
     */
    public void movePlayer(ArrayList<String> input) {
        // basic movement of the player
        if (input.contains("W")) {      // Z on AZERTY keyboard
            //player.addForces(0, -5);   // UP
            player.setVelocity(0, -5);
            player.setDirection(Player.CharacterDirection.UP);
            player.updateImages("thrust up");
        }
        if (input.contains("D")) {
            //player.addForces(5, 0);    // RIGHT
            player.setVelocity(5, 0);
            player.setDirection(Player.CharacterDirection.RIGHT);
            player.updateImages("thrust right");
        }
        if (input.contains("S")) {
            //player.addForces(0, 5);    // DOWN
            player.setVelocity(0, 5);
            player.setDirection(Player.CharacterDirection.DOWN);
            player.updateImages("thrust down");
        }
        if (input.contains("A")) {      // Q on AZERTY keyboard
            //player.addForces(-5, 0);   // LEFT
            player.setVelocity(-5, 0);
            player.setDirection(Player.CharacterDirection.LEFT);
            player.updateImages("thrust left");
        }

        //add or remove shield to the player
        if (input.contains("K")) {
            player.setShieldOn(true);
            if (player.getShieldOn()) {
                player.updateImages("shield");
            } else {
                player.updateImages("idle right");
            }
        } else {
            player.setShieldOn(false);
            player.updateImages("idle right");
        }

        if (input.contains("J")) {
            player.setGripWall(true);
        }

        if (input.contains(null)) {
            if (player.getDirection() == Player.CharacterDirection.UP) {
                player.updateImages("idle right");
            }
            if (player.getDirection() == Player.CharacterDirection.RIGHT) {
                player.updateImages("idle right");
            }
            if (player.getDirection() == Player.CharacterDirection.DOWN) {
                player.updateImages("idle left");
            }
            if (player.getDirection() == Player.CharacterDirection.LEFT) {
                player.updateImages("idle left");
            }
            player.setShieldOn(false);
            player.setGripWall(false);
        }
    }
    /**
     * update the player and the puzzle
     * @param t the time
     */
    public void update_time(double t, ArrayList<String> input) {
        // empty list to use for the update
        ArrayList<Ghost> empty_ghosts = new ArrayList<Ghost>();
        player.update(t, asteroids, empty_ghosts, treasure);
    }

    //getter function
    public ArrayList<Asteroid> getAsteroids() { return asteroids;}

    public Player getPlayer() { return player;}

    public Treasure getTreasure() { return treasure;}

    public ArrayList<BackTile> getBacktiles() { return backtiles;}

    public double getDimensionX() {return dimensionX;}

    public double getDimensionY() {return dimensionY;}

    public double getPlayerLives() {return player.getLives();}

    public double getPlayerFieldEnergy() {return player.getFieldEnergy();}

    public boolean getIsCompleted() {return isCompleted;}

    public int getType() {return type;}

}