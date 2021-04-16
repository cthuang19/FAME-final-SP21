/**
 * the class that draws treasure
 * assuming that the image of treasure does not 
 * animated for now
 */

import javafx.scene.image.Image;

public class Treasure extends AnimatedImage{
    
    //TODO: change this to a image of an element/ treasure
    private static final Image TREASURE_IMAGE = new Image(".//Images/asteroids/Mega/asteroidR4.png");

    private double positionX_;
    private double positionY_;

    public Treasure () {
        super();
        positionX_ = 0;
        positionY_ = 0;
        Image[] f = new Image[] {TREASURE_IMAGE};
    }

    public Treasure(Image image) {
        super();
        positionX_ = 0;
        positionY_ = 0;
        Image[] f = new Image[]{image};
        setFrames(f);
    }

    public Treasure(double x, double y) {
        super();
        positionX_ = x;
        positionY_ = y;
    }
    
}