package com.company;

/* Andy Phy Lim
May 15, 2018
This is the Piranha class. It is in charge of moving the Piranha based on the location of the mouse. The Piranha can jump when above the water level, and swim when below the water level.
It is also responsible for the upgrading managements */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Piranha extends Screen {

    private final byte POSITION_X = 25;
    final byte POSITION_Y = 120;

    final byte DEFAULT_JUMP = 5, DEFAULT_SPEED = 3, DEFAULT_HUNGER_GAINED = 35;
    final float DEFAULT_DELAY = 0.05f;

    private final byte SLIGHTLY_DOWN = 54, SLIGHTLY_DOWN_2 = 50;
    final byte SPACING = 20;
    final short OCEAN_LEVEL = 550;
    private final byte HIT_BOX_ADJUSTMENT = 17;

    private final short HALF_OF_SCREEN = SIZE_OF_GUI_X / 2, HUNGER_POSITION_X = 67, HUNGER_BAR_HEIGHT = 60, HUNGER_WIDTH = 635;

    private final byte SPACING_FROM_ICON = 5, SIZE_OF_UPGRADE_ICON = 35, SIZE_OF_ICON = 50, SPACING_FOR_LEVELS = 68;

    final int PIRANHA_SIZE_X_DIVIDE_2 = Piranha.getWidth() / 2, PIRANHA_SIZE_X_DIVIDE_3 = Piranha.getWidth() / 3, PIRANHA_SIZE_Y_DIVIDE_2 = Piranha.getHeight() / 2, PIRANHA_SIZE_Y_DIVIDE_4 = Piranha.getHeight() / 4;

    private static final byte PIRANHA_HIT_BOX_WIDTH = 50, PIRANHA_HIT_BOX_HEIGHT = 60, PIRANHA_EATING_HIT_BOX_WIDTH = 50, PIRANHA_EATING_HIT_BOX_HEIGHT = 50;

    byte Bite_level, Fin_level, Speed_level, Stomach_level, LevelUP;

    private final byte No_Levels = 0, Level_Limit = 5, Base_Hunger_Increase = 10, Increasing_Hunger = 3;

    short Hunger_gained = DEFAULT_HUNGER_GAINED;

    private final byte BASE_JUMP_INCREASE = 9;
    private final float INCREASING_JUMP = 2.2f;
    static byte Jump, Jump_Reset;

    private final byte BASE_SPEED = 1;

    byte Speed = DEFAULT_SPEED, Buffer = (byte)(Speed + 3);

    private final float BASE_DELAY = 0.5f, DIMINISHING_DELAY = 0.1f;
    float End_of_Delay = DEFAULT_DELAY;

    boolean Alive = true;

    private ImageIcon PiranhaR = imgResizer(new ImageIcon(getClass().getResource("PiranhaR.png")), PIRANHA_WIDTH, PIRANHA_HEIGHT);
    private ImageIcon PiranhaL = imgResizer(new ImageIcon(getClass().getResource("PiranhaL.png")), PIRANHA_WIDTH, PIRANHA_HEIGHT);

    JLabel Hunger, Hunger_Bar, Bites, Fin, Tail, Stomach, Piranha_Eating_Hit_Box, Piranha_Hit_Box;

    static JLabel[] Levels_Below_The_Upgrades = new JLabel[4];
    static ImageIcon[] levels = new ImageIcon[6];

    private final byte TIMER_TICK_PER_SECOND = 15;

    // Instance variables are declared along with a bunch of constants that will be used in the following code

    public Piranha() {
        super();
        // Necessary to allow access to the addLabel method and to allow other classes to have access to the Screen class; see Screen Class
        setJump(DEFAULT_JUMP);
        // Sets the jump to 5 (default value)
        Jump_Reset = getJump();
        // Sets Jump_Reset to the value of Jump (should be 5)
        addLabel(Piranha_Eating_Hit_Box = new JLabel(), Piranha.getX() + PIRANHA_SIZE_X_DIVIDE_2, Piranha.getY() + PIRANHA_SIZE_Y_DIVIDE_2, PIRANHA_EATING_HIT_BOX_WIDTH, PIRANHA_EATING_HIT_BOX_HEIGHT);
        addLabel(Piranha_Hit_Box = new JLabel(), Piranha.getX() + PIRANHA_SIZE_X_DIVIDE_3, Piranha.getY() + PIRANHA_SIZE_Y_DIVIDE_4, PIRANHA_HIT_BOX_WIDTH, PIRANHA_HIT_BOX_HEIGHT);
        /* Creates the piranha's hit boxes and adds it to the frame; see addLabel method in Screen class
         The math basically makes it so that the hit boxes are adjusted fairly (The eating hit hox is moved to the mouth of the piranha, and the hit box is moving to the overall body of the piranha) */

//         CheckHitBoxes(Piranha_Eating_Hit_Box, Color.BLACK);
//         CheckHitBoxes(Piranha_Hit_Box, Color.WHITE);
        // If you wish to check the hit boxes you may uncomment this code ^

        setVisible(true);
        //Sets the new screen to be visible to the user (especially true in the case when play or tutorial is clicked)
        addLabel(Hunger = new JLabel(), HUNGER_POSITION_X, SLIGHTLY_DOWN, HUNGER_WIDTH, SIZE_OF_ICON, "Hunger.png");
        addLabel(Hunger_Bar = new JLabel(), POSITION_X, SLIGHTLY_DOWN_2, HALF_OF_SCREEN, HUNGER_BAR_HEIGHT, "HungerBar.png");
        addLabel(Bites = new JLabel(), POSITION_X, POSITION_Y, SIZE_OF_ICON, SIZE_OF_ICON, "Bite.png");
        addLabel(Fin = new JLabel(), Bites.getX() + Bites.getWidth() + SPACING, POSITION_Y, SIZE_OF_ICON, SIZE_OF_ICON, "Fin.png");
        addLabel(Tail = new JLabel(), Fin.getX() + Fin.getWidth() + SPACING, POSITION_Y, SIZE_OF_ICON, SIZE_OF_ICON, "Tail.png");
        addLabel(Stomach = new JLabel(), Tail.getX() + Tail.getWidth() + SPACING, POSITION_Y, SIZE_OF_ICON, SIZE_OF_ICON, "Stomach.png");
        // Creates the Hunger bar and the upgrades section in the top left corner and adds it to the frame; see addLabel method in Screen class. Math makes it so that the upgrades are side by side with each other

        for (int i = 0; i < Levels_Below_The_Upgrades.length; i++) {
            // Loops 5 times
            addLabel(Levels_Below_The_Upgrades[i] = new JLabel(), SIZE_OF_UPGRADE_ICON + SPACING_FOR_LEVELS * i, Bites.getY() + Bites.getHeight() + SPACING_FROM_ICON, SIZE_OF_UPGRADE_ICON, SIZE_OF_UPGRADE_ICON, "Level0.png");
            // Creates new labels that will show the user how many levels they have in an upgrade. Math makes it so that it is spaced out correctly and placed below their designated upgrades
        }
        // This loop creates the levels below the upgrade

        for (int i = 0; i < levels.length; i++){
            levels[i] = imgResizer(new ImageIcon(getClass().getResource("Level" + Integer.toString(i) + ".png")), SIZE_OF_UPGRADE_ICON, SIZE_OF_UPGRADE_ICON);
            // The levels array is filled with Image Icons. They will be used later to change the icons of the labels in the array: Levels_Below_The_Upgrades. Lvl0 is at levels[0], Lvl1 is at levels[1] and so on...
        }
        // This loop creates an array of Image Icons with images showing increasing levels (Level 0: 5 empty bars, Level 1: 4 empty bars, 1 filled, etc.) If confused, see images in src folder named Level0.png/Level1.png/etc.

        addKeyListener(new KeyListener() {
            // Adds a key listener to the frame that listens for any key events
            public void keyTyped(KeyEvent e) {
            }
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                //The key that the user pressed on will be saved as "code"
                if (code == KeyEvent.VK_Q && Bite_level < Level_Limit && LevelUP > No_Levels) {
                    // If Q is clicked and the Bite Upgrade can be upgraded (max level is 5) and the user has a Level ready to be used then
                    Hunger_gained += Base_Hunger_Increase + Increasing_Hunger * Bite_level;
                    // Increase the value of Hunger_gained; Math makes it so that you get more out of every time you skill this upgrade
                    LevelUP--;
                    // Decrease LevelUP by 1 (representing the user using a level)
                    Bite_level++;
                    // Increase Bite Level by 1
                    Levels_Below_The_Upgrades[0].setIcon(levels[Bite_level]);
                    // Change the BiteLevels according to how many levels are put into the Bite Upgrade (Lvl1 Bite results in 1 bar); see GUI for reference
                }
                //The below code is similar so refer to these comments
                if (code == KeyEvent.VK_W && Fin_level < Level_Limit && LevelUP > No_Levels) {
                    Jump += BASE_JUMP_INCREASE - INCREASING_JUMP * Fin_level;
                    // Math makes it so that you get less out of every time you skill this upgrade (Makes it so that you don't jump off screen)
                    Jump_Reset += BASE_JUMP_INCREASE - INCREASING_JUMP * Fin_level;
                    LevelUP--;
                    Fin_level++;
                    Levels_Below_The_Upgrades[1].setIcon(levels[Fin_level]);
                }
                if (code == KeyEvent.VK_E && Speed_level < Level_Limit && LevelUP > No_Levels) {
                    Speed += BASE_SPEED;
                    Buffer = (byte)(Speed + 3);
                    LevelUP--;
                    Speed_level++;
                    Levels_Below_The_Upgrades[2].setIcon(levels[Speed_level]);
                }
                if (code == KeyEvent.VK_R && Stomach_level < Level_Limit && LevelUP > No_Levels) {
                    End_of_Delay += BASE_DELAY - DIMINISHING_DELAY * (double) Stomach_level;
                    // Math makes it so that you get less out of every time you skill this upgrade (discouraging players from skilling all their points into this)
                    LevelUP--;
                    Stomach_level++;
                    Levels_Below_The_Upgrades[3].setIcon(levels[Stomach_level]);
                }
            }
            // Whenever Q, W, E, or R is pressed, an upgrade may be provided
            public void keyReleased(KeyEvent e) {
            }
        });
        // Adds a key listener that will listen for any key events. The keys will be associated to the upgrades the user can level up
        MouseMotionListener MS = new MouseMotionListener() {
            // Creates a mouse motion listener that listens for mouse motion events
            public void mouseDragged(MouseEvent e) {
                // When the mouse is clicked and being dragged
                double PiranhaX = Piranha.getX() + PIRANHA_SIZE_X_DIVIDE_2;
                double PiranhaY = Piranha.getY() + PIRANHA_SIZE_Y_DIVIDE_2;
                // This acts as an indicator for where the mouse is (This variable will be compared to the mouse's location) The variable takes the Piranha's coordinates and accounts for it's size (The indicator is in the center of the label)
                double MousePositionX = e.getX();
                double MousePositionY = e.getY();
                // These variables save the position (x coordinate and y coordinate) of the mouse
                if (Alive == true) {
                    // If the piranha is alive then
                    mouseMove(MousePositionX, MousePositionY, PiranhaX, PiranhaY);
                    // Allow the piranha to move; see mouseMove method in this class
                }
            }
            // Allows the mouse to move based on the mouse's position
            public void mouseMoved(MouseEvent e) {
                //This code is the same as the code in the mouseDragged method however this method is for when the mouse is moved (no clicking involved)
                double PiranhaX = Piranha.getX() + PIRANHA_SIZE_X_DIVIDE_2;
                double PiranhaY = Piranha.getY() + PIRANHA_SIZE_Y_DIVIDE_2;
                double MousePositionX = e.getX();
                double MousePositionY = e.getY();

                if (Alive == true) {
                    mouseMove(MousePositionX, MousePositionY, PiranhaX, PiranhaY);
                }
            }
        };
        // This is where the movement of the Piranha is coded
        addMouseMotionListener(MS);
        // Adds the mouse motion listener to the JFrame which allows the code above to work

        Timer t = new Timer(TIMER_TICK_PER_SECOND, new ActionListener() {
            // Creates a new timer that does something every 15 milliseconds
            public void actionPerformed(ActionEvent e) {
                double PiranhaX = Piranha.getX() + PIRANHA_SIZE_X_DIVIDE_2;
                if (Piranha.getY() <= OCEAN_LEVEL) {
                    // If the Piranha's Y value is greater than or equal to the ocean level then
                    // Simulate Jumping by setting the Piranha's Y component to whatever value the Piranha's Y value is - Jump ( - Jump because negative results in up in GUI)
                    if (PiranhaX >= MouseInfo.getPointerInfo().getLocation().getX()) {
                        // If the mouse is to the left of the piranha then
                        Piranha.setLocation(Piranha.getX(), Piranha.getY() - Jump);
                        // Make the piranha jump by subtracting Jump to the Piranha's current Y coordinate
                        Piranha.setIcon(PiranhaL);
                        // Change the icon so that the piranha is looking left
                        Piranha_Eating_Hit_Box.setLocation(Piranha.getX() - Speed, Piranha.getY() + PIRANHA_SIZE_Y_DIVIDE_2);
                        Piranha_Hit_Box.setLocation(Piranha.getX() + Speed, Piranha.getY() + PIRANHA_SIZE_Y_DIVIDE_4);
                        // The hitboxes follow as well; math makes it so that the hitboxes follow are correct when it turns
                    } else if (PiranhaX < MouseInfo.getPointerInfo().getLocation().getX()) {
                        // Similar code to above except for when the mouse is to the right (piranha looks right as well)
                        Piranha.setLocation(Piranha.getX(), Piranha.getY() - Jump);
                        Piranha.setIcon(PiranhaR);
                        Piranha_Eating_Hit_Box.setLocation(Piranha.getX() + PIRANHA_SIZE_X_DIVIDE_2 + Speed, Piranha.getY() + PIRANHA_SIZE_Y_DIVIDE_2);
                        Piranha_Hit_Box.setLocation(Piranha.getX() + PIRANHA_SIZE_X_DIVIDE_3 + Speed, Piranha.getY() + PIRANHA_SIZE_Y_DIVIDE_4);
                    }
                    Jump--;
                    //Decrease jump by 1 (this will add up the longer the piranha is in the air for, so eventually jump will become negative and thus bring the Piranha back down (simulating the end of the jump))
                } else
                    Jump = Jump_Reset;
                //If the Piranha is in the Ocean then Jump is reset back to its original value
            }
        });
        //Piranha's Movement Code (Above the Ocean level)
        t.start();
        //Starts the timer, allowing for the code above to work
    }

    public void mouseMove(double MousePositionX, double MousePositionY, double PiranhaX, double PiranhaY) {
        if (PiranhaY >= MousePositionY && PiranhaY > OCEAN_LEVEL) {
            // If the Piranha is below the mouse and the piranha is in the ocean then
            Piranha.setLocation(Piranha.getX(), Piranha.getY() - Speed);
            // The Piranha will move up
            Piranha_Eating_Hit_Box.setLocation(Piranha.getX(), Piranha_Eating_Hit_Box.getY() - Speed);
            Piranha_Hit_Box.setLocation(Piranha.getX(), Piranha_Hit_Box.getY() - Speed);
            // The hit boxes follow as well
        } else if (PiranhaY < MousePositionY && PiranhaY > OCEAN_LEVEL) {
            // If the Piranha is above the mouse and the piranha is in the ocean then
            Piranha.setLocation(Piranha.getX(), Piranha.getY() + Speed);
            // The Piranha will move down
            Piranha_Eating_Hit_Box.setLocation(Piranha.getX(), Piranha_Eating_Hit_Box.getY() + Speed);
            Piranha_Hit_Box.setLocation(Piranha.getX(), Piranha_Hit_Box.getY() + Speed);
            // The hit boxes follow as well
        }

        if (PiranhaX > MousePositionX)
            // If the Piranha is to the right of the mouse then
            Piranha.setLocation(Piranha.getX() - Speed, Piranha.getY());
            // Have the Piranha follow the mouse (moves left)
        else if (PiranhaX < MousePositionX)
            // If the Piranha is to the left of the mouse then
            Piranha.setLocation(Piranha.getX() + Speed, Piranha.getY());
            // Have the Piranha follow the mouse (moves right)

        if (PiranhaX < MousePositionX + Buffer) {
            // Similar code to below except this is when the Piranha is to the left of the mouse
            Piranha.setIcon(PiranhaR);
            Piranha_Eating_Hit_Box.setLocation(Piranha.getX() + PIRANHA_SIZE_X_DIVIDE_2 + Speed, Piranha.getY() + PIRANHA_SIZE_Y_DIVIDE_2);
            Piranha_Hit_Box.setLocation(Piranha.getX() + PIRANHA_SIZE_X_DIVIDE_3 + Speed, Piranha.getY() + PIRANHA_SIZE_Y_DIVIDE_4);
        } else if (PiranhaX >= MousePositionX - Buffer) {
            // If the Piranha is to the right of the mouse then
            Piranha.setIcon(PiranhaL);
            // Change the Piranha's image so that it looks to the left
            Piranha_Eating_Hit_Box.setLocation(Piranha.getX() - Speed, Piranha_Eating_Hit_Box.getY());
            Piranha_Hit_Box.setLocation(Piranha.getX() + HIT_BOX_ADJUSTMENT + Speed, Piranha.getY() + PIRANHA_SIZE_Y_DIVIDE_4);
            // Have the Hit boxes follow the flipped piranha
        }
    }
    // This method is intended to have the piranha move based on the position of the mouse in comparison to the position of the piranha

    public static byte getJump() {
        return Jump;
        // returns the value of the jump variable
    }
    // getter method that gets the value of the jump variable

    public static void setJump(byte jump) {
        Jump = jump;
        //Jump becomes the value that is given when the method is called
    }
    // setter method that gets the value of the setJump variable

    // Code below is the same concept

    public void setSpeed(byte speed) {
        Speed = speed;
    }

    public void setLevelUP(byte levelUP) {
        LevelUP = levelUP;
    }

    public void setBite_level(byte bite_level) {
        Bite_level = bite_level;
    }

    public void setFin_level(byte fin_level) {
        Fin_level = fin_level;
    }

    public void setSpeed_level(byte speed_level) {
        Speed_level = speed_level;
    }

    public void setStomach_level(byte stomach_level) {
        Stomach_level = stomach_level;
    }

    public void setHunger_gained(byte hunger_gained) {
        Hunger_gained = hunger_gained;
    }

    public void setEnd_of_Delay(float end_of_Delay) {
        End_of_Delay = end_of_Delay;
    }

    public void CheckHitBoxes(JLabel x, Color c){
        x.setVisible(true);
        x.setOpaque(true);
        x.setBackground(c);
        // Allows the user to see the hit box
    }
    // This method is intended to show the user the piranha's hit boxes
}
