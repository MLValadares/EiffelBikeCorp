package edu.m2sia.eiffelbikecorp.gui_mockup;

import edu.m2sia.eiffelbikecorp.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginApp extends Application {
    private UserService userService = new UserService();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/m2sia/eiffelbikecorp/gui/Login.fxml"));
        Pane root = loader.load();
        LoginController controller = loader.getController();
        controller.setUserService(userService);
        controller.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
