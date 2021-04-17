/**
 * the class that draws doors
 * assuming that the image of door is not
 * animated for now
 */

import javafx.scene.image.Image;

public class Door extends AnimatedImage {

    //TODO: change this to a image of an door
    private static final Image DOOR_IMAGE = new Image(".//Images/asteroids/Mega/asteroidR4.png");
    private double positionX_;
    private double positionY_;

    public Door () {
        super();
        positionX_ = 0;
        positionY_ = 0;
        Image[] f = new Image[] {DOOR_IMAGE};
    }

    public Door(Image image) {
        super();
        positionX_ = 0;
        positionY_ = 0;
        Image[] f = new Image[]{image};
        setFrames(f);
    }

    public Door(double x, double y) {
        super();
        positionX_ = x;
        positionY_ = y;
    }

}