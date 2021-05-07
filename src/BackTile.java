import javafx.scene.image.Image;

/**
 * will be used for drawing purposes
 * no interaction with the other elements of the game
 */

public class BackTile extends AnimatedImage {

    private double positionX;
    private double positionY;
    private String letterType;

    public BackTile() {
        this("A",0,0);
    }

    public BackTile(String letter, double x, double y) {
        super();
        positionX = x;
        positionY = y;
        Image back_tile_image = new Image(".//Images/tileset/backtiles_test/backtile"+letter+".png");
        Image[] f = new Image[]{back_tile_image};
        setFrames(f);
        setDuration(0.1);
        letterType = letter;
    }

    //getter functions
    public double getPositionX() {return positionX;}

    public double getPositionY() {return positionY;}
}
