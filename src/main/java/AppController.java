import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppController {
    @FXML
    public Label jobTitleLabel;
    @FXML
    public ListView<String> jobTitleListView;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private TextField keyNameField;
    @FXML
    private TextField translationField;
    @FXML
    private Button addUpdateButton;

    private ResourceBundle bundle;

    @FXML
    public void initialize() {
        languageComboBox.getItems().addAll("English", "Français", "Español", "中文");
        languageComboBox.setValue("English");
        bundle = ResourceBundle.getBundle("messages", new Locale("en", "US"));
        updateUI();
        fetchFromDatabase();
    }

    @FXML
    public void switchLanguage() {
        String selectedLanguage = languageComboBox.getValue();
        Locale locale;
        switch (selectedLanguage) {
            case "Français":
                locale = new Locale("fr", "FR");
                break;
            case "Español":
                locale = new Locale("es", "ES");
                break;
            case "中文":
                locale = new Locale("zh", "CN");
                break;
            default:
                locale = new Locale("en", "US");
        }
        bundle = ResourceBundle.getBundle("messages", locale);
        updateUI();
        fetchFromDatabase();
    }

    private void updateUI() {
        jobTitleLabel.setText(bundle.getString("employeeJobTitle"));
        addUpdateButton.setText(bundle.getString("addUpdateButton"));
        keyNameField.setPromptText(bundle.getString("keyNameField"));
        translationField.setPromptText(bundle.getString("translationField"));
    }

    @FXML
    public void addOrUpdateTranslation() {
        String keyName = keyNameField.getText();
        String translation = translationField.getText();
        String languageCode = bundle.getLocale().getLanguage();

        if (keyName.isEmpty() || translation.isEmpty()) {
            showAlert(bundle.getString("errorTitle"), bundle.getString("errorMessage"));
            return;
        }

        String url = "jdbc:mariadb://localhost:3306/w3_Class";
        String user = "newuser";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "INSERT INTO translations (Key_name,  Language_code, translation_text) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE translation_text = VALUES(translation_text)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, keyName);
            stmt.setString(2, languageCode);
            stmt.setString(3, translation);
            stmt.executeUpdate();
            fetchFromDatabase();
            showAlert(bundle.getString("successTitle"), bundle.getString("successMessage"));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(bundle.getString("errorTitle"), bundle.getString("errorUpdateMessage"));
        }
    }

    public void fetchFromDatabase() {
        String url = "jdbc:mariadb://localhost:3306/w3_Class";
        String user = "newuser";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT Key_name, translation_text FROM translations WHERE Language_code = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, bundle.getLocale().getLanguage());
            ResultSet rs = stmt.executeQuery();
            ObservableList<String> jobTitles = FXCollections.observableArrayList();
            while (rs.next()) {
                String keyName = rs.getString("Key_name");
                String translationText = rs.getString("translation_text");
                jobTitles.add(keyName + " : " + translationText);
            }
            jobTitleListView.setItems(jobTitles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}