package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shootKeyPressed;
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
        if (code == KeyEvent.VK_F){
            shootKeyPressed = false;
        }
    }

    /**
     * Handler for when in the title game state
     * @param code the key code given by key pressed by user.
     */
    public void titleState(int code){

        // Move cursor up
        if (code == KeyEvent.VK_W){
            gp.ui.commandNumber--;
            // If it cant move up, start from bottom
            if (gp.ui.commandNumber < 0){
                gp.ui.commandNumber = 2;
            }
        }

        // Move cursor down
        if (code == KeyEvent.VK_S){
            gp.ui.commandNumber++;
            // If it cant move down, start from top
            if (gp.ui.commandNumber > 2){
                gp.ui.commandNumber = 0;
            }
        }

        // Select option
        if (code == KeyEvent.VK_ENTER){
            // Start Game
            if (gp.ui.commandNumber == 0){
                gp.gameState = gp.playState;
                gp.playMusic(0);
            }
            // Load game
            else if (gp.ui.commandNumber == 1){
                //add later
            }
            // Quit
            else if (gp.ui.commandNumber == 2){
                System.exit(0);
            }
        }
    }

    /**
     * Handles inputs while in play state
     * @param code The key code given by user key press
     */
    public void playState(int code){
        switch (code){
            case KeyEvent.VK_W:
                upPressed = true;
                break;
            case KeyEvent.VK_A:
                leftPressed = true;
                break;
            case KeyEvent.VK_S:
                downPressed = true;
                break;
            case KeyEvent.VK_D:
                rightPressed = true;
                break;
            case KeyEvent.VK_F:
                shootKeyPressed = true;
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = true;
                break;
            case KeyEvent.VK_P:
                gp.gameState = gp.pauseState;
                gp.stopMusic();
                break;
            case KeyEvent.VK_C:
                gp.gameState = gp.menuState;
                break;
            case KeyEvent.VK_T:
                checkDrawTime = !checkDrawTime;
                break;
        }

    }

    public void pauseState(int code){
        if (code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.playState;
            gp.playMusic(0);
        }
        if (code == KeyEvent.VK_ENTER){
            System.exit(0);
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

        if (code == KeyEvent.VK_W){
            if (gp.ui.slotRow != 0){
                gp.ui.slotRow--;
                gp.playSE(9);
            }

        }
        if (code == KeyEvent.VK_A){
            if (gp.ui.slotCol != 0){
                gp.ui.slotCol--;
                gp.playSE(9);
            }

        }
        if (code == KeyEvent.VK_S){
            if (gp.ui.slotRow != 3){
                gp.ui.slotRow++;
                gp.playSE(9);
            }

        }
        if (code == KeyEvent.VK_D){
            if (gp.ui.slotCol != 4){
                gp.ui.slotCol++;
                gp.playSE(9);
            }
        }
        if (code == KeyEvent.VK_ENTER){
            gp.player.selectItem();
        }
    }

}
