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

    /**
     * Invoked when a key has been typed.
     * Not used but must be implemented as part of KeyListener interface.
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Invoked when a key has been pressed.
     * Sends the key code to the appropriate game state handler.
     * @param e the event to be processed
     */
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

       else if (gp.gameState == gp.optionsState){
           optionsState(code);
        }

    }

    /**
     * Invoked when a key has been released.
     * Updates the state of movement and action keys to false.
     * @param e the event to be processed
     */
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
                gp.playMusic(Sound.SoundType.MUSIC);
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
            case KeyEvent.VK_ESCAPE:
                gp.gameState = gp.optionsState;
                break;
            case KeyEvent.VK_T:
                checkDrawTime = !checkDrawTime;
                break;
        }

    }

    /**
     * Handles key inputs when in the pause state
     * @param code The key pressed
     */
    public void pauseState(int code){
        if (code == KeyEvent.VK_P){
            gp.gameState = gp.playState;
            gp.playMusic(Sound.SoundType.MUSIC);
        }
        if (code == KeyEvent.VK_ENTER){
            System.exit(0);
        }
    }

    /**
     * Handles key inputs while in the dialogue state
     * @param code The key pressed
     */
    public void dialogueState(int code){
        if (code == KeyEvent.VK_ENTER){
            gp.gameState = gp.playState;
        }
    }

    /**
     * Handles key inputs in the menu state
     * @param code The key pressed
     */
    public void menuState(int code){
        if (code == KeyEvent.VK_C){
            gp.gameState = gp.playState;
        }

        if (code == KeyEvent.VK_W){
            if (gp.ui.slotRow != 0){
                gp.ui.slotRow--;
                gp.playSE(Sound.SoundType.CURSOR);
            }

        }
        if (code == KeyEvent.VK_A){
            if (gp.ui.slotCol != 0){
                gp.ui.slotCol--;
                gp.playSE(Sound.SoundType.CURSOR);
            }

        }
        if (code == KeyEvent.VK_S){
            if (gp.ui.slotRow != 3){
                gp.ui.slotRow++;
                gp.playSE(Sound.SoundType.CURSOR);
            }

        }
        if (code == KeyEvent.VK_D){
            if (gp.ui.slotCol != 4){
                gp.ui.slotCol++;
                gp.playSE(Sound.SoundType.CURSOR);
            }
        }
        if (code == KeyEvent.VK_ENTER){
            gp.player.selectItem();
        }
    }

    /**
     * Handles key inputs in the options stage
     * @param code The key pressed
     */
    public void optionsState(int code){

        int maxCommandNumber = switch (gp.ui.subState) {
            case UI.OPTIONS_MAIN -> 5;
            case UI.OPTIONS_QUIT -> 1;
            default -> 0;
        };

        switch (code){

            case KeyEvent.VK_ESCAPE:
                gp.gameState = gp.playState;
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = true;
                break;

            case KeyEvent.VK_W:
                gp.ui.commandNumber--;
                if (gp.ui.commandNumber < 0){
                    gp.ui.commandNumber = maxCommandNumber;
                }
                gp.playSE(Sound.SoundType.CURSOR);
                break;

            case KeyEvent.VK_S:
                gp.ui.commandNumber++;
                if (gp.ui.commandNumber > maxCommandNumber){
                    gp.ui.commandNumber = 0;
                }
                gp.playSE(Sound.SoundType.CURSOR);
                break;

            case KeyEvent.VK_A:
                // Check if we are in the options main screen
                if (gp.ui.subState == UI.OPTIONS_MAIN){
                    // Check if the music volume can be lowered
                    if (gp.ui.commandNumber == gp.ui.COMMAND_MUSIC && gp.music.volumeScale > 0){
                        gp.music.volumeScale--;
                        gp.music.setVolume();
                        gp.playSE(Sound.SoundType.CURSOR);
                    }

                    // Check if the sound effects volume can be lowered
                    if (gp.ui.commandNumber == gp.ui.COMMAND_SE && gp.SE.volumeScale > 0){
                        gp.SE.volumeScale--;
                        gp.playSE(Sound.SoundType.CURSOR);
                    }
                }
                break;

            case KeyEvent.VK_D:
                // Check if we are in the options main screen
                if (gp.ui.subState == UI.OPTIONS_MAIN){
                    // Check if the music volume can be increased
                    if (gp.ui.commandNumber == 1 && gp.music.volumeScale < 5){
                        gp.music.volumeScale++;
                        gp.music.setVolume();
                        gp.playSE(Sound.SoundType.CURSOR);
                    }

                    // Check if the sound effect volume can be increased
                    if (gp.ui.commandNumber == gp.ui.COMMAND_SE && gp.SE.volumeScale < 5){
                        gp.SE.volumeScale++;
                        gp.playSE(Sound.SoundType.CURSOR);
                    }
                }
        }

    }

}
