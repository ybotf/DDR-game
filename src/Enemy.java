import bagel.Image;
import bagel.util.Vector2;

import java.util.List;
import java.util.Random;

/**
 * Class for managing the behavior of enemy entities in the game
 */
public class Enemy {
    private final static int X_UPPER_BOUND = 900;
    private final static int X_LOWER_BOUND = 100;
    private final static int Y_UPPER_BOUND = 500;
    private final static int Y_LOWER_BOUND = 100;
    private final static int DEFAULT_SPEED = 1;
    private final static int COLLISION_RADIUS = 104;
    private final Image ENEMY_IMAGE = new Image("res/enemy.png");
    private final double speed;
    private Vector2 position;
    private boolean isMovingRight;
    private boolean isActive = true;

    /**
     * Constructor for creating an enemy with a random initial position and moving direction
     */
    public Enemy() {
        Random rand = new Random();

        // Set initial position using Vector2 within specified ranges
        double initialX = rand.nextDouble() * (X_UPPER_BOUND - X_LOWER_BOUND) + X_LOWER_BOUND;
        double initialY = rand.nextDouble() * (Y_UPPER_BOUND - Y_LOWER_BOUND) + Y_LOWER_BOUND;
        position = new Vector2(initialX, initialY);

        // Set speed and initial direction (randomly)
        speed = DEFAULT_SPEED;
        isMovingRight = rand.nextBoolean();
    }

    /**
     * Update the enemy's position and check for collisions with notes
     *
     * @param lanes list of lanes of notes
     */
    public void update(List<Lane> lanes) {
        if (isActive) {
            ENEMY_IMAGE.draw(position.x, position.y);
            // Enemy movement logic
            if (isMovingRight) {
                position = position.add(new Vector2(speed, 0));
                if (position.x >= X_UPPER_BOUND) {
                    // Change direction when reaching 900
                    isMovingRight = false;
                }
            } else {
                position = position.add(new Vector2(-speed, 0));
                if (position.x <= X_LOWER_BOUND) {
                    // Change direction when reaching 100
                    isMovingRight = true;
                }
            }
            checkCollision(lanes);
        }
    }

    /**
     * Check for collisions between the enemy and game notes in the lanes
     *
     * @param lanes list of lanes of notes
     */
    public void checkCollision(List<Lane> lanes) {
        for (Lane lane : lanes) {
            for (Note note : lane.getNotes()) {
                // Make sure note of type NormalNote and is active
                if (note.isActive() && note instanceof NormalNote && !(note instanceof SpecialNote)) {
                    if (Accuracy.detectCollision(new Vector2(lane.getLocation(), note.getY())
                            , position, COLLISION_RADIUS)) {
                        note.setStolen(true);
                    }
                }
            }
        }
    }

    /**
     * Get the current position of the enemy
     *
     * @return Vector2 representing the enemy's position
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Check if the enemy is active
     *
     * @return True if the enemy is active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Deactivate the enemy
     */
    public void deactivate() {
        isActive = false;
    }
}
