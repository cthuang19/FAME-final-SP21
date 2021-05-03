
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
import javafx.scene.paint.*;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;

import java.util.*;
import java.io.*;
import java.applet.AudioClip;
import javafx.event.EventHandler;
import java.beans.XMLEncoder;

import java.util.ArrayList;
import java.lang.Math;

public class GameEngine extends Application {

    public static final int CANVAS_WIDTH = 1600;
    public static final int CANVAS_HEIGHT = 800;
    public static final int BUTTON_WIDTH = 600;
    public static final int BUTTON_HEIGHT = 75;

    private static final int MAIN_GAME_DISPLAY_WIDTH = 64;

    public static final Font FONT_LARGE = Font.font("helvetica", FontWeight.LIGHT, FontPosture.REGULAR, 30);

    public static final Font FONT_SMALL = Font.font("helvetica", FontWeight.LIGHT, FontPosture.REGULAR, 15);

    public static final Image BACKGROUND_IMAGE = new Image(".//Images/Background-4.png");

    /* the page of the current scene (not sure if useful)*/
    enum Page {LANGUAGE, INITIAL, MAIN, GAME, PUZZLE};
    
    /* the language of the game */
    enum Language {ENGLISH, FRENCH};

    private Scene scene;
    private Stage stage_;

    private static Page page_;
    private static Language language_ = Language.ENGLISH;

    /* the current game level */
    private static int current_game_level_;

    /* number of the last unlocked level */
    private static int max_unlocked_level;

    /* the current (or last) puzzle level */
    private static int current_puzzle_level;
    private static int current_puzzle_type;
    private boolean goToPuzzle = false;

