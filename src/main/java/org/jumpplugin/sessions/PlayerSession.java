package org.jumpplugin.sessions;

public class PlayerSession {
    private final String courseName;
    private int lastCheckpointId = 0;
    private int score = 0;

    public PlayerSession(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() { return courseName; }
    public int getLastCheckpointId() { return lastCheckpointId; }
    public void setLastCheckpointId(int id) { this.lastCheckpointId = id; }

    public int getScore() { return score; }
    public void addPoint() { this.score++; }
}
