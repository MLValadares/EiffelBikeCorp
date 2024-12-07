package edu.m2sia.eiffelbikecorp.gui_mockup;

import edu.m2sia.eiffelbikecorp.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private UserService userService;
    private Stage primaryStage;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String token = userService.login(username, password);
        if (token != null) {
            // Proceed to main window
            MainWindowController mainWindowController = new MainWindowController();
            mainWindowController.setUserService(userService);
            mainWindowController.setToken(token);
            mainWindowController.start(primaryStage);
        } else {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait();
        }
    }
}