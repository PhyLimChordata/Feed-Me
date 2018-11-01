package com.company;

/* Andy Phy Lim
July 4, 2018
This is the HomeScreen class. Generates the first screen the user will see. The home screen consists of the title of the game: "Feed Me" and three buttons: "Play, Tutorial and Exit".
It also consists of a scrolling background and a piranha that moves in a cosine function. */

import javax.swing.*;
import java.awt.event.*;

class HomeScreen extends Screen {

    private final byte TIMER_TICK_PER_SECOND = 24;
    private final float X_INCREMENT = 0.1f;
    private float x = 0;

    private final byte FISH_SPEED = 8;

    private final short TOP_OF_MOVEMENT = 700, DISTANCE_FROM_TOP_MOVEMENT = 80, SPACING = 200;

    private final short POSITION_OF_TITLE_X = 125, POSITION_OF_TITLE_Y = 150;

    private final short POSITION_OF_PLAY_BUTTON_X = 100, POSITION_OF_TUTORIAL_BUTTON_X = 500, POSITION_OF_EXIT_BUTTON_X = 1100, POSITION_OF_BUTTONS_Y = 400;

    private final short TITLE_WIDTH = 1200, TITLE_HEIGHT = 200;

    private final short PLAY_BUTTON_WIDTH = 350, TUTORIAL_BUTTON_WIDTH = 550, EXIT_BUTTON_WIDTH = 200, BUTTONS_HEIGHT = 125;

    private JButton[] Buttons = new JButton[3];

    // Instance variables are declared along with a bunch of constants that will be used in the following code

    HomeScreen() {
        super();
        // Creates a new JFrame with useful components and methods that this class will use; see Screen Class
        addLabel(new JLabel(), POSITION_OF_TITLE_X, POSITION_OF_TITLE_Y, TITLE_WIDTH, TITLE_HEIGHT, "TitleOfGame.png");
        // Creates the title of the game and adds it in the centre of the home screen; see addLabel method in Screen class
        Timer t = new Timer(TIMER_TICK_PER_SECOND, new ActionListener() {
            // Creates a new timer that executes the code below every 24 milliseconds
            public void actionPerformed(ActionEvent e) {
                x += X_INCREMENT;
                // x increases by 0.1 each time the timer ticks
                Piranha.setLocation(Piranha.getX() + FISH_SPEED, TOP_OF_MOVEMENT + CosineMovement(x, DISTANCE_FROM_TOP_MOVEMENT));
                /* The piranha (JLabel created in the Screen class; see Screen class) ends up moving in a cosine function.
                The numbers in the y component makes it so that the piranha stays in the water and moves up and down; see CosineMovement method in Screen class
                while the numbers in the x component makes it so that the piranha moves to the right. */
                BackgroundMovement();
                // Makes the background move to the left so that it is panning; see BackgroundMovement method in Screen class
                PiranhaReset();
                // Essentially checks if the piranha reaches the end of the screen, if so, then put the piranha back to the left side of the screen; see PiranhaReset method in this class
            }
        });
        // Creates a timer that allows for the background to pan and for the piranha to move in a cosine function
        t.start();
        // Starts the timer, allowing for the code above to work

        for (byte i = 0; i < Buttons.length; i++) {
            // Loops a total of 3 times
            Buttons[i] = new JButton();
            // Creates the JButtons and puts them into the array

            if (i == 0) {
                // When i = 0 in the loop, this will be referring to the first button
                addButton(Buttons[i], POSITION_OF_PLAY_BUTTON_X, POSITION_OF_BUTTONS_Y, PLAY_BUTTON_WIDTH, BUTTONS_HEIGHT, "PLAY.png");
                // Uses the first button in the array: Buttons to create the play button and add it onto the home screen; see addButton method in Screen class
                setButton(i, "HoverPLAY.png", "PLAY.png", PLAY_BUTTON_WIDTH);
                // Creates a mouse listener that allows for an indication of if the user is hovering over the play button; see setButton method in this class
                Buttons[i].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new TheGame();
                        dispose();
                        // When the play button is clicked, get rid of the home screen and generate the game; see TheGame class
                    }
                });
                // Creates an action listener that listens for when the button is clicked
            } else if (i == 1) {
                // When i = 1 in the loop, this will be referring to the second button
                addButton(Buttons[i], POSITION_OF_TUTORIAL_BUTTON_X, POSITION_OF_BUTTONS_Y, TUTORIAL_BUTTON_WIDTH, BUTTONS_HEIGHT, "TUTORIAL.png");
                // Uses the second button in the array: Buttons to create the tutorial button and add it onto the home screen; see addButton method in Screen class
                setButton(i, "HoverTUTORIAL.png", "TUTORIAL.png", TUTORIAL_BUTTON_WIDTH);
                // Creates a mouse listener that allows for an indication of if the user is hovering over the tutorial button; see setButton method in this class
                Buttons[i].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new Tutorial();
                        dispose();
                        //When the tutorial button is clicked, get rid of the home screen and generate the tutorial; see Tutorial class
                    }
                });
                // Creates an action listener that listens for when the button is clicked
            } else if (i == 2) {
                // When i = 2, this will be referring to the third button
                addButton(Buttons[i], POSITION_OF_EXIT_BUTTON_X, POSITION_OF_BUTTONS_Y, EXIT_BUTTON_WIDTH, BUTTONS_HEIGHT, "Exit.png");
                // Uses the third button in the array: Buttons to create the exit button and add it onto the home screen; see addButton method in Screen class
                setButton(i, "HoverEXIT.png", "Exit.png", EXIT_BUTTON_WIDTH);
                // Creates a mouse listener that allows for an indication of if a user is hovering over the exit button; see setButton method in this class
                Buttons[i].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                        //When the exit button is clicked, exit the program
                    }
                });
                // Creates an action listener that listens for when the button is clicked
            }
        }
        //In summary, this loop creates the Buttons' and modifies their properties so that they are displayed on the home screen
    }

    private void setButton(byte i, String hoverfileName, String fileName, short buttonWidth) {
        Buttons[i].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Buttons[i].setIcon((imgResizer(new ImageIcon(getClass().getResource(hoverfileName)), buttonWidth, BUTTONS_HEIGHT)));
                // When the mouse hovers over this button, the button's text will change its image to a golden version of itself; see imgResizer method in Screen class
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Buttons[i].setIcon((imgResizer(new ImageIcon(getClass().getResource(fileName)), buttonWidth, BUTTONS_HEIGHT)));
                // When the mouse hovers away from the button, the button will revert back to its original image (silver); see imgResizer in Screen class
            }
        });
        // Creates a mouse listener that listens for when a mouse enters or exits from a button
    }
    // This method is intended to add a feature that allows the user to know when they hover over the button. This is done by changing the image icon of the buttons

    private void PiranhaReset() {
        if (Piranha.getX() >= SIZE_OF_GUI_X + SPACING)
            //if the piranha's x coordinate reaches the end of the GUI + an additional 200 pixels then
            Piranha.setLocation(-Piranha.getWidth(), TOP_OF_MOVEMENT);
            //Set the piranha's location back to where it started (left side of program) (if statement has an additional +200 which creates a brief period of time before the piranha swims again)
    }
    // This method is intended to have the piranha reset its location back to where it started when it reaches the right side of the screen
}


