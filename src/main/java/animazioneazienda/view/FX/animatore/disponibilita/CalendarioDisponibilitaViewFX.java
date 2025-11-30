package animazioneazienda.view.FX.animatore.disponibilita;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.dao.animatore.disponibilita.VisualizzaDisponibilitaDAO;
import animazioneazienda.dao.animatore.disponibilita.InserisciDisponibilitaDAO;
import animazioneazienda.dao.animatore.disponibilita.ModificaDisponibilitaDAO;
import animazioneazienda.dao.animatore.disponibilita.EliminaDisponibilitaDAO;

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

    // I nuovi DAO modulari
    private final VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO;
    private final InserisciDisponibilitaDAO inserisciDisponibilitaDAO;
    private final ModificaDisponibilitaDAO modificaDisponibilitaDAO;
    private final EliminaDisponibilitaDAO eliminaDisponibilitaDAO;

    public CalendarioDisponibilitaViewFX(
            Stage primaryStage,
            UtenteBean utente,
            VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO,
            InserisciDisponibilitaDAO inserisciDisponibilitaDAO,
            ModificaDisponibilitaDAO modificaDisponibilitaDAO,
            EliminaDisponibilitaDAO eliminaDisponibilitaDAO
    ) {
        this.primaryStage = primaryStage;
        this.utente = utente;
        this.visualizzaDisponibilitaDAO = visualizzaDisponibilitaDAO;
        this.inserisciDisponibilitaDAO = inserisciDisponibilitaDAO;
        this.modificaDisponibilitaDAO = modificaDisponibilitaDAO;
        this.eliminaDisponibilitaDAO = eliminaDisponibilitaDAO;
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

        // Pulsanti con azioni specializzate
        Button inserisciBtn = buttonIcon("Inserisci Disponibilità", "/inserisci.png");
        Button modificaBtn = buttonIcon("Modifica Disponibilità", "/modifica.png");
        Button visualizzaBtn = buttonIcon("Visualizza Disponibilità", "/visualizza.png");
        Button eliminaBtn = buttonIcon("Elimina Disponibilità", "/elimina.png");

        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(28); backIcon.setFitWidth(28);
        Button indietroBtn = new Button("", backIcon);
        indietroBtn.setStyle("-fx-background-color: transparent;");
        indietroBtn.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietroBtn);
        boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(18,0,0,0));

        root.getChildren().addAll(titleBox, inserisciBtn, modificaBtn, visualizzaBtn, eliminaBtn, boxIndietro);

        inserisciBtn.setOnAction(ev ->
                new InserisciDisponibilitaViewFX(primaryStage, utente, inserisciDisponibilitaDAO, visualizzaDisponibilitaDAO).show());
        modificaBtn.setOnAction(ev ->
                new ModificaDisponibilitaViewFX(primaryStage, utente, modificaDisponibilitaDAO, visualizzaDisponibilitaDAO).show());
        visualizzaBtn.setOnAction(ev ->
                new VisualizzaDisponibilitaViewFX(primaryStage, utente, visualizzaDisponibilitaDAO).show());
        eliminaBtn.setOnAction(ev ->
                new EliminaDisponibilitaViewFX(primaryStage, utente, eliminaDisponibilitaDAO, visualizzaDisponibilitaDAO).show());

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