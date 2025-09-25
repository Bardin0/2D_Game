package object;

import entity.Entity;
import main.GamePanel;
import main.Sound;

public class OBJ_SuspiciousShroom extends Entity {

    GamePanel gp;

    public OBJ_SuspiciousShroom(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = typeConsumable;
        price = 2;
        name = "Suspicious Shroom";
        down1 = setup("objects/SuspiciousShroom",gp.tileSize,gp.tileSize);
        description = "[" + name + "] - \nA suspicious fungi. \nIt looks like it \n can be consumed.\n I wonder what happens\n" +
                " if you eat it?";
        value = 3;
    }

    public void use(Entity entity){

        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You ate the " + name + ". Why would you do that?";
        entity.speed += value;
        entity.onShrooms = true;
        entity.shroomDuration = System.currentTimeMillis();
        gp.playSE(Sound.SoundType.POWERUP);

    }
}
