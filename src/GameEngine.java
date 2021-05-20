
import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.animation.*;
import javafx.scene.input.KeyEvent;
import javafx.event.*;
import javafx.scene.control.Button;

import javafx.event.EventHandler;

import java.util.ArrayList;

public class GameEngine extends Application {

    public static final int CANVAS_WIDTH = 1500;
    public static final int CANVAS_HEIGHT = 800;
    public static final int BUTTON_WIDTH = 600;
    public static final int BUTTON_HEIGHT = 150;
    public static final int SMALL_BUTTON_SIZE = 100;
    public static final int SMALL_BUTTON_X = 1320;
    public static final int SMALL_BUTTON_Y = 680;

    private static final int MAIN_GAME_DISPLAY_WIDTH = 64;

    /* the maximum level for the game*/
    private static final int MAX_LEVEL = 6;

    public static final Font FONT_XLARGE = Font.font("helvetica", FontWeight.LIGHT, FontPosture.REGULAR, 35);
    public static final Font FONT_LARGE = Font.font("helvetica", FontWeight.LIGHT, FontPosture.REGULAR, 27);
    public static final Font FONT_SMALL = Font.font("helvetica", FontWeight.LIGHT, FontPosture.REGULAR, 15);
    public static final Font FONT_TITLE = Font.font("helvetica", FontWeight.LIGHT, FontPosture.REGULAR, 100);

    public static final Image BACKGROUND_IMAGE = new Image(".//Images/Background-4.png");

    /* the page of the current scene (not sure if useful)*/
    enum Page {LANGUAGE, CREDITS, INITIAL, MAIN, GAME, PUZZLE, ENDLEVEL};
    
    /* the language of the game */
    enum Language {ENGLISH, FRENCH};

    private Scene scene;
    private Stage stage;

    private static Page page;
    private static Language language = Language.ENGLISH;

    /* the current game level */
    private static int current_game_level;

    /* number of the last unlocked level */
    private static int max_unlocked_level;

    /* check if the game has ended*/
    private static boolean endMainGame;

    /* check if the game has ended by game over */
    private static boolean gameOver;

    /* the current (or last) puzzle level */
    private static int current_puzzle_level;
    private static int current_puzzle_type;

    /* the current stats of the player to be kept when change of page */
    private static int player_lives = 3;
    private static int player_energy = 300;
    private static double player_back_puzzle_X = 0;
    private static double player_back_puzzle_Y = 0;

    @Override
    public void start(Stage theStage) {
        stage = theStage;
        switch (page) {
            case LANGUAGE:
                scene = getLanguageScene();
                break;
            case CREDITS:
                scene = getCreditsScene();
                break;
            case INITIAL:
                scene = getInitialScene();
                break;
            case MAIN:
                scene = getMainScene();
                break;
            case GAME:
                scene = getGameScene();
                break;
            case PUZZLE:
                scene = getPuzzleScene();
                break;
            case ENDLEVEL:
                scene = getEndLevelScene();
                break;
            default:
                Group group_default = new Group();
                scene = new Scene(group_default);
        }

        stage.setScene(scene);
        stage.show();
    }

