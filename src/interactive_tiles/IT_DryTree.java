package interactive_tiles;

import main.GamePanel;
import entity.Entity;
import tile.InteractiveTile;

public class IT_DryTree extends InteractiveTile {

    GamePanel gp;

    public IT_DryTree(GamePanel gp, int col, int row){
        super(gp, col, row);
        this.gp = gp;

        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        down1 = setup("interactive_tiles/drytree", gp.tileSize, gp.tileSize);
        destructible = true;

        life = 3;
    }

    public boolean checkValidWeaponToBreak(Entity entity){

        return entity.currentWeapon.type == typeAxe;
    }

    public void playSE(){
        gp.playSE(11);
    }

    public InteractiveTile getDestroyedForm(){
        return new IT_Trunk(gp,worldX/gp.tileSize,worldY/gp.tileSize);
    }

}
