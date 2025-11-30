package animazioneazienda.view.FX.animatore.disponibilita;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.dao.animatore.disponibilita.EliminaDisponibilitaDAO;
import animazioneazienda.dao.animatore.disponibilita.VisualizzaDisponibilitaDAO;
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

import java.util.List;

public class EliminaDisponibilitaViewFX {
    private final Stage primaryStage;
    private final UtenteBean utente;
    private final EliminaDisponibilitaDAO eliminaDisponibilitaDAO;
    private final VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO;

    public EliminaDisponibilitaViewFX(
            Stage primaryStage,
            UtenteBean utente,
            EliminaDisponibilitaDAO eliminaDisponibilitaDAO,
            VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO
    ) {
        this.primaryStage = primaryStage;
        this.utente = utente;
        this.eliminaDisponibilitaDAO = eliminaDisponibilitaDAO;
        this.visualizzaDisponibilitaDAO = visualizzaDisponibilitaDAO;
    }

    public void show() {
        VBox pane = new VBox(16);
        pane.setPadding(new Insets(18));
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: #181818;");

        Label titolo = new Label("Elimina Disponibilità");
        titolo.setFont(Font.font("Arial", 22));
        titolo.setTextFill(Color.web("#1CA9E2"));

        ListView<String> dispList = new ListView<>();
        dispList.setStyle("-fx-control-inner-background: #181818; -fx-text-fill: #1CA9E2;");
        List<DisponibilitaAnimatoreBean> lista = visualizzaDisponibilitaDAO.trovaPerAnimatore(utente.getAziendaId(), utente.getId());
        for (DisponibilitaAnimatoreBean d : lista) {
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
        Label esito = new Label(); esito.setTextFill(Color.web("#1CA9E2"));

        elimina.setOnAction(ev -> {
            int idx = dispList.getSelectionModel().getSelectedIndex();
            if (idx == -1) {
                esito.setText("Seleziona una disponibilità da eliminare!");
            } else {
                DisponibilitaAnimatoreBean d = lista.get(idx);
                boolean ok = eliminaDisponibilitaDAO.elimina(d.getId(), utente.getAziendaId(), utente.getId());
                if (ok) {
                    dispList.getItems().remove(idx);
                    lista.remove(idx);
                    esito.setText("Eliminata!");
                } else {
                    esito.setText("Errore nell'eliminazione!");
                }
            }
        });

        indietro.setOnAction(ev ->
                new CalendarioDisponibilitaViewFX(
                        primaryStage,
                        utente,
                        animazioneazienda.view.FX.EntryPointViewFX.visualizzaDisponibilitaDAO,
                        animazioneazienda.view.FX.EntryPointViewFX.inserisciDisponibilitaDAO,
                        animazioneazienda.view.FX.EntryPointViewFX.modificaDisponibilitaDAO,
                        animazioneazienda.view.FX.EntryPointViewFX.eliminaDisponibilitaDAO
                ).show()
        );

        pane.getChildren().addAll(titolo, dispList, elimina, boxIndietro, esito);
        primaryStage.setScene(new Scene(pane, 485, 350));
        primaryStage.centerOnScreen();
    }
}