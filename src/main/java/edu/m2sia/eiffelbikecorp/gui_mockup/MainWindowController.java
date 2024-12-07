package edu.m2sia.eiffelbikecorp.gui_mockup;

import edu.m2sia.eiffelbikecorp.service.UserService;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainWindowController extends Application {
    private UserService userService;
    private String token;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize the main window here
        primaryStage.setTitle("Main Window");
        primaryStage.show();
    }
}
