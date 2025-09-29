package entity;

import main.GamePanel;

import java.util.logging.Level;

public class NPC_Josh extends Entity{

    public NPC_Josh(GamePanel gp){
        super(gp);

        direction = "down";
        speed = 0;
        type = typeNPC;

        getImage();
        setDialogue();
    }

    public void getImage(){

        try{
            up1 = setup("npc/Josh", gp.tileSize, gp.tileSize);
            up2 = setup("npc/Josh", gp.tileSize, gp.tileSize);
            down1 = setup("npc/Josh", gp.tileSize, gp.tileSize);
            down2 = setup("npc/Josh", gp.tileSize, gp.tileSize);
            left1 = setup("npc/Josh", gp.tileSize, gp.tileSize);
            left2 = setup("npc/Josh", gp.tileSize, gp.tileSize);
            right1 = setup("npc/Josh", gp.tileSize, gp.tileSize);
            right2 = setup("npc/Josh", gp.tileSize, gp.tileSize);
        }catch(Exception e){
            gp.LOGGER.log(Level.WARNING, "NPC Josh could not be loaded");
        }

    }

    public void setDialogue(){
        dialogues[0] = "Hmm. Is this on?";
        dialogues[1] = "I made a chronal accelerator. I'm sure I can do this.";
        dialogues[2] = "To all agents of O—";
        dialogues[3] = "To all agents of Overwatch. That's not right.";
        dialogues[4] = "To the former agents of Overwatch. This is Winston! Ha ha! Obviously...";
        dialogues[5] = "Thirty years ago, the Omnics declared war. The nations of the world had no answer, until they called upon a small group of heroes. Overwatch was created to rescue humanity from the Omnic Crisis. " +
                "We became the greatest champions of peace and progress mankind has ever seen!";
        dialogues[6] = "ou were chosen because you had powers and abilities that made you— You joined because you... You already know this... Look, the people decided they were better off without us. They even called us criminals! They tore our family apart. But look around! Someone has to do something! We have to do something! " +
                "We can make a difference again. The world needs us now, more than ever! Are you with me? ";
    }

    public void speak(){

        super.speak();

    }
}
