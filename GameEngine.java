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
import java.beans.XMLEncoder;

public class GameEngine extends Application {

    public final int CANVAS_WIDTH = 1600;
    public final int CANVAS_HEIGHT = 1200;
    public final int BUTTON_WIDTH = 800;
    public final int BUTTON_HEIGHT = 100;
    
    public boolean state;
    @Override
    public void start(Stage theStage) {
        Group root = new Group();
        Scene initial = new Scene(root);
        theStage.setScene(initial);

        Canvas canvas_initial = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);

        Button english_button = new Button("English");
        english_button.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        root.getChildren().add(canvas_initial);
        root.getChildren().add(english_button);

        GraphicsContext gc_initial = canvas_initial.getGraphicsContext2D();

        Image background = new Image(".//Images/Background-4.png");
        gc_initial.drawImage(background, 0, 0);

        theStage.show();
    }

    

}