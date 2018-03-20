import check.regexes.Regexes;
import data.UserData;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import notice.Notice;
import sqlserver.database.consultant.Consultant;

import java.io.IOException;
import java.sql.ResultSet;
import java.time.LocalDate;
import static data.UserData.*;
import static notice.Notice.showQuestion;
import static sqlserver.database.administrator.Administrator.getAdministrator;
import static sqlserver.database.consultant.Consultant.getConsultant;

public class EditForm extends Application {

    @FXML
    ComboBox typeConsultationComboBox, placeComboBox, consultantComboBox, placeStudyComboBox, placeWorkComboBox;
    @FXML
    CheckBox healthStatusCheckBox;
    @FXML
    TextField timeTextField, ageTextField, diagnosisTextField, fullNameTextField, numberTextField, placeStudyTextField, placeWorkTextField;
    @FXML
    Label workPlaceLabel, studyPlaceLabel;
    @FXML
    DatePicker datePicker;
    @FXML
    Button applyAgeButton;

    private static Stage editStage = null;
    private static String mode = null;

    private static int idConsultation = 0;

    public static Stage getEditStage() {
        return editStage;
    }

    protected static void setMode(String mode) {
        EditForm.mode = mode;
    }

    public static void setIdConsultation(int idConsultation) {
        EditForm.idConsultation = idConsultation;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        editStage = loadStage(
                "/edit.fxml", "icons/main_icon.png", "Record", 436, 375, false
        );
    }

