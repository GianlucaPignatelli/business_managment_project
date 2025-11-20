package animazioneazienda.view.FX;

import animazioneazienda.bean.Utente;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SetupWizardViewFX {
    public void start(Stage stage) {
        stage.setTitle("Setup Iniziale Applicazione");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        Label aziendaLabel = new Label("Nome prima azienda:");
        TextField aziendaField = new TextField();
        grid.add(aziendaLabel, 0, 0);
        grid.add(aziendaField, 1, 0);

        Label emailLabel = new Label("Email SUPERADMIN:");
        TextField emailField = new TextField();
        grid.add(emailLabel, 0, 1);
        grid.add(emailField, 1, 1);

        Label pwLabel = new Label("Password SUPERADMIN:");
        PasswordField pwField = new PasswordField();
        grid.add(pwLabel, 0, 2);
        grid.add(pwField, 1, 2);

        Button btn = new Button("Crea e Avvia Gestionale");
        Label message = new Label();
        grid.add(btn, 1, 3);
        grid.add(message, 1, 4);

        btn.setOnAction(ev -> {
            String aziendaNome = aziendaField.getText().trim();
            String email = emailField.getText().trim();
            String password = pwField.getText();

            if (aziendaNome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                message.setText("Compila tutti i campi!");
                return;
            }

            try {
                int aziendaId = FXContextHolder.aziendaDAO.registraAzienda(aziendaNome);
                if (aziendaId <= 0) throw new Exception("Errore creazione azienda");

                boolean res = FXContextHolder.loginController.registraUtente(email, password, Utente.Ruolo.SUPERADMIN, aziendaId);
                if (res) {
                    message.setText("Setup completato. Riavvia l'applicazione!");
                    btn.setDisable(true);
                } else {
                    message.setText("Errore registrazione SUPERADMIN!");
                }
            } catch (Exception e) {
                message.setText("Errore: " + e.getMessage());
            }
        });

        Scene scene = new Scene(grid, 450, 230);
        stage.setScene(scene);
        stage.show();
    }
}