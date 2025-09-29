package main;

import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * This class handles all UI elements of the game, including
 * the title screen, player life, pause screen, dialogue screen,
 * menu screen, inventory screen, options screen, game over screen,
 * and transition effects.
 */
public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font arial_40, arial_80B, customFont;
    private final UtilityTool uTool = new UtilityTool();

    // Messages
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public String currentDialogue = "";

    public int commandNumber = 0;

    //Command Number Constants
    public final int COMMAND_FULLSCREEN = 0;
    public final int COMMAND_MUSIC = 1;
    public final int COMMAND_SE = 2;
    public final int COMMAND_CONTROLS = 3;
    public final int COMMAND_QUIT = 4;
    public final int COMMAND_BACK = 5;

    int subState = 0;
    int fadeinCounter = 0;

    // Substate constants
    public static final int OPTIONS_MAIN = 0;
    public static final int OPTIONS_FULL_SCREEN_NOTIFICATION = 1;
    public static final int OPTIONS_CONTROLS = 2;
    public static final int OPTIONS_QUIT = 3;

    public static final int TRADE_SELECT = 0;
    public static final int TRADE_BUY = 1;
    public static final int TRADE_SELL = 2;

    public boolean inspecting = false;

    public int slotCol = 0;
    public int slotRow = 0;

    BufferedImage heartFull, heartHalf, heartBlank, crystalFull, crystalBlank;
    public Entity npc;


    /**
     * Instantiates the UI font, and images
     *
     * @param gp The primary GamePanel element
     */
    public UI(GamePanel gp) {
        this.gp = gp;
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);

        InputStream is = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("fonts/MedodicaRegular.otf"));
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        // Create HUD Objects
        Entity heart = new OBJ_Heart(gp);
        heartFull = heart.image;
        heartHalf = heart.image2;
        heartBlank = heart.image3;

        Entity crystal = new OBJ_ManaCrystal(gp);
        crystalFull = crystal.image;
        crystalBlank = crystal.image2;
    }

    /**
     * Draws the UI throughout the various game states
     *
     * @param g2 The primary Graphics2D object
     */
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(customFont);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);

        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        } else if (gp.gameState == gp.playState) {
            drawPlayerLife();
            drawMessage();
        } else if (gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        } else if (gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        } else if (gp.gameState == gp.menuState) {
            drawMenuScreen();
            drawInventory();
        } else if (gp.gameState == gp.optionsState) {
            drawOptionsScreen();
        } else if (gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        } else if (gp.gameState == gp.transitionState) {
            drawTransition();
        } else if (gp.gameState == gp.tradeState) {
            drawTradeScreen();
        }


    }

    /**
     * Draws the title screen
     */
    public void drawTitleScreen() {

        g2.setColor(new Color(199, 137, 137));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Name
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 96));
        String text = "Unc's Journey";
        int x = getXForCenterText(text);
        int y = gp.tileSize * 3;

        // Shadow
        g2.setColor(Color.black);
        g2.drawString(text, x + 5, y + 5);

        // Main color
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        // Image

        x = gp.screenWidth / 2 - gp.tileSize;
        y += gp.tileSize * 2;

        g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        // Menu
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
        text = "New Game";
        x = getXForCenterText(text);
        y += gp.tileSize * 3;
        g2.drawString(text, x, y);

        if (commandNumber == 0) {
            g2.drawString(">", x - gp.tileSize, y - 4);
        }

        text = "Load Game";
        x = getXForCenterText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);

        if (commandNumber == 1) {
            g2.drawString(">", x - gp.tileSize, y - 4);
        }

        text = "Quit";
        x = getXForCenterText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);

        if (commandNumber == 2) {
            g2.drawString(">", x - gp.tileSize, y - 4);
        }


    }

    /**
     * Draws the pause screen
     */
    public void drawPauseScreen() {

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80));
        String text = "PAUSED";

        int x = getXForCenterText(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    /**
     * Draws the dialogue box and text
     */
    public void drawDialogueScreen() {

        // Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 5);
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }


    }

    /**
     * Draws the players health bar, in the top left of the screen
     */
    public void drawPlayerLife() {

        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        // Draw max life
        while (i < gp.player.maxLife / 2) {
            g2.drawImage(heartBlank, x, y, null);
            i++;
            x += gp.tileSize;
        }

        i = 0;
        x = gp.tileSize / 2;

        // Draw current life
        while (i < gp.player.life) {
            g2.drawImage(heartHalf, x, y, null);
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heartFull, x, y, null);
                i++;
            }
            x += gp.tileSize;
        }

        // Draw max mana
        x = (gp.tileSize / 2) - 5;
        y = (int) (gp.tileSize * 1.5);
        i = 0;
        while (i < gp.player.maxMana) {
            g2.drawImage(crystalBlank, x, y, null);
            i++;
            x += 35;
        }

        x = (gp.tileSize / 2) - 5;
        i = 0;

        while (i < gp.player.mana) {
            g2.drawImage(crystalFull, x, y, null);
            i++;
            x += 35;
        }
    }

    /**
     * Draws the menu screen
     */
    public void drawMenuScreen() {

        // Create a Frame
        final int frameX = gp.tileSize * 2;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Text
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 40;

        // Names
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Mana", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Coin", textX, textY);
        textY += lineHeight + 25;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 25;
        g2.drawString("Shield", textX, textY);

        // Values
        int tailX = (frameX + frameWidth) - 30;

        // Reset textY
        textY = frameY + gp.tileSize;
        String value;

        // Level
        value = String.valueOf(gp.player.level);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Life
        value = gp.player.life + "/" + gp.player.maxLife;
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Mana
        value = gp.player.mana + "/" + gp.player.maxMana;
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Strength
        value = String.valueOf(gp.player.strength);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Dexterity
        value = String.valueOf(gp.player.dexterity);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Attack
        value = String.valueOf(gp.player.attack);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Defense
        value = String.valueOf(gp.player.defense);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Exp
        value = String.valueOf(gp.player.exp);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Next Level
        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Coin
        value = String.valueOf(gp.player.coin);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Weapon
        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 14, null);
        textY += lineHeight;

        //Shield
        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY + 7, null);

        g2.setFont(customFont);

    }

    /**
     * Adds messages to be displayed
     *
     * @param text The message to be added
     */
    public void addMessage(String text) {

        message.add(text);
        messageCounter.add(0);

    }

    /**
     * Draws a message within the dialogue window
     */
    public void drawMessage() {

        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for (int i = 0; i < message.size(); i++) {
            if (message.get(i) != null) {

                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX + 2, messageY + 2);

                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i, counter);
                messageY += 50;

                if (messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                    i--; // Adjust index if list size changes
                }
            }
        }

    }

    /**
     * Draws the inventory screen
     */
    public void drawInventory() {

        int frameX = gp.tileSize * 12;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 3;

        // Draw player items
        for (int i = 0; i < gp.player.inventory.size(); i++) {

            if (gp.player.inventory.get(i) == gp.player.currentWeapon || gp.player.inventory.get(i) == gp.player.currentShield) {
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }

            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);

            slotX += slotSize;
            if (i == 4 | i == 9 | i == 14 | i == 19) {
                slotX = slotXStart;
                slotY += slotSize;
            }
        }

        // Cursor
        int cursorX = slotXStart + (slotSize * slotCol);
        int cursorY = slotYStart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        // Draw cursor
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // Description window
        frameY += frameHeight;
        frameHeight = gp.tileSize * 3;


        // Draw description text
        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(36F));

        int itemIndex = getItemInventoryIndex();
        if (itemIndex < gp.player.inventory.size()) {

            drawSubWindow(frameX, frameY, frameWidth, frameHeight);    // Creates sub window only when an item is selected in inventory
            for (String line : gp.player.inventory.get(itemIndex).description.split("\n")) {

                g2.drawString(line, textX, textY);
                textY += 36;
            }

        }

    }

    /**
     * Gets the index of an item in the inventory relative to the inventory arraylist
     */
    public int getItemInventoryIndex() {

        return slotCol + (slotRow * 5);

    }

    /*
     * Draws the options screen
     */
    public void drawOptionsScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        // Sub window
        int frameX = gp.tileSize * 6;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState) {
            case OPTIONS_MAIN:
                optionsMain(frameX, frameY);
                break;
            case OPTIONS_FULL_SCREEN_NOTIFICATION:
                optionsFullScreenNotification(frameX, frameY);
                break;
            case OPTIONS_CONTROLS:
                optionsControls(frameX, frameY);
                break;
            case OPTIONS_QUIT:
                optionsQuit(frameX, frameY);
                break;
        }

        gp.keyHandler.enterPressed = false;
    }

    /**
     * Draws the primary view of the options menu
     *
     * @param frameX The X coordinate of the options frame
     * @param frameY The Y coordinate of the options frame
     */
    public void optionsMain(int frameX, int frameY) {

        int textX;
        int textY;
        //Title
        String text = "Options";
        textX = getXForCenterText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        // Full Screen On/Off
        textX = frameX + gp.tileSize;
        textY += gp.tileSize * 2;
        g2.drawString("Full Screen", textX, textY);
        if (commandNumber == COMMAND_FULLSCREEN) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                gp.fullScreenOn = !gp.fullScreenOn;
                gp.setFullScreen();
                subState = OPTIONS_FULL_SCREEN_NOTIFICATION;
            }
        }

        //Music
        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if (commandNumber == COMMAND_MUSIC) {
            g2.drawString(">", textX - 25, textY);
        }

        //SE
        textY += gp.tileSize;
        g2.drawString("SE", textX, textY);
        if (commandNumber == COMMAND_SE) {
            g2.drawString(">", textX - 25, textY);
        }

        // Controls
        textY += gp.tileSize;
        g2.drawString("Controls", textX, textY);
        if (commandNumber == COMMAND_CONTROLS) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                commandNumber = 0;
                subState = OPTIONS_CONTROLS;
            }
        }

        // Quit
        textY += gp.tileSize;
        g2.drawString("Quit", textX, textY);
        if (commandNumber == COMMAND_QUIT) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                commandNumber = 0;
                subState = OPTIONS_QUIT;
            }
        }

        // Back
        textY += gp.tileSize * 2;
        g2.drawString("Back", textX, textY);
        if (commandNumber == COMMAND_BACK) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                gp.gameState = gp.playState;
                gp.config.saveConfig();
            }
        }

        // Full screen check box
        textX = frameX + (int) (gp.tileSize * 4.5);
        textY = frameY + gp.tileSize * 2 + gp.tileSize / 2;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY, gp.tileSize / 2, gp.tileSize / 2);
        if (gp.fullScreenOn) {
            g2.fillRect(textX, textY, gp.tileSize / 2, gp.tileSize / 2);
        }

        // Music Volume
        textY += gp.tileSize;
        g2.drawRect(textX, textY, (int) (gp.tileSize * 2.5),gp.tileSize / 2); // 124/24
        int volumeWidth = (int) ((gp.tileSize * 2.5) / 5) * gp.music.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, gp.tileSize / 2);


        // SE Volume
        textY += gp.tileSize;
        g2.drawRect(textX, textY, (int) (gp.tileSize * 2.5), gp.tileSize / 2); // 124/24
        int seWidth = (int) ((gp.tileSize * 2.5) / 5) * gp.SE.volumeScale;
        g2.fillRect(textX, textY, seWidth, gp.tileSize / 2);
    }

    /**
     * A substate of the options menu that draws the notification
     * that full screen changes will take effect after restarting the game
     *
     * @param frameX The X coordinate of the options frame
     * @param frameY The Y coordinate of the options frame
     */
    public void optionsFullScreenNotification(int frameX, int frameY) {

        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        String text = "The change will take effect \nafter restarting";

        for (String line : text.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        // Back
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if (commandNumber == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                subState = 0;
                commandNumber = 0;
            }
        }

    }

    /**
     * A substate of the options menu that displays
     * the controls for the game
     *
     * @param frameX The X coordinate of the options frame
     * @param frameY The Y coordinate of the options frame
     */
    public void optionsControls(int frameX, int frameY) {

        commandNumber = 0;
        int textX;
        int textY;

        String text = "Controls";
        textX = getXForCenterText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        int keyTextX = frameX + gp.tileSize * 6;

        String[][] options = {
                {"Move", "WASD"},
                {"Interact/Attack", "Enter"},
                {"Shoot/Cast", "F"},
                {"Character Screen", "C"},
                {"Pause", "P"},
                {"Options", "ESC"}
        };
        for (String[] optionArr : options) {
            g2.drawString(optionArr[0], textX, textY);
            g2.drawString(optionArr[1], keyTextX, textY);
            textY += gp.tileSize;
        }

        textY += gp.tileSize;

        g2.drawString("Back", textX, textY);
        if (commandNumber == 0) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                subState = OPTIONS_MAIN;
            }
        }


    }

    /**
     * A substate of the options menu that displays
     * a quit confirmation dialogue
     *
     * @param frameX The X coordinate of the options frame
     * @param frameY The Y coordinate of the options frame
     */
    public void optionsQuit(int frameX, int frameY) {

        int COMMAND_QUIT_YES = 0;
        int COMMAND_QUIT_NO = 1;
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;
        String text = "Quit the game and return \nto the title screen?";

        for (String line : text.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += gp.tileSize;
        }

        // YES
        text = "Yes";
        textX = getXForCenterText(text);
        textY += gp.tileSize * 2;
        g2.drawString(text, textX, textY);
        if (commandNumber == COMMAND_QUIT_YES) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                subState = OPTIONS_MAIN;
                gp.gameState = gp.titleState;
                gp.stopMusic();
            }
        }

        text = "No";
        textX = getXForCenterText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if (commandNumber == COMMAND_QUIT_NO) {
            g2.drawString(">", textX - 25, textY);
            if (gp.keyHandler.enterPressed) {
                subState = OPTIONS_MAIN;
                commandNumber = COMMAND_QUIT;
            }
        }


    }

    /**
     * Draws the game over screen
     */
    public void drawGameOverScreen() {

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);


        int x;
        int y;
        String text;

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        text = "Game Over";

        // Text shadow
        g2.setColor(Color.black);
        x = getXForCenterText(text);
        y = gp.tileSize * 4;
        g2.drawString(text, x, y);

        // Main Text
        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);

        // Retry
        g2.setFont(g2.getFont().deriveFont(50F));
        text = "Retry";
        x = getXForCenterText(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if (commandNumber == 0) {
            g2.drawString(">", x - 40, y);
        }

        // Back to title screen
        text = "Quit";
        x = getXForCenterText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNumber == 1) {
            g2.drawString(">", x - 40, y);
        }

    }

    /**
     * Draws the transition screen when moving between maps
     */
    public void drawTransition() {

        fadeinCounter++;
        g2.setColor(new Color(0, 0, 0, fadeinCounter * 5));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        if (fadeinCounter == 50) {
            fadeinCounter = 0;
            gp.gameState = gp.playState;
            gp.currentMap = gp.eventH.tempMap;
            gp.player.worldX = gp.tileSize * gp.eventH.tempCol;
            gp.player.worldY = gp.tileSize * gp.eventH.tempRow;
            gp.eventH.previousEventX = gp.player.worldX;
            gp.eventH.previousEventY = gp.player.worldY;
        }
    }

    /**
     * Draws the trade screen when interacting with an NPC
     */
    public void drawTradeScreen() {

        switch (subState) {
            case 0:
                tradeSelect();
                break;
            case 1:
                tradeBuy();
                break;
            case 2:
                tradeSell();
                break;
        }
        gp.keyHandler.enterPressed = false;
    }

    /**
     * Draws the initial trade screen where the player can select
     * to buy, sell, or leave
     */
    public void tradeSelect() {

        long drawStart = 0;
        if (gp.keyHandler.checkDrawTime){
            //Debug
            drawStart = System.nanoTime();
        }

        drawDialogueScreen();

        //Draw Window
        int x = gp.tileSize * 5;
        int y = gp.tileSize * 5;
        int width = gp.tileSize * 9;
        int height = (int) (gp.tileSize * 3.5);
        drawSubWindow(x, y, width, height);

        // Draw Text
        x = gp.tileSize * 7;
        y = gp.tileSize * 8;
        g2.drawString("Buy", x, y);
        if (commandNumber == 0) {
            g2.drawString(">", x - 25, y);
            if (gp.keyHandler.enterPressed) {
                subState = 1;
                commandNumber = 0;
            }
        }

        x += gp.tileSize * 2;
        g2.drawString("Sell", x, y);
        if (commandNumber == 1) {
            g2.drawString(">", x - 25, y);
            if (gp.keyHandler.enterPressed) {
                subState = 2;
                commandNumber = 0;
            }
        }

        x += gp.tileSize * 2;
        g2.drawString("Leave", x, y);
        if (commandNumber == 2) {
            g2.drawString(">", x - 25, y);
            if (gp.keyHandler.enterPressed) {
                subState = 0;
                commandNumber = 0;
                gp.gameState = gp.dialogueState;
                currentDialogue = "Later, chump.";
            }
        }

        if (gp.keyHandler.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 460);
        }
    }

    /**
     * Draws the buy screen where the player can buy items from the NPC
     */
    public void tradeBuy() {

        long drawStart = 0;
        if (gp.keyHandler.checkDrawTime){
            //Debug
            drawStart = System.nanoTime();
        }

        if (gp.ui.inspecting) {
            // Draw Sub Window
            int x = (int) (gp.tileSize * 5.5);
            int y = gp.tileSize;
            int width = gp.tileSize * 9;
            int height = gp.tileSize * 10;
            drawSubWindow(x, y, width, height);

            // Draw Item Image, and scale it to 2x tile size
            int itemIndex = getItemInventoryIndex();
            BufferedImage image = uTool.scaleImage(gp.ui.npc.inventory.get(itemIndex).down1, gp.tileSize * 2, gp.tileSize * 2);
            int imageX = x + (width / 2) - (image.getWidth() / 2);
            int imageY = y + 10;
            g2.drawImage(image, imageX, imageY, null);
            g2.setFont(g2.getFont().deriveFont(36F));
            g2.drawString("Price: " + gp.ui.npc.inventory.get(itemIndex).price + "g", imageX, imageY + gp.tileSize * 2 + 30);

            int textY = y + gp.tileSize * 4;
            int textX = x + gp.tileSize;
            HashMap<String, Integer> boundaries = getSubWindowBoundaries(x, y, height, width);

            // Draw Item Description
            String description = gp.ui.npc.inventory.get(itemIndex).description;
            int padding = textX - boundaries.get("left");

            description = wrapTextToWidth(g2, description, boundaries.get("right") - textX - padding);
            for (String line : description.split("\n")) {
                g2.drawString(line, textX, textY);
                textY += 36;
            }
            return;
        }

        drawDialogueScreen();

        //Draw Window
        int x = gp.tileSize * 5;
        int y = gp.tileSize * 5;
        int width = gp.tileSize * 9;
        int height = (int) (gp.tileSize * 3.5);
        drawSubWindow(x, y, width, height);

        final int slotXStart = gp.tileSize * 5 + 20;
        final int slotYStart = gp.tileSize * 5 + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 3;

        // Draw items
        for (int i = 0; i < gp.ui.npc.inventory.size(); i++) {

            g2.drawImage(gp.ui.npc.inventory.get(i).down1, slotX, slotY, null);

            slotX += slotSize;
            if (i == 7) {
                slotX = slotXStart;
                slotY += slotSize;
            }
        }

        // Cursor
        int cursorX = slotXStart + (slotSize * slotCol);
        int cursorY = slotYStart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        // Draw cursor
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // Description window
        int frameX = gp.tileSize * (5 + slotCol);
        int frameY = gp.tileSize * (6 + slotRow) + 20;
        int frameWidth = gp.tileSize * 7;
        int frameHeight = gp.tileSize * 4;
        int priceY = frameY + frameHeight - 20;

        // Draw description text
        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(36F));

        int itemIndex = getItemInventoryIndex();
        if (itemIndex < gp.ui.npc.inventory.size()) {

            drawSubWindow(frameX, frameY, frameWidth, frameHeight);    // Creates sub window only when an item is selected in inventory

            HashMap<String, Integer> boundaries = getSubWindowBoundaries(frameX, frameY, frameHeight, frameWidth);
            String description = gp.ui.npc.inventory.get(itemIndex).description;
            int padding = textX - boundaries.get("left");

            description = wrapTextToWidth(g2, description, boundaries.get("right") - textX - padding);
            for (String line : description.split("\n")) {
                if (textY + 36 >= priceY) {
                    line = line + "...";
                    g2.drawString(line, textX, textY);
                    break;
                }
                g2.drawString(line, textX, textY);
                textY += 36;
            }
            g2.drawString("Price: " + gp.ui.npc.inventory.get(itemIndex).price + "g", textX, priceY);
        }

        if (gp.keyHandler.checkDrawTime) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 460);
            g2.drawString("Draw Time: " + passed, 10, 460);
        }
    }

    /**
     * Draws the sell screen where the player can sell items to the NPC
     */
    public void tradeSell() {

    }

    /**
     * Calculates the right wall X coordinate of a sub window
     * @param x     - the X coordinate of the sub window
     * @param width - the width of the sub window
     * @return The X coordinate of the right wall of the sub window
     */
    public HashMap<String, Integer> getSubWindowBoundaries(int x, int y, int length, int width) {
        HashMap<String, Integer> boundaries = new HashMap<>();
        boundaries.put("left", x + 5);
        boundaries.put("top", y + 5);
        boundaries.put("bottom", y + length - 5);
        boundaries.put("right", x + width - 5);

        return boundaries;
    }

    /**
     * Wraps text to a specified pixel width, inserting newline characters as needed.
     * Respects existing newline characters in the input text.
     * @param g2 - the Graphics2D object
     * @param text - the text to be wrapped
     * @param maxWidth - the maximum pixel width
     * @return The wrapped text with newline characters inserted
     */
    public String wrapTextToWidth(Graphics2D g2, String text, int maxWidth) {
        FontMetrics fm = g2.getFontMetrics();
        StringBuilder result = new StringBuilder();

        // Split by paragraphs
        String[] paragraphs = text.split("\n");

        for (int p = 0; p < paragraphs.length; p++) {
            String[] words = paragraphs[p].split(" ");
            StringBuilder line = new StringBuilder();

            for (String word : words) {
                String testLine = line.isEmpty() ? word : line + " " + word;
                int testWidth = fm.stringWidth(testLine);

                if (testWidth > maxWidth) {
                    // Commit current line and start new one
                    result.append(line).append("\n");
                    line = new StringBuilder(word);
                } else {
                    line = new StringBuilder(testLine);
                }
            }

            // Flush remaining words in paragraph
            if (!line.isEmpty()) {
                result.append(line);
            }

            // Add a paragraph break unless it's the last one
            if (p < paragraphs.length - 1) {
                result.append("\n");
            }
        }

        return result.toString();
    }

    /**
     * Draws the primary dialogue box
     */
    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    /**
     * Gets the X coordinates for text that will be centered
     *
     * @param text The text to be displayed
     * @return The X coordinate for the text
     */
    public int getXForCenterText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth / 2 - length / 2;
    }

    /**
     * Calculates the X values of text that will be aligned to the right
     *
     * @param text  The text to be displayed on screen
     * @param tailX end of the dialogue box
     * @return The X coordinate where the text will be displayed
     */
    public int getXForAlignToRightText(String text, int tailX) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return tailX - length;
    }
}
