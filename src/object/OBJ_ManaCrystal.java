package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ManaCrystal extends Entity {

    GamePanel gp;

    public OBJ_ManaCrystal(GamePanel gp){
        super(gp);
        this.gp = gp;
        type = typePickupOnly;
        value = 1;

        name = "Mana Crystal";
        down1 = setup("objects/manacrystal_full", gp.tileSize, gp.tileSize);
        image = setup("objects/manacrystal_full", gp.tileSize, gp.tileSize);
        image2 = setup("objects/manacrystal_blank", gp.tileSize, gp.tileSize);

    }

    public void use (Entity entity){

        gp.playSE(2);
        gp.ui.addMessage("+" + value + " Mana");
        entity.mana += value;

    }

}
