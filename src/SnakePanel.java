import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import java.awt.*;


public class SnakePanel extends JPanel implements ActionListener {
    private final int WIDTH = 400;
    private final int HEIGHT = 400;
    private final int SIZE = 10;
    private final int ALL_DOTS = 2500;
     
    
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];
    private int bodyParts = 10 ;
    private int apple_x;
    private int apple_y;
    private char direction = 'R';
    private boolean inGame = false;
    private Timer timer;

    private Image apple;
    
    private final int initialDelay = 80;
    private final int scoreThreshold = 15;
    private final int minimumDelay = 20; // Set a reasonable minimum to avoid excessive speed.

    // reset button
    

    private int score = 0;

    Random rand = new Random();

    private boolean showResetText = false;

    public SnakePanel() {
        rand = new Random();
        addKeyListener(new MyKeyAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        loadImages();
        startGame();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (showResetText) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();
    
                    // Calculate the position of the "Reset Game" text
                    FontMetrics metrics = getFontMetrics(new Font("Arial", Font.BOLD, 14));
                    int resetX = (WIDTH - metrics.stringWidth("Reset Game")) / 2;
                    int resetY = HEIGHT / 2 + 30;
    
                    // Check if the click is within the bounds of the "Reset Game" text
                    if (mouseX >= resetX && mouseX <= (resetX + metrics.stringWidth("Reset Game")) 
                        && mouseY >= (resetY - metrics.getHeight()) && mouseY <= resetY) {
                        resetGame();
                    }
                }
            }
        });
            
    };

    private void resetGame() {
        inGame = true;
        score = 0;
        bodyParts = 10;
        direction = 'R';
        
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 50 - i * 10; // Reset initial positions
            y[i] = 50;
        }
        
        startGame();
        showResetText = false; // Hide the reset text
        repaint(); 
    }


    
    public void loadImages() {
        // ImageIcon iid = new ImageIcon("src/images/dot.png");
        // ball = iid.getImage();
        ImageIcon iia = new ImageIcon("src/images/apple.png");

        apple = iia.getImage();
        apple = apple.getScaledInstance(SIZE, SIZE, Image.SCALE_DEFAULT);

        
    }
    public void startGame() {
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 50 - i * 10;
            y[i] = 50;
        }
        newApple();
        inGame = true;
        timer = new Timer(initialDelay, this);
        timer.start();
    }
    public void newApple() {
        apple_x = rand.nextInt((int) (WIDTH / SIZE)) * SIZE;
        apple_y = rand.nextInt((int) (HEIGHT / SIZE)) * SIZE;
    }
    
    public void draw(Graphics g) {
        g.drawImage(apple, apple_x, apple_y, this);
       // g.fillOval(apple_x, apple_y, SIZE, SIZE);

       for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.green);
                g.fillRect(x[i], y[i], SIZE, SIZE);
                
            }
            else {
                g.setColor(Color.green);
                g.fillRect(x[i], y[i], SIZE, SIZE);
            }
       }

       g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Score: " + score, 5, 15);

    }

    private void adjustSpeed() {
        int newDelay = initialDelay - (score / scoreThreshold);
        if (newDelay < minimumDelay) {
            newDelay = minimumDelay;
        }
        timer.setDelay(newDelay);
    }
    
    
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
    
        switch(direction) {
            case 'U':
                y[0] -= SIZE;
                if (y[0] < 0) {
                    y[0] = HEIGHT - SIZE;
                }
                break;
            case 'D':
                y[0] += SIZE;
                if (y[0] >= HEIGHT) {
                    y[0] = 0;
                }
                break;
            case 'L':
                x[0] -= SIZE;
                if (x[0] < 0) {
                    x[0] = WIDTH - SIZE;
                }
                break;
            case 'R':
                x[0] += SIZE;
                if (x[0] >= WIDTH) {
                    x[0] = 0;
                }
                break;
        }
    }
    

    public void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            bodyParts++;
            score += 5; 
            adjustSpeed();
            newApple();
        }
    }
    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
                break;
            }
        }
        if (!inGame) {
            timer.stop();
        }
    }
    
    

    public void gameOver(Graphics g) {
        //score
        g.setColor(Color.white);
        Font scoreFont = new Font("Arial", Font.BOLD, 24); // Larger font size for score
        g.setFont(scoreFont);
        String scoreText = "Score: " + score;
        FontMetrics scoreMetrics = getFontMetrics(scoreFont);
        int scoreX = (WIDTH - scoreMetrics.stringWidth(scoreText)) / 2; // Centering the score
        g.drawString(scoreText, scoreX, scoreMetrics.getAscent() + 20); // Adjust '20' for vertical positioning
    


        // Game Over text
        String gameOverMsg = "Game Over";
        Font font = new Font("Arial", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(font);
    
        g.setColor(Color.red);
        g.setFont(font);
        g.drawString(gameOverMsg, (WIDTH - metrics.stringWidth(gameOverMsg)) / 2, HEIGHT / 2);
    
        // Reset Game text
        String resetMsg = "Reset Game";
        g.setColor(Color.green);
        g.drawString(resetMsg, (WIDTH - metrics.stringWidth(resetMsg)) / 2, HEIGHT / 2 + 30);
    
        showResetText = true;
    }
    

    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
   
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            draw(g);
        } else {
            gameOver(g);
        }
    }
    

    public class MyKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
    }

}
    
}

