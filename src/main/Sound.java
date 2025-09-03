package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {

    Clip clip;
    public enum SoundType{
        MUSIC(0),
        COIN(1),
        POWERUP(2),
        UNLOCK(3),
        FANFARE(4),
        HITMONSTER(5),
        SWING_SWORD(6),
        RECEIVE_DAMAGE(7),
        LEVEL_UP(8),
        CURSOR(9),
        BURNING(10),
        CUT_TREE(11);

        private final int value;
        SoundType(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    URL[] soundURL = new URL[30];

    public Sound(){

        soundURL[0] = getClass().getClassLoader().getResource("sound/BlueBoyAdventure.wav");
        soundURL[1] = getClass().getClassLoader().getResource("sound/coin.wav");
        soundURL[2] = getClass().getClassLoader().getResource("sound/powerup.wav");
        soundURL[3] = getClass().getClassLoader().getResource("sound/unlock.wav");
        soundURL[4] = getClass().getClassLoader().getResource("sound/fanfare.wav");
        soundURL[5] = getClass().getClassLoader().getResource("sound/hitmonster.wav");
        soundURL[6] = getClass().getClassLoader().getResource("sound/swingsword.wav");
        soundURL[7] = getClass().getClassLoader().getResource("sound/receivedamage.wav");
        soundURL[8] = getClass().getClassLoader().getResource("sound/levelup.wav");
        soundURL[9] = getClass().getClassLoader().getResource("sound/cursor.wav");
        soundURL[10] = getClass().getClassLoader().getResource("sound/burning.wav");
        soundURL[11] = getClass().getClassLoader().getResource("sound/cuttree.wav");

    }

    /**
     * Loads an audio file to be played
     * @param soundType The Sound to be played
     */
    public void setFile(SoundType soundType){

        int i = soundType.getValue();
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

        }catch(Exception e){
            throw new RuntimeException();
        }
    }

    /**
     * Plays the loaded audio file
     */
    public void play(){
        clip.start();
    }

    /**
     * Loops the loaded audio file continuously
     */
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Stops the loaded audio file from playing
     */
    public void stop(){
        clip.stop();
    }

}
