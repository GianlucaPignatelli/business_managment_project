package animazioneazienda.view.FX.animatore;

import animazioneazienda.bean.Utente;
import animazioneazienda.bean.animatore.StatusAnimatore;
import animazioneazienda.dao.animatore.StatusAnimatoreDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AnimatoreStatusViewFX {
    private final Utente animatore;
    private final StatusAnimatoreDAO statusDAO;

    public AnimatoreStatusViewFX(Utente animatore, StatusAnimatoreDAO statusDAO) {
        this.animatore = animatore;
        this.statusDAO = statusDAO;
    }

    public void start(Stage stage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Gestione Profilo & Status Animatore");

        TextField modelloAutoTF = new TextField();
        modelloAutoTF.setPromptText("Modello auto");
        ComboBox<String> dimensioneCB = new ComboBox<>();
        dimensioneCB.getItems().addAll("piccola", "media", "grande", "furgone");
        dimensioneCB.setPromptText("Dimensione auto");
        TextField lavoriAccettatiTF = new TextField();
        lavoriAccettatiTF.setPromptText("Tipologie lavori accettate (es: clown, mago, mascotte)");

        ComboBox<String> statoCB = new ComboBox<>();
        statoCB.getItems().addAll("Disponibile", "Non operativo");
        statoCB.setPromptText("Stato animatore");

        Label esito = new Label();
        Button saveBtn = new Button("Salva");

        // Loading stato attuale
        try {
            StatusAnimatore curr = statusDAO.findByAnimatore(animatore.getAziendaId(), animatore.getId());
            if (curr != null) {
                modelloAutoTF.setText(curr.getModelloAuto());
                dimensioneCB.setValue(curr.getDimensioneAuto());
                lavoriAccettatiTF.setText(curr.getLavoriAccettati());
                statoCB.setValue(curr.getStato());
            }
        } catch (Exception e) {
            esito.setText("Errore: " + e.getMessage());
        }

        saveBtn.setOnAction(ev -> {
            try {
                StatusAnimatore nuovo = new StatusAnimatore();
                nuovo.setAnimatoreId(animatore.getId());
                nuovo.setAziendaId(animatore.getAziendaId());
                nuovo.setModelloAuto(modelloAutoTF.getText());
                nuovo.setDimensioneAuto(dimensioneCB.getValue());
                nuovo.setLavoriAccettati(lavoriAccettatiTF.getText());
                nuovo.setStato(statoCB.getValue());

                if (statusDAO.insertOrUpdate(nuovo)) {
                    esito.setText("Profilo/status salvato!");
                } else {
                    esito.setText("Errore nel salvataggio!");
                }
            } catch (Exception ex) {
                esito.setText("Errore: " + ex.getMessage());
            }
        });

        root.getChildren().addAll(title, modelloAutoTF, dimensioneCB, lavoriAccettatiTF, statoCB, saveBtn, esito);

        stage.setScene(new Scene(root, 370, 370));
        stage.setTitle("Profilo e Status Animatore");
        stage.show();
    }
}