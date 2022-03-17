package Controllers;

import Model.Student;
import Utils.Connections;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class Home implements Initializable {

    @FXML
    private TableView<Student> tblAbsensi;

    @FXML
    private TableColumn<Student, String> idCol;

    @FXML
    private TableColumn<Student, String> nimCol;

    @FXML
    private TableColumn<Student, String> nameCol;

    @FXML
    private TableColumn<Student, String> birthCol;

    @FXML
    private TableColumn<Student, String> addressCol;

    @FXML
    private TableColumn<Student, String> emailCol;

    @FXML
    private TableColumn<Student, String> statusCol;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnAbsensi;

    @FXML
    private Button btnPDF;

    @FXML
    private Button btnXLSX;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private StackPane stackView;

    @FXML
    private Pane pnlPDF;

    @FXML
    private Pane pnlAbsensi;

    @FXML
    private Pane pnlHome;

    @FXML
    private Pane pnlXLSX;

    @FXML
    private Pane pnlSettings;

    @FXML
    private Label lblHadir;

    @FXML
    private Label lblSakit;

    @FXML
    private Label lblIzin;

    @FXML
    private Label lblAlfa;

    @FXML
    private DatePicker datePicker;
    private final String pattern = "dd-mm-yyyy";

    @FXML
    private Label lblTime;
    private volatile boolean stop = false;

    // Database Connection
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Student student = null;

    ObservableList<Student> StudentsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Time view
        TimeNow();

        // Date view
        datePicker.setValue(LocalDate.now());

        // Data load
        loadData();

    }

    private void loadData() {

        connection = Connections.conDB();
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nimCol.setCellValueFactory(new PropertyValueFactory<>("nim"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthCol.setCellValueFactory(new PropertyValueFactory<>("birth"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void refreshTable() {

        try {
            StudentsList.clear();

            query = "SELECT * FROM `students`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                StudentsList.add(new Student(
                        resultSet.getInt("id_student"),
                        resultSet.getString("nim"),
                        resultSet.getString("name"),
                        resultSet.getDate("birth"),
                        resultSet.getString("address"),
                        resultSet.getString("email")
                ));
                tblAbsensi.setItems(StudentsList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                Platform.runLater(() -> {
                    lblTime.setText(timenow);
                });
            }
        });
        thread.start();
    }

    public void handleClicks(ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() == btnPDF) {
            pnlPDF.setStyle("-fx-background-color : #1620A1");
            pnlPDF.toFront();
        }
        if (actionEvent.getSource() == btnXLSX) {
            pnlXLSX.setStyle("-fx-background-color : #53639F");
            pnlXLSX.toFront();
        }
        if (actionEvent.getSource() == btnSettings) {
            Parent fxml = FXMLLoader.load(getClass().getResource("/Fxml/SettingPane.fxml"));
            stackView.getChildren().removeAll();
            stackView.getChildren().setAll(fxml);
//            pnlSettings.setStyle("-fx-background-color : #424647");
//            pnlSettings.toFront();
        }
        if (actionEvent.getSource() == btnHome) {
            pnlHome.setStyle("-fx-background-color : #F8F8F8");
            pnlHome.toFront();
        }
        if(actionEvent.getSource()==btnAbsensi) {
            Parent fxml = FXMLLoader.load(getClass().getResource("/Fxml/AbsensiPane.fxml"));
            stackView.getChildren().removeAll();
            stackView.getChildren().setAll(fxml);
//            pnlAbsensi.setStyle("-fx-background-color : #464F67");
//            pnlAbsensi.toFront();
        }
        if (actionEvent.getSource()==btnSignout) {
            System.exit(0);
        }
    }
}

