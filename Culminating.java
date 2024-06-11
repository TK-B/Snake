import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Culminating extends Applet implements KeyListener, Runnable {
    Image background, apple, snakeUp, snakeDown, snakeLeft, snakeRight, currentSnake, obstacle;
    int snakeX, snakeY, appleX, appleY, counter = 0, speed = 10, appletHeight, appletWidth;
    String direction = "right";
    Graphics bufferGraphics;
    Image offscreen;

    public void init() {
        setBackground(Color.blue);
        addKeyListener(this);
        background = this.getImage(this.getDocumentBase(), "Grass_Sample.png");
        apple = this.getImage(this.getDocumentBase(), "apple.png");
        snakeUp = this.getImage(this.getDocumentBase(), "snake-up.png");
        snakeDown = this.getImage(this.getDocumentBase(), "snake-down.png");
        snakeLeft = this.getImage(this.getDocumentBase(), "snake-left.png");
        snakeRight = this.getImage(this.getDocumentBase(), "snake-right.png");
        currentSnake = this.getImage(this.getDocumentBase(), "snake-right.png");
        obstacle = this.getImage(this.getDocumentBase(), "obstacle.png");
        snakeX = 50;
        snakeY = 50;
        appleX = (int) (Math.random() * 1400);
        appleY = (int) (Math.random() * 640);
        appletHeight = 640;
        appletWidth = 1408;

        offscreen = createImage(appletWidth, appletHeight);
        bufferGraphics = offscreen.getGraphics();
    }

    public void paint(Graphics g) {

        bufferGraphics.drawImage(background, 0, 0, this);
        bufferGraphics.drawImage(background, 0, 320, this);
        bufferGraphics.drawImage(background, 704, 320, this);
        bufferGraphics.drawImage(background, 704, 0, this);
        bufferGraphics.drawImage(obstacle, 600, 160, 150, 300, this);
        bufferGraphics.drawImage(apple, appleX, appleY, 50, 50, this);
        bufferGraphics.drawImage(currentSnake, snakeX, snakeY, 50, 50, this);
        bufferGraphics.drawString("Score " + counter, 20, 20);
        if (speed == 0) {
            bufferGraphics.drawString("Game over", 600, 280);
            bufferGraphics.setColor(Color.red);
            bufferGraphics.fillRect(550, 300, 150, 75); // draws a square at bX and bY
            bufferGraphics.setColor(Color.black);
            bufferGraphics.drawString("Restart ", 580, 330);
        }
        g.drawImage(offscreen, 0, 0, this);

    }

    // ********************************
    public void update(Graphics g) {
        paint(g);

    }

    public void start() {
        // create a thread to do multi-tasking
        Thread myAnimation = new Thread(this);
        myAnimation.start();
    }

    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        System.out.println("you pressed " + key);
        if (key == 38) {
            currentSnake = snakeUp;
            direction = "up";
        }
        if (key == 40) {
            currentSnake = snakeDown;
            direction = "down";
        }
        if (key == 37) {
            currentSnake = snakeLeft;
            direction = "left";
        }
        if (key == 39) {
            currentSnake = snakeRight;
            direction = "right";
        }
        repaint();
        e.consume();
    }

    public void run() {

        while (true) {

            if (collision(snakeX, snakeY, 50, 50, appleX, appleY, 50, 50)) {
                counter = counter + 1;
                speed = speed + 5;
                appleX = (int) (Math.random() * 1400);
                appleY = (int) (Math.random() * 640);
                repaint();

            }
            if (collision(appleX, appleY, 50, 50, 600, 160, 150, 300)) {
                appleX = (int) (Math.random() * 1400);
                appleY = (int) (Math.random() * 640);
                repaint();

            }
            if (collision(snakeX, snakeY, 50, 50, 600, 160, 150, 300)) {
                snakeX = 10;
                snakeY = 10;
                speed = 0;
            }

            if (snakeX > 704 * 2 - 50) {
                snakeX = 0;
            }
            if (snakeX < 0) {
                snakeX = 704 * 2 - 50;
            }
            if (snakeY < 0) {
                snakeY = 320 * 2 - 50;
            }
            if (snakeY > 320 * 2 - 50) {
                snakeY = 0;
            }
            if (direction == "up") {
                snakeY = snakeY - speed;
            } else if (direction == "right") {
                snakeX = snakeX + speed;
            } else if (direction == "down") {
                snakeY = snakeY + speed;
            } else if (direction == "left") {
                snakeX = snakeX - speed;
            }
            repaint(); // calls the paint method to be run.
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    private boolean collision(int x1, int y1, int width1, int height1, int x2, int y2, int width2, int height2) {
        for (int x = x1; x <= x1 + width1; x++)
            for (int y = y1; y <= y1 + height1; y++) {

                if ((x >= x2) && (x <= x2 + width2) && (y >= y2) && (y <= y2 + height2)) {

                    return true;
                }
            }
        return false;

    }
}
