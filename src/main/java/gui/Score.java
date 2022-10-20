package gui;

import model.Player;

import javax.swing.*;
import java.awt.*;

import static java.awt.Color.WHITE;
import static java.awt.Font.BOLD;

public class Score extends JComponent {

    private static final Font FONT = new Font("Arial", BOLD, 80);
    private static final int Y = 100;

    private final Player playerOne;
    private final Player playerTwo;

    public Score() {
        this.playerOne = new Player();
        this.playerTwo = new Player();
    }

    @Override
    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setColor(WHITE);
        g.setFont(FONT);
        g.drawString(scoreOf(playerOne), centerX() - 120, Y);
        g.drawString(scoreOf(playerTwo), centerX() + 55, Y);
    }

    private String scoreOf(Player player) {
        return String.valueOf(player.getScore());
    }

    public void addPointToPlayerOne() {
        playerOne.updateScore(1);
    }

    public void addPointToPlayerTwo() {
        playerTwo.updateScore(1);
    }

    int centerX() {
        return getWidth() / 2;
    }

}