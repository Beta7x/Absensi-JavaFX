package Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.ResourceBundle;

public class Home implements Initializable {

    @FXML
    private VBox pnItems = null;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnAbsensi;

    @FXML
    private Button btnPDF;

    @FXML
    private Button btnXLSX;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlPDF;

    @FXML
    private Pane pnlAbsensi;

    @FXML
    private Pane pnlHome;

    @FXML
    private Pane pnlXLSX;

    @FXML
    private Pane pnlSettings;

    @FXML
    private Label lblHadir;

    @FXML
    private Label lblSakit;

    @FXML
    private Label lblIzin;

    @FXML
    private Label lblAlfa;

    @FXML
    private DatePicker datePicker;
    private final String pattern = "dd-mm-yyyy";

    @FXML
    private Label lblTime;
    private volatile boolean stop = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Time view
        TimeNow();

        // Date view
        datePicker.setValue(LocalDate.now());

        // Node as Table
        Node[] nodes = new Node[10];
        for (int i = 0; i < nodes.length; i++) {
            try {
                final int j = i;
                nodes[i] = FXMLLoader.load(getClass().getResource("../Fxml/Item.fxml"));
                nodes[i].setOnMouseEntered(event -> {
                    nodes[j].setStyle("-fx-background-color : #009DCE");
                });
                nodes[i].setOnMouseExited(event -> {
                    nodes[j].setStyle("-fx-background-color : #FFFFFF");
                });
                pnItems.getChildren().add(nodes[i]);

            } catch (IOException e) {
                e.printStackTrace();
            }
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
                Platform.runLater(() -> {
                    lblTime.setText(timenow);
                });
            }
        });
        thread.start();
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnPDF) {
            pnlPDF.setStyle("-fx-background-color : #1620A1");
            pnlPDF.toFront();
        }
        if (actionEvent.getSource() == btnXLSX) {
            pnlXLSX.setStyle("-fx-background-color : #53639F");
            pnlXLSX.toFront();
        }
        if (actionEvent.getSource() == btnSettings) {
            pnlSettings.setStyle("-fx-background-color : #424647");
            pnlSettings.toFront();
        }
        if (actionEvent.getSource() == btnHome) {
            pnlHome.setStyle("-fx-background-color : #F8F8F8");
            pnlHome.toFront();
        }
        if(actionEvent.getSource()==btnAbsensi) {
            pnlAbsensi.setStyle("-fx-background-color : #464F67");
            pnlAbsensi.toFront();
        }
        if (actionEvent.getSource()==btnSignout) {
            System.exit(0);
        }
    }
}

