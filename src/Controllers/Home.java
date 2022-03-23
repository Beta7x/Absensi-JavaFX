package Controllers;

import Model.Employee;
import Utils.Connections;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class Home implements Initializable {

    @FXML
    private TableView<Employee> tblAbsensi;

    @FXML
    private TableColumn<Employee, String> idCol;

    @FXML
    private TableColumn<Employee, String> nameCol;

    @FXML
    private TableColumn<Employee, String> dateCol;

    @FXML
    private TableColumn<Employee, String> timeCol;

    @FXML
    private TableColumn<Employee, String> statusCol;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnAbsensi;

    @FXML
    private Button btnExport;

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
    private TextField txtSearch;

    @FXML
    private DatePicker datePicker;
    private final String pattern = "dd-mm-yyyy";

    @FXML
    private Label lblTime;
    private volatile boolean stop = false;

    @FXML
    void rowSelect(MouseEvent event) {

    }

    // Database Connection
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Employee employee = null;

    ObservableList<Employee> EmployeeList = FXCollections.observableArrayList();
    FilteredList<Employee> filteredData = new FilteredList<>(EmployeeList, b -> true);

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

        idCol.setCellValueFactory(new PropertyValueFactory<>("id_absen"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("id_pegawai"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("waktu"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add button edit
        Callback<TableColumn<Employee, String>, TableCell<Employee, String>> cellFactory = (TableColumn<Employee, String> param) -> {
            // Make cell containing button
            final TableCell<Employee, String> cell = new TableCell<Employee, String>() {
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button status = new Button();
                        status.setText("ALPA");
                        status.setStyle(
                                "-fx-cursor: hand;"
                                + "-fx-background-color: #0FDB86;"
                        );
                    }
                }
            };
            return cell;
        };
    }

    private void refreshTable() {

        try {
            EmployeeList.clear();
            query = "SELECT absence.id_absen, employee.nama, absence.tanggal, absence.waktu, absence.status\n" +
                    "FROM absence INNER JOIN employee on employee.id_pegawai=absence.id_pegawai " +
                    "ORDER BY absence.id_absen AND absence.waktu ASC;";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                EmployeeList.add(new Employee(
                        resultSet.getInt("id_absen"),
                        resultSet.getString("nama"),
                        resultSet.getString("tanggal"),
                        resultSet.getString("waktu"),
                        resultSet.getString("status")));
                tblAbsensi.setItems(EmployeeList);
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
        if (actionEvent.getSource() == btnExport) {
            colorExport();
            Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/ExportPane.fxml")));
            stackView.getChildren().removeAll();
            stackView.getChildren().setAll(fxml);
        }
        if (actionEvent.getSource() == btnSettings) {
            colorSetting();
            Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/SettingPane.fxml")));
            stackView.getChildren().removeAll();
            stackView.getChildren().setAll(fxml);
            datePicker.setVisible(false);
        }
        if (actionEvent.getSource() == btnHome) {
            colorHome();
            Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/HomePane.fxml")));
            stackView.getChildren().removeAll();
            stackView.getChildren().setAll(fxml);
            datePicker.setVisible(true);
        }
        if(actionEvent.getSource()==btnAbsensi) {
            colorAbsen();
            Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/AbsensiPane.fxml")));
            stackView.getChildren().removeAll();
            stackView.getChildren().setAll(fxml);
            datePicker.setVisible(false);
        }
        if (actionEvent.getSource()==btnSignout) {
            colorSignOut();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Please Confirm");
            alert.setHeaderText("Keluar?");
            alert.setContentText("Keluar");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                System.exit(0);
                System.out.println("logout");
            }
        }
    }

    private void colorHome() {
        btnHome.setStyle("-fx-background-color: #47D298");
        btnExport.setStyle("-fx-background-color: transparent");
        btnAbsensi.setStyle("-fx-background-color: transparent");
        btnSettings.setStyle("-fx-background-color: transparent");
        btnSignout.setStyle("-fx-background-color: transparent");
    }

    private void colorAbsen() {
        btnHome.setStyle("-fx-background-color: transparent");
        btnExport.setStyle("-fx-background-color: transparent");
        btnAbsensi.setStyle("-fx-background-color: #47D298");
        btnSettings.setStyle("-fx-background-color: transparent");
        btnSignout.setStyle("-fx-background-color: transparent");
    }

    private void colorExport() {
        btnHome.setStyle("-fx-background-color: transparent");
        btnExport.setStyle("-fx-background-color: #47D298");
        btnAbsensi.setStyle("-fx-background-color: transparent");
        btnSettings.setStyle("-fx-background-color: transparent");
        btnSignout.setStyle("-fx-background-color: transparent");
    }

    private void colorSetting() {
        btnHome.setStyle("-fx-background-color: transparent");
        btnExport.setStyle("-fx-background-color: transparent");
        btnAbsensi.setStyle("-fx-background-color: transparent");
        btnSettings.setStyle("-fx-background-color: #47D298");
        btnSignout.setStyle("-fx-background-color: transparent");
    }

    private void colorSignOut() {
        btnHome.setStyle("-fx-background-color: transparent");
        btnExport.setStyle("-fx-background-color: transparent");
        btnAbsensi.setStyle("-fx-background-color: transparent");
        btnSettings.setStyle("-fx-background-color: transparent");
        btnSignout.setStyle("-fx-background-color: #47D298");
    }

}