    /**
     * create the language scene and return it
     * the scene contains languages buttons and credits button
     * @return the scene in selected language
     */
    private Scene getLanguageScene() {

        //reset max unlocked level, lives and energy for the player to restart the game
        //max_unlocked_level = 1;
        //player_lives = 3;
        //player_energy = 300;

        //set all the layout of the language scene
        Group root_language = new Group();
        Scene scene_language = new Scene(root_language);
        Canvas canvas_language = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc_language = canvas_language.getGraphicsContext2D();

        gc_language.drawImage(BACKGROUND_IMAGE, 0, 0);

        // draw game name
        Image game_name_image = new Image(".//Images/gui/blue/panel-5.png",BUTTON_WIDTH*2, BUTTON_HEIGHT*2,false,true);
        gc_language.drawImage(game_name_image, 130, 50);
        gc_language.setFill(Color.LIGHTYELLOW);
        gc_language.setFont(FONT_TITLE);
        gc_language.fillText("GAME NAME", 400, 230);

        // draw english button
        Button english_button = new Button("English (US)");
        english_button.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        english_button.setLayoutX(450);
        english_button.setLayoutY(400);

        Image english_button_image = new Image(".//Images/gui/purple/panel-4.png",BUTTON_WIDTH, BUTTON_HEIGHT,false,true);
        gc_language.drawImage(english_button_image,450,400);
        english_button.setStyle("-fx-background-color: transparent;-fx-border-color: transparent;-fx-text-fill: white;-fx-font-size: 25");

        english_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                page = Page.INITIAL;
                language = Language.ENGLISH;
                updateScene(page);
            }
        });

        // draw french button
        Button french_button = new Button("Français (Fr)");
        french_button.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        french_button.setLayoutX(450);
        french_button.setLayoutY(600);

        Image french_button_image = new Image(".//Images/gui/blue/panel-5.png",BUTTON_WIDTH, BUTTON_HEIGHT,false,true);
        gc_language.drawImage(french_button_image,450,600);
        french_button.setStyle("-fx-background-color: transparent;-fx-border-color: transparent;-fx-text-fill: white;-fx-font-size: 25");

        french_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                page = Page.INITIAL;
                language = Language.FRENCH;
                updateScene(page);
            }
        });

        Button credits_button = drawButton("Credits", gc_language, SMALL_BUTTON_X, SMALL_BUTTON_Y, SMALL_BUTTON_SIZE, SMALL_BUTTON_SIZE, Page.CREDITS);
        Button exit_button = drawButton("Exit ", gc_language, 25, SMALL_BUTTON_Y, SMALL_BUTTON_SIZE, SMALL_BUTTON_SIZE, null);
        exit_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                System.exit(0);
            }
        });

        root_language.getChildren().add(canvas_language);
        root_language.getChildren().add(english_button);
        root_language.getChildren().add(french_button);
        root_language.getChildren().add(credits_button);
        root_language.getChildren().add(exit_button);

        return scene_language;
    }

    /**
     * create the credits scene and return it
     * the scene contains credits and back button
     * @return the scene
     */
    private Scene getCreditsScene() {
        Group root_credits = new Group();
        Scene credits = new Scene(root_credits);
        Canvas canvas_credits = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc_credits = canvas_credits.getGraphicsContext2D();

        gc_credits.drawImage(BACKGROUND_IMAGE, 0, 0);

        Button back_button = drawButton("Back", gc_credits, SMALL_BUTTON_X, SMALL_BUTTON_Y, SMALL_BUTTON_SIZE, SMALL_BUTTON_SIZE, Page.LANGUAGE);

        root_credits.getChildren().add(canvas_credits);
        root_credits.getChildren().add(back_button);

        FileReader fr = new FileReader(".//story_files/credits.txt");
        ArrayList<String> all = fr.allLines();

        // drawing a rectangle background
        Image gui_image = new Image(".//Images/gui/blue/panel-1.png",1300,700,false,true);
        gc_credits.drawImage(gui_image,50,50);
        // drawing the text
        gc_credits.setFill(Color.LIGHTYELLOW);
        gc_credits.setFont(FONT_LARGE);
        for (int i = 0; i < all.size(); i++) {
            gc_credits.fillText(all.get(i), 170, 160 + 60 * i);
        }
        return credits;
    }

    /**
     * create the initial scene according to the language chosen and return it
     * the scene contains story and next button
     * @return the initial scene
     */
    private Scene getInitialScene() {
        //set the basic element of the initial scene
        Group root_initial = new Group();
        Scene initial = new Scene(root_initial);
        Canvas canvas_initial = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc_initial = canvas_initial.getGraphicsContext2D();

        gc_initial.drawImage(BACKGROUND_IMAGE, 0, 0);

        Button next_button = drawButton("Next", gc_initial, SMALL_BUTTON_X, SMALL_BUTTON_Y, SMALL_BUTTON_SIZE, SMALL_BUTTON_SIZE, Page.MAIN);
        Button back_button = drawButton("Back", gc_initial, 25, SMALL_BUTTON_Y, SMALL_BUTTON_SIZE, SMALL_BUTTON_SIZE, Page.LANGUAGE);

        root_initial.getChildren().add(canvas_initial);
        root_initial.getChildren().add(next_button);
        root_initial.getChildren().add(back_button);

        //depends on the language of the game
        //the initial scene will load different file
        String fileName = "";
        fileName = Util.convertLanguage(language, "story_english.txt");
        
        FileReader fr = new FileReader(".//story_files/" + fileName);
        ArrayList<String> all = fr.allLines();

        // drawing a rectangle background
        Image gui_image = new Image(".//Images/gui/blue/panel-1.png",1300,700,false,true);
        gc_initial.drawImage(gui_image,80,50);
        // drawing the text
        gc_initial.setFill(Color.LIGHTYELLOW);
        gc_initial.setFont(FONT_LARGE);
        for (int i = 0; i < all.size(); i++) {
            gc_initial.fillText(all.get(i), 190, 180 + 70 * i);
        }
        return initial;
    }

    /**
     * create the main scene and return it
     * the scene contains level buttons
     * @return the main scene
     */
    public Scene getMainScene() {
        endMainGame = false;
        gameOver = false;
        Group root_main = new Group();
        Scene scene_main = new Scene(root_main);
        Canvas canvas_main = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc_main = canvas_main.getGraphicsContext2D();
        gc_main.drawImage(BACKGROUND_IMAGE, 0, 0);
        //display the ending message
        if (max_unlocked_level > MAX_LEVEL) {
            //display the message of completing the whole game and exit
            String endingFileName = Util.convertLanguage(language, "ending_english.txt");
            Image gui_image = new Image(".//Images/gui/yellow/panel-1.png",1300,700,false,true);
            gc_main.drawImage(gui_image, 50, 50);

            //TODO: probably change the font style here
            gc_main.setFill(Color.BLACK);
            gc_main.setFont(FONT_XLARGE);
            FileReader fr = new FileReader(".//story_files/" + endingFileName);
            ArrayList<String> all = fr.allLines();
            for (int i = 0; i < all.size(); i++) {
                gc_main.fillText(all.get(i), 280, 340 + 70 * i);
            }

            //the restart button to restart the game
            Button restart_button = drawButton("Restart", gc_main, 200, 500, 450, BUTTON_HEIGHT, Page.LANGUAGE);

            //the exit button to exit the game
            Button exit_button = drawButton("Exit", gc_main, 700, 500, 450, BUTTON_HEIGHT, null);
            exit_button.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    System.exit(0);
                }
            });

            root_main.getChildren().add(canvas_main);
            root_main.getChildren().add(restart_button);
            root_main.getChildren().add(exit_button);
            return scene_main;
        }

        String level_label = "";
        ArrayList<Button> level_buttons = new ArrayList<Button>();
        ArrayList<Image> button_images = new ArrayList<>();
        for (int i=0; i<max_unlocked_level; i++) {
            level_label = Util.convertLanguage(language, "LEVEL ") + (i + 1);
            button_images.add(new Image(".//Images/asteroids/Mega/asteroidR"+i+".png",200,200,true,true));

            level_buttons.add(new Button(level_label));
            level_buttons.get(i).setMinSize(200, 200);

            if (i<3) {
                gc_main.drawImage(button_images.get(i),200+400*i,100);
                level_buttons.get(i).setLayoutX(200 + 400 * i);
                level_buttons.get(i).setLayoutY(100);
            } else {
                gc_main.drawImage(button_images.get(i),1000-400*(i-3),500);
                level_buttons.get(i).setLayoutX(1000-400*(i-3));
                level_buttons.get(i).setLayoutY(500);
            }

            level_buttons.get(i).setStyle("-fx-background-color: transparent;-fx-border-color: transparent;-fx-text-fill: white;-fx-font-size: 28");

            final int j=i+1;
            level_buttons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    page = Page.GAME;
                    current_game_level = j;
                    scene = getGameScene();
                    stage.setScene(scene);
                    stage.show();
                }
            });
        }

        gc_main.drawImage(new Image(".//Images/spaceship/spiked_ship_3_small_red.png"),50,230);

