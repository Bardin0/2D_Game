package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {

    Clip clip;

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
     * @param i The index of the sound file in the soundURL array
     */
    public void setFile(int i){

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
