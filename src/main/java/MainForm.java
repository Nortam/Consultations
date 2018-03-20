import data.сollections.DataWeek;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import check.network.NetworkConnection;
import notice.Notice;
import sqlserver.database.administrator.Administrator;
import sqlserver.database.consultant.Consultant;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;
import static data.Constants.*;
import static data.ConnectConstants.*;
import static data.UserData.getUserData;
import static notice.Notice.*;
import static sqlserver.database.administrator.Administrator.*;

public class MainForm extends Application {

    @FXML
    protected Circle networkIndicator;
    @FXML
    protected Label userName;
    @FXML
    protected MenuItem changePasswordMenuItem;
    @FXML
    protected Tab recordTab, calendarTab, settingsTab;
    @FXML
    protected MenuItem exitMenuButton;
    @FXML
    protected TableView<DataWeek> tableRecordTab = new TableView<>();
    @FXML
    protected TableColumn id_columnRecordTab, wd_columnRecordTab, dateCons_columnRecordTab,
            consTime_columnRecordTab, consTypes_columnRecordTab, otm_columnRecordTab,
            person_columnRecordTab, location_columnRecordTab, place_columnRecordTab;
    @FXML
    protected Button editButton, addButton, getConsultationButton, previousWeekButton, nextWeekButton;
    @FXML
    protected DatePicker datePicker;

    protected static Stage mainStage = null;
    protected static ObservableList<DataWeek> dataWeek = FXCollections.observableArrayList();
    private static String[] dateArray = new String[] {"2017", "12", "11"};
    private Timeline timelineNetworkIndicator;
    private String date = "20171211";

    protected static Stage getMainStage() {
        return mainStage;
    }

