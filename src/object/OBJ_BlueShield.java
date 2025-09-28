package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_BlueShield extends Entity {

    public OBJ_BlueShield(GamePanel gp){

        super(gp);
        name = "Blue Shield";
        down1 = setup("objects/shield_blue", gp.tileSize, gp.tileSize);
        defenseValue = 2;
        price = 42;
        description = "[" + name + "] - A basic shield, it's blue!.";
        type = typeShield;

    }

}
