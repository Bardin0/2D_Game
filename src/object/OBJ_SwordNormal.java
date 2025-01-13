package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_SwordNormal extends Entity {

    public OBJ_SwordNormal(GamePanel gp){

        super(gp);
        name = "NormalSword";
        down1 = setup("objects/sword_normal", gp.tileSize, gp.tileSize);
        attackValue = 1;
        description = "[Sword] - A blunt sword \nfound washed on the \nbeach, inscribed with the \nletters BCE.";
        attackArea.width = 36;
        attackArea.height = 36;
        type = typeSword;

    }

}
