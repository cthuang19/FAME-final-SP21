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

import java.util.*;
import java.io.*;
import java.applet.AudioClip;
import javafx.event.EventHandler;
import java.beans.XMLEncoder;

public class GameEngine extends Application {

    public final int CANVAS_WIDTH = 1600;
    public final int CANVAS_HEIGHT = 1200;
    public final int BUTTON_WIDTH = 600;
    public final int BUTTON_HEIGHT = 75;

    enum Page {LANGUAGE, INITIAL, MAIN, GAME, PUZZLE};
    enum Language {ENGLISH, FRENCH};

    private Scene scene;
    private Stage stage_;

    private static Page page_;
    
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
            /*    
            case MAIN:

                break;
            case GAME:

                break;
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
        Scene language = new Scene(root_language);
        Canvas canvas_language = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);

        Button english_button = new Button("English (US)");
        Button french_button = new Button("Français (fr)");
        english_button.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        french_button.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        english_button.setLayoutX(450);
        english_button.setLayoutY(400);
        french_button.setLayoutX(450);
        french_button.setLayoutY(600);

        english_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                page_ = Page.INITIAL;
                scene = getInitialScene(Language.ENGLISH);
                stage_.setScene(scene);
                stage_.show();
            }
        });

        french_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                page_ = Page.INITIAL;
                scene = getInitialScene(Language.FRENCH);
                stage_.setScene(scene);
                stage_.show();
            }
        });
        
        root_language.getChildren().add(canvas_language);
        root_language.getChildren().add(english_button);
        root_language.getChildren().add(french_button);

        GraphicsContext gc_language = canvas_language.getGraphicsContext2D();

        Image background = new Image(".//Images/Background-4.png");
        gc_language.drawImage(background, 0, 0);
        return language;
    }

    /**
     * create the initial scene according to the langauge produced
     * @param lang the language that the game is in
     * @return the initial scene
     */
    private Scene getInitialScene(Language lang) {
        Group root_initial = new Group();
        Scene initial = new Scene(root_initial);
        Canvas canvas_initial = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        GraphicsContext gc_initial = canvas_initial.getGraphicsContext2D();

        root_initial.getChildren().add(canvas_initial);

        Image background = new Image(".//Images/Background-4.png");
        switch (lang) {
            case ENGLISH:
                break;
            case FRENCH:
                break;
        }
        

        return initial;
    }

    public static void main(String args[]) {
        page_ = Page.LANGUAGE;
        launch(args);
    }
}