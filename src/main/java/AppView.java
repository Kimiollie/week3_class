import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale locale = new Locale("en", "US");
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.messages", locale);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"), bundle);
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle(bundle.getString("title"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}