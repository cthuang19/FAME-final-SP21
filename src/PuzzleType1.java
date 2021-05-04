// just a test, don't know if it will be useful
// or if we can just use the Puzzle class

import java.util.ArrayList;

/** a puzzle type 1 is a pattern printed on the background
 * that the player must decipher in order to do with the
 * directional keys the reight pattern
 * **/


public class PuzzleType1 extends Puzzle {

    // states of the FSM that will recognize the pattern
    //enum PuzzleState {ZERO, ONE, TWO, THREE};
    //private PuzzleState state;
    private int state;
    private long timeStamp;

    public PuzzleType1() {
        super(1, 1);
    }

    public PuzzleType1(int lvl) {
        super(1, lvl);
    }

    // for now, a basic FSM : recognizes a pattern of UP, RIGHT, LEFT
    /** update of the FSM that will recognize the pattern
     * updates the variable isCompleted
     * @param input a keyboard event
     * **/
    public void updateFSM(ArrayList<String> input) {
        switch (state) {
            case 0 :
                if (input.contains("W")) {      // Z on AZERTY keyboard
                    this.state = 1;
                    timeStamp = System.currentTimeMillis();
                } else {
                    this.state = 0;
                }
                break;
            case 1 :
                // if UP, stay here + new timeStamp
                // else if RIGHT, go to 2 + new timeStamp
                //      else if nothing, stay here
                //           else (if every key else) go back to 0
                if ((System.currentTimeMillis()-timeStamp)/1000 < 1) {
                    if (input.contains("W")) {      // Z on AZERTY keyboard
                        this.state = 1;
                        timeStamp = System.currentTimeMillis();
                    } else {
                        if (input.contains("D")) {
                            this.state = 2;
                            timeStamp = System.currentTimeMillis();
                        } else {
                            if (input.isEmpty()) {
                                this.state = 1;
                            } else {
                                //this.state = 0;
                            }
                        }
                    }
                } else {
                    this.state = 0;
                }
                break;
            case 2 :
                if ((System.currentTimeMillis()-timeStamp)/1000 < 1) {
                    if (input.contains("W")) {      // Z on AZERTY keyboard
                        this.state = 1;
                        timeStamp = System.currentTimeMillis();
                    } else {
                        if (input.contains("A")) {  // Q on AZERTY keyboard
                            this.state = 3;
                            timeStamp = System.currentTimeMillis();
                        } else {
                            if (input.isEmpty()) {
                                this.state = 2;
                            } else {
                                //this.state = 0;
                            }
                        }
                    }
                } else {
                    this.state = 0;
                }
                break;
            case 3 :
                this.isCompleted = true;
                break;
            default :
                this.state = 0;
                break;
        }
        System.out.println(state);
    }


}