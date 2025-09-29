package main;

import entity.NPC_Josh;
import entity.NPC_Merchant;
import entity.NPC_OldMan;
import entity.NPC_TyroneThugsley;
import interactive_tiles.IT_DryTree;
import monster.MON_GreenSlime;
import object.*;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setObject(){

        int mapNum = 0;
        int i = 0;
        gp.obj[mapNum][i] = new OBJ_ManaCrystal(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 25;
        gp.obj[mapNum][i].worldY = gp.tileSize * 23;
        i++;

        gp.obj[mapNum][i] = new OBJ_CoinBronze(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 21;
        gp.obj[mapNum][i].worldY = gp.tileSize * 19;
        i++;

        gp.obj[mapNum][i] = new OBJ_Key(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 26;
        gp.obj[mapNum][i].worldY = gp.tileSize * 21;
        i++;

        gp.obj[mapNum][i] = new OBJ_Axe(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 33;
        gp.obj[mapNum][i].worldY = gp.tileSize * 21;
        i++;

        gp.obj[mapNum][i] = new OBJ_BlueShield(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 35;
        gp.obj[mapNum][i].worldY = gp.tileSize * 21;
        i++;

        gp.obj[mapNum][i] = new OBJ_RedPotion(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 27;
        gp.obj[mapNum][i].worldY = gp.tileSize * 22;

        gp.obj[mapNum][i] = new OBJ_Heart(gp);
        gp.obj[mapNum][i].worldX = gp.tileSize * 28;
        gp.obj[mapNum][i].worldY = gp.tileSize * 22;
    }

    public void setNPC(){

        int mapNum = 0;
        int i = 0;

        // Map 0
        gp.npc[mapNum][i] = new NPC_OldMan(gp);
        gp.npc[mapNum][i].worldX = gp.tileSize*23;
        gp.npc[mapNum][i].worldY = gp.tileSize*35;
        i++;

        // Map 1
        mapNum = 1;
        i = 0;
        gp.npc[mapNum][i] = new NPC_Merchant(gp);
        gp.npc[mapNum][i].worldX = gp.tileSize*12;
        gp.npc[mapNum][i].worldY = gp.tileSize*7;
        i++;

        gp.npc[mapNum][i] = new NPC_Josh(gp);
        gp.npc[mapNum][i].worldX = gp.tileSize*10;
        gp.npc[mapNum][i].worldY = gp.tileSize*7;
        i++;

        gp.npc[mapNum][i] = new NPC_TyroneThugsley(gp);
        gp.npc[mapNum][i].worldX = gp.tileSize*14;
        gp.npc[mapNum][i].worldY = gp.tileSize*7;
        i++;

    }

    public void setMonster(){

        int mapNum = 0;
        int i = 0;
        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*23;
        gp.monster[mapNum][i].worldY = gp.tileSize*36;
        i++;

        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*23;
        gp.monster[mapNum][i].worldY = gp.tileSize*37;
        i++;

        gp.monster[mapNum][i] = new MON_GreenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tileSize*23;
        gp.monster[mapNum][i].worldY = gp.tileSize*38;

    }

    public void setInteractiveTiles(){

        int mapNum = 0;
        int i = 0;
        gp.interactiveTiles[mapNum][i] = new IT_DryTree(gp, 27, 12); i++;
        gp.interactiveTiles[mapNum][i] = new IT_DryTree(gp, 28, 12); i++;
        gp.interactiveTiles[mapNum][i] = new IT_DryTree(gp, 29, 12); i++;
        gp.interactiveTiles[mapNum][i] = new IT_DryTree(gp, 30, 12); i++;
        gp.interactiveTiles[mapNum][i] = new IT_DryTree(gp, 31, 12); i++;
        gp.interactiveTiles[mapNum][i] = new IT_DryTree(gp, 32, 12); i++;
        gp.interactiveTiles[mapNum][i] = new IT_DryTree(gp, 33, 12);

    }

}
