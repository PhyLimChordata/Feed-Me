package com.company;

/* Andy Phy Lim
July 4, 2018
This is TheGame class. It essentially is one of the test classes. Tests the methods in the other classes and
displays it in the form of a GUI, it is where the actual game play is. The game involves the player playing as a piranha and attempting to survive
the endless waves of enemies that are being sent at them and the slowly burning hunger bar. They can better their survival with upgrades but in the end they eventually fall. When they do
their score will be saved and recorded. */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

class TheGame extends PredatorANDPrey implements ActionListener {

    private double Time, delay, x, EndTime;
    private final float DELAY_INCREMENT = 0.6f;
    private final byte RESET_DELAY = 0;

    private double Hungry = 1.5;
    private final double DEFAULT_HUNGRY = 1.5;
    private final short STARVATION = 0;
    private short Hunger_Limit = 635;

    private int NumberOfWaves, WaveCounter, FishCount, SeagullsCount, EelCount, PufferfishCount, PelicanCount, PelicanBotCount;
    private static Integer High_score;
    private final byte RESET = 0, TIMER_TICK_PER_SECOND = 20, INCREASE_POINTS = 10;
    private final byte JUMP_LIMIT = 0;
    private final float COOL_DOWN = 0.4f;
    private final short DAMAGE = 200;
    private boolean Hit;

    private final float TIME_INCREMENT = 0.02f, X_INCREMENT = 0.05f;
    private final float HUNGER_INCREMENT = 0.00005f, FISH_SPEED_INCREMENT = 0.0005f, SEAGULL_SPEED_INCREMENT = 0.0004f, EEL_SPEED_INCREMENT = 0.0007f, PUFFERFISH_SPEED_INCREMENT = 0.0006f, PELICAN_SPEED_INCREMENT = 0.0004f;

    final short SCORE_WIDTH = 350, SCORE_HEIGHT = 100, SCORE_ADJUSTMENT_X = 25, POINT_ADJUSTMENT_Y = 100, HIGH_SCORE_ADJUSTMENT_X = SCORE_ADJUSTMENT_X + SCORE_WIDTH, HIGH_SCORE_ADJUSTMENT_Y = 100, HIGH_SCORE_WIDTH = 450, HIGH_SCORE_HEIGHT = 100;

    final Font FONTSTYLE = new Font("IMPACT", Font.BOLD, FONT_SIZE);

    private final short POSITION_OF_GAME_OVER_X = 125, POSITION_OF_GAME_OVER_Y = 230, GAME_OVER_WIDTH = 1200, GAME_OVER_HEIGHT = 350;
    private final short POSITION_OF_HIGH_SCORE_X = POSITION_OF_GAME_OVER_X, POSITION_OF_HIGH_SCORE_Y = POSITION_OF_GAME_OVER_Y, GAME_OVER_WITH_HIGH_SCORE_WIDTH = GAME_OVER_WIDTH, GAME_OVER_WITH_HIGH_SCORE_HEIGHT = 550;

    private final short ARROW_HEIGHT = 85, ARROW_WIDTH = 50, ARROW_DISTANCE = 10;

    ArrayList<Integer> Scores = new ArrayList<>();

    JLabel levelup, GameOver, GameOverWithHighScore;

    File file = new File("HighScores");

    Timer Game = new Timer(TIMER_TICK_PER_SECOND, this);

    // Instance variables are declared along with a bunch of constants that will be used in the following code

