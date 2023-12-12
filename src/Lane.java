import bagel.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for the lanes which notes fall down
 */
public class Lane {
    private static final int HEIGHT = 384;
    private static final int TARGET_HEIGHT = 657;
    private static final int EFFECT_FRAMES = 480;
    private static final int SPEED_EFFECT = 1;
    private static final int SCORE_MULTIPLIER = 2;

    private final String type;
    private final Image image;
    private Keys relevantKey;
    private final int location;
    private int currNote = 0;

    private final List<Note> notes = new ArrayList<>();

    /**
     * Constructor for creating a game lane
     *
     * @param dir      The type of lane
     * @param location The x position of the lane
     */
    public Lane(String dir, int location) {
        this.type = dir;
        this.location = location;
        image = new Image("res/lane" + dir + ".png");
        switch (dir) {
            case "Left":
                relevantKey = Keys.LEFT;
                break;
            case "Right":
                relevantKey = Keys.RIGHT;
                break;
            case "Up":
                relevantKey = Keys.UP;
                break;
            case "Down":
                relevantKey = Keys.DOWN;
                break;
            case "Special":
                relevantKey = Keys.SPACE;
                break;
        }
    }

    /**
     * Get the type of the lane
     *
     * @return The type of the lane
     */
    public String getType() {
        return type;
    }

    /**
     * Get the list of notes in the lane
     *
     * @return The list of notes in the lane
     */
    public List<Note> getNotes() {return notes;}

    /**
     * Get the horizontal location of the lane
     *
     * @return The horizontal location of the lane
     */
    public int getLocation() {return location;}

    /**
     * Deactivate all active notes in the lane
     */
    public void deactivateAllActiveNotes() {
        for (Note note : notes) {
            if (note.isActive()) {
                note.deactivate();
            }
        }
    }

    /**
     * Update all the notes in the lane and handle game effects
     *
     * @param input    The game input
     * @param accuracy The accuracy manager
     * @param level    The game level
     * @return The score based on the accuracy of the note press
     */
    public int update(Input input, Accuracy accuracy, Levels level) {
        // Making effect last for set amount of frames
        level.setFrameCount(level.getFrameCount()+1);
        if (level.getFrameCount() > EFFECT_FRAMES){
            level.setScoreMultiplier(1);
        }
        draw();
        for (int i = currNote; i < notes.size(); i++) {
            Note note = notes.get(i);
            note.update(level.getSpeedEffect());

            int score = note.checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);

            if (note.isCompleted()) {
                if (note instanceof SpecialNote) {
                    String effect = ((SpecialNote) note).checkEffect(accuracy);

                    switch (effect) {
                        case "SpeedUp":
                            level.setSpeedEffect(level.getSpeedEffect() + SPEED_EFFECT);
                            break;
                        case "SlowDown":
                            level.setSpeedEffect(level.getSpeedEffect() - SPEED_EFFECT);
                            break;
                        case "2x":
                            level.setFrameCount(0);
                            level.setScoreMultiplier(level.getScoreMultiplier() * SCORE_MULTIPLIER);
                            break;
                        case "Bomb":
                            deactivateAllActiveNotes();
                            break;
                    }
                }
                currNote++;
                return score * level.getScoreMultiplier();
            }
        }
        return Accuracy.NOT_SCORED;
    }

    /**
     * Add a note to the lane
     *
     * @param note The note to be added to the lane
     */
    public void addNote(Note note) {
        notes.add(note);
    }

    /**
     * Check if the lane is finished, which occurs when all notes have been pressed or missed
     *
     * @return True if the lane is finished, false otherwise
     */
    public boolean isFinished() {
        return currNote >= notes.size();
    }

    /**
     * Draws the lane and the notes
     */
    public void draw() {
        image.draw(location, HEIGHT);

        for (int i = currNote; i < notes.size(); i++) {
            if(!notes.get(i).isStolen()){
                notes.get(i).draw(location);
            }
        }
    }

}

