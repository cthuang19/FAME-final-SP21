/**
 * the class that draws doors
 * assuming that the image of door is not
 * animated for now
 */

import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

public class Door extends AnimatedImage {

    //TODO: change this to a image of an door
    private static final Image DOOR_IMAGE = new Image(".//Images/asteroids/Mega/asteroidR4.png");
    private double positionX_;
    private double positionY_;

    /* variable that checks if the puzzle in this door has been completed*/
    private boolean isComplete;

    public Door () {
        super();
        positionX_ = 0;
        positionY_ = 0;
        Image[] f = new Image[] {DOOR_IMAGE};
        isComplete = false;
    }

    public Door(Image image) {
        super();
        positionX_ = 0;
        positionY_ = 0;
        Image[] f = new Image[]{image};
        setFrames(f);
        isComplete = false;
    }

    public Door(double x, double y) {
        super();
        positionX_ = x;
        positionY_ = y;
        Image[] f = new Image[] {DOOR_IMAGE};
        setFrames(f);
        isComplete = false;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public double getPositionX_() {
        return positionX_;
    }

    public double getPositionY_() {
        return positionY_;
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX_,positionY_,frames[0].getWidth(),frames[0].getHeight());
    }

}