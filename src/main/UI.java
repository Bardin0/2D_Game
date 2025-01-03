package main;

import object.OBJ_Key;

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

        if (gp.gameState == gp.playState){
            //TO-DO
        }
        else if (gp.gameState == gp.pauseState){
            drawPauseScreen();
        }
        else if (gp.gameState == gp.dialogueState){
            drawDialogueScreen();
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


}
