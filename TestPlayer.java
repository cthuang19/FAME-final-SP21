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
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import javafx.geometry.Rectangle2D;

public class TestPlayer extends Application {

    public void start(Stage theStage) {
        theStage.setTitle( "Test Player" );

        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );

        Canvas canvas = new Canvas( 1500, 800 );    // adapted to the size of my screen (was too big)
        root.getChildren().add( canvas );

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image background = new Image( ".//Images/Background-4.png" );

        // initialising player
        Player player = new Player("player 1",300, 200, 3, 30, Player.PlayerState.ALIVE);
        player.initializeImages();

        // initialising ghost
        Ghost ghost = new Ghost("ghost test", 400, 400, Ghost.Colour.RED, Ghost.GhostState.PASSIVE);
        ghost.initializeImages();

        ArrayList<String> input = new ArrayList<String>();

        theScene.setOnKeyPressed(
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

        theScene.setOnKeyReleased(
                new EventHandler<KeyEvent>()
                {
                    public void handle(KeyEvent e)
                    {
                        String code = e.getCode().toString();
                        input.remove( code );
                    }
                });

        ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
        asteroids.add(new Asteroid(new Image(".//Images/asteroids/Mega/asteroidR1.png"),30,300));
        asteroids.add(new Asteroid(new Image(".//Images/asteroids/Mega/asteroidR2.png"),800,30));
        asteroids.add(new Asteroid(new Image(".//Images/asteroids/Mini/06.png"),700,600));

        final long startNanoTime = System.nanoTime();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                // for QWERTY keyboard
                player.setForces(0,0);
                if (input.contains("W")) {      // Z on AZERTY keyboard
                    player.addForces(0,-5);     // UP
                    player.setCharacterDirection(Player.CharacterDirection.UP);
                    player.updateImages("thrust up");
                }
                if (input.contains("D")) {
                    player.addForces(5,0);      // RIGHT
                    player.setCharacterDirection(Player.CharacterDirection.RIGHT);
                    player.updateImages("thrust right");
                }
                if (input.contains("S")) {
                    player.addForces(0,5);      // DOWN
                    player.setCharacterDirection(Player.CharacterDirection.DOWN);
                    player.updateImages("thrust down");
                }
                if (input.contains("A")) {      // Q on AZERTY keyboard
                    player.addForces(-5,0);     // LEFT
                    player.setCharacterDirection(Player.CharacterDirection.LEFT);
                    player.updateImages("thrust left");
                }
                if (input.contains(null)) {
                    if (player.getDirection()==Player.CharacterDirection.UP) {
                        player.updateImages("idle right");
                    }
                    if (player.getDirection()==Player.CharacterDirection.RIGHT) {
                        player.updateImages("idle right");
                    }
                    if (player.getDirection()==Player.CharacterDirection.DOWN) {
                        player.updateImages("idle left");
                    }
                    if (player.getDirection()==Player.CharacterDirection.LEFT) {
                        player.updateImages("idle left");
                    }
                }

                player.update(t, asteroids);
                ghost.update(t, player);

                /* offsets */
                // values are wrong
/*              double offsetAsteroidsX = player.getPositionX() - 750;
                if (offsetAsteroidsX<0) offsetAsteroidsX=0;
                if (offsetAsteroidsX>1600) offsetAsteroidsX=1600;

                double offsetAsteroidsY = player.getPositionY() - 400;
                if (offsetAsteroidsY<0) offsetAsteroidsY=0;
                if (offsetAsteroidsY>1200) offsetAsteroidsY=1200;

                double offsetBackgroundX = (player.getPositionX()-750)/5;
                if (offsetBackgroundX<0) offsetBackgroundX=0;
                if (offsetBackgroundX>170) offsetBackgroundX=170;

                double offsetBackgroundY = (player.getPositionY()-400)/5;
                if (offsetBackgroundY<0) offsetBackgroundY=0;
                if (offsetBackgroundY>160) offsetBackgroundY=160;

                gc.drawImage( background, offsetBackgroundX, offsetBackgroundY, 1500, 800, 0, 0, 1500, 800 );
                gc.drawImage( player.getFrame(t), player.getPositionX()-offsetAsteroidsX, player.getPositionY()-offsetAsteroidsY);
                gc.drawImage( ghost.getFrame(t), ghost.getPositionX()-offsetAsteroidsX, ghost.getPositionY()-offsetAsteroidsY);
                for (Asteroid a : asteroids) {
                    gc.drawImage( a.getFrame(t), offsetAsteroidsX+a.getPositionX_(), offsetAsteroidsY+a.getPositionY_(),  1500, 800, a.getPositionX_(), a.getPositionY_(), 1500, 800);
                }
*/
                gc.drawImage( background, 0, 0 );
                gc.drawImage( player.getFrame(t), player.getPositionX(), player.getPositionY() );
                gc.drawImage( ghost.getFrame(t), ghost.getPositionX(), ghost.getPositionY() );
                for (Asteroid a : asteroids) {
                    gc.drawImage( a.getFrame(t), a.getPositionX_(), a.getPositionY_() );
                }
                gc.setFill(Color.RED);
                Font theFont = Font.font( "Times New Roman", FontWeight.BOLD, 16 );
                gc.setFont( theFont );
                gc.fillText(String.valueOf(ghost.getGhostState()),400,30);
                gc.setFill(Color.GREEN);
                /* to use next line, go in Ghost.java, comment canSeePlayer(Player player, ArrayList<MovingAnimatedImage> obstacles)
                 * and setSeesPlayer(Player player, ArrayList<MovingAnimatedImage> obstacles)
                 * and use canSeePlayer(Player player, ArrayList<Asteroid> asteroids) instead */
                // gc.fillText(String.valueOf(ghost.canSeePlayer(player, asteroids)),400,48);

                boolean explosion = false;
                if (player.intersects(ghost)) {explosion = true;}
                if (explosion) { ghost.updateImages("explosion"); }
                else { ghost.updateImages(ghost.getStrDirection()); }


            }
        }.start();

        theStage.show();
    }
}