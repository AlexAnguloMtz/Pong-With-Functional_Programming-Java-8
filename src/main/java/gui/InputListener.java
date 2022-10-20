package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_UP;
import static java.lang.Thread.sleep;

public class InputListener implements KeyListener, Runnable {

    private static final int PLAYER_DISTANCE_PER_REFRESH = 10;

    private final int playersDelay;
    private final int windowHeight;
    private boolean up;
    private boolean down;
    private boolean w;
    private boolean s;
    private int playerOneY;
    private int playerTwoY;

    public InputListener(int height, int playersDelay) {
        this.windowHeight = height;
        this.playersDelay = playersDelay;
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == VK_UP)
            up = true;

        else if (e.getKeyCode() == VK_DOWN)
            down = true;

        else if (e.getKeyChar() == 'w')
            w = true;

        else if (e.getKeyChar() == 's')
            s = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == VK_UP)
            up = false;

        else if (e.getKeyCode() == VK_DOWN)
            down = false;

        else if (e.getKeyChar() == 'w')
            w = false;

        else if (e.getKeyChar() == 's')
            s = false;
    }

    public int getPlayerOneY() {
        return initialPlayerY() + playerOneY;
    }

    public int getPlayerTwoY() {
        return initialPlayerY() + playerTwoY;
    }

    private int initialPlayerY() {
        return windowHeight / 2;
    }

    @Override
    public void run() {
        while (true) {
            if (up)
                playerTwoY -= PLAYER_DISTANCE_PER_REFRESH;
            if (down)
                playerTwoY += PLAYER_DISTANCE_PER_REFRESH;
            if (w)
                playerOneY -= PLAYER_DISTANCE_PER_REFRESH;
            if (s)
                playerOneY += PLAYER_DISTANCE_PER_REFRESH;

            try {
                sleep(playersDelay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}