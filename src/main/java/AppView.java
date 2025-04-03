import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class AppView extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale locale = new Locale("en", "US");
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app.fxml"));
        loader.setResources(bundle);
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.setTitle("Kim Tran-Do");
        primaryStage.show();
    }
}