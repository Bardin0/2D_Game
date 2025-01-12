package main;

import entity.Entity;
import object.OBJ_Heart;

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
    public String currentDialouge = "";
    public int commandNumber = 0;

    public int slotCol = 0;
    public int slotRow = 0;

    BufferedImage heartFull, heartHalf, heartBlank;


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
    }

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
    }

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

    public void drawPauseScreen(){

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80));
        String text = "PAUSED";

        int x = getXForCenterText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text,x,y);
    }

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

        for(String line : currentDialouge.split("\n")){
            g2.drawString(line,x,y);
            y += 40;
        }


    }

    public void drawSubWindow(int x, int y, int width, int height){

        Color c = new Color(0,0,0,210);
        g2.setColor(c);
        g2.fillRoundRect(x,y,width,height,35,35);

        c = new Color (255,255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5,y+5,width-10,height-10,25,25);
    }

    public int getXForCenterText(String text) {

        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }

    public int getXForAlignToRightText(String text, int tailX){
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        return tailX - length;
    }

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
    }

    public void drawMenuScreen(){

        // Create a Frame
        final int frameX = gp.tileSize;
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

    public void addMessage(String text){

        message.add(text);
        messageCounter.add(0);

    }

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

    public void drawInventory(){

        int frameX = gp.tileSize*9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*6;
        int frameHeight = gp.tileSize * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 3;

        for (int i = 0; i < gp.player.inventory.size(); i++){
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
        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        // Draw description text
        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(36F));

        int itemIndex = getItemInventoryIndex();
        if (itemIndex < gp.player.inventory.size()){

            for (String line: gp.player.inventory.get(itemIndex).description.split("\n")){

                g2.drawString(line, textX, textY);
                textY += 36;
            }

        }

    }

    public int getItemInventoryIndex(){

        return slotCol + (slotRow * 5);

    }

}
