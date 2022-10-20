package gui;

import common.Random;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static java.awt.Color.*;

public class Pong extends JPanel {

    private static final int UPPER_BORDER = 0;
    private static final int LEFT_BORDER = 0;
    private static final Color BACKGROUND_COLOR = new Color(14, 127, 18);
    private static final int PLAYER_WIDTH = 20;
    private static final int PLAYER_HEIGHT = 150;
    private static final int DELTA = 15;
    private final Random random;
    private final Score score;
    private InputListener inputListener;
    private int ballX;
    private int ballY;
    private int playersDelay;
    private Shape ball;
    private Function<Integer, Integer> xAxisStrategy;
    private Function<Integer, Integer> yAxisStrategy;
    private boolean firstPaint;
    private double ballRadius;
    private Rectangle2D.Double playerOne;
    private Rectangle2D.Double playerTwo;

    public Pong(int playersDelay) {
        super(new GridLayout(0, 1));
        this.random = new Random();
        this.score = new Score();
        this.ballRadius = 30;
        this.firstPaint = true;
        this.ballX = centerX();
        this.ballY = centerY();
        this.xAxisStrategy = randomAxisStrategy();
        this.yAxisStrategy = randomAxisStrategy();
        this.playersDelay = playersDelay;
        setBackground(BACKGROUND_COLOR);
        add(score);
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            doPaint( (Graphics2D) g );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void recalculateBallPosition() {
        ballX = xAxisStrategy.apply(ballX);
        ballY = yAxisStrategy.apply(ballY);
    }

    private void doPaint(Graphics2D g) throws IOException {
        if (firstPaint()) {
            initializeInputListener();
            initializePlayers();
            initializeBall();
        }
        drawBall(g);
        drawScore();
        drawPlayers(g);
    }

    private void initializeInputListener() {
        setFirstPaint(false);
        this.inputListener = new InputListener(getHeight(), playersDelay);
        addKeyListener(inputListener);
        new Thread(inputListener).start();
    }

    private void initializePlayers() {
        this.playerOne = createPlayerOne();
        this.playerTwo = createPlayerTwo();
    }

    private void initializeBall() {
        this.ball = createBall();
    }

    private Rectangle2D.Double createPlayerTwo() {
        return new Rectangle2D.Double(getWidth() - PLAYER_WIDTH, playerTwoY(), PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    private Rectangle2D.Double createPlayerOne() {
        return new Rectangle2D.Double(0, playerOneY(), PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    private Shape createBall() {
        return new Ellipse2D.Double
                (ballX - ballRadius, ballY - ballRadius, 2.0 * ballRadius, 2.0 * ballRadius);
    }

    private void setFirstPaint(boolean b) {
        this.firstPaint = b;
    }

    private boolean firstPaint() {
        return firstPaint;
    }

    private void drawPlayers(Graphics2D g) {
        drawPlayerOne(g);
        drawPlayerTwo(g);
    }

    private void drawPlayerTwo(Graphics2D g) {
        playerTwo.x = getWidth() - PLAYER_WIDTH;
        playerTwo.y = playerTwoY();
        g.setColor(BLUE);
        g.fill(playerTwo);
    }

    private void drawPlayerOne(Graphics2D g) {
        playerOne.y = playerOneY();
        g.setColor(RED);
        g.fill(playerOne);
    }

    private int playerOneY() {
        return inputListener.getPlayerOneY();
    }

    private int playerTwoY() {
        return inputListener.getPlayerTwoY();
    }

    private void drawScore() {
        if (ballIsOutOfBounds()) {
            updateScore();
            score.repaint();
            reset();
        }
    }

    private void updateScore() {
        if (playerOneScored()) {
            score.addPointToPlayerOne();
        }
        else if (playerTwoScored()) {
            score.addPointToPlayerTwo();
        }
    }

    private void drawBall(Graphics2D g) {
        reassignCoordinateStrategies();
        ball = new Ellipse2D.Double
                (ballX - ballRadius, ballY - ballRadius, 2.0 * ballRadius, 2.0 * ballRadius);
        g.setColor(YELLOW);
        g.fill(ball);
    }

    private void reset() {
        ballX = centerX();
        ballY = centerY();
        setXAxisStrategy(randomAxisStrategy());
        setYAxisStrategy(randomAxisStrategy());
    }

    private void reassignCoordinateStrategies() {
        if (ballIsOnUpperBorder())
            setYAxisStrategy(positiveAxisStrategy());

        if (ballIsOnLowerBorder())
            setYAxisStrategy(negativeAxisStrategy());

        if (ballHitsPlayerOne())
            setXAxisStrategy(positiveAxisStrategy());

        if (ballHitsPlayerTwo())
            setXAxisStrategy(negativeAxisStrategy());
    }

    private boolean ballHitsPlayerOne() {
        return ball.intersects(playerOne);
    }

    private boolean ballHitsPlayerTwo() {
        return ball.intersects(playerTwo);
    }

    private boolean ballIsOnLowerBorder() {
        return ballY >= lowerBorder() - ballRadius;
    }

    private boolean ballIsOnUpperBorder() {
        return ballY <= UPPER_BORDER + ballRadius;
    }

    private int lowerBorder() {
        return getHeight();
    }

    private Function<Integer, Integer> positiveAxisStrategy() {
        return coordinate -> coordinate + DELTA;
    }

    private Function<Integer, Integer> negativeAxisStrategy() {
        return coordinate -> coordinate - DELTA;
    }

    private void setYAxisStrategy(Function<Integer, Integer> axisStrategy) {
        this.yAxisStrategy = axisStrategy;
    }

    private void setXAxisStrategy(Function<Integer, Integer> axisStrategy) {
        this.xAxisStrategy = axisStrategy;
    }

    private Function<Integer, Integer> randomAxisStrategy() {
        List<Function<Integer, Integer>> axisStrategies = List.of(
            coordinate -> coordinate + 5,
            coordinate -> coordinate + 10,
            coordinate -> coordinate + 15,
            coordinate -> coordinate + 20,
            coordinate -> coordinate - 5,
            coordinate -> coordinate - 10,
            coordinate -> coordinate - 15,
            coordinate -> coordinate - 20
        );
        return random.from(axisStrategies);
    }

    private boolean playerOneScored() {
        return ballX > getWidth();
    }

    private boolean playerTwoScored() {
        return ballX < LEFT_BORDER;
    }

    private boolean ballIsOutOfBounds() {
        return     ball.intersects(-200, 0, 1, getHeight())
                || ball.intersects(getWidth() + 200, 0, 1, getHeight());
    }

    private int centerY() {
        return getHeight() / 2;
    }

    private int centerX() {
        return (getWidth() / 2) - 23;
    }

}