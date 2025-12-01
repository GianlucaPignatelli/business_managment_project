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

public class EliminaDisponibilitaViewFX {
    private final Stage primaryStage;
    private final UtenteBean utente;
    private final DisponibilitaAnimatoreRepository disponibilitaRepository;

    public EliminaDisponibilitaViewFX(Stage primaryStage, UtenteBean utente, DisponibilitaAnimatoreRepository disponibilitaRepository) {
        this.primaryStage = primaryStage;
        this.utente = utente;
        this.disponibilitaRepository = disponibilitaRepository;
    }

    public void show() {
        VBox pane = new VBox(16);
        pane.setPadding(new Insets(18));
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: #181818;");

        Label titolo = new Label("Elimina Disponibilità");
        titolo.setFont(Font.font("Arial", 22));
        titolo.setStyle("-fx-text-fill: #1CA9E2;");

        ListView<String> dispList = new ListView<>();
        dispList.setStyle("-fx-control-inner-background: #181818; -fx-text-fill: #1CA9E2;");
        List<DisponibilitaAnimatoreBean> lista;
        try {
            lista = disponibilitaRepository.findByAnimatore(utente.getAziendaId(), utente.getId());
        } catch (DaoException e) {
            dispList.getItems().add("Errore: " + e.getMessage());
            lista = List.of();
        }
        final List<DisponibilitaAnimatoreBean> listFinal = lista;
        for (DisponibilitaAnimatoreBean d : listFinal) {
            if (d.isTuttoIlGiorno())
                dispList.getItems().add(d.getData() + " | Tutto il giorno");
            else
                dispList.getItems().add(String.format("%s | %02d:%02d - %02d:%02d",
                        d.getData(), d.getOrarioInizio().getHour(), d.getOrarioInizio().getMinute(),
                        d.getOrarioFine().getHour(), d.getOrarioFine().getMinute()));
        }

        Button elimina = new Button("Elimina selezionata");
        elimina.setStyle("-fx-font-size: 16px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(28); backIcon.setFitWidth(28);
        Button indietro = new Button("", backIcon);
        indietro.setStyle("-fx-background-color: transparent;");
        indietro.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietro); boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(18,0,0,0));
        Label esito = new Label(); esito.setStyle("-fx-text-fill: #1CA9E2;");

        elimina.setOnAction(ev -> {
            int idx = dispList.getSelectionModel().getSelectedIndex();
            if (idx == -1) {
                esito.setText("Seleziona una disponibilità da eliminare!");
            } else {
                DisponibilitaAnimatoreBean d = listFinal.get(idx);
                try {
                    boolean ok = disponibilitaRepository.eliminaDisponibilita(d);
                    if (ok) {
                        dispList.getItems().remove(idx);
                        listFinal.remove(idx);
                        esito.setText("Eliminata!");
                    } else {
                        esito.setText("Errore nell'eliminazione!");
                    }
                } catch (DaoException ex) {
                    esito.setText("Errore: " + ex.getMessage());
                }
            }
        });

        indietro.setOnAction(ev -> new CalendarioDisponibilitaViewFX(primaryStage, utente, disponibilitaRepository).show());

        pane.getChildren().addAll(titolo, dispList, elimina, boxIndietro, esito);
        primaryStage.setScene(new Scene(pane, 485, 350));
        primaryStage.centerOnScreen();
    }
}