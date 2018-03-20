package notice;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.Optional;

public final class Notice {
    public static final void showAlertDialog(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("icons/dialog_icon.png"));
        alert.show();
    }

    public static final boolean showQuestion(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(title);
        alert.setContentText(content);
        ButtonType okButton = new ButtonType("Yes");
        ButtonType cancelButton = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(okButton, cancelButton);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("icons/dialog_icon.png"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == okButton){
            return true;
        }
        return false;
    }
}
