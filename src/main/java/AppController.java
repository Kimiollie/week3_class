import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController {
    @FXML
    private ComboBox<String> languageComboBox;

    private ResourceBundle bundle;

    @FXML
    public void initialize() {
        languageComboBox.getItems().addAll("English", "French", "Spanish", "Chinese");
        languageComboBox.setValue("English");
        bundle = ResourceBundle.getBundle("messages", new Locale("en", "US"));
    }

    @FXML
    public void switchLanguage() {
        String selectedLanguage = languageComboBox.getValue();
        Locale locale;
        switch (selectedLanguage) {
            case "French":
                locale = new Locale("fr", "FR");
                break;
            case "Spanish":
                locale = new Locale("es", "ES");
                break;
            case "Chinese":
                locale = new Locale("zh", "CN");
                break;
            default:
                locale = new Locale("en", "US");
        }
        bundle = ResourceBundle.getBundle("messages", locale);
        // Update UI elements with new language
    }

    @FXML
    public void fetchFromDatabase() {
        String url = "jdbc:mariadb://localhost:3306/w3_Class";
        String user = "newuser";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT translation_text FROM translations WHERE Key_name = ? AND Language_code = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "job_title");
            stmt.setString(2, bundle.getLocale().getLanguage());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String translation = rs.getString("translation_text");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle(bundle.getString("title"));
                alert.setHeaderText(null);
                alert.setContentText(translation);
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}