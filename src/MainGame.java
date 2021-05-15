import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.image.Image;

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

    private static final ArrayList<String> alphabetRocks = new ArrayList<>(Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"));
    private static final ArrayList<String> alphabetBackTiles = new ArrayList<>(Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","L"));

    /* the dimension for the easiest levels, change this if needed*/
    private static final double BASIC_DIMENSIONX = 300;
    private static final double BASIC_DIMENSIONY = 150;

    /* the level of the current game */
    private int level;
    private double dimensionX;
    private double dimensionY;

    /* lists that contain all the Ghosts of given level */
    private ArrayList<Ghost> redGhosts = new ArrayList<Ghost>();
    private ArrayList<Ghost> blueGhosts = new ArrayList<Ghost>();
    private ArrayList<Ghost> yellowGhosts = new ArrayList<Ghost>();
    private ArrayList<Ghost> greenGhosts = new ArrayList<Ghost>();
    private ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
    private ArrayList<Door> doors = new ArrayList<Door>();
    private ArrayList<BackTile> backtiles = new ArrayList<>();

    private Player player;
    private Treasure treasure;

    private boolean isCompleted;

    public MainGame() {
        this(1);
    }

    public MainGame(int level) {
        this.level = level;
        FileReader fr = new FileReader(".//game_environment_files/game_level_"+ this.level +".txt");
        FileReader fr_rock = new FileReader(".//game_environment_files/game_level_"+ this.level +"_tiles.txt");
        FileReader fr_back_tiles = new FileReader(".//game_environment_files/game_level_"+ this.level +"_backtiles.txt");
        
        //default value, will change after initializing the array
        dimensionX = 1430;
        dimensionY = 800;

        initializeArray(fr.allLines(), fr_rock.allLines(), fr_back_tiles.allLines());
        isCompleted = false;
    }

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
                    case RED_GHOST_CELL:
                        redGhosts.add(new Ghost("red_ghost_"+c+"_"+i, c * CELL_WIDTH, i * CELL_WIDTH, Ghost.Colour.RED, Ghost.GhostState.PASSIVE));
                        break;
                    case BLUE_GHOST_CELL:
                        blueGhosts.add(new Ghost("blue_ghost_"+c+"_"+i,c * CELL_WIDTH, i * CELL_WIDTH, Ghost.Colour.BLUE, Ghost.GhostState.PASSIVE));
                        break;
                    case YELLOW_GHOST_CELL:
                        yellowGhosts.add(new Ghost("yellow_ghost_"+c+"_"+i, c * CELL_WIDTH, i * CELL_WIDTH, Ghost.Colour.YELLOW, Ghost.GhostState.PASSIVE));
                        break;
                    case GREEN_GHOST_CELL:
                        greenGhosts.add(new Ghost("green_ghost_"+c+"_"+i, c * CELL_WIDTH, i * CELL_WIDTH, Ghost.Colour.GREEN, Ghost.GhostState.PASSIVE));
                        break;    
                    case TREASURE_CELL:
                        Image treasure_image = new Image(".//Images/treasures/Treasure"+level+".png");
                        treasure = new Treasure(treasure_image, c * CELL_WIDTH, i * CELL_WIDTH);
                        break;
                    case DOOR_CELL:
                        doors.add(new Door(c * CELL_WIDTH, i * CELL_WIDTH));
                        break;
                }
            }
        }
        initializeDimension();
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

    private void initializeDimension() {
        player.setDimension(dimensionX, dimensionY);
        for (Ghost rg: redGhosts) {
            rg.setDimension(dimensionX, dimensionY);
        }
        for (Ghost yg: yellowGhosts) {
            yg.setDimension(dimensionX, dimensionY);
        }
        for (Ghost bg: blueGhosts) {
            bg.setDimension(dimensionX, dimensionY);
        }
        for (Ghost gg: greenGhosts) {
            gg.setDimension(dimensionX, dimensionY);
        }
    }
    /**
     * move the player according to the keycode
     * @param input the keycode that represents which key was pressed
     */
    // TODO : figure out why the frames aren't updating
    public void movePlayer(ArrayList<String> input) {
        // basic movement of the player
        if (input.contains("W")) {      // Z on AZERTY keyboard
            //player.addForces(0, -5);   // UP
            player.setVelocity(0, -5);
            player.setDirection(Player.CharacterDirection.UP);
            player.updateImages("thrust up");
            //System.out.println("fThrustUp");
        }
        if (input.contains("D")) {
            //player.addForces(5, 0);    // RIGHT
            player.setVelocity(5, 0);
            player.setDirection(Player.CharacterDirection.RIGHT);
            player.updateImages("thrust right");
            //System.out.println("fThrustRight");
        }
        if (input.contains("S")) {
            //player.addForces(0, 5);    // DOWN
            player.setVelocity(0, 5);
            player.setDirection(Player.CharacterDirection.DOWN);
            player.updateImages("thrust down");
            //System.out.println("fThrustDown");
        }
        if (input.contains("A")) {      // Q on AZERTY keyboard
            //player.addForces(-5, 0);   // LEFT
            player.setVelocity(-5, 0);
            player.setDirection(Player.CharacterDirection.LEFT);
            player.updateImages("thrust left");
            //System.out.println("fThrustLeft");
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

        if (input.contains("L")) {
            player.setBeforeDoor(doors);
            if (player.getBeforeDoor()) {
                player.setVelocity(0,0);
            }
        }

        if (input.contains("J")) {
            player.setGripWall(true);
        }

        if (input.contains(null)) {
            /*if (player.getDirection() == Player.CharacterDirection.UP) {
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
            }*/
            player.setShieldOn(false);
        }
    }
    /**
     * update each moving element in the game
     * @param t the time
     */
    public void update_time(double t) {
        player.update(t, asteroids, getAllGhost(), treasure);
        for (Ghost rg: redGhosts) {
            rg.update(t, player, asteroids);
        }
        for (Ghost bg: blueGhosts) {
            bg.update(t, player, asteroids);
        }
        for (Ghost yg: yellowGhosts) {
            yg.update(t, player, asteroids);
        }
        for (Ghost gg: greenGhosts) {
            gg.update(t, player, asteroids);
        }
    }
    
    /**
    * end the game and recover the treasure
    */
    public void endGame() {
        player.setVelocity(0,0);
        treasure.setRecovered(true);
    }

    /**
     * to check if the player has complete the game
     * @return true if the player completes the game
     */
    public boolean isGameComplete() {
        return player.getIsGameCompleted();
    }

    /**
     * check if the game has ended
     * either the player has collected the treasure
     * or if the player loses all lives
     * @return true if the game has ended
     */
    public boolean isGameEnd() {
        return player.getIsGameCompleted() || player.getLives() == 0;
    }


    //getter function
    public ArrayList<Asteroid> getAsteroids() { return asteroids;}
    
    public Player getPlayer() { return player;}
    
    public Treasure getTreasure() { return treasure;}

    public ArrayList<Ghost> getAllGhost() {
        ArrayList<Ghost> all_ghosts = redGhosts;
        all_ghosts.addAll(blueGhosts);
        all_ghosts.addAll(yellowGhosts);
        all_ghosts.addAll(greenGhosts);
        return all_ghosts;
    }
    
    public ArrayList<Door> getDoors() { return doors;}

    public ArrayList<BackTile> getBacktiles() { return backtiles;}

    public double getDimensionX() {return dimensionX;}

    public double getDimensionY() {return dimensionY;}

    public double getPlayerLives() {return player.getLives();}

    public double getPlayerFieldEnergy() {return player.getFieldEnergy();}

}