package entity;

import main.GamePanel;

public class Projectile extends Entity{

    Entity user;

    public Projectile(GamePanel gp){
        super(gp);
    }

    public void subtractResource(Entity user){}
    /**
     * Checks if entity has enough mana to cast a spell
     * @param user The entity using the spell
     * @return Returns true if entity has enough mana, false otherwise
     */
    public boolean hasResource(Entity user){return false;}

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user){

        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.alive = alive;
        this.user = user;
        this.life = this.maxLife;

    }

    public void update(){

        if (user == gp.player){
            int monsterIndex = gp.checker.checkEntity(this,gp.monster);
            if (monsterIndex != 999){
                gp.player.damageMonster(monsterIndex, attack);
                alive = false;
            }
        }
        else{
            boolean contactPlayer = gp.checker.checkPlayer(this);
            if(!gp.player.invincible && contactPlayer){
                damagePlayer(attack);
                alive = false;
            }

        }


        switch(direction){
            case "up": worldY -= speed; break;
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
        }

        life --;
        if (life <= 0){
            alive = false;
        }
        spriteCounter++;
        if (spriteCounter > 12){
            if (spriteNum == 1){
                spriteNum = 2;
            } else{
                spriteNum = 1;
            }
        }
    }
}
