package com.company;

/* Andy Phy Lim
July 4, 2018
This is the Main class; where the program starts. It contains the main method which will generate the home screen. */

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

class Main {

    static int BREAK = 1000;

    public static void main(String[] args) {
        HomeScreen HS = new HomeScreen();
        // Generates a new HomeScreen object; see HomeScreen class
        HS.setVisible(true);
        // Sets the home screen visible to the user
        File RelaxingMusic = new File(".RelaxingMusic.wav.icloud");
        // Creates a new file that stores the music file
        PlayMusic(RelaxingMusic);
        PlayMusic(RelaxingMusic);
        PlayMusic(RelaxingMusic);
        PlayMusic(RelaxingMusic);
        PlayMusic(RelaxingMusic);
        PlayMusic(RelaxingMusic);
        // Plays the audio file (multiple lines are needed to keep the music going for at least 10 minutes); see PlayMusic method in this class
    }
    public static void PlayMusic(File music){
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(music));
            clip.start();
            // Plays the audio

            Thread.sleep(clip.getMicrosecondLength()/BREAK);
            // Necessary to ensure the audio clip plays smoothly
        }
        catch(Exception e){
        }
    }
}
