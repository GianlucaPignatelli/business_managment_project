package animazioneazienda.view.FX.animatore;

import animazioneazienda.bean.Utente;
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
import java.util.ArrayList;
import java.util.List;

public class CalendarioDisponibilitaViewFX {
    private final Stage primaryStage;
    private final Utente utente;
    private final List<String> disponibilita = new ArrayList<>();

    public CalendarioDisponibilitaViewFX(Stage primaryStage, Utente utente) {
        this.primaryStage = primaryStage;
        this.utente = utente;
    }

    public void show() {
        VBox root = new VBox(18);
        root.setPadding(new Insets(28));
        root.setAlignment(Pos.CENTER);
        root.setPrefWidth(900);
        root.setPrefHeight(600);
        root.setStyle("-fx-background-color: #181818;");

        Label title = new Label("Gestione Disponibilità Animatore");
        title.setFont(Font.font("Arial", 24));
        title.setTextFill(Color.web("#1CA9E2"));
        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);

        // Icone dei bottoni
        ImageView inserisciIcon = new ImageView(new Image(getClass().getResourceAsStream("/inserisci.png")));
        inserisciIcon.setFitWidth(26); inserisciIcon.setFitHeight(26);
        Button inserisciBtn = new Button("Inserisci Disponibilità", inserisciIcon);
        inserisciBtn.setGraphicTextGap(18);

        ImageView modificaIcon = new ImageView(new Image(getClass().getResourceAsStream("/modifica.png")));
        modificaIcon.setFitWidth(26); modificaIcon.setFitHeight(26);
        Button modificaBtn = new Button("Modifica Disponibilità", modificaIcon);
        modificaBtn.setGraphicTextGap(18);

        ImageView visualizzaIcon = new ImageView(new Image(getClass().getResourceAsStream("/visualizza.png")));
        visualizzaIcon.setFitWidth(26); visualizzaIcon.setFitHeight(26);
        Button visualizzaBtn = new Button("Visualizza Disponibilità", visualizzaIcon);
        visualizzaBtn.setGraphicTextGap(18);

        ImageView eliminaIcon = new ImageView(new Image(getClass().getResourceAsStream("/elimina.png")));
        eliminaIcon.setFitWidth(26); eliminaIcon.setFitHeight(26);
        Button eliminaBtn = new Button("Elimina Disponibilità", eliminaIcon);
        eliminaBtn.setGraphicTextGap(18);

        for (Button b : new Button[]{inserisciBtn, modificaBtn, visualizzaBtn, eliminaBtn}) {
            b.setStyle("-fx-font-size: 16px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
            b.setMaxWidth(320);
            VBox.setMargin(b, new Insets(0,0,8,0));
        }

        // Bottone indietro come icona centrato
        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(28); backIcon.setFitWidth(28);
        Button indietroBtn = new Button("", backIcon);
        indietroBtn.setStyle("-fx-background-color: transparent;");
        indietroBtn.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietroBtn);
        boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(18,0,0,0));

        root.getChildren().addAll(titleBox, inserisciBtn, modificaBtn, visualizzaBtn, eliminaBtn, boxIndietro);

        inserisciBtn.setOnAction(ev -> showInserisci());
        modificaBtn.setOnAction(ev -> showModifica());
        visualizzaBtn.setOnAction(ev -> showVisualizza());
        eliminaBtn.setOnAction(ev -> showElimina());
        indietroBtn.setOnAction(ev -> {
            AnimatoreMenuFX menu = new AnimatoreMenuFX(primaryStage, utente);
            menu.show();
        });

        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.centerOnScreen();
    }

    // --- Altre schermate come prima ---

    private void showInserisci() {
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
            String disp;
            if (group.getSelectedToggle() == allDayBtn) {
                disp = giorno + " | Tutto il giorno";
            } else {
                disp = String.format("%s | da %02d:%02d a %02d:%02d", giorno, oraInizio.getValue(), minInizio.getValue(), oraFine.getValue(), minFine.getValue());
            }
            disponibilita.add(disp);
            esito.setText("Disponibilità inserita.");
        });

        indietro.setOnAction(ev -> show());

        pane.getChildren().addAll(titolo, giornoPicker, new HBox(16, allDayBtn, fasciaBtn), boxOrario, conferma, boxIndietro, esito);
        primaryStage.setScene(new Scene(pane, 500, 430));
        primaryStage.centerOnScreen();
    }

    private void showModifica() {
        VBox pane = new VBox(16);
        pane.setPadding(new Insets(18));
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: #181818;");
        Label titolo = new Label("Modifica Disponibilità");
        titolo.setFont(Font.font("Arial", 22));
        titolo.setTextFill(Color.web("#1CA9E2"));
        ListView<String> dispList = new ListView<>();
        dispList.setStyle("-fx-control-inner-background: #181818; -fx-text-fill: #1CA9E2;");
        dispList.getItems().addAll(disponibilita);

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

        Button modifica = new Button("Modifica");
        modifica.setStyle("-fx-font-size: 16px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(28); backIcon.setFitWidth(28);
        Button indietro = new Button("", backIcon);
        indietro.setStyle("-fx-background-color: transparent;");
        indietro.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietro); boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(18,0,0,0));
        Label esito = new Label(); esito.setTextFill(Color.web("#1CA9E2"));

        modifica.setOnAction(ev -> {
            int idx = dispList.getSelectionModel().getSelectedIndex();
            if (idx == -1) {
                esito.setText("Seleziona una disponibilità!");
            } else {
                String vec = dispList.getItems().get(idx);
                dispList.getItems().set(idx, String.format("%s | da %02d:%02d a %02d:%02d",
                        vec.split("\\|")[0].trim(), oraInizio.getValue(), minInizio.getValue(), oraFine.getValue(), minFine.getValue()));
                disponibilita.set(idx, dispList.getItems().get(idx));
                esito.setText("Disponibilità modificata.");
            }
        });

        indietro.setOnAction(ev -> show());
        pane.getChildren().addAll(titolo, dispList, boxOrario, modifica, boxIndietro, esito);
        primaryStage.setScene(new Scene(pane, 550, 430));
        primaryStage.centerOnScreen();
    }

    private void showVisualizza() {
        VBox pane = new VBox(16);
        pane.setPadding(new Insets(18));
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: #181818;");
        Label titolo = new Label("Disponibilità attuali");
        titolo.setFont(Font.font("Arial", 22));
        titolo.setTextFill(Color.web("#1CA9E2"));
        ListView<String> dispList = new ListView<>();
        dispList.setStyle("-fx-control-inner-background: #181818; -fx-text-fill: #1CA9E2;");
        dispList.getItems().addAll(disponibilita);
        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(28); backIcon.setFitWidth(28);
        Button indietro = new Button("", backIcon);
        indietro.setStyle("-fx-background-color: transparent;");
        indietro.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietro); boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(18,0,0,0));
        indietro.setOnAction(ev -> show());
        pane.getChildren().addAll(titolo, dispList, boxIndietro);
        primaryStage.setScene(new Scene(pane, 420, 330));
        primaryStage.centerOnScreen();
    }

    private void showElimina() {
        VBox pane = new VBox(16);
        pane.setPadding(new Insets(18));
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: #181818;");
        Label titolo = new Label("Elimina Disponibilità");
        titolo.setFont(Font.font("Arial", 22));
        titolo.setTextFill(Color.web("#1CA9E2"));
        ListView<String> dispList = new ListView<>();
        dispList.setStyle("-fx-control-inner-background: #181818; -fx-text-fill: #1CA9E2;");
        dispList.getItems().addAll(disponibilita);
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
                disponibilita.remove(idx);
                dispList.getItems().remove(idx);
                esito.setText("Eliminata!");
            }
        });

        indietro.setOnAction(ev -> show());
        pane.getChildren().addAll(titolo, dispList, elimina, boxIndietro, esito);
        primaryStage.setScene(new Scene(pane, 485, 350));
        primaryStage.centerOnScreen();
    }
}