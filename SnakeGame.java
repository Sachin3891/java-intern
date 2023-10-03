import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 700;
    private static final int UNIT_SIZE = 10;
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 105;

    private final ArrayList<Point> snake;
    private Point food;
    private char direction = 'R';
    private boolean running = false;

    public SnakeGame() {
        snake = new ArrayList<>();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        snake.clear();
        snake.add(new Point(0, 0)); // Initial snake position
        generateFood();
        running = true;
        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    public void generateFood() {
        int x = new Random().nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        int y = new Random().nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        food = new Point(x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
            checkFood();
            repaint();
        }
    }

    public void move() {
        // Move the snake by adding a new head in the current direction
        // and removing the tail segment
        Point head = snake.get(0);
        Point newHead;
        switch (direction) {
            case 'U':
                newHead = new Point(head.x, head.y - UNIT_SIZE);
                break;
            case 'D':
                newHead = new Point(head.x, head.y + UNIT_SIZE);
                break;
            case 'L':
                newHead = new Point(head.x - UNIT_SIZE, head.y);
                break;
            case 'R':
                newHead = new Point(head.x + UNIT_SIZE, head.y);
                break;
            default:
                newHead = head;
        }

        snake.add(0, newHead);
        if (newHead.equals(food)) {
            generateFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    public void checkCollision() {
        Point head = snake.get(0);
        // Check collision with boundaries
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            running = false;
        }
        // Check collision with itself
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
                break;
            }
        }
    }

    public void checkFood() {
        if (snake.get(0).equals(food)) {
            snake.add(food);
            generateFood();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // Draw food
            g.setColor(Color.RED);
            g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (Point point : snake) {
                g.setColor(Color.GREEN);
                g.fillRect(point.x, point.y, UNIT_SIZE, UNIT_SIZE);
            }
        } else {
            // Game over
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Game Over", WIDTH / 2 - 75, HEIGHT / 2);
        }
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            char key = e.getKeyChar();
            if (key == 'w' || key == 'W' || key == KeyEvent.VK_UP) {
                if (direction != 'D') {
                    direction = 'U';
                }
            } else if (key == 's' || key == 'S' || key == KeyEvent.VK_DOWN) {
                if (direction != 'U') {
                    direction = 'D';
                }
            } else if (key == 'a' || key == 'A' || key == KeyEvent.VK_LEFT) {
                if (direction != 'R') {
                    direction = 'L';
                }
            } else if (key == 'd' || key == 'D' || key == KeyEvent.VK_RIGHT) {
                if (direction != 'L') {
                    direction = 'R';
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
