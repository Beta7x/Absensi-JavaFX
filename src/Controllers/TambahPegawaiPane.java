package Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TambahPegawaiPane {

    @FXML
    private JFXButton btnHapus;

    @FXML
    private JFXButton btnTambah;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblTime;

    @FXML
    private TextArea txtAlamat;

    @FXML
    private TextField txtGolongan;

    @FXML
    private TextField txtJabatan;

    @FXML
    private TextField txtNIP;

    @FXML
    private TextField txtNama;

    @FXML
    private TextField txtTanggalLahir;

    @FXML
    void buttonControlHandle(ActionEvent event) {

    }

}
