package main;

import object.OBJ_Heart;
import object.OBJ_Key;
import object.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Objects;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B, GomePixel;
    public boolean messageOn = false;
    public boolean gameFinished = false;
    public String message = "";
    int messageCounter = 0;
    public String currentDialouge = "";
    public int commandNumber = 0;

    BufferedImage heartFull, heartHalf, heartBlank;


    public UI(GamePanel gp){
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        InputStream is = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("fonts/GomePixel-ARJd7.otf"));
        try {
            GomePixel = Font.createFont(Font.TRUETYPE_FONT,is);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        // Create HUD Objects
        SuperObject heart = new OBJ_Heart(gp);
        heartFull = heart.image;
        heartHalf = heart.image2;
        heartBlank = heart.image3;
    }

    public void showMessage(String text){
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2){
        this.g2 = g2;

        g2.setFont(GomePixel);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);

        if (gp.gameState == gp.titleState){
            drawTitleScreen();
        }
        else if (gp.gameState == gp.playState){
            drawPlayerLife();
        }
        else if (gp.gameState == gp.pauseState){
            drawPlayerLife();
            drawPauseScreen();
        }
        else if (gp.gameState == gp.dialogueState){
            drawPlayerLife();
            drawDialogueScreen();
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
}