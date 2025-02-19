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

public class Thermonuclear_War_v10 extends JFrame {
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

     public Thermonuclear_War_v10() {
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
}