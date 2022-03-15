package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Item extends HBox {
    @FXML
    private Label lblNoUrut;

    @FXML
    private Label lblWaktu;

    @FXML
    private Label lblNama;

    @FXML
    private Label lblTanggal;

    @FXML
    private Button btnStatus;

    public void setItem(String nomor, String waktu, String nama, String tanggal, String status) {
        lblNoUrut.setText(nomor);
        lblWaktu.setText(waktu);
        lblNama.setText(nama);
        lblTanggal.setText(tanggal);
        btnStatus.setText(status);

        if (btnStatus.equals("HADIR")) {
            btnStatus.setStyle("-fx-background-color: #0FDB86; -fx-background-radius: 10;");
        } else if (btnStatus.equals("IZIN")) {
            btnStatus.setStyle("-fx-background-color: #FF9E14; -fx-background-radius: 10;");
        } else if (btnStatus.equals("SAKIT")) {
            btnStatus.setStyle("-fx-background-color: #FF0000; -fx-background-radius: 10;");
        } else if (btnStatus.equals("ALPA")) {
            btnStatus.setStyle("-fx-background-color: #676E6F; -fx-background-radius: 10;");
        }
    }
}
