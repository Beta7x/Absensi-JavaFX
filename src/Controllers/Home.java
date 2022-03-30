package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class Home implements Initializable {

    @FXML
    private Button btnHome;

    @FXML
    private Button btnAbsensi;

    @FXML
    private Button btnPegawai;

    @FXML
    private Button btnExport;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignOut;

    @FXML
    protected StackPane stackView = new StackPane();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colorHome();
        Parent fxml = null;
        try {
            fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/HomePane.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stackView.getChildren().removeAll();
        stackView.getChildren().setAll(fxml);

    }

    public void handleClicks(ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() == btnHome) {
            colorHome();
            Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/HomePane.fxml")));
            stackView.getChildren().removeAll();
            stackView.getChildren().setAll(fxml);

        }
        if(actionEvent.getSource()==btnAbsensi) {
            colorAbsen();
            Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/AbsensiPane.fxml")));
            stackView.getChildren().removeAll();
            stackView.getChildren().setAll(fxml);

        }
        if (actionEvent.getSource() == btnPegawai) {
            colorPegawai();
            Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/PegawaiPane.fxml")));
            stackView.getChildren().removeAll();
            stackView.getChildren().setAll(fxml);
        }
        if (actionEvent.getSource() == btnExport) {
            colorExport();
            Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/ExportPane.fxml")));
            stackView.getChildren().removeAll();
            stackView.getChildren().setAll(fxml);

        }
        if (actionEvent.getSource() == btnSettings) {
            colorSetting();
            Parent fxml = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/SettingPane.fxml")));
            stackView.getChildren().removeAll();
            stackView.getChildren().setAll(fxml);

        }
        if (actionEvent.getSource()==btnSignOut) {
            colorSignOut();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText("Keluar?");
            alert.setContentText("Keluar");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                System.exit(0);
                System.out.println("logout");
            }
        }
    }

    private void colorHome() {
        btnHome.setStyle("-fx-background-color: #47D298");
        btnAbsensi.setStyle("-fx-background-color: transparent");
        btnPegawai.setStyle("-fx-background-color: transparent");
        btnExport.setStyle("-fx-background-color: transparent");
        btnSettings.setStyle("-fx-background-color: transparent");
        btnSignOut.setStyle("-fx-background-color: transparent");
    }

    private void colorAbsen() {
        btnHome.setStyle("-fx-background-color: transparent");
        btnAbsensi.setStyle("-fx-background-color: #47D298");
        btnPegawai.setStyle("-fx-background-color: transparent");
        btnExport.setStyle("-fx-background-color: transparent");
        btnSettings.setStyle("-fx-background-color: transparent");
        btnSignOut.setStyle("-fx-background-color: transparent");
    }
    
    private void colorPegawai() {
        btnHome.setStyle("-fx-background-color: transparent");
        btnAbsensi.setStyle("-fx-background-color: transparent");
        btnPegawai.setStyle("-fx-background-color: #47D298");
        btnExport.setStyle("-fx-background-color: transparent");
        btnSettings.setStyle("-fx-background-color: transparent");
        btnSignOut.setStyle("-fx-background-color: transparent");
    }

    private void colorExport() {
        btnHome.setStyle("-fx-background-color: transparent");
        btnAbsensi.setStyle("-fx-background-color: transparent");
        btnPegawai.setStyle("-fx-background-color: transparent");
        btnExport.setStyle("-fx-background-color: #47D298");
        btnSettings.setStyle("-fx-background-color: transparent");
        btnSignOut.setStyle("-fx-background-color: transparent");
    }

    private void colorSetting() {
        btnHome.setStyle("-fx-background-color: transparent");
        btnAbsensi.setStyle("-fx-background-color: transparent");
        btnPegawai.setStyle("-fx-background-color: transparent");
        btnExport.setStyle("-fx-background-color: transparent");
        btnSettings.setStyle("-fx-background-color: #47D298");
        btnSignOut.setStyle("-fx-background-color: transparent");
    }

    private void colorSignOut() {
        btnHome.setStyle("-fx-background-color: transparent");
        btnAbsensi.setStyle("-fx-background-color: transparent");
        btnPegawai.setStyle("-fx-background-color: transparent");
        btnExport.setStyle("-fx-background-color: transparent");
        btnSettings.setStyle("-fx-background-color: transparent");
        btnSignOut.setStyle("-fx-background-color: #47D298");
    }

}

