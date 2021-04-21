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

        /* frames possible for the player */
        // idle
        Image iPlayerIdle[] = new Image[1];
        for (int i=0;i<1;i++) iPlayerIdle[i]=new Image(".//Images/spaceman/Spaceman1.png");

        // idle left
        Image iPlayerIdleLeft[] = new Image[5];
        for (int i=0;i<5;i++) iPlayerIdleLeft[i] = new Image(".//Images/spaceman/SpacemanIdleLeft/SpacemanIdleLeft_"+i+".png");

        // idle right
        Image iPlayerIdleRight[] = new Image[5];
        for (int i=0;i<5;i++) iPlayerIdleRight[i] = new Image(".//Images/spaceman/SpacemanIdleRight/SpacemanIdleRight_"+i+".png");

        // thrust up
        Image iPlayerThrustUp[] = new Image[5];
        for (int i=0;i<5;i++) iPlayerThrustUp[i] = new Image(".//Images/spaceman/SpacemanThrustUp/SpacemanThrustUp_"+i+".png");

        // thrust right
        Image iPlayerThrustRight[] = new Image[5];
        for (int i=0;i<5;i++) iPlayerThrustRight[i] = new Image(".//Images/spaceman/SpacemanThrustRight/SpacemanThrustRight_"+i+".png");

        // thrust down
        Image iPlayerThrustDown[] = new Image[5];
        for (int i=0;i<5;i++) iPlayerThrustDown[i] = new Image(".//Images/spaceman/SpacemanThrustDown/SpacemanThrustDown_"+i+".png");

        // thrust left
        Image iPlayerThrustLeft[] = new Image[5];
        for (int i=0;i<5;i++) iPlayerThrustLeft[i] = new Image(".//Images/spaceman/SpacemanThrustLeft/SpacemanThrustLeft_"+i+".png");


        // initialising player
        Player player = new Player("player 1",300, 200, 3, 30, Player.PlayerState.ALIVE);
        player.setFrames(iPlayerIdle);
        player.setDuration(0.1);

        // initialising ghost
        //Image ghostI[] = new Image[1];
        //for (int i=0;i<1;i++) ghostI[i]=new Image(".//Images/ghosts/ghost_test.png");
        Ghost ghost = new Ghost("ghost test", 400, 400, Ghost.Colour.RED, Ghost.GhostState.PASSIVE);
        //ghost.setFrames(ghostI);
        //ghost.setDuration(0.1);
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

        Image explosionI[] = new Image[7];
        for (int i=0;i<7;i++) explosionI[i]=new Image(".//Images/explosion/test_explosion_2/explosion_"+i+".png");

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
                    player.setFrames(iPlayerThrustUp);
                }
                if (input.contains("D")) {
                    player.addForces(5,0);      // RIGHT
                    player.setCharacterDirection(Player.CharacterDirection.RIGHT);
                    player.setFrames(iPlayerThrustRight);
                }
                if (input.contains("S")) {
                    player.addForces(0,5);      // DOWN
                    player.setCharacterDirection(Player.CharacterDirection.DOWN);
                    player.setFrames(iPlayerThrustDown);
                }
                if (input.contains("A")) {      // Q on AZERTY keyboard
                    player.addForces(-5,0);     // LEFT
                    player.setCharacterDirection(Player.CharacterDirection.LEFT);
                    player.setFrames(iPlayerThrustLeft);
                }
                if (input.contains(null)) {
                    if (player.getDirection()==Player.CharacterDirection.UP) {
                        player.setFrames(iPlayerIdleRight);
                    }
                    if (player.getDirection()==Player.CharacterDirection.RIGHT) {
                        player.setFrames(iPlayerIdleRight);
                    }
                    if (player.getDirection()==Player.CharacterDirection.DOWN) {
                        player.setFrames(iPlayerIdleLeft);
                    }
                    if (player.getDirection()==Player.CharacterDirection.LEFT) {
                        player.setFrames(iPlayerIdleLeft);
                    }
                }

                player.update(t, asteroids);
                ghost.update(t, player);

                /* offsets */
                // values are wrong
                /*double offsetAsteroidsX = player.getPositionX() - 750;
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
                if (explosion) {
                    ghost.setFrames(explosionI);
                }
                //else { ghost.setFrames(ghostI); }


            }
        }.start();

        theStage.show();
    }
}