package animazioneazienda.view.FX.animatore;

import animazioneazienda.bean.Utente;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class AnimatoreStatusViewFX {
    private final Stage primaryStage;
    private final Utente utente;

    public AnimatoreStatusViewFX(Stage primaryStage, Utente utente) {
        this.primaryStage = primaryStage;
        this.utente = utente;
    }

    public void show() {
        VBox root = new VBox(18);
        root.setStyle("-fx-background-color: #181818; -fx-padding: 55; -fx-alignment: center;");
        root.setPrefWidth(900);
        root.setPrefHeight(600);

        // Titolo + icona man.png
        Label titleLabel = new Label("Gestione Status Animatore");
        titleLabel.setFont(Font.font("Arial", 25));
        titleLabel.setTextFill(Color.web("#1CA9E2"));
        ImageView manIcon = new ImageView(new Image(getClass().getResourceAsStream("/man.png")));
        manIcon.setFitHeight(32); manIcon.setFitWidth(32);
        HBox titleBox = new HBox(12, titleLabel, manIcon);
        titleBox.setAlignment(Pos.CENTER);

        TextField nomeMacchina = new TextField();
        nomeMacchina.setPromptText("Nome Macchina");
        nomeMacchina.setMaxWidth(320); nomeMacchina.setPrefHeight(36);
        nomeMacchina.setFont(Font.font("Arial", FontWeight.NORMAL, 17));

        ComboBox<String> comboDimensione = new ComboBox<>();
        comboDimensione.getItems().addAll("Piccola", "Media", "Grande", "Furgone");
        comboDimensione.setPromptText("Dimensione");
        comboDimensione.setMaxWidth(320);

        CheckBox capoAnimatore = new CheckBox("Capoanimatore");
        CheckBox aiutoAnimatore = new CheckBox("Aiutoanimatore");
        CheckBox delivery = new CheckBox("Delivery");
        CheckBox operatoreCarretti = new CheckBox("Operatore carretti");

        capoAnimatore.setStyle("-fx-text-fill: #1CA9E2;");
        aiutoAnimatore.setStyle("-fx-text-fill: #1CA9E2;");
        delivery.setStyle("-fx-text-fill: #1CA9E2;");
        operatoreCarretti.setStyle("-fx-text-fill: #1CA9E2;");

        CheckBox haccp = new CheckBox("Certificato HACCP");
        haccp.setStyle("-fx-text-fill: #1CA9E2;");

        operatoreCarretti.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                haccp.setSelected(false);
                haccp.setDisable(true);
            } else {
                haccp.setDisable(false);
            }
        });
        haccp.setDisable(!operatoreCarretti.isSelected());

        ComboBox<String> comboStato = new ComboBox<>();
        comboStato.getItems().addAll("Disponibile", "Non operativo");
        comboStato.setPromptText("Stato");
        comboStato.setMaxWidth(320);

        Button btnSalva = new Button("Salva Status");
        btnSalva.setStyle("-fx-font-size: 18px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        btnSalva.setMaxWidth(180);

        // Bottone indietro come icona centrato
        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(28); backIcon.setFitWidth(28);
        Button indietroBtn = new Button("", backIcon);
        indietroBtn.setStyle("-fx-background-color: transparent;");
        indietroBtn.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietroBtn);
        boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(22,0,0,0)); // spazio sopra

        Label messageLabel = new Label("");
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        messageLabel.setTextFill(Color.web("#1CA9E2"));
        messageLabel.setAlignment(Pos.CENTER);

        HBox salvaBox = new HBox(btnSalva);
        salvaBox.setAlignment(Pos.CENTER);
        salvaBox.setPadding(new Insets(12,0,0,0));

        root.getChildren().addAll(
                titleBox,
                nomeMacchina,
                comboDimensione,
                capoAnimatore, aiutoAnimatore, delivery, operatoreCarretti,
                haccp,
                comboStato,
                salvaBox,
                boxIndietro,
                messageLabel
        );

        btnSalva.setOnAction(ev -> {
            messageLabel.setText("Status salvato!");
        });

        indietroBtn.setOnAction(ev -> {
            AnimatoreMenuFX menu = new AnimatoreMenuFX(primaryStage, utente);
            menu.show();
        });

        Scene scena = new Scene(root, 900, 600);
        primaryStage.setScene(scena);
        primaryStage.centerOnScreen();
    }
}