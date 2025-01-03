package entity;

import main.KeyHandler;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity{

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH){

        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle(8,16,28,28);
        solidAreaDefaultX = 8;
        solidAreaDefaultY = 16;

        setDefaultValues();
        getPlayerImage();

    }

    public void setDefaultValues(){

        worldX = gp.tileSize*23;
        worldY = gp.tileSize*21;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage(){

        up1 = setup("player/boy_up_1");
        up2 = setup("player/boy_up_2");
        down1 = setup("player/boy_down_1");
        down2 = setup("player/boy_down_2");
        left1 = setup("player/boy_left_1");
        left2 = setup("player/boy_left_2");
        right1 = setup("player/boy_right_1");
        right2 = setup("player/boy_right_2");

    }

    public void update(){

        if (keyH.downPressed || keyH.leftPressed || keyH.upPressed || keyH.rightPressed) {
            if (keyH.upPressed){
                direction = "up";
            }
            else if (keyH.downPressed){
                direction = "down";
            }
            else if (keyH.leftPressed){
                direction = "left";
            }
            else {
                direction = "right";
            }

            // CHECK TILE COLLISION
            collisionOn = false;
            gp.checker.checkTile(this);

            //Check object collision
            int objIndex = gp.checker.checkObject(this,true);
            pickupObject(objIndex);

            // Check npc collision
            int npcIndex = gp.checker.checkEntity(this,gp.npc);
            interactNPC(npcIndex);

            // If collision is false, player can move
            if (!collisionOn){
                switch (direction){
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }

            spriteCounter ++;
            if (spriteCounter > 12){
                if (spriteNum == 1){
                    spriteNum = 2;
                }else if (spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
    }

    public void pickupObject(int i){

        if (i != 999){

        }
    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;

        switch(direction){
            case "up":
                if (spriteNum == 1){
                    image = up1;
                }else{
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1){
                    image = down1;
                }else{
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1){
                    image = left1;
                }else{
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1){
                    image = right1;
                }else{
                    image = right2;
                }
                break;
        }

        g2.drawImage(image,screenX,screenY,null);
    }

    public void interactNPC(int i){

        if (i != 999){
            if (gp.keyHandler.enterPressed){
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
        gp.keyHandler.enterPressed = false;

    }
}
