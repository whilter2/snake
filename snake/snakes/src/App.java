
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class App extends JPanel implements ActionListener, KeyListener {
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    Player player1;
    Player player2;

    Timer gameLoop;

    boolean gameOver = false;

    Snake snake;

    Random random;

    App(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));

        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snake = new Snake();
        random = new Random();

        player1 = new Player();
        player2 = new Player();

        placeFood();

        // game timer
        gameLoop = new Timer(100, this); // how long it takes to start timer, milliseconds gone between frames
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Grid Lines
        for (int i = 0; i < boardWidth / tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }

        // Food
        g.setColor(Color.red);
        g.fill3DRect(snake.food.x * tileSize, snake.food.y * tileSize, tileSize, tileSize, true);

        // Snake Head
        g.setColor(Color.green);
        g.fill3DRect(snake.snakeHead.x * tileSize, snake.snakeHead.y * tileSize, tileSize, tileSize, true);

        // Snake Body
        for (int i = 0; i < snake.snakeBody.size(); i++) {
            Tile snakePart = snake.snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        // Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));

        if (!player1.isGameOver) {
            player1.setScore(snake.snakeBody.size());
            g.drawString("Player 1 Score: " + player1.score, tileSize - 16, tileSize);
        } else if (player1.isGameOver && !player2.isGameOver) {
            player2.setScore(snake.snakeBody.size());
            g.drawString("Player 2 Score: " + player2.score, tileSize - 16, tileSize);
        }

        if (player1.isGameOver && player2.isGameOver) {
            // g.setColor(Color.red);

            if (player1.score > player2.score) {
                g.drawString("Player 1 won", tileSize - 16, tileSize);
            } else {
                g.drawString("Player 2 won", tileSize - 16, tileSize);
            }
        }
        // } else {
        // g.drawString("Player 2 Score: " + String.valueOf(snake.snakeBody.size()),
        // tileSize - 16, tileSize);
        // }
    }

    public void placeFood() {
        snake.food.x = random.nextInt(boardWidth / tileSize);
        snake.food.y = random.nextInt(boardHeight / tileSize);
    }

    public void move() {
        // Eat food
        if (collision(snake.snakeHead, snake.food)) {
            snake.snakeBody.add(new Tile(snake.food.x, snake.food.y));
            placeFood();
        }

        // Move snake body
        for (int i = snake.snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snake.snakeBody.get(i);
            if (i == 0) { // Right before the head
                snakePart.x = snake.snakeHead.x;
                snakePart.y = snake.snakeHead.y;
            } else {
                Tile prevSnakePart = snake.snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        // Move snake head
        snake.snakeHead.x += snake.velocityX;
        snake.snakeHead.y += snake.velocityY;

        // Game over conditions
        for (int i = 0; i < snake.snakeBody.size(); i++) {
            Tile snakePart = snake.snakeBody.get(i);

            // Collide with snake head
            if (collision(snake.snakeHead, snakePart)) {
                if (!player1.isGameOver) {
                    player1.setGameOver(true);

                    // reset ang game
                    snake = new Snake();
                    random = new Random();
                    gameLoop.stop();
                } else if (player1.isGameOver && !player2.isGameOver) {
                    System.out.println("pagkaboto sa head");
                    player2.setGameOver(true);
                }
            }
        }

        if (snake.snakeHead.x * tileSize < 0 || snake.snakeHead.x * tileSize > boardWidth || // Passed left border or
                                                                                             // right border
                snake.snakeHead.y * tileSize < 0 || snake.snakeHead.y * tileSize > boardHeight) { // Passed top border
                                                                                                  // or bottom
            // border
            if (!player1.isGameOver) {
                player1.setGameOver(true);

                // reset ang game
                snake = new Snake();
                random = new Random();
                gameLoop.stop();
            }

            else if (player1.isGameOver && !player2.isGameOver) {
                System.out.println("pagkaboto sa border");
                player2.setGameOver(true);
            }
        }
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (player1.isGameOver && player2.isGameOver) {
            gameLoop.stop();

        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            gameLoop.start();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && snake.velocityY != 1) {
            snake.velocityX = 0;
            snake.velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && snake.velocityY != -1) {
            snake.velocityX = 0;
            snake.velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && snake.velocityX != 1) {
            snake.velocityX = -1;
            snake.velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && snake.velocityX != -1) {
            snake.velocityX = 1;
            snake.velocityY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) throws Exception {
        int boardWidth = 700;
        int boardHeight = boardWidth;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        App snakeGame = new App(boardWidth, boardHeight);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
    }
}
