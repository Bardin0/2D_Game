package entity;

import main.GamePanel;
import object.OBJ_Boots;
import object.OBJ_Key;
import object.OBJ_RedPotion;
import object.OBJ_SuspiciousShroom;

import java.util.Random;

public class NPC_Merchant extends Entity{

    public NPC_Merchant(GamePanel gp){
        super(gp);

        direction = "down";
        speed = 0;
        type = typeNPC;

        getImage();
        setDialogue();
        setItems();
    }

    public void getImage(){

        down1 = setup("npc/merchant_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("npc/merchant_down_2", gp.tileSize, gp.tileSize);
        up1 = down1;
        up2 = down2;
        left1 = down1;
        left2 = down2;
        right1 = down1;
        right2 = down2;

    }

    public void setDialogue(){
        dialogues[0] = "Hey kid... want to buy something... fun?";
    }

    public void speak(){
        super.speak();
        gp.gameState = gp.tradeState;
        gp.ui.npc = this;
    }

    public void setItems(){
        inventory.add(new OBJ_RedPotion(gp));
        inventory.add(new OBJ_RedPotion(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Boots(gp));
        inventory.add(new OBJ_SuspiciousShroom(gp));
    }


}

