package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Entity {

    public int worldX, worldY;
    public int speed;

    public BufferedImage up1, up2, down1, down2, right1, right2, left1, left2;
    public String direction = "down";

    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int standCounter = 0;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public boolean collisionOn = false;

    public int solidAreaDefaultX, solidAreaDefaultY;

    public int actionLockCounter = 0;

    String[] dialogues = new String[20];
    int dialogueIndex = 0;

    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;
    public boolean invincible = false;
    public int invincibleCounter = 0;

    // Stats
    public int maxLife;
    public int life;
    public int type; // 0 = player, 1 = NPC, 2 = monster

    GamePanel gp;

    public Entity(GamePanel gp){
        this.gp = gp;
    }

    public BufferedImage setup(String imagePath){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image;

        try{
            image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath + ".png")));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return image;
    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize< gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize> gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize< gp.player.worldY + gp.player.screenY){

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

            g2.drawImage(image,screenX,screenY,gp.tileSize,gp.tileSize,null);
        }
    }

    public void setAction(){

    }

    public void update(){
        setAction();

        collisionOn = false;
        gp.checker.checkTile(this);
        gp.checker.checkObject(this,false);
        gp.checker.checkEntity(this,gp.npc);
        gp.checker.checkEntity(this,gp.monster);
       boolean contactPlayer =  gp.checker.checkPlayer(this);

       if (this.type == 2 && contactPlayer && !gp.player.invincible){
            gp.player.life-=1;
            gp.player.invincible = true;
       }

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

    public void speak(){

        if (dialogues[dialogueIndex] == null){
            dialogueIndex = 0;
        }
        gp.ui.currentDialouge = dialogues[dialogueIndex];
        dialogueIndex++;

        switch(gp.player.direction){
            case"up":
                direction = "down";
                break;
            case"down":
                direction = "up";
                break;
            case"left":
                direction = "right";
                break;
            case"right":
                direction = "left";
                break;
        }

    }

}
