package animazioneazienda.view.FX.animatore;

import animazioneazienda.bean.Utente;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class OfferteAnimatoreViewFX {
    private final Stage primaryStage;
    private final Utente utente;

    public OfferteAnimatoreViewFX(Stage primaryStage, Utente utente) {
        this.primaryStage = primaryStage;
        this.utente = utente;
    }

    public void show() {
        VBox root = new VBox(30);
        root.setStyle("-fx-background-color: #181818; -fx-alignment: center;");
        root.setPrefWidth(900);
        root.setPrefHeight(600);

        Label titleLabel = new Label("Offerte di Lavoro Assegnate");
        titleLabel.setFont(Font.font("Arial", 26));
        titleLabel.setTextFill(Color.web("#1CA9E2"));

        // Placeholder: qui andrà la lista offerte quando sarà sviluppata
        Label placeholder = new Label("Nessuna offerta disponibile al momento.");
        placeholder.setFont(Font.font("Arial", 18));
        placeholder.setTextFill(Color.web("#1CA9E2"));

        Button btnBack = new Button("Torna al Menù Animatore");
        btnBack.setStyle("-fx-font-size: 17px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        btnBack.setOnAction(ev -> new AnimatoreMenuFX(primaryStage, utente).show());

        root.getChildren().addAll(titleLabel, placeholder, btnBack);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setWidth(900);
        primaryStage.setHeight(600);
        primaryStage.centerOnScreen();
    }
}