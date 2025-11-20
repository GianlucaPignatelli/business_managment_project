package animazioneazienda.view.FX;

import animazioneazienda.bean.Utente;
import animazioneazienda.dao.AziendaDAO;
import animazioneazienda.controller.LoginController;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RegistrazioneViewFX {
    private AziendaDAO aziendaDAO;
    private LoginController loginController;

    // Costruttore vuoto per utilizzo con setter/injection
    public RegistrazioneViewFX() {}

    // Costruttore diretto per utilizzo con parametri
    public RegistrazioneViewFX(AziendaDAO aziendaDAO, LoginController loginController) {
        this.aziendaDAO = aziendaDAO;
        this.loginController = loginController;
    }

    // Setter per injection style
    public void setAziendaDAO(AziendaDAO aziendaDAO) {
        this.aziendaDAO = aziendaDAO;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void start(Stage stage) {
        VBox root = new VBox(20);
        root.setPrefWidth(400);

        Label messageLabel = new Label();

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        ComboBox<String> ruoloComboBox = new ComboBox<>();
        ruoloComboBox.getItems().addAll("ANIMATORE", "AMMINISTRATORE");
        ruoloComboBox.setPromptText("Ruolo");

        TextField nomeAziendaField = new TextField();
        nomeAziendaField.setPromptText("Nome Azienda");

        Button registraBtn = new Button("Registrati");

        root.getChildren().addAll(emailField, passwordField, ruoloComboBox, nomeAziendaField, registraBtn, messageLabel);

        registraBtn.setOnAction(ev -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String ruoloStr = ruoloComboBox.getValue();
            String nomeAzienda = nomeAziendaField.getText().trim();

            if (!isEmailValid(email)) {
                messageLabel.setText("Email non valida! Usa una email @gmail.com.");
                return;
            }

            Utente.Ruolo ruolo = "AMMINISTRATORE".equals(ruoloStr)
                    ? Utente.Ruolo.AMMINISTRATORE
                    : Utente.Ruolo.ANIMATORE;

            int aziendaId;
            try {
                aziendaId = aziendaDAO.findIdByNome(nomeAzienda);
            } catch (SQLException e) {
                messageLabel.setText("Errore DB durante la ricerca dell'azienda: " + e.getMessage());
                return;
            }

            if (aziendaId == -1) {
                messageLabel.setText("Azienda non esistente! Chiedi al Superadmin di registrare la tua azienda.");
                return;
            }

            // Chiamata corretta all'istanza del LoginController
            boolean successo = loginController.registraUtente(email, password, ruolo, aziendaId);
            if (successo) {
                messageLabel.setText("Registrazione completata con successo!");
            } else {
                messageLabel.setText("Registrazione fallita!");
            }
        });

        stage.setScene(new Scene(root));
        stage.show();
    }

    private boolean isEmailValid(String email) {
        return email != null && email.endsWith("@gmail.com") && email.length() > 10;
    }
}