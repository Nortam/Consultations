import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import check.network.NetworkConnection;
import sqlserver.server.ServerConnection;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import notice.Notice;
import java.io.IOException;

import static data.ConnectConstants.DATABASE_NAME;
import static data.ConnectConstants.LOGIN_AUTHORIZATION;
import static data.ConnectConstants.PASSWORD_AUTHORIZATION;
import static data.Constants.*;
import static data.UserData.*;

public class LoginForm extends Application {

    @FXML
    protected TextField loginField, passwordField, confirmPasswordField;
    @FXML
    protected Button loginButton;
    @FXML
    protected ProgressIndicator progressIndicator;
    @FXML
    protected Circle networkIndicator;

    private ServerConnection serverConnection = null;
    private static Stage loginStage = null;
    private Timeline timelineNetworkIndicator;

    protected static Stage getLoginStage() {
        return loginStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    public void initialize() {
        checkNetworkConnection();
        timelineNetworkIndicator = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkNetworkConnection();
            }
        }));
        timelineNetworkIndicator.setCycleCount(Timeline.INDEFINITE);
        timelineNetworkIndicator.play();
    }

    @Override
    public void start(final Stage primaryStage) throws IOException {
        loginStage = loadStage(
                "login.fxml", "icons/login_icon.png", "Log In", 240, 270, false
        );
    }

    @FXML
    protected void onClickLoginButton(ActionEvent event) throws IOException {
        progressIndicator.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (NetworkConnection.isConnected()) {
                    String login = loginField.getText();
                    String password = passwordField.getText();
                    if (!login.isEmpty()) {
                        if (!password.isEmpty()) {
                            serverConnection = new ServerConnection(DATABASE_NAME, LOGIN_AUTHORIZATION, PASSWORD_AUTHORIZATION);
                            try {
                                serverConnection.connect();
                                if (serverConnection.isConnected()) {
                                    setUserData(serverConnection.getUserDB(login, password));
                                    if (getUserData() != null) {
                                        serverConnection.disconnect();
                                        new MainForm().start(null);
                                        progressIndicator.setVisible(false);
                                        timelineNetworkIndicator.stop();
                                        loginStage.hide();
                                    } else {
                                        throw new Exception();
                                    }
                                } else {
                                    throw new Exception();
                                }
                            } catch (Exception e) {
                                progressIndicator.setVisible(false);
                                Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), CONNECTION_MESSAGE_ERROR);
                            }
                        } else {
                            Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), PASSWORD_IS_EMPTY_ERROR);
                        }
                    } else {
                        Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), LOGIN_IS_EMPTY_ERROR);
                    }
                } else {
                    Notice.showAlertDialog(Alert.AlertType.ERROR, Alert.AlertType.ERROR.toString(), CONNECTION_NETWORK_ERROR);
                }
                progressIndicator.setVisible(false);
            }
        }));
        timeline.play();
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
            if (serverConnection != null
                    && serverConnection.getConnection() != null
                    && serverConnection.isConnected()) {
                serverConnection.disconnect();
            }
            if (timelineNetworkIndicator != null) {
                timelineNetworkIndicator.stop();
            }
            stage.close();
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