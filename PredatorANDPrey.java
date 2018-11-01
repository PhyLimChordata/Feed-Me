package com.company;

/* Andy Phy Lim
June 4, 2018
This is the PredatorANDPrey class. Generates many labels, sets their properties and stores them into arrays for later use. */

import javax.swing.*;
import java.awt.*;

class PredatorANDPrey extends Piranha {

    final byte LIMIT = 15;

    private final byte FISH_WIDTH = 50, FISH_HEIGHT = 20;
    private final byte SEAGULL_WIDTH = 75, SEAGULL_HEIGHT = 75, SEAGULL_HIT_BOX_WIDTH = 50, SEAGULL_HIT_BOX_HEIGHT = 50;
    private final short EEL_WIDTH = 300, EEL_HEIGHT = 80, EEL_HIT_BOX_WIDTH = 65, EEL_HIT_BOX_HEIGHT = 57;
    private final short PUFFERFISH_WIDTH = 125, PUFFERFISH_HEIGHT = 125, PUFFERFISH_HIT_BOX_WIDTH = 125, PUFFERFISH_HIT_BOX_HEIGHT = 125;
    private final short PELICAN_WIDTH = 250, PELICAN_HEIGHT = 200, PELICAN_HIT_BOX_WIDTH = 100, PELICAN_HIT_BOX_HEIGHT = 110;

    JLabel[] Fish = new JLabel[LIMIT], Seagull = new JLabel[LIMIT], Eel = new JLabel[LIMIT], Pufferfish = new JLabel[LIMIT], Pelican = new JLabel[LIMIT], Pelican_Bot = new JLabel[LIMIT];
    JLabel[] Fish_Hit_Box = new JLabel[LIMIT], Seagulls_Hit_Box = new JLabel[LIMIT], Eel_Hit_Box = new JLabel[LIMIT], PufferFish_Hit_Box = new JLabel[LIMIT], Pelicans_Hit_Box = new JLabel[LIMIT], Pelican_Bot_Hit_Box = new JLabel[LIMIT];

    final int SOME_DISTANCE_FROM_RIGHT_OF_SCREEN = 1500, SOME_DISTANCE_FROM_TOP_OF_SCREEN = 150, EXTENDED_DISTANCE_FROM_RIGHT_OF_SCREEN = 5500;

    final byte EEL_ADJUSTMENT_X = 8, EEL_ADJUSTMENT_Y = 17, PUFFERFISH_ADJUSTMENT = 12;
    final short FLYING_ADJUSTMENT = 18, PELICANS_ADJUSTMENT = 60, SPACE_FROM_OCEAN_LEVEL = 225, PELICANS_STARTING_POSITION = 200;

    float Fish_Speed = 8f, Seagull_Speed = 16f, Eel_Speed = 18f, Pufferfish_Speed = 6f, Pelican_Speed = 10f;
    final byte DEFAULT_FISH_SPEED = 8, DEFAULT_SEAGULL_SPEED = 16, DEFAULT_EEL_SPEED = 18, DEFAULT_PUFFERFISH_SPEED = 6, DEFAULT_PELICAN_SPEED = 10;

    // Instance variables are declared along with a bunch of constants that will be used in the following code

