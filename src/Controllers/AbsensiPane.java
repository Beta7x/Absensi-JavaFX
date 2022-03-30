package Controllers;

import Model.Absen;
import Utils.Connections;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class AbsensiPane implements Initializable {

    @FXML
    private ComboBox statusBox;

    @FXML
    private TableColumn<Absen, String> idCol;

    @FXML
    private TableColumn<Absen, String> nameCol;

    @FXML
    private TableColumn<Absen, String> dateCol;

    @FXML
    private TableColumn<Absen, String> timeCol;

    @FXML
    private TableColumn<Absen, String> statusCol;

    @FXML
    private TableView<Absen> tblAbsensi;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblTime;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtTime;

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtSearch;

    @FXML
    private DatePicker dateAbsen;

    @FXML
    private ImageView imgLup;

    @FXML
    void rowSelect(MouseEvent event) {
        Absen clicked = tblAbsensi.getSelectionModel().getSelectedItem();
        txtName.setText(String.valueOf(clicked.getId_pegawai()));
        txtTime.setText(String.valueOf(clicked.getWaktu()));
    }

    @FXML
    void searchAct(KeyEvent event) {
        loadData();
    }


    // Database Connection
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Absen employee = null;
    String sql, key;
    int noUrut;
    private final boolean stop = false;

    ObservableList<Absen> EmployeeList = FXCollections.observableArrayList();
    ObservableList<String> stats = FXCollections.observableArrayList(
            "ALPA",
            "HADIR",
            "IZIN",
            "SAKIT"
    );

    private void loadData() {

        connection = Connections.conDB();
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id_absen"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("id_pegawai"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("waktu"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void refreshTable() {

        try {
            EmployeeList.clear();
            key = txtSearch.getText();
            sql = key.isEmpty()? "" : "WHERE absence.id_absen " +
                    "LIKE '%" + key + "%' OR employee.nama LIKE '%" + key + "%' OR absence.tanggal LIKE " +
                    "'%" + key + "%' OR absence.waktu LIKE '%" + key + "%' OR absence.status LIKE '%" + key + "%'";
            query = "SELECT absence.id_absen, employee.nama, absence.tanggal, absence.waktu, absence.status\n" +
                    "FROM absence INNER JOIN employee on employee.id_pegawai=absence.id_pegawai " + sql +
                    " ORDER BY absence.id_absen DESC;";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            noUrut = 0;

            while (resultSet.next()) {
                EmployeeList.add(new Absen(
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
                Platform.runLater(() -> lblTime.setText(timenow));
            }
        });
        thread.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TimeNow();
        loadData();
        statusBox.setItems(stats);
    }
}
