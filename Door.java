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
    private double positionX_;
    private double positionY_;

    /* variable that checks if the puzzle in this door has been completed*/
    private boolean isComplete;

    /* defines the puzzle accessed from this door*/
    private int puzzleType;
    private int puzzleLevel;

    public Door () {
        super();
        positionX_ = 0;
        positionY_ = 0;
        Image[] f = new Image[] {DOOR_IMAGE};
        isComplete = false;
        puzzleType = 1;
        puzzleLevel = 1;
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

    public Door(double x, double y, int pt, int pl) {
        super();
        positionX_ = x;
        positionY_ = y;
        Image[] f = new Image[] {DOOR_IMAGE};
        setFrames(f);
        isComplete = false;
        puzzleType = pt;
        puzzleLevel = pl;
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX_ , positionY_, 64, 64);
    }

    //getter functions
    public boolean getIsComplete() {return isComplete;}

    public double getPositionX_() {return positionX_;}

    public double getPositionY_() {return positionY_;}

    public int getPuzzleType() {return puzzleType;}

    public int getPuzzleLevel() {return puzzleLevel;}

}