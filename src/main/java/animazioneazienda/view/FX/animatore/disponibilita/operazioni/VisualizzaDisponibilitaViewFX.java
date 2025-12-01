package animazioneazienda.view.FX.animatore.disponibilita.operazioni;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.dao.animatore.disponibilita.DisponibilitaAnimatoreRepository;
import animazioneazienda.exception.DaoException;

import animazioneazienda.view.FX.animatore.disponibilita.CalendarioDisponibilitaViewFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class VisualizzaDisponibilitaViewFX {
    private final Stage primaryStage;
    private final UtenteBean utente;
    private final DisponibilitaAnimatoreRepository disponibilitaRepository;

    public VisualizzaDisponibilitaViewFX(Stage primaryStage, UtenteBean utente, DisponibilitaAnimatoreRepository disponibilitaRepository) {
        this.primaryStage = primaryStage;
        this.utente = utente;
        this.disponibilitaRepository = disponibilitaRepository;
    }

    public void show() {
        VBox pane = new VBox(16);
        pane.setPadding(new Insets(18));
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: #181818;");

        Label titolo = new Label("Disponibilità attuali");
        titolo.setFont(Font.font("Arial", 22));
        titolo.setStyle("-fx-text-fill: #1CA9E2;");

        ListView<String> dispList = new ListView<>();
        dispList.setStyle("-fx-control-inner-background: #181818; -fx-text-fill: #1CA9E2;");

        try {
            List<DisponibilitaAnimatoreBean> lista = disponibilitaRepository.findByAnimatore(utente.getAziendaId(), utente.getId());
            for (DisponibilitaAnimatoreBean d : lista) {
                if (d.isTuttoIlGiorno())
                    dispList.getItems().add(d.getData() + " | Tutto il giorno");
                else
                    dispList.getItems().add(String.format("%s | %02d:%02d - %02d:%02d",
                            d.getData(), d.getOrarioInizio().getHour(), d.getOrarioInizio().getMinute(),
                            d.getOrarioFine().getHour(), d.getOrarioFine().getMinute()));
            }
        } catch (DaoException e) {
            dispList.getItems().add("Errore: " + e.getMessage());
        }

        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(28); backIcon.setFitWidth(28);
        Button indietro = new Button("", backIcon);
        indietro.setStyle("-fx-background-color: transparent;");
        indietro.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietro);
        boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(18, 0, 0, 0));

        indietro.setOnAction(ev -> new CalendarioDisponibilitaViewFX(primaryStage, utente, disponibilitaRepository).show());

        pane.getChildren().addAll(titolo, dispList, boxIndietro);
        primaryStage.setScene(new Scene(pane, 420, 330));
        primaryStage.centerOnScreen();
    }
}