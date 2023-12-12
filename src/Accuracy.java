import bagel.Font;
import bagel.Window;
import bagel.util.Vector2;

import static java.lang.Math.abs;

/**
 * Class for dealing with accuracy of pressing the notes
 */
public class Accuracy {
    public static final int SPECIAL_SCORE = 15;
    public static final int PERFECT_SCORE = 10;
    public static final int GOOD_SCORE = 5;
    public static final int BAD_SCORE = -1;
    public static final int MISS_SCORE = -5;
    public static final int NOT_SCORED = 0;
    public static final String PERFECT = "PERFECT";
    public static final String GOOD = "GOOD";
    public static final String BAD = "BAD";
    public static final String MISS = "MISS";
    private static final int PERFECT_RADIUS = 15;
    private static final int GOOD_RADIUS = 50;
    private static final int BAD_RADIUS = 100;
    private static final int MISS_RADIUS = 200;
    private static final Font ACCURACY_FONT = new Font(ShadowDance.FONT_FILE, 40);
    private static final int RENDER_FRAMES = 30;
    private String currAccuracy = null;
    private int frameCount = 0;

    /**
     * Calculate the distance between two Vector2 points
     *
     * @param point1 The first point
     * @param point2 The second point
     * @return The distance between the two points
     */
    public static double getDistance(Vector2 point1, Vector2 point2) {
        return abs(point1.sub(point2).length());
    }

    /**
     * Detect a collision between two Vector 2 points using a target radius
     *
     * @param point1       The first point
     * @param point2       The second point
     * @param targetRadius The radius within which a collision is detected
     * @return True if a collision is detected
     */
    public static boolean detectCollision(Vector2 point1, Vector2 point2, double targetRadius) {
        return getDistance(point1, point2) <= targetRadius;
    }

    /**
     * Set the current accuracy level and reset the frame count
     *
     * @param accuracy The accuracy level to set and display
     */
    public void setAccuracy(String accuracy) {
        currAccuracy = accuracy;
        frameCount = 0;
    }

    /**
     * Evaluate the score based on the distance between the note height and target height
     *
     * @param height       The height of the note
     * @param targetHeight The target height for the note
     * @param triggered    Whether the note has been triggered or not
     * @param isSpecial    Whether the note is special or not
     * @return The score for the accuracy of the note pressed
     */
    public int evaluateScore(int height, int targetHeight, boolean triggered, boolean isSpecial) {
        int distance = abs(height - targetHeight);

        if (triggered && !isSpecial) {
            if (distance <= PERFECT_RADIUS) {
                setAccuracy(PERFECT);
                return PERFECT_SCORE;
            } else if (distance <= GOOD_RADIUS) {
                setAccuracy(GOOD);
                return GOOD_SCORE;
            } else if (distance <= BAD_RADIUS) {
                setAccuracy(BAD);
                return BAD_SCORE;
            } else if (distance <= MISS_RADIUS) {
                setAccuracy(MISS);
                return MISS_SCORE;
            }

        } else if (triggered) {
            if (distance <= GOOD_RADIUS) {
                return SPECIAL_SCORE;
            }
        } else if (height >= (Window.getHeight())) {
            // Missed special notes does not display message
            if (!isSpecial) {
                setAccuracy(MISS);
            }
            return MISS_SCORE;
        }

        return NOT_SCORED;

    }

    /**
     * Update the accuracy display for a limited number of frames
     */
    public void update() {
        frameCount++;
        if (currAccuracy != null && frameCount < RENDER_FRAMES) {
            ACCURACY_FONT.drawString(currAccuracy,
                    (double) Window.getWidth() / 2 - ACCURACY_FONT.getWidth(currAccuracy) / 2,
                    (double) Window.getHeight() / 2);
        }
    }
}
