/**
 * the class that draws treasure
 * assuming that the image of treasure is not
 * animated for now
 */

import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;

public class Treasure extends AnimatedImage{
    
    private static final Image TREASURE_IMAGE = new Image(".//Images/treasures/Treasure1.png");
    private double positionX;
    private double positionY;

    /* indicates if the player already took the treasure or not */
    private boolean recovered;

    /* indicates if the treasure is a life or some energy */
    private String type;

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
        positionX = x;
        positionY = y;
        Image[] f = new Image[]{image};
        setFrames(f);
        setDuration(0.1);
        recovered = false;
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX, positionY,frames[0].getWidth(),frames[0].getHeight());
    }

    //getter functions
    public double getPositionX() {return positionX;}

    public double getPositionY() {return positionY;}

    public boolean getRecovered() {return recovered;}

    public String getType() {return type;}

    //setter functions
    public void setType(String str) {type = str;}

    public void setRecovered(boolean b) {recovered = b;}

}