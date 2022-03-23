package Controllers;

import Model.Employee;
import Utils.Connections;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ExportPane implements Initializable {

    @FXML
    private JFXComboBox<String> bulanBox;

    @FXML
    private TableColumn<Employee, String> dateCol;

    @FXML
    private TableColumn<Employee, String> idCol;

    @FXML
    private TableColumn<Employee, String> nameCol;

    @FXML
    private TableColumn<Employee, String> statusCol;

    @FXML
    private JFXComboBox<String> tahunBox;

    @FXML
    private TableView<Employee> tblAbsensi;

    @FXML
    private TableColumn<?, ?> timeCol;

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
    Employee employee = null;

    ObservableList<Employee> EmployeeList = FXCollections.observableArrayList();
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
        loadData();
        bulanBox.setItems(bulan);
        tahunBox.setItems(tahun);
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
                    "ORDER BY absence.waktu ASC;";
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

    private JasperPrint generateJasperPrint(String paramName) throws Exception {
        JasperPrint jp = null;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("TANGGAL_SORT", paramName);

        System.out.println("button export clicked!");
        try {
            jp = JasperFillManager.fillReport("report/Laporan_Kehadiran.jasper", param, Connections.conDB());
            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setTitle("Laporan Kehadiran");
            viewer.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
        }
        return jp;
    }

}
