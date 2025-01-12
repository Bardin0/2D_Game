package entity;

import main.KeyHandler;
import main.GamePanel;
import object.OBJ_Key;
import object.OBJ_ShieldNormal;
import object.OBJ_SwordNormal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity{

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public boolean attackCanceled = false;

    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySpace = 20;

    public Player(GamePanel gp, KeyHandler keyH){

        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle(8,16,28,28);
        solidAreaDefaultX = 8;
        solidAreaDefaultY = 16;

        attackArea.width = 36;
        attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();

    }

    public void setItems(){

        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));

    }

    public void setDefaultValues(){

        worldX = gp.tileSize*23;
        worldY = gp.tileSize*21;
        speed = 5;
        direction = "down";

        // Stats
        level = 1;
        maxLife = 6;
        life = maxLife;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        currentWeapon = new OBJ_SwordNormal(gp);
        currentShield = new OBJ_ShieldNormal(gp);
        attack = getAttack();   // Calculated using strength and attackValue of weapon
        defense = getDefense(); // Calculated using dexterity and defenseValue of shield

    }

    public int getAttack(){

        return strength * currentWeapon.attackValue;

    }

    public int getDefense(){

        return dexterity * currentShield.defenseValue;

    }

    public void getPlayerImage(){

        up1 = setup("player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("player/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("player/boy_right_2", gp.tileSize, gp.tileSize);

    }

    public void getPlayerAttackImage(){
        attackUp1 = setup("player/boy_attack_up_1", gp.tileSize, gp.tileSize*2);
        attackUp2 = setup("player/boy_attack_up_2", gp.tileSize, gp.tileSize*2);
        attackDown1 = setup("player/boy_attack_down_1", gp.tileSize, gp.tileSize*2);
        attackDown2 = setup("player/boy_attack_down_2", gp.tileSize, gp.tileSize*2);
        attackLeft1 = setup("player/boy_attack_left_1", gp.tileSize*2, gp.tileSize);
        attackLeft2 = setup("player/boy_attack_left_2", gp.tileSize*2, gp.tileSize);
        attackRight1 = setup("player/boy_attack_right_1", gp.tileSize*2, gp.tileSize);
        attackRight2 = setup("player/boy_attack_right_2", gp.tileSize*2, gp.tileSize);
    }

    public void update(){

        if (attacking){
            attacking();
        }
        else if (keyH.downPressed || keyH.leftPressed || keyH.upPressed || keyH.rightPressed || keyH.enterPressed) {
            if (keyH.upPressed){
                direction = "up";
            }
            else if (keyH.downPressed){
                direction = "down";
            }
            else if (keyH.leftPressed){
                direction = "left";
            }
            else if (keyH.rightPressed){
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

            // Check monster collision
            int monsterIndex = gp.checker.checkEntity(this,gp.monster);
            contactMonster(monsterIndex);

            // Check event
            gp.eventH.checkEvent();

            // If collision is false, player can move
            if (!collisionOn && !keyH.enterPressed){
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

            if (keyH.enterPressed && !attackCanceled){
                gp.playSE(6);
                attacking = true;
                spriteCounter = 0;
            }

            attackCanceled = false;
            gp.keyHandler.enterPressed = false;

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

        if (invincible){
            invincibleCounter++;
            if(invincibleCounter > 60){
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void attacking(){

        spriteCounter++;

        if (spriteCounter <= 5){
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25){
            spriteNum = 2;

            // Save the current player position and solid area
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // adjust player position for attack

            switch(direction){
                case"up": worldY -= attackArea.height; break;
                case"down": worldY += attackArea.height; break;
                case"left": worldX -= attackArea.width; break;
                case"right": worldX += attackArea.width; break;
            }

            // attackArea become solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            //Check monster collision with updated position
            int monsterIndex = gp.checker.checkEntity(this,gp.monster);
            damageMonster(monsterIndex);

            // Restore data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.height = solidAreaHeight;
            solidArea.width = solidAreaWidth;

        }
        if (spriteCounter > 25){
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }

    }

    public void pickupObject(int i){

    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch(direction){
            case "up":

                if (!attacking){
                    if (spriteNum == 1){
                        image = up1;
                    }else{
                        image = up2;
                    }
                }else{
                    tempScreenY = screenY - gp.tileSize;
                    if (spriteNum == 1){
                        image = attackUp1;
                    }else{
                        image = attackUp2;
                    }
                }

                break;
            case "down":
                if (!attacking){
                    if (spriteNum == 1){
                        image = down1;
                    }else{
                        image = down2;
                    }
                }else{
                    if (spriteNum == 1){
                        image = attackDown1;
                    }else{
                        image = attackDown2;
                    }
                }
                break;
            case "left":
                if (!attacking){
                    if (spriteNum == 1){
                        image = left1;
                    }else{
                        image = left2;
                    }
                }else{
                    tempScreenX = screenX - gp.tileSize;
                    if (spriteNum == 1){
                        image = attackLeft1;
                    }else{
                        image = attackLeft2;
                    }
                }
                break;
            case "right":
                if (!attacking){
                    if (spriteNum == 1){
                        image = right1;
                    }else{
                        image = right2;
                    }
                }else{
                    if (spriteNum == 1){
                        image = attackRight1;
                    }else{
                        image = attackRight2;
                    }
                }
                break;
        }

        // Set transparency to player if invincible
        if (invincible){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F));
        }

        g2.drawImage(image,tempScreenX,tempScreenY,null);

        // Reset g2 transparency
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
    }

    public void interactNPC(int i){

        if (gp.keyHandler.enterPressed){
            if (i != 999){
                attackCanceled = true;
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
    }

    public void contactMonster(int i){

        if (i != 999){
            if (!invincible){
                gp.playSE(7);

                int damage = gp.monster[i].attack - defense;
                if (damage < 0){
                    damage = 0;
                }

                life-= damage;
                invincible = true;
            }

        }
    }

    public void damageMonster(int i){

        if (i != 999) {

            if (!gp.monster[i].invincible){
                gp.playSE(5);

                int damage = attack - gp.monster[i].defense;
                if (damage < 0){
                    damage = 0;
                }
                gp.monster[i].life -= damage;
                gp.ui.addMessage(damage + " damage!");
                gp.monster[i].invincible = true;
                gp.monster[i].damageReaction();

                if (gp.monster[i].life <= 0){
                    gp.monster[i].dying = true;
                    gp.ui.addMessage(gp.monster[i].name + " died!");
                    gp.ui.addMessage("EXP + " + gp.monster[i].exp);
                    exp += gp.monster[i].exp;
                    checkLevelUp();
                }
            }
        }
    }

    public void checkLevelUp(){

        if (exp > nextLevelExp){
            exp -= nextLevelExp;
            level ++;
            nextLevelExp = nextLevelExp*2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();
            gp.playSE(8);

            gp.gameState = gp.dialogueState;
            gp.ui.currentDialouge = "You Leveled Up!";
        }



    }


}
