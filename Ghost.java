public class Ghost extends MovingAnimatedImage {

    private final double width = 7;
    private final double height = 20;
    private final double mass = 30;

    /* the colour of the ghost determine its behaviour */
    enum Colour {red, blue, orange, green};
    enum State {passive, suspicious, active, explosive};

    private Colour colour;
    private State state;

    private long timeStamp;
    private boolean seesPlayer;

    public Ghost(String n, int x, int y) {
        super(n, x, y, width, height, mass);
        colour = red;
        state = passive;
    }

    public Ghost(String n, int x, int y, Colour c, State s) {
        super(n, x, y, width, height, mass);
        colour = c;
        state = s;
    }

    public void setColour(Colour c) {this.colour = c;}
    public Colour getColour() {return this.colour;}
    public void setState(State s) {this.state = s;}
    public State getState() {return this.state;}


    @Override
    public void update(double time) {

        if (this.Color = red) {
            switch (state)
            case passive:
                if (seesPlayer) {
                    state = State.active;}
                break;
            case suspicious:
                double timePassed = (System.currentTimeMillis()-timeStamp)/1000;
                if (seesUFO) state=State.active;
                if (timePassed>4){
                    state=State.passive;
                    startingPoint=positionX;
                    velocityX=+2;}
                break;
            case active:
                if (!seesPlayer) {
                    state = State.suspicious;
                    timeStamp = System.currentTimeMillis();}
                break;
            case explosive:
                break;

        }

        if (this.Colour = blue) {
            switch (state)
            case passive:
                break;
            case suspicious:
                break;
            case active:
                break;
            case explosive:
                break;

        }

        if (this.Colour = orange) {
            switch (state)
            case passive:
                break;
            case suspicious:
                break;
            case active:
                break;
            case explosive:
                break;

        }

        if (this.Colour = green) {
            switch (state)
            case passive:
                break;
            case suspicious:
                break;
            case active:
                break;
            case explosive:
                break;

        }
    }

}