import bagel.*;

/**
 * Class for normal notes
 */
public class NormalNote extends Note {
    private static final int DEFAULT_INITIAL_Y = 100;
    /**
     * Constructor for creating a normal note with a specified direction and appearance frame
     *
     * @param dir            The direction of the note
     * @param appearanceFrame The frame at which the note appears
     */
    public NormalNote(String dir, int appearanceFrame) {
        super(dir, appearanceFrame, DEFAULT_INITIAL_Y);
    }

    /**
     * Checks and score normal note
     * @param input       The game input
     * @param accuracy    The accuracy manager
     * @param targetHeight The target height for the note
     * @param relevantKey The relevant key to press/release
     * @return The score based on the accuracy of the note press
     */
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateScore(y, targetHeight, input.wasPressed(relevantKey), isStolen());

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                if (isStolen()) return 0;
                return score;
            }

        }

        return 0;
    }

    @Override
    protected String getImageResourcePrefix() {
        return "note";
    }

}

