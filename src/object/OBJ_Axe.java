package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity {

    public OBJ_Axe(GamePanel gp){

        super(gp);

        name = "Yula's Axe";
        down1 = setup("objects/axe", gp.tileSize, gp.tileSize);
        attackValue = 2;
        price = 25;
        attackArea.width = 30;
        attackArea.height = 30;
        type = typeAxe;
        description = "[Yula's Axe] - An axe belonging to Yula, who's that?";

    }

}
