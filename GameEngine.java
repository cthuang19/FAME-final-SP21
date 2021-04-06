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

import java.util.*;
import java.io.*;
import java.applet.AudioClip;
import java.beans.XMLEncoder;

public class GameEngine extends Application {
    @Override
    public void start(Stage theStage) {
        Group root = new Group();
        Scene initial = new Scene(root);
        theStage.setScene(initial);

        Canvas canvas_initial = new Canvas(1600, 1200);
        root.getChildren().add(canvas_initial);

        GraphicsContext gc_initial = canvas_initial.getGraphicsContext2D();

        Image background = new Image(".//Images/Background-4.png");
        gc_initial.drawImage(background, 0, 0);

        theStage.show();
    }

}