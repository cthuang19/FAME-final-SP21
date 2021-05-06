/**
 * the class that draws treasure
 * assuming that the image of treasure is not
 * animated for now
 */

import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

public class Treasure extends AnimatedImage{
    
    private static final Image TREASURE_IMAGE = new Image(".//Images/treasures/Treasure1.png");
    private double positionX_;
    private double positionY_;

    /* indicates if the player already took the treasure or not */
    private boolean recovered;

    public Treasure () {
        this(TREASURE_IMAGE,0,0);
    }

    public Treasure(Image image) {
        this(image,0,0);
    }

    public Treasure(double x, double y) {
        this(TREASURE_IMAGE,x,y);
    }

    public Treasure(Image image, double x, double y) {
        super();
        positionX_ = x;
        positionY_ = y;
        Image[] f = new Image[]{image};
        setFrames(f);
        setDuration(0.1);
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX_,positionY_,frames[0].getWidth(),frames[0].getHeight());
    }

    //getter functions
    public double getPositionX_() {return positionX_;}

    public double getPositionY_() {return positionY_;}

    public boolean getRecovered() {return recovered;}
    public void setRecovered(boolean b) {recovered = b;}

}