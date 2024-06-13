import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Culminating extends Applet implements KeyListener, Runnable, MouseListener, MouseMotionListener {
    Image background, apple, snakeUp, snakeDown, snakeLeft, snakeRight, currentSnake, obstacle, snakeBody;
    int snakeX, snakeY, appleX, appleY, counter = 0, speed = 35, appletHeight, appletWidth, mouseX, mouseY;
    String direction = "right";
    Graphics bufferGraphics;
    Image offscreen;
    boolean isDead = false;
    int[] bodyX = new int[100];
    int[] bodyY = new int[100];
    int bodyLength = 1;

    public void init() {
        setBackground(Color.blue);
        addKeyListener(this);

        addMouseListener(this);
        addMouseMotionListener(this);
        background = this.getImage(this.getDocumentBase(), "Grass_Sample.png");
        apple = this.getImage(this.getDocumentBase(), "apple.png");
        snakeUp = this.getImage(this.getDocumentBase(), "snake-up.png");
        snakeDown = this.getImage(this.getDocumentBase(), "snake-down.png");
        snakeLeft = this.getImage(this.getDocumentBase(), "snake-left.png");
        snakeRight = this.getImage(this.getDocumentBase(), "snake-right.png");
        currentSnake = this.getImage(this.getDocumentBase(), "snake-right.png");
        snakeBody = this.getImage(this.getDocumentBase(), "snake-body.png");
        obstacle = this.getImage(this.getDocumentBase(), "obstacle.png");
        snakeX = 50;
        snakeY = 50;
        appleX = (int) (Math.random() * 1350);
        appleY = (int) (Math.random() * 590);
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
        bufferGraphics.drawImage(currentSnake, snakeX, snakeY, 35, 35, this);
        bufferGraphics.drawString("Score " + counter, 20, 20);
        for (int i = 0; i < bodyLength; i++) {
            bufferGraphics.drawImage(snakeBody, bodyX[i], bodyY[i], 35, 35, this);
        }
        if (isDead == true) {
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

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    // MouseReleased method
    public void mouseReleased(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        // Check if the mouse click is inside the green square

        if ((mouseX >= 550) && (mouseX <= 550 + 150) &&
                (mouseY >= 300) && (mouseY <= 300 + 75)) {
            bodyLength = 1;
            snakeX = 0;
            snakeY = 0;
            isDead = false;
            counter = 0;
        }

        repaint(); // Repaint the applet to update the score
        e.consume();
    }

    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        System.out.println("you pressed " + key);
        if (isDead == false) {
            if (key == 38 && direction != "bottom") {
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
        }
        repaint();
        e.consume();
    }

    public void run() {

        while (true) {

            if (collision(snakeX, snakeY, 35, 35, appleX, appleY, 50, 50)) {
                counter = counter + 1;
                bodyX[bodyLength] = 0;
                bodyY[bodyLength] = 0;
                bodyLength = bodyLength + 1;
                appleX = (int) (Math.random() * 1350);
                appleY = (int) (Math.random() * 590);
                repaint();

            }
            if (collision(appleX, appleY, 50, 50, 600, 160, 150, 300)) {
                appleX = (int) (Math.random() * 1350);
                appleY = (int) (Math.random() * 590);
                repaint();

            }

            for (int i = 2; i < bodyLength - 1; i++) {
                if (collision(snakeX, snakeY, 35, 35, bodyX[i], bodyY[i], 35, 35) && isDead == false) {
                    isDead = true;
                    counter = 0;
                    repaint();

                }
            }

            for (int i = bodyLength - 1; i > 0; i--) {
                bodyX[i] = bodyX[i - 1];
                bodyY[i] = bodyY[i - 1];
            }
            bodyX[0] = snakeX;
            bodyY[0] = snakeY;

            if (collision(snakeX, snakeY, 35, 35, 600, 160, 150, 300)) {
                snakeX = 10;
                snakeY = 10;
                isDead = true;
            }

            if (snakeX > 704 * 2 - 35) {
                snakeX = 0;
            }
            if (snakeX < 0) {
                snakeX = 704 * 2 - 35;
            }
            if (snakeY < 0) {
                snakeY = 320 * 2 - 35;
            }
            if (snakeY > 320 * 2 - 35) {
                snakeY = 0;
            }
            if (isDead == false) {
                if (direction == "up") {
                    snakeY = snakeY - speed;
                } else if (direction == "right") {
                    snakeX = snakeX + speed;
                } else if (direction == "down") {
                    snakeY = snakeY + speed;
                } else if (direction == "left") {
                    snakeX = snakeX - speed;
                }
            }

            repaint(); // calls the paint method to be run.
            try {
                Thread.sleep(150);
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
