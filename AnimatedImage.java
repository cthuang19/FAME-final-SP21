import javafx.scene.image.Image;

public class AnimatedImage {
	protected Image[] frames;
	protected double duration;

    public void setFrames (Image[] f){
		frames=f;
	}
    
    public void setDuration (double d){
		duration=d;
	}
 
	public Image getFrame(double time) {
    	int index = (int)((time % (frames.length * duration)) / duration);
 		return frames[index];
    }
}
