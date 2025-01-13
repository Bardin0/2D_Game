package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_RedPotion extends Entity {

    GamePanel gp;
    int value = 5;

    public OBJ_RedPotion(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = typeConsumable;
        name = "Red Potion";
        down1 = setup("objects/potion_red",gp.tileSize,gp.tileSize);
        description = "[" + name + "] - Magic potion \ncrafted in tromal. \nHeals for " + value+ " HP.";
    }

    public void use(Entity entity){

        gp.gameState = gp.dialogueState;
        gp.ui.currentDialouge = "You drink the " + name + ".\n Your life has been \n recovered by " + value + ".";
        entity.life += value;
        if(gp.player.life > gp.player.maxLife){
            gp.player.life = gp.player.maxLife;
        }

        gp.playSE(2);

    }


}