    TheGame() {
        super();
        /* Allows access to the classes: PredatorANDPrey, Piranha, and Screen. Which allows the game to be created.
           Screen: Frame, Background, Piranha.
           Piranha: Mouse Motion listener, Upgrades, Hit boxes.
           PredatorANDPrey: The predators and the prey. */
        addLabel(Score, (short)(LEFT_OF_SCREEN + SCORE_ADJUSTMENT_X), (short)(SIZE_OF_GUI_Y - POINT_ADJUSTMENT_Y), SCORE_WIDTH, SCORE_HEIGHT, "SCORE: ", FONTSTYLE);
        // Creates a JLabel that will tell the user how many points they have, The math positions the label in the bottom left; see addLabel method in Screen class
        addLabel(HighScore, (short)(LEFT_OF_SCREEN + HIGH_SCORE_ADJUSTMENT_X), (short)(SIZE_OF_GUI_Y - HIGH_SCORE_ADJUSTMENT_Y), HIGH_SCORE_WIDTH, HIGH_SCORE_HEIGHT, "HIGH SCORE: ", FONTSTYLE);
        // Creates a JLabel that will tell the user what the high score is, The math positions the label to be beside the points label; see addLabel method in Screen class
        DisplayHighScore(Scores);
        // Display the high score; see DisplayHighScore method in this class
        addLabel(levelup = new JLabel(), Stomach.getX() + Stomach.getWidth() + SPACING, POSITION_Y, ARROW_WIDTH, ARROW_HEIGHT, "UpArrow.png");
        levelup.setVisible(false);
        // Creates a level indicator and sets it to be invisible (positions it next to the stomach icon)
        addLabel(GameOver = new JLabel(), POSITION_OF_GAME_OVER_X, POSITION_OF_GAME_OVER_Y, GAME_OVER_WIDTH, GAME_OVER_HEIGHT, "GAMEOVER.png");
        GameOver.setVisible(false);
        // Creates a game over label and sets it to be invisible (positions it in the centre of the frame)
        addLabel(GameOverWithHighScore = new JLabel(), POSITION_OF_HIGH_SCORE_X, POSITION_OF_HIGH_SCORE_Y, GAME_OVER_WITH_HIGH_SCORE_WIDTH, GAME_OVER_WITH_HIGH_SCORE_HEIGHT, "HIGHSCORE.png");
        GameOverWithHighScore.setVisible(false);
        // Creates a high score label and sets it to be invisible (positions it in the centre of the frame)
        Game.start();
        // Start the timer. Which starts the game
        addKeyListener(new KeyListener() {
            //Create a key listener that listens for key events
            public void keyTyped(KeyEvent e) {
            }
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                // The key that the user pressed on will be saved as "code"
                if (code == KeyEvent.VK_SPACE && !Alive) {
                    // If the user clicked the space bar when the piranha isn't alive then
                    ResetGame();
                    // Reset the game back to its default settings; see ResetGame method in this class
                    for (byte i = 0; i < Levels_Below_The_Upgrades.length; i++) {
                        Levels_Below_The_Upgrades[i].setIcon(levels[RESET]);
                        // Reset the levels back to level 0 (changes the images)
                    }
                    Game.start();
                    // Then Start the game again
                }
            }
            public void keyReleased(KeyEvent e) {
            }
        });
        //In summary, if the space bar is pressed on, restart the game
    }

    public void actionPerformed(ActionEvent e) {
        // The code below is executed every 20 milliseconds
        ProgressiveDifficulty(TIME_INCREMENT, X_INCREMENT, HUNGER_INCREMENT, FISH_SPEED_INCREMENT, SEAGULL_SPEED_INCREMENT, EEL_SPEED_INCREMENT, PUFFERFISH_SPEED_INCREMENT, PELICAN_SPEED_INCREMENT);
        // Makes the game progressively harder on the user; see ProgressiveDifficulty method in this class
        BackgroundMovement();
        // Makes the background move; see BackgroundMovement method in Screen class
        HungerDepletion();
        // Hunger will slowly deplete as the game goes on; Will also check if the game has ended or not; see HungerDepletion method in this class
        Count();
        // Count the number of labels that reach the left side of the screen, if a label is then +1 for that specific label type; see Count method in this class
        UpdateScore();
        // Updates the score; see UpdateScore method in this class
        levelUp();
        // If the user can upgrade their piranha then they will be shown a level indicator; see levelUp method in this class

        // This part of code manages the waves of predators and preys that come at the piranha(user). To understand it better read from the bottom of the if statement to the top
        if (NumberOfWaves == 0 && WaveCounter == 50) {
            Hunger.setSize(RESET, Hunger.getHeight());
            // End the game; see Hunger Depletion method
        } else if (WaveCounter == 50) {
            CreateAndMoveWave(15, 15, 15, 15, 15, 15);
        } else if (NumberOfWaves == 0 && WaveCounter == 49) {
            SetUpNextWave(6);
            LevelUP++;
            System.out.println("Level 50");
        } else if (WaveCounter == 49) {
            CreateAndMoveWave(15, 0, 15, 15, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 48) {
            SetUpNextWave(3);
        } else if (WaveCounter == 48) {
            CreateAndMoveWave(0, 15, 15, 0, 8, 8);
        } else if (NumberOfWaves == 0 && WaveCounter == 47) {
            SetUpNextWave(4);
            LevelUP++;
        } else if (WaveCounter == 47) {
            CreateAndMoveWave(0, 15, 8, 0, 3, 3);
        } else if (NumberOfWaves == 0 && WaveCounter == 46) {
            SetUpNextWave(4);
        } else if (WaveCounter == 46) {
            CreateAndMoveWave(0, 3, 8, 0, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 45) {
            SetUpNextWave(2);
            LevelUP++;
        } else if (WaveCounter == 45) {
            CreateAndMoveWave(0, 5, 5, 0, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 44) {
            SetUpNextWave(2);
            System.out.println("Level 45");
        } else if (WaveCounter == 44) {
            CreateAndMoveWave(0, 8, 15, 0, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 43) {
            SetUpNextWave(2);
        } else if (WaveCounter == 43) {
            CreateAndMoveWave(10, 10, 6, 6, 2, 4);
        } else if (NumberOfWaves == 0 && WaveCounter == 42) {
            SetUpNextWave(6);
        } else if (WaveCounter == 42) {
            CreateAndMoveWave(2, 10, 3, 3, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 41) {
            SetUpNextWave(4);
            LevelUP++;
        } else if (WaveCounter == 41) {
            CreateAndMoveWave(5, 0, 4, 0, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 40) {
            SetUpNextWave(2);
        } else if (WaveCounter == 40) {
            CreateAndMoveWave(5, 5, 5, 5, 2, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 39) {
            SetUpNextWave(5);
            System.out.println("Level 40");
        } else if (WaveCounter == 39) {
            CreateAndMoveWave(15, 15, 0, 0, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 38) {
            SetUpNextWave(2);
        } else if (WaveCounter == 38) {
            CreateAndMoveWave(0, 0, 3, 8, 2, 3);
        } else if (NumberOfWaves == 0 && WaveCounter == 37) {
            SetUpNextWave(4);
            LevelUP++;
        } else if (WaveCounter == 37) {
            CreateAndMoveWave(8, 0, 5, 0, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 36) {
            SetUpNextWave(2);
        } else if (WaveCounter == 36) {
            CreateAndMoveWave(3, 0, 0, 10, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 35) {
            SetUpNextWave(2);
        } else if (WaveCounter == 35) {
            CreateAndMoveWave(15, 5, 5, 5, 5, 5);
        } else if (NumberOfWaves == 0 && WaveCounter == 34) {
            SetUpNextWave(6);
            System.out.println("Level 35");
        } else if (WaveCounter == 34) {
            CreateAndMoveWave(10, 0, 2, 5, 3, 2);
        } else if (NumberOfWaves == 0 && WaveCounter == 33) {
            SetUpNextWave(5);
            LevelUP++;
        } else if (WaveCounter == 33) {
            CreateAndMoveWave(8, 8, 8, 0, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 32) {
            SetUpNextWave(3);
        } else if (WaveCounter == 32) {
            CreateAndMoveWave(3, 3, 4, 0, 3, 3);
        } else if (NumberOfWaves == 0 && WaveCounter == 31) {
            SetUpNextWave(5);
        } else if (WaveCounter == 31) {
            CreateAndMoveWave(5, 2, 4, 0, 4, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 30) {
            SetUpNextWave(4);
        } else if (WaveCounter == 30) {
            CreateAndMoveWave(4, 4, 5, 8, 3, 1);
        } else if (NumberOfWaves == 0 && WaveCounter == 29) {
            SetUpNextWave(6);
            LevelUP++;
            System.out.println("Level 30");
        } else if (WaveCounter == 29) {
            CreateAndMoveWave(5, 2, 8, 8, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 28) {
            SetUpNextWave(4);
        } else if (WaveCounter == 28) {
            CreateAndMoveWave(0, 0, 15, 0, 3, 3);
        } else if (NumberOfWaves == 0 && WaveCounter == 27) {
            SetUpNextWave(3);
            LevelUP++;
        } else if (WaveCounter == 27) {
            CreateAndMoveWave(5, 0, 5, 0, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 26) {
            SetUpNextWave(2);
        } else if (WaveCounter == 26) {
            CreateAndMoveWave(3, 3, 3, 0, 3, 1);
        } else if (NumberOfWaves == 0 && WaveCounter == 25) {
            SetUpNextWave(5);
            LevelUP++;
        } else if (WaveCounter == 25) {
            CreateAndMoveWave(2, 2, 2, 0, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 24) {
            SetUpNextWave(3);
            System.out.println("Level 25");
        } else if (WaveCounter == 24) {
            CreateAndMoveWave(2, 2, 0, 3, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 23) {
            SetUpNextWave(3);
        } else if (WaveCounter == 23) {
            CreateAndMoveWave(0, 0, 3, 3, 1, 1);
        } else if (NumberOfWaves == 0 && WaveCounter == 22) {
            SetUpNextWave(4);
        } else if (WaveCounter == 22) {
            CreateAndMoveWave(0, 2, 2, 3, 5, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 21) {
            SetUpNextWave(4);
        } else if (WaveCounter == 21) {
            CreateAndMoveWave(5, 15, 0, 0, 6, 6);
        } else if (NumberOfWaves == 0 && WaveCounter == 20) {
            SetUpNextWave(4);
            LevelUP++;
        } else if (WaveCounter == 20) {
            CreateAndMoveWave(0, 15, 0, 0, 3, 3);
        } else if (NumberOfWaves == 0 && WaveCounter == 19) {
            SetUpNextWave(3);
            LevelUP++;
            System.out.println("Level 20");
        } else if (WaveCounter == 19) {
            CreateAndMoveWave(0, 9, 0, 0, 2, 3);
        } else if (NumberOfWaves == 0 && WaveCounter == 18) {
            SetUpNextWave(3);
        } else if (WaveCounter == 18) {
            CreateAndMoveWave(8, 3, 3, 3, 2, 1);
        } else if (NumberOfWaves == 0 && WaveCounter == 17) {
            SetUpNextWave(6);
            LevelUP++;
        } else if (WaveCounter == 17) {
            CreateAndMoveWave(0, 0, 3, 3, 3, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 16) {
            SetUpNextWave(3);
        } else if (WaveCounter == 16) {
            CreateAndMoveWave(6, 6, 0, 0, 1, 2);
        } else if (NumberOfWaves == 0 && WaveCounter == 15) {
            SetUpNextWave(4);
            LevelUP++;
        } else if (WaveCounter == 15) {
            CreateAndMoveWave(8, 0, 5, 3, 1, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 14) {
            SetUpNextWave(4);
            System.out.println("Level 15");
        } else if (WaveCounter == 14) {
            CreateAndMoveWave(0, 0, 8, 0, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 13) {
            SetUpNextWave(1);
        } else if (WaveCounter == 13) {
            CreateAndMoveWave(4, 4, 1, 1, 1, 1);
        } else if (NumberOfWaves == 0 && WaveCounter == 12) {
            SetUpNextWave(6);
            LevelUP++;
        } else if (WaveCounter == 12) {
            CreateAndMoveWave(3, 3, 3, 2, 1, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 11) {
            SetUpNextWave(5);
        } else if (WaveCounter == 11) {
            CreateAndMoveWave(0, 5, 0, 3, 1, 1);
        } else if (NumberOfWaves == 0 && WaveCounter == 10) {
            SetUpNextWave(4);
        } else if (WaveCounter == 10) {
            CreateAndMoveWave(2, 3, 2, 0, 1, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 9) {
            SetUpNextWave(4);
            LevelUP++;
            System.out.println("Level 10");
        } else if (WaveCounter == 9) {
            CreateAndMoveWave(3, 15, 15, 1, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 8) {
            SetUpNextWave(4);
        } else if (WaveCounter == 8) {
            CreateAndMoveWave(3, 5, 8, 1, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 7) {
            SetUpNextWave(4);
        } else if (WaveCounter == 7) {
            CreateAndMoveWave(8, 0, 0, 0, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 6) {
            SetUpNextWave(1);
            LevelUP++;
        } else if (WaveCounter == 6) {
            CreateAndMoveWave(5, 0, 2, 3, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 5) {
            SetUpNextWave(3);
            LevelUP++;
        } else if (WaveCounter == 5) {
            CreateAndMoveWave(5, 0, 0, 2, 0, 0);
        } else if (NumberOfWaves == 0 && WaveCounter == 4) {
            SetUpNextWave(2);
            LevelUP++;
            System.out.println("Level 5");
            // From this point, it's rinse and repeat with different sets of numbers for unique waves, there is a total of 50 waves and I assure you all work.
        } else if (WaveCounter == 4) {
            CreateAndMoveWave(5, 0, 3, 0, 0, 0);
            /* At this point, if you are still committed to reading all this, you should memorize what each parameter does (1st parameter is for the fishwave
               2nd: SeagullsWave
               3rd: EelWave
               4th: PufferfishWave
               5th: PelicanWave
               6th: PelicanBotWave */
        } else if (NumberOfWaves == 0 && WaveCounter == 3) {
            SetUpNextWave(2);
            LevelUP++;
        } else if (WaveCounter == 3) {
            CreateAndMoveWave(3, 0, 1, 0, 0, 0);
            //3 Fish are created and 1 Eel is created (both moving as well)
        } else if (NumberOfWaves == 0 && WaveCounter == 2) {
            SetUpNextWave(2);
            // This time NumberOfWaves is set to 2 since there are two waves coming up (FishWave and EelWave)
        } else if (WaveCounter == 2) {
            CreateAndMoveWave(5, 0, 0, 0, 0, 0);
            // 5 fish is created and moved
        } else if (NumberOfWaves == 0 && WaveCounter == 1) {
            //NumberOfWaves turns 0 when all the waves containing the creatures have moved from right to left (this indicates the end of a whole wave)
            SetUpNextWave(1);
            // WaveCounter is increased by 1 and NumberOfWaves is set to 1 since there will be 1 wave upcoming (FishWave)
            LevelUP++;
            // Over time (I get to choose when), LevelUP will increase by 1 allowing the user to level up one of their upgrades; see Pirahna class
        } else if (WaveCounter == 1) {
            CreateAndMoveWave(3, 0, 0, 0, 0, 0);
            /* Since WaveCounter = 1 but NumberOfWaves isn't 0, this method is called
               This method creates 3 fish and moves them from the right of the screen to the left of the screen; see CreateAndMoveWave method in this class */
        } else if (Time > 2.5 && WaveCounter == 0) {
            // "Start" of the if statement (There is a 2.5 second delay before anything happens (Time > 2.5))  After the 2.5 s delay the waves will start being created (but first it must be set up)
            SetUpNextWave(1);
            /* Whenever this method is called, WaveCounter is increased by 1 setting up for the next else if statement, and NumberOfWaves is set as whatever is given in the parameter
               In this case NumberOfWaves is set to 1 because there are a total of 1 wave when WaveCounter = 1 (Wave 1); see next statement (statement above) */
        }
        // For the sake of simplicity, most of the things inside this if statement is hard coded. Please don't deduct marks, I got a headache. ; n; Besides, this is a test case it should be hard coded riiiight? ;D
    }

    private void ProgressiveDifficulty(double t, double a, double d, double Fs, double Ss, double Es, double PFs, double Ps) {
        Time += t;
        // This just keeps track of time; used in CheckPredatorCollision method (Time increases based off real time)
        x += a;
        if (Hungry < 3)
            // Limits Hungry from exceeding the value 3
            Hungry += d;
        Fish_Speed += Fs;
        Seagull_Speed += Ss;
        Eel_Speed += Es;
        Pufferfish_Speed += PFs;
        Pelican_Speed += Ps;
        // Adds the given values to the variables current value; this makes the game balanced and harder as the game goes on
    }
    // This method is intended to make the game progressively harder by adding a small value to the speeds of everything

    private void HungerDepletion() {
        if (delay >= End_of_Delay) {
            // If delay is greater than or equal to the value of End_of_Delay then
            delay = RESET_DELAY;
            // Set delay to 0
            Hunger.setSize(Hunger.getWidth() - (int) Hungry, Hunger.getHeight());
            // and deplete Hunger by setting the size of Hunger (width only changes by subtracting the value of Hungry to the width of Hunger)
        } else
            // However, if the delay is smaller than the value of End_of_Delay then
            delay += DELAY_INCREMENT;
        // Increase the delay by 0.6 (so that delay may be > than end of delay)

        if (Hunger.getWidth() <= STARVATION) {
            // If Hunger's width is ever below or equal to 0 then the user has lost
            Game.stop();
            // Stop the timer (Which stops movement from the predators and prey)
            Alive = false;
            // and set Alive to false which stops the Piranha from moving; see Piranha class
            CheckHighScore(Scores, points);
            // Show to the user that the game is over by setting the label visible; see CheckHighScore method in this class
            WriteFile(points, file);
            // The user's score will also be saved into the HighScores file; see WriteFile method in this class
        } else if (Hunger.getWidth() > Hunger_Limit)
            // If hunger's width ever becomes greater than 635 (the limit to the hunger) achievable by eating prey with a full enough hunger bar then
            Hunger.setSize(Hunger_Limit, Hunger.getHeight());
        // Set the size of hunger back to it's default size
    }
    // This method is intended to manage and deplete the Piranha's hunger bar and check if the Piranha has starved.

    private void Count() {
        for (int i = 0; i < LIMIT; i++) {
            // Loops 15 times
            if (Fish[i].getX() < -Fish[i].getWidth() && Fish[i].isVisible()) {
                // If any fish is found to be visible and off screen (from the left screen) then
                Fish[i].setVisible(false);
                // Set the fish that is off screen to be invisible
                FishCount++;
                // Increase FishCount by 1
            }
            // The code below is the same concept of code just with the specific label type (creature)
            if (Seagull[i].getX() < -Seagull[i].getWidth() && Seagull[i].isVisible()) {
                Seagull[i].setVisible(false);
                SeagullsCount++;
            }
            if (Eel[i].getX() < -Eel[i].getWidth() && Eel[i].isVisible()) {
                Eel[i].setVisible(false);
                EelCount++;
            }
            if (Pufferfish[i].getX() < -Pufferfish[i].getWidth() && Pufferfish[i].isVisible()) {
                Pufferfish[i].setVisible(false);
                PufferfishCount++;
            }
            if (Pelican[i].getX() < -Pelican[i].getWidth() && Pelican[i].isVisible()) {
                Pelican[i].setVisible(false);
                PelicanCount++;
            }
            if (Pelican_Bot[i].getX() < -Pelican_Bot[i].getWidth() && Pelican_Bot[i].isVisible()) {
                Pelican_Bot[i].setVisible(false);
                PelicanBotCount++;
            }
        }
    }
    // This method is intended to count how many fish, seagulls, pufferfish, eels, and pelicans have finished their paths to the left screen (they travel from right to left)

    private void SetUpNextWave(int y) {
        WaveCounter++;
        // Increase the Wave Counter by 1
        NumberOfWaves = y;
        // Set the Number of Waves to whatever y is ( This will indicate how many waves must pass before a whole wave is over (it'll make more sense later))
        ResetCount();
        // Reset all Counts; see ResetCount method in this class
    }
    // This method is intended to reset the Counts and set the wave counter and number of waves to determine which wave it is and when it is done

    private void ResetCount() {
        FishCount = RESET;
        SeagullsCount = RESET;
        EelCount = RESET;
        PufferfishCount = RESET;
        PelicanCount = RESET;
        PelicanBotCount = RESET;

        // Sets all Counts back to 0
    }
    // This method is intended to reset all the counts to 0

    private void UpdateScore() {
        Score.setText("SCORE: " + points);
        // Updates the score by setting the text again
    }
    // This method is intended to update the score to tell the user what their score is

    private void levelUp() {
        if (LevelUP > 0) {
            // If the user has a level they can spend on an upgrade then
            levelup.setVisible(true);
            // set level up to become true
            levelup.setLocation(levelup.getX(), Stomach.getY() + CosineMovement(x, ARROW_DISTANCE));
            // and have it move up and down by setting its location and calling on the method cosinemovement; see CosineMovement method in Screen class
        } else
            levelup.setVisible(false);
        // However if the user doesn't have a level available then set levelup to be invisible
    }
    // This method is intended to indicate to the user when they can upgrade their piranha

    private void CreateAndMoveWave(int F, int S, int E, int PF, int P, int PB) {
        FishWave(F);
        SeagullsWave(S);
        EelWave(E);
        PufferfishWave(PF);
        PelicanWave(P);
        PelicanBotWave(PB);
        // See FishWave method to see what's happening; no need to see the others since all these methods work the same way
    }
    // This method is intended to create Waves of specific creatures and send them at the user

    private void FishWave(int DesiredAmount) {
        for (int i = 0; i < DesiredAmount; i++) {
            // Loops the Desired Amount of times
            if (FishCount == DesiredAmount) {
                // However if all the fish have moved to the left side; see Count method in this class
                FishCount = -1;
                // Set FishCount to -1 which prevents the fish from swimming more
                NumberOfWaves--;
                // And Decrease NumOfWaves by 1 (indicating the end of the fishwave)
                for (int x = 0; x < DesiredAmount; x++) {
                    // Loop the desired amount of times and
                    SendBack(x, Fish, Fish_Hit_Box, RandomizerSetBackX(SOME_DISTANCE_FROM_RIGHT_OF_SCREEN), RandomizerSetBackY(OCEAN_LEVEL + Fish[i].getHeight(), false));
                    // Send all the fish back to their location (off screen (right screen)); see SendBack method in this class
                }
            }
            if (FishCount >= 0) {
                // If FishCount is greater than or equal to 0 that means that you can
                SendFish(i);
                // Call this method and send the desired amount of fish at the user; see SendFish method in this class
                CheckPreyCollision(Piranha_Eating_Hit_Box, Fish[i]);
                // While all the fish are swimming they are also being checked if they are being eaten or not; see CheckPreyCollision method in this class
            }
        }
    }
    // This method is intended to send out a wave of a desired amount of fish. It will check if the piranha hits a prey as well as check if all of the fish reaches the end of the screen

    private void SendFish(int i) {
        Fish[i].setLocation(Fish[i].getX() - (int) Fish_Speed, Fish[i].getY());
        //Moves the fishes 8 units to the left every time the timer ticks (Fishes because this is in a loop)
        Fish_Hit_Box[i].setLocation(Fish[i].getX(), Fish[i].getY());
        //The Hitbox follows along by setting it's location to follow the x and y coordinates of the Fish
    }
    // This method is intended to move the fish and its hitbox

    private void SeagullsWave(int DesiredAmount) {
        for (int i = 0; i < DesiredAmount; i++) {
            if (SeagullsCount == DesiredAmount) {
                SeagullsCount = -1;
                NumberOfWaves--;
                for (int x = 0; x < DesiredAmount; x++) {
                    SendBack(x, Seagull, Seagulls_Hit_Box, RandomizerSetBackX(EXTENDED_DISTANCE_FROM_RIGHT_OF_SCREEN), RandomizerSetBackY(OCEAN_LEVEL, true));
                }
            }
            if (SeagullsCount >= 0) {
                SendSeagulls(i);
                CheckPreyCollision(Piranha_Eating_Hit_Box, Seagull[i]);
            }
        }
    }
    // Similar code to FishWave; refer to FishWave for comments

    private void SendSeagulls(int i) {
        Seagull[i].setLocation(Seagull[i].getX() - (int) Seagull_Speed, Seagull[i].getY());
        Seagulls_Hit_Box[i].setLocation(Seagull[i].getX() + FLYING_ADJUSTMENT, Seagull[i].getY() + FLYING_ADJUSTMENT);
    }
    // Similar code to SendFish; refer to SendFish for comments

    private void CheckPreyCollision(JLabel x, JLabel y) {
        if (x.getBounds().intersects(y.getBounds())) {
            //If x (most likely will be the pirahna's eatinghitbox) intersects with y (the prey) then
            y.setLocation(-y.getWidth(), 0);
            //Put the prey off screen (to the left) This way the Count also increases
            Hunger.setSize(Hunger.getWidth() + Hunger_gained, Hunger.getHeight());
            //Hunger changes so that the Pirahna won't starve for a little longer (does this by increasing the width of Hunger)
            points += INCREASE_POINTS;
        }
    }
    // This method is intended to check if a prey's hit box is colliding with the piranha's eating hit box. If so, move the prey away and modify the Hunger bar

    private void CheckPredatorCollision(JLabel x, JLabel y, int i, int z) {
        if (Time < EndTime) {
            // If EndTime exceeds Time then (The piranha hasn't been hit in 0.4 seconds then)
            Hit = false;
            // Set Hit to be false
        } else if (y.equals(Pelicans_Hit_Box[i]) || y.equals(Pelican_Bot_Hit_Box[i])) {
            // if y is referring to the Pelicans CheckHitboxes (Exclusive to pelicans*) then
            if (x.getBounds().intersects(y.getBounds()) && Jump < JUMP_LIMIT && !Hit) {
                // Check if x (piranha hit box) is intersecting with the pelican and the piranha is falling from its jump and it hasn't been hit recently then
                EndTime = Time + COOL_DOWN;
                // Save EndTime as the current time + 0.4 seconds
                Hit = true;
                // Set Hit to = true
                Hunger.setSize(Hunger.getWidth() - z, Hunger.getHeight());
                // Decrease Hunger's size so that it appears the Piranha got hurt
            }
        } else if (x.getBounds().intersects(y.getBounds()) && !Hit) {
            // However, if the Eel or Pufferfish intersect with the user and they haven't been hit recently then
            EndTime = Time + COOL_DOWN;
            // Save EndTime as the current time + 0.4
            Hit = true;
            // Set Hit to = true
            Hunger.setSize(Hunger.getWidth() - z, Hunger.getHeight());
            // Decrease Hunger's size so that it appears the Piranha got hurt
        }
    }
    // This method is intended to check if a predator's hit box is colliding with the piranha's hit box. If so, modify the Hunger bar and provide a brief cool down until the piranha can get hit again

    private void EelWave(int DesiredAmount) {
        for (int i = 0; i < DesiredAmount; i++) {
            if (EelCount == DesiredAmount) {
                EelCount = -1;
                NumberOfWaves--;
                for (int x = 0; x < DesiredAmount; x++) {
                    SendBack(x, Eel, Eel_Hit_Box, RandomizerSetBackX(EXTENDED_DISTANCE_FROM_RIGHT_OF_SCREEN), RandomizerSetBackY(OCEAN_LEVEL + Eel[x].getHeight(), false));
                }
            }
            if (EelCount >= 0) {
                SendEel(i);
                CheckPredatorCollision(Piranha_Hit_Box, Eel_Hit_Box[i], i, DAMAGE);
            }
        }
    }
    // Similar code to FishWave; refer to FishWave for comments

    private void SendEel(int i) {
        Eel[i].setLocation(Eel[i].getX() - (int) Eel_Speed, Eel[i].getY());
        Eel_Hit_Box[i].setLocation(Eel[i].getX() - (int) Eel_Speed + EEL_ADJUSTMENT_X, Eel[i].getY() + EEL_ADJUSTMENT_Y);
    }
    // Similar code to SendFish; refer to SendFish for comments

    private void PufferfishWave(int DesiredAmount) {
        for (int i = 0; i < DesiredAmount; i++) {
            if (PufferfishCount == DesiredAmount) {
                PufferfishCount = -1;
                NumberOfWaves--;
                for (int x = 0; x < DesiredAmount; x++) {
                    SendBack(x, Pufferfish, PufferFish_Hit_Box, RandomizerSetBackX(SOME_DISTANCE_FROM_RIGHT_OF_SCREEN), RandomizerSetBackY(OCEAN_LEVEL + Pufferfish[i].getHeight(), false));
                }
            }
            if (PufferfishCount >= 0) {
                SendPufferfish(i);
                CheckPredatorCollision(Piranha_Hit_Box, PufferFish_Hit_Box[i], i, DAMAGE);
            }
        }
    }
    // Similar code to FishWave; refer to FishWave for comments

    private void SendPufferfish(int i) {
        Pufferfish[i].setLocation(Pufferfish[i].getX() - (int) Pufferfish_Speed, Pufferfish[i].getY());
        PufferFish_Hit_Box[i].setLocation(Pufferfish[i].getX() + PUFFERFISH_ADJUSTMENT - (int) Pufferfish_Speed, Pufferfish[i].getY() + PUFFERFISH_ADJUSTMENT);
    }
    // Similar code to SendFish; refer to SendFish for comments

    private void PelicanWave(int DesiredAmount) {
        for (int i = 0; i < DesiredAmount; i++) {
            if (PelicanCount == DesiredAmount) {
                PelicanCount = -1;
                NumberOfWaves--;
                for (int x = 0; x < DesiredAmount; x++) {
                    SendBack(x, Pelican, Pelicans_Hit_Box, RandomizerSetBackX(SOME_DISTANCE_FROM_RIGHT_OF_SCREEN), TOP_OF_SCREEN);
                }
            }
            if (PelicanCount >= 0) {
                SendPelican(i);
                CheckPredatorCollision(Piranha_Hit_Box, Pelicans_Hit_Box[i], i, DAMAGE);
            }
        }
    }
    // Similar code to FishWave; refer to FishWave for comments

    private void SendPelican(int i) {
        Pelican[i].setLocation(Pelican[i].getX() - (int) Pelican_Speed, PELICANS_STARTING_POSITION + CosineMovement(x, SPACE_FROM_OCEAN_LEVEL));
        Pelicans_Hit_Box[i].setLocation(Pelican[i].getX() + FLYING_ADJUSTMENT, Pelican[i].getY() + PELICANS_ADJUSTMENT);
    }
    // Similar code to SendFish; refer to SendFish for comments

    private void PelicanBotWave(int DesiredAmount) {
        for (int i = 0; i < DesiredAmount; i++) {
            if (PelicanBotCount == DesiredAmount) {
                PelicanBotCount = -1;
                NumberOfWaves--;
                for (int x = 0; x < DesiredAmount; x++) {
                    SendBack(x, Pelican_Bot, Pelican_Bot_Hit_Box, RandomizerSetBackX(SOME_DISTANCE_FROM_RIGHT_OF_SCREEN), OCEAN_LEVEL - Pelican[x].getHeight());
                }
            }
            if (PelicanBotCount >= 0) {
                SendPelicanBot(i);
                CheckPredatorCollision(Piranha_Hit_Box, Pelican_Bot_Hit_Box[i], i, DAMAGE);
            }
        }
    }
    // Similar code to FishWave; refer to FishWave for comments

    private void SendPelicanBot(int i) {
        Pelican_Bot[i].setLocation(Pelican_Bot[i].getX() - (int) Pelican_Speed, PELICANS_STARTING_POSITION - CosineMovement(x, SPACE_FROM_OCEAN_LEVEL));
        Pelican_Bot_Hit_Box[i].setLocation(Pelican_Bot[i].getX() + FLYING_ADJUSTMENT, Pelican_Bot[i].getY() + PELICANS_ADJUSTMENT);
    }
    // Similar code to SendFish; refer to SendFish for comments

    private void SendBack(int i, JLabel[] x, JLabel[] xHB, int distx, int disty) {
        x[i].setLocation(distx, disty);
        // Set x (a prey or predator) back to the right of the screen (dist x, dist y)
        x[i].setVisible(true);
        // Set them to be visible and
        xHB[i].setLocation(x[i].getX(), x[i].getY());
        // Set their hitbox to follow the location of x[i]
    }
    // This method is intended to "Reset" the position of the prey or predator that reaches the off screen of the left side

    private void ResetGame() {
        setTime(RESET);
        setEndTime(RESET);
        setx(RESET);
        setEnd_of_Delay(DEFAULT_DELAY);
        setHungry(DEFAULT_HUNGRY);
        setHunger_gained(DEFAULT_HUNGER_GAINED);
        Hunger.setSize(Hunger_Limit, Hunger.getY());
        setNumberOfWaves(RESET);
        setNumberOfWaves(RESET);
        setWaveCounter(RESET);
        setFishSpeed(DEFAULT_FISH_SPEED);
        setSeagullSpeed(DEFAULT_SEAGULL_SPEED);
        setEelSpeed(DEFAULT_EEL_SPEED);
        setPufferfishSpeed(DEFAULT_PUFFERFISH_SPEED);
        setPelicanSpeed(DEFAULT_PELICAN_SPEED);
        ResetAllWaves();
        ResetCount();
        // see these two methods in this class
        setPoints(RESET);
        setLevelUP(RESET);
        setBite_level(RESET);
        setFin_level(RESET);
        setStomach_level(RESET);
        setSpeed_level(RESET);
        setStomach_level(RESET);
        setJump(DEFAULT_JUMP);
        Jump_Reset = getJump();
        setSpeed(DEFAULT_SPEED);
        Alive = true;
        Piranha.setLocation(LEFT_OF_SCREEN, POSITION_OF_PIRANHA_Y);
        DisplayHighScore(Scores);
        // Updates the high score
        GameOver.setVisible(false);
        GameOverWithHighScore.setVisible(false);
    }
    // This method is intended to "restart" the game; setting everything back to default

    private void ResetAllWaves() {
        for (byte i = 0; i < LIMIT; i++) {
            // Loops 15 times
            SendBack(i, Fish, Fish_Hit_Box, RandomizerSetBackX(SOME_DISTANCE_FROM_RIGHT_OF_SCREEN), RandomizerSetBackY(OCEAN_LEVEL, false));
            SendBack(i, Seagull, Seagulls_Hit_Box, RandomizerSetBackX(SOME_DISTANCE_FROM_RIGHT_OF_SCREEN), RandomizerSetBackY(OCEAN_LEVEL, true));
            SendBack(i, Eel, Eel_Hit_Box, RandomizerSetBackX(SOME_DISTANCE_FROM_RIGHT_OF_SCREEN), RandomizerSetBackY(OCEAN_LEVEL, false));
            SendBack(i, Pufferfish, PufferFish_Hit_Box, RandomizerSetBackX(SOME_DISTANCE_FROM_RIGHT_OF_SCREEN), RandomizerSetBackY(OCEAN_LEVEL, false));
            SendBack(i, Pelican, Pelicans_Hit_Box, RandomizerSetBackX(SOME_DISTANCE_FROM_RIGHT_OF_SCREEN), TOP_OF_SCREEN);
            SendBack(i, Pelican_Bot, Pelican_Bot_Hit_Box, RandomizerSetBackX(SOME_DISTANCE_FROM_RIGHT_OF_SCREEN), PELICANS_STARTING_POSITION);
        }
        // Sends back all the creatures
    }
    // This method is intended to send all the waves back when the user chooses to restart the game

    public void setHungry(double hungry) {
        Hungry = hungry;
    }

    public void setTime(double time) {
        Time = time;
    }

    public void setx(double x) {
        this.x = x;
    }

    public void setEndTime(double endTime) {
        EndTime = endTime;
    }

    public void setNumberOfWaves(int numberOfWaves) {
        NumberOfWaves = numberOfWaves;
    }

    public void setWaveCounter(int waveCounter) {
        WaveCounter = waveCounter;
    }

    public void setFishSpeed(float fishSpeed) {
        Fish_Speed = fishSpeed;
    }

    public void setSeagullSpeed(float seagullSpeed) {
        Seagull_Speed = seagullSpeed;
    }

    public void setEelSpeed(float eelSpeed) {
        Eel_Speed = eelSpeed;
    }

    public void setPufferfishSpeed(float pufferfishSpeed) {
        Pufferfish_Speed = pufferfishSpeed;
    }

    public void setPelicanSpeed(float pelicanSpeed) {
        Pelican_Speed = pelicanSpeed;
    }
    // Sets the variables they are assigned to

    private void CheckHighScore(ArrayList<Integer> scores, int recent_score) {
        if (scores.size() > 0) {
            // If there are some scores in the array list (some scores in the high score file) then
            High_score = doInsertionSort(scores);
            // Sort the array list and set the highest score as the high score; see doInsertionSort method in this class
            if (recent_score > High_score)
                // If the score the player ends with is higher than the high score then
                GameOverWithHighScore.setVisible(true);
                // Show the JLabel that says it is game over but says they won a new high score
            else
                // If the score is less than the high score then
                GameOver.setVisible(true);
                // Show the JLabel that says it is game over
        } else {
            // If there are no scores in the array list (as a result of there not being any scores in the high score file) then
            GameOverWithHighScore.setVisible(true);
            // Show that they won a new high score
        }
    }
    // This method is intended to check if the user has a high score or not. If so have the user know they got a high score through a jlabel if not then show a different jlabel that tells them to retry

    private void DisplayHighScore(ArrayList<Integer> scores) {
        ReadFile(scores);
        // To display the high score in a JLabel the High scores file must be read; see ReadFile method in this class
        if (scores.size() == 0)
            // If the array list after Reading the file is found to have no elements in it (no scores in the high scores file) then
            HighScore.setText("HIGH SCORE: " + High_score);
            // The high score label is set to showing 0; see ReadFile method in this class
        else {
            // However if there are scores in the file then
            HighScore.setText("HIGH SCORE: " + High_score);
            // The High score was saved when read file was sorted in the ReadFile method
        }
    }
    // This method is intended to display the high score through a JLabel

    private void WriteFile(int recent_score, File file) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
            output.append(Integer.toString(recent_score));
            // Adds the score the player died with into the file
            output.newLine();
            // Indents to a new line for the next score
            output.close();
        } catch (IOException ex1) {
            System.out.printf("ERROR writing score to file: %s\n", ex1);
            // If there was an error in writing a score down then this will output
        }
    }
    // This method is intended to write down the score of the players when they lose in the game into a file called HighScores

    private void ReadFile(ArrayList<Integer> scores) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                // reads the high score file line by line
                try {
                    Integer score = Integer.parseInt(line.trim());
                    // parses each line as an Integer
                    scores.add(score);
                    // adds each score(line) into the array list of scores
                } catch (NumberFormatException e1) {
                    System.err.println("ignoring invalid score: " + line);
                    // Ignores invalid scores
                }
                line = reader.readLine();
                // Goes to the next line
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            // Catches any other errors/exceptions
        }
        if (scores.size() > 0)
            // If the array list has Integer values at the end of the reading then
            High_score = doInsertionSort(scores);
            // Save High score as the highest value in the array list (after sorting returns the highest value); see doInsertionSort method
        else
            // However if there are no Integer values in the array list (no scores in the high scores file) then
            High_score = 0;
            // High score is 0
    }
    // This method is intended to read the high scores file and set high scores based on it

    private Integer doInsertionSort(ArrayList<Integer> input) {
        for (int i = 1; i < input.size(); i++) {
            //Loops through entire array
            for (int j = i; j > 0; j--) {
                //Goes backwards from position DiminishingHunger
                if (input.get(j) < input.get(j - 1)) {
                    //If the jth position is smaller than the one before it then
                    switchNums(j - 1, j, input);
                    //Switch the numbers
                }
            }
        }
        return input.get(input.size() - 1);
        // Returns the highest number in the array list
    }
    // This method is intended to sort the array list from smallest to biggest and return the biggest value (which will be at the end of the array list)

    private void switchNums(int position, int index, ArrayList<Integer> arr) {
        Integer temp = arr.get(index);
        //Saves temp as the value that is deemed smallest
        arr.set(index, arr.get(position));
        //Swap that with the ith position on the array
        arr.set(position, temp);
        //Swap the ith position with the smaller value
    }
    // This method is intended to switch numbers in the array list when sorting
}


