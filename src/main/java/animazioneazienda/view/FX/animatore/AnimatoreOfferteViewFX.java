package animazioneazienda.view.FX.animatore;

import animazioneazienda.bean.Utente;
import animazioneazienda.bean.animatore.OffertaLavoro;
import animazioneazienda.dao.animatore.OffertaLavoroDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class AnimatoreOfferteViewFX {
    private final Utente animatore;
    private final OffertaLavoroDAO offertaDAO;

    public AnimatoreOfferteViewFX(Utente animatore, OffertaLavoroDAO offertaDAO) {
        this.animatore = animatore;
        this.offertaDAO = offertaDAO;
    }

    public void start(Stage stage) {
        VBox root = new VBox(13);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Offerte di Lavoro Ricevute");
        ListView<String> offerteList = new ListView<>();
        Button aggiornaStatoBtn = new Button("Aggiorna stato offerta");
        Button closeBtn = new Button("Chiudi");
        Label esito = new Label();

        updateOfferte(offerteList);

        aggiornaStatoBtn.setOnAction(ev -> {
            String sel = offerteList.getSelectionModel().getSelectedItem();
            if (sel == null) return;
            int id = Integer.parseInt(sel.split("\\|")[0].replace("ID:", "").trim());

            ComboBox<String> statoCB = new ComboBox<>();
            statoCB.getItems().addAll("accettato", "rifiutato");
            statoCB.setPromptText("Nuovo stato");

            Button confermaBtn = new Button("Conferma");
            Label esitoMod = new Label();
            Stage updateStage = new Stage();
            VBox updateBox = new VBox(10);
            updateBox.setAlignment(Pos.CENTER);
            updateBox.setPadding(new Insets(16));
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

        closeBtn.setOnAction(ev -> stage.close());

        root.getChildren().addAll(title, offerteList, aggiornaStatoBtn, closeBtn, esito);
        stage.setScene(new Scene(root, 530, 360));
        stage.setTitle("Opportunità di Lavoro (Animatore)");
        stage.show();
    }

    private void updateOfferte(ListView<String> lv) {
        lv.getItems().clear();
        try {
            List<OffertaLavoro> lista = offertaDAO.findByAnimatore(animatore.getAziendaId(), animatore.getId());
            for (OffertaLavoro o : lista) {
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