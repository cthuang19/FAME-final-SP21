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

    public Treasure () {
        super();
        positionX = 0;
        positionY = 0;
        Image[] f = new Image[] {TREASURE_IMAGE};
        setFrames(f);
        setDuration(0.1);
    }

    public Treasure(Image image) {
        super();
        positionX = 0;
        positionY = 0;
        Image[] f = new Image[]{image};
        setFrames(f);
        setDuration(0.1);
    }

    public Treasure(double x, double y) {
        super();
        positionX = x;
        positionY = y;
        Image[] f = new Image[] {TREASURE_IMAGE};
        setFrames(f);
        setDuration(0.1);
    }

    public Treasure(Image image, double x, double y) {
        super();
        positionX = x;
        positionY = y;
        Image[] f = new Image[]{image};
        setFrames(f);
        setDuration(0.1);
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX,positionY,frames[0].getWidth(),frames[0].getHeight());
    }

}