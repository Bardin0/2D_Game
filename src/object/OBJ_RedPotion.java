package object;

import entity.Entity;
import main.GamePanel;
import main.Sound;

public class OBJ_RedPotion extends Entity {

    GamePanel gp;

    public OBJ_RedPotion(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = typeConsumable;
        name = "Red Potion";
        down1 = setup("objects/potion_red",gp.tileSize,gp.tileSize);
        value = 5;
        price = 5;
        description = "[" + name + "] - Magic potion crafted in tromal. Heals for " + value+ " HP.";
    }

    public void use(Entity entity){

        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You drink the " + name + ". Your life has been recovered by " + value + ".";
        entity.life += value;
        if(gp.player.life > gp.player.maxLife){
            gp.player.life = gp.player.maxLife;
        }

        gp.playSE(Sound.SoundType.POWERUP);

    }


}
