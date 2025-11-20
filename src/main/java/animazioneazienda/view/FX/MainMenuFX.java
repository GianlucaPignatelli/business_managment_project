package animazioneazienda.view.FX;

import animazioneazienda.bean.Utente;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuFX {
    public void start(Stage stage) {
        VBox root = new VBox(28);
        root.setStyle("-fx-padding: 90; -fx-alignment: center;");

        Label title = new Label("Gestionale Animazione");
        title.getStyleClass().add("title");

        Button btnLogin = new Button("Login");
        Button btnRegistrati = new Button("Registrati");
        Label messageLabel = new Label("");

        root.getChildren().addAll(title, btnLogin, btnRegistrati, messageLabel);

        btnLogin.setMinWidth(180);
        btnRegistrati.setMinWidth(180);

        btnLogin.setOnAction(ev -> {
            LoginViewFX loginViewFX = new LoginViewFX();
            loginViewFX.setLoginController(FXContextHolder.loginController);
            loginViewFX.setOnLoginSuccess(user -> {
                stage.close();
                if (user.getRuolo() == animazioneazienda.bean.Utente.Ruolo.SUPERADMIN) {
                    SuperadminMenuFX menuAdmin = new SuperadminMenuFX();
                    menuAdmin.start(new Stage(), user);
                } else {
                    AdminMenuFX menuAdmin = new AdminMenuFX();
                    menuAdmin.start(new Stage(), user);
                }
            });
            loginViewFX.start(new Stage());
        });

        btnRegistrati.setOnAction(ev -> {
            RegistrazioneViewFX regViewFX = new RegistrazioneViewFX();
            regViewFX.setLoginController(FXContextHolder.loginController);
            regViewFX.setAziendaDAO(FXContextHolder.aziendaDAO);
            regViewFX.start(new Stage());
        });

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}