package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;

    // Set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    int FPS = 60;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run(){

        double drawInterval = (double)1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            lastTime = currentTime;

            if (delta >= 1){
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000){
                drawCount = 0;
                timer = 0;
            }
        }
    }

    /**
     * Updates player positions based off of key presses
     */
    public void update(){

        if (keyHandler.upPressed && keyHandler.rightPressed && keyHandler.leftPressed && keyHandler.downPressed){
        }
        else if (keyHandler.upPressed && keyHandler.rightPressed && keyHandler.leftPressed){
            playerY -= playerSpeed;
        }
        else if (keyHandler.downPressed && keyHandler.rightPressed && keyHandler.leftPressed){
            playerY += playerSpeed;
        }
        else if (keyHandler.upPressed && keyHandler.leftPressed){
            playerY -= playerSpeed;
            playerX -= playerSpeed;
        }
        else if (keyHandler.upPressed && keyHandler.rightPressed) {
            playerY -= playerSpeed;
            playerX += playerSpeed;
        }
        else if (keyHandler.upPressed && keyHandler.downPressed){
        }
        else if (keyHandler.downPressed && keyHandler.leftPressed){
            playerY += playerSpeed;
            playerX -= playerSpeed;
        }
        else if (keyHandler.downPressed && keyHandler.rightPressed){
            playerY += playerSpeed;
            playerX += playerSpeed;
        }
        else if (keyHandler.leftPressed && keyHandler.rightPressed){
        }
        else if (keyHandler.upPressed){
            playerY -= playerSpeed;
        }
        else if (keyHandler.downPressed){
            playerY += playerSpeed;
        }
        else if (keyHandler.leftPressed){
            playerX -= playerSpeed;
        }
        else if (keyHandler.rightPressed) {
            playerX += playerSpeed;
        }
    }

    /**
     * This is a built-in java method to draw things to a JPanel
     * @param g the Graphics object to protect
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.white);

        g2.fillRect(playerX,playerY,tileSize,tileSize);
        g2.dispose();

    }
}