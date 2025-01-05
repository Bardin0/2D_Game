package main;

import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable{

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels


    // FPS
    int FPS = 60;


    // WORLD PARAMETERS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;


    //System
    TileManager tileM = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    Sound music = new Sound();
    Sound SE = new Sound();
    public CollisionChecker checker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui= new UI(this);
    public EventHandler eventH = new EventHandler(this);
    Thread gameThread;


    //Entity and Object
    public Player player = new Player(this, keyHandler);
    public Entity[] obj = new Entity[10];
    public Entity[] npc = new Entity[10];
    ArrayList<Entity> entityList = new ArrayList<>();
    public Entity[] monster = new Entity[20];

    //Game State
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;


    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    /**
     * Setup of the game, places all objects and Npc's on the map
     */
    public void setupGame(){

        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        gameState = titleState;

    }

    /**
     * Makes a new game thread
     */
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Function to run the game
     */
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

            if (delta >= 1){
                update();
                repaint();  // calls the paint component method, unsure why this is used
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000){
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    /**
     * Updates player positions based off of key presses
     */
    public void update(){

        if (gameState == playState){
            // Player
            player.update();

            for (Entity entity: npc){
                if (entity != null){
                    entity.update();
                }
            }

            for (Entity entity: monster){
                if (entity != null){
                    entity.update();
                }
            }

        }

    }

    /**
     * This is a built-in java method to draw things to a JPanel
     * This method is responsible for drawing everything we see to the screen
     * @param g the Graphics object to protect
     */
    public void paintComponent(Graphics g){

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        //Debug
        long drawStart = 0;
        if (keyHandler.checkDrawTime){
            //Debug
            drawStart = System.nanoTime();
        }

        // Title Screen
        if (gameState == titleState){
            ui.draw(g2);
        }else{

            //Tile
            tileM.draw(g2);

            // Add all entities to list
            entityList.add(player);

            // Adds Npc's
            for (Entity entity : npc){
                if (entity != null){
                    entityList.add(entity);
                }
            }

            // Adds objects
            for (Entity entity : obj){
                if (entity != null){
                    entityList.add(entity);
                }
            }

            for (Entity entity: monster){
                if (entity != null){
                    entityList.add(entity);
                }
            }

            // Sort entity list
            entityList.sort(new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.worldY, e2.worldY);
                }
            });

            // Draw entities
            for (Entity entity : entityList){
                entity.draw(g2);
            }

            // Clear list
            entityList.clear();

            // UI
            ui.draw(g2);

            // Debug
            if (keyHandler.checkDrawTime) {
                long drawEnd = System.nanoTime();
                long passed = drawEnd - drawStart;
                g2.setColor(Color.white);
                g2.drawString("Draw Time: " + passed, 10, 400);
                System.out.println("Draw Time: " + passed);
            }
        }

        g2.dispose();

    }

    /**
     * Begins playing music
     * @param i The index of the music to play
     */
    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }

    /**
     * Stops music from playing
     */
    public void stopMusic(){
        music.stop();
    }

    /**
     * Plays sound effect
     * @param i The index of the sound effect to play
     */
    public void playSE(int i){
        SE.setFile(i);
        SE.play();
    }


}
