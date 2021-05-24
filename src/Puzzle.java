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

    protected ArrayList<Asteroid> asteroids = new ArrayList<>();
    protected ArrayList<BackTile> backtiles = new ArrayList<>();

    protected Player player;
    protected Treasure treasure;

    protected boolean isCompleted;

    // states of the FSM that will recognize the pattern
    private int state;
    private long timeStamp;

    private ArrayList<MovingAnimatedImage> lights = new ArrayList<>();
    private final Image[] lightOn = new Image[1];
    private final Image[] lightOff = new Image[1];

    public Puzzle() {
        this(1,1,3,300);
    }

    public Puzzle(int tp, int lvl, int player_lives, int player_energy) {
        type = tp;
        level = lvl;
        FileReader fr = new FileReader(".//game_environment_files/puzzle_level_"+type+"_"+level+".txt");
        FileReader fr_rock = new FileReader(".//game_environment_files/puzzle_level_"+type+"_"+level+"_tiles.txt");
        FileReader fr_back_tiles = new FileReader(".//game_environment_files/puzzle_level_"+type+"_"+level+"_backtiles.txt");
        initializeArray(fr.allLines(), fr_rock.allLines(), fr_back_tiles.allLines(), player_lives, player_energy);
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
    private void initializeArray(ArrayList<String> s, ArrayList<String> rock_file, ArrayList<String> back_tile_file, int player_lives, int player_energy) {
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
                        player = new Player("player", c * CELL_WIDTH, i * CELL_WIDTH, player_lives, player_energy, Player.PlayerState.ALIVE);
                        break;
                    case TREASURE_CELL:
                        Random rand = new Random();
                        if (rand.nextInt(2)==0) {
                            Image treasure_image = new Image(".//Images/puzzle_treasures/heart.png");
                            treasure = new Treasure(treasure_image, c * CELL_WIDTH, i * CELL_WIDTH);
                            treasure.setType("life");
                        } else {
                            Image treasure_image = new Image(".//Images/puzzle_treasures/energy.png");
                            treasure = new Treasure(treasure_image, c * CELL_WIDTH, i * CELL_WIDTH);
                            treasure.setType("energy");
                        }
                        treasure.setCanBeRecovered(false);
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

        // default set of frames when nothing is pressed
        if (player.getDirection() == Player.CharacterDirection.RIGHT) {
            player.updateImages("idle right");
        }
        if (player.getDirection() == Player.CharacterDirection.LEFT) {
            player.updateImages("idle left");
        }
        player.setShieldOn(false);

        if (input.contains("W")) {      // UP           // Z on AZERTY keyboard
            player.setVelocity(0, -5);
            if (player.getDirection() == Player.CharacterDirection.RIGHT) {
                player.updateImages("thrust up right");
            } else {
                player.updateImages("thrust up left");
            }
        }
        if (input.contains("D")) {      // RIGHT
            player.setVelocity(5, 0);
            player.setDirection(Player.CharacterDirection.RIGHT);
            player.updateImages("thrust right");
        }
        if (input.contains("S")) {      // DOWN
            player.setVelocity(0, 5);
            if (player.getDirection() == Player.CharacterDirection.RIGHT) {
                player.updateImages("thrust down right");
            } else {
                player.updateImages("thrust down left");
            }
        }
        if (input.contains("A")) {      // LEFT         // Q on AZERTY keyboard
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
            //player.updateImages("idle right");
        }

        if (input.contains("J")) {
            player.setGripWall(true);
        } else {
            player.setGripWall(false);
        }
    }
    /**
     * update the player and the puzzle
     * @param t the time
     */
    public void update_time(double t, ArrayList<String> input) {
        // empty list to use for the update
        ArrayList<Ghost> empty_ghosts = new ArrayList<>();
        player.update(t, asteroids, empty_ghosts, treasure);
    }

    /** update of the FSM that will recognize the pattern
     * updates the variable isCompleted
     * @param input a keyboard event
     * **/
    public void updateFSM(ArrayList<String> input) {
        if (type == 1) {
            switch (state) {
                case 0 :
                    if (input.contains("W")) {      // Z on AZERTY keyboard
                        this.state = 1;
                        timeStamp = System.currentTimeMillis();
                    }
                    break;
                case 1 :
                    if ((System.currentTimeMillis()-timeStamp)/1000 < 1) {
                        if (input.contains("W")) {      // Z on AZERTY keyboard
                            this.state = 1;
                            timeStamp = System.currentTimeMillis();
                        } else {
                            if (input.contains("D")) {
                                this.state = 2;
                                timeStamp = System.currentTimeMillis();
                            } else {
                                if (input.isEmpty()) {
                                    this.state = 1;
                                } else {
                                    //this.state = 0;
                                }
                            }
                        }
                    } else {
                        this.state = 0;
                    }
                    break;
                case 2 :
                    if ((System.currentTimeMillis()-timeStamp)/1000 < 1) {
                        if (input.contains("W")) {      // Z on AZERTY keyboard
                            this.state = 1;
                            timeStamp = System.currentTimeMillis();
                        } else {
                            if (input.contains("A")) {  // Q on AZERTY keyboard
                                this.state = 3;
                                timeStamp = System.currentTimeMillis();
                            } else {
                                if (input.isEmpty()) {
                                    this.state = 2;
                                } else {
                                    //this.state = 0;
                                }
                            }
                        }
                    } else {
                        this.state = 0;
                    }
                    break;
                case 3 :
                    this.isCompleted = true;
                    break;
                default :
                    this.state = 0;
                    break;
            }
        }
        if (type == 2) {
            switch (state) {
                case 0 :
                    if (player.intersects(lights.get(0))) {
                        this.state = 1;
                    }
                    break;
                case 1 :
                    if (player.intersects(lights.get(1))) {
                        this.state = 2;
                    } else {
                        if (player.intersects(lights.get(2)) || player.intersects(lights.get(3))) {
                            this.state = 0;
                        }
                    }

                    break;
                case 2 :
                    if (player.intersects(lights.get(2))) {
                        this.state = 3;
                    } else {
                        if (player.intersects(lights.get(3))) {
                            this.state = 0;
                        }
                    }
                    break;
                case 3 :
                    if (player.intersects(lights.get(3))) {
                        this.state = 4;
                    }
                    break;
                case 4 :
                    this.isCompleted = true;
                    break;
                default :
                    this.state = 0;
                    break;
            }
        }
    }

    public void initializeLights() {
        lightOn[0] = new Image(".//Images/puzzle_lights/platformOn.png",64,64,false,true);
        lightOff[0] = new Image(".//Images/puzzle_lights/platformOff.png",64,64,false,true);

        if (type == 1) {
            for (int j = 0; j < 3; j++) {
                lights.add(new MovingAnimatedImage("light", 512 + 128 * j + 7, 192 + 3, 50, 50, 0));
                lights.get(j).setDuration(0.2);
                lights.get(j).setFrames(lightOff);
            }
        }
        if (type == 2) {
            for (int j=0;j<4;j++) {
                if (j==0||j==1) {
                    lights.add(new MovingAnimatedImage("light", 512 + 256 * j, 256, 64, 64, 0));
                } else {
                    lights.add(new MovingAnimatedImage("light", 768 - 256 * (3-j), 512, 64, 64, 0));
                }
                lights.get(j).setDuration(0.2);
                lights.get(j).setFrames(lightOff);
            }

        }
    }

    public void updateLights() {
        if (type == 1) {
            switch (state) {
                case 0:
                    for (int i = 0; i < 3; i++) lights.get(i).setFrames(lightOff);
                    break;
                case 1:
                    lights.get(0).setFrames(lightOn);
                    for (int i = 1; i < 3; i++) lights.get(i).setFrames(lightOff);
                    break;
                case 2:
                    for (int i = 0; i < 2; i++) lights.get(i).setFrames(lightOn);
                    lights.get(2).setFrames(lightOff);
                    break;
                case 3:
                    for (int i = 0; i < 3; i++) lights.get(i).setFrames(lightOn);
                    break;
                default:
                    for (int i = 0; i < 3; i++) lights.get(i).setFrames(lightOff);
                    break;
            }
        }
        if (type == 2) {
            switch (state) {
                case 0 :
                    for (int i=0; i<4; i++) lights.get(i).setFrames(lightOff);
                    break;
                case 1 :
                    lights.get(0).setFrames(lightOn);
                    for (int i=1; i<4; i++) lights.get(i).setFrames(lightOff);
                    break;
                case 2 :
                    for (int i=0; i<2; i++) lights.get(i).setFrames(lightOn);
                    for (int i=2; i<4; i++) lights.get(i).setFrames(lightOff);
                    break;
                case 3 :
                    for (int i=0; i<3; i++) lights.get(i).setFrames(lightOn);
                    lights.get(3).setFrames(lightOff);
                    break;
                case 4 :
                    for (int i=0; i<4; i++) lights.get(i).setFrames(lightOn);
                    break;
                default :
                    for (int i=0; i<4; i++) lights.get(i).setFrames(lightOff);
                    break;
            }
        }
    }


    //getter functions
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

    public ArrayList<MovingAnimatedImage> getLights() {return lights;}

}