/**
 * the class that draws doors
 * assuming that the image of door is not
 * animated for now
 */

import javafx.scene.image.Image;

public class Door extends AnimatedImage {

    //TODO: change this to a image of an door
    private static final Image DOOR_IMAGE = new Image(".//Images/asteroids/Mega/asteroidR4.png");
    private double positionX;
    private double positionY;

    public Door () {
        super();
        positionX = 0;
        positionY = 0;
        Image[] f = new Image[] {DOOR_IMAGE};
    }

    public Door(Image image) {
        super();
        positionX = 0;
        positionY = 0;
        Image[] f = new Image[]{image};
        setFrames(f);
    }

    public Door(double x, double y) {
        super();
        positionX = x;
        positionY = y;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

}