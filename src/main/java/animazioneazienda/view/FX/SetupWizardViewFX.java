package animazioneazienda.view.FX;

import animazioneazienda.dao.AziendaDAO;
import animazioneazienda.dao.UtenteDAO;
import animazioneazienda.bean.Utente;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SetupWizardViewFX {
    private final AziendaDAO aziendaDAO;
    private final UtenteDAO utenteDAO;

    public SetupWizardViewFX(AziendaDAO aziendaDAO, UtenteDAO utenteDAO) {
        this.aziendaDAO = aziendaDAO;
        this.utenteDAO = utenteDAO;
    }

    public void mostraWizard(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #181818; -fx-padding: 40; -fx-alignment: center;");

        Label titolo = new Label("Setup Iniziale Applicazione");
        titolo.setFont(Font.font("Arial", 30));
        titolo.setStyle("-fx-text-fill: #1CA9E2;");

        TextField nomeAziendaField = new TextField();
        nomeAziendaField.setPromptText("Nome Azienda");
        nomeAziendaField.setMaxWidth(320);

        TextField emailField = new TextField();
        emailField.setPromptText("Email Superadmin");
        emailField.setMaxWidth(320);

        PasswordField pwField = new PasswordField();
        pwField.setPromptText("Password Superadmin");
        pwField.setMaxWidth(320);

        Button creaBtn = new Button("Crea Superadmin e Azienda");
        creaBtn.setStyle("-fx-font-size: 19px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        creaBtn.setMaxWidth(250);

        Label messageLabel = new Label("");
        messageLabel.setStyle("-fx-text-fill: #1CA9E2;");

        root.getChildren().addAll(titolo, nomeAziendaField, emailField, pwField, creaBtn, messageLabel);

        creaBtn.setOnAction(ev -> {
            String nomeAzienda = nomeAziendaField.getText().trim();
            String email = emailField.getText().trim();
            String pw = pwField.getText().trim();

            if (nomeAzienda.isEmpty() || email.isEmpty() || pw.isEmpty()) {
                messageLabel.setText("Compila tutti i campi!");
                return;
            }
            try {
                int aziendaId = aziendaDAO.registraAzienda(nomeAzienda);
                if (aziendaId <= 0) throw new Exception("Errore creazione azienda");
                boolean res = utenteDAO.insertUtente(
                        new Utente(email, pw, Utente.Ruolo.SUPERADMIN, aziendaId, nomeAzienda)
                );
                if (res) {
                    messageLabel.setText("Setup completato! Superadmin e azienda creati.");
                } else {
                    messageLabel.setText("Errore registrazione superadmin!");
                }
            } catch (Exception e) {
                messageLabel.setText("Setup fallito: " + e.getMessage());
            }
        });

        Scene scene = new Scene(root, 700, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Setup Wizard FX");
        primaryStage.show();
        primaryStage.centerOnScreen();
    }
}