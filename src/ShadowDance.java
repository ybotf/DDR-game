import bagel.*;

/**
 * Base on sample solution for SWEN20003 Project 1, Semester 2, 2023 from Stella Li
 * <p>
 * Project 2B edited and implemented by
 * @author Toby Fung
 */
public class ShadowDance extends AbstractGame  {

    private static final int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    public final static String FONT_FILE = "res/FSO8BITR.TTF";
    private final static int TITLE_X = 220;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 100;
    private final static int INS_Y_OFFSET = 190;
    private final static int SCORE_LOCATION = 35;
    private final static int END_MESSAGE_HEIGHT = 300;
    private final static int RETRY_MESSAGE_HEIGHT = 500;

    private final Font TITLE_FONT = new Font(FONT_FILE, 64);
    private final Font INSTRUCTION_FONT = new Font(FONT_FILE, 24);
    private final Font SCORE_FONT = new Font(FONT_FILE, 30);
    private static final String INSTRUCTIONS = "SELECT LEVELS WITH\n" +
            "NUMBER KEYS\n\n" +
            "1          2          3";
    private static final String CLEAR_MESSAGE = "CLEAR!";
    private static final String TRY_AGAIN_MESSAGE = "TRY AGAIN";
    private static final String RETRY_MESSAGE = "PRESS SPACE TO RETURN TO LEVEL SELECTION";

    private final Accuracy accuracy = new Accuracy();
    private static int currFrame = 0;
    private boolean started = false;
    private boolean finished = false;
    private boolean paused = false;

    private Levels level = null;

    public ShadowDance(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }


    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     *
     * @param input Input manager
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (!started) {
            // starting screen
            TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString(INSTRUCTIONS,
                    TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);

            if (input.wasPressed(Keys.NUM_1)) {
                started = true;
                level = new Level1();
            } else if (input.wasPressed(Keys.NUM_2)) {
                started = true;
                level = new Level2();
            } else if (input.wasPressed(Keys.NUM_3)) {
                started = true;
                level = new Level3();
            }
        } else if (finished) {
            // end screen
            String endMessage = (level.getScore() >= level.getClearScore()) ? CLEAR_MESSAGE : TRY_AGAIN_MESSAGE;

            TITLE_FONT.drawString(endMessage,
                    (double) WINDOW_WIDTH /2 - TITLE_FONT.getWidth(endMessage)/2,
                    END_MESSAGE_HEIGHT);

            INSTRUCTION_FONT.drawString(RETRY_MESSAGE,
                    (double) WINDOW_WIDTH /2 - INSTRUCTION_FONT.getWidth(RETRY_MESSAGE)/2,
                    RETRY_MESSAGE_HEIGHT);

            if (input.wasPressed(Keys.SPACE)){
                accuracy.setAccuracy(null);
                currFrame = 0;
                started = false;
                finished = false;
            }

        } else {
            // gameplay
//            level.getTrack().start();

            SCORE_FONT.drawString("Score " + level.getScore(), SCORE_LOCATION, SCORE_LOCATION);

            if (paused) {
                if (input.wasPressed(Keys.TAB)) {
                    paused = false;
                    level.getTrack().run();
                }

                level.pause();

            } else {
                currFrame++;
                level.update(input, accuracy);
                accuracy.update();
                finished = level.checkFinished();
                if (input.wasPressed(Keys.TAB)) {
                    paused = true;
                    level.getTrack().pause();
                }
            }
        }
    }
    /**
     * Gets the current frame number of the game.
     *
     * @return The current frame number.
     */
    public static int getCurrFrame() {
        return currFrame;
    }

    /**
     * Gets the width of the game window.
     *
     * @return The width of the game window.
     */
    public static int getWindowWidth() {
        return WINDOW_WIDTH;
    }

    /**
     * Gets the height of the game window.
     *
     * @return The height of the game window.
     */
    public static int getWindowHeight() {
        return WINDOW_HEIGHT;
    }

}

