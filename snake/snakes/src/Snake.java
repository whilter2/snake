import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class Snake implements ActionListener {
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    Tile food;
    Random randomPos;

    int velocityX;
    int velocityY;

    Snake() {
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);

        velocityX = 1;
        velocityY = 0;

        // game timer
        // gameLoop = new Timer(100, this);
        // gameLoop.start();
    }



	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
	}
}