    @Override
    public void start(Stage theStage) {
        stage_ = theStage;
        switch (page_) {
            case LANGUAGE:
                scene = getLanguageScene();
                break;
            case INITIAL:
                scene = getInitialScene(Language.ENGLISH);
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
                break;

        }

        stage_.setScene(scene);
        stage_.show();
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

        Button english_button = new Button("English (US)");
        Button french_button = new Button("Français (Fr)");
        english_button.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        french_button.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        english_button.setLayoutX(450);
        english_button.setLayoutY(400);
        french_button.setLayoutX(450);
        french_button.setLayoutY(600);

        //set the button handler and switch to a different scene
        english_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                page_ = Page.INITIAL;
                language_ = Language.ENGLISH;
                scene = getInitialScene(language_);
                stage_.setScene(scene);
                stage_.show();
            }
        });

        french_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                page_ = Page.INITIAL;
                language_ = Language.FRENCH;
                scene = getInitialScene(language_);
                stage_.setScene(scene);
                stage_.show();
            }
        });
        
        root_language.getChildren().add(canvas_language);
        root_language.getChildren().add(english_button);
        root_language.getChildren().add(french_button);

        GraphicsContext gc_language = canvas_language.getGraphicsContext2D();

        gc_language.drawImage(BACKGROUND_IMAGE, 0, 0);
        return scene_language;
    }

    /**
     * create the initial scene according to the langauge produced
     * @param lang the language that the game is in
     * @return the initial scene
     */
    private Scene getInitialScene(Language lang) {
        //set the basic element of the initial scene
        Group root_initial = new Group();
        Scene initial = new Scene(root_initial);
        Canvas canvas_initial = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);

        String next_label = "";
        switch (lang) {
            case ENGLISH:
                next_label = "next";
                break;
            case FRENCH:
                next_label = "suivant";
                break;
            default:
                next_label = "next";    
        }

        Button next_button = new Button(next_label);
        next_button.setMinSize(100, 100);
        next_button.setLayoutX(1335);
        next_button.setLayoutY(712);
        next_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                page_ = Page.MAIN;
                scene = getMainScene();
                stage_.setScene(scene);
                stage_.show();
            }
        });

        root_initial.getChildren().add(canvas_initial);
        root_initial.getChildren().add(next_button);
        
        GraphicsContext gc_initial = canvas_initial.getGraphicsContext2D();
        
        //depends on the language of the game
        //the initial scene will load different file
        String fileName = "";
        switch (lang) {
            case ENGLISH:
                fileName = "story_english.txt";
                break;
            case FRENCH:
                fileName = "story_french.txt";
                break;
            default:
                fileName = "story_english.txt";    
        }
        
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
        Group root_main = new Group();
        Scene scene_main = new Scene(root_main);
        Canvas canvas_main = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc_main = canvas_main.getGraphicsContext2D();

        gc_main.drawImage(BACKGROUND_IMAGE, 0, 0);
        String level_label = "";
        ArrayList<Button> level_buttons = new ArrayList<Button>();
        for (int i=0; i<max_unlocked_level; i++) {
            switch (language_) {
                case ENGLISH:
                    level_label = "Level " + (i+1);
                    break;
                case FRENCH:
                    level_label = "Niveau " + (i+1);
                    break;
                default:
                    level_label = "Level " + (i+1);
            }
            level_buttons.add(new Button(level_label));
            level_buttons.get(i).setMinSize(100, 100);
            level_buttons.get(i).setLayoutX(100 + 200 * i);
            level_buttons.get(i).setLayoutY(300);

            final int j=i+1;
            level_buttons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    page_ = Page.GAME;
                    current_game_level_ = j;
                    scene = getGameScene();
                    stage_.setScene(scene);
                    stage_.show();
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
        MainGame main_game = new MainGame(current_game_level_);
        final long startNanoTime = System.nanoTime();

        ArrayList<String> input = new ArrayList<String>();

        //deal with the key events
        scene_game.setOnKeyPressed(
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

        // TODO : find where to place this to get a puzzle page
        // (it's not in the AnimationTimer)
        if (goToPuzzle) {
            page_ = Page.PUZZLE;
            scene = getPuzzleScene();
            stage_.setScene(scene);
            stage_.show();
        }

        new AnimationTimer() {
            public void handle(long current_nano_time) {
                double t = (current_nano_time - startNanoTime) / 1000000000.0;
                main_game.movePlayer(input);
                main_game.update_time(t);

                Player display_player = main_game.getPlayer();
                //System.out.println(display_player.getPositionX() + " " +display_player.getPositionY());

                if (display_player.getBeforeDoor()) {
                    goToPuzzle = true;
                    current_puzzle_type = display_player.getCurrentDoor().getPuzzleType();
                    current_puzzle_level = display_player.getCurrentDoor().getPuzzleLevel();
                    page_ = Page.PUZZLE;
                } else {
                    goToPuzzle = false;
                }

                //TODO: edit the position here
                double offsetX = display_player.getPositionX() - (CANVAS_WIDTH / 2);
				if (offsetX<0) offsetX=0;
                if (offsetX>main_game.getDimensionX() - (CANVAS_WIDTH)/2) offsetX=main_game.getDimensionX() - (CANVAS_WIDTH)/2;
                
                double offsetY = display_player.getPositionY() - (CANVAS_HEIGHT / 2);
				if (offsetY<0) offsetY=0;
                if (offsetY>main_game.getDimensionY() - (CANVAS_HEIGHT)/2) offsetY=main_game.getDimensionY() - (CANVAS_HEIGHT)/2;

                //draw background image
                gc_game.drawImage(BACKGROUND_IMAGE, 0, 0);
                for (Asteroid a: display_asteroid) {
                    gc_game.drawImage(a.getFrame(0), a.getPositionX_() - offsetX, a.getPositionY_() - offsetY);
                }

                //draw the treasure
                gc_game.drawImage(display_treasure.getFrame(0), display_treasure.getPositionX_() - offsetX,
                       display_treasure.getPositionY_() - offsetY, MAIN_GAME_DISPLAY_WIDTH, MAIN_GAME_DISPLAY_WIDTH);

                // draw the doors
                ArrayList<Door> display_door = main_game.getDoors();
                for (Door d: display_door) {
                    gc_game.drawImage(d.getFrame(0), d.getPositionX_()-offsetX, d.getPositionY_()-offsetY, MAIN_GAME_DISPLAY_WIDTH, MAIN_GAME_DISPLAY_WIDTH);
                }

                //draw the player
                gc_game.drawImage(display_player.getFrame(t), display_player.getPositionX() - offsetX, display_player.getPositionY() - offsetY);

                //draw the ghosts
                ArrayList<Ghost> display_ghosts = main_game.getAllGhost();
                for (Ghost g: display_ghosts) {
                    gc_game.drawImage(g.getFrame(t), g.getPositionX() - offsetX, g.getPositionY() - offsetY);
                }

                //display level of the current game
                gc_game.setFill(Color.LIGHTSTEELBLUE);
                gc_game.fillRect(1250, 0, 200, 100);
                gc_game.setFill(Color.BLACK);
                gc_game.setFont(FONT_SMALL);
                switch (language_) {
                    case ENGLISH:
                        gc_game.fillText("Level " + current_game_level_, 1255, 20);
                        break;
                    case FRENCH:
                        gc_game.fillText("Niveau " + current_game_level_, 1255, 20);
                        break;
                    default:
                        gc_game.fillText("Level " + current_game_level_, 1255, 20);
                        break;
                }

                //display lives
                double display_live = main_game.getPlayerLives();
                Image heart = new Image(".//Images/heart.png");
                for (int i = 0; i < display_live; i++) {
                    gc_game.drawImage(heart, 1310 + i * 30, 25, 25, 25);
                }

                // display field energy
                double display_field_energy = main_game.getPlayerFieldEnergy();

                // TODO : display this with a prettier gauge image
                gc_game.setFill(Color.DARKBLUE);
                gc_game.fillRect(1250, 60, display_field_energy*5, 40);

                // display goToPuzzle
                gc_game.setFill(Color.WHITE);
                gc_game.setFont(FONT_SMALL);
                gc_game.fillText(String.valueOf(goToPuzzle),300, 100);

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

                //draw background image
                gc_puzzle.drawImage(BACKGROUND_IMAGE, 0, 0);
                for (Asteroid a: display_asteroid) {
                    gc_puzzle.drawImage(a.getFrame(0), a.getPositionX_(), a.getPositionY_());
                }

                //draw the treasure only if the level in completed
                if (puzzle.getIsCompleted()) {
                    gc_puzzle.drawImage(display_treasure.getFrame(0), display_treasure.getPositionX_(),
                            display_treasure.getPositionY_(), MAIN_GAME_DISPLAY_WIDTH, MAIN_GAME_DISPLAY_WIDTH);
                }

                //draw the player
                gc_puzzle.drawImage(display_player.getFrame(t), display_player.getPositionX(), display_player.getPositionY());

                //display lives
                gc_puzzle.setFill(Color.LIGHTSTEELBLUE);
                gc_puzzle.fillRect(1250, 0, 200, 100);
                double display_live = puzzle.getPlayerLives();
                Image heart = new Image(".//Images/heart.png");
                for (int i = 0; i < display_live; i++) {
                    gc_puzzle.drawImage(heart, 1310 + i * 30, 25, 25, 25);
                }

                // display field energy
                double display_field_energy = puzzle.getPlayerFieldEnergy();

                // TODO : display this with a prettier gauge image
                gc_puzzle.setFill(Color.DARKBLUE);
                gc_puzzle.fillRect(1250, 60, display_field_energy*5, 40);

                gc_puzzle.setFill(Color.RED);
                gc_puzzle.setFont(FONT_SMALL);
                gc_puzzle.fillText(String.valueOf(puzzle.getIsCompleted()), 400, 10);

            }
        }.start();

        root_puzzle.getChildren().add(canvas_puzzle);
        return scene_puzzle;
    }
    
    public static void main(String args[]) {
        page_ = Page.PUZZLE;
        current_game_level_ = 1;
        max_unlocked_level = 4;
        current_puzzle_type = 1;
        current_puzzle_level = 1;
        launch(args);
    }
}