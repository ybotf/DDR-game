import bagel.*;

/**
 * Class for hold notes
 */
public class HoldNote extends Note {
    private static final int HEIGHT_OFFSET = 82;
    private static final int DEFAULT_INITIAL_Y = 24;
    private boolean holdStarted = false;

    /**
     * Constructor for creating a hold note with a specified direction and appearance frame
     *
     * @param dir            The direction of the note
     * @param appearanceFrame The frame at which the note appears
     */
    public HoldNote(String dir, int appearanceFrame) {
        super(dir, appearanceFrame, DEFAULT_INITIAL_Y);
    }

    /**
     * Start the hold of the note
     */
    public void startHold() {
        holdStarted = true;
    }

    /**
     * Check and score the hold note, considering both the start and end of the hold
     *
     * @param input       The game input
     * @param accuracy    The accuracy manager
     * @param targetHeight The target height for the note
     * @param relevantKey The relevant key to press/release
     * @return The score based on the accuracy of the note press
     */
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive() && !holdStarted) {
            int score = accuracy.evaluateScore(getBottomHeight(), targetHeight, input.wasPressed(relevantKey), false);

            if (score == Accuracy.MISS_SCORE) {
                deactivate();
                return score;
            } else if (score != Accuracy.NOT_SCORED) {
                startHold();
                return score;
            }
        } else if (isActive() && holdStarted) {

            int score = accuracy.evaluateScore(getTopHeight(), targetHeight, input.wasReleased(relevantKey), false);

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            } else if (input.wasReleased(relevantKey)) {
                deactivate();
                accuracy.setAccuracy(Accuracy.MISS);
                return Accuracy.MISS_SCORE;
            }
        }

        return 0;
    }

    /**
     * gets the location of the start of the note
     */
    private int getBottomHeight() {
        return y + HEIGHT_OFFSET;
    }

    /**
     * gets the location of the end of the note
     */
    private int getTopHeight() {
        return y - HEIGHT_OFFSET;
    }

    @Override
    protected String getImageResourcePrefix() {
        return "holdNote";
    }
}
