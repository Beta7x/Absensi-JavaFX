package Controllers;

import Model.Absen;
import Utils.Connections;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomePane implements Initializable {

    @FXML
    private TableColumn<Absen, String> dateCol;

    @FXML
    private TableColumn<Absen, String> idCol;

    @FXML
    private TableColumn<Absen, String> nameCol;

    @FXML
    private TableColumn<Absen, String> statusCol;

    @FXML
    private TableColumn<Absen, String> timeCol;

    @FXML
    private TableView<Absen> tblAbsensi;

    @FXML
    private DatePicker datePicker;

    @FXML
    private JFXButton btnGenerate;

    @FXML
    private JFXButton btnClean;

    @FXML
    private Label lblTime;
    private final boolean stop = false;

    @FXML
    private Label lblAlfa = new Label();

    @FXML
    private Label lblHadir = new Label();

    @FXML
    private Label lblIzin = new Label();

    @FXML
    private Label lblSakit = new Label();

    @FXML
    void generateAction(MouseEvent event) {
        if (event.getSource() == btnGenerate) {
            // tambah data otomatis
        }
        if (event.getSource() == btnClean) {
            try {
                query = "DELETE FROM absence WHERE tanggal LIKE '%" + today + "%';";
                if (today != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi");
                    alert.setHeaderText("Hapus Data?");
                    alert.setContentText("Apakah anda yakin akan menghapus seluruh daftar hadir hari ini("+today+")?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        connection = Connections.conDB();
                        assert connection != null;
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.execute();
                        refreshTable();
                    } else {
                        connection.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
     protected void searchAct(MouseEvent event) {
        System.out.println("aksi dijalankan!");
        String todate = datePicker.getValue().toString();
        System.out.println(todate);
    }

    @FXML
    void rowSelect(MouseEvent event) {}

    // Database Connection
    int hadir, izin, sakit, alpa, noUrut;
    String query = null;
    String where = "";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Absen employee = null;

    String key;
    LocalDate today;
    String myDate;

    ObservableList<Absen> EmployeeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set time
        TimeNow();

        // Set local date
        datePicker.setValue(LocalDate.now());
        today = datePicker.getValue();
        System.out.println(today.toString());

        loadData();

        // check button
        checkDate();
    }

    private void checkDate() {
        connection = Connections.conDB();
        try {
            query = "SELECT tanggal FROM `absence` WHERE id_absen IN (SELECT MAX(id_absen) FROM `absence`);";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                myDate = resultSet.getString("tanggal");
                System.out.println(myDate);
                if (today.toString().equals(myDate)) {
                    btnGenerate.setDisable(true);
                    btnClean.setDisable(false);
                } else {
                    System.out.println("Query gagal");
                    btnGenerate.setDisable(false);
                    btnClean.setDisable(true);
                }
            }
            connection.close();
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

    public void loadData() {
        connection = Connections.conDB();
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id_absen"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("id_pegawai"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("waktu"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void refreshTable() {
        try {
//            key = txtSearch.getText();
            EmployeeList.clear();

            query = "SELECT absence.id_absen, employee.nama, absence.tanggal, absence.waktu, absence.status FROM " +
                    "absence INNER JOIN employee on employee.id_pegawai=absence.id_pegawai WHERE " +
                    "absence.tanggal = '" + today +"'";

            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            hadir = 0;
            izin = 0;
            sakit = 0;
            alpa = 0;
            noUrut = 0;

            while (resultSet.next()) {
                noUrut++;
//                numberCol.setCellValueFactory();
                EmployeeList.add(new Absen(
                        resultSet.getInt("id_absen"),
                        resultSet.getString("nama"),
                        resultSet.getString("tanggal"),
                        resultSet.getString("waktu"),
                        resultSet.getString("status")));
                tblAbsensi.setItems(EmployeeList);
                if (resultSet.getString("status").equals("HADIR")) {
                    hadir++;
                } else if (resultSet.getString("status").equals("IZIN")) {
                    izin++;
                } else if (resultSet.getString("status").equals("SAKIT")) {
                    sakit++;
                } else if (resultSet.getString("status").equals("ALPA")) {
                    alpa++;
                }
                tblAbsensi.setItems(EmployeeList);
                lblHadir.setText("" + hadir);
                lblIzin.setText("" + izin);
                lblSakit.setText("" + sakit);
                lblAlfa.setText("" + alpa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
