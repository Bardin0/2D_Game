package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity {

    public OBJ_Key(GamePanel gp){

        super(gp);
        name = "Key";
        price = 10;
        down1 = setup("objects/key", gp.tileSize, gp.tileSize);
        description = "[Key] - Opens every door in the world apparently?";

    }

}
