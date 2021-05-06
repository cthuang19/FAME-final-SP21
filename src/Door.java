/**
 * the class that draws doors
 * assuming that the image of door is not
 * animated for now
 */

import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

public class Door extends AnimatedImage {

    //TODO: change this to a image of an door
    private static final Image DOOR_IMAGE = new Image(".//Images/test_door.png");
    private double positionX;
    private double positionY;

    /* variable that checks if the puzzle in this door has been completed*/
    private boolean isCompleted;
    public void setIsCompleted(boolean b) {isCompleted = b;}

    /* defines the puzzle accessed from this door*/
    private int puzzleType;
    private int puzzleLevel;

    public Door () {
        super();
        positionX = 0;
        positionY = 0;
        Image[] f = new Image[] {DOOR_IMAGE};
        isCompleted = false;
        puzzleType = 1;
        puzzleLevel = 1;
    }

    public Door(Image image) {
        super();
        positionX = 0;
        positionY = 0;
        Image[] f = new Image[]{image};
        setFrames(f);
        isCompleted = false;
    }

    public Door(double x, double y) {
        super();
        positionX = x;
        positionY = y;
        Image[] f = new Image[] {DOOR_IMAGE};
        setFrames(f);
        isCompleted = false;
    }

    public Door(double x, double y, int pt, int pl) {
        super();
        positionX = x;
        positionY = y;
        Image[] f = new Image[] {DOOR_IMAGE};
        setFrames(f);
        isCompleted = false;
        puzzleType = pt;
        puzzleLevel = pl;
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY, 64, 64);
    }

    //getter functions
    public boolean getIsCompleted() {return isCompleted;}

    public double getPositionX() {return positionX;}

    public double getPositionY() {return positionY;}

    public int getPuzzleType() {return puzzleType;}

    public int getPuzzleLevel() {return puzzleLevel;}

}