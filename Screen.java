package com.company;

/* Andy Phy Lim
May 15, 2018
This is the Screen class. Generates the necessary components for multiple classes. Components such as the actual JFrame,
the frame's background, and the piranha.
It also consists of methods that create JLabels and JButtons to be added into the JFrame. */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Screen extends JFrame {

    final short SIZE_OF_GUI_X = 1450, SIZE_OF_GUI_Y = 900;
    final byte TOP_OF_SCREEN = 0, LEFT_OF_SCREEN = 0, BRING_TO_FRONT = 0;

    private JLabel Background = new JLabel(), Background2 = new JLabel();
    private final byte SPEED_OF_BACKGROUND = 2;

    JLabel Piranha = new JLabel(), HighScore = new JLabel(), Score = new JLabel();

    static int points = 0;
    final byte FONT_SIZE = 55;

    final short POSITION_OF_PIRANHA_Y = 650;
    final byte PIRANHA_WIDTH = 100, PIRANHA_HEIGHT = 100;

    // Instance variables are declared along with a bunch of constants that will be used in the following code

    Screen() {
        setLayout(null);
        // Allows freedom of setting anything to anywhere (no structured layout of how components are placed)
        setSize(SIZE_OF_GUI_X, SIZE_OF_GUI_Y);
        // Sets the size of the frame to be 1450 x 900 (fullscreen on my laptop)
        setResizable(false);
        // Prevents user from resizing form
        setDefaultCloseOperation(Screen.EXIT_ON_CLOSE);
        // Allows user to close the program safely
        setUndecorated(true);
        // Removes the top part of the program (prevents user from minimizing, full screen, closing program)
        setFocusable(true);
        // Allows the key listeners to work (sets the focus of the event on the frame)
        addKeyListener(new KeyListener() {
            // Creates a key listener that listens for key events
            public void keyTyped(KeyEvent e) {
            }
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                // The key that the user pressed on will be saved as "code"
                if (code == KeyEvent.VK_ESCAPE)
                    // If the user clicked the esc key then
                    System.exit(0);
                // Exit the program
            }
            public void keyReleased(KeyEvent e) {
            }
        });
        // Creates a key listener that listens if the esc key is pressed on, if it is then the program exits
        addLabel(Background, LEFT_OF_SCREEN, TOP_OF_SCREEN, SIZE_OF_GUI_X, SIZE_OF_GUI_Y, "Ocean.png");
        // Creates a JLabel that will become the background and adds it to the frame; see addLabel method in this class
        addLabel(Background2, SIZE_OF_GUI_X, TOP_OF_SCREEN, SIZE_OF_GUI_X, SIZE_OF_GUI_Y, "Ocean.png");
        // Creates an identical background label. However, this Background is positioned so that it's attached to the first background; allowing for a pan to occur; see actionPerformed in HomeScreen class
        addLabel(Piranha, LEFT_OF_SCREEN, POSITION_OF_PIRANHA_Y, PIRANHA_WIDTH, PIRANHA_HEIGHT, "PiranhaR.png");
        // Creates a JLabel that will represent the piranha and adds it to the frame; see addLabel method
    }

    void addLabel(JLabel label, int positionx, int positiony, short width, short height) {
        label.setBounds(positionx, positiony, width, height);
        // Sets the position of the label provided. It also sets it's dimensions. (to whatever is provided)
        label.setVisible(false);
        // Typically this method will be used for hit boxes and so the label should be invisible
        add(label, BRING_TO_FRONT);
        // Add the JLabel to the JFrame
    }
    /* This method is intended to set the properties of a JLabel (position, dimensions, and visibility) and add it to the JFrame
       This method is mainly for creating hit boxes (doesn't require an image icon or visibility to be true) */

    void addLabel(JLabel label, int positionx, int positiony, short width, short height, String imagefile) {
        label.setBounds(positionx, positiony, width, height);
        // Sets the position of the label provided. It also sets it's dimensions. (to whatever is provided)
        label.setIcon(imgResizer(new ImageIcon(getClass().getResource(imagefile)), width, height));
        // Sets the image of the label; see imgResizer method in this class
        label.setOpaque(false);
        // Sets the image to be transparent (most of the image's background are transparent and therefore this statement is important)
        add(label, BRING_TO_FRONT);
        // Add the label to the JFrame
    }
    /* This method is intended to set the properties of a JLabel (position, dimensions, ImageIcon, and opaqueness) and add it to the JFrame
       This method is mainly for creating the images found in the GUI (is visible and requires a String to be converted into an Image Icon) */

    void addLabel(JLabel label, short positionx, short positiony, short width, short height, String text, Font font) {
        label.setBounds(positionx, positiony, width, height);
        // Sets the position of the label provided. It also sets it's dimensions. (to whatever is provided)
        label.setText(text + points);
        // Sets the text of the label to be whatever text is and the value of point which will be 0 in the cases of when this is called
        label.setForeground(Color.WHITE);
        // Sets the foreground of the label to white (sets the text colour to be white)
        label.setFont(font);
        // Sets the font of the text to Impact, Bolds the text and changes the font size to 20
        label.setVisible(true);
        // Sets the Score label to become visible
        add(label, BRING_TO_FRONT);
        // Add the JLabel to the JFrame
    }
    // This method is intended to add a label that tells the user how many points they have. The properties of it are set. Properties such as foreground, text, font, and visibility

    void addButton(JButton button, short positionx, short positiony, short width, short height, String imagefile) {
        button.setBounds(positionx, positiony, width, height);
        // Sets the position of the button provided. It also sets it's dimensions. (to whatever is provided)
        button.setIcon(imgResizer(new ImageIcon(getClass().getResource(imagefile)), width, height));
        // Sets the image of the button; see imgResizer method in this class
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        // Gets rid of the button's white Background (Makes the image in the button transparent)
        add(button, BRING_TO_FRONT);
        // Add the button to the JFrame
    }
    // This method is intended to set the properties of a JButton (position, dimensions, ImageIcon, and opaqueness) and adds them to the JFrame

    ImageIcon imgResizer(ImageIcon image, short width, short height) {
        Image Image = image.getImage();
        // Saves the image provided in the ImageIcon provided as tempImg
        return new ImageIcon(Image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        // Returns the now resized image (in the form of ImageIcon): Resize the image by using the image and the command getScaledInstance, then converts it back into an ImageIcon to be saved as ResizedImg
    }
    // This method is intended to resize images and return them

    void BackgroundMovement() {
        Background.setLocation(Background.getX() - SPEED_OF_BACKGROUND, TOP_OF_SCREEN);
        // The background ends up moving to the left by subtracting SPEED_OF_BACKGROUND (2) to the background's x coordinate, making the water and clouds seem to move
        Background2.setLocation(Background2.getX() - SPEED_OF_BACKGROUND, TOP_OF_SCREEN);
        // This makes the second background move along with the first background (the images match together so it looks as if the screen is panning)

        if (Background.getX() + Background.getWidth() == LEFT_OF_SCREEN)
            // If the background goes off screen (through the left screen) (including the width) then
            Background.setLocation(SIZE_OF_GUI_X, TOP_OF_SCREEN);
            // Set the Background's location back to the right side of the program
        else if (Background2.getX() + Background2.getWidth() == LEFT_OF_SCREEN)
            // However, if the second Background goes off screen then
            Background2.setLocation(SIZE_OF_GUI_X, TOP_OF_SCREEN);
            // Set the second Background's location back to the right side of the program
    }
    // This method is intended to move the background

    int CosineMovement(double x, short VariableDistance){
        return (short)(Math.cos(x) * VariableDistance);
        // Returns this math statement; will create multiple numbers as x changes; Usually will be paired with a timer which will cause an up and down motion among the pelicans and the piranha in the home screen
    }
    // This method is intended to return a value involving the cosine function

    void setPoints(int point) {
        points = point;
    }
    // Sets the points to a value provided
}
