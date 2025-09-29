package entity;

import main.GamePanel;

import java.util.logging.Level;

public class NPC_TyroneThugsley extends Entity{

    public NPC_TyroneThugsley(GamePanel gp){
        super(gp);

        direction = "down";
        speed = 0;
        type = typeNPC;

        getImage();
        setDialogue();
    }

    public void getImage(){

        try{
            up1 = setup("npc/TyroneThugsley", gp.tileSize, gp.tileSize);
            up2 = setup("npc/TyroneThugsley", gp.tileSize, gp.tileSize);
            down1 = setup("npc/TyroneThugsley", gp.tileSize, gp.tileSize);
            down2 = setup("npc/TyroneThugsley", gp.tileSize, gp.tileSize);
            left1 = setup("npc/TyroneThugsley", gp.tileSize, gp.tileSize);
            left2 = setup("npc/TyroneThugsley", gp.tileSize, gp.tileSize);
            right1 = setup("npc/TyroneThugsley", gp.tileSize, gp.tileSize);
            right2 = setup("npc/TyroneThugsley", gp.tileSize, gp.tileSize);
        }catch(Exception e){
            gp.LOGGER.log(Level.WARNING, "NPC Tyrone Thugsley could not be loaded");
        }
    }

    public void setDialogue(){
        dialogues[0] = "What's good kid, whatchu want?";
        dialogues[1] = "I'm the dapper thug of Yonwarp, Tyrone Thugsley.";
        dialogues[2] = "I've got some mad drip, that which you couldn't even fathom in your wildest dreams.";
        dialogues[3] = "I know I'm easy on the eyes, but don't let that distract you from my impeccable fashion sense.";
        dialogues[4] = "Let me know if you see any baddies that need some... dealing with, from the thug.";
        dialogues[5] = "I've heard they are doing some shady business down by the beach, you should check it out for me.";
        dialogues[6] = "I'll make it worth your while if you find some real good ones with a fat... fat pocket, they keep the shady stuff in them.";
    }

    public void speak(){

        super.speak();

    }
}
