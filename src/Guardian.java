import bagel.Image;
import bagel.util.Vector2;

/**
 * Class for managing the behavior of a guardian entity in the game
 */
public class Guardian {
    private final Image GUARDIAN_IMAGE = new Image("res/guardian.png");
    private final static double GUARDIAN_X = 800;
    private final static double GUARDIAN_Y = 600;
    private final Vector2 position;

    /**
     * Constructor for creating a guardian at specified position
     */
    public Guardian() {
        position = new Vector2(GUARDIAN_X, GUARDIAN_Y);
    }

    /**
     * Get the current position of the guardian
     *
     * @return Vector2 representing the guardian's position
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Render the guardian at its current position
     */
    public void render() {
        GUARDIAN_IMAGE.draw(position.x, position.y);
    }
}
