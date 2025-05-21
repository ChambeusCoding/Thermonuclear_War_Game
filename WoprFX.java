import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;

public class  extends Application {

    private static final String USER_PASSWORD_FILE = "user_password.txt";
    private static final String ADMIN_PASSWORD_FILE = "admin_password.txt";

    private TextArea taMessage = new TextArea();
    private Label slidingLabel = new Label("Opening Joshua... User or Admin Mode.");
    private double xPosition = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("W.O.P.R Interface");

        VBox root = new VBox(10);
        root.setStyle("-fx-background-color: #8B0000; -fx-padding: 20;");

        // Sliding label (simulating marquee)
        slidingLabel.setFont(Font.font("Segoe Print", 18));
        slidingLabel.setTextFill(Color.WHITE);
        StackPane slidingPane = new StackPane(slidingLabel);
        slidingPane.setMinHeight(30);

        // Message area
        taMessage.setFont(Font.font("Segoe Print", 16));
        taMessage.setEditable(false);
        taMessage.setWrapText(true);
        taMessage.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        ScrollPane scrollPane = new ScrollPane(taMessage);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent;");

        // Buttons
        Button btnUser = new Button("User");
        Button btnAdmin = new Button("Admin");
        btnUser.setFont(Font.font("Segoe Print", 16));
        btnAdmin.setFont(Font.font("Segoe Print", 16));

        btnUser.setOnAction(e -> handleUserMode());
        btnAdmin.setOnAction(e -> handleAdminMode());

        HBox buttonBox = new HBox(10, btnUser, btnAdmin);
        buttonBox.setStyle("-fx-alignment: center;");

        root.getChildren().addAll(slidingPane, scrollPane, buttonBox);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        startSlidingText();
    }

    private void startSlidingText() {
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(30);
                    xPosition += 2;
                    final double finalX = xPosition;

                    javafx.application.Platform.runLater(() -> {
                        slidingLabel.setTranslateX(finalX);
                        if (finalX > 600) {
                            xPosition = -slidingLabel.getWidth();
                        }
                    });
                }
            } catch (InterruptedException ignored) {}
        }).start();
    }

    private void handleUserMode() {
        taMessage.setText("Confirm to play Thermonuclear War\n");

        String storedPassword = readPassword(USER_PASSWORD_FILE);
        if (storedPassword == null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Set Password");
            dialog.setHeaderText("Set a user password:");
            dialog.setContentText("Password:");
            dialog.showAndWait().ifPresent(password -> {
                if (!password.isBlank()) {
                    writePassword(USER_PASSWORD_FILE, password);
                    taMessage.appendText("User password set successfully.\n");
                } else {
                    taMessage.appendText("Password not set.\n");
                }
            });
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("User Login");
        dialog.setHeaderText("Enter user password:");
        dialog.setContentText("Password:");
        dialog.showAndWait().ifPresent(entered -> {
            if (entered.equals(readPassword(USER_PASSWORD_FILE))) {
                taMessage.appendText("Access granted... Accessing W.O.P.R\n");
                launchMissile();
            } else {
                taMessage.appendText("Incorrect password. Access denied.\n");
            }
        });
    }

    private void handleAdminMode() {
        taMessage.setText("Enter Admin to reset W.O.P.R.\n");

        String storedPassword = readPassword(ADMIN_PASSWORD_FILE);
        if (storedPassword == null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Set Admin Password");
            dialog.setHeaderText("Set an admin password:");
            dialog.setContentText("Password:");
            dialog.showAndWait().ifPresent(password -> {
                if (!password.isBlank()) {
                    writePassword(ADMIN_PASSWORD_FILE, password);
                    taMessage.appendText("Admin password set successfully.\n");
                } else {
                    taMessage.appendText("Password not set.\n");
                }
            });
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Admin Login");
        dialog.setHeaderText("Enter admin password:");
        dialog.setContentText("Password:");
        dialog.showAndWait().ifPresent(entered -> {
            if (entered.equals(readPassword(ADMIN_PASSWORD_FILE))) {
                taMessage.appendText("Admin access granted... Accessing W.O.P.R\n");
                taMessage.appendText("Greetings Professor Falken... Shall we play a game?\n");
                deleteFile(USER_PASSWORD_FILE);
                taMessage.appendText("User password has been reset.\n");
            } else {
                taMessage.appendText("Incorrect admin password.\n");
            }
        });
    }

    private String readPassword(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    private void writePassword(String filename, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(password);
        } catch (IOException e) {
            taMessage.appendText("Error writing password file.\n");
        }
    }

    private void deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }

    private void launchMissile() {
        taMessage.appendText("Launching missiles...\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
