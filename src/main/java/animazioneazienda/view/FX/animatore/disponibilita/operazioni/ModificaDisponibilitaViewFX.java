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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ModificaDisponibilitaViewFX {
    private final Stage primaryStage;
    private final UtenteBean utente;
    private final DisponibilitaAnimatoreRepository disponibilitaRepository;

    public ModificaDisponibilitaViewFX(
            Stage primaryStage,
            UtenteBean utente,
            DisponibilitaAnimatoreRepository disponibilitaRepository
    ) {
        this.primaryStage = primaryStage;
        this.utente = utente;
        this.disponibilitaRepository = disponibilitaRepository;
    }

    public void show() {
        VBox pane = new VBox(16);
        pane.setPadding(new Insets(18));
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: #181818;");

        Label titolo = new Label("Modifica Disponibilità");
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

        DatePicker giornoPicker = new DatePicker();
        giornoPicker.setPromptText("Nuova data");
        giornoPicker.setStyle("-fx-font-size: 14px; -fx-background-color: #252525; -fx-text-fill: #1CA9E2;");
        RadioButton allDayBtn = new RadioButton("Tutto il giorno");
        RadioButton fasciaBtn = new RadioButton("Fascia oraria");
        ToggleGroup group = new ToggleGroup();
        allDayBtn.setToggleGroup(group); fasciaBtn.setToggleGroup(group);

        ComboBox<Integer> oraInizio = new ComboBox<>(); ComboBox<Integer> minInizio = new ComboBox<>();
        ComboBox<Integer> oraFine = new ComboBox<>(); ComboBox<Integer> minFine = new ComboBox<>();
        for(int h=0; h<24; h++) { oraInizio.getItems().add(h); oraFine.getItems().add(h);}
        for(int m=0; m<60; m+=5) { minInizio.getItems().add(m); minFine.getItems().add(m);}
        oraInizio.setValue(9); minInizio.setValue(0); oraFine.setValue(13); minFine.setValue(0);

        HBox boxOrario = new HBox(8, new Label("Da:"), oraInizio, new Label(":"), minInizio,
                new Label("A:"), oraFine, new Label(":"), minFine);
        boxOrario.setAlignment(Pos.CENTER);

        group.selectedToggleProperty().addListener((obs, old, selected) -> {
            boolean isAllDay = selected == allDayBtn;
            boxOrario.setDisable(isAllDay);
        });

        Button modifica = new Button("Modifica");
        modifica.setStyle("-fx-font-size: 16px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(28); backIcon.setFitWidth(28);
        Button indietro = new Button("", backIcon);
        indietro.setStyle("-fx-background-color: transparent;");
        indietro.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietro); boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(18,0,0,0));
        Label esito = new Label(); esito.setStyle("-fx-text-fill: #1CA9E2;");

        modifica.setOnAction(ev -> {
            int idx = dispList.getSelectionModel().getSelectedIndex();
            if (idx == -1) {
                esito.setText("Seleziona una disponibilità!");
                return;
            }
            DisponibilitaAnimatoreBean old = listFinal.get(idx);
            LocalDate data = giornoPicker.getValue() != null ? giornoPicker.getValue() : old.getData();
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
            old.setData(data); old.setTuttoIlGiorno(tuttoIlGiorno); old.setOrarioInizio(inizio); old.setOrarioFine(fine);
            try {
                boolean ok = disponibilitaRepository.modificaDisponibilita(old, data, inizio, fine, tuttoIlGiorno);
                if (ok) {
                    esito.setText("Disponibilità aggiornata!");
                    dispList.getItems().set(idx, tuttoIlGiorno ? (data + " | Tutto il giorno")
                            : String.format("%s | %02d:%02d - %02d:%02d",
                            data, inizio.getHour(), inizio.getMinute(), fine.getHour(), fine.getMinute()));
                } else esito.setText("Errore nell'aggiornamento!");
            } catch (DaoException ex) {
                esito.setText("Errore: " + ex.getMessage());
            }
        });

        indietro.setOnAction(ev -> new CalendarioDisponibilitaViewFX(primaryStage, utente, disponibilitaRepository).show());

        pane.getChildren().addAll(titolo, dispList, giornoPicker, new HBox(16, allDayBtn, fasciaBtn), boxOrario, modifica, boxIndietro, esito);
        primaryStage.setScene(new Scene(pane, 550, 430));
        primaryStage.centerOnScreen();
    }
}