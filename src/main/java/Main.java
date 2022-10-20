import gui.Board;
import gui.Pong;

import javax.swing.*;

import java.awt.*;

import static javax.swing.SwingUtilities.invokeLater;


public class Main extends JFrame {

    // Change this variable to set the speed of the ball
    private static final int BALL_DELAY = 7;

    // Change this variable to set the speed of the players
    private static final int PLAYER_DELAY = 7;

    public static void main(String[] args) {
        invokeLater(Main::pong);
    }

    private static void pong() {
        var board = new Board("Pong");
        board.setMinimumSize(new Dimension(1100, 700));
        board.setResizable(true);

        var pong = new Pong(PLAYER_DELAY);

        pong.setMinimumSize(new Dimension(1100, 700));
        board.getContentPane().add(pong);

        board.pack();
        board.setVisible(true);
        timer(pong).start();
    }

    private static Timer timer(Pong pong) {
        return new Timer(BALL_DELAY, event -> {
            pong.recalculateBallPosition();
            pong.repaint();
        });
    }

}
