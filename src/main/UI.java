package main;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B, customFont;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public String currentDialogue = "";
    public int commandNumber = 0;

    public int slotCol = 0;
    public int slotRow = 0;

    BufferedImage heartFull, heartHalf, heartBlank, crystalFull, crystalBlank;

    int subState = 0;


    /**
     * Instantiates the UI font, and images
     * @param gp The primary GamePanel element
     */
    public UI(GamePanel gp){
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        InputStream is = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("fonts/MedodicaRegular.otf"));
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT,is);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        // Create HUD Objects
        Entity heart = new OBJ_Heart(gp);
        heartFull = heart.image;
        heartHalf = heart.image2;
        heartBlank = heart.image3;

        Entity crystal = new OBJ_ManaCrystal(gp);
        crystalFull = crystal.image;
        crystalBlank = crystal.image2;
    }

    /**
     * Draws the UI throughout the various game states
     * @param g2 The primary Graphics2D object
     */
    public void draw(Graphics2D g2){
        this.g2 = g2;

        g2.setFont(customFont);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);

        if (gp.gameState == gp.titleState){
            drawTitleScreen();
        }
        else if (gp.gameState == gp.playState){
            drawPlayerLife();
            drawMessage();
        }
        else if (gp.gameState == gp.pauseState){
            drawPlayerLife();
            drawPauseScreen();
        }
        else if (gp.gameState == gp.dialogueState){
            drawPlayerLife();
            drawDialogueScreen();
        }
        else if (gp.gameState == gp.menuState){
            drawMenuScreen();
            drawInventory();
        }
        else if (gp.gameState == gp.optionsState){
            drawOptionsScreen();
        }


    }

    /**
     * Draws the title screen
     */
    public void drawTitleScreen(){

        g2.setColor(new Color(199, 137, 137));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        // Name
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,96));
        String text = "Unc's Journey";
        int x = getXForCenterText(text);
        int y = gp.tileSize*3;

        // Shadow
        g2.setColor(Color.black);
        g2.drawString(text,x+5,y+5);

        // Main color
        g2.setColor(Color.white);
        g2.drawString(text,x,y);

        // Image

        x = gp.screenWidth/2 - gp.tileSize;
        y += gp.tileSize*2;

        g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);

        // Menu
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,40F));
        text = "New Game";
        x = getXForCenterText(text);
        y += gp.tileSize*3;
        g2.drawString(text,x,y);

        if (commandNumber == 0){
            g2.drawString(">", x - gp.tileSize,y-4);
        }

        text = "Load Game";
        x = getXForCenterText(text);
        y += gp.tileSize;
        g2.drawString(text,x,y);

        if (commandNumber == 1){
            g2.drawString(">", x - gp.tileSize,y-4);
        }

        text = "Quit";
        x = getXForCenterText(text);
        y += gp.tileSize;
        g2.drawString(text,x,y);

        if (commandNumber == 2){
            g2.drawString(">", x - gp.tileSize,y-4);
        }


    }

    /**
     * Draws the pause screen
     */
    public void drawPauseScreen(){

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80));
        String text = "PAUSED";

        int x = getXForCenterText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text,x,y);
    }

    /**
     * Draws the dialogue box and text
     */
    public void drawDialogueScreen(){

        // Window
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*4;

        drawSubWindow(x,y,width,height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F));
        x += gp.tileSize;
        y += gp.tileSize;

        for(String line : currentDialogue.split("\n")){
            g2.drawString(line,x,y);
            y += 40;
        }


    }

    /**
     * Draws the primary dialogue box
     */
    public void drawSubWindow(int x, int y, int width, int height){

        Color c = new Color(0,0,0,210);
        g2.setColor(c);
        g2.fillRoundRect(x,y,width,height,35,35);

        c = new Color (255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5,y+5,width-10,height-10,25,25);
    }

    /**
     * Gets the X coordinates for text that will be centered
     * @param text The text to be displayed
     * @return The X coordinate for the text
     */
    public int getXForCenterText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }

    /**
     * Calculates the X values of text that will be aligned to the right
     * @param text The text to be displayed on screen
     * @param tailX end of the dialogue box
     * @return The X coordinate where the text will be displayed
     */
    public int getXForAlignToRightText(String text, int tailX){
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return tailX - length;
    }

    /**
     * Draws the players health bar, in the top left of the screen
     */
    public void drawPlayerLife(){

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        // Draw max life
        while (i < gp.player.maxLife/2){
            g2.drawImage(heartBlank,x,y,null);
            i++;
            x += gp.tileSize;
        }

        i = 0;
        x = gp.tileSize/2;

        // Draw current life
        while (i < gp.player.life){
            g2.drawImage(heartHalf,x,y,null);
            i++;
            if (i < gp.player.life){
                g2.drawImage(heartFull,x,y,null);
                i++;
            }
            x += gp.tileSize;
        }

        // Draw max mana
        x = (gp.tileSize/2) - 5;
        y = (int) (gp.tileSize * 1.5);
        i = 0;
        while (i < gp.player.maxMana){
            g2.drawImage(crystalBlank, x, y, null);
            i++;
            x += 35;
        }

        x = (gp.tileSize/2) - 5;
        i = 0;

        while (i < gp.player.mana){
            g2.drawImage(crystalFull, x, y, null);
            i++;
            x += 35;
        }
    }

    /**
     * Draws the menu screen
     */
    public void drawMenuScreen(){

        // Create a Frame
        final int frameX = gp.tileSize * 2;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Text
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 40;

        // Names
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Mana", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Coin", textX, textY);
        textY += lineHeight + 25;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 25;
        g2.drawString("Shield", textX, textY);

        // Values
        int tailX = (frameX + frameWidth) - 30;

        // Reset textY
        textY = frameY + gp.tileSize;
        String value;

        // Level
        value = String.valueOf(gp.player.level);
        System.out.println(gp.player.level);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Life
        value = gp.player.life + "/" + gp.player.maxLife;
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Mana
        value = gp.player.mana + "/" + gp.player.maxMana;
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Strength
        value = String.valueOf(gp.player.strength);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Dexterity
        value = String.valueOf(gp.player.dexterity);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Attack
        value = String.valueOf(gp.player.attack);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Defense
        value = String.valueOf(gp.player.defense);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Exp
        value = String.valueOf(gp.player.exp);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Next Level
        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Coin
        value = String.valueOf(gp.player.coin);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Weapon
        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY-14, null);
        textY += lineHeight;

        //Shield
        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY+7, null);

        g2.setFont(customFont);

    }

    /**
     * Adds messages to be displayed
     * @param text The message to be added
     */
    public void addMessage(String text){

        message.add(text);
        messageCounter.add(0);

    }

    /**
     * Draws a message within the dialogue window
     */
    public void drawMessage(){

        int messageX = gp.tileSize;
        int messageY = gp.tileSize*4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for (int i = 0; i < message.size(); i++){
            if (message.get(i) != null) {

                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX+2, messageY+2);

                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i,counter);
                messageY += 50;

                if (messageCounter.get(i) > 180){
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }

    }

    /**
     * Draws the inventory screen
     */
    public void drawInventory(){

        int frameX = gp.tileSize*12;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*6;
        int frameHeight = gp.tileSize * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 3;

        // Draw player items
        for (int i = 0; i < gp.player.inventory.size(); i++){

            if (gp.player.inventory.get(i) == gp.player.currentWeapon || gp.player.inventory.get(i) == gp.player.currentShield){
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRoundRect(slotX,slotY,gp.tileSize,gp.tileSize,10,10);
            }

            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);

            slotX+= slotSize;
            if (i == 4 | i ==9 | i == 14 | i == 19){
                slotX = slotXStart;
                slotY += slotSize;
            }
        }

        // Cursor
        int cursorX = slotXStart + (slotSize * slotCol);
        int cursorY = slotYStart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        // Draw cursor
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // Description window
        frameY += frameHeight;
        frameHeight = gp.tileSize * 3;


        // Draw description text
        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(36F));

        int itemIndex = getItemInventoryIndex();
        if (itemIndex < gp.player.inventory.size()){

            drawSubWindow(frameX,frameY,frameWidth,frameHeight);    // Creates sub window only when an item is selected in inventory
            for (String line: gp.player.inventory.get(itemIndex).description.split("\n")){

                g2.drawString(line, textX, textY);
                textY += 36;
            }

        }

    }

    /**
     * Gets the index of an item in the inventory relative to the inventory arraylist
     */
    public int getItemInventoryIndex(){

        return slotCol + (slotRow * 5);

    }

    public void drawOptionsScreen(){
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        // Sub window
        int frameX = gp.tileSize*6;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*8;
        int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState){
            case 0:
                optionsTop(frameX, frameY);
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }

    public void optionsTop(int frameX, int frameY) {

        int textX;
        int textY;
        //Title
        String text = "Options";
        textX = getXForCenterText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        // Full Screen On/Off
        textX = frameX + gp.tileSize;
        textY += gp.tileSize*2;
        g2.drawString("Full Screen", textX, textY);
        if (commandNumber == 0){
            g2.drawString(">", textX - 25, textY);
        }

        //Music
        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if (commandNumber == 1){
            g2.drawString(">", textX - 25, textY);
        }

        //SE
        textY += gp.tileSize;
        g2.drawString("SE", textX, textY);
        if (commandNumber == 2){
            g2.drawString(">", textX - 25, textY);
        }

        // Controls
        textY += gp.tileSize;
        g2.drawString("Controls", textX, textY);
        if (commandNumber == 3){
            g2.drawString(">", textX - 25, textY);
        }

        // Quit
        textY += gp.tileSize;
        g2.drawString("Quit", textX, textY);
        if (commandNumber == 4){
            g2.drawString(">", textX - 25, textY);
        }

        textY += gp.tileSize * 2;
        g2.drawString("Back", textX, textY);
        if (commandNumber == 5){
            g2.drawString(">", textX - 25, textY);
        }


    }

}
