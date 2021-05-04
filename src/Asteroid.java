
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
    private double positionX_;
    private double positionY_;

    public Asteroid () {
        super();
        positionX_ = 0;
        positionY_ = 0;
        Image[] f = new Image[] {ASTEROID_IMAGE};
        setFrames(f);
        setDuration(0.1);
    }

    public Asteroid(Image image) {
        super();
        positionX_ = 0;
        positionY_ = 0;
        Image[] f = new Image[]{image};
        setFrames(f);
        setDuration(0.1);
    }

    public Asteroid(double x, double y) {
        super();
        positionX_ = x;
        positionY_ = y;
        Image[] f = new Image[] {ASTEROID_IMAGE};
        setFrames(f);
        setDuration(0.1);
    }

    public Asteroid(Image image, double x, double y) {
        super();
        positionX_ = x;
        positionY_ = y;
        Image[] f = new Image[]{image};
        setFrames(f);
        setDuration(0.1);
    }

    public Rectangle2D getBoundary() {
        //return new Rectangle2D(positionX_,positionY_,frames[0].getWidth(),frames[0].getHeight());
        return new Rectangle2D(positionX_ , positionY_, 64, 64);
    }

    //getter functions
    public double getPositionX_() {return positionX_;}

    public double getPositionY_() {return positionY_;}

}