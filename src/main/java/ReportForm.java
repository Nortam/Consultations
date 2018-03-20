import check.system.CurrentSystem;
import documents.Document;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class ReportForm extends Application {

    @FXML
    protected TextField pathTextField;
    @FXML
    protected Button openButton;
    @FXML
    protected ComboBox typeComboBox;
    @FXML
    protected Label statusLabel;

    private static Stage reportStage = null;
    private static Document document = new Document();
    private static String filename = null, path = null, type = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        reportStage = loadStage(
                "directory.fxml", "icons/file_icon.png", "Report generation", 312, 160, false
        );
    }

    @FXML
    public void initialize() {
        typeComboBox.getItems().addAll("doc", "docx", "odt");
        typeComboBox.getSelectionModel().select(0);
        pathTextField.setText(document.getCurrentDirectory());
        if (!CurrentSystem.isWindows()) {
            openButton.setDisable(true);
        }
    }

    @FXML
    protected void chooseOnClickButton() {
        String path = document.chooseFileDirectory();
        if (path != null) {
            pathTextField.setText(path);
            statusLabel.setText("Status: waiting action");
        }
    }

    @FXML
    protected void saveOnClickButton() {
        path = pathTextField.getText();
        filename = document.createFileName();
        type = typeComboBox.getSelectionModel().getSelectedItem().toString();
        try {
            document.createNewDocument(path, filename, type);
            document.writeDataToFile(MainForm.dataWeek, path, filename, type);
            if (CurrentSystem.isWindows()) {
                openButton.setDisable(false);
            }
            statusLabel.setText("Status: file was successfully created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void openOnClickButton() {
        try {
            document.openFile(path, filename, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage loadStage(String sceneName, String icon, String title, int width, int height, boolean resizable) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(sceneName));
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image(icon));
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(resizable);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        stage.show();
        //stage.setOnCloseRequest(event -> {});
        return stage;
    }
}
