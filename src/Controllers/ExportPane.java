package Controllers;

import Model.Absen;
import Utils.Connections;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ExportPane implements Initializable {

    @FXML
    private DatePicker datePicker;

    @FXML
    private ImageView imgLup;

    @FXML
    private Label lblTime;

    @FXML
    private TextField txtSearch;

    @FXML
    private JFXComboBox<String> bulanBox;

    @FXML
    private TableColumn<Absen, String> dateCol;

    @FXML
    private TableColumn<Absen, String> idCol;

    @FXML
    private TableColumn<Absen, String> nameCol;

    @FXML
    private TableColumn<Absen, String> statusCol;

    @FXML
    private JFXComboBox<String> tahunBox;

    @FXML
    private TableView<Absen> tblAbsensi;

    @FXML
    private TableColumn<Absen, String> timeCol;

    @FXML
    private JFXButton exportBtn;

    @FXML
    void searchAct(KeyEvent event) {
        loadData();
    }

    @FXML
    void showData() {
        try {
            if ("Januari".equals(bulanBox.getSelectionModel().getSelectedItem())) {
                bulanValue = "01";
            } else if ("Februari".equals(bulanBox.getSelectionModel().getSelectedItem())) {
                bulanValue = "02";
            } else if ("Maret".equals(bulanBox.getSelectionModel().getSelectedItem())) {
                bulanValue = "03";
            } else if ("April".equals(bulanBox.getSelectionModel().getSelectedItem())) {
                bulanValue = "04";
            } else if ("Mei".equals(bulanBox.getSelectionModel().getSelectedItem())) {
                bulanValue = "05";
            } else if ("Juni".equals(bulanBox.getSelectionModel().getSelectedItem())) {
                bulanValue = "06";
            } else if ("Juli".equals(bulanBox.getSelectionModel().getSelectedItem())) {
                bulanValue = "07";
            } else if ("Agustus".equals(bulanBox.getSelectionModel().getSelectedItem())) {
                bulanValue = "08";
            } else if ("September".equals(bulanBox.getSelectionModel().getSelectedItem())) {
                bulanValue = "09";
            } else if ("Oktober".equals(bulanBox.getSelectionModel().getSelectedItem())) {
                bulanValue = "10";
            } else if ("November".equals(bulanBox.getSelectionModel().getSelectedItem())) {
                bulanValue = "11";
            } else if ("Desember".equals(bulanBox.getSelectionModel().getSelectedItem())) {
                bulanValue = "01";
            } else {
                bulanValue = " ";
            }

            System.out.println(bulanValue);

            tahunValue = tahunBox.getSelectionModel().getSelectedItem();

            if (tahunValue != null) {
                generateJasperPrint("%"+tahunValue+"-"+bulanValue+"%");
            } else {
                // Popup Notification
                title = "Export Error";
                message = "Bulan dan Tahun Kosong!";
                tray = new TrayNotification();
                type = AnimationType.POPUP;

                tray.setAnimationType(type);
                tray.setTitle(title);
                tray.setMessage(message);
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(Duration.millis(3000));
                generateJasperPrint("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // value
    String bulanValue = "10";
    String tahunValue;

    // Notification
    String title = null;
    String message = null;
    TrayNotification tray;
    AnimationType type;

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
    ObservableList<String> bulan = FXCollections.observableArrayList(
            "Januari",
            "Februari",
            "Maret",
            "April",
            "Mei",
            "Juni",
            "Juli",
            "Agustus",
            "September",
            "Oktober",
            "Desember"
    );
    ObservableList<String> tahun = FXCollections.observableArrayList(
            "2022",
            "2023",
            "2024",
            "2025",
            "2026",
            "2027",
            "2028",
            "2029",
            "2030"
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TimeNow();
        datePicker.setValue(LocalDate.now());
        loadData();
        bulanBox.setItems(bulan);
        tahunBox.setItems(tahun);
    }

    protected void TimeNow() {
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
            sql = key.isEmpty()? "" : "WHERE absence.id_absen " + "LIKE '%" + key + "%' OR employee.nama LIKE " +
                    "'%" + key + "%' OR absence.tanggal LIKE '%" + key + "%' OR absence.waktu LIKE '%" + key + "%' OR " +
                    "absence.status LIKE '%" + key + "%'";
            query = "SELECT absence.id_absen, employee.nama, absence.tanggal, absence.waktu, absence.status\n" +
                    "FROM absence INNER JOIN employee on employee.id_pegawai=absence.id_pegawai " + sql + " ORDER BY absence.waktu ASC;";
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

    private JasperPrint generateJasperPrint(String paramName) throws Exception {
        JasperPrint jp = null;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("TANGGAL_SORT", paramName);

        System.out.println("button export clicked!");
        try {
            jp = JasperFillManager.fillReport("/home/venom/IdeaProjects/AbsensiFX/src/report/Laporan_Kehadiran.jasper", param, Connections.conDB());
            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setTitle("Laporan Kehadiran");
            viewer.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
        }
        return jp;
    }

}
