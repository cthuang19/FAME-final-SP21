/**
 * the class that draws asteroid
 * assuming that the image of asteroid is not
 * animated for now
 */

import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

public class Asteroid extends AnimatedImage{
    
    //TODO: still figuring how to resize the image and made it fit into 40*40 pixel size
    //private static final Image ASTEROID_IMAGE = new Image(".//Images/asteroids/Mega/asteroidR1.png");
    private static final Image ASTEROID_IMAGE = new Image(".//Images/tileset/tiles/tile01.png");
    private double positionX;
    private double positionY;

    public Asteroid () {
        super();
        positionX = 0;
        positionY = 0;
        Image[] f = new Image[] {ASTEROID_IMAGE};
        setFrames(f);
        setDuration(0.1);
    }

    public Asteroid(Image image) {
        super();
        positionX = 0;
        positionY = 0;
        Image[] f = new Image[]{image};
        setFrames(f);
        setDuration(0.1);
    }

    public Asteroid(double x, double y) {
        super();
        positionX = x;
        positionY = y;
        Image[] f = new Image[] {ASTEROID_IMAGE};
        setFrames(f);
        setDuration(0.1);
    }

    public Asteroid(Image image, double x, double y) {
        super();
        positionX = x;
        positionY = y;
        Image[] f = new Image[]{image};
        setFrames(f);
        setDuration(0.1);
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX,positionY,frames[0].getWidth(),frames[0].getHeight());
    }

}