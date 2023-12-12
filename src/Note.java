import bagel.Image;
import bagel.Input;
import bagel.Keys;

/**
 * Abstract parent class for notes
 */
public abstract class Note {
    private final Image image;
    private final int appearanceFrame;

    protected int y;
    protected boolean active = false;
    protected boolean completed = false;
    private boolean isStolen = false;

    /**
     * Constructor for Note
     *
     * @param dir            The directory of the image resource
     * @param appearanceFrame The frame at which the note should appear
     * @param initialY       The initial vertical position of the note
     */
    public Note(String dir, int appearanceFrame, int initialY) {
        image = new Image("res/" + getImageResourcePrefix() + dir + ".png");
        this.appearanceFrame = appearanceFrame;
        y = initialY;
    }

    /**
     * Checks if a note is stolen by enemy
     * @return True of stolen
     */
    public boolean isStolen() {
        return isStolen;
    }

    /**
     * Set if a note is stolen by enemy
     * @param stolen True if stolen
     */
    public void setStolen(boolean stolen) {
        isStolen = stolen;
    }

    /**
     * Gets the current vertical position of the note.
     *
     * @return The vertical position of the note.
     */
    public int getY() {
        return y;
    }

    /**
     * Checks if the note is active (visible and can be interacted with).
     *
     * @return `true` if the note is active, `false` otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Checks if the note has been completed (scored or missed).
     *
     * @return `true` if the note is completed, `false` otherwise.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Deactivates the note, marking it as completed.
     */
    public void deactivate() {
        active = false;
        completed = true;
    }

    /**
     * Updates the note's position and activation status.
     *
     * @param s The speed at which the note should move.
     */
    public void update(int s) {
        if (active) {
            y += s;
        }

        if (ShadowDance.getCurrFrame() >= appearanceFrame && !completed) {
            active = true;
        }
    }

    /**
     * Draws the note on the screen if it is active.
     *
     * @param x The horizontal position at which to draw the note.
     */
    public void draw(int x) {
        if (active) {
            image.draw(x, y);
        }
    }

    /**
     * Checks and evaluates the score for the note based on user input and accuracy.
     *
     * @param input        The input object providing information about the key press.
     * @param accuracy     The accuracy evaluator used to determine the score.
     * @param targetHeight The target height for the note.
     * @param relevantKey  The relevant key that was pressed.
     * @return The evaluated score for the note.
     */
    public abstract int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey);

    /**
     * Get the image resource prefix for the specific note type.
     *
     * @return The image resource prefix for the note type.
     */
    protected abstract String getImageResourcePrefix();
}
