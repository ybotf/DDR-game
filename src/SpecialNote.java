import bagel.*;
/**
 * A class representing special notes in a rhythm game that may have unique effects when hit.
 * Extends the functionality of a NormalNote.
 */
public class SpecialNote extends NormalNote {
    private String effect = null;
    private boolean isMissed = false;

    /**
     * Constructs a new SpecialNote with a specified image directory and appearance frame.
     *
     * @param dir            The directory of the image resource.
     * @param appearanceFrame The frame at which the special note should appear.
     */
    public SpecialNote(String dir, int appearanceFrame) {
        super(dir, appearanceFrame);
        this.effect = dir;
    }

    /**
     * Checks and evaluates the score for the special note based on user input and accuracy,
     * considering its unique effects.
     *
     * @param input        The input object providing information about the key press.
     * @param accuracy     The accuracy evaluator used to determine the score.
     * @param targetHeight The target height for the special note.
     * @param relevantKey  The relevant key that was pressed.
     * @return The evaluated score for the special note, taking into account its effects.
     */
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // Evaluate accuracy of the key press
            int score = accuracy.evaluateScore(y, targetHeight, input.wasPressed(relevantKey), true);

            if (score != Accuracy.NOT_SCORED) {
                deactivate();

                if (score == Accuracy.MISS_SCORE) {
                    isMissed = true; // Disable its effect
                    return 0; // Missed special notes are not scored
                }

                if (effect.equals("2x") || effect.equals("Bomb")) {
                    return 0; // Double score and bomb notes have no scores
                }
                return score;
            }
        }
        return 0;
    }

    /**
     * Checks and returns the effect associated with the special note.
     * If the note is missed, it returns an empty string, indicating no effect.
     *
     * @param accuracy The accuracy evaluator for setting the effect description.
     * @return The effect associated with the special note or an empty string if missed.
     */
    public String checkEffect(Accuracy accuracy) {
        if (isMissed) {
            return "";
        }
        if (this.effect != null) {
            switch (effect) {
                case "2x":
                    accuracy.setAccuracy("Double Score");
                    break;
                case "SpeedUp":
                    accuracy.setAccuracy("Speed Up");
                    break;
                case "SlowDown":
                    accuracy.setAccuracy("Slow Down");
                    break;
                case "Bomb":
                    accuracy.setAccuracy("Lane Clear");
                    break;
            }
            return effect;
        }
        return null;
    }
}
