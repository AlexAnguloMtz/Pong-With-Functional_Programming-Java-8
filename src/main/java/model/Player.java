package model;

public class Player {

    private int score;

    public void updateScore(int amount) {
        this.score += amount;
    }

    public int getScore() {
        return score;
    }

}