    PredatorANDPrey() {
        super();
        // Necessary to access the addLabel method and to allow other classes (TheGame and Tutorial) to access Piranha and Screen class
        for (byte i = 0; i < LIMIT; i++) {
            // Loops 15 times
            addLabel(Fish[i] = new JLabel(), RandomizerSetBackX(SOME_DISTANCE_FROM_RIGHT_OF_SCREEN), RandomizerSetBackY(OCEAN_LEVEL + FISH_HEIGHT, false), FISH_WIDTH, FISH_HEIGHT, "Fish.png");
            /* The ith position in the array is filled with a new JLabel
               Set its x position a random distance away from the right side of the screen (0 - 1500); see RandomizerSetBackX in this class
               Set its y position a random place under sea level; see RandomizerSetBackY in this class
               Set its dimensions and add an image to the JLabel
               See addLabel method in Screen class */
            addLabel(Fish_Hit_Box[i] = new JLabel(), (short)Fish[i].getX(), (short)Fish[i].getY(), FISH_WIDTH, FISH_HEIGHT);
            // This will create a hitbox that will follow the x and y coordinates of its designated label ex) Fish[0] will have Fish_Hit_Box[0]; see addLabel method in Screen class

            // The rest is similar code

            addLabel(Eel[i] = new JLabel(), RandomizerSetBackX(EXTENDED_DISTANCE_FROM_RIGHT_OF_SCREEN), RandomizerSetBackY(OCEAN_LEVEL + EEL_HEIGHT, false), EEL_WIDTH, EEL_HEIGHT, "Eel.png");
            addLabel(Eel_Hit_Box[i] = new JLabel(), Eel[i].getX() + EEL_ADJUSTMENT_X, Eel[i].getX() + EEL_ADJUSTMENT_Y, EEL_HIT_BOX_WIDTH, EEL_HIT_BOX_HEIGHT);
            // The introduction of the adjustments makes it so that the hit box is slightly shifted according to the image, making the hit box more fair to the user (This applies to the other code below as well)

            addLabel(Pufferfish[i] = new JLabel(), RandomizerSetBackX(SOME_DISTANCE_FROM_RIGHT_OF_SCREEN), RandomizerSetBackY(OCEAN_LEVEL + PUFFERFISH_HEIGHT, false), PUFFERFISH_WIDTH, PUFFERFISH_HEIGHT, "PufferfishL.png");
            addLabel(PufferFish_Hit_Box[i] = new JLabel(), Pufferfish[i].getX() + PUFFERFISH_ADJUSTMENT, Pufferfish[i].getY() + PUFFERFISH_ADJUSTMENT, PUFFERFISH_HIT_BOX_WIDTH, PUFFERFISH_HIT_BOX_HEIGHT);

            addLabel(Pelican[i] = new JLabel(), RandomizerSetBackX(SOME_DISTANCE_FROM_RIGHT_OF_SCREEN), TOP_OF_SCREEN, PELICAN_WIDTH, PELICAN_HEIGHT, "PelicanL.png");
            addLabel(Pelicans_Hit_Box[i] = new JLabel(), Pelican[i].getX() + FLYING_ADJUSTMENT, Pelican[i].getY() + PELICANS_ADJUSTMENT, PELICAN_HIT_BOX_WIDTH, PELICAN_HIT_BOX_HEIGHT);
            // The pelicans and seagulls are set to a y position that is above sea level; These pelicans start from the top of the screen

            addLabel(Pelican_Bot[i] = new JLabel(), RandomizerSetBackX(SOME_DISTANCE_FROM_RIGHT_OF_SCREEN), PELICANS_STARTING_POSITION, PELICAN_WIDTH, PELICAN_HEIGHT, "PelicanL.png");
            addLabel(Pelican_Bot_Hit_Box[i] = new JLabel(), Pelican_Bot[i].getX() + FLYING_ADJUSTMENT, Pelican_Bot[i].getY() + PELICANS_ADJUSTMENT, PELICAN_HIT_BOX_WIDTH, PELICAN_HIT_BOX_HEIGHT);
            // These pelicans start from near the surface of the water

            addLabel(Seagull[i] = new JLabel(), RandomizerSetBackX(EXTENDED_DISTANCE_FROM_RIGHT_OF_SCREEN), RandomizerSetBackY(OCEAN_LEVEL, true), SEAGULL_WIDTH, SEAGULL_HEIGHT, "SeagullL.png");
            addLabel(Seagulls_Hit_Box[i] = new JLabel(), Seagull[i].getX() + FLYING_ADJUSTMENT, Seagull[i].getY(), SEAGULL_HIT_BOX_WIDTH, SEAGULL_HIT_BOX_HEIGHT);

            // CheckHitboxes(i, Fish_Hit_Box);
            // CheckHitboxes(i, Seagulls_Hit_Box);
            // CheckHitboxes(i, Eel_Hit_Box);
            // CheckHitboxes(i, PufferFish_Hit_Box);
            // CheckHitboxes(i, Pelicans_Hit_Box);
            // CheckHitboxes(i, Pelican_Bot_Hit_Box);

            // If you wish to check the hit boxes you may uncomment this chunk of code ^
        }
        // In summary, this loop creates a lot of labels with the addLabel method in Screen class and stores them into their designated arrays. Their location is set off screen (right of screen)
    }

    private void CheckHitboxes(byte i, JLabel[] x) {
        x[i].setVisible(true);
        x[i].setOpaque(true);
        x[i].setBackground(Color.BLACK);
    }
    // This method is intended to check the hit boxes by setting them to be visible to the user as a black box

    public int RandomizerSetBackX(int x) {
        return (int)(SIZE_OF_GUI_X + (Math.random() * x));
        // Returns a random distance from the right side of the screen
    }
    // This method returns a value that is randomized that will send a predator/prey back to the right of the screen

    public int RandomizerSetBackY(int y, boolean flying) {
        if (flying) {
            return (int) (SOME_DISTANCE_FROM_TOP_OF_SCREEN + (Math.random() * (OCEAN_LEVEL - SPACE_FROM_OCEAN_LEVEL)));
            // if flying is true (usually will be because it is representing a pelican or a seagull) then return a random y value that indicates the predator/prey will be placed in the sky
        }
        return (int) (OCEAN_LEVEL + (Math.random() * (SIZE_OF_GUI_Y - y)));
        // else return a random y value that indicates the predator/prey will be placed in the ocean
    }
    // This method returns a value that is randomized that will set a predator/prey's y location
}
