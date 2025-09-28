package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_SwordNormal extends Entity {

    public OBJ_SwordNormal(GamePanel gp){

        super(gp);
        name = "NormalSword";
        down1 = setup("objects/sword_normal", gp.tileSize, gp.tileSize);
        attackValue = 1;
        price = 15;
        description = "[Sword] - A blunt sword found washed on the beach, inscribed with the letters BCE.";
        attackArea.width = 36;
        attackArea.height = 36;
        type = typeSword;

    }

}
