public class Level2 extends Levels {
    private final static String CSV_FILE = "res/level2.csv";
    private final static String TRACK_FILE ="res/track2.wav";
    private final static int CLEAR_SCORE = 400;

    /**
     * Constructor for level 2
     */
    public Level2() {
        super(CLEAR_SCORE, TRACK_FILE);
        readCSV(CSV_FILE);
    }


}
