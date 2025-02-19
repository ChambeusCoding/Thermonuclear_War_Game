import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Scanner;

public class Thermonuclear_War_v9_JavaFx extends Application {

    private TextArea taMessage;
    private Player player;
    private AI ai;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize UI components
        taMessage = new TextArea();
        taMessage.setFont(Font.font("Segoe Print", 18));
        taMessage.setEditable(false);
        taMessage.setWrapText(true);
        taMessage.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        // Layout components
        BorderPane root = new BorderPane();
        root.setCenter(taMessage);

        // Buttons for User and Admin mode
        FlowPane buttonPanel = new FlowPane();
        Button btnUserMode = new Button("User Mode");
        Button btnAdminMode = new Button("Admin Mode");

        btnUserMode.setFont(Font.font("Segoe Print", 18));
        btnAdminMode.setFont(Font.font("Segoe Print", 18));

        // Button actions
        btnUserMode.setOnAction(e -> handleUserMode());
        btnAdminMode.setOnAction(e -> handleAdminMode());

        buttonPanel.getChildren().addAll(btnUserMode, btnAdminMode);
        root.setBottom(buttonPanel);

        // Scene setup
        Scene scene = new Scene(root, 800, 600, Color.BLACK);
        primaryStage.setTitle("W.O.P.R Joshua");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start the text transition and game
        startTextTransition();
    }

    private void startTextTransition() {
        String currentMessage = "Opening Joshua... User or Admin Mode.";
        taMessage.appendText(currentMessage + "\n");
    }

    private void handleUserMode() {
        taMessage.setText("Confirm to play Thermonuclear War\n");

        String userPassword = readUserPassword();
        if (userPassword == null) {
            String generatedPassword = generatePassword();
            taMessage.appendText("Generated Password: " + generatedPassword + "\n");
            storeUserPassword(generatedPassword);
        } else {
            String enteredPassword = "User enters password";  // Replace with real input field
            if (verifyUserPassword(enteredPassword)) {
                taMessage.appendText("Access granted... Accessing W.O.P.R\n");
                startGame();
            } else {
                taMessage.appendText("Incorrect password. Access denied.\n");
            }
        }
    }

    private void handleAdminMode() {
        taMessage.setText("Enter Admin to reset W.O.P.R.\n");

        String adminPassword = readAdminPassword();
        if (adminPassword == null) {
            String enteredPassword = "Admin enters password";  // Replace with real input field
            storeAdminPassword(enteredPassword);
            taMessage.appendText("Admin password set successfully.\n");
        }

        String enteredAdminPassword = "Admin enters password";  // Replace with real input field
        if (verifyAdminPassword(enteredAdminPassword)) {
            taMessage.appendText("Admin access granted... Accessing W.O.P.R\n");
            taMessage.appendText("Greetings Professor Falken... Shall we play a game?\n");
            resetUserPassword();
        } else {
            taMessage.appendText("Incorrect admin password.\n");
        }
    }

    private void startGame() {
        taMessage.setText("Starting Thermonuclear War game...\n");

        player = new Player("Player");
        ai = new AI("AI");

        // Game loop or turn-based mechanics would go here
        // Example of player input handling and AI actions
        handlePlayerTurn();
        handleAITurn();
    }

    private void handlePlayerTurn() {
        // Simulate player actions (e.g., launch missile, defend, etc.)
        taMessage.appendText("Player's turn...\n");
    }

    private void handleAITurn() {
        // Simulate AI actions (e.g., make decisions, launch missile, etc.)
        taMessage.appendText("AI's turn...\n");
    }

    private String generatePassword() {
        // Generate and return a random password
        return "GeneratedPassword123";
    }

    private boolean verifyUserPassword(String enteredPassword) {
        // Placeholder: implement password verification
        return "UserPassword".equals(enteredPassword);
    }

    private void storeUserPassword(String password) {
        // Store the user password for later verification
        // In this example, we just print it (you may want to store it securely)
        System.out.println("Storing user password: " + password);
    }

    private String readUserPassword() {
        // Placeholder: read the user password from somewhere
        return null;  // Assuming no password set yet, hence it returns null
    }

    private void resetUserPassword() {
        // Reset the user password after the game ends
        taMessage.appendText("User password reset.\n");
    }

    private String readAdminPassword() {
        // Placeholder: read the admin password
        return null;  // Assuming no admin password set
    }

    private void storeAdminPassword(String password) {
        // Store the admin password securely
        System.out.println("Storing admin password: " + password);
    }

    private boolean verifyAdminPassword(String enteredPassword) {
        // Verify admin password
        return "AdminPassword".equals(enteredPassword);
    }

    // Inner classes for Player and AI (simplified versions)
    class Player {
        private String name;
        public Player(String name) {
            this.name = name;
        }
    }

    class AI {
        private String name;
        public AI(String name) {
            this.name = name;
        }
    }
}