/*        AnimatedImage ship = new AnimatedImage();
        final Image[] fship = new Image[4];
        for (int i=0;i<4;i++) fship[i] = new Image(".//Images/spaceship/red_ship/red_ship_"+i+".png");
        ship.setFrames(fship);
        ship.setDuration(0.2);
        gc_main.drawImage(ship.getFrame(0),75,300);
*/
        drawPlayerStatus(gc_main, player_lives, player_energy,"choose level");
        drawPlayerTreasures(gc_main);

        root_main.getChildren().add(canvas_main);
        for (int i=0; i<max_unlocked_level; i++) {
            root_main.getChildren().add(level_buttons.get(i));
        }

        return scene_main;
    }

    /**
     * create the game scene and return it
     * the scene contains the level
     * @return the game scene
     */
    public Scene getGameScene() {
        Group root_game = new Group();
        Scene scene_game = new Scene(root_game);
        Canvas canvas_game = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc_game = canvas_game.getGraphicsContext2D();
        MainGame main_game = new MainGame(current_game_level, player_lives, player_energy);
        final long startNanoTime = System.nanoTime();

        ArrayList<String> input = new ArrayList<String>();

        //deal with the key events
        scene_game.setOnKeyPressed(
                new EventHandler<KeyEvent>()
                {
                    public void handle(KeyEvent e)
                    {
                        String code = e.getCode().toString();
                        if ( !input.contains(code) ){
                            input.add( code );
                        }
                    }
                });

        scene_game.setOnKeyReleased(
                new EventHandler<KeyEvent>()
                {
                    public void handle(KeyEvent e)
                    {
                        String code = e.getCode().toString();
                        input.remove( code );
                    }
                });

        //getting all the fix information to draw
        ArrayList<Asteroid> display_asteroid = main_game.getAsteroids();       
        Treasure display_treasure = main_game.getTreasure();
        ArrayList<BackTile> display_backtiles = main_game.getBacktiles();

        new AnimationTimer() {
            public void handle(long current_nano_time) {
                if (endMainGame){
                    stop();
                }
                double t = (current_nano_time - startNanoTime) / 1000000000.0;
                main_game.movePlayer(input);
                main_game.update_time(t);

                Player display_player = main_game.getPlayer();
                if ((player_back_puzzle_X!=0)&&(player_back_puzzle_Y!=0)) {
                    display_player.setPosition(player_back_puzzle_X, player_back_puzzle_Y);
                    player_back_puzzle_X = 0;
                    player_back_puzzle_Y = 0;
                }

                player_lives = (int) display_player.getLives();
                player_energy = (int) display_player.getFieldEnergy();

                // stepping in a puzzle
                if (display_player.getBeforeDoor()) {
                    current_puzzle_type = display_player.getCurrentDoor().getPuzzleType();
                    current_puzzle_level = display_player.getCurrentDoor().getPuzzleLevel();
                    player_back_puzzle_X = display_player.getCurrentDoor().getPositionX();
                    player_back_puzzle_Y = display_player.getCurrentDoor().getPositionY();
                    page = Page.PUZZLE;
                    updateScene(page);
                    this.stop();
                }

                // going out of the level
                display_player.setGoOut();
                if (display_player.getGoOut()) {
                    page = Page.MAIN;
                    updateScene(page);
                    this.stop();
                }
                //TODO: still need modification
                double offsetX = display_player.getPositionX() - CANVAS_WIDTH/2;
				if (offsetX<0) offsetX=0;
                if (offsetX>main_game.getDimensionX() - CANVAS_WIDTH) offsetX=main_game.getDimensionX() - CANVAS_WIDTH;
                
                double offsetY = display_player.getPositionY() - CANVAS_HEIGHT/2;
				if (offsetY<0) offsetY=0;
                if (offsetY>main_game.getDimensionY() - CANVAS_HEIGHT) offsetY=main_game.getDimensionY() - CANVAS_HEIGHT;

                //draw background image
                gc_game.drawImage(BACKGROUND_IMAGE, 0, 0);
                for (BackTile b: display_backtiles) {
                    gc_game.drawImage(b.getFrame(0), b.getPositionX() - offsetX, b.getPositionY() - offsetY);
                }
                for (Asteroid a: display_asteroid) {
                    gc_game.drawImage(a.getFrame(0), a.getPositionX() - offsetX, a.getPositionY() - offsetY);
                }

                //draw the treasure
                gc_game.drawImage(display_treasure.getFrame(0), display_treasure.getPositionX() - offsetX,
                       display_treasure.getPositionY() - offsetY, MAIN_GAME_DISPLAY_WIDTH, MAIN_GAME_DISPLAY_WIDTH);

                // draw the doors
                ArrayList<Door> display_door = main_game.getDoors();
                for (Door d: display_door) {
                    gc_game.drawImage(d.getFrame(0), d.getPositionX()-offsetX, d.getPositionY()-offsetY, MAIN_GAME_DISPLAY_WIDTH, MAIN_GAME_DISPLAY_WIDTH);
                }

                //draw the player
                gc_game.drawImage(display_player.getFrame(t), display_player.getPositionX() - offsetX, display_player.getPositionY() - offsetY);

                //draw the ghosts
                ArrayList<Ghost> display_ghosts = main_game.getAllGhost();
                for (Ghost g: display_ghosts) {
                    gc_game.drawImage(g.getFrame(t), g.getPositionX() - offsetX, g.getPositionY() - offsetY);
                }

                drawPlayerStatus(gc_game, main_game.getPlayerLives(), main_game.getPlayerFieldEnergy(),"main");

                if (main_game.isGameEnd()) {
                    if (main_game.isGameComplete() && current_game_level == max_unlocked_level) {
                        max_unlocked_level++;
                    }
                    main_game.endGame();
                    endMainGame = true;
                    gameOver = (display_player.getLives()==0);
                    page = Page.ENDLEVEL;
                    updateScene(page);
                    this.stop();
                }

            }
        }.start();
        
        root_game.getChildren().add(canvas_game);
        return scene_game;
    }

    /**
     * create the puzzle scene and return it
     * the scene contains the puzzle
     * @return the puzzle scene
     */
    public Scene getPuzzleScene() {
        Group root_puzzle = new Group();
        Scene scene_puzzle = new Scene(root_puzzle);
        Canvas canvas_puzzle = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc_puzzle = canvas_puzzle.getGraphicsContext2D();
        // initialize puzzle
        PuzzleType1 puzzle = new PuzzleType1(1, player_lives, player_energy);
        puzzle.setLevel(current_puzzle_level);
        puzzle.initializeLights();
        /*switch (current_puzzle_type) {
            case 1 :
                puzzle.setLevel(current_puzzle_level);
                puzzle.initializeLights();
                break;
            default :
                puzzle.setLevel(current_puzzle_level);
                break;
        }*/
        final long startNanoTime = System.nanoTime();

        ArrayList<String> input = new ArrayList<String>();

        //deal with the key events
        scene_puzzle.setOnKeyPressed(
                new EventHandler<KeyEvent>()
                {
                    public void handle(KeyEvent e)
                    {
                        String code = e.getCode().toString();   // configured for QWERTY keyboard
                        if ( !input.contains(code) ){
                            input.add( code );
                        }
                    }
                });

        scene_puzzle.setOnKeyReleased(
                new EventHandler<KeyEvent>()
                {
                    public void handle(KeyEvent e)
                    {
                        String code = e.getCode().toString();
                        input.remove( code );
                    }
                });

        //getting all the fix information to draw
        ArrayList<Asteroid> display_asteroid = puzzle.getAsteroids();
        Treasure display_treasure = puzzle.getTreasure();
        display_treasure.setCanBeRecovered(false);
        ArrayList<BackTile> display_backtiles = puzzle.getBacktiles();

        new AnimationTimer() {
            public void handle(long current_nano_time) {
                double t = (current_nano_time - startNanoTime) / 1000000000.0;
                puzzle.movePlayer(input);
                puzzle.update_time(t, input);
                switch (current_puzzle_type) {
                    case 1 :
                        puzzle.updateFSM(input);
                        puzzle.updateLights();
                        break;
                    default :
                        break;
                }
                System.out.println(display_treasure.getCanBeRecovered());

                Player display_player = puzzle.getPlayer();

                player_lives = (int) display_player.getLives();
                player_energy = (int) display_player.getFieldEnergy();

                ArrayList<AnimatedImage> lights = puzzle.getLights();

                display_player.setGoOut();
                if (display_player.getGoOut()) {
                    page = Page.GAME;
                    updateScene(page);
                    this.stop();
                }

                //draw background image
                gc_puzzle.drawImage(BACKGROUND_IMAGE, 0, 0);
                for (BackTile b: display_backtiles) {
                    gc_puzzle.drawImage(b.getFrame(0), b.getPositionX(), b.getPositionY());
                }
                for (Asteroid a: display_asteroid) {
                    gc_puzzle.drawImage(a.getFrame(0), a.getPositionX(), a.getPositionY());
                }

                display_treasure.setCanBeRecovered(puzzle.getIsCompleted());

                // draw the treasure only if the level in completed
                // and set door.isCompleted to true
                if (display_treasure.getCanBeRecovered()&&(!display_treasure.getRecovered())) {
                    gc_puzzle.drawImage(display_treasure.getFrame(0), display_treasure.getPositionX(),
                            display_treasure.getPositionY(), MAIN_GAME_DISPLAY_WIDTH, MAIN_GAME_DISPLAY_WIDTH);
                    //display_player.getCurrentDoor().setIsCompleted(true);
                }

                // stops displaying treasure if recovered
                if (!display_treasure.getRecovered()) {
                    if (display_player.intersects(display_treasure)&&display_treasure.getCanBeRecovered()) {
                        // stop displaying treasure : repaint background and asteroids over
                        gc_puzzle.drawImage(BACKGROUND_IMAGE, 0, 0);
                        for (Asteroid a : display_asteroid) {
                            gc_puzzle.drawImage(a.getFrame(0), a.getPositionX(), a.getPositionY());
                        }
                        display_treasure.setRecovered(true);
                        if (display_treasure.getType().equals("life")) {
                            display_player.addLife();
                        }
                        if (display_treasure.getType().equals("energy")) {
                            display_player.addEnergy();
                        }
                    } else {
                        display_treasure.setRecovered(false);
                    }
                }

                // draw the lights for the FSM
                if (current_puzzle_type == 1) {
                    for (int i = 0; i < 3; i++) {
                        gc_puzzle.drawImage(lights.get(i).getFrame(0), 512 + 128 * i + 7, 192 + 3, 50, 50);
                    }
                }

                // draw the player
                gc_puzzle.drawImage(display_player.getFrame(t), display_player.getPositionX(), display_player.getPositionY());

                drawPlayerStatus(gc_puzzle, puzzle.getPlayerLives(), puzzle.getPlayerFieldEnergy(),"puzzle" );

            }
        }.start();

        root_puzzle.getChildren().add(canvas_puzzle);
        return scene_puzzle;
    }

    /**
     * create the end of level scene and return it
     * the scene contains a message and restart and exit buttons
     * @return the end of level scene
     */
    private Scene getEndLevelScene() {
        Group root_endgame = new Group();
        Scene scene_endgame = new Scene(root_endgame);
        Canvas canvas_endgame = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc_endgame = canvas_endgame.getGraphicsContext2D();

        gc_endgame.drawImage(BACKGROUND_IMAGE, 0, 0);

        Image gui_image = new Image(".//Images/gui/yellow/panel-1.png",1300,700,false,true);
        gc_endgame.drawImage(gui_image, 50, 50);
        gc_endgame.setFill(Color.BLACK);
        gc_endgame.setFont(FONT_XLARGE);
        if (gameOver) {
            gc_endgame.fillText("GAME OVER", 550, 340);
            String text = Util.convertLanguage(language, "The ghosts got you!");
            gc_endgame.fillText(text, 490, 390);
        } else {
            String text = Util.convertLanguage(language, "Congrats! You found a new piece!");
            gc_endgame.fillText(text, 400, 340);
        }

        Button restart_button;
        if (gameOver) {
            restart_button = drawButton("Restart", gc_endgame, 200, 500, 450, BUTTON_HEIGHT, Page.MAIN);
        } else {
            restart_button = drawButton("Choose another level", gc_endgame, 200, 500, 450, BUTTON_HEIGHT, Page.MAIN);
        }
        Button exit_button = drawButton("Exit", gc_endgame, 700, 500, 450, BUTTON_HEIGHT, Page.LANGUAGE);

        root_endgame.getChildren().add(canvas_endgame);
        root_endgame.getChildren().add(restart_button);
        root_endgame.getChildren().add(exit_button);
        return scene_endgame;
    }

    /**
     * draw the important information for the game at the top of the screen
     * level, lives, field energy
     * @param gc the gc of the level
     * @param player_lives the current lives of the player
     * @param player_field_energy the current field energy of the player
     * @param type_of_game if the game is a main level or a puzzle
     */

    public void drawPlayerStatus(GraphicsContext gc, double player_lives, double player_field_energy, String type_of_game) {
        // display background of the box
        Image box = new Image(".//Images/gui/cyan/panel-1.png");
        gc.drawImage(box, 1200, 0, 250, 150);

        // display level of the current game/puzzle
        gc.setFill(Color.WHITE);
        gc.setFont(FONT_SMALL);
        if (type_of_game.equals("main")) {
            gc.fillText(Util.convertLanguage(language, "Level ") + current_game_level, 1230, 40);
        }
        if (type_of_game.equals("puzzle")) {
            gc.fillText("Puzzle", 1230, 40);
        }
        if (type_of_game.equals("choose level")) {
            gc.fillText(Util.convertLanguage(language, "Ship "), 1230, 40);
        }

        // display lives
        Image heart = new Image(".//Images/heart.png");
        for (int i = 0; i < player_lives; i++) {
            gc.drawImage(heart, 1310 + i * 30, 25, 25, 25);
        }
        // display field energy
        Image empty_bar = new Image(".//Images/bars/empty_bar.png");
        Image blue_bar = new Image(".//Images/bars/blue_bar.png");
        gc.drawImage(empty_bar,1230, 70, 150+38, 40 );
        gc.drawImage(blue_bar,1230+20, 70+8, player_field_energy*5/10, 23 );
    }

    /**
     * update the scene show on stage according to the page variable
     * @param p the page variable represents which page the game is on
     */
    private void updateScene(Page p) {
        Scene temp;
        switch (p) {
            case LANGUAGE:
                temp = getLanguageScene();
                break;
            case CREDITS:
                temp = getCreditsScene();
                break;
            case INITIAL:
                temp = getInitialScene();
                break;
            case MAIN:
                temp = getMainScene();
                break;
            case GAME:
                temp = getGameScene();
                break;
            case PUZZLE:
                temp = getPuzzleScene();
                break;
            case ENDLEVEL:
                temp = getEndLevelScene();
                break;
            default:
                Group group_default = new Group();
                temp = new Scene(group_default);
        }
        scene = temp;
        stage.setScene(temp);
    }

    /**
     * create a button
     * @param message the message shown on the button
     * @param gc the graphic context that the scene is currently on
     * @param x the x position of the button
     * @param y the y position of the button
     * @param button_w the width of the button
     * @param button_h the height of the button
     * @param destination the destination page that pressing the button will lead to 
     * @return a button containing the info passed in from parameter
     */
    private Button drawButton(String message, GraphicsContext gc, int x, int y, int button_w, int button_h, Page destination) {
        String label = Util.convertLanguage(language, message);
        Button button = new Button(label);
        button.setMinSize(button_w, button_h);
        button.setLayoutX(x);
        button.setLayoutY(y);
        Image button_image;
        if (message.equals("Restart") || message.equals("Exit") || message.equals("Choose another level")) {
            button.setStyle("-fx-background-color: transparent;-fx-border-color: transparent;-fx-text-fill: white;-fx-font-size: 25");
            button_image = new Image(".//Images/gui/purple/panel-4.png", button_w, button_h, false, true);
        } else {
            button.setStyle("-fx-background-color: transparent;-fx-border-color: transparent;-fx-text-fill: white;-fx-font-size: 20");
            button_image = new Image(".//Images/gui/purple/panel-2.png", button_w, button_h, false, true);
        }
        gc.drawImage(button_image,x,y);
        if (destination != null) {
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    page = destination;
                    updateScene(destination);
                }
            });
        }
        return button;
    }

    private void drawPlayerTreasures(GraphicsContext gc) {
        ArrayList<Image> treasures_images = new ArrayList<>();
        Image box = new Image(".//Images/gui/cyan/panel-1.png");
        gc.drawImage(box, 40, 380, 180, 120);
        for (int i=0; i<max_unlocked_level-1; i++) {
            treasures_images.add(new Image(".//Images/treasures/Treasure" + (i+1) + ".png", 30, 30, true, true));
            if (i < 3) {
                gc.drawImage(treasures_images.get(i), 65 + 50 * i, 400);
            } else {
                gc.drawImage(treasures_images.get(i), 65 + 50 * (i - 3), 450);
            }
        }
    }

    public static void main(String args[]) {
        endMainGame = false;
        gameOver = false;
        page = Page.MAIN;
        current_game_level = 1;
        max_unlocked_level = 1;
        current_puzzle_type = 1;
        current_puzzle_level = 1;
        launch(args);
    }
}