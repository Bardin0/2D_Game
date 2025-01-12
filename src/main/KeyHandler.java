package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
    //DEBUG
    public boolean checkDrawTime = false;

    public KeyHandler(GamePanel gp){
        this.gp=gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        // Title State
        if (gp.gameState == gp.titleState) {
            titleState(code);
        }
        // Play State
        else if (gp.gameState == gp.playState) {
            playState(code);
        }

        //Pause State
        else if (gp.gameState == gp.pauseState){
            pauseState(code);
        }

        //Dialogue state
        else if (gp.gameState == gp.dialogueState){
            dialogueState(code);
        }

        // Menu State
        else if (gp.gameState == gp.menuState){
            menuState(code);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
    }

    public void titleState(int code){

        if (code == KeyEvent.VK_W){
            gp.ui.commandNumber--;
            if (gp.ui.commandNumber < 0){
                gp.ui.commandNumber = 2;
            }
        }
        if (code == KeyEvent.VK_S){
            gp.ui.commandNumber++;
            if (gp.ui.commandNumber > 2){
                gp.ui.commandNumber = 0;
            }
        }

        if (code == KeyEvent.VK_ENTER){
            if (gp.ui.commandNumber == 0){
                gp.gameState = gp.playState;
                gp.playMusic(0);
            }
            else if (gp.ui.commandNumber == 1){
                //add later
            }
            else if (gp.ui.commandNumber == 2){
                System.exit(0);
            }
        }
    }

    public void playState(int code){

        if(code == KeyEvent.VK_W){
            upPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.pauseState;
        }
        if (code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }
        if (code == KeyEvent.VK_C){
            gp.gameState = gp.menuState;
        }

        // Debug
        if(code == KeyEvent.VK_T){
            checkDrawTime = !checkDrawTime;
        }
    }

    public void pauseState(int code){
        if (code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.playState;
        }
    }
    public void dialogueState(int code){
        if (code == KeyEvent.VK_ENTER){
            gp.gameState = gp.playState;
        }
    }
    public void menuState(int code){
        if (code == KeyEvent.VK_C){
            gp.gameState = gp.playState;
        }
    }


}
