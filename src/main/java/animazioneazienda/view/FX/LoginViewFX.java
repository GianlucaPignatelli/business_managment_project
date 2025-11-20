package animazioneazienda.view.FX;

import animazioneazienda.bean.Utente;
import animazioneazienda.controller.LoginController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.function.Consumer;

public class LoginViewFX {
    private LoginController loginController;
    private Consumer<Utente> onLoginSuccess;

    public void setLoginController(LoginController controller) { this.loginController = controller; }
    public void setOnLoginSuccess(Consumer<Utente> consumer) { this.onLoginSuccess = consumer; }

    public void start(Stage stage) {
        stage.setTitle("Login Utente");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(70));
        grid.setVgap(22);
        grid.setHgap(16);
        grid.setStyle("-fx-alignment: center;");

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        grid.add(emailLabel, 0, 0);
        grid.add(emailField, 1, 0);

        Label pwLabel = new Label("Password:");
        PasswordField pwField = new PasswordField();
        grid.add(pwLabel, 0, 1);
        grid.add(pwField, 1, 1);

        Button loginBtn = new Button("Login");
        grid.add(loginBtn, 1, 2);

        Label messageLabel = new Label("");
        grid.add(messageLabel, 1, 3);

        loginBtn.setOnAction(ev -> {
            String email = emailField.getText().trim();
            String password = pwField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Compila tutti i campi!");
                return;
            }
            if (loginController != null) {
                Utente utente = loginController.doLoginReturnUtente(email, password);
                if (utente != null) {
                    if (onLoginSuccess != null) onLoginSuccess.accept(utente);
                    stage.close();
                } else {
                    messageLabel.setText("Login fallito: credenziali errate o utente inesistente.");
                }
            } else {
                messageLabel.setText("Errore interno: LoginController non impostato.");
            }
        });

        Scene scene = new Scene(grid, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}