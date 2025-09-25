package entity;

// Imports
import main.GamePanel;
import main.Sound;
import main.UtilityTool;
import tile.InteractiveTile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The Entity class represents a general unit in the game, such as a player, NPC, monster, or item.
 * It includes attributes for position, appearance, interactions, and behavior.
 */
public class Entity {

    // Entity world coordinates
    public int worldX, worldY;

    // Entity action images
    public BufferedImage up1, up2, down1, down2, right1, right2, left1, left2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public String direction = "down";

    // Sprite animation control
    public int spriteCounter = 0;
    public int spriteNum = 1;

    // Hitboxes
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    public boolean collisionOn = false;
    public int solidAreaDefaultX, solidAreaDefaultY;

    public int actionLockCounter = 0;

    // Dialogue
    String[] dialogues = new String[20];
    int dialogueIndex = 0;

    // Attributes for smaller sprites such as heart
    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public int invincibleCounter = 0;

    // State trackers
    int dyingCounter = 0;
    private int swapTracker = 0;
    boolean hpBarOn = false;
    int hpBarCounter = 0;
    public boolean alive = true;
    public boolean dying = false;

    // Stats
    public int maxLife;
    public int life;
    public int level;
    public int speed;
    public int strength;
    public int dexterity;
    public int defense;
    public int attack;
    public int exp;
    public int nextLevelExp;
    public int coin;
    public int mana;
    public int maxMana;
    public int ammo;

    // Equipment
    public Entity currentWeapon;
    public Entity currentShield;
    public Projectile projectile;

    // Item attributes
    public int value;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost = 0;
    public int shotAvailable = 0;

    // Entity Types
    public int type;
    public final int typePlayer = 0;
    public final int typeNPC = 1;
    public final int typeMonster = 2;
    public final int typeSword = 3;
    public final int typeAxe = 4;
    public final int typeShield = 5;
    public final int typeConsumable = 6;
    public final int typePickupOnly = 7;

    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySpace = 20;

    public int price;

    public boolean onShrooms = false;
    public long shroomDuration = 0;

    GamePanel gp;

    /**
     * Constructs a new Entity object associated with a specific GamePanel.
     * @param gp The {@link GamePanel} instance to which this entity belongs.
     */
    public Entity(GamePanel gp){

        this.gp = gp;

    }

    // OVERRIDDEN METHODS START HERE
    /**
     * Handles the reaction when the entity takes damage.
     * This method is expected to be executed when the entity receives damage
     * and defines the entity's response such as visual, behavioral, or state changes
     * due to the damage received.
     */
    public void damageReaction(){}

    /**
     * Performs an action when the given entity is used. The specific behavior depends
     * on the implementation of this method in subclasses or the context in which it is invoked.
     * @param entity The entity that is the target or context of the use action.
     */
    public void use(Entity entity){}

    /**
     * Defines the behavior or actions for this entity during the game loop.
     * The method determines and sets the current action or state of the entity
     * based on its specific behavior, logic, or AI patterns. Actions may
     * include movement, attacks, interaction, or idle states depending on
     * the entity type and conditions.
     */
    public void setAction(){}

    /**
     * Handles the logic for determining if an item or object should be dropped
     * upon the event of a specific condition, such as the defeat of an entity.
     * This method is typically invoked when an entity dies or when a predefined
     * action requiring item drop mechanics occurs in the game. Based on conditions
     * such as the entity's type, state, or other gameplay factors, appropriate
     * items are determined for dropping into the game world.
     */
    public void checkDrop(){}
    // OVERRIDDEN METHODS END HERE

    /**
     * Loads an image from the given file path, scales it to the specified width and height,
     * and returns the resulting image. This method uses the UtilityTool class to perform
     * the scaling operation.
     *
     * @param imagePath The path to the image file (without the file extension).
     * @param width     The desired width of the scaled image.
     * @param height    The desired height of the scaled image.
     * @return A BufferedImage object representing the scaled image.
     * @throws RuntimeException If the image cannot be loaded or if an IO error occurs.
     */
    public BufferedImage setup(String imagePath, int width, int height){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image;

        try{
            image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath + ".png")));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return image;
    }

    /**
     * Renders the entity on the screen using the provided {@link Graphics2D} object.
     * The method calculates the appropriate position on the screen relative to the world position
     * and the player's position. It also handles conditional rendering like HP bar, invincibility,
     * and dying animations.
     *
     * @param g2 The {@link Graphics2D} object used for drawing the entity and related visuals.
     */
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

            // Monster HP bar
            if (type == 2 && hpBarOn){

                double oneScale = (double)gp.tileSize/maxLife;
                double hpBarValue = oneScale*life;

                // Outline/Background
                g2.setColor(new Color(35,35,35));
                g2.fillRect(screenX-1,screenY-16, gp.tileSize+2,12);
                // Bar
                g2.setColor(new Color(255,0,30));
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;
                if(hpBarCounter > 600){
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (!(this instanceof InteractiveTile)){
                if (invincible){
                    hpBarOn = true;
                    hpBarCounter = 0;
                    changeAlpha(g2, 0.4F);
                }

            }

            if (dying){
                playDeathAnimation(g2);
            }

            g2.drawImage(image,screenX,screenY,null);

            // Reset g2 transparency
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));

        }
    }

    /**
     * Updates the state and behavior of the entity during the game loop.
     * This method executes various actions such as movement, collision detection,
     * interaction with entities, sprite animation updates, and handling invincibility
     * or attack mechanics
     * Key actions performed include:
     * - Setting the current action of the entity using the setAction method.
     * - Checking collisions with tiles, objects, entities including NPCs,
     *   monsters, and interactive tiles, as well as the player.
     * - Applying damage to the player if the entity is a monster and contacts
     *   the player while they are not invincible.
     * - Updating the entity's position based on its direction and speed if no collision occurs.
     * - Managing sprite animations for visual representation.
     * - Handling the invincibility state of the entity, including decrementing its timer.
     * - Tracking the reload time for actions such as attacking or shooting.
     */
    public void update(){
        setAction();

        collisionOn = false;
        gp.checker.checkTile(this);
        gp.checker.checkObject(this,false);
        gp.checker.checkEntity(this,gp.npc);
        gp.checker.checkEntity(this,gp.monster);
        gp.checker.checkEntity(this,gp.interactiveTiles);
       boolean contactPlayer =  gp.checker.checkPlayer(this);

       // Monster contacts player
       if (this.type == 2 && contactPlayer && !gp.player.invincible){
           damagePlayer(attack);
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
        if (spriteCounter > 24){
            if (spriteNum == 1){
                spriteNum = 2;
            }else if (spriteNum == 2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        if (invincible){
            invincibleCounter++;
            if(invincibleCounter > 30){
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (shotAvailable < 30){
            shotAvailable++;
        }

    }

    /**
     * Executes the dialogue interaction for the entity.
     * The method cycles through predefined dialogues for the entity, updating
     * the current dialogue shown in the user interface. If the entity has exhausted
     * all dialogues, it resets to the first one.
     * Additionally, based on the player's direction, it adjusts the entity's
     * facing direction to face the player during the interaction.
     */
    public void speak(){

        if (dialogues[dialogueIndex] == null){
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
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

    /**
     * Plays the death animation for the entity. This method alternates the
     * transparency of the entity's appearance to create a fading effect
     * and updates the entity's state when the animation is complete.
     *
     * @param g2 The Graphics2D object used for rendering the animation.
     */
    public void playDeathAnimation(Graphics2D g2){

        dyingCounter++;

        if (dyingCounter % 5 == 0){
            if (swapTracker == 0){
                changeAlpha(g2, 0);
                swapTracker = 1;
            }else{
                changeAlpha(g2,1);
                swapTracker = 0;
            }
        }
        if (dyingCounter > 40){
            alive = false;
        }
    }

    /**
     * Changes the transparency level (alpha) of the provided Graphics2D object.
     *
     * @param g2    The Graphics2D object whose transparency level will be altered.
     * @param alpha The alpha value to set, where 0.0 is completely transparent and 1.0 is fully opaque.
     */
    public void changeAlpha(Graphics2D g2, float alpha){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    /**
     * Reduces the player's life by calculating the damage received
     * and sets the player to an invincible state temporarily.
     *
     * @param attack The attack value inflicted on the player.
     */
    public void damagePlayer(int attack){
        gp.playSE(Sound.SoundType.RECEIVE_DAMAGE);

        int damage = attack - gp.player.defense;

        if (damage < 0){
            damage = 0;
        }

        gp.player.life -= damage;
        gp.player.invincible = true;
    }

    /**
     * Drops an item into the game world at the entity's current position.
     * The dropped item is placed into the first available slot in the game panel's object list.
     *
     * @param droppedItem The entity representing the item to be dropped into the game world.
     */
    public void dropItem(Entity droppedItem){

        for (int i = 0; i < gp.obj[1].length; i++){
            if (gp.obj[gp.currentMap][i] == null){
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.obj[gp.currentMap][i].worldX = worldX;
                gp.obj[gp.currentMap][i].worldY = worldY;
                break;
            }
        }

    }

    /**
     * Retrieves the color of the particle associated with this entity.
     * The color determines the visual appearance of the particle when rendered.
     *
     * @return The color of the particle as a {@link Color} object, or null if no color is defined.
     */
    public Color getParticleColor() {
        return null;
    }

    /**
     * Retrieves the size of the particle.
     *
     * @return The size of the particle as an integer.
     */
    public int getParticleSize(){
        return 0;
    }

    /**
     * Retrieves the speed of the particle, which influences how fast it moves within the game world.
     *
     * @return The speed of the particle as an integer value.
     */
    public int getParticleSpeed(){
        return 0;
    }

    /**
     * Retrieves the maximum lifespan of a particle.
     *
     * @return The maximum life value for a particle, representing how many updates
     *         the particle exists before it is removed or fades out.
     */
    public int getParticleMaxLife(){
        return 0;
    }

    /**
     * Generates particle effects based on the properties of the generator entity
     * and adds them to the game panel's particle list. Four particles are created
     * with varying movement directions to simulate an explosion or diffusion effect.
     *
     * @param generator The entity responsible for generating the particles,
     *                  providing their initial properties such as color, size,
     *                  speed, and maximum lifetime.
     * @param target    The entity that is the target of the particle generation
     *                  (currently unused in this implementation).
     */
    public void generateParticle (Entity generator, Entity target){

        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particle p1 = new Particle(gp, generator, color, size, speed, maxLife, -1, -1);
        Particle p2 = new Particle(gp, generator, color, size, speed, maxLife, 1, -1);
        Particle p3 = new Particle(gp, generator, color, size, speed, maxLife, -1, 1);
        Particle p4 = new Particle(gp, generator, color, size, speed, maxLife, 1, 1);

        gp.particles.add(p1);
        gp.particles.add(p2);
        gp.particles.add(p3);
        gp.particles.add(p4);


    }

}
