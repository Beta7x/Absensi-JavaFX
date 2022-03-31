package Controllers;

import Model.Pegawai;
import Utils.Connections;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TambahPegawaiPane implements Initializable {

    @FXML
    private JFXButton btnBatal;

    @FXML
    private JFXButton btnTambah;

    @FXML
    private TextField txtId;

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
    void buttonControlHandle(ActionEvent event) throws SQLException {
        connection = Connections.conDB();
        String name = txtNama.getText();
        String nomor = txtNIP.getText();
        String jabatan = txtJabatan.getText();
        String golongan = txtGolongan.getText();
        String ttl = txtTanggalLahir.getText();
        String alamat = txtAlamat.getText();
        String idPegawai = txtId.getText();

        if (event.getSource() == btnTambah) {
            if (update == true) {
                query = "UPDATE `employee` SET " +
                        "nomor_pegawai = '"+nomor+"', " +
                        "nama = '"+name+"', jabatan = '"+jabatan+"', " +
                        "golongan = '"+golongan+"', ttl = '"+ttl+"', " +
                        "alamat = '"+alamat+"' WHERE id_pegawai = '"+idPegawai+"';";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
            } else {
                query = "INSERT INTO `employee` (id_pegawai, nomor_pegawai, nama, jabatan, golongan, ttl, alamat) " +
                        "VALUES (NULL, '"+nomor+"', '"+name+"', '"+jabatan+"', '"+golongan+"', '"+ttl+"', '"+alamat+"');";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                clean();


            }
        }
        if (event.getSource() == btnBatal) {
            System.exit(0);
        }
    }

    // Database Connection
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Pegawai pegawai = null;
    private boolean update;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    protected void setTextField(int id, String nama, String NIP, String jabatan, String golongan, String alamat, String tanggal) {
        txtId.setText(String.valueOf(id));
        txtNama.setText(nama);
        txtNIP.setText(NIP);
        txtJabatan.setText(jabatan);
        txtGolongan.setText(golongan);
        txtAlamat.setText(alamat);
        txtTanggalLahir.setText(tanggal);
    }

    protected void setUpdate(boolean b) {
        this.update = b;
    }

    private void clean() {
        txtId.setText(null);
        txtNama.setText(null);
        txtGolongan.setText(null);
        txtJabatan.setText(null);
        txtTanggalLahir.setText(null);
        txtNIP.setText(null);
        txtAlamat.setText(null);
    }
}
