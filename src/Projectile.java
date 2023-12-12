import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Vector2;

import java.util.List;
/**
 * A class representing a projectile fired by a Guardian to hit the closest enemy.
 */
public class Projectile {
    private final Image PROJECTILE_IMAGE = new Image("res/arrow.png");
    private Vector2 position = new Vector2();
    private Vector2 velocity = new Vector2();
    DrawOptions rotation = new DrawOptions();
    private boolean isActive = true;

    /**
     * Constructs a new projectile for a Guardian to target the closest enemy.
     *
     * @param guardian The Guardian firing the projectile.
     * @param enemies  A list of enemies to target.
     */
    public Projectile(Guardian guardian, List<Enemy> enemies) {
        position = guardian.getPosition();
        Enemy target = closestEnemy(enemies);

        velocity = target.getPosition().sub(this.position);
        velocity = velocity.normalised().mul(6);

        rotation.setRotation(Math.atan2(velocity.y, velocity.x));
    }

    /**
     * Updates the position and state of the projectile.
     *
     * @param enemies A list of enemies for collision detection.
     */
    public void update(List<Enemy> enemies) {
        if (isActive) {
            // Check distance to enemy
            if (position.x < 0 || position.x > ShadowDance.getWindowWidth()
                    || position.y < 0 || position.y > ShadowDance.getWindowHeight()) {
                isActive = false; // Deactivate the projectile
            }

            position = position.add(velocity);
            PROJECTILE_IMAGE.draw(position.x, position.y, rotation);

            this.checkCollision(enemies);
        }
    }

    /**
     * Finds the closest active enemy to the projectile.
     *
     * @param enemies A list of enemies to search for the closest one.
     * @return The closest active enemy, or null if there are no active enemies.
     */
    public Enemy closestEnemy(List<Enemy> enemies) {
        double minDistance = ShadowDance.getWindowWidth();
        Enemy closestEnemy = null;

        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                double distance = Accuracy.getDistance(enemy.getPosition(), this.position);

                if (distance < minDistance) {
                    closestEnemy = enemy;
                    minDistance = distance;
                }
            }
        }
        return closestEnemy;
    }

    /**
     * Checks for collisions between the projectile and enemies and deactivates both upon collision.
     *
     * @param enemies A list of enemies to check for collisions with the projectile.
     */
    public void checkCollision(List<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                if (Accuracy.detectCollision(enemy.getPosition(), this.position, 64)) {
                    this.deactivate();
                    enemy.deactivate();
                    return;
                }
            }
        }
    }

    /**
     * Deactivates the projectile, marking it as inactive.
     */
    public void deactivate() {
        isActive = false;
    }
}