    @FXML
    public void initialize() {
        datePicker.setValue(LocalDate.now());
        try {
            tableRecordTab.setItems(dataWeek);
            exitMenuButton.setOnAction(event -> {
                mainStage.close();
            });
            if (getUserData().isAdmin()) {
                userName.setText(getUserData().getFullName() + " (Administrator)");
                setAdministrator(new Administrator(
                        DATABASE_NAME,
                        getUserData().getDbAccountLogin(),
                        getUserData().getDbAccountPassword())
                );
                getAdministrator().connect();
            } else {
                userName.setText(getUserData().getFullName() + " (Consultant)");
                setConsultant(new Consultant(
                        DATABASE_NAME,
                        getUserData().getDbAccountLogin(),
                        getUserData().getDbAccountPassword())
                );
                getConsultant().connect();
                calendarTab.setDisable(true);
                calendarTab.setTooltip(new Tooltip(TAB_IS_NOT_AVAILABLE_ERROR));
                settingsTab.setDisable(true);
                settingsTab.setTooltip(calendarTab.getTooltip());
                addButton.setDisable(true);
            }
            updateTableData(date);
            tableRecordTab.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (tableRecordTab.getSelectionModel().getSelectedItem().getPerson() != null) {
                        System.out.println(tableRecordTab.getSelectionModel().getSelectedItem().getPerson());
                    }
                }
            });
            changePasswordMenuItem.setOnAction(event -> {
                showChangePasswordDialog();
            });
            checkNetworkConnection();
            timelineNetworkIndicator = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    checkNetworkConnection();
                }
            }));
            timelineNetworkIndicator.setCycleCount(Timeline.INDEFINITE);
            timelineNetworkIndicator.play();
        } catch (Exception e) {
            showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), e.toString());
        }
    }

    private String[] getDateArrayFromPicker() {
        LocalDate localDate = datePicker.getValue();
        //Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        //Date date = Date.from(instant); 20171211
        String[] numbers = localDate.toString().split("/");
        return dateArray = new String[] {numbers[2], numbers[0], numbers[1]};
    }

    private String[] changeDateArray() {
        return null;
    }

    @FXML
    protected void editButtonOnClick() throws IOException {
        showEditRecordStage("Edit");
    }

    @FXML
    protected void addButtonOnClick() throws IOException {
        showEditRecordStage("Add");
    }

    @FXML
    protected void makeReportOnClickButton() throws Exception {
        new ReportForm().start(null);
    }

    private void updateTableData(String date) throws SQLException {
        if (getUserData().isAdmin()) {
            fillTable(
                    getAdministrator().getTableData(date)
            );
        } else {
            fillTable(
                    getConsultant().getTableData(date)
            );
        }
    }

    @FXML
    protected void datePickerOnAction() throws SQLException {
        while (datePicker.getValue().getDayOfWeek().getValue() > 1) {
            datePicker.setValue(datePicker.getValue().minusDays(1));
        }
        String[] numbers = datePicker.getValue().toString().split("-");
        dateArray = new String[] {numbers[2], numbers[0], numbers[1]};
        String date = dateArray[1] + dateArray[2] + dateArray[0];
        updateTableData(date);
    }

    @FXML
    protected void previousWeekOnClickButton() throws SQLException {
        String[] numbers = datePicker.getValue().toString().split("-");
        datePicker.getValue().getDayOfWeek().minus(7);
        dateArray = new String[] {numbers[2], numbers[0], numbers[1]};

        int year = Integer.valueOf(dateArray[1]), month = Integer.valueOf(dateArray[2]), day = Integer.valueOf(dateArray[0]);
        int countDaysInMonth = getCountDaysInMonth(year, month);
        if (day - 7 < 1) {
            if (month > 1) {
                month--;
            } else {
                month = 12;
                year--;
            }
            day = getCountDaysInMonth(year, month) - (7 - day);
        } else {
            day = day - 7;
        }
        String date = String.valueOf(year)
                + '-' + String.valueOf(month <= 9 ? ('0' + String.valueOf(month)) : month)
                + '-' + String.valueOf(day <= 9 ? ('0' + String.valueOf(day)) : day);
        datePicker.setValue(LocalDate.parse(date));
        updateTableData(String.valueOf(year)
                + String.valueOf(month <= 9 ? ('0' + String.valueOf(month)) : month)
                + String.valueOf(day <= 9 ? ('0' + String.valueOf(day)) : day));
    }

    @FXML
    protected void nextWeekOnClickButton() throws SQLException, ParseException {
        String[] numbers = datePicker.getValue().toString().split("-");
        datePicker.getValue().getDayOfWeek().plus(7);
        dateArray = new String[] {numbers[2], numbers[0], numbers[1]};

        int year = Integer.valueOf(dateArray[1]), month = Integer.valueOf(dateArray[2]), day = Integer.valueOf(dateArray[0]);
        int countDaysInMonth = getCountDaysInMonth(year, month);
        if (day + 7 > getCountDaysInMonth(year, month)) {
            if (month < 12) {
                month++;
            } else {
                month = 01;
                year++;
            }
            day = 7 - (countDaysInMonth - day);
        } else {
            day = day + 7;
        }
        String date = String.valueOf(year)
                + '-' + String.valueOf(month <= 9 ? ('0' + String.valueOf(month)) : month)
                + '-' + String.valueOf(day <= 9 ? ('0' + String.valueOf(day)) : day);
        datePicker.setValue(LocalDate.parse(date));
        updateTableData(String.valueOf(year)
                + String.valueOf(month <= 9 ? ('0' + String.valueOf(month)) : month)
                + String.valueOf(day <= 9 ? ('0' + String.valueOf(day)) : day));
    }

    private int getCountDaysInMonth(int year, int month) {
        YearMonth yearMonthObject = YearMonth.of(year, month);
        return yearMonthObject.lengthOfMonth();
    }

    private void showEditRecordStage(String mode) throws IOException {
        EditForm.setMode(mode);
        if (mode == "Edit") {
            if (tableRecordTab.getSelectionModel().getSelectedIndex() != -1
                    && (tableRecordTab.getSelectionModel().getSelectedIndex() != -1
                    && tableRecordTab.getSelectionModel().getSelectedItem().getTime() != null)) {
                EditForm.setMode(mode);
                EditForm.setIdConsultation(dataWeek.get(tableRecordTab.getSelectionModel().getSelectedIndex()).getId());
                new EditForm().start(null);
            } else {
                Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), "Запись не выбрана");
            }
        } else {
            new EditForm().start(null);
        }
    }

    private void fillTable(ResultSet resultSet) {
        dataWeek.clear();
        try {
            while (resultSet.next()) {
                dataWeek.add(new DataWeek(resultSet.getInt("cons_id"), resultSet.getString("weekName"), resultSet.getDate("DateCons"),
                        resultSet.getTime("consTime"), resultSet.getString("constypes"), resultSet.getString("otm"),
                        resultSet.getString("Person"), resultSet.getString("location"), resultSet.getString("placeworkORstudy"), resultSet.getString("Consultant")));
            }
            int index = 0;
            int count = tableRecordTab.getItems().size();
            for (int i = 1; i < count; i++) {
                if (i != index) {
                    if (wd_columnRecordTab.getCellData(i).equals(wd_columnRecordTab.getCellData(index))) {
                        dataWeek.get(i).setWd(null);
                        dataWeek.get(i).setDate(null);
                    } else {
                        index = i;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showChangePasswordDialog() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Change Password");
        ImageView image = new ImageView(this.getClass().getResource("pictures/password.png").toString());
        image.setFitHeight(32);
        image.setFitWidth(32);
        dialog.setGraphic(image);
        ButtonType applyButton = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(applyButton, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 20));
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirm Password");
        grid.add(new Label("New Password:"), 0, 0);
        grid.add(password, 1, 0);
        grid.add(new Label("Confirm Password:"), 0, 1);
        grid.add(confirmPassword, 1, 1);
        Node loginButton = dialog.getDialogPane().lookupButton(applyButton);
        loginButton.setDisable(true);
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            confirmPassword.setDisable(newValue.trim().isEmpty());
        });
        confirmPassword.setDisable(true);
        confirmPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> password.requestFocus());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == applyButton) {
                return new Pair<>(password.getText(), confirmPassword.getText());
            }
            return null;
        });
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("icons/password_icon.png"));
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(newPassword -> {
            if (newPassword.getKey().length() >= 5) {
                if (newPassword.getKey().length() <= 20) {
                    if (newPassword.getKey() == newPassword.getValue()) {
                        // change password
                    } else {
                        showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), PASSWORDS_DO_NOT_MATCH_ERROR);
                    }
                } else {
                    showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), LONG_PASSWORD_ERROR);
                }
            } else {
                showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), INSUFFICIENT_PASSWORD_LENGTH_ERROR);
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        mainStage = loadStage(
                "main.fxml", "icons/main_icon.png", "Main - Consultations", 1000, 600, true
        );
    }

    @FXML
    protected void exitMenuButtonOnClick() {
        mainStage.close();
    }

    private void exit() {

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
        stage.setOnCloseRequest(event -> {
            if (showQuestion(EXIT_PROFILE_TITLE, EXIT_PROFILE_QUESTION)) {
                if (timelineNetworkIndicator != null) {
                    timelineNetworkIndicator.stop();
                }
                if (getUserData().isAdmin()) {
                    getAdministrator().disconnect();
                } else {
                    getConsultant().disconnect();
                }
                LoginForm.getLoginStage().show();
                mainStage.close();
            } else {
                event.consume();
            }
        });
        return stage;
    }

    private void checkNetworkConnection() {
        if (NetworkConnection.isConnected()) {
            setNetworkIndicatorTip("Network connection status:\nConnected");
            networkIndicator.setFill(Paint.valueOf("#2cb29a"));
        } else {
            setNetworkIndicatorTip("Network connection status:\nNot connected");
            networkIndicator.setFill(Paint.valueOf("#dd0909"));
        }
    }

    private void setNetworkIndicatorTip(String tip) {
        Tooltip.install(
                networkIndicator,
                new Tooltip(tip)
        );
    }
}