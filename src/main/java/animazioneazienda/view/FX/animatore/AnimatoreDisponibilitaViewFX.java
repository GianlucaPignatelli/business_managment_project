package animazioneazienda.view.FX.animatore;

import animazioneazienda.bean.Utente;
import animazioneazienda.bean.animatore.DisponibilitaAnimatore;
import animazioneazienda.dao.animatore.DisponibilitaAnimatoreDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AnimatoreDisponibilitaViewFX {
    private final Utente animatore;
    private final DisponibilitaAnimatoreDAO disponibilitaDAO;

    public AnimatoreDisponibilitaViewFX(Utente animatore, DisponibilitaAnimatoreDAO disponibilitaDAO) {
        this.animatore = animatore;
        this.disponibilitaDAO = disponibilitaDAO;
    }

    public void start(Stage stage) {
        VBox root = new VBox(18);
        root.setPadding(new Insets(22));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Gestione Disponibilità Calendario");
        ListView<String> listView = new ListView<>();
        updateList(listView);

        Button btnAgg = new Button("Aggiungi Disponibilità");
        Button btnMod = new Button("Modifica Disponibilità");
        Button btnDel = new Button("Elimina Disponibilità");
        Button btnClose = new Button("Chiudi");

        root.getChildren().addAll(title, listView, btnAgg, btnMod, btnDel, btnClose);

        /** Aggiunta disponibilità **/
        btnAgg.setOnAction(ev -> {
            Stage addStage = new Stage();
            VBox addBox = new VBox(10);
            addBox.setAlignment(Pos.CENTER);
            addBox.setPadding(new Insets(16));
            DatePicker datePicker = new DatePicker();
            CheckBox tuttoGiornoCB = new CheckBox("Tutto il giorno");
            TextField inizioTF = new TextField();
            inizioTF.setPromptText("Inizio (HH:MM)");
            TextField fineTF = new TextField();
            fineTF.setPromptText("Fine (HH:MM)");
            Label esito = new Label();
            Button saveBtn = new Button("Salva");

            tuttoGiornoCB.selectedProperty().addListener((obs, old, val) -> {
                inizioTF.setDisable(val);
                fineTF.setDisable(val);
            });

            saveBtn.setOnAction(saveEv -> {
                try {
                    LocalDate data = datePicker.getValue();
                    boolean tuttoGiorno = tuttoGiornoCB.isSelected();
                    LocalTime inizio = tuttoGiorno ? null : LocalTime.parse(inizioTF.getText().trim());
                    LocalTime fine = tuttoGiorno ? null : LocalTime.parse(fineTF.getText().trim());

                    DisponibilitaAnimatore d = new DisponibilitaAnimatore();
                    d.setAziendaId(animatore.getAziendaId());
                    d.setAnimatoreId(animatore.getId());
                    d.setData(data);
                    d.setTuttoIlGiorno(tuttoGiorno);
                    d.setOrarioInizio(inizio);
                    d.setOrarioFine(fine);

                    if (disponibilitaDAO.insertDisponibilita(d)) {
                        esito.setText("Disponibilità aggiunta!");
                        updateList(listView);
                    } else {
                        esito.setText("Errore di inserimento!");
                    }
                } catch (Exception ex) {
                    esito.setText("Errore: " + ex.getMessage());
                }
            });

            addBox.getChildren().addAll(new Label("Data:"), datePicker, tuttoGiornoCB, inizioTF, fineTF, saveBtn, esito);
            addStage.setScene(new Scene(addBox, 300, 340));
            addStage.setTitle("Aggiungi Disponibilità");
            addStage.show();
        });

        /** Modifica disponibilità (correzione: d deve essere final) **/
        btnMod.setOnAction(ev -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            int id = Integer.parseInt(selected.split("\\|")[0].replace("ID:", "").trim());
            Stage modStage = new Stage();
            VBox modBox = new VBox(10);
            modBox.setAlignment(Pos.CENTER);
            modBox.setPadding(new Insets(14));

            DatePicker datePicker = new DatePicker();
            CheckBox tuttoGiornoCB = new CheckBox("Tutto il giorno");
            TextField inizioTF = new TextField();
            inizioTF.setPromptText("Inizio (HH:MM)");
            TextField fineTF = new TextField();
            fineTF.setPromptText("Fine (HH:MM)");
            Label esito = new Label();
            Button saveBtn = new Button("Salva modifiche");

            final DisponibilitaAnimatore d;
            try {
                d = disponibilitaDAO.findByAnimatore(animatore.getAziendaId(), animatore.getId())
                        .stream().filter(x -> x.getId() == id).findFirst().orElse(null);
            } catch (Exception e) { esito.setText("Errore DB: " + e.getMessage()); return; }
            if (d == null) { esito.setText("Non trovato."); return; }
            datePicker.setValue(d.getData());
            tuttoGiornoCB.setSelected(d.isTuttoIlGiorno());
            inizioTF.setText(d.getOrarioInizio() == null ? "" : d.getOrarioInizio().toString());
            fineTF.setText(d.getOrarioFine() == null ? "" : d.getOrarioFine().toString());
            inizioTF.setDisable(d.isTuttoIlGiorno());
            fineTF.setDisable(d.isTuttoIlGiorno());

            tuttoGiornoCB.selectedProperty().addListener((obs, old, val) -> {
                inizioTF.setDisable(val); fineTF.setDisable(val);
            });

            saveBtn.setOnAction(saveEv -> {
                try {
                    LocalDate data = datePicker.getValue();
                    boolean tuttoGiorno = tuttoGiornoCB.isSelected();
                    LocalTime inizio = tuttoGiorno ? null : LocalTime.parse(inizioTF.getText().trim());
                    LocalTime fine = tuttoGiorno ? null : LocalTime.parse(fineTF.getText().trim());

                    d.setData(data);
                    d.setTuttoIlGiorno(tuttoGiorno);
                    d.setOrarioInizio(inizio);
                    d.setOrarioFine(fine);
                    if (disponibilitaDAO.updateDisponibilita(d)) {
                        esito.setText("Modifica salvata!");
                        updateList(listView);
                    } else {
                        esito.setText("Errore modifica!");
                    }
                } catch (Exception ex) {
                    esito.setText("Errore: " + ex.getMessage());
                }
            });
            modBox.getChildren().addAll(new Label("Data:"), datePicker, tuttoGiornoCB, inizioTF, fineTF, saveBtn, esito);
            modStage.setScene(new Scene(modBox, 300, 340));
            modStage.setTitle("Modifica Disponibilità");
            modStage.show();
        });

        /** Elimina disponibilità **/
        btnDel.setOnAction(ev -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            int id = Integer.parseInt(selected.split("\\|")[0].replace("ID:", "").trim());
            try {
                if (disponibilitaDAO.removeDisponibilita(id, animatore.getAziendaId(), animatore.getId())) {
                    updateList(listView);
                }
            } catch (Exception ex) {
                // gestione errore
            }
        });

        btnClose.setOnAction(ev -> stage.close());
        stage.setScene(new Scene(root, 540, 420));
        stage.setTitle("Gestione Disponibilità Calendario (Animatore)");
        stage.show();
    }

    private void updateList(ListView<String> view) {
        try {
            view.getItems().clear();
            List<DisponibilitaAnimatore> all = disponibilitaDAO.findByAnimatore(animatore.getAziendaId(), animatore.getId());
            for (DisponibilitaAnimatore d : all) {
                String descr = String.format("ID:%d | %s | %s",
                        d.getId(), d.getData(),
                        d.isTuttoIlGiorno() ? "Tutto il giorno" : d.getOrarioInizio() + " - " + d.getOrarioFine());
                view.getItems().add(descr);
            }
        } catch (Exception ignored) {}
    }
}