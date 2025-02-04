package main;

import entity.Entity;
import entity.Player;
import tile.InteractiveTile;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable{

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 4;
    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // Full screen
    int fullScreenHeight = screenHeight;
    int fullScreenWidth = screenWidth;
    BufferedImage tempImage;
    Graphics2D g2;

    // FPS
    int FPS = 60;

    // WORLD PARAMETERS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;


    //System
    Thread gameThread;
    TileManager tileM = new TileManager(this);
    Sound music = new Sound();
    Sound SE = new Sound();
    public KeyHandler keyHandler = new KeyHandler(this);
    public CollisionChecker checker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui= new UI(this);
    public EventHandler eventH = new EventHandler(this);

    //Entity and Object
    public Player player = new Player(this, keyHandler);
    public Entity[] obj = new Entity[20];
    public Entity[] npc = new Entity[10];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public Entity[] monster = new Entity[20];
    public InteractiveTile[] interactiveTiles = new InteractiveTile[50];
    public ArrayList<Entity> particles = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();

    //Game State
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int menuState = 4;


    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    /**
     * Setup of the game, places all entities on the map and sets gameState
     */
    public void setupGame(){

        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTiles();
        gameState = titleState;

        tempImage = new BufferedImage(fullScreenWidth, fullScreenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempImage.getGraphics();  // set g2 to draw to tempImage

        setFullScreen();
    }

    public void setFullScreen(){

        // Get local screen device
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        //get full screen width and height
        fullScreenHeight = Main.window.getHeight();
        fullScreenWidth = Main.window.getWidth();
    }

    /**
     * Makes a new game thread
     */
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Manages running the game using the delta method.
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
                drawTempScreen();
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
     * Updates all entities on the map
     */
    public void update(){

        if (gameState == playState){

            player.update();

            // Updates all npcs
            for (Entity entity: npc){
                if (entity != null){
                    entity.update();
                }
            }

            //Updates all monsters
            for (int i = 0; i < monster.length; i++){
                if (monster[i] != null){
                    if (monster[i].alive && !monster[i].dying){
                        monster[i].update();
                    }
                    if (!monster[i].alive){
                        monster[i].checkDrop();
                        monster[i] = null;
                    }
                }
            }

            // Updates any projectiles on the map
            for (int i = 0; i < projectileList.size(); i++){
                if (projectileList.get(i) != null){
                    if (projectileList.get(i).alive){
                        projectileList.get(i).update();
                    }
                    if (!projectileList.get(i).alive){
                        projectileList.remove(i);
                    }
                }
            }

            // Update particles
            for (int i = 0; i < particles.size(); i++){
                if (particles.get(i) != null){
                    if (particles.get(i).alive){
                        particles.get(i).update();
                    }
                    if (!particles.get(i).alive){
                        particles.remove(i);
                    }
                }
            }

            for (Entity entity: interactiveTiles){

                if(entity != null){
                    entity.update();
                }

            }

        }
    }

    public void drawTempScreen(){

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

            // Interactive Tile
            for (Entity entity: interactiveTiles){
                if (entity != null){
                    entity.draw(g2);
                }
            }

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

            // Adds Monsters
            for (Entity entity: monster){
                if (entity != null){
                    entityList.add(entity);
                }
            }

            // Adds Monsters
            for (Entity entity: projectileList){
                if (entity != null){
                    entityList.add(entity);
                }
            }

            for (Entity particle : particles) {
                if (particle != null) {
                    entityList.add(particle);
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

    }

    /**
     * This is a built-in java method to draw things to a JPanel
     * This method is responsible for drawing everything we see to the screen
     * @param g the Graphics object to protect
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Clears the screen
        if (tempImage != null) {
            g.drawImage(tempImage, 0, 0, fullScreenWidth, fullScreenHeight, null);
        }
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
