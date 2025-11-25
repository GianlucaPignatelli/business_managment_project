package animazioneazienda.view.FX.amministratore;

import animazioneazienda.bean.Utente;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminMenuFX {
    public void start(Stage stage, Utente utente) {
        VBox box = new VBox(20);
        box.setStyle("-fx-padding: 38; -fx-alignment: center;");
        Label msg = new Label("Benvenuto, " + utente.getRuolo() + " di " + utente.getAziendaId());
        box.getChildren().addAll(new Label("Menu Utente"), msg);
        stage.setScene(new Scene(box, 320, 150));
        stage.show();
    }
}

/*
package animazioneazienda.view.FX.amministratore;

import animazioneazienda.bean.Utente;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AdminMenuFX {
    public void show(Stage stage, Utente utente) {
        VBox box = new VBox(24);
        box.setStyle("-fx-background-color: #181818; -fx-padding: 38; -fx-alignment: center;");
        box.setPrefWidth(900);
        box.setPrefHeight(600);

        // Banner/logo (puoi cambiare nome file e posizione)
        try {
            Image banner = new Image("admin_banner.png");
            ImageView bannerView = new ImageView(banner);
            bannerView.setFitWidth(320);
            bannerView.setPreserveRatio(true);
            box.getChildren().add(bannerView);
        } catch (Exception ex) {
            // se mancante, niente immagine
        }

        Label benvenuto = new Label("Benvenuto, " + utente.getRuolo() + " di azienda #" + utente.getAziendaId());
        benvenuto.setFont(Font.font("Arial", 28));
        benvenuto.setTextFill(Color.web("#1CA9E2"));

        Label menuLabel = new Label("Dashboard Amministrazione");
        menuLabel.setFont(Font.font("Arial", 22));
        menuLabel.setTextFill(Color.web("#1CA9E2"));

        // Esempio: Card per funzioni
        HBox cardBox = new HBox(28);
        cardBox.setAlignment(Pos.CENTER);

        Button btnGestioneAnimatori = new Button("Gestione Animatori");
        Button btnGestioneEventi = new Button("Gestione Eventi");
        Button btnStatistiche = new Button("Statistiche");
        for (Button btn : new Button[]{btnGestioneAnimatori, btnGestioneEventi, btnStatistiche}) {
            btn.setStyle("-fx-font-size: 18px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818; -fx-padding: 15 35 15 35; -fx-background-radius: 18;");
        }
        cardBox.getChildren().addAll(btnGestioneAnimatori, btnGestioneEventi, btnStatistiche);

        // nello stile dashboard, puoi inserire anche altri box/menu/grafici/icona qui!

        box.getChildren().addAll(
            benvenuto,
            menuLabel,
            cardBox
        );

        Scene scene = new Scene(box, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Pannello di Controllo Amministratore");
        stage.centerOnScreen();
        stage.show();
    }
}
 */