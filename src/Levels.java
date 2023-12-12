import bagel.Input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public abstract class Levels {
    private final static int DEFAULT_SPEED = 2;
    private final static int DEFAULT_SCORE_MULTIPLIER = 1;
    private final Track track;
    private final int clearScore;
    private int speedEffect = DEFAULT_SPEED;
    private int scoreMultiplier = DEFAULT_SCORE_MULTIPLIER;
    private int frameCount = 0;
    private int score = 0;
    private final List<Lane> lanes = new ArrayList<>();

    /**
     * Constructor for creating a game level
     *
     * @param clearScore The score required to clear the level
     * @param trackFile  The file path to the track definition
     */
    public Levels(int clearScore, String trackFile) {
        this.clearScore = clearScore;
        this.track = new Track(trackFile);
    }

    /**
     * Read a CSV file to initialize game elements such as lanes and notes
     *
     * @param filePath The file path to the CSV file
     */
    public void readCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String textRead;
            while ((textRead = br.readLine()) != null) {
                String[] splitText = textRead.split(",");

                if (splitText[0].equals("Lane")) {
                    // reading lanes
                    String laneType = splitText[1];
                    int pos = Integer.parseInt(splitText[2]);
                    Lane lane = new Lane(laneType, pos);
                    lanes.add(lane);
                } else {
                    // reading notes
                    String dir = splitText[0];
                    Lane lane = null;
                    for (Lane existingLane : lanes) {
                        if (existingLane.getType().equals(dir)) {
                            lane = existingLane;
                            break;
                        }
                    }

                    if (lane != null) {
                        String noteType = splitText[1];
                        if (noteType.equals("DoubleScore")) {
                            noteType = "2x";
                        }
                        if ("Normal".equals(noteType) || "Hold".equals(noteType)) {
                            Note note = ("Normal".equals(noteType))
                                    ? new NormalNote(dir, Integer.parseInt(splitText[2]))
                                    : new HoldNote(dir, Integer.parseInt(splitText[2]));
                            lane.addNote(note);

                        } else if ("SpeedUp".equals(noteType) || "SlowDown".equals(noteType)
                                || "Bomb".equals(noteType) || "2x".equals(noteType)) {
                            // Handle SpeedUp, SlowDown, Bomb, DoubleScore
                            Note note = new SpecialNote(noteType, Integer.parseInt(splitText[2]));
                            lane.addNote(note);
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Get the list of lanes in the level
     *
     * @return The list of lanes
     */
    public List<Lane> getLanes() {
        return lanes;
    }

    /**
     * Get the track associated with the level
     *
     * @return The game track
     */
    public Track getTrack() {
        return track;
    }

    /**
     * Get the score required to clear the level
     *
     * @return The clear score
     */
    public int getClearScore() {
        return clearScore;
    }

    /**
     * Get the current score in the level
     *
     * @return The current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Add a score to the current total score
     *
     * @param score The score to add
     */
    public void addScore(int score) {
        this.score += score;
    }

    /**
     * Get the current frame count
     *
     * @return The frame count
     */
    public int getFrameCount() {
        return frameCount;
    }

    /**
     * Set the frame count
     *
     * @param frameCount The new frame count
     */
    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    /**
     * Get the current speed effect
     *
     * @return The speed effect
     */
    public int getSpeedEffect() {
        return speedEffect;
    }

    /**
     * Set the speed effect
     *
     * @param speedEffect The new speed effect
     */
    public void setSpeedEffect(int speedEffect) {
        this.speedEffect = speedEffect;
    }

    /**
     * Get the current score multiplier
     *
     * @return The score multiplier
     */
    public int getScoreMultiplier() {
        return scoreMultiplier;
    }

    /**
     * Set the score multiplier
     *
     * @param scoreMultiplier The new score multiplier
     */
    public void setScoreMultiplier(int scoreMultiplier) {
        this.scoreMultiplier = scoreMultiplier;
    }

    /**
     * Update the game state based on player input and game logic
     *
     * @param input    The game input
     * @param accuracy The accuracy manager
     */
    public void update(Input input, Accuracy accuracy) {
        for (Lane lane : getLanes()) {
            if (lane != null) {
                addScore(lane.update(input, accuracy, this));
            }
        }
    }

    /**
     * Pauses the game
     */
    public void pause() {
        for (Lane lane : getLanes()) {
            if (lane != null) {
                lane.draw();
            }
        }
    }

    /**
     * Checks if game is finished
     * @return True if game is finished
     */
    public boolean checkFinished() {
        for (Lane lane : getLanes()) {
            if (lane != null && !lane.isFinished()) {
                return false;
            }
        }
        return true;
    }

}

