import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;

public class Thermonuclear_War_v3 extends JFrame {
    // Constants for password management
    static final String USER_PASSWORD_FILE = "user_password.txt";
    static final String ADMIN_PASSWORD_FILE = "admin_password.txt";
    static final String ENCRYPTION_KEY = "0123456789abcdef";  // 16-byte key for AES
    static final int MAX_WEAPONS = 3000;  // Max number of nuclear weapons a player can have
    static final int MISSILE_DAMAGE = 25;  // Percentage damage from each missile hit
    static final int NUM_CITIES = 109;  // Number of cities per side
    static final double DEFENSE_CHANCE = 0.5;  // Chance of missile being intercepted by defense systems
    static final double WEATHER_EFFECT = 0.2;  // Chance that weather will impact missile damage or defenses

    private JTextArea taMessage;  // Used to display messages
    private int xPosition = 0;  // Initial position of the text for the transition
    private String currentMessage = "Opening Joshua... User or Admin Mode.";  // Initial message
    private Player player;
    private AI ai;

    // ArrayList to store cities
    private ArrayList<String> cities;

    public Thermonuclear_War_v3() {
        setTitle("W.O.P.R Joshua");
        setSize(500, 600);
        setMinimumSize(new Dimension(1000, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialize();
    }

    public void initialize() {
        // Initialize cities list with provided cities
   
        // cities = CityDatabase.getCities();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(139, 0, 0));

        taMessage = new JTextArea();
        taMessage.setFont(new Font("Segoe print", Font.BOLD, 18));
        taMessage.setEditable(false);
        taMessage.setText(currentMessage);
        taMessage.setOpaque(false);  // Make it transparent for smooth transitions
        JScrollPane scrollPane = new JScrollPane(taMessage);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton btnUserMode = new JButton("User");
        btnUserMode.setFont(new Font("Segoe print", Font.BOLD, 18));
        btnUserMode.addActionListener(e -> handleUserMode());

        JButton btnAdminMode = new JButton("Admin");
        btnAdminMode.setFont(new Font("Segoe print", Font.BOLD, 18));
        btnAdminMode.addActionListener(e -> handleAdminMode());

        buttonPanel.add(btnUserMode);
        buttonPanel.add(btnAdminMode);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        setVisible(true);

        // Start the text transition effect
        startTextTransition();
    }

    // Start the sliding text transition
    private void startTextTransition() {
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the position of the text
                xPosition += 2;
                if (xPosition > getWidth()) {
                    xPosition = -getFontMetrics(getFont()).stringWidth(currentMessage);  // Reset to left side
                }
                taMessage.repaint();  // Trigger a repaint to update the text position
            }
        });
        timer.start();
    }

    // Override paintComponent to draw the text at the updated position
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(new Font("Segoe print", Font.BOLD, 18));
        g.setColor(Color.WHITE);
        g.drawString(currentMessage, xPosition, 100);  // Draw text at the current position
    }

    // Handle User Mode
    private void handleUserMode() {
        taMessage.setText("Confirm to play Thermonuclear War\n");

        String userPassword = readUserPassword();
        if (userPassword == null) {
            String generatedPassword = generatePassword();
            taMessage.append("Generated Password: " + generatedPassword + "\n");
            storeUserPassword(generatedPassword);
        } else {
            String enteredPassword = JOptionPane.showInputDialog(this, "Enter password:");
            if (verifyUserPassword(enteredPassword)) {
                taMessage.append("Access granted... Accessing W.O.P.R\n");
            } else {
                taMessage.append("Incorrect password. Access denied.\n");
            }
        }
    }

    // Handle Admin Mode
    private void handleAdminMode() {
        taMessage.setText("Enter Admin to reset W.O.P.R.\n");

        String adminPassword = readAdminPassword();
        if (adminPassword == null) {
            String enteredPassword = JOptionPane.showInputDialog(this, "Set Admin:");
            storeAdminPassword(enteredPassword);
            taMessage.append("Admin password set successfully.\n");
        }

        String enteredAdminPassword = JOptionPane.showInputDialog(this, "Enter Admin password:");
        if (verifyAdminPassword(enteredAdminPassword)) {
            taMessage.append("Admin access granted... Accessing W.O.P.R\n");
            taMessage.append("Greetings Professor Falken... Shall we play a game?\n");
            resetUserPassword();
        } else {
            taMessage.append("Incorrect admin password.\n");
        }
    }

    // Generate a random password of 12 characters
    public static String generatePassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        for (int i = 0; i < 12; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }

    // Encrypt and store the user password
    public static void storeUserPassword(String password) {
        try {
            String encryptedPassword = encryptPassword(password);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_PASSWORD_FILE))) {
                writer.write(encryptedPassword);
            }
        } catch (Exception e) {
            System.err.println("Error storing user password: " + e.getMessage());
        }
    }

    // Encrypt the password using AES
    public static String encryptPassword(String password) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt the user password
    public static String decryptPassword(String encryptedPassword) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
        return new String(decryptedBytes);
    }

    // Verify the user password
    public static boolean verifyUserPassword(String enteredPassword) {
        try {
            String storedEncryptedPassword = readUserPassword();
            String decryptedPassword = decryptPassword(storedEncryptedPassword);
            return decryptedPassword.equals(enteredPassword);
        } catch (Exception e) {
            System.err.println("Error verifying user password: " + e.getMessage());
            return false;
        }
    }

    // Read the encrypted user password
    public static String readUserPassword() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_PASSWORD_FILE))) {
            return reader.readLine();
        } catch (IOException e) {
            return null;  // Password file doesn't exist
        }
    }

    // Store the admin password
    public static void storeAdminPassword(String password) {
        try {
            String encryptedPassword = encryptPassword(password);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADMIN_PASSWORD_FILE))) {
                writer.write(encryptedPassword);
            }
        } catch (Exception e) {
            System.err.println("Error storing admin password: " + e.getMessage());
        }
    }

    // Verify the admin password
    public static boolean verifyAdminPassword(String enteredPassword) {
        try {
            String storedEncryptedPassword = readAdminPassword();
            String decryptedPassword = decryptPassword(storedEncryptedPassword);
            return decryptedPassword.equals(enteredPassword);
        } catch (Exception e) {
            System.err.println("Error verifying admin password: " + e.getMessage());
            return false;
        }
    }

    // Read the encrypted admin password
    public static String readAdminPassword() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_PASSWORD_FILE))) {
            return reader.readLine();
        } catch (IOException e) {
            return null;  // Admin password file doesn't exist
        }
    }

    // Reset the user password by the admin
    public void resetUserPassword() {
        String newPassword = JOptionPane.showInputDialog(this, "Play Thermonuclear War?: ");
        storeUserPassword(newPassword);
        taMessage.append("A strange game. The winning move is not to play \n");
    }

    // Start the Thermonuclear War game
    private void startGame() {
        taMessage.setText("Starting Thermonuclear War game...\n");
    
        // Create Player and AI instances
        player = new Player("Player", taMessage);
        ai = new AI("AI", taMessage);
    
        // Initialize the game loop
        new Thread(() -> playGame()).start();
    }

    // Function to simulate the game
    private void playGame() {
        Random random = new Random();
        boolean gameOver = false;
    
        while (!gameOver) {
            taMessage.append("\n" + player.name + "'s Weapons: " + player.weapons + ", " + ai.name + "'s Weapons: " + ai.weapons);
            taMessage.append("\n" + player.name + "'s Cities: " + player.cities + ", " + ai.name + "'s Cities: " + ai.cities);
    
            // Player's turn
            String playerChoice = JOptionPane.showInputDialog(this,
                    "\nChoose an option:\n1. Launch a missile\n2. Defend\n3. Negotiate\n4. W.O.P.R");
            switch (playerChoice) {
                case "1":
                    player.launchMissile(ai);
                    break;
                case "2":
                    player.defend();
                    break;
                case "3":
                    if (!ai.negotiate(player)) {
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
            if (player.cities.size() <= 0 || ai.cities.size() <= 0) {
                gameOver = true;
                taMessage.append("\nGame Over!");
                if (player.cities.size() <= 0 && ai.cities.size() <= 0) {
                    taMessage.append("It's a draw! Both sides are destroyed.");
                } else if (player.cities.size() <= 0) {
                    taMessage.append(ai.name + " won! All of your cities were destroyed.");
                } else if (ai.cities.size() <= 0) {
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

    private void randomEvent() {
        // Simulating random events here
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Thermonuclear_War_v3());
    }
}

// Player class definition
class Player {
    private static final int MISSILE_DAMAGE = 0;
        String name;
        int weapons;
        ArrayList<String> cities; // Corrected to ArrayList<String> for cities
        double defense;
        int aggressionLevel;
    
        private JTextArea taMessage;  // Reference to taMessage
    
        Player(String name, JTextArea taMessage) {
            this.name = name;
            this.weapons = 3000;
            this.cities = new ArrayList<>();  // Initialize the cities list
            this.defense = 0.5;
            this.aggressionLevel = 0;
            this.taMessage = taMessage;
            // You can add cities here, e.g., this.cities.add("City1");
        }
    
        public void launchMissile(Player target) {
            if (this.weapons > 0) {
                taMessage.append(this.name + " launches a missile!\n");
                this.weapons--;  // Decrease the missile count
        
                // Get target cities as an array from the Player's cities
                String[] cityArray = target.getCities().toArray(new String[0]);  // Convert List to array
        
                // Show input dialog to select the city
                String targetCity = (String) JOptionPane.showInputDialog(null,
                        cityArray);
        
                // If a valid city is selected
                if (targetCity != null) {
                    taMessage.append("Missile heading to " + targetCity + ".\n");
        
                    // Simulate a random hit or miss
                    if (new Random().nextDouble() < 0.5) {  // 50% chance for a hit
                        taMessage.append(targetCity + " has been hit!\n");
        
                        // Calculate damage
                        int damage = new Random().nextInt(MISSILE_DAMAGE) + 1;  // Damage between 1 and MISSILE_DAMAGE
    
                    // Remove city and apply damage
                    target.getCities().remove(targetCity);  // Remove the city as it's destroyed
                    taMessage.append(this.name + " destroyed " + targetCity + " causing " + damage + "% damage.\n");
                    
                    // Optional: Check if all cities are destroyed
                    if (target.getCities().isEmpty()) {
                        taMessage.append(target.name + " has no cities left!\n");
                    }
                } else {
                    taMessage.append(targetCity + " has survived the attack.\n");
                }
            } else {
                taMessage.append("No city selected. Missile launch aborted.\n");
            }
        } else {
            taMessage.append(this.name + " has no missiles left!\n");
        }
    }
    
    // Method to get cities in Player class
    public ArrayList<String> getCities() {
        return this.cities;  // Returning the ArrayList of cities
    }
    void defend() {
        taMessage.append(this.name + " activates missile defense systems!\n");
        this.defense = 0.7;  // Increase defense for the turn
    }

    boolean negotiate(Player target) {
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
        return this.cities.size() > 0;  // Player is alive if they have cities left
    }
}

class AI extends Player {
    AI(String name, JTextArea taMessage) {
        super(name, taMessage);
    }

    void makeDecision(Player player) {
        if (this.weapons > 0 && this.cities.size() > 0) {
            // AI chooses an action based on its aggression level and random chance
            String action = null;
            if (this.aggressionLevel > player.aggressionLevel) {
                action = new Random().nextDouble() < 0.5 ? "launch" : "defend";
            } else {
                action = new Random().nextDouble() < 0.6 ? "launch" : "negotiate";
            }

            // Execute the chosen action
            switch (action) {
                case "launch":
                    this.launchMissile(player);
                    break;
                case "defend":
                    this.defend();
                    break;
                case "negotiate":
                    if (!player.negotiate(this)) {
                        this.launchMissile(player);  // AI retaliates if negotiations fail
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
