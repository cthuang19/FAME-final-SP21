/**
 * the class that draws asteroid
 * assuming that the image of asteroid does not 
 * animated for now
 */

import javafx.scene.image.Image;

public class Asteroid extends AnimatedImage{
    
    /* image of asteroid, use background image for not*/
    private static final Image ASTEROID_IMAGE = new Image(".//Images/asteroids/Mega/asteroidR4");

    private double positionX_;
    private double positionY_;

    public Asteroid () {
        super();
        positionX_ = 0;
        positionY_ = 0;
        Image[] f = new Image[] {ASTEROID_IMAGE};
    }

    public Asteroid(Image image) {
        super();
        positionX_ = 0;
        positionY_ = 0;
        Image[] f = new Image[]{image};
        setFrames(f);
    }

    public Asteroid(double x, double y) {
        super();
        positionX_ = x;
        positionY_ = y;
    }
    
}