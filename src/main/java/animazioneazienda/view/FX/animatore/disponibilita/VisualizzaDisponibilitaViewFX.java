package animazioneazienda.view.FX.animatore.disponibilita;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
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

public class VisualizzaDisponibilitaViewFX {
    private final Stage primaryStage;
    private final UtenteBean utente;
    private final VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO;

    public VisualizzaDisponibilitaViewFX(Stage primaryStage, UtenteBean utente, VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO) {
        this.primaryStage = primaryStage;
        this.utente = utente;
        this.visualizzaDisponibilitaDAO = visualizzaDisponibilitaDAO;
    }

    public void show() {
        VBox pane = new VBox(16);
        pane.setPadding(new Insets(18));
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: #181818;");

        Label titolo = new Label("Disponibilità attuali");
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
                        d.getData(), d.getOrarioInizio().getHour(), d.getOrarioInizio().getMinute(), d.getOrarioFine().getHour(), d.getOrarioFine().getMinute()));
        }

        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(28); backIcon.setFitWidth(28);
        Button indietro = new Button("", backIcon);
        indietro.setStyle("-fx-background-color: transparent;");
        indietro.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietro); boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(18,0,0,0));

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

        pane.getChildren().addAll(titolo, dispList, boxIndietro);
        primaryStage.setScene(new Scene(pane, 420, 330));
        primaryStage.centerOnScreen();
    }
}