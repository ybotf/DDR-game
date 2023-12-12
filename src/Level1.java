public class Level1 extends Levels {
    private final static String CSV_FILE = "res/level1.csv";
    private final static String TRACK_FILE = "res/track1.wav";
    private final static int CLEAR_SCORE = 150;

    /**
     * Constructor for level 1
     */
    public Level1() {
        super(CLEAR_SCORE, TRACK_FILE);
        readCSV(CSV_FILE);
    }
}

