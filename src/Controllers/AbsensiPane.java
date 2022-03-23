package Controllers;

import Model.Employee;
import Utils.Connections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AbsensiPane implements Initializable {

    @FXML
    private ComboBox statusBox;

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
    private TableView<Employee> tblAbsensi;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtTime;

    @FXML
    private Button btnSave;

    @FXML
    void rowSelect(MouseEvent event) {
        Employee clicked = tblAbsensi.getSelectionModel().getSelectedItem();
        txtName.setText(String.valueOf(clicked.getId_pegawai()));
        txtTime.setText(String.valueOf(clicked.getWaktu()));
    }

    // Database Connection
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Employee employee = null;

    ObservableList<Employee> EmployeeList = FXCollections.observableArrayList();
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

        //add cell of button edit
        Callback<TableColumn<Employee, String>, TableCell<Employee, String>> cellFactory = (TableColumn<Employee, String> param) -> {
            // make cell containing buttons
            final TableCell<Employee, String> cell = new TableCell<Employee, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        statusBox.setItems(stats);
    }
}
