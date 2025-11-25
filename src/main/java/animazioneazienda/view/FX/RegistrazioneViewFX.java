package animazioneazienda.view.FX;

import animazioneazienda.bean.Utente;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class RegistrazioneViewFX {
    private final Stage primaryStage;
    public RegistrazioneViewFX(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void show() {
        VBox root = new VBox(18);
        root.setStyle("-fx-background-color: #181818; -fx-padding: 40; -fx-alignment: center;");
        root.setPrefWidth(900); root.setPrefHeight(600);

        Label titleLabel = new Label("Compila i campi per registrarti");
        titleLabel.setFont(Font.font("Arial", 26));
        titleLabel.setTextFill(Color.web("#1CA9E2"));
        ImageView loginIcon = new ImageView(new Image(getClass().getResourceAsStream("/login.png")));
        loginIcon.setFitHeight(38); loginIcon.setFitWidth(38); // più grande se serve
        HBox titleBox = new HBox(12, titleLabel, loginIcon);
        titleBox.setAlignment(Pos.CENTER);

        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome");
        nomeField.setMaxWidth(360);
        nomeField.setFont(Font.font("Arial", FontWeight.NORMAL, 17));
        nomeField.setPrefHeight(48);

        TextField cognomeField = new TextField();
        cognomeField.setPromptText("Cognome");
        cognomeField.setMaxWidth(360);
        cognomeField.setFont(Font.font("Arial", FontWeight.NORMAL, 17));
        cognomeField.setPrefHeight(48);

        ComboBox<String> sessoCombo = new ComboBox<>();
        sessoCombo.getItems().addAll("Maschio", "Femmina", "Altro");
        sessoCombo.setPromptText("Sesso"); sessoCombo.setMaxWidth(120);
        sessoCombo.setPrefHeight(44);

        DatePicker dataNascitaPicker = new DatePicker();
        dataNascitaPicker.setPromptText("Data di nascita"); dataNascitaPicker.setMaxWidth(180);
        dataNascitaPicker.setPrefHeight(44);

        TextField emailField = new TextField();
        emailField.setPromptText("Email"); emailField.setMaxWidth(360);
        emailField.setPrefHeight(48);
        emailField.setFont(Font.font("Arial", FontWeight.NORMAL, 17));

        PasswordField passwordField = new PasswordField();
        TextField passwordVisibleField = new TextField();
        passwordVisibleField.setManaged(false);
        passwordVisibleField.setVisible(false);
        passwordVisibleField.setMaxWidth(360);
        passwordVisibleField.setPrefHeight(48);
        passwordVisibleField.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        passwordField.setPrefHeight(48);
        passwordField.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        ImageView eyeImg = new ImageView(new Image(getClass().getResourceAsStream("/eye.png")));
        eyeImg.setFitHeight(32); eyeImg.setFitWidth(32); eyeImg.setPickOnBounds(true);
        StackPane pwStack = new StackPane(passwordField, passwordVisibleField, eyeImg);
        pwStack.setMaxWidth(360); pwStack.setPrefHeight(48);
        StackPane.setAlignment(eyeImg, Pos.CENTER_RIGHT); StackPane.setMargin(eyeImg, new Insets(0,12,0,0));
        passwordField.setStyle("-fx-padding: 0 54 0 0;");
        passwordVisibleField.setStyle("-fx-padding: 0 54 0 0;");
        eyeImg.setOnMouseClicked(e -> {
            boolean showing = passwordVisibleField.isVisible();
            if (!showing) {
                passwordVisibleField.setText(passwordField.getText());
                passwordVisibleField.setVisible(true);
                passwordVisibleField.setManaged(true);
                passwordField.setVisible(false);
                passwordField.setManaged(false);
            } else {
                passwordField.setText(passwordVisibleField.getText());
                passwordVisibleField.setVisible(false);
                passwordVisibleField.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
            }
        });

        ComboBox<String> ruoloComboBox = new ComboBox<>();
        ruoloComboBox.getItems().addAll("ANIMATORE", "AMMINISTRATORE");
        ruoloComboBox.setPromptText("Ruolo"); ruoloComboBox.setMaxWidth(360);
        ruoloComboBox.setPrefHeight(44);

        TextField nomeAziendaField = new TextField();
        nomeAziendaField.setPromptText("Nome Azienda");
        nomeAziendaField.setMaxWidth(360); nomeAziendaField.setPrefHeight(48);
        nomeAziendaField.setFont(Font.font("Arial", FontWeight.NORMAL, 17));

        Button registraBtn = new Button("Registrati");
        registraBtn.setStyle("-fx-font-size: 19px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        registraBtn.setMaxWidth(160);

        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(26); backIcon.setFitWidth(26);
        Button indietroBtn = new Button("", backIcon);
        indietroBtn.setStyle("-fx-background-color: transparent;");
        indietroBtn.setMinSize(40, 40);
        HBox indietroBox = new HBox(indietroBtn);
        indietroBox.setAlignment(Pos.CENTER); indietroBox.setPadding(new Insets(16, 0, 0, 0));

        Label messageLabel = new Label("");
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        messageLabel.setTextFill(Color.web("#1CA9E2"));
        messageLabel.setAlignment(Pos.CENTER);

        HBox registratiBox = new HBox(registraBtn);
        registratiBox.setAlignment(Pos.CENTER);
        registratiBox.setPadding(new Insets(12,0,0,0));

        root.getChildren().addAll(
                titleBox, nomeField, cognomeField, sessoCombo, dataNascitaPicker, emailField, pwStack,
                ruoloComboBox, nomeAziendaField, indietroBox, registratiBox, messageLabel
        );

        registraBtn.setOnAction(ev -> {
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String sesso = sessoCombo.getValue();
            if ("Maschio".equals(sesso)) sesso = "M";
            else if ("Femmina".equals(sesso)) sesso = "F";
            else if ("Altro".equals(sesso)) sesso = "A";
            java.time.LocalDate dataNascita = dataNascitaPicker.getValue();
            String email = emailField.getText().trim();
            String password = passwordField.isVisible()
                    ? passwordField.getText().trim()
                    : passwordVisibleField.getText().trim();
            String ruoloStr = ruoloComboBox.getValue();
            String nomeAzienda = nomeAziendaField.getText().trim();
            if (nome.isEmpty() || cognome.isEmpty() || sesso == null || dataNascita == null) {
                messageLabel.setText("Compila tutti i dati personali!"); return;
            }
            if (!isEmailValid(email)) {
                messageLabel.setText("Email non valida! Usa una email @gmail.com."); return;
            }
            Utente.Ruolo ruolo = "AMMINISTRATORE".equals(ruoloStr)
                    ? Utente.Ruolo.AMMINISTRATORE
                    : Utente.Ruolo.ANIMATORE;
            int aziendaId;
            try {
                aziendaId = EntryPointViewFX.aziendaDAO.findIdByNome(nomeAzienda);
            } catch (Exception e) {
                messageLabel.setText("Errore DB durante la ricerca dell'azienda: " + e.getMessage());
                return;
            }
            if (aziendaId == -1) {
                messageLabel.setText("Azienda non esistente! Chiedi al Superadmin di registrare la tua azienda."); return;
            }
            Utente nuovo = new Utente(email, password, ruolo, aziendaId, nomeAzienda,
                    nome, cognome, sesso, java.sql.Date.valueOf(dataNascita));
            try {
                boolean successo = EntryPointViewFX.utenteDAO.insertUtente(nuovo);
                if (successo) {
                    messageLabel.setText("Registrazione completata con successo!");
                } else {
                    messageLabel.setText("Registrazione fallita!");
                }
            } catch (Exception ex) {
                messageLabel.setText("Errore inserimento: " + ex.getMessage());
            }
        });
        indietroBtn.setOnAction(ev -> {
            MainMenuFX mainMenu = new MainMenuFX(primaryStage);
            mainMenu.show();
        });

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene); primaryStage.centerOnScreen();
    }

    private boolean isEmailValid(String email) {
        return email != null && email.endsWith("@gmail.com") && email.length() > 10;
    }
}