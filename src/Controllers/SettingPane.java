package Controllers;

import Utils.Connections;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class SettingPane implements Initializable {

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblTime;
    private final boolean stop = false;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField txtUser;

    @FXML
    private TextField txtConfirmPass;

    @FXML
    private TextField txtNewPass;

    @FXML
    private TextField txtOldPass;

    // Database Connection
    Connection con;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String sql = null;
    String title;
    String message;
    TrayNotification tray;
    AnimationType type;

    @FXML
    public void handleButtonUpdate(MouseEvent mouseEvent) {
        String user = txtUser.getText();
        String oldPass = txtOldPass.getText();
        String newPass = txtNewPass.getText();
        String confirm = txtConfirmPass.getText();
        if (mouseEvent.getSource() == btnUpdate) {
            if (user.isEmpty() || oldPass.isEmpty() || newPass.isEmpty() || confirm.isEmpty()) {
                title = "Error!";
                message = "Kolom tidak boleh kosong!";
                tray = new TrayNotification();
                type = AnimationType.POPUP;

                tray.setAnimationType(type);
                tray.setTitle(title);
                tray.setMessage(message);
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(Duration.millis(3000));
            } else {
                sql = "SELECT * FROM admins WHERE username = ? AND password = ? ";
                try {
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, user);
                    preparedStatement.setString(2, oldPass);
                    resultSet = preparedStatement.executeQuery();
                    if (!resultSet.next()) {
                        title = "Error!";
                        message = "Username dan Password Tidak Sesuai!";
                        tray = new TrayNotification();
                        type = AnimationType.POPUP;

                        tray.setAnimationType(type);
                        tray.setTitle(title);
                        tray.setMessage(message);
                        tray.setNotificationType(NotificationType.ERROR);
                        tray.showAndDismiss(Duration.millis(3000));

                        System.out.println("Wrong username and password!");
                    } else {
                        if (newPass.equals(confirm) && newPass.length() > 8) {
                            try {
                                sql = "UPDATE admins SET password = '" + confirm + "' WHERE username = '" + user + "';";
                                con.createStatement().executeUpdate(sql);

                                // Notification Popup
                                title = "Success!";
                                message = "Data telah diperbarui!";
                                tray = new TrayNotification();
                                type = AnimationType.POPUP;

                                tray.setAnimationType(type);
                                tray.setTitle(title);
                                tray.setMessage(message);
                                tray.setNotificationType(NotificationType.SUCCESS);
                                tray.showAndDismiss(Duration.millis(3000));

                                System.out.println("Database has been Update!");

                                // Clear all text field
                                txtUser.clear();
                                txtOldPass.clear();
                                txtNewPass.clear();
                                txtConfirmPass.clear();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else if (newPass.length() < 8) {
                            title = "Error!";
                            message = "Panjang password minimal 8 karakter!";
                            tray = new TrayNotification();
                            type = AnimationType.POPUP;

                            tray.setAnimationType(type);
                            tray.setTitle(title);
                            tray.setMessage(message);
                            tray.setNotificationType(NotificationType.ERROR);
                            tray.showAndDismiss(Duration.millis(3000));
                        } else {
                            title = "Error!";
                            message = "Konfirmasi Password tidak Cocok!";
                            tray = new TrayNotification();
                            type = AnimationType.POPUP;

                            tray.setAnimationType(type);
                            tray.setTitle(title);
                            tray.setMessage(message);
                            tray.setNotificationType(NotificationType.ERROR);
                            tray.showAndDismiss(Duration.millis(3000));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set time
        TimeNow();

        // Set local date
        datePicker.setValue(LocalDate.now());

        if (con == null) {
            System.out.println("Server Error!");
        } else {
            System.out.println("Server ready to go!");
        }

    }

    public SettingPane() {
        con = Connections.conDB();
    }

    private void TimeNow() {
        Thread thread = new Thread(() -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("k:mm:ss");
            while (!stop) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final String timenow = simpleDateFormat.format(new Date());
                Platform.runLater(() -> lblTime.setText(timenow));
            }
        });
        thread.start();
    }
}
