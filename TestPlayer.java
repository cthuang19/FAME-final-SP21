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

import java.util.ArrayList;

public class TestPlayer extends Application {

    public void start(Stage theStage) {
        theStage.setTitle( "Test Player" );

        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );

        Canvas canvas = new Canvas( 1600, 1200 );
        root.getChildren().add( canvas );

        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image background = new Image( ".//Images/Background-4.png" );

        Image playerI[] = new Image[1];
        for (int i=0;i<1;i++) playerI[i]=new Image(".//Images/spaceman/Spaceman1.png");
        Player player = new Player("player 1",100, 100, 3, 30, Player.PlayerState.ALIVE);
        player.setFrames(playerI);
        player.setDuration(0.1);

        /*Image rocketS[] = new Image[4];
        for (int i=0;i<4;i++) rocketS[i]=new Image(".//img//rocket_static_"+i+".png");
        Enemy enemy = new Enemy("From Vega",1100,220,40,40);
        enemy.setFrames(rocketS);
        enemy.setDuration(0.1);*/


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

        /*Image explosionI[] = new Image[8];
        for (int i=0;i<8;i++) explosionI[i]=new Image(".//img//explosion_"+i+".png");*/

        ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
        asteroids.add(new Asteroid(new Image(".//Images/asteroids/Mega/asteroidR1.png"),30,300));
        asteroids.add(new Asteroid(new Image(".//Images/asteroids/Mega/asteroidR2.png"),800,30));
        asteroids.add(new Asteroid(new Image(".//Images/asteroids/Mega/asteroidR3.png"),700,300));

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
                }
                if (input.contains("D")) {
                    player.addForces(5,0);      // RIGHT
                }
                if (input.contains("S")) {
                    player.addForces(0,5);      // DOWN
                }
                if (input.contains("A")) {      // Q on AZERTY keyboard
                    player.addForces(-5,0);     // LEFT
                }


                player.update(t, asteroids);
                //enemy.update(t);

                /*double offsetDubai = ufo.getPositionX() - 256;
                if (offsetDubai<0) offsetDubai=0;
                if (offsetDubai>1200) offsetDubai=1200;

                double offsetSky = (ufo.getPositionX()-256)/6;
                if (offsetSky<0) offsetSky=0;
                if (offsetSky>157) offsetSky=157;*/

                gc.drawImage( background, 0, 0 );
                gc.drawImage( player.getFrame(t), player.getPositionX(), player.getPositionY());
                for (Asteroid a : asteroids) {
                    gc.drawImage( a.getFrame(t), a.getPositionX_(), a.getPositionY_());
                }
                /* gc.drawImage( enemy.getFrame(t),(int) (enemy.getPositionX()-offsetDubai), enemy.getPositionY());
                gc.setFill(Color.RED);
                enemy.isInLightOfSeight(ufo,sets);
                Font theFont = Font.font( "Times New Roman", FontWeight.BOLD, 16 );
                gc.setFont( theFont );
                gc.fillText(enemy.getState(),400,30);
                gc.setFill(Color.GREEN);
                gc.fillText(enemy.getSeesUFO(),400,48);

                boolean explosion=false;
                for (MovingAnimatedImage set : sets){
                    if (ufo.intersects(set))
                    {explosion=true;}
                }
                if (explosion)
                {gc.setStroke(Color.RED);
                    gc.strokeRect((int) (ufo.getPositionX()-offsetDubai), ufo.getPositionY(),ufo.getWidth(), ufo.getHeight());
                    ufo.setFrames(explosionI);
                }
                else{ufo.setFrames(ufoI);}*/


            }
        }.start();

        theStage.show();
    }
}