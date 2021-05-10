
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

    public static final int CANVAS_WIDTH = 1600;
    public static final int CANVAS_HEIGHT = 800;
    public static final int BUTTON_WIDTH = 600;
    public static final int BUTTON_HEIGHT = 150;

    private static final int MAIN_GAME_DISPLAY_WIDTH = 64;

    public static final Font FONT_LARGE = Font.font("helvetica", FontWeight.LIGHT, FontPosture.REGULAR, 30);

    public static final Font FONT_SMALL = Font.font("helvetica", FontWeight.LIGHT, FontPosture.REGULAR, 15);

    public static final Image BACKGROUND_IMAGE = new Image(".//Images/Background-4.png");

    /* the page of the current scene (not sure if useful)*/
    enum Page {LANGUAGE, INITIAL, MAIN, GAME, PUZZLE};
    
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

    /* the current (or last) puzzle level */
    private static int current_puzzle_level;
    private static int current_puzzle_type;

    @Override
    public void start(Stage theStage) {
        stage = theStage;
        switch (page) {
            case LANGUAGE:
                scene = getLanguageScene();
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
            default:
                Group group_default = new Group();
                scene = new Scene(group_default);
        }

        stage.setScene(scene);
        stage.show();
    }
    /**
     * create the language scene and return it
     * @return the scene in selecting language
     */
    private Scene getLanguageScene() {

        //set all the layout of the language scene
        Group root_language = new Group();
        Scene scene_language = new Scene(root_language);
        Canvas canvas_language = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc_language = canvas_language.getGraphicsContext2D();

        gc_language.drawImage(BACKGROUND_IMAGE, 0, 0);

        Button english_button = new Button("English (US)");
        Button french_button = new Button("Français (Fr)");
        english_button.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        french_button.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        english_button.setLayoutX(450);
        english_button.setLayoutY(400);
        french_button.setLayoutX(450);
        french_button.setLayoutY(600);

        Image english_button_image = new Image(".//Images/gui/purple/panel-4.png",BUTTON_WIDTH, BUTTON_HEIGHT,false,true);
        Image french_button_image = new Image(".//Images/gui/blue/panel-5.png",BUTTON_WIDTH, BUTTON_HEIGHT,false,true);

        gc_language.drawImage(english_button_image,450,400);
        gc_language.drawImage(french_button_image,450,600);

        english_button.setStyle("-fx-background-color: transparent;-fx-border-color: transparent;-fx-text-fill: white;-fx-font-size: 25");
        french_button.setStyle("-fx-background-color: transparent;-fx-border-color: transparent;-fx-text-fill: white;-fx-font-size: 25");


        //set the button handler and switch to a different scene
        english_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                page = Page.INITIAL;
                language = Language.ENGLISH;
                updateScene(page);
            }
        });

        french_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                page = Page.INITIAL;
                language = Language.FRENCH;
                updateScene(page);
            }
        });
        
        root_language.getChildren().add(canvas_language);
        root_language.getChildren().add(english_button);
        root_language.getChildren().add(french_button);

        return scene_language;
    }

    /**
     * create the initial scene according to the language produced
     * @return the initial scene
     */
    private Scene getInitialScene() {
        //set the basic element of the initial scene
        Group root_initial = new Group();
        Scene initial = new Scene(root_initial);
        Canvas canvas_initial = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);

        String next_label = "";
        next_label = Util.convertLanguage(language, "next");

        Button next_button = new Button(next_label);
        next_button.setMinSize(100, 100);
        next_button.setLayoutX(1335);
        next_button.setLayoutY(712);
        next_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                page = Page.MAIN;
                scene = getMainScene();
                stage.setScene(scene);
                stage.show();
            }
        });

        root_initial.getChildren().add(canvas_initial);
        root_initial.getChildren().add(next_button);
        
        GraphicsContext gc_initial = canvas_initial.getGraphicsContext2D();
        
        //depends on the language of the game
        //the initial scene will load different file
        String fileName = "";
        fileName = Util.convertLanguage(language, "story_english.txt");
        
        FileReader fr = new FileReader(".//text_files/" + fileName);
        ArrayList<String> all = fr.allLines();
        gc_initial.drawImage(BACKGROUND_IMAGE, 0, 0);
        // drawing a rectangle background
        gc_initial.setFill(Color.MIDNIGHTBLUE);
        gc_initial.fillRect(50, 50, 1300, 700);
        // drawing the text
        gc_initial.setFill(Color.LIGHTYELLOW);
        gc_initial.setFont(FONT_LARGE);
        for (int i = 0; i < all.size(); i++) {
            gc_initial.fillText(all.get(i), 100, 150 + 80 * i);
        }
        return initial;
    }

    public Scene getMainScene() {
        endMainGame = false;
        Group root_main = new Group();
        Scene scene_main = new Scene(root_main);
        Canvas canvas_main = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc_main = canvas_main.getGraphicsContext2D();

        gc_main.drawImage(BACKGROUND_IMAGE, 0, 0);
        String level_label = "";
        ArrayList<Button> level_buttons = new ArrayList<Button>();
        ArrayList<Image> button_images = new ArrayList<>();
        for (int i=0; i<max_unlocked_level; i++) {
            level_label = Util.convertLanguage(language, "Level ") + (i + 1);
            button_images.add(new Image(".//Images/asteroids/Mega/asteroidR"+i+".png",200,200,true,true));
            gc_main.drawImage(button_images.get(i),100+300*i,300);

            level_buttons.add(new Button(level_label));
            level_buttons.get(i).setMinSize(200, 200);
            level_buttons.get(i).setLayoutX(100 + 300 * i);
            level_buttons.get(i).setLayoutY(300);

            level_buttons.get(i).setStyle("-fx-background-color: transparent;-fx-border-color: transparent;-fx-text-fill: white;-fx-font-size: 30");

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

        root_main.getChildren().add(canvas_main);
        for (int i=0; i<max_unlocked_level; i++) {
            root_main.getChildren().add(level_buttons.get(i));
        }

        Player p = new Player("player test", 0, 0);
        return scene_main;
    }

    public Scene getGameScene() {
        Group root_game = new Group();
        Scene scene_game = new Scene(root_game);
        Canvas canvas_game = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc_game = canvas_game.getGraphicsContext2D();
        MainGame main_game = new MainGame(current_game_level);
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

                if (display_player.getBeforeDoor()) {
                    current_puzzle_type = display_player.getCurrentDoor().getPuzzleType();
                    current_puzzle_level = display_player.getCurrentDoor().getPuzzleLevel();
                    page = Page.PUZZLE;
                    updateScene(page);
                    this.stop();
                }

                //TODO: edit the position here
                double offsetX = display_player.getPositionX() - CANVAS_WIDTH/2;
				if (offsetX<0) offsetX=0;
                if (offsetX>main_game.getDimensionX() - CANVAS_WIDTH/2) offsetX=main_game.getDimensionX() - CANVAS_WIDTH/2;
                
                double offsetY = display_player.getPositionY() - CANVAS_HEIGHT/2;
				if (offsetY<0) offsetY=0;
                if (offsetY>main_game.getDimensionY() - CANVAS_HEIGHT/2) offsetY=main_game.getDimensionY() - CANVAS_HEIGHT/2;

                //draw background image
                gc_game.drawImage(BACKGROUND_IMAGE, 0, 0);

                //draw the backtiles
                for (BackTile b: display_backtiles) {
                    gc_game.drawImage(b.getFrame(0), b.getPositionX() - offsetX, b.getPositionY() - offsetY);
                }

                //draw the asteroids
                for (Asteroid a: display_asteroid) {
                    gc_game.drawImage(a.getFrame(0), a.getPositionX() - offsetX, a.getPositionY() - offsetY);
                }

                //draw the treasure
                gc_game.drawImage(display_treasure.getFrame(0), display_treasure.getPositionX_() - offsetX,
                       display_treasure.getPositionY_() - offsetY, MAIN_GAME_DISPLAY_WIDTH, MAIN_GAME_DISPLAY_WIDTH);

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
                    page = Page.MAIN;

                    //TODO: probably add a delay here for ending message
                    updateScene(page);
                    this.stop();
                }

            }
        }.start();
        
        root_game.getChildren().add(canvas_game);
        return scene_game;
    }

    public Scene getPuzzleScene() {
        Group root_puzzle = new Group();
        Scene scene_puzzle = new Scene(root_puzzle);
        Canvas canvas_puzzle = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc_puzzle = canvas_puzzle.getGraphicsContext2D();
        // initialize puzzle
        PuzzleType1 puzzle = new PuzzleType1();
        switch (current_puzzle_type) {
            case 1 :
                puzzle.setLevel(current_puzzle_level);
                break;
            default :
                puzzle.setLevel(current_puzzle_level);
                break;
        }
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
        ArrayList<BackTile> display_backtiles = puzzle.getBacktiles();

        new AnimationTimer() {
            public void handle(long current_nano_time) {
                double t = (current_nano_time - startNanoTime) / 1000000000.0;
                puzzle.movePlayer(input);
                puzzle.update_time(t, input);
                switch (current_puzzle_type) {
                    case 1 :
                        puzzle.updateFSM(input);
                        break;
                    default :
                        break;
                }

                Player display_player = puzzle.getPlayer();

                //TODO : find how to get the player to have the coordinates of the door when he gets out
                display_player.setGoOutOfPuzzle();
                if (display_player.getGoOutOfPuzzle()) {
                    page = Page.GAME;
                    updateScene(page);
                    //display_player.setPosition(display_player.getCurrentDoor().getPositionX(), display_player.getCurrentDoor().getPositionY());
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

                // draw the treasure only if the level in completed
                // and set door.isCompleted to true
                if (puzzle.getIsCompleted()&&(!display_treasure.getRecovered())) {
                    gc_puzzle.drawImage(display_treasure.getFrame(0), display_treasure.getPositionX_(),
                            display_treasure.getPositionY_(), MAIN_GAME_DISPLAY_WIDTH, MAIN_GAME_DISPLAY_WIDTH);
                    //display_player.getCurrentDoor().setIsCompleted(true);
                }

                // stops displaying treasure if recovered
                if (!display_treasure.getRecovered()) {
                    if (display_player.intersects(display_treasure)) {
                        // stop displaying treasure : repaint background and asteroids over
                        gc_puzzle.drawImage(BACKGROUND_IMAGE, 0, 0);
                        for (Asteroid a : display_asteroid) {
                            gc_puzzle.drawImage(a.getFrame(0), a.getPositionX(), a.getPositionY());
                        }
                        display_treasure.setRecovered(true);
                    } else {
                        display_treasure.setRecovered(false);
                    }
                }

                // draw the player
                gc_puzzle.drawImage(display_player.getFrame(t), display_player.getPositionX(), display_player.getPositionY());

                drawPlayerStatus(gc_puzzle, puzzle.getPlayerLives(), puzzle.getPlayerFieldEnergy(),"puzzle" );

                // for FSM test
                gc_puzzle.setFill(Color.RED);
                gc_puzzle.setFont(FONT_SMALL);
                gc_puzzle.fillText(String.valueOf(puzzle.getIsCompleted()), 300, 30);

            }
        }.start();

        root_puzzle.getChildren().add(canvas_puzzle);
        return scene_puzzle;
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
        if (type_of_game == "main") {
            gc.fillText(Util.convertLanguage(language, "Level ") + current_game_level, 1230, 40);
        }
        if (type_of_game == "puzzle") {
            gc.fillText("Puzzle", 1230, 40);
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
     * update the scene show on stage
     * according to the page variable
     * @param p the page variable represents
     * which page the game is on
     */
    private void updateScene(Page p) {
        Scene temp;
        switch (p) {
            case LANGUAGE:
                temp = getLanguageScene();
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
            default:
                Group group_default = new Group();
                temp = new Scene(group_default);
        }
        scene = temp;
        stage.setScene(temp);
    }
    
    public static void main(String args[]) {
        endMainGame = false;
        page = Page.LANGUAGE;
        current_game_level = 1;
        max_unlocked_level = 3;
        current_puzzle_type = 1;
        current_puzzle_level = 1;
        launch(args);
    }
}