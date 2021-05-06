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

    private Player player;
    private Treasure treasure;

    public MainGame() {
        this(1);
    }

    public MainGame(int level) {
        this.level = level;
        FileReader fr = new FileReader(".//game_environment_files/game_level_"+ this.level +".txt");
        initializeArray(fr.allLines());

        //change this if needed
        dimensionX = 1430;
        dimensionY = 800;
        //change it to something like this according to the level
        /*
        dimensionX = BASIC_DIMENSIONX * (level/3 + 1);
        dimensionY = BASIC_DIMENSIONY * (level/3 + 1);
        */
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
                        player = new Player("player", c * CELL_WIDTH, i * CELL_WIDTH, 3, 30, Player.PlayerState.ALIVE);
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
                        treasure = new Treasure(c * CELL_WIDTH, i * CELL_WIDTH);
                        break;
                    case DOOR_CELL:
                        doors.add(new Door(c * CELL_WIDTH, i * CELL_WIDTH));
                        break;
                }
            }
        }
        initializeDimension();
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
            System.out.println("fThrustUp");
        }
        if (input.contains("D")) {
            //player.addForces(5, 0);    // RIGHT
            player.setVelocity(5, 0);
            player.setDirection(Player.CharacterDirection.RIGHT);
            player.updateImages("thrust right");
            System.out.println("fThrustRight");
        }
        if (input.contains("S")) {
            //player.addForces(0, 5);    // DOWN
            player.setVelocity(0, 5);
            player.setDirection(Player.CharacterDirection.DOWN);
            player.updateImages("thrust down");
            System.out.println("fThrustDown");
        }
        if (input.contains("A")) {      // Q on AZERTY keyboard
            //player.addForces(-5, 0);   // LEFT
            player.setVelocity(-5, 0);
            player.setDirection(Player.CharacterDirection.LEFT);
            player.updateImages("thrust left");
            System.out.println("fThrustLeft");
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

    public double getDimensionX() {return dimensionX;}

    public double getDimensionY() {return dimensionY;}

    public double getPlayerLives() {return player.getLives();}

    public double getPlayerFieldEnergy() {return player.getFieldEnergy();}

}