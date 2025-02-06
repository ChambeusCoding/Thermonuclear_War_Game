import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Thermonuclear_War_v4 extends JFrame {
    private JTextArea taMessage;
    private Player player;
    private AI ai;
    private ArrayList<String> cities;

    public Thermonuclear_War_v4() {
        setTitle("W.O.P.R Joshua");
        setSize(500, 600);
        setMinimumSize(new Dimension(1000, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialize();
    }

    public void initialize() {
        cities = new ArrayList<>();
        // Adding some example cities for the sake of simplicity
        cities.add("Adrian");
        cities.add("Alma");
        cities.add("Ann Arbor");
        cities.add("Battle Creek");
        cities.add("Detroit");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(139, 0, 0));

        taMessage = new JTextArea();
        taMessage.setFont(new Font("Segoe print", Font.BOLD, 18));
        taMessage.setEditable(false);
        taMessage.setText("Welcome to Thermonuclear War!");
        taMessage.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(taMessage);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton btnStartGame = new JButton("Start Game");
        btnStartGame.setFont(new Font("Segoe print", Font.BOLD, 18));
        btnStartGame.addActionListener(e -> startGame());

        buttonPanel.add(btnStartGame);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    private void startGame() {
        taMessage.setText("Starting Thermonuclear War game...\n");

        // Create Player and AI instances
        player = new Player("Player", taMessage);
        ai = new AI("AI", taMessage);

        // Start the game loop
        new Thread(() -> playGame()).start();
    }

    private void playGame() {
        Random random = new Random();
        boolean gameOver = false;

        while (!gameOver) {
            taMessage.append("\n" + player.name + "'s Weapons: " + player.weapons + ", " + ai.name + "'s Weapons: " + ai.weapons);
            taMessage.append("\n" + player.name + "'s Cities: " + player.cities + ", " + ai.name + "'s Cities: " + ai.cities);

            // Display available actions to the player
            String playerChoice = JOptionPane.showInputDialog(this,
                    "\nChoose an option:\n1. Launch a missile\n2. Defend\n3. Negotiate\n4. W.O.P.R");
            switch (playerChoice) {
                case "1":
                    launchMissile();
                    break;
                case "2":
                    player.defend();
                    break;
                case "3":
                    if (!ai.negotiate(ai)) {
                        player.launchMissile(ai);
                    }
                    break;
                case "4":
                    taMessage.append("\nThe only winning move is not to play.");
                    taMessage.append("\nGame Over.");
                    return;
                default:
                    taMessage.append("Invalid choice! Please select a valid option.");
            }

            // Random event (weather or sabotage)
            randomEvent();

            // AI's turn
            if (ai.isAlive()) {
                ai.makeDecision(player);
            }

            // Check if game is over
            if (player.cities <= 0 || ai.cities <= 0) {
                gameOver = true;
                taMessage.append("\nGame Over!");
                if (player.cities <= 0 && ai.cities <= 0) {
                    taMessage.append("It's a draw! Both sides are destroyed.");
                } else if (player.cities <= 0) {
                    taMessage.append(ai.name + " won! All of your cities were destroyed.");
                } else if (ai.cities <= 0) {
                    taMessage.append(player.name + " won! All of " + ai.name + "'s cities were destroyed.");
                }
            }

            try {
                Thread.sleep(2000);  // Pause before the next round
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void launchMissile() {
        taMessage.append("Missile launched! Choose a city to target.\n");

        String[] cityArray = cities.toArray(new String[0]);  // Convert ArrayList to array
        String targetCity = (String) JOptionPane.showInputDialog(this,
                "Choose a target city:",
                "Missile Target",
                JOptionPane.QUESTION_MESSAGE,
                null,
                cityArray,
                cityArray[0]);

        if (targetCity != null) {
            taMessage.append("Missile heading to " + targetCity + ".\n");
            // Simulate missile hit or miss (random outcome for simplicity)
            if (new Random().nextBoolean()) {
                taMessage.append(targetCity + " has been hit!\n");
                ai.cities -= 1;  // Decrease AI's city count
            } else {
                taMessage.append(targetCity + " has survived the attack.\n");
            }
        } else {
            taMessage.append("No city selected. Missile launch aborted.\n");
        }
    }

    private void randomEvent() {
        // Simulating random events like weather or sabotage (for now just random missile hits)
        Random random = new Random();
        if (random.nextDouble() < 0.2) {
            taMessage.append("\nA random event has occurred!\n");
            int eventEffect = random.nextInt(10) + 1;  // Random event that impacts cities
            taMessage.append("Both sides suffer a " + eventEffect + "% city damage.\n");
            player.cities -= eventEffect;
            ai.cities -= eventEffect;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Thermonuclear_War_v4());
    }
}

class Player {
    String name;
    int weapons;
    int cities;
    double defense;
    int aggressionLevel;
    private JTextArea taMessage;

    Player(String name, JTextArea taMessage) {
        this.name = name;
        this.weapons = 3000;
        this.cities = 5;  // Set initial city count to 5 for simplicity
        this.defense = 0.5;
        this.aggressionLevel = 0;
        this.taMessage = taMessage;
    }

    void launchMissile(AI target) {
        if (this.weapons > 0) {
            taMessage.append(this.name + " launches a missile!\n");
            this.weapons--;
            int damage = new Random().nextInt(25) + 1;
            target.cities -= damage;
            taMessage.append(this.name + " destroyed " + damage + "% of " + target.name + "'s cities.\n");
        } else {
            taMessage.append(this.name + " has no missiles left!\n");
        }
    }

    void defend() {
        taMessage.append(this.name + " activates missile defense systems!\n");
        this.defense = 0.7;  // Increase defense for the turn
    }

    boolean negotiate(AI target) {
        taMessage.append(this.name + " is trying to negotiate with " + target.name + "...\n");
        if (new Random().nextDouble() < 0.5) {
            taMessage.append(this.name + " successfully convinced " + target.name + " to stop the missile strike!\n");
            return true;
        } else {
            taMessage.append(this.name + "'s negotiations failed. " + target.name + " ignores them.\n");
            return false;
        }
    }

    boolean isAlive() {
        return this.cities > 0;
    }
}

class AI extends Player {
    AI(String name, JTextArea taMessage) {
        super(name, taMessage);
    }

    void makeDecision(Player player) {
        String action = null;
        if (this.weapons > 0 && this.cities > 0) {
            if (this.aggressionLevel > player.aggressionLevel) {
                action = new Random().nextDouble() < 0.5 ? "launch" : "defend";
            } else {
                action = new Random().nextDouble() < 0.6 ? "launch" : "negotiate";
            }

            // Execute the chosen action
            switch (action) {
                case "launch":
                    this.launchMissile((AI) player);
                    break;
                case "defend":
                    this.defend();
                    break;
                case "negotiate":
                    if (!player.negotiate(this)) {
                        this.launchMissile((AI) player);  // AI retaliates if negotiations fail
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
