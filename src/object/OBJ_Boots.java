package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Boots extends Entity {

    public OBJ_Boots(GamePanel gp){

        super(gp);
        name = "Swift Boots";
        price = 50;
        down1 = setup("objects/boots", gp.tileSize, gp.tileSize);
        description = "[" + name + "] - A pair of boots. Increases speed.";
        collision = true;

    }

}
