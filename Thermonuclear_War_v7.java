import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;

public class Thermonuclear_War_v7 extends JFrame {
    // Constants
    static final String USER_PASSWORD_FILE = "user_password.txt";
    static final String ADMIN_PASSWORD_FILE = "admin_password.txt";
    static final String ENCRYPTION_KEY = "0123456789abcdef";  // 16-byte key for AES
    static final int MAX_WEAPONS = 3000;  // Max number of nuclear weapons a player can have
    static final int MISSILE_DAMAGE = 25;  // Percentage damage from each missile hit
    static final int NUM_CITIES = 19502;  // Number of cities per side
    static final double DEFENSE_CHANCE = 0.5;  // Chance of missile being intercepted by defense systems
    static final double WEATHER_EFFECT = 0.2;  // Chance that weather will impact missile damage or defenses

    private JTextArea taMessage;  // Used to display messages
    private int xPosition = 0;  // Initial position of the text for the transition
    private String currentMessage = "Opening Joshua... User or Admin Mode.";  // Initial message
    private Player player;
    private AI ai;
    private ArrayList<String> cities; // ArrayList to store cities

    public Thermonuclear_War_v7() {
        setTitle("W.O.P.R Joshua");
        setSize(500, 600);
        setMinimumSize(new Dimension(1000, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialize();
            }
        
            private void initialize() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'initialize'");
            }
        
            // Player's Attributes
    static class Player {
        String name;
        int weapons;
        int cities;
        double defense;
        int aggressionLevel;

        Player(String name) {
            this.name = name;
            this.weapons = MAX_WEAPONS;
            this.cities = NUM_CITIES;
            this.defense = 0.5;
            this.aggressionLevel = 0;
        }

        boolean isAlive() {
            return this.cities > 0;
        }

        void launchMissile(Player target, ArrayList<String> cities) {
            if (this.weapons > 0) {
                System.out.println(this.name + " launches a missile!");
                this.weapons--;
                int damage = new Random().nextInt(MISSILE_DAMAGE) + 1;
                target.cities -= damage;
                this.aggressionLevel++;
                System.out.println(this.name + " destroyed " + damage + "% of " + target.name + "'s cities.");

                // Select target city
                String[] cityArray = cities.toArray(new String[0]);
                String targetCity = (String) JOptionPane.showInputDialog(null,
                        "Choose a target city:",
                        "Missile Target",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        cityArray,
                        cityArray[0]);

                if (targetCity != null) {
                    System.out.println(this.name + " targets " + targetCity);
                    // Simulate the missile hit
                    if (new Random().nextBoolean()) {
                        System.out.println(targetCity + " has been hit!");
                    } else {
                        System.out.println(targetCity + " survived.");
                    }
                } else {
                    System.out.println("No city selected. Missile launch aborted.");
                }
            } else {
                System.out.println(this.name + " has no missiles left!");
            }
        }

        void defend() {
            System.out.println(this.name + " activates missile defense systems!");
            this.defense = 0.7;  // Increase defense for the turn
        }

        boolean negotiate(Player target) {
            System.out.println(this.name + " is trying to negotiate with " + target.name + "...");
            if (new Random().nextDouble() < 0.5) {
                System.out.println(this.name + " successfully convinced " + target.name + " to stop the missile strike!");
                return true;
            } else {
                System.out.println(this.name + "'s negotiations failed. " + target.name + " ignores them.");
                return false;
            }
        }

        void sabotage(Player target) {
            System.out.println(this.name + " attempts to sabotage " + target.name + "'s defense systems!");
            if (new Random().nextDouble() < 0.3) {
                target.defense -= 0.1;  // Reduce target defense
                System.out.println(target.name + "'s defense systems have been weakened!");
            } else {
                System.out.println(this.name + " failed to sabotage " + target.name + "'s defense systems.");
            }
        }
    }

    // AI's Attributes (Improvised for strategy)
    static class AI extends Player {

        AI(String name) {
            super(name);
        }

        void makeDecision(Player player, ArrayList<String> cities) {
            String action = null;
            if (this.weapons > 0 && this.cities > 0) {
                // AI decision-making based on aggression levels
                if (this.aggressionLevel > player.aggressionLevel) {
                    action = new Random().nextDouble() < 0.5 ? "launch" : (new Random().nextDouble() < 0.5 ? "defend" : "sabotage");
                } else {
                    action = new Random().nextDouble() < 0.6 ? "launch" : (new Random().nextDouble() < 0.3 ? "defend" : "negotiate");
                }

                switch (action) {
                    case "launch":
                        this.launchMissile(player, cities);
                        break;
                    case "defend":
                        this.defend();
                        break;
                    case "negotiate":
                        if (!player.negotiate(this)) {
                            this.launchMissile(player, cities);  // If negotiations fail, launch an attack
                        }
                        break;
                    case "sabotage":
                        this.sabotage(player);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    // Function to simulate random events
    static String randomEvent() {
        if (new Random().nextDouble() < WEATHER_EFFECT) {
            System.out.println("\nA sudden weather event disrupts the launch process!");
            return "weather";
        }
        return null;
    }

    // Function to simulate the game
    public static void playGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Expanded Thermonuclear War game!");
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        Player player = new Player(playerName);
        AI ai = new AI("AI");

        // Initialize cities
        ArrayList<String> cities = (ArrayList<String>) CityDatabase.getCities();  // Assuming CityDatabase is an external class providing the cities list

        boolean gameOver = false;

        while (!gameOver) {
            System.out.println("\n" + player.name + "'s Weapons: " + player.weapons + ", " + ai.name + "'s Weapons: " + ai.weapons);
            System.out.println(player.name + "'s Cities: " + player.cities + ", " + ai.name + "'s Cities: " + ai.cities);

            // Check if the player chooses W.O.P.R.
            System.out.println("\nChoose an option:");
            System.out.println("1. Launch a missile");
            System.out.println("2. Defend");
            System.out.println("3. Negotiate");
            System.out.println("4. W.O.P.R");
            System.out.print("Enter 1, 2, 3, or 4: ");
            String playerChoice = scanner.nextLine();

            switch (playerChoice) {
                case "1":
                    player.launchMissile(ai, cities);
                    break;
                case "2":
                    player.defend();
                    break;
                case "3":
                    if (!ai.negotiate(player)) {
                        player.launchMissile(ai, cities);
                    }
                    break;
                case "4":
                    System.out.println("\nThe only winning move is not to play.");
                    System.out.println("Game Over.");
                    return;
                default:
                    System.out.println("Invalid choice! Please select a valid option.");
            }

            // Random event (weather or sabotage)
            randomEvent();

            // AI's turn (Automated for simplicity)
            if (ai.isAlive()) {
                ai.makeDecision(player, cities);
            }

            // Handle missile defense: If the defense succeeds, reduce damage
            if (new Random().nextDouble() < player.defense) {
                System.out.println(player.name + "'s missile defense successfully intercepted the attack!");
                ai.cities += new Random().nextInt(5) + 1;  // Recover a bit of city damage
            }
            player.defense = 0.5;  // Reset the defense after the turn

            if (new Random().nextDouble() < ai.defense) {
                System.out.println(ai.name + "'s missile defense intercepted the attack!");
                player.cities += new Random().nextInt(5) + 1;  // Recover a bit of city damage
            }
            ai.defense = 0.5;  // Reset the defense after the turn

            // Check if game is over
            if (player.cities <= 0 || ai.cities <= 0) {
                gameOver = true;
                System.out.println("\nGame Over!");
                if (player.cities <= 0 && ai.cities <= 0) {
                    System.out.println("It's a draw! Both sides are destroyed.");
                } else if (player.cities <= 0) {
                    System.out.println(ai.name + " won! All of your cities were destroyed.");
                } else if (ai.cities <= 0) {
                    System.out.println(player.name + " won! All of " + ai.name + "'s cities were destroyed.");
                }
            }

            try {
                Thread.sleep(2000);  // Pause before the next round
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        playGame();
    }
}
