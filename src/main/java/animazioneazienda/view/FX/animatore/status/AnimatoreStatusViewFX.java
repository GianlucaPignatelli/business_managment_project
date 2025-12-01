package animazioneazienda.view.FX.animatore.status;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.StatusAnimatoreBean;
import animazioneazienda.dao.animatore.status.StatusAnimatoreRepository;
import animazioneazienda.exception.DaoException;
import animazioneazienda.view.FX.animatore.AnimatoreMenuFX;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnimatoreStatusViewFX {
    private final Stage primaryStage;
    private final UtenteBean utente;
    private final StatusAnimatoreRepository statusAnimatoreRepository;

    public AnimatoreStatusViewFX(Stage primaryStage, UtenteBean utente, StatusAnimatoreRepository statusAnimatoreRepository) {
        this.primaryStage = primaryStage;
        this.utente = utente;
        this.statusAnimatoreRepository = statusAnimatoreRepository;
    }

    public void show() {
        StatusAnimatoreBean status = null;
        try {
            status = statusAnimatoreRepository.findByAnimatore(utente.getAziendaId(), utente.getId());
        } catch (DaoException e) {
            System.out.println("Errore nel caricamento profilo: " + e.getMessage());
        }
        if (status == null) {
            showProfiloNonCreato();
        } else {
            showProfiloCreato(status);
        }
    }

    private void showProfiloNonCreato() {
        VBox root = new VBox(28);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #181818; -fx-padding: 60;");
        Label lbl = new Label("Profilo non ancora creato.\nVuoi crearne uno?");
        lbl.setFont(Font.font("Arial", 24));
        lbl.setTextFill(Color.web("#1CA9E2"));
        lbl.setAlignment(Pos.CENTER);

        Button btnSi = new Button("Sì, crea profilo");
        btnSi.setStyle("-fx-font-size: 18px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        Button btnNo = new Button("No");
        btnNo.setStyle("-fx-font-size: 18px; -fx-background-color: #252525; -fx-text-fill: #1CA9E2;");
        HBox boxBtns = new HBox(22, btnSi, btnNo);
        boxBtns.setAlignment(Pos.CENTER);
        VBox.setMargin(boxBtns, new Insets(24,0,0,0));
        root.getChildren().addAll(lbl, boxBtns);

        btnSi.setOnAction(ev -> showSetupStatus(null)); // creazione
        btnNo.setOnAction(ev -> {
            AnimatoreMenuFX menu = new AnimatoreMenuFX(primaryStage, utente);
            menu.show();
        });
        Scene scene = new Scene(root, 600, 320);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    private void showProfiloCreato(StatusAnimatoreBean status) {
        VBox root = new VBox(24);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #181818; -fx-padding: 46;");
        Label lbl = new Label("Il tuo profilo attuale è il seguente:");
        lbl.setFont(Font.font("Arial", 23));
        lbl.setTextFill(Color.web("#1CA9E2"));

        Label dati = new Label(statusToString(status));
        dati.setFont(Font.font("Consolas", 18));
        dati.setTextFill(Color.web("#1CA9E2"));
        dati.setAlignment(Pos.CENTER_LEFT);

        VBox boxDati = new VBox(dati);
        boxDati.setAlignment(Pos.CENTER);
        boxDati.setStyle("-fx-padding: 24 0 10 0;");

        Label domanda = new Label("Vuoi modificarlo?");
        domanda.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        domanda.setTextFill(Color.web("#1CA9E2"));

        Button btnSi = new Button("Sì, modifica");
        btnSi.setStyle("-fx-font-size: 18px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        Button btnNo = new Button("No");
        btnNo.setStyle("-fx-font-size: 18px; -fx-background-color: #252525; -fx-text-fill: #1CA9E2;");
        HBox boxBtns = new HBox(22, btnSi, btnNo);
        boxBtns.setAlignment(Pos.CENTER);

        root.getChildren().addAll(lbl, boxDati, domanda, boxBtns);

        btnSi.setOnAction(ev -> showSetupStatus(status)); // modifica
        btnNo.setOnAction(ev -> {
            AnimatoreMenuFX menu = new AnimatoreMenuFX(primaryStage, utente);
            menu.show();
        });
        Scene scene = new Scene(root, 650, 360);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    private void showSetupStatus(StatusAnimatoreBean status) {
        VBox root = new VBox(18);
        root.setStyle("-fx-background-color: #181818; -fx-padding: 55; -fx-alignment: center;");
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(status == null ? "Creazione Profilo Animatore" : "Modifica Profilo Animatore");
        titleLabel.setFont(Font.font("Arial", 25));
        titleLabel.setTextFill(Color.web("#1CA9E2"));

        Label possiediLabel = new Label("Possiedi una macchina?");
        possiediLabel.setFont(Font.font("Arial", 17));
        possiediLabel.setTextFill(Color.web("#1CA9E2"));

        RadioButton siMacchina = new RadioButton("Sì");
        RadioButton noMacchina = new RadioButton("No");
        siMacchina.setStyle("-fx-font-size: 19px; -fx-text-fill: #1CA9E2;");
        noMacchina.setStyle("-fx-font-size: 19px; -fx-text-fill: #1CA9E2;");
        siMacchina.setPrefWidth(88); noMacchina.setPrefWidth(88);

        ToggleGroup macchinaGroup = new ToggleGroup();
        siMacchina.setToggleGroup(macchinaGroup);
        noMacchina.setToggleGroup(macchinaGroup);

        HBox scelteMacchinaBox = new HBox(40, siMacchina, noMacchina);
        scelteMacchinaBox.setAlignment(Pos.CENTER);
        scelteMacchinaBox.setPadding(new Insets(2, 0, 16, 0));

        TextField modelloAuto = new TextField();
        modelloAuto.setPromptText("Modello Auto");
        modelloAuto.setMaxWidth(320); modelloAuto.setPrefHeight(36);
        modelloAuto.setFont(Font.font("Arial", FontWeight.NORMAL, 17));

        ComboBox<String> comboDimensione = new ComboBox<>();
        comboDimensione.getItems().addAll("Piccola", "Media", "Grande", "Furgone");
        comboDimensione.setPromptText("Dimensione Auto");
        comboDimensione.setMaxWidth(320);

        VBox boxMacchina = new VBox(10, modelloAuto, comboDimensione);
        boxMacchina.setAlignment(Pos.CENTER);

        boolean hasMacchina = status != null && status.getModelloAuto() != null && !status.getModelloAuto().isEmpty();
        siMacchina.setSelected(hasMacchina);
        noMacchina.setSelected(!hasMacchina);
        boxMacchina.setVisible(hasMacchina);
        boxMacchina.setManaged(hasMacchina);

        macchinaGroup.selectedToggleProperty().addListener((obs, old, selected) -> {
            boolean si = selected == siMacchina;
            boxMacchina.setVisible(si);
            boxMacchina.setManaged(si);
            if (!si) {
                modelloAuto.clear();
                comboDimensione.setValue(null);
            }
        });

        if (status != null && hasMacchina) {
            modelloAuto.setText(status.getModelloAuto());
            comboDimensione.setValue(status.getDimensioneAuto());
        }

        List<String> lavoriPossibili = Arrays.asList("Capoanimatore", "Aiutoanimatore", "Delivery", "Operatore carretti");
        List<CheckBox> cbLavori = new ArrayList<>();
        for (String ruolo : lavoriPossibili) {
            CheckBox cb = new CheckBox(ruolo);
            cb.setStyle("-fx-text-fill: #1CA9E2;");
            if (status != null && status.getLavoriAccettati() != null) {
                List<String> accettati = Arrays.asList(status.getLavoriAccettati().split(","));
                cb.setSelected(accettati.contains(ruolo));
            }
            if (ruolo.equals("Delivery")) {
                cb.disableProperty().bind(noMacchina.selectedProperty());
                cb.visibleProperty().bind(siMacchina.selectedProperty());
                cb.managedProperty().bind(siMacchina.selectedProperty());
            }
            cbLavori.add(cb);
        }

        CheckBox haccp = new CheckBox("Certificato HACCP");
        haccp.setStyle("-fx-text-fill: #1CA9E2");

        if (status != null) haccp.setSelected(status.isHaccp());
        haccp.setDisable(status != null && status.getLavoriAccettati() != null &&
                !Arrays.asList(status.getLavoriAccettati().split(",")).contains("Operatore carretti"));

        cbLavori.get(3).selectedProperty().addListener((obs, oldVal, newVal) -> {
            haccp.setDisable(!newVal);
            if (!newVal) haccp.setSelected(false);
        });

        ComboBox<String> comboStato = new ComboBox<>();
        comboStato.getItems().addAll("Disponibile", "Non operativo");
        comboStato.setPromptText("Stato operativo");
        comboStato.setMaxWidth(320);
        if (status != null) comboStato.setValue(status.getStato());

        Button btnSalva = new Button(status == null ? "Salva Profilo" : "Aggiorna Profilo");
        btnSalva.setStyle("-fx-font-size: 18px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        btnSalva.setMaxWidth(180);

        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(28); backIcon.setFitWidth(28);
        Button indietroBtn = new Button("", backIcon);
        indietroBtn.setStyle("-fx-background-color: transparent;");
        indietroBtn.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietroBtn);
        boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(22,0,0,0));

        Label messageLabel = new Label("");
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        messageLabel.setTextFill(Color.web("#1CA9E2"));
        messageLabel.setAlignment(Pos.CENTER);

        HBox salvaBox = new HBox(btnSalva);
        salvaBox.setAlignment(Pos.CENTER);
        salvaBox.setPadding(new Insets(12,0,0,0));

        VBox lavoriBox = new VBox(7);
        lavoriBox.setAlignment(Pos.CENTER);
        lavoriBox.getChildren().addAll(cbLavori);
        lavoriBox.getChildren().add(haccp);

        root.getChildren().addAll(
                titleLabel,
                possiediLabel,
                scelteMacchinaBox,
                boxMacchina,
                lavoriBox,
                comboStato,
                salvaBox,
                boxIndietro,
                messageLabel
        );

        btnSalva.setOnAction(ev -> {
            boolean macchina = siMacchina.isSelected();
            String modello = macchina ? modelloAuto.getText().trim() : "";
            String dimensione = macchina ? comboDimensione.getValue() : null;
            List<String> lavoriSelezionati = new ArrayList<>();
            for (CheckBox cb : cbLavori) {
                if (cb.isSelected()) lavoriSelezionati.add(cb.getText());
            }
            String lavoriAccettatiCsv = String.join(",", lavoriSelezionati);
            String statoValue = comboStato.getValue();

            StatusAnimatoreBean nuovoStatus = new StatusAnimatoreBean();
            nuovoStatus.setAnimatoreId(utente.getId());
            nuovoStatus.setAziendaId(utente.getAziendaId());
            nuovoStatus.setModelloAuto(modello);
            nuovoStatus.setDimensioneAuto(dimensione);
            nuovoStatus.setLavoriAccettati(lavoriAccettatiCsv);
            nuovoStatus.setStato(statoValue);

            if (cbLavori.get(3).isSelected()) {
                nuovoStatus.setHaccp(haccp.isSelected());
            } else {
                nuovoStatus.setHaccp(false);
            }

            if (statoValue == null || statoValue.isEmpty()) {
                messageLabel.setText("Seleziona lo stato operativo!");
                return;
            }

            try {
                boolean ok = statusAnimatoreRepository.insertOrUpdate(nuovoStatus);
                messageLabel.setText(ok ? "Profilo aggiornato!" : "Errore aggiornamento!");
            } catch (DaoException ex) {
                messageLabel.setText("Errore DB: " + ex.getMessage());
            }
        });

        indietroBtn.setOnAction(ev -> {
            AnimatoreMenuFX menu = new AnimatoreMenuFX(primaryStage, utente);
            menu.show();
        });

        Scene scena = new Scene(root, 920, 680);
        primaryStage.setScene(scena);
        primaryStage.centerOnScreen();
    }

    private String statusToString(StatusAnimatoreBean s) {
        StringBuilder sb = new StringBuilder();
        if (s.getModelloAuto() != null && !s.getModelloAuto().isEmpty())
            sb.append("Auto: ").append(s.getModelloAuto())
                    .append(" (").append(s.getDimensioneAuto()).append(")\n");
        else
            sb.append("Auto: Nessuna\n");

        sb.append("Ruoli: ").append(s.getLavoriAccettati() != null ? s.getLavoriAccettati() : "Nessuno").append("\n");
        sb.append("HACCP: ").append(s.isHaccp() ? "Sì" : "No");
        sb.append("\nStato operativo: ").append(s.getStato());
        return sb.toString();
    }
}