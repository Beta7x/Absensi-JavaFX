package Controllers;

import Utils.Connections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private ImageView close;

    // Database Connection
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    public void handleButtonAction(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == btnLogin) {
            // Login
            if (logIn().equals("Success!")) {
                try {
                    Node node = (Node) mouseEvent.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/Fxml/Home.fxml")));
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public void closeClick (MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == close) {
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (con == null) {
            System.out.println("Server Error!");
        } else {
            System.out.println("Server ready to go!");
        }
    }

    public Login() {
        con = Connections.conDB();
    }

    private String logIn() {
        String status = "Success!";
        String user = txtUsername.getText();
        String pass = txtPassword.getText();
        if (user.isEmpty() || pass.isEmpty()) {
            System.out.println("Empty credentials!");
            status = "Error!";
        } else {
            // SQL
            String sql = "SELECT * FROM admins WHERE username = ? AND password = ? ";
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, user);
                preparedStatement.setString(2, pass);
                resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    System.out.println("Enter correct Username/Passsword!");
                } else {
                    System.out.println("Login Successfull...");
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                status = "Exceptions";
            }
        }
        return status;
    }
}