    @FXML
    public void initialize() {
        ResultSet resultSet = null;
        if (getUserData().isAdmin()) {
            try {
                if (mode == "Edit") {
                    resultSet = getAdministrator().getConsultation(getAdministrator(), idConsultation);
                    resultSet.next();
                    datePicker.setValue(resultSet.getDate("consDate").toLocalDate());
                    timeTextField.setText(resultSet.getTime("consTime").toString());
                    fullNameTextField.setText(resultSet.getString("fio"));
                    ageTextField.setText(String.valueOf(resultSet.getInt("age")));
                    applyAgeOnClickButton();
                    numberTextField.setText(resultSet.getString("phones"));
                    String diagnosis = resultSet.getString("diagnosis");
                    if (diagnosis != null) {
                        healthStatusCheckBox.setSelected(true);
                        diagnosisTextField.setDisable(false);
                        diagnosisTextField.setText(diagnosis);
                    }
                    String school = resultSet.getString("school"),
                            classOfSchool = resultSet.getString("class");
                    placeWorkTextField.setText(school);
                    placeStudyTextField.setText(classOfSchool);
                    int idConsultationType = resultSet.getInt("constypes_id"),
                            idLocation = resultSet.getInt("location_id"),
                            idConsultant = resultSet.getInt("acntApp_id"),
                            idPlaceStudy = resultSet.getInt("placestudy_id"),
                            idPlaceWork = resultSet.getInt("placework_id");
                    resultSet = getAdministrator().getConsultationTypes(getAdministrator());
                    while (resultSet.next()) {
                        typeConsultationComboBox.getItems().add(resultSet.getString(2));
                        if (resultSet.getInt(1) == idConsultationType) {
                            typeConsultationComboBox.getSelectionModel().select(typeConsultationComboBox.getItems().size() - 1);
                        }
                    }
                    resultSet = getAdministrator().getLocations(getAdministrator());
                    while (resultSet.next()) {
                        placeComboBox.getItems().add(resultSet.getString(2));
                        if (resultSet.getInt(1) == idLocation) {
                            placeComboBox.getSelectionModel().select(placeComboBox.getItems().size() - 1);
                        }
                    }
                    resultSet = getAdministrator().getConsultants(getAdministrator());
                    while (resultSet.next()) {
                        consultantComboBox.getItems().add(resultSet.getString(2));
                        if (resultSet.getInt(1) == idConsultant) {
                            consultantComboBox.getSelectionModel().select(consultantComboBox.getItems().size() - 1);
                        }
                    }
                    resultSet = getAdministrator().getPlacesStudy(getAdministrator());
                    while (resultSet.next()) {
                        placeStudyComboBox.getItems().add(resultSet.getString(2));
                        if (resultSet.getInt(1) == idPlaceStudy) {
                            placeStudyComboBox.getSelectionModel().select(placeStudyComboBox.getItems().size() - 1);
                        }
                    }
                    resultSet = getAdministrator().getPlacesWork(getAdministrator());
                    while (resultSet.next()) {
                        placeWorkComboBox.getItems().add(resultSet.getString(2));
                        if (resultSet.getInt(1) == idPlaceWork) {
                            placeWorkComboBox.getSelectionModel().select(placeWorkComboBox.getItems().size() - 1);
                        }
                    }
                } else if (mode == "Add") {
                    datePicker.setValue(LocalDate.now());
                    resultSet = getAdministrator().getConsultationTypes(getAdministrator());
                    while (resultSet.next()) {
                        typeConsultationComboBox.getItems().add(resultSet.getString(2));
                    }
                    resultSet = getAdministrator().getLocations(getAdministrator());
                    while (resultSet.next()) {
                        placeComboBox.getItems().add(resultSet.getString(2));
                    }
                    resultSet = getAdministrator().getConsultants(getAdministrator());
                    while (resultSet.next()) {
                        consultantComboBox.getItems().add(resultSet.getString(2));
                    }
                    resultSet = getAdministrator().getPlacesStudy(getAdministrator());
                    while (resultSet.next()) {
                        placeStudyComboBox.getItems().add(resultSet.getString(2));
                    }
                    resultSet = getAdministrator().getPlacesWork(getAdministrator());
                    while (resultSet.next()) {
                        placeWorkComboBox.getItems().add(resultSet.getString(2));
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
        } else {
            try {
                resultSet = getConsultant().getConsultation(getConsultant(), idConsultation);
                resultSet.next();
                datePicker.setValue(resultSet.getDate("consDate").toLocalDate());
                timeTextField.setText(resultSet.getTime("consTime").toString());
                fullNameTextField.setText(resultSet.getString("fio"));
                ageTextField.setText(String.valueOf(resultSet.getInt("age")));
                applyAgeOnClickButton();
                numberTextField.setText(resultSet.getString("phones"));
                diagnosisTextField.setText(resultSet.getString("diagnosis"));
                int idConsultationType = resultSet.getInt("constypes_id"),
                        idLocation = resultSet.getInt("location_id"),
                        idConsultant = resultSet.getInt("acntApp_id"),
                        idPlaceStudy = resultSet.getInt("placestudy_id"),
                        idPlaceWork = resultSet.getInt("placework_id");
                placeWorkTextField.setText(resultSet.getString("school"));
                placeStudyTextField.setText(resultSet.getString("class"));
                //getConsultationType
                if (idConsultationType != 0) {
                    resultSet = getConsultant().getConsultationType(getConsultant(), idConsultationType);
                    resultSet.next();
                    typeConsultationComboBox.getItems().add(resultSet.getString(2));
                    typeConsultationComboBox.getSelectionModel().select(0);
                }
                //getLocation
                if (idLocation != 0) {
                    resultSet = getConsultant().getLocation(getConsultant(), idLocation);
                    resultSet.next();
                    placeComboBox.getItems().add(resultSet.getString(2));
                    placeComboBox.getSelectionModel().select(0);
                }
                //getConsultant
                resultSet = getConsultant().getConsultants(getConsultant());
                while (resultSet.next()) {
                    if (resultSet.getInt(1) == getUserData().getIdAccount()) {
                        consultantComboBox.getItems().add(resultSet.getString(2));
                        if (resultSet.getInt(1) == idConsultant) {
                            consultantComboBox.getSelectionModel().select(0);
                        }
                    }
                }
                //getPlaceStudy
                if (idPlaceStudy != 0) {
                    resultSet = getConsultant().getPlaceStudy(getConsultant(), idPlaceStudy);
                    resultSet.next();
                    placeStudyComboBox.getItems().add(resultSet.getString(2));
                    placeStudyComboBox.getSelectionModel().select(0);
                }
                //getPlaceWork
                if (idPlaceWork != 0) {
                    resultSet = getConsultant().getPlaceWork(getConsultant(), idPlaceWork);
                    resultSet.next();
                    placeWorkComboBox.getItems().add(resultSet.getString(2));
                    placeWorkComboBox.getSelectionModel().select(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //setDisableAllTextFields
            timeTextField.setEditable(false);
            ageTextField.setEditable(false);
            diagnosisTextField.setEditable(false);
            fullNameTextField.setEditable(false);
            numberTextField.setEditable(false);
            placeWorkTextField.setEditable(false);
            placeStudyTextField.setEditable(false);
            //setDisableOther
            datePicker.setDisable(true);
            healthStatusCheckBox.setDisable(true);
            applyAgeButton.setDisable(true);
        }
    }

    @FXML
    protected void applyAgeOnClickButton() {
        if (ageTextField.getText().length() != 0 && ageTextField.getText().toString().charAt(0) != '-') {
            int age = -1;
            try {
                age = Integer.valueOf(ageTextField.getText());
            } catch (Exception e) { }
            System.out.println(age);
            if (age != -1) {
                if (age > 0 && age <= 100) {
                    setDisableComboBox(new ComboBox[] {placeWorkComboBox, placeStudyComboBox}, false);
                    setDisableLabels(new Label[] {workPlaceLabel, studyPlaceLabel}, false);
                    if (age <= 18) {
                        placeWorkComboBox.setVisible(false);
                        placeStudyComboBox.setVisible(false);
                        placeWorkTextField.setVisible(true);
                        placeStudyTextField.setVisible(true);
                        workPlaceLabel.setText("Школа");
                        studyPlaceLabel.setText("Класс");
                    } else {
                        placeWorkComboBox.setVisible(true);
                        placeStudyComboBox.setVisible(true);
                        placeWorkTextField.setVisible(false);
                        placeStudyTextField.setVisible(false);
                        workPlaceLabel.setText("Место работы");
                        studyPlaceLabel.setText("Место учебы");
                    }
                } else {
                    setDisableComboBox(new ComboBox[] {placeWorkComboBox, placeStudyComboBox}, true);
                    setDisableLabels(new Label[] {workPlaceLabel, studyPlaceLabel}, true);
                }
            } else {
                setDisableComboBox(new ComboBox[] {placeWorkComboBox, placeStudyComboBox}, true);
                setDisableLabels(new Label[] {workPlaceLabel, studyPlaceLabel}, true);
            }
        } else {
            setDisableComboBox(new ComboBox[] {placeWorkComboBox, placeStudyComboBox}, true);
            setDisableLabels(new Label[] {workPlaceLabel, studyPlaceLabel}, true);
        }
    }

    private void setDisableTextFields(TextField[] textFields, boolean disable) {
        for (int i = 0; i < textFields.length; i++) {
            textFields[i].setDisable(disable);
        }
    }

    private void setDisableComboBox(ComboBox[] comboBoxes, boolean disable) {
        for (int i = 0; i < comboBoxes.length; i++) {
            comboBoxes[i].setDisable(disable);
        }
    }

    private void setDisableLabels(Label[] labels, boolean disable) {
        for (int i = 0; i < labels.length; i++) {
            labels[i].setDisable(disable);
        }
    }

    @FXML
    protected void applyButtonOnClick() {
        boolean comboBoxStatus = checkCheckBoxes(typeConsultationComboBox, placeComboBox, consultantComboBox, placeStudyComboBox, placeWorkComboBox);
        boolean textFieldStatus = true;
        if (comboBoxStatus) {
            textFieldStatus = checkTextFields(timeTextField, ageTextField,
                    fullNameTextField, diagnosisTextField, numberTextField);
        }
        if (textFieldStatus && comboBoxStatus) {
            if (datePicker.getValue() != null) {
                if (Regexes.checkTime(timeTextField.getText())) {

                } else {
                    Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), "Некорректная запись времени");
                }
            } else {
                Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), "Дата не выбрана");
            }
        }
    }

    private LocalDate getDateFromDatePicker() {
        LocalDate localDate = datePicker.getValue();
        //Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        //Date date = Date.from(instant);
        return localDate;
    }

    private boolean checkTextFields(TextField... textFields) {
        for (TextField textField : textFields) {
            if (textField.getText().length() == 0) {
                switch (textField.getId()) {
                    case "timeTextField": {
                        Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), "Время не указано");
                        break;
                    }
                    case "ageTextField": {
                        Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), "Возраст не указан");
                        break;
                    }
                    case "fullNameTextField": {
                        Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), "ФИО не указаны");
                        break;
                    }
                    case "diagnosisTextField": {
                        if (healthStatusCheckBox.isSelected()) {
                            Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), "Диагноз не указан");
                        }
                        break;
                    }
                    case "numberTextField": {
                        Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), "Номер телефона не указан");
                        break;
                    }
                }
                return false;
            }
        }
        return true;
    }

    private boolean checkCheckBoxes(ComboBox... comboBoxes) {
        for (ComboBox comboBox : comboBoxes) {
            if (comboBox.getSelectionModel().getSelectedIndex() == -1) {
                switch (comboBox.getId()) {
                    case "typeConsultationComboBox": {
                        Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), "Тип консультации не выбран");
                        break;
                    }
                    case "placeComboBox": {
                        Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), "Место жительства не выбрано");
                        break;
                    }
                    case "consultantComboBox": {
                        Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), "Консультант не выбран");
                        break;
                    }
                    /*case "placeWorkComboBox": {
                        Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), "Место работы не указано");
                        break;
                    }
                    case "placeStudyComboBox": {
                        Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), "Место учебы не указано");
                        break;
                    }*/
                }
                return false;
            }
        }
        return true;
    }

    @FXML
    protected void сheckBoxOnClick() {
        diagnosisTextField.setDisable(!healthStatusCheckBox.isSelected());
    }

    private Stage loadStage(String sceneName, String icon, String title, int width, int height, boolean resizable) throws IOException {
        Stage stage = new Stage();
        AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource(sceneName));
        if (root != null) {
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
            /*stage.setOnCloseRequest(event -> {
                if (showQuestion("Exit from Record", "Отменить внесенные изменения?")) {
                    editStage.close();
                } else {
                    event.consume();
                }
            });*/
        }
        return stage;
    }
}