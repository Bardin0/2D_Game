package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_CoinBronze extends Entity {

    GamePanel gp;

    public OBJ_CoinBronze(GamePanel gp){

        super(gp);
        this.gp = gp;

        type = typePickupOnly;
        name = "Bronze Coin";
        down1 = setup("objects/coin_bronze", gp.tileSize, gp.tileSize);
        value = 1;

    }

    public void use(Entity entity){

        gp.ui.addMessage("+" + value + " Coin");
        gp.playSE(1);
        gp.player.coin += 1;

    }

}
