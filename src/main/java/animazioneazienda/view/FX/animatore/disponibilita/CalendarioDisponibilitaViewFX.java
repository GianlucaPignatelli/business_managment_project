package animazioneazienda.view.FX.animatore.disponibilita;

import animazioneazienda.bean.Utente;
import animazioneazienda.dao.animatore.DisponibilitaAnimatoreDAO;
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
    private final Utente utente;
    private final DisponibilitaAnimatoreDAO disponibilitaDAO;

    public CalendarioDisponibilitaViewFX(Stage primaryStage, Utente utente, DisponibilitaAnimatoreDAO disponibilitaDAO) {
        this.primaryStage = primaryStage;
        this.utente = utente;
        this.disponibilitaDAO = disponibilitaDAO;
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

        ImageView inserisciIcon = new ImageView(new Image(getClass().getResourceAsStream("/inserisci.png")));
        inserisciIcon.setFitWidth(26); inserisciIcon.setFitHeight(26);
        Button inserisciBtn = new Button("Inserisci Disponibilità", inserisciIcon);

        ImageView modificaIcon = new ImageView(new Image(getClass().getResourceAsStream("/modifica.png")));
        modificaIcon.setFitWidth(26); modificaIcon.setFitHeight(26);
        Button modificaBtn = new Button("Modifica Disponibilità", modificaIcon);

        ImageView visualizzaIcon = new ImageView(new Image(getClass().getResourceAsStream("/visualizza.png")));
        visualizzaIcon.setFitWidth(26); visualizzaIcon.setFitHeight(26);
        Button visualizzaBtn = new Button("Visualizza Disponibilità", visualizzaIcon);

        ImageView eliminaIcon = new ImageView(new Image(getClass().getResourceAsStream("/elimina.png")));
        eliminaIcon.setFitWidth(26); eliminaIcon.setFitHeight(26);
        Button eliminaBtn = new Button("Elimina Disponibilità", eliminaIcon);

        for (Button b : new Button[]{inserisciBtn, modificaBtn, visualizzaBtn, eliminaBtn}) {
            b.setStyle("-fx-font-size: 16px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
            b.setMaxWidth(320);
            VBox.setMargin(b, new Insets(0,0,8,0));
        }

        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(28); backIcon.setFitWidth(28);
        Button indietroBtn = new Button("", backIcon);
        indietroBtn.setStyle("-fx-background-color: transparent;");
        indietroBtn.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietroBtn);
        boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(18,0,0,0));

        root.getChildren().addAll(titleBox, inserisciBtn, modificaBtn, visualizzaBtn, eliminaBtn, boxIndietro);

        inserisciBtn.setOnAction(ev -> new InserisciDisponibilitaViewFX(primaryStage, utente, disponibilitaDAO).show());
        modificaBtn.setOnAction(ev -> new ModificaDisponibilitaViewFX(primaryStage, utente, disponibilitaDAO).show());
        visualizzaBtn.setOnAction(ev -> new VisualizzaDisponibilitaViewFX(primaryStage, utente, disponibilitaDAO).show());
        eliminaBtn.setOnAction(ev -> new EliminaDisponibilitaViewFX(primaryStage, utente, disponibilitaDAO).show());
        indietroBtn.setOnAction(ev -> {
            // Torna al menu principale animatore
            new animazioneazienda.view.FX.animatore.AnimatoreMenuFX(primaryStage, utente).show();
        });

        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.centerOnScreen();
    }
}