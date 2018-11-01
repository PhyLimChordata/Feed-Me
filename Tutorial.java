package com.company;

/* Andy Phy Lim
July 4, 2018
This is the Tutorial class. It essentially is one of the test classes. Tests the methods in the other classes and
displays it in the form of a GUI, it is where the test game play is.  It involves telling the user how the game is played and what controls do what.
For more information, see the comments in TheGame class. */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Tutorial extends PredatorANDPrey implements ActionListener {

    private final short W = 400, H = 400, DEEPER_INTO_OCEAN = 250, A_BIT_DEEPER_INTO_THE_OCEAN = 60, DESIGNATED_AREA = 900;
    private final byte DEFAULT = 0, MAX = 20;

    private final byte HUNGER_DEPLETION = 1, ARROW_DISTANCE = 20, ARROW_HIT_BOX_ADJUSTMENT_X = 10, BUOY_VARIABLE_DISTANCE = 10, INSTRUCTIONS_ADJUSTMENT_X = 80, MINOR_ARROW_ADJUSTMENT_X = 7, SOME_OTHER_MINOR_ARROW_ADJUSTMENT = 30;

    private final short ARROW_ADJUSTMENT_Y = 125, ARROW_HEIGHT = 100, ARROW_WIDTH = 60, ARROW_ADJUSTMENT_X = 200, ARROW_PLACEMENT = 150;
    private final short NEXT_WIDTH = 140, NEXT_HEIGHT = 80;

    private final short BUOY_HEIGHT = 200, BUOY_WIDTH = 100, BUOY_SPEED = 4;

    private final short HIT_BOX_SIZE = 80, HIT_BOX_WIDTH = 180;

    private final short EEL_WIDTH = 750, EEL_HEIGHT = 80, EEL_HIT_BOX_WIDTH = 65, EEL_HIT_BOX_HEIGHT = 57, STOP_EEL_AT_THIS_DISTANCE = 300;
    private final short PUFFERFISH_SIZE = 100, PUFFERFISH_HIT_BOX_WIDTH = 335, PUFFERFISH_HIT_BOX_HEIGHT = 125, STOP_PUFFERFISH_AT_THIS_DISTANCE = 500;
    private final short PELICAN_WIDTH = 700, PELICAN_HEIGHT = 200, PELICAN_HIT_BOX_WIDTH = 100, PELICAN_HIT_BOX_HEIGHT = 110, STOP_PELICAN_AT_THIS_DISTANCE = 125;

    private final float TIME_INCREMENT = 0.02f, COS_INCREMENT = 0.1f;
    private final byte TIMER_TICK_PER_SECOND = 20;

    private byte Tutorial_Counter, Amount;
    private double cos, Time, End_Time;

    private JLabel Buoy, Instructions, Down_Arrow, Up_Arrow, Down_Hit_box, Up_Hit_box, Pelican, Pelican_Hit_Box, Eel, Eels_Hit_Box, Pufferfish, Pufferfish_Hit_Box, Hit_box_Check, Next;

    private ImageIcon Move = imgResizer(new ImageIcon(getClass().getResource("Move.png")), W, H);
    private ImageIcon Jumps = imgResizer(new ImageIcon(getClass().getResource("Jump.png")), W, H);
    private ImageIcon Jumped = imgResizer(new ImageIcon(getClass().getResource("Jumped.png")), W, H);
    private ImageIcon HungerBars = imgResizer(new ImageIcon(getClass().getResource("HungerBars.png")), W, H);
    private ImageIcon Prey = imgResizer(new ImageIcon(getClass().getResource("Prey.png")), W, H);
    private ImageIcon Predator = imgResizer(new ImageIcon(getClass().getResource("Predators.png")), W, H);
    private ImageIcon Upgrades = imgResizer(new ImageIcon(getClass().getResource("Upgrades.png")), W, H);
    private ImageIcon Levels = imgResizer(new ImageIcon(getClass().getResource("Levels.png")), W, H);
    private ImageIcon PressQ = imgResizer(new ImageIcon(getClass().getResource("Q.png")), W, H);
    private ImageIcon PressW = imgResizer(new ImageIcon(getClass().getResource("W.png")), W, H);
    private ImageIcon PressE = imgResizer(new ImageIcon(getClass().getResource("E.png")), W, H);
    private ImageIcon PressR = imgResizer(new ImageIcon(getClass().getResource("R.png")), W, H);
    private ImageIcon QInfo = imgResizer(new ImageIcon(getClass().getResource("QInfo.png")), W, H);
    private ImageIcon WInfo = imgResizer(new ImageIcon(getClass().getResource("WInfo.png")), W, H);
    private ImageIcon EInfo = imgResizer(new ImageIcon(getClass().getResource("EInfo.png")), W, H);
    private ImageIcon RInfo = imgResizer(new ImageIcon(getClass().getResource("RInfo.png")), W, H);
    private ImageIcon Everything = imgResizer(new ImageIcon(getClass().getResource("Everything.png")), W, H);
    private ImageIcon Remember = imgResizer(new ImageIcon(getClass().getResource("Remember.png")), W, H);
    private ImageIcon Survive = imgResizer(new ImageIcon(getClass().getResource("Survive.png")), W, H);

    private Timer Tutorial = new Timer(TIMER_TICK_PER_SECOND, this);

    // Instance variables are declared along with a bunch of constants that will be used in the following code

    Tutorial() {
        super();
        /* Allows access to the classes: PredatorANDPrey, Piranha, and Screen. Which allows the tutorial to be created.
           Screen: Frame, Background, Piranha.
           Piranha: Mouse Motion listener, Upgrades, Hit boxes.
           PredatorANDPrey: The predators and the prey. */
        Tutorial.start();
        // Start the timer. Which starts the tutorial
        setJump(DEFAULT);
        // Sets the jump to 0 so that the user can't learn to jump first thing (see if statement later in this code)
        Jump_Reset = getJump();
        // Sets Jump_Reset to the value of Jump (should be 0)
        setLevelUP(MAX);
        // Sets the LevelUP to 20 so that the user can upgrade all of their upgrades
        addLabel(Buoy = new JLabel(), SIZE_OF_GUI_X + BUOY_WIDTH, OCEAN_LEVEL - BUOY_HEIGHT, BUOY_WIDTH, BUOY_HEIGHT, "Buoy.png");
        addLabel(Instructions = new JLabel(), Buoy.getX(), Buoy.getY(), W, H, "Instructions.png");
        addLabel(Down_Arrow = new JLabel(), SIZE_OF_GUI_X, DEFAULT, ARROW_WIDTH, ARROW_HEIGHT, "DownArrow.png");
        addLabel(Up_Arrow = new JLabel(), SIZE_OF_GUI_X, DEFAULT, ARROW_WIDTH, ARROW_HEIGHT, "UpArrow.png");
        addLabel(Down_Hit_box = new JLabel(), SIZE_OF_GUI_X, DEFAULT, HIT_BOX_SIZE, HIT_BOX_SIZE);
        addLabel(Up_Hit_box = new JLabel(), SIZE_OF_GUI_X, DEFAULT, HIT_BOX_SIZE, HIT_BOX_SIZE);
        addLabel(Hit_box_Check = new JLabel(), SIZE_OF_GUI_X, DEFAULT, HIT_BOX_WIDTH, HIT_BOX_SIZE, "CheckHitBox.png");
        addLabel(Next = new JLabel(), SIZE_OF_GUI_X, DEFAULT, NEXT_WIDTH, NEXT_HEIGHT, "Next.png");
        addLabel(Pelican = new JLabel(), SIZE_OF_GUI_X + PELICAN_WIDTH, DEFAULT, PELICAN_WIDTH, PELICAN_HEIGHT, "PelicanStats.png");
        addLabel(Pelican_Hit_Box = new JLabel(), SIZE_OF_GUI_X, DEFAULT, PELICAN_HIT_BOX_WIDTH, PELICAN_HIT_BOX_HEIGHT);
        addLabel(Eel = new JLabel(), SIZE_OF_GUI_X, OCEAN_LEVEL + DEEPER_INTO_OCEAN, EEL_WIDTH, EEL_HEIGHT, "EelStats.png");
        addLabel(Eels_Hit_Box = new JLabel(), SIZE_OF_GUI_X, DEFAULT, EEL_HIT_BOX_WIDTH, EEL_HIT_BOX_HEIGHT);
        addLabel(Pufferfish = new JLabel(), SIZE_OF_GUI_X, OCEAN_LEVEL + PUFFERFISH_SIZE, PUFFERFISH_HIT_BOX_WIDTH, PUFFERFISH_HIT_BOX_HEIGHT, "PufferfishStats.png");
        addLabel(Pufferfish_Hit_Box = new JLabel(), SIZE_OF_GUI_X, DEFAULT, PUFFERFISH_SIZE, PUFFERFISH_SIZE);
        // Creates and adds all of these labels to the Tutorial. The location doesn't matter just know that they are off screen and ready to be used.
    }

    public void actionPerformed(ActionEvent e) {
        Time += TIME_INCREMENT;
        // Time increases by 0.02 each time the timer ticks (based off real time)
        cos += COS_INCREMENT;
        // cos increases by 0.1 each time the timer ticks
        BackgroundMovement();
        // Makes the background move; see BackgroundMovement method in Screen class

        // This part of code manages the order of how the tutorial is shown. To understand it better read from the bottom of the if statement to the top
        // Just a little side note: The seconds delay is needed for the users to read the things that the buoy has got to say
        if (Buoy.getX() + Buoy.getWidth() + Instructions.getWidth() < 0) {
            System.exit(0);
            // When the buoy gets off screen to the left of the screen, exit the program
        } else if (Tutorial_Counter == 15) {
            Instructions.setIcon(Survive);
            // The instructions changes to repeating a more boiled down goal of the game which is to survive for as long as possible
            MoveBuoy();
            // and the buoy continues to move
        } else if (Time > End_Time && Tutorial_Counter == 14) {
            Tutorial_Counter++;
            // After the 3 second delay, tutorial counter increases by 1
        } else if (Tutorial_Counter == 14) {
            Instructions.setIcon(Remember);
            // The instructions will be set to telling the user the threats and goal of the game
            MoveBuoy();
            // The buoy will not move towards the left screen again
        } else if (Time > End_Time && Tutorial_Counter == 13) {
            // After the 1.5 second delay
            SetUpNextLesson(3);
            // A new lesson is set
        } else if (Tutorial_Counter == 13) {
            Instructions.setIcon(Everything);
            // The instructions changes to telling the user that that's Everything!
            BobbingBuoy(Everything);
            // The buoy will say that bobbing still
            ResetArrows();
            // The arrows are set off screen
        } else if (Time > End_Time && Tutorial_Counter == 12) {
            SetUpNextLesson(1.5);
            // A new lesson is set
        } else if (Tutorial_Counter == 12) {
            BobbingBuoy(RInfo);
            Up_Arrow.setLocation(Stomach.getX() - MINOR_ARROW_ADJUSTMENT_X, Stomach.getY() + ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE));
        } else if (Stomach_level > 0 && Tutorial_Counter == 11) {
            SetUpNextLesson(2.2);
        } else if (Tutorial_Counter == 11) {
            BobbingBuoy(PressR);
            Up_Arrow.setLocation(Stomach.getX() - MINOR_ARROW_ADJUSTMENT_X, Stomach.getY() + ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE));
        } else if (Time > End_Time && Tutorial_Counter == 10) {
            SetUpNextLesson(2.2);
        } else if (Tutorial_Counter == 10) {
            BobbingBuoy(EInfo);
            Up_Arrow.setLocation(Tail.getX() - MINOR_ARROW_ADJUSTMENT_X, Tail.getY() + ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE));
        } else if (Speed_level > 0 && Tutorial_Counter == 9) {
            SetUpNextLesson(2.2);
        } else if (Tutorial_Counter == 9) {
            BobbingBuoy(PressE);
            Up_Arrow.setLocation(Tail.getX() - MINOR_ARROW_ADJUSTMENT_X, Tail.getY() + ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE));
        } else if (Time > End_Time && Tutorial_Counter == 8) {
            SetUpNextLesson(2.2);
        } else if (Tutorial_Counter == 8) {
            BobbingBuoy(WInfo);
            Up_Arrow.setLocation(Fin.getX() - MINOR_ARROW_ADJUSTMENT_X, Fin.getY() + ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE));
        } else if (Fin_level > 0 && Tutorial_Counter == MINOR_ARROW_ADJUSTMENT_X) {
            SetUpNextLesson(2.2);
        } else if (Tutorial_Counter == MINOR_ARROW_ADJUSTMENT_X) {
            BobbingBuoy(PressW);
            Up_Arrow.setLocation(Fin.getX() - MINOR_ARROW_ADJUSTMENT_X, Fin.getY() + ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE));
        } else if (Time > End_Time && Tutorial_Counter == 6) {
            SetUpNextLesson(2.2);
        } else if (Tutorial_Counter == 6) {
            BobbingBuoy(QInfo);
            // The buoy will now say the properties of the PressQ upgrade
            Up_Arrow.setLocation(Bites.getX() - MINOR_ARROW_ADJUSTMENT_X, Bites.getY() + ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE));
            // Arrow placed under designated upgrade
            MovingPredator(-Pelican.getWidth(), -Pufferfish.getWidth(), -Eel.getWidth());
            // Predators will continue to move just in case
            //The above code involving the upgrades are similar enough to this
        } else if (Bite_level > 0 && Tutorial_Counter == 5) {
            SetUpNextLesson(2.2);
            // The next lesson is set up
        } else if (Tutorial_Counter == 5) {
            BobbingBuoy(PressQ);
            // The buoy will now say: Press PressQ
            Up_Arrow.setLocation(Bites.getX() - MINOR_ARROW_ADJUSTMENT_X, Bites.getY() + ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE));
            // Up arrow is now placed before the bite label
            MovingPredator(-Pelican.getWidth(), -Pufferfish.getWidth(), -Eel.getWidth());
            // Predators will continue to move just in case
        } else if (Time > End_Time && Tutorial_Counter == 4) {
            // After the 4 second delay
            Tutorial_Counter++;
            // Tutorial counter increases by 1 setting off the next else if statement
        } else if (Tutorial_Counter == 4) {
            BobbingBuoy(Levels);
            // Another statement is brought up telling the users about the levels of their upgrades
            Up_Arrow.setLocation(Hunger_Bar.getX() + Hunger_Bar.getWidth() / 8, Bites.getY() + ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE));
            // The arrow is kept in its location
            MovingPredator(-Pelican.getWidth(), -Pufferfish.getWidth(), -Eel.getWidth());
            // The predators are continuing to move off screen if needed (if player decided to gio to the next part immediately)
        } else if (Time > End_Time && Tutorial_Counter == 3) {
            // After the 4 second delay caused by the set up next lesson before
            SetUpNextLesson(4);
            // Another lesson is set up (remember wave counter +1)
            Amount = DEFAULT;
            // Amount is set to 0 so that the else if containing Amount = 6 doesn't trigger instead of the else statement
        } else if (Tutorial_Counter == 3) {
            // Immediately after hitting the next part, tutorial counter increases by 1 (because of SetUpNextLesson method) triggering this else if statement
            BobbingBuoy(Upgrades);
            // The buoy will give out a message involving the upgrades
            Up_Arrow.setLocation(Hunger_Bar.getX() + Hunger_Bar.getWidth() / 8, Bites.getY() + ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE));
            // The up arrow is positioned underneath the upgrades bar and will be indicating the users that the icons it is pointing at are the upgrades, again arrows moving up and down and there's no hit box needed
            MovingPredator(-Pelican.getWidth(), -Pufferfish.getWidth(), -Eel.getWidth());
            // The predators are now free to move off screen
        } else if (Piranha_Hit_Box.getBounds().intersects(Up_Hit_box.getBounds())) {
            // However if the player moves the piranha to the arrow saying next part then the hit box will intersect with the arrow's hitbox and
            SetUpNextLesson(4);
            // Set up for the next lesson
            ResetArrows();
            // The arrows are set back off screen
            setJump((byte)5);
            Jump_Reset = getJump();
            // The fish jump has been modified to become 5 for the next lesson
        } else if (Piranha_Hit_Box.getBounds().intersects(Down_Hit_box.getBounds()) && Tutorial_Counter == 2) {
            // If the player chooses to move the piranha to the check hit boxes arrow then the player will hit the hit box associated with the arrow and then
            BobbingBuoy((ImageIcon) Instructions.getIcon());
            // The buoy will continue to bob
            SetUpArrows(SIZE_OF_GUI_X - ARROW_ADJUSTMENT_X, OCEAN_LEVEL - ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE), ARROW_PLACEMENT, OCEAN_LEVEL + ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE));
            // The arrows will remain the same
            Next.setLocation(Up_Arrow.getX() - SOME_OTHER_MINOR_ARROW_ADJUSTMENT, Up_Arrow.getY() + Up_Arrow.getHeight() + CosineMovement(cos, ARROW_DISTANCE));
            Hit_box_Check.setLocation(Down_Arrow.getX() - SOME_OTHER_MINOR_ARROW_ADJUSTMENT, Down_Arrow.getY() - ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE));
            // The words will remain the same
            MovingPredator(STOP_PELICAN_AT_THIS_DISTANCE, STOP_PUFFERFISH_AT_THIS_DISTANCE, STOP_EEL_AT_THIS_DISTANCE);
            // The predators will continue to move
            CheckHitboxes(Eels_Hit_Box);
            CheckHitboxes(Pelican_Hit_Box);
            CheckHitboxes(Pufferfish_Hit_Box);
            // The hit boxes are displayed as black boxes in the areas where the predators pose threatening, if the players choose to move away from the check hit boxes the else if statement below will be triggered again
        } else if (Amount == 6) {
            // When all the prey has moved to the left or has gotten eaten
            BobbingBuoy(Predator);
            // the new instruction is introduced: "Be Wary of Predators"
            SetUpArrows(SIZE_OF_GUI_X - ARROW_ADJUSTMENT_X, OCEAN_LEVEL - ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE), ARROW_PLACEMENT, OCEAN_LEVEL + ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE));
            // An up arrow and a down arrow is placed in the right side and the left side (respectively) of the program
            Next.setLocation(Up_Arrow.getX() - SOME_OTHER_MINOR_ARROW_ADJUSTMENT, Up_Arrow.getY() + Up_Arrow.getHeight() + CosineMovement(cos, ARROW_DISTANCE));
            // A JLabel is paired with the up arrow. The JLabel is just an image: "Next"
            Hit_box_Check.setLocation(Down_Arrow.getX() - SOME_OTHER_MINOR_ARROW_ADJUSTMENT, Down_Arrow.getY() - ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE));
            // A  JLabel is paired with the down arrow. The JLabel is just an image: "Hit Box Check"
            Eels_Hit_Box.setVisible(false);
            Pelican_Hit_Box.setVisible(false);
            Pufferfish_Hit_Box.setVisible(false);
            // Although the hit boxes are set visible when it already is, it is necessary for if the user chooses to check the hit boxes
            MovingPredator(STOP_PELICAN_AT_THIS_DISTANCE, STOP_PUFFERFISH_AT_THIS_DISTANCE, STOP_EEL_AT_THIS_DISTANCE);
            // Have the predators move from the right screen to the left screen
        } else if (Tutorial_Counter == 2 && Time > End_Time + 4) {
            // When 4 seconds has passed since the last else if statement
            cos = DEFAULT;
            BobbingBuoy(Prey);
            // The instructions will update to tell the players what the prey in the game are
            Up_Arrow.setLocation(Hunger_Bar.getX() + Hunger_Bar.getWidth() * 3 / 4, Hunger_Bar.getY() + Hunger_Bar.getHeight() + SOME_OTHER_MINOR_ARROW_ADJUSTMENT + CosineMovement(cos, ARROW_DISTANCE));
            // The up arrow will stay in its position
            Hunger.setSize(Hunger.getWidth() - HUNGER_DEPLETION, Hunger.getHeight());
            // The hunger bar will continue to deplete
            MovingPrey();
            // The prey will start moving from the right of the screen to the left; there's no need to implement code that will prevent the piranha from dying of hunger since the next else if statement will occur before that
        } else if (Tutorial_Counter == 2 && Time > End_Time) {
            // When the user completes the jump and the time has elapsed (in this case 1.5 seconds has passed)
            BobbingBuoy(HungerBars);
            // The next instruction is introduced: telling the player that they have a hunger bar and that they can die if it reaches the end
            SetUpArrows(Hunger_Bar.getX() + Hunger_Bar.getWidth() * 3 / 4, Hunger_Bar.getY() + Hunger_Bar.getHeight() + SOME_OTHER_MINOR_ARROW_ADJUSTMENT + CosineMovement(cos, ARROW_DISTANCE), SIZE_OF_GUI_X, DEFAULT);
            // An Up Arrow will be put below the hunger bar and moving up and down so that the player knows the thing its pointing at is the hunger bar
            Hunger.setSize(Hunger.getWidth() - HUNGER_DEPLETION, Hunger.getHeight());
            // As that's happening, the hunger bar will deplete by having its width get decreased by HUNGER_DEPLETION with each timer tick
        } else if (Piranha.getY() < OCEAN_LEVEL && Tutorial_Counter == 1) {
            // When the user gets the piranha to be above sea level the fish will demonstrate that it can jump
            ResetArrows();
            // The arrows are then placed off screen
            BobbingBuoy(Jumped);
            // and a new statement will pop up: The Fish Jumps!
            SetUpNextLesson(1.5);
            // The next lesson is set up with the SetUpNextLesson method in this class
        } else if (Tutorial_Counter == 1) {
            // When tutorial counter is equal to 1
            Up_Arrow.setLocation(SIZE_OF_GUI_X / 2, OCEAN_LEVEL - Buoy.getHeight() + CosineMovement(cos, ARROW_DISTANCE));
            // Set the location of the up arrow and have it move up and down. It is positioned in the middle of the screen above sea level (no need to use SetUpArrows method here because a hit box isn't needed)
            BobbingBuoy((ImageIcon) Instructions.getIcon());
            // The bouy continues to bob (maintaining the same instructions), this else if will wait until the user decides to move the fish above sea level (go to next else if statement)
        } else if (Piranha_Hit_Box.getBounds().intersects(Down_Hit_box.getBounds()) && Tutorial_Counter == 0) {
            // If the player successfully moves the piranha to the arrow (arrow has a hit box attached below it) and tutorial counter is still 0 then
            setJump((byte)25);
            // The piranha now has a jump of 25
            Jump_Reset = getJump();
            // and a jump reset of 25
            ResetArrows();
            // The arrows are then placed off screen; see ResetArrows method in this class
            BobbingBuoy(Jumps);
            // The next instruction is introduced: Move the fish above sea level
            Tutorial_Counter++;
            // Tutorial Counter increases by 1 making the next if statements trigger
        } else if (Time > 5 && Tutorial_Counter == 0) {
            // After a 5 second delay and if the tutorial counter is 0 (which it is set to initially)
            BobbingBuoy(Move);
            // Have the buoy bob and introduce an instruction: Move the mouse to move the fish; see BobbingBuoy method
            SetUpArrows(SIZE_OF_GUI_X, DEFAULT, Buoy.getX() + ARROW_DISTANCE, OCEAN_LEVEL + ARROW_ADJUSTMENT_Y + CosineMovement(cos, ARROW_DISTANCE));
            // Set up a down arrow so that it will be positioned below the buoy and moving up and down (Indicating that the piranha should move there); see SetUpArrows method in this class
        } else if (Time > 1 && Buoy.getX() > DESIGNATED_AREA) {
            // After a 1 second delay, the buoy will start moving because its x coordinate is not at the designated area (x coordinate is greater than designated area)
            MoveBuoy();
            // Moves the buoy to a designated area (moves it left)
        } else
            // A break will occur when the tutorial waits for the users input or if there is a brief delay before the next instruction
            BobbingBuoy((ImageIcon) Instructions.getIcon());
        // Whenever there is a "break" in the tutorial, have the buoy bob; see BobbingBuoy method in this class
    }
    // For the sake of simplicity, some of the things inside this if statement is hard coded.

    public void MoveBuoy() {
        Buoy.setLocation(Buoy.getX() - BUOY_SPEED, OCEAN_LEVEL + A_BIT_DEEPER_INTO_THE_OCEAN - Buoy.getHeight() + (int) ((Math.cos((cos / 2))) * BUOY_VARIABLE_DISTANCE));
        // The Buoy is moved to the left by whatever is provided (x) each time the timer is ticked, additionally the y coordinate allows for the buoy to bob while moving
        Instructions.setLocation(Buoy.getX() - INSTRUCTIONS_ADJUSTMENT_X, Buoy.getY() - Instructions.getHeight());
        // The Instructions will follow
    }
    // This method is intended to have the buoy move to the left with the instructions following it

    public void BobbingBuoy(ImageIcon I) {
        Instructions.setIcon(I);
        // Instructions JLabel will be set with a new Icon (the icon provided)
        Buoy.setLocation(Buoy.getX(), OCEAN_LEVEL + A_BIT_DEEPER_INTO_THE_OCEAN - Buoy.getHeight() + (int) ((Math.cos((cos / 2))) * BUOY_VARIABLE_DISTANCE));
        // The buoy bobs up and down by staying in its x location but having its y location use a cosine function
        Instructions.setLocation(Buoy.getX() - INSTRUCTIONS_ADJUSTMENT_X, Buoy.getY() - Instructions.getHeight());
        // The instructions will follow the movement of the buoy
    }
    // This method is intended to have the buoy bob up and down with the instructions.

    public void ResetArrows() {
        Down_Hit_box.setLocation(SIZE_OF_GUI_X, DEFAULT);
        Down_Arrow.setLocation(SIZE_OF_GUI_X, DEFAULT);
        Up_Hit_box.setLocation(SIZE_OF_GUI_X, DEFAULT);
        Up_Arrow.setLocation(SIZE_OF_GUI_X, DEFAULT);
        Next.setLocation(SIZE_OF_GUI_X, DEFAULT);
        Hit_box_Check.setLocation(SIZE_OF_GUI_X, DEFAULT);
        // Set all arrows and their hit boxes to be offscreen
    }
    // This method is intended to put everything associated with arrows off screen and therefore not visible to the player

    public void SetUpNextLesson(double x) {
        // x indicates the amount of time required to pass until another instruction/statement is introduced
        Tutorial_Counter++;
        // tutorial counter increases by 1 (setting up for the next else if statement)
        End_Time = Time + x;
        // End_Time is the Time + how many seconds needed to pass
        BobbingBuoy((ImageIcon) Instructions.getIcon());
        // The buoy will continue to bob up and down with the instructions
    }
    // This method is intended to set up for the next lesson in the tutorial. It does this by increasing tutorial counter and setting Delayed times

    public void SetUpArrows(int ux, int uy, int dx, int dy) {
        Up_Arrow.setLocation(ux, uy);
        // The UpArrows location is updated
        Up_Hit_box.setLocation(Up_Arrow.getX() - ARROW_HIT_BOX_ADJUSTMENT_X, Up_Arrow.getY() - Up_Hit_box.getHeight());
        // The UpArrows hit box will follow and be positioned above it

        Down_Arrow.setLocation(dx, dy);
        // The DownArrows location is updated
        Down_Hit_box.setLocation(Down_Arrow.getX() - ARROW_HIT_BOX_ADJUSTMENT_X, Down_Arrow.getY() + Down_Arrow.getHeight());
        // The DownArrows hit box will follow and be positioned below it
    }
    // This method is intended to relocate the position of the up and down arrows and their hit boxes

    public void MovingPrey() {
        for (byte i = 0; i < LIMIT / 5; i++) {
            // Loops a total of 3 times
            Fish[i].setLocation(Fish[i].getX() - (int) Fish_Speed, Fish[i].getY());
            // Sends a fish and moves it to the left according to its position
            if (Fish[i].getX() < LEFT_OF_SCREEN - Fish[i].getWidth() && Fish[i].isVisible()) {
                // If the fish is off screen (left screen)
                Fish[i].setVisible(false);
                // The fish is set invisible (rendering this if statement false (thus not moving it more))
                Amount++;
                // Amount will increase by 1
            }
            // This will apply to all fish

            if (Piranha_Eating_Hit_Box.getBounds().intersects(Fish[i].getBounds())) {
                // If the piranha's eating hit box collides with any of the fish then
                Fish[i].setVisible(false);
                // Set the fish to be invisible
                Fish[i].setLocation(LEFT_OF_SCREEN - Fish[i].getWidth(), OCEAN_LEVEL);
                // Set its location to the left screen
                Hunger.setSize(Hunger.getWidth() + 2 * Hunger_gained, Hunger.getHeight());
                Amount++;
                // Amount will increase by 1
            }
        }

        // Code below has similar concept

        for (byte i = 0; i < LIMIT / 5; i++) {
            Seagull[i].setLocation(Seagull[i].getX() - (int) Seagull_Speed, Seagull[i].getY());
            Seagulls_Hit_Box[i].setLocation(Seagull[i].getX() + FLYING_ADJUSTMENT, Seagull[i].getY() + FLYING_ADJUSTMENT);
            if (Seagull[i].getX() < LEFT_OF_SCREEN - Seagull[i].getWidth() && Seagull[i].isVisible()) {
                Seagull[i].setVisible(false);
                Amount++;
            }
            if (Piranha_Eating_Hit_Box.getBounds().intersects(Seagulls_Hit_Box[i].getBounds())) {
                Seagull[i].setVisible(false);
                Seagull[i].setLocation(LEFT_OF_SCREEN - Seagull[i].getWidth(), 0);
                Hunger.setSize(Hunger.getWidth() + 2 * Hunger_gained, Hunger.getHeight());
                Amount++;
            }
        }
    }
    // This method is intended to have the prey move and checking if they get eaten or not

    public void MovingPredator(int x, int y, int z) {
        if (Pelican.getX() > x) {
            // If the pelican hasn't reached where x is (based off the pelican's x coordinate) then
            Pelican.setLocation(Pelican.getX() - (int) Pelican_Speed, PELICANS_STARTING_POSITION + CosineMovement(cos, SPACE_FROM_OCEAN_LEVEL));
            // Move the pelican to the right and incorporate a cosine movement
            Pelican_Hit_Box.setLocation(Pelican.getX() + FLYING_ADJUSTMENT, Pelican.getY() + PELICANS_ADJUSTMENT);
            // The hit box will follow the pelican's x and y coordinates
        }
        // The pelican will stop moving when it reaches x

        //The code below is the same concept

        if (Pufferfish.getX() > y) {
            Pufferfish.setLocation(Pufferfish.getX() - (int) Pufferfish_Speed, Pufferfish.getY());
            Pufferfish_Hit_Box.setLocation(Pufferfish.getX() + PUFFERFISH_ADJUSTMENT, Pufferfish.getY() + PUFFERFISH_ADJUSTMENT);
        }

        if (Eel.getX() > z) {
            Eel.setLocation(Eel.getX() - (int) Eel_Speed, Eel.getY());
            Eels_Hit_Box.setLocation(Eel.getX() + EEL_ADJUSTMENT_X, Eel.getY() + EEL_ADJUSTMENT_Y);
        }
    }
    // This method is intended to have the predators move

    public void CheckHitboxes(JLabel Hit) {
        Hit.setVisible(true);
        Hit.setOpaque(true);
        Hit.setBackground(Color.BLACK);
    }
    // This method is intended to check the hit boxes by setting them to be visible to the user as a black box
}
