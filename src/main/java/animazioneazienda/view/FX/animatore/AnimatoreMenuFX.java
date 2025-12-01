package animazioneazienda.view.FX.animatore;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.dao.animatore.status.StatusAnimatoreRepository;
import animazioneazienda.dao.animatore.offerta.OffertaLavoroRepository;
import animazioneazienda.dao.animatore.disponibilita.DisponibilitaAnimatoreRepository;
import animazioneazienda.view.FX.EntryPointViewFX;
import animazioneazienda.view.FX.animatore.disponibilita.CalendarioDisponibilitaViewFX;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AnimatoreMenuFX {
    private final Stage primaryStage;
    private final UtenteBean utente;

    public AnimatoreMenuFX(Stage primaryStage, UtenteBean utente) {
        this.primaryStage = primaryStage;
        this.utente = utente;
    }

    public void show() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #181818; -fx-padding: 0;");
        root.setPrefWidth(900);
        root.setPrefHeight(600);

        Label titleLabel = new Label("Menu Animatore");
        titleLabel.setFont(Font.font("Arial", 28));
        titleLabel.setTextFill(Color.web("#1CA9E2"));

        ImageView manIcon = new ImageView(new Image(getClass().getResourceAsStream("/man.png")));
        manIcon.setFitHeight(22); manIcon.setFitWidth(22);
        Button statusBtn = new Button("Status Animatore", manIcon);
        statusBtn.setGraphicTextGap(12);
        statusBtn.setStyle("-fx-font-size: 18px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        statusBtn.setMaxWidth(320);

        ImageView notificationIcon = new ImageView(new Image(getClass().getResourceAsStream("/notification.png")));
        notificationIcon.setFitHeight(22); notificationIcon.setFitWidth(22);
        Button offerteBtn = new Button("Visualizza Offerte", notificationIcon);
        offerteBtn.setGraphicTextGap(12);
        offerteBtn.setStyle("-fx-font-size: 18px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        offerteBtn.setMaxWidth(320);

        ImageView calendarIcon = new ImageView(new Image(getClass().getResourceAsStream("/calendar.png")));
        calendarIcon.setFitHeight(22); calendarIcon.setFitWidth(22);
        Button disponibilitaBtn = new Button("Gestisci Disponibilità", calendarIcon);
        disponibilitaBtn.setGraphicTextGap(12);
        disponibilitaBtn.setStyle("-fx-font-size: 18px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        disponibilitaBtn.setMaxWidth(320);

        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(26); backIcon.setFitWidth(26);
        Button indietroBtn = new Button("", backIcon);
        indietroBtn.setStyle("-fx-background-color: transparent;");
        indietroBtn.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietroBtn);
        boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(16, 0, 0, 0));

        root.getChildren().setAll(
                titleLabel,
                statusBtn,
                offerteBtn,
                disponibilitaBtn,
                boxIndietro
        );

        statusBtn.setOnAction(ev -> {
            StatusAnimatoreRepository statusRepo = EntryPointViewFX.statusAnimatoreRepository;
            AnimatoreStatusViewFX statusView = new AnimatoreStatusViewFX(primaryStage, utente, statusRepo);
            statusView.show();
        });

        offerteBtn.setOnAction(ev -> {
            OffertaLavoroRepository offertaRepo = EntryPointViewFX.offertaLavoroRepository;
            AnimatoreOfferteViewFX offerteView = new AnimatoreOfferteViewFX(primaryStage, utente, offertaRepo);
            offerteView.show();
        });

        disponibilitaBtn.setOnAction(ev -> {
            DisponibilitaAnimatoreRepository disponibilitaRepo = EntryPointViewFX.disponibilitaAnimatoreRepository;
            CalendarioDisponibilitaViewFX disponibilitaView = new CalendarioDisponibilitaViewFX(
                    primaryStage,
                    utente,
                    disponibilitaRepo
            );
            disponibilitaView.show();
        });

        indietroBtn.setOnAction(ev -> {
            EntryPointViewFX.showMainMenu(primaryStage);
        });

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }
}