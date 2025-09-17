package entity;

import main.KeyHandler;
import main.GamePanel;
import main.Sound;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_ShieldNormal;
import object.OBJ_SwordNormal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity{

    KeyHandler keyH;

    // Coordinates on the screen
    public final int screenX;
    public final int screenY;

    public boolean attackCanceled = false;

    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySpace = 20;

    public Player(GamePanel gp, KeyHandler keyH){

        // Instantiate GamePanel and KeyHandler
        super(gp);
        this.keyH = keyH;

        // Locks player to center of the screen
        // The world moves around the player, not the player moving in the world
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        // Define player's solid area
        solidArea = new Rectangle(8,16,28,28);
        solidAreaDefaultX = 8;
        solidAreaDefaultY = 16;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();

    }

    public void setDefaultValues(){

        worldX = gp.tileSize*23;
        worldY = gp.tileSize*21;
        speed = 5;
        direction = "down";
        type = typePlayer;

        // Stats
        level = 1;
        maxLife = 6;
        life = maxLife;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        ammo = 10;
        currentWeapon = new OBJ_SwordNormal(gp);
        currentShield = new OBJ_ShieldNormal(gp);
        attack = getAttack();   // Calculated using strength and attackValue of weapon
        defense = getDefense(); // Calculated using dexterity and defenseValue of shield
        projectile = new OBJ_Fireball(gp);

        maxMana = 4;
        mana = maxMana;

    }

    /**
     * Sets the player's starting items in their inventory.
     */
    public void setItems(){

        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));

    }

    /**
     * Loads the players images
     */
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

    /**
     * Loads the player's attack images based on their current weapon
     */
    public void getPlayerAttackImage(){

        if (currentWeapon.type == typeSword){
            attackUp1 = setup("player/boy_attack_up_1", gp.tileSize, gp.tileSize*2);
            attackUp2 = setup("player/boy_attack_up_2", gp.tileSize, gp.tileSize*2);
            attackDown1 = setup("player/boy_attack_down_1", gp.tileSize, gp.tileSize*2);
            attackDown2 = setup("player/boy_attack_down_2", gp.tileSize, gp.tileSize*2);
            attackLeft1 = setup("player/boy_attack_left_1", gp.tileSize*2, gp.tileSize);
            attackLeft2 = setup("player/boy_attack_left_2", gp.tileSize*2, gp.tileSize);
            attackRight1 = setup("player/boy_attack_right_1", gp.tileSize*2, gp.tileSize);
            attackRight2 = setup("player/boy_attack_right_2", gp.tileSize*2, gp.tileSize);
        }
        else if (currentWeapon.type == typeAxe){
            attackUp1 = setup("player/boy_axe_up_1", gp.tileSize, gp.tileSize*2);
            attackUp2 = setup("player/boy_axe_up_2", gp.tileSize, gp.tileSize*2);
            attackDown1 = setup("player/boy_axe_down_1", gp.tileSize, gp.tileSize*2);
            attackDown2 = setup("player/boy_axe_down_2", gp.tileSize, gp.tileSize*2);
            attackLeft1 = setup("player/boy_axe_left_1", gp.tileSize*2, gp.tileSize);
            attackLeft2 = setup("player/boy_axe_left_2", gp.tileSize*2, gp.tileSize);
            attackRight1 = setup("player/boy_axe_right_1", gp.tileSize*2, gp.tileSize);
            attackRight2 = setup("player/boy_axe_right_2", gp.tileSize*2, gp.tileSize);
        }

    }

    /**
     * Calculates the player's attack value based on their strength and weapon
     * and updates the attack area based on the equipped weapon.
     * @return The player's attack value
     */
    public int getAttack(){

        attackArea = currentWeapon.attackArea;
        return strength * currentWeapon.attackValue;

    }

    /**
     * Calculates the player's defense value based on their dexterity and shield
     * @return The player's defense value
     */
    public int getDefense(){

        return dexterity * currentShield.defenseValue;

    }

    /**
     * Updates the player's state, position, and interactions each frame.
     */
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

            // Check interactive tile collision
            gp.checker.checkEntity(this,gp.interactiveTiles);

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
                gp.playSE(Sound.SoundType.SWING_SWORD);
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

            if(life > maxLife){
                life = maxLife;
            }

            if(mana > maxMana){
                mana = maxMana;
            }

            if (life <= 0){
              gp.gameState = gp.gameOverState;
            }
        }

        /*
         * Did the user hit the shoot key?
         * Is there a fireball already on screen?
         * Is shooting on cooldown?
         * Does the player have enough mana to shoot?
         * If yes to all of these then spawn a fireball projectile
         */
        if (gp.keyHandler.shootKeyPressed && !projectile.alive && shotAvailable == 30 && projectile.hasResource(this)){

            projectile.set(worldX, worldY, direction, true, this);
            projectile.subtractResource(this);

            gp.projectileList.add(projectile);
            gp.playSE(Sound.SoundType.FIREBALL);
            shotAvailable = 0;
        }

        if (invincible){
            invincibleCounter++;
            if(invincibleCounter > 60){
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (shotAvailable < 30){
            shotAvailable++;
        }
    }

    /**
     * Handles the player's attacking state and collision detection during an attack.
     */
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
            damageMonster(monsterIndex, attack);

            int iTileIndex = gp.checker.checkEntity(this,gp.interactiveTiles);
            damageInteractiveTile(iTileIndex);

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

        if (i != 999){

            // Pickup Only Items
            if (gp.obj[i].type == typePickupOnly){
                gp.obj[i].use(this);
                gp.obj[i] = null;   // Remove from the map
            }
            else{
                //Inventory Items
                String text;

                if (inventory.size() != maxInventorySpace){

                    inventory.add(gp.obj[i]);
                    gp.playSE(Sound.SoundType.COIN);
                    text = "Picked up " + gp.obj[i].name;

                }
                else{
                    text = "Inventory Full";
                }

                gp.ui.addMessage(text);

            }
            gp.obj[i] = null;
        }
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
                gp.playSE(Sound.SoundType.RECEIVE_DAMAGE);

                int damage = gp.monster[i].attack - defense;
                if (damage < 0){
                    damage = 0;
                }

                life-= damage;
                invincible = true;
            }

        }
    }

    public void damageMonster(int i, int attack){

        if (i != 999) {

            if (!gp.monster[i].invincible){
                gp.playSE(Sound.SoundType.HITMONSTER);

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
            gp.playSE(Sound.SoundType.LEVEL_UP);

            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You Leveled Up!";
        }



    }

    public void selectItem(){

        int itemIndex = gp.ui.getItemInventoryIndex();

        if (itemIndex < inventory.size()){
            Entity selectedItem = inventory.get(itemIndex);

            if (selectedItem.type == typeSword || selectedItem.type == typeAxe){
                currentWeapon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage();
            }

            if (selectedItem.type == typeShield){
                currentShield = selectedItem;
                defense = getDefense();
            }

            if (selectedItem.type == typeConsumable){
                selectedItem.use(this);
                inventory.remove(itemIndex);
            }

        }

    }

    /**
     * Checks to see if the player can damage an interactive tile.
     * @param i The index of the interactive tile to check
     */
    public void damageInteractiveTile(int i ){

        if (i != 999 && gp.interactiveTiles[i].destructible && gp.interactiveTiles[i].checkValidWeaponToBreak(this) && !gp.interactiveTiles[i].invincible){
            gp.interactiveTiles[i].playSE();
            gp.interactiveTiles[i].life--;
            gp.interactiveTiles[i].invincible = true;

            generateParticle(gp.interactiveTiles[i], gp.interactiveTiles[i]);

            if (gp.interactiveTiles[i].life == 0) {
                gp.interactiveTiles[i] = gp.interactiveTiles[i].getDestroyedForm();
            }
        }

    }
}
