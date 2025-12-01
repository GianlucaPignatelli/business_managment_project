package animazioneazienda.view.FX.animatore.disponibilita;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.dao.animatore.disponibilita.DisponibilitaAnimatoreRepository;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CalendarioDisponibilitaViewFX {
    private final Stage primaryStage;
    private final UtenteBean utente;
    private final DisponibilitaAnimatoreRepository disponibilitaRepository;

    public CalendarioDisponibilitaViewFX(
            Stage primaryStage,
            UtenteBean utente,
            DisponibilitaAnimatoreRepository disponibilitaRepository
    ) {
        this.primaryStage = primaryStage;
        this.utente = utente;
        this.disponibilitaRepository = disponibilitaRepository;
    }

    public void show() {
        VBox root = new VBox(18);
        root.setPadding(new Insets(28));
        root.setAlignment(Pos.CENTER);
        root.setPrefWidth(900);
        root.setPrefHeight(600);
        root.setStyle("-fx-background-color: #181818;");

        Label title = new Label("Gestione Disponibilità Animatore");
        title.setFont(Font.font("Arial", 24));
        title.setTextFill(Color.web("#1CA9E2"));
        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);

        Button visualizzaBtn = buttonIcon("Visualizza Disponibilità", "/visualizza.png");
        Button inserisciBtn = buttonIcon("Inserisci Disponibilità", "/inserisci.png");
        Button modificaBtn = buttonIcon("Modifica Disponibilità", "/modifica.png");
        Button eliminaBtn = buttonIcon("Elimina Disponibilità", "/elimina.png");

        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(28); backIcon.setFitWidth(28);
        Button indietroBtn = new Button("", backIcon);
        indietroBtn.setStyle("-fx-background-color: transparent;");
        indietroBtn.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietroBtn);
        boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(18, 0, 0, 0));

        root.getChildren().addAll(titleBox, visualizzaBtn, inserisciBtn, modificaBtn, eliminaBtn, boxIndietro);

        visualizzaBtn.setOnAction(ev -> new VisualizzaDisponibilitaViewFX(primaryStage, utente, disponibilitaRepository).show());
        inserisciBtn.setOnAction(ev -> new InserisciDisponibilitaViewFX(primaryStage, utente, disponibilitaRepository).show());
        modificaBtn.setOnAction(ev -> new ModificaDisponibilitaViewFX(primaryStage, utente, disponibilitaRepository).show());
        eliminaBtn.setOnAction(ev -> new EliminaDisponibilitaViewFX(primaryStage, utente, disponibilitaRepository).show());

        indietroBtn.setOnAction(ev -> {
            new animazioneazienda.view.FX.animatore.AnimatoreMenuFX(primaryStage, utente).show();
        });

        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.centerOnScreen();
    }

    private Button buttonIcon(String text, String res) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(res)));
        icon.setFitHeight(26); icon.setFitWidth(26);
        Button btn = new Button(text, icon);
        btn.setStyle("-fx-font-size: 16px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        btn.setMaxWidth(320);
        VBox.setMargin(btn, new Insets(0,0,8,0));
        btn.setGraphicTextGap(12);
        return btn;
    }
}