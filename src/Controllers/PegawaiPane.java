package Controllers;

import Model.Pegawai;
import Utils.Connections;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class PegawaiPane implements Initializable {

    @FXML
    private TableColumn<Pegawai, String> NIPCol;

    @FXML
    private TableColumn<Pegawai, String> alamatCol;

    @FXML
    private TableColumn<Pegawai, String> golCol;

    @FXML
    private TableColumn<Pegawai, String> idCol;

    @FXML
    private TableColumn<Pegawai, String> nameCol;

    @FXML
    private TableView<Pegawai> tblAbsensi;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ImageView imgLup;

    @FXML
    private Label lblTime;

    @FXML
    private TextField txtSearch;

    @FXML
    private JFXButton btnDownload;

    @FXML
    void searchAct(KeyEvent event) {
        loadData();
    }

    @FXML
    void downloadAct(ActionEvent event) throws Exception {
        generateJasperPrint();
    }

    @FXML
    private void buttonControlHandle(ActionEvent actionEvent) throws SQLException, IOException {
        if (actionEvent.getSource() == btnEdit) {
            pegawai = tblAbsensi.getSelectionModel().getSelectedItem();
        }
        if (actionEvent.getSource() == btnTambah) {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/TambahPegawaiPane.fxml")));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
//            stage.initOwner();
//            stage.initModality(Modality.APPLICATION_MODAL);
        }
        if (actionEvent.getSource() == btnHapus) {

            pegawai = tblAbsensi.getSelectionModel().getSelectedItem();
            query = "DELETE FROM `employee` WHERE id_pegawai = " + pegawai.getId_pegawai();

            if (pegawai != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Konfirmasi");
                alert.setHeaderText("Hapus Data?");
                alert.setContentText("Apakah anda yakin akan menghapus data ini?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    connection = Connections.conDB();
                    assert connection != null;
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.execute();
                    refreshTable();
                }
            } else {
                title = "Error!";
                message = "Kolom Username dan Password tidak boleh kosong!";
                tray = new TrayNotification();
                type = AnimationType.POPUP;

                tray.setAnimationType(type);
                tray.setTitle(title);
                tray.setMessage(message);
                tray.setNotificationType(NotificationType.ERROR);
                tray.showAndDismiss(Duration.millis(3000));
            }
        }
    }

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnTambah;

    @FXML
    private JFXButton btnHapus;

    // Notification
    String title = null;
    String message = null;
    TrayNotification tray;
    AnimationType type;

    private final boolean stop = false;

    // Database Connection
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Pegawai pegawai = null;

    ObservableList<Pegawai> PegawaiList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TimeNow();
        loadData();
        datePicker.setValue(LocalDate.now());
    }

    private  void loadData() {
        connection = Connections.conDB();
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id_pegawai"));
        NIPCol.setCellValueFactory(new PropertyValueFactory<>("nomor_pegawai"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        golCol.setCellValueFactory(new PropertyValueFactory<>("golongan"));
        alamatCol.setCellValueFactory(new PropertyValueFactory<>("alamat"));
    }

    private void refreshTable() {
        try {
            PegawaiList.clear();
            String key = txtSearch.getText();
            String sql = key.isEmpty()? "" : "WHERE id_pegawai LIKE '%" + key + "%' OR nomor_pegawai LIKE '%" + key + "%' " +
                    "OR nama LIKE '%" + key + "%' OR golongan LIKE '%" + key + "%' OR alamat LIKE '%" + key + "%'";
            query = "SELECT id_pegawai, nomor_pegawai, nama, golongan, alamat FROM employee " + sql;
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                PegawaiList.add(new Pegawai(
                        resultSet.getInt("id_pegawai"),
                        resultSet.getString("nomor_pegawai"),
                        resultSet.getString("nama"),
                        resultSet.getString("golongan"),
                        resultSet.getString("alamat")
                ));
                tblAbsensi.setItems(PegawaiList);
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

    private JasperPrint generateJasperPrint() throws Exception {
        JasperPrint jp = null;
        Map<String, Object> param = new HashMap<String, Object>();

        System.out.println("button export pegawai clicked!");
        try {
            jp = JasperFillManager.fillReport("report/Pegawai.jasper", param, Connections.conDB());
            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setTitle("Daftar Pegawai");
            viewer.setVisible(true);
        } catch (JRException e) {
            e.printStackTrace();
        }
        return jp;
    }

}
