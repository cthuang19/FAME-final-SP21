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

public class GameEngine extends Application {

    public static final int CANVAS_WIDTH = 1600;
    public static final int CANVAS_HEIGHT = 1200;
    public static final int BUTTON_WIDTH = 600;
    public static final int BUTTON_HEIGHT = 75;

    public static final Font FONT_LARGE = Font.font("helvetica", FontWeight.LIGHT, FontPosture.REGULAR, 40);

    public static final Image BACKGROUND_IMAGE = new Image(".//Images/Background-4.png");

    /* the page of the current scene (not sure if useful)*/
    enum Page {LANGUAGE, INITIAL, MAIN, GAME, PUZZLE};
    
    /* the language of the game */
    enum Language {ENGLISH, FRENCH};

    private Scene scene;
    private Stage stage_;

    private static Page page_;
    private static Language language_;

    /* the current game level */
    private static int current_game_level_;
    
    @Override
    public void start(Stage theStage) {
        stage_ = theStage;
        switch (page_) {
            case LANGUAGE:
                //scene = getLanguageScene();
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
            /* 
            case PUZZLE:
                break;
            */    
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
        Button french_button = new Button("Français (fr)");
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
        gc_initial.setFill(Color.MIDNIGHTBLUE);
        gc_initial.fillRect(50, 50, 1200, 700);
        gc_initial.fillRect(0, 0, 40, 40);
        //Drawing a Rectangle
        gc_initial.setFill(Color.LIGHTYELLOW);
        gc_initial.setFont(FONT_LARGE);
        for (int i = 0; i < all.size(); i++) {
            gc_initial.fillText(all.get(i), 120, 150 + 80 * i);
        }
        
        return initial;
    }

    public Scene getMainScene() {
        Group root_main = new Group();
        Scene scene_main = new Scene(root_main);
        Canvas canvas_main = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc_main = canvas_main.getGraphicsContext2D();

        gc_main.drawImage(BACKGROUND_IMAGE, 0, 0);
        root_main.getChildren().add(canvas_main);
        Player p = new Player("player test", 0, 0);
        return scene_main;
    }

    public Scene getGameScene() {
        Group root_game = new Group();
        Scene scene_game = new Scene(root_game);
        Canvas canvas_game = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc_game = canvas_game.getGraphicsContext2D();
        MainGame main_game = new MainGame(current_game_level_);
        //int[][] game_status = main_game.getGameArray();
        gc_game.drawImage(BACKGROUND_IMAGE, 0, 0);
        AnimatedImage[][] display = main_game.getDisplayArray();
        for (int i = 0; i < display.length; i++) {
            for (int j = 0; j < display[i].length; j++) {
                String object_type = main_game.cellType(i, j);
                if (object_type.equals("asteroid")) {
                    //gc_game.drawImage(display[i][j].getFrame(0), 20 + 40 * i, 20 + 40 * j);
                    gc_game.drawImage(display[i][j].getFrame(0), 40 * i, 40 * j);
                }
                if (object_type.equals("treasure")) {
                    gc_game.drawImage(display[i][j].getFrame(0), 40 * i, 40 * j);
                }
            }
        }
        /*
        gc_game.setFill(Color.LIGHTYELLOW);
        for (int i = 0; i < game_status.length; i++) {
            for (int j = 0; j < game_status[i].length; j++) {
                // for testing purpose now
                // switch to drawing the picture later
                gc_game.fillText(String.valueOf(game_status[i][j]), 20 + 40 * i, 20 + 40 * j); 
            }
        }
        */
        root_game.getChildren().add(canvas_game);
        return scene_game;
    }
    
    public static void main(String args[]) {
        page_ = Page.GAME;
        current_game_level_ = 1;
        launch(args);
    }
}