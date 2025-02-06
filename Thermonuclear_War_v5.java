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

public class Thermonuclear_War_v5 extends JFrame {
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

    public Thermonuclear_War_v5() {
        setTitle("W.O.P.R Joshua");
        setSize(500, 600);
        setMinimumSize(new Dimension(1000, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialize();
    }

    public void initialize() {
        // Initialize cities list with provided cities
        cities = new ArrayList<>();
        cities.add("Adrian");
        cities.add("Alma");
        cities.add("Ann Arbor");
        cities.add("Battle Creek");
        cities.add("Bay City");
        cities.add("Benton Harbor");
        cities.add("Bloomfield Hills");
        cities.add("Cadillac");
        cities.add("Charlevoix");
        cities.add("Cheboygan");
        cities.add("Dearborn");
        cities.add("Detroit");
        cities.add("East Lansing");
        cities.add("Eastpointe");
        cities.add("Ecorse");
        cities.add("Escanaba");
        cities.add("Flint");
        cities.add("Grand Haven");
        cities.add("Grand Rapids");
        cities.add("Grayling");
        cities.add("Grosse Pointe");
        cities.add("Hancock");
        cities.add("Highland Park");
        cities.add("Holland");
        cities.add("Houghton");
        cities.add("Interlochen");
        cities.add("Iron Mountain");
        cities.add("Ironwood");
        cities.add("Ishpeming");
        cities.add("Jackson");
        cities.add("Kalamazoo");
        cities.add("Lansing");
        cities.add("Livonia");
        cities.add("Ludington");
        cities.add("Mackinaw City");
        cities.add("Manistee");
        cities.add("Marquette");
        cities.add("Menominee");
        cities.add("Midland");
        cities.add("Monroe");
        cities.add("Mount Clemens");
        cities.add("Mount Pleasant");
        cities.add("Muskegon");
        cities.add("Niles");
        cities.add("Petoskey");
        cities.add("Pontiac");
        cities.add("Port Huron");
        cities.add("Royal Oak");
        cities.add("Saginaw");
        cities.add("Saint Ignace");
        cities.add("Saint Joseph");
        cities.add("Sault Sainte Marie");
        cities.add("Traverse City");
        cities.add("Trenton");
        cities.add("Warren");
        cities.add("Wyandotte");
        cities.add("Ypsilanti");
        cities.add("Montgomery");
        cities.add("Juneau");
        cities.add("Phoenix");
        cities.add("Little Rock");
        cities.add("Sacramento");
        cities.add("Denver");
        cities.add("Hartford");
        cities.add("Dover");
        cities.add("Tallahassee");
        cities.add("Atlanta");
        cities.add("Honolulu");
        cities.add("Boise");
        cities.add("Springfield");
        cities.add("Indianapolis");
        cities.add("Des Moines");
        cities.add("Topeka");
        cities.add("Frankfort");
        cities.add("Baton Rouge");
        cities.add("Augusta");
        cities.add("Annapolis");
        cities.add("Boston");
        cities.add("Lansing");
        cities.add("Saint Paul");
        cities.add("Jackson");
        cities.add("Jefferson City");
        cities.add("Helena");
        cities.add("Lincoln");
        cities.add("Carson City");
        cities.add("Concord");
        cities.add("Trenton");
        cities.add("Santa Fe");
        cities.add("Albany");
        cities.add("Raleigh");
        cities.add("Bismarck");
        cities.add("Columbus");
        cities.add("Oklahoma City");
        cities.add("Salem");
        cities.add("Harrisburg");
        cities.add("Providence");
        cities.add("Columbia");
        cities.add("Pierre");
        cities.add("Nashville");
        cities.add("Austin");
        cities.add("Salt Lake City");
        cities.add("Montpelier");
        cities.add("Richmond");
        cities.add("Olympia");
        cities.add("Charleston");
        cities.add("Madison");
        cities.add("Cheyenne");
        cities.add("440 Old Orchard Dr, Essexville, MI 48732");

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
                launchMissile();
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
                            
                                private String readAdminPassword() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'readAdminPassword'");
            }
        
                                private void storeAdminPassword(String enteredPassword) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'storeAdminPassword'");
                }
            
                        private boolean verifyAdminPassword(String enteredAdminPassword) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'verifyAdminPassword'");
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

    // Launch the missile and prompt for a city selection
    private void launchMissile() {
        taMessage.append("Missile launched! Select a target city.\n");

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
            // Simulate hit or miss (random outcome for simplicity)
            if (new Random().nextBoolean()) {
                taMessage.append(targetCity + " has been hit!\n");
            } else {
                taMessage.append(targetCity + " has survived the attack.\n");
            }
        } else {
            taMessage.append("No city selected. Missile launch aborted.\n");
        }
    }

    // Encrypt and store the user password
    public static void storeUserPassword(String password) {
        try {
            String encryptedPassword = encryptPassword(password);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_PASSWORD_FILE))) {
                writer.write(encryptedPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Encrypt password using AES
    public static String encryptPassword(String password) throws Exception {
        SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Read the user password from the file
    private String readUserPassword() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(USER_PASSWORD_FILE));
            String encryptedPassword = reader.readLine();
            return encryptedPassword;
        } catch (IOException e) {
            return null;
        }
    }

    // Verify the user password
    private boolean verifyUserPassword(String enteredPassword) {
        try {
            String encryptedPassword = readUserPassword();
            if (encryptedPassword == null) {
                return false;
            }
            String decryptedPassword = decryptPassword(encryptedPassword);
            return enteredPassword.equals(decryptedPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Decrypt the password using AES
    public static String decryptPassword(String encryptedPassword) throws Exception {
        SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedPassword = Base64.getDecoder().decode(encryptedPassword);
        byte[] decrypted = cipher.doFinal(decodedPassword);
        return new String(decrypted);
    }

    // Reset the user password
    private void resetUserPassword() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(USER_PASSWORD_FILE));
            writer.write("");  // Clear the file
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Similar methods for handling admin password storage, retrieval, and verification...

    public static void main(String[] args) {
        new Thermonuclear_War_v5();
    }
}
