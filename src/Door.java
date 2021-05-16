/**
 * the class that draws doors
 * assuming that the image of door is not
 * animated for now
 */

import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

public class Door extends AnimatedImage {

    private static final Image DOOR_IMAGE = new Image(".//Images/door/door_def.png");
    private double positionX;
    private double positionY;

    /* variable that checks if the puzzle in this door has been completed*/
    private boolean isCompleted;
    public void setIsCompleted(boolean b) {isCompleted = b;}

    /* defines the puzzle accessed from this door*/
    private int puzzleType;
    private int puzzleLevel;

    public Door () {
        this(DOOR_IMAGE,0,0,1,1);
    }

    public Door(Image image) {
        this(image,0,0,1,1);
    }

    public Door(double x, double y) {
        this(x,y,1,1);
    }

    public Door(double x, double y, int pt, int pl) {
        this(DOOR_IMAGE,x,y,pt,pl);
    }

    public Door(Image image, double x, double y, int pt, int pl) {
        super();
        positionX = x;
        positionY = y;
        Image[] f = new Image[] {image};
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