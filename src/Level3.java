import bagel.Input;
import bagel.Keys;

import java.util.ArrayList;
import java.util.List;

public class Level3 extends Levels {
    private final static String CSV_FILE = "res/level3.csv";
    private final static String TRACK_FILE ="res/track3.wav";
    private final static int CLEAR_SCORE = 350;
    private final static int ENEMY_CREATION_RATE = 600;

    private final Guardian guardian = new Guardian();
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Projectile> projectiles = new ArrayList<>();

    /**
     * Constructor for level 3
     */
    public Level3() {
        super(CLEAR_SCORE, TRACK_FILE);
        readCSV(CSV_FILE);

    }

    /**
     * Update the game state and entities based on player input and game logic
     *
     * @param input    The game input
     * @param accuracy The accuracy manager
     */
    @Override
    public void update(Input input, Accuracy accuracy) {
        super.update(input, accuracy); // Call the update method of the superclass
        guardian.render();

        // Creating enemy every 600 frame
        if (ShadowDance.getCurrFrame() % ENEMY_CREATION_RATE == 0) {
            enemies.add(new Enemy());
        }

        // Create projectile
        if (input.wasPressed(Keys.LEFT_SHIFT)){
            if (haveEnemies(enemies)){
                projectiles.add(new Projectile(guardian, enemies));
            }
        }

        for (Projectile projectile: projectiles){
            projectile.update(enemies);
        }

        for (Enemy enemy : enemies) {
            enemy.update(getLanes());
        }
    }

    /**
     * Check if there are any active enemies in the list
     *
     * @param enemies The list of enemies to check
     * @return True if there are active enemies, otherwise false
     */
    public boolean haveEnemies(List<Enemy> enemies){
        if (enemies.isEmpty()){
            return false;
        }
        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                return true;
            }
        }
        return false;
    }
}

