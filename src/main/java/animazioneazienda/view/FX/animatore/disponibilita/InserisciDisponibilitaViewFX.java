package animazioneazienda.view.FX.animatore.disponibilita;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.dao.animatore.disponibilita.InserisciDisponibilitaDAO;
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

import java.time.LocalDate;
import java.time.LocalTime;

public class InserisciDisponibilitaViewFX {
    private final Stage primaryStage;
    private final UtenteBean utente;
    private final InserisciDisponibilitaDAO inserisciDisponibilitaDAO;
    private final VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO;

    public InserisciDisponibilitaViewFX(Stage primaryStage, UtenteBean utente,
                                        InserisciDisponibilitaDAO inserisciDisponibilitaDAO, VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO) {
        this.primaryStage = primaryStage;
        this.utente = utente;
        this.inserisciDisponibilitaDAO = inserisciDisponibilitaDAO;
        this.visualizzaDisponibilitaDAO = visualizzaDisponibilitaDAO;
    }

    public void show() {
        VBox pane = new VBox(16);
        pane.setPadding(new Insets(18));
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: #181818;");

        Label titolo = new Label("Nuova Disponibilità");
        titolo.setFont(Font.font("Arial", 22));
        titolo.setTextFill(Color.web("#1CA9E2"));

        DatePicker giornoPicker = new DatePicker();
        giornoPicker.setPromptText("Seleziona il giorno");
        giornoPicker.setStyle("-fx-font-size: 15px; -fx-background-color: #252525; -fx-text-fill: #1CA9E2;");

        RadioButton allDayBtn = new RadioButton("Tutto il giorno");
        RadioButton fasciaBtn = new RadioButton("Fascia oraria");
        ToggleGroup group = new ToggleGroup();
        allDayBtn.setToggleGroup(group); fasciaBtn.setToggleGroup(group);
        fasciaBtn.setSelected(true);

        ComboBox<Integer> oraInizio = new ComboBox<>(); ComboBox<Integer> minInizio = new ComboBox<>();
        ComboBox<Integer> oraFine = new ComboBox<>(); ComboBox<Integer> minFine = new ComboBox<>();
        for(int h=0; h<24; h++) { oraInizio.getItems().add(h); oraFine.getItems().add(h);}
        for(int m=0; m<60; m+=5) { minInizio.getItems().add(m); minFine.getItems().add(m);}
        oraInizio.setValue(9); minInizio.setValue(0); oraFine.setValue(13); minFine.setValue(0);

        HBox boxOrario = new HBox(8,
                new Label("Da:"), oraInizio, new Label(":"), minInizio,
                new Label("A:"), oraFine, new Label(":"), minFine
        );
        boxOrario.setAlignment(Pos.CENTER);

        group.selectedToggleProperty().addListener((obs, old, selected) -> {
            boolean isAllDay = selected == allDayBtn;
            boxOrario.setDisable(isAllDay);
        });

        Button conferma = new Button("Conferma");
        conferma.setStyle("-fx-font-size: 16px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(28); backIcon.setFitWidth(28);
        Button indietro = new Button("", backIcon);
        indietro.setStyle("-fx-background-color: transparent;");
        indietro.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietro);
        boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(18,0,0,0));
        Label esito = new Label(); esito.setTextFill(Color.web("#1CA9E2"));

        conferma.setOnAction(ev -> {
            LocalDate giorno = giornoPicker.getValue();
            if (giorno == null) {
                esito.setText("Seleziona un giorno!");
                return;
            }
            LocalDate oggi = LocalDate.now();
            if (giorno.isBefore(oggi)) {
                esito.setText("Errore: Non puoi inserire una disponibilità per una data passata!");
                return;
            }
            boolean tuttoIlGiorno = group.getSelectedToggle() == allDayBtn;
            LocalTime inizio = null, fine = null;
            if (!tuttoIlGiorno) {
                inizio = LocalTime.of(oraInizio.getValue(), minInizio.getValue());
                fine = LocalTime.of(oraFine.getValue(), minFine.getValue());
                if (!fine.isAfter(inizio)) {
                    esito.setText("Errore: L'orario di fine deve essere successivo all'orario di inizio!");
                    return;
                }
            }
            // >>> CONTROLLO SOVRAPPOSIZIONE DAL DAO CORRETTO
            if (visualizzaDisponibilitaDAO.esisteSovrapposizione(utente.getAziendaId(), utente.getId(), giorno, inizio, fine, tuttoIlGiorno)) {
                esito.setText("Disponibilità sovrapposta o già presente per quel giorno!");
                return;
            }
            DisponibilitaAnimatoreBean d = new DisponibilitaAnimatoreBean();
            d.setAziendaId(utente.getAziendaId());
            d.setAnimatoreId(utente.getId());
            d.setData(giorno);
            d.setTuttoIlGiorno(tuttoIlGiorno);
            d.setOrarioInizio(inizio);
            d.setOrarioFine(fine);
            boolean ok = inserisciDisponibilitaDAO.inserisci(d);
            if (ok) esito.setText("Disponibilità aggiunta!");
            else esito.setText("Errore nell'aggiunta");
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

        pane.getChildren().addAll(titolo, giornoPicker, new HBox(16, allDayBtn, fasciaBtn), boxOrario, conferma, boxIndietro, esito);
        primaryStage.setScene(new Scene(pane, 500, 430));
        primaryStage.centerOnScreen();
    }
}