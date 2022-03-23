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

public class HomePane implements Initializable {

    @FXML
    private TableColumn<Employee, String> dateCol;

    @FXML
    private TableColumn<Employee, String> idCol;

    @FXML
    private TableColumn<Employee, String> nameCol;

    @FXML
    private TableColumn<Employee, String> statusCol;

    @FXML
    private TableColumn<Employee, String> timeCol;

    @FXML
    private TableView<Employee> tblAbsensi;

    @FXML
    private Label lblAlfa;

    @FXML
    private Label lblHadir;

    @FXML
    private Label lblIzin;

    @FXML
    private Label lblSakit;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

}
