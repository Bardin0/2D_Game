package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ShieldNormal extends Entity {

    public OBJ_ShieldNormal(GamePanel gp){

        super(gp);
        name = "Normal Shield";
        down1 = setup("objects/shield_wood", gp.tileSize, gp.tileSize);
        defenseValue = 1;
        description = "[Wooden Shield] - A basic \nshield, nothing special \nabout it.";
        type = typeShield;
    }
}
