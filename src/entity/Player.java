package entity;

import main.KeyHandler;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{

    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH){

        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
        getPlayerImage();

    }

    public void setDefaultValues(){

        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage(){

        try{
            up1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/boy_right_2.png"));
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void update(){

//        if (keyH.upPressed && keyH.rightPressed && keyH.leftPressed && keyH.downPressed){
//        }
//        else if (keyH.upPressed && keyH.rightPressed && keyH.leftPressed) {
//            y -= speed;
//        }
//        else if (keyH.downPressed && keyH.rightPressed && keyH.leftPressed){
//            y += speed;
//        }
//        else if (keyH.upPressed && keyH.leftPressed){
//            y -= speed;
//            x -= speed;
//        }
//        else if (keyH.upPressed && keyH.rightPressed) {
//            y -= speed;
//            x += speed;
//        }
//        else if (keyH.upPressed && keyH.downPressed){
//        }
//        else if (keyH.downPressed && keyH.leftPressed){
//            y += speed;
//            x -= speed;
//        }
//        else if (keyH.downPressed && keyH.rightPressed){
//            y += speed;
//            x += speed;
//        }
//        else if (keyH.leftPressed && keyH.rightPressed){
//        }

        if (keyH.downPressed || keyH.leftPressed || keyH.upPressed || keyH.rightPressed) {
            if (keyH.upPressed){
                direction = "up";
                y -= speed;
            }
            else if (keyH.downPressed){
                direction = "down";
                y += speed;
            }
            else if (keyH.leftPressed){
                direction = "left";
                x -= speed;
            }
            else if (keyH.rightPressed) {
                direction = "right";
                x += speed;
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

    public void draw(Graphics2D g2){
//        g2.setColor(Color.white);
//
//        g2.fillRect(x,y,gp.tileSize,gp.tileSize);

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

        g2.drawImage(image,x,y,gp.tileSize,gp.tileSize,null);
    }
}
