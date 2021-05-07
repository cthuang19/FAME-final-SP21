
/**
 * the class that draws asteroid
 * assuming that the image of asteroid is not
 * animated for now
 */

import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

public class Asteroid extends AnimatedImage{
    
//    private static final Image ASTEROID_IMAGE = new Image(".//Images/asteroids/Mega/asteroidR1.png");
    private static final Image ASTEROID_IMAGE = new Image(".//Images/tileset/tiles/tile01.png");
    private double positionX;
    private double positionY;
    private String letterType;

    public Asteroid () {
        this(ASTEROID_IMAGE,0,0);
    }

    public Asteroid(Image image) {
        this(image,0,0);
    }

    public Asteroid(double x, double y) {
        this(ASTEROID_IMAGE,x,y);
    }

    public Asteroid(Image image, double x, double y) {
        super();
        positionX = x;
        positionY = y;
        Image[] f = new Image[]{image};
        setFrames(f);
        setDuration(0.1);
        letterType = "A";
    }
    public Asteroid(String letter, double x, double y) {
        super();
        positionX = x;
        positionY = y;
        Image rock_image = new Image(".//Images/tileset/tiles_test/tile"+letter+".png");
        Image[] f = new Image[]{rock_image};
        setFrames(f);
        setDuration(0.1);
        letterType = letter;
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, 64, 64);
    }

    //getter functions
    public double getPositionX() {return positionX;}

    public double getPositionY() {return positionY;}

}