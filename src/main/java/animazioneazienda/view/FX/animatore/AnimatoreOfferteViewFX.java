package animazioneazienda.view.FX.animatore;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.OffertaLavoroBean;
import animazioneazienda.dao.animatore.OffertaLavoroDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import java.util.List;

public class AnimatoreOfferteViewFX {
    private final Stage primaryStage;
    private final UtenteBean animatore;
    private final OffertaLavoroDAO offertaDAO;

    public AnimatoreOfferteViewFX(Stage primaryStage, UtenteBean animatore, OffertaLavoroDAO offertaDAO) {
        this.primaryStage = primaryStage;
        this.animatore = animatore;
        this.offertaDAO = offertaDAO;
    }

    public void show() {
        VBox root = new VBox(13);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #181818;");

        // Titolo + icona offerte
        Label title = new Label("Offerte di Lavoro Ricevute");
        title.setFont(Font.font("Arial", 24));
        title.setTextFill(Color.web("#1CA9E2"));
        ImageView notificationIcon = new ImageView(new Image(getClass().getResourceAsStream("/notification.png")));
        notificationIcon.setFitHeight(30); notificationIcon.setFitWidth(30);

        HBox titleBox = new HBox(12, title, notificationIcon);
        titleBox.setAlignment(Pos.CENTER);

        ListView<String> offerteList = new ListView<>();
        offerteList.setStyle("-fx-control-inner-background: #181818; -fx-text-fill: #1CA9E2;");
        Button aggiornaStatoBtn = new Button("Aggiorna stato offerta");
        aggiornaStatoBtn.setStyle("-fx-font-size: 16px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818; -fx-font-weight: bold;");

        // Bottone indietro come icona centrato
        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(28); backIcon.setFitWidth(28);
        Button indietroBtn = new Button("", backIcon);
        indietroBtn.setStyle("-fx-background-color: transparent;");
        indietroBtn.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietroBtn);
        boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(20,0,0,0));

        Label esito = new Label();
        esito.setTextFill(Color.web("#1CA9E2"));

        updateOfferte(offerteList);

        aggiornaStatoBtn.setOnAction(ev -> {
            String sel = offerteList.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            int id = Integer.parseInt(sel.split("\\|")[0].replace("ID:", "").trim());

            ComboBox<String> statoCB = new ComboBox<>();
            statoCB.getItems().addAll("accettato", "rifiutato");
            statoCB.setPromptText("Nuovo stato");
            Button confermaBtn = new Button("Conferma");
            confermaBtn.setStyle("-fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
            Label esitoMod = new Label();
            esitoMod.setTextFill(Color.web("#1CA9E2;"));

            Stage updateStage = new Stage();
            VBox updateBox = new VBox(10);
            updateBox.setAlignment(Pos.CENTER);
            updateBox.setPadding(new Insets(16));
            updateBox.setStyle("-fx-background-color: #181818;");
            updateBox.getChildren().addAll(new Label("Aggiorna stato offerta:"), statoCB, confermaBtn, esitoMod);

            confermaBtn.setOnAction(cfgEv -> {
                String statoNuovo = statoCB.getValue();
                try {
                    if (statoNuovo != null &&
                            (statoNuovo.equals("accettato") || statoNuovo.equals("rifiutato")) &&
                            offertaDAO.aggiornaStato(id, statoNuovo, animatore.getAziendaId(), animatore.getId())
                    ) {
                        esitoMod.setText("Stato aggiornato!");
                        updateOfferte(offerteList);
                    } else {
                        esitoMod.setText("Errore nell'aggiornamento");
                    }
                } catch (Exception e) {
                    esitoMod.setText("Errore: " + e.getMessage());
                }
            });

            updateStage.setScene(new Scene(updateBox, 240, 180));
            updateStage.setTitle("Aggiorna Offerta");
            updateStage.show();
        });

        indietroBtn.setOnAction(ev -> {
            AnimatoreMenuFX menu = new AnimatoreMenuFX(primaryStage, animatore);
            menu.show();
        });

        root.getChildren().addAll(titleBox, offerteList, aggiornaStatoBtn, boxIndietro, esito);

        Scene scene = new Scene(root, 530, 380);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Opportunità di Lavoro (Animatore)");
        primaryStage.centerOnScreen();
    }

    private void updateOfferte(ListView<String> lv) {
        lv.getItems().clear();
        try {
            List<OffertaLavoroBean> lista = offertaDAO.findByAnimatore(animatore.getAziendaId(), animatore.getId());
            for (OffertaLavoroBean o : lista) {
                String descr = String.format("ID:%d | %s | %s-%s | %s | Stato: %s",
                        o.getId(),
                        o.getDataEvento(),
                        o.getOrarioInizio(),
                        o.getOrarioFine(),
                        o.getDescrizione(),
                        o.getStato());
                lv.getItems().add(descr);
            }
        } catch (Exception ignored) { }
    }
}