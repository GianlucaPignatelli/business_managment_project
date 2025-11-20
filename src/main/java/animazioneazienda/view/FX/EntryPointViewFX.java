package animazioneazienda.view.FX;

import animazioneazienda.bean.Utente;
import animazioneazienda.bean.Azienda;
import animazioneazienda.controller.LoginController;
import animazioneazienda.dao.AziendaDAO;
import animazioneazienda.dao.UtenteDAO;
import animazioneazienda.dao.animatore.*;
import animazioneazienda.view.FX.animatore.AnimatoreMainViewFX;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class EntryPointViewFX extends Application {
    public static AziendaDAO aziendaDAO;
    public static UtenteDAO utenteDAO;
    public static DisponibilitaAnimatoreDAO disponibilitaAnimatoreDAO;
    public static StatusAnimatoreDAO statusAnimatoreDAO;
    public static OffertaLavoroDAO offertaLavoroDAO;
    public static LoginController loginController;

    @Override
    public void start(Stage primaryStage) {
        mostraSplash(primaryStage);
    }

    private void mostraSplash(Stage stage) {
        VBox splashRoot = new VBox(24);
        splashRoot.setAlignment(Pos.CENTER);
        splashRoot.setStyle("-fx-background-color: #111111;");
        splashRoot.setPrefSize(700, 420);

        ImageView logoView = new ImageView();
        try {
            Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
            logoView.setImage(logo);
            logoView.setFitWidth(220);
            logoView.setPreserveRatio(true);
        } catch (Exception ignored) {}

        Label txt = new Label("Benvenuto in Gestionale Animazione!");
        txt.setStyle("-fx-text-fill: #29ABE2; -fx-font-size: 26px; -fx-font-weight: bold;");

        splashRoot.getChildren().addAll(logoView, txt);

        Scene splashScene = new Scene(splashRoot);
        stage.setScene(splashScene);
        stage.setMaximized(true);
        stage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(7));
        pause.setOnFinished(ev -> mostraScelta(stage));
        pause.play();
    }

    private void mostraScelta(Stage stage) {
        VBox root = new VBox(28);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #111111;");
        root.setPrefSize(700, 420);

        ImageView logoView = new ImageView();
        try {
            Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
            logoView.setImage(logo);
            logoView.setFitWidth(90);
            logoView.setPreserveRatio(true);
        } catch (Exception ignored) {}

        Label title = new Label("GESTIONALE ANIMAZIONE");
        title.setStyle("-fx-text-fill: #29ABE2; -fx-font-size: 20px;");

        Button btnLogin = new Button("Login");
        btnLogin.setStyle("-fx-background-color: #29ABE2; -fx-text-fill: white; -fx-font-size: 15px;");
        btnLogin.setMinWidth(180);

        Button btnReg = new Button("Registrati");
        btnReg.setStyle("-fx-background-color: #29ABE2; -fx-text-fill: white; -fx-font-size: 15px;");
        btnReg.setMinWidth(180);

        VBox areaCampi = new VBox(15);
        areaCampi.setAlignment(Pos.CENTER);

        root.getChildren().addAll(logoView, title, btnLogin, btnReg, areaCampi);

        Scene scena = new Scene(root);
        stage.setScene(scena);
        stage.setMaximized(true);

        btnLogin.setOnAction(ev -> mostraCampiLogin(stage, areaCampi));
        btnReg.setOnAction(ev -> mostraCampiRegistrazione(stage, areaCampi));
    }


    private void mostraCampiLogin(Stage stage, VBox container) {
        container.getChildren().clear();

        TextField emailTF = new TextField();
        emailTF.setPromptText("Email");
        emailTF.setMaxWidth(220);

        PasswordField pwTF = new PasswordField();
        pwTF.setPromptText("Password");
        pwTF.setMaxWidth(220);

        Button btnAccedi = new Button("Accedi");
        btnAccedi.setStyle("-fx-background-color: #29ABE2; -fx-text-fill: white;");
        btnAccedi.setMinWidth(160);

        Label esito = new Label();
        esito.setStyle("-fx-text-fill: #FF5252;");

        container.getChildren().addAll(emailTF, pwTF, btnAccedi, esito);

        btnAccedi.setOnAction(ev -> {
            String email = emailTF.getText().trim();
            String pw = pwTF.getText().trim();
            try {
                Utente utente = EntryPointViewFX.loginController.doLoginReturnUtente(email, pw);
                if (utente == null) {
                    esito.setText("Email o password errati!");
                    return;
                }
                if (utente.getRuolo() == Utente.Ruolo.ANIMATORE) {
                    AnimatoreMainViewFX menuFX = new AnimatoreMainViewFX(
                            utente,
                            disponibilitaAnimatoreDAO,
                            statusAnimatoreDAO,
                            offertaLavoroDAO
                    );
                    menuFX.start(stage);
                }
                // Puoi aggiungere qui view per AMMINISTRATORE e SUPERADMIN
            } catch (Exception e) {
                esito.setText("Errore: " + e.getMessage());
            }
        });
    }

    private void mostraCampiRegistrazione(Stage stage, VBox container) {
        container.getChildren().clear();

        TextField emailTF = new TextField();
        emailTF.setPromptText("Email");
        emailTF.setMaxWidth(220);

        PasswordField pwTF = new PasswordField();
        pwTF.setPromptText("Password");
        pwTF.setMaxWidth(220);

        ComboBox<Utente.Ruolo> ruoloCB = new ComboBox<>();
        ruoloCB.getItems().addAll(Utente.Ruolo.ANIMATORE, Utente.Ruolo.AMMINISTRATORE);
        ruoloCB.setPromptText("Ruolo");
        ruoloCB.setMaxWidth(220);

        // ---- ComboBox aziende ----
        ComboBox<Azienda> aziendaCB = new ComboBox<>();
        try {
            List<Azienda> aziende = EntryPointViewFX.aziendaDAO.getAll();
            ObservableList<Azienda> osservabile = FXCollections.observableArrayList(aziende);
            aziendaCB.setItems(osservabile);
            aziendaCB.setPromptText("Azienda");
            aziendaCB.setMaxWidth(220);
            aziendaCB.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Azienda item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNome());
                    }
                }
            });
            aziendaCB.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Azienda item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Azienda");
                    } else {
                        setText(item.getNome());
                    }
                }
            });
        } catch (Exception e) {
            // Se non ci sono aziende
        }

        Button btnReg = new Button("Conferma Registrazione");
        btnReg.setStyle("-fx-background-color: #29ABE2; -fx-text-fill: white;");
        btnReg.setMinWidth(160);

        Label esito = new Label();
        esito.setStyle("-fx-text-fill: #FF5252;");

        container.getChildren().addAll(emailTF, pwTF, ruoloCB, aziendaCB, btnReg, esito);

        btnReg.setOnAction(ev -> {
            String email = emailTF.getText().trim();
            String pw = pwTF.getText().trim();
            Utente.Ruolo ruolo = ruoloCB.getValue();
            Azienda azienda = aziendaCB.getValue();

            if (email.isEmpty() || pw.isEmpty() || ruolo == null || azienda == null) {
                esito.setText("Compila tutti i campi!");
                return;
            }

            int aziendaId = azienda.getId();    // Ottieni l'ID tramite l'oggetto Azienda

            try {
                boolean ok = EntryPointViewFX.loginController.registraUtente(email, pw, ruolo, aziendaId);
                if (!ok) {
                    esito.setText("Registrazione fallita!");
                } else {
                    esito.setText("Registrazione effettuata! Ora puoi effettuare il login.");
                }
            } catch (Exception e) {
                esito.setText("Errore: " + e.getMessage());
            }
        });
    }
}