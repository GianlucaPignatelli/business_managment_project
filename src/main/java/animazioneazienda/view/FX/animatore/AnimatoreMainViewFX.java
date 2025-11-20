package animazioneazienda.view.FX.animatore;

import animazioneazienda.bean.Utente;
import animazioneazienda.dao.animatore.DisponibilitaAnimatoreDAO;
import animazioneazienda.dao.animatore.StatusAnimatoreDAO;
import animazioneazienda.dao.animatore.OffertaLavoroDAO;
import animazioneazienda.bean.animatore.StatusAnimatore;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AnimatoreMainViewFX {
    private final Utente animatore;
    private final DisponibilitaAnimatoreDAO disponDao;
    private final StatusAnimatoreDAO statusDao;
    private final OffertaLavoroDAO offertaDao;

    public AnimatoreMainViewFX(
            Utente animatore,
            DisponibilitaAnimatoreDAO disponDao,
            StatusAnimatoreDAO statusDao,
            OffertaLavoroDAO offertaDao
    ) {
        this.animatore = animatore;
        this.disponDao = disponDao;
        this.statusDao = statusDao;
        this.offertaDao = offertaDao;
    }

    public void start(Stage stage) {
        VBox root = new VBox(28);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #111111;");
        root.setPrefWidth(700);

        Label title = new Label("Gestione Ruoli Animatore");
        title.setStyle("-fx-text-fill: #29ABE2; -fx-font-size: 22px;");

        // Checklist dei ruoli
        CheckBox cbCapoanimatore = new CheckBox("Capoanimatore");
        cbCapoanimatore.setStyle("-fx-text-fill: white;");

        CheckBox cbAiutoanimatore = new CheckBox("Aiutoanimatore");
        cbAiutoanimatore.setStyle("-fx-text-fill: white;");

        CheckBox cbDelivery = new CheckBox("Delivery");
        cbDelivery.setStyle("-fx-text-fill: white;");

        CheckBox cbCarretti = new CheckBox("Operatore carretti");
        cbCarretti.setStyle("-fx-text-fill: white;");

        CheckBox cbHaccp = new CheckBox("Ho certificazione HACCP");
        cbHaccp.setStyle("-fx-text-fill: white;");
        cbHaccp.setVisible(false); // Mostra solo se 'Operatore carretti' è selezionato

        cbCarretti.setOnAction(ev -> {
            cbHaccp.setVisible(cbCarretti.isSelected());
            if (!cbCarretti.isSelected()) {
                cbHaccp.setSelected(false);
            }
        });

        Button btnSalva = new Button("Salva Status Ruoli");
        btnSalva.setStyle("-fx-background-color: #29ABE2; -fx-text-fill: white;");

        Label esito = new Label();
        esito.setStyle("-fx-text-fill: #FF5252;");

        root.getChildren().addAll(title, cbCapoanimatore, cbAiutoanimatore, cbDelivery, cbCarretti, cbHaccp, btnSalva, esito);

        Scene scene = new Scene(root, 400, 430);
        stage.setScene(scene);
        stage.setMaximized(true);

        btnSalva.setOnAction(ev -> {
            List<String> ruoliSelezionati = new ArrayList<>();
            if (cbCapoanimatore.isSelected()) ruoliSelezionati.add("Capoanimatore");
            if (cbAiutoanimatore.isSelected()) ruoliSelezionati.add("Aiutoanimatore");
            if (cbDelivery.isSelected()) ruoliSelezionati.add("Delivery");
            if (cbCarretti.isSelected()) ruoliSelezionati.add("Operatore carretti");

            boolean haccp = cbCarretti.isSelected() && cbHaccp.isSelected();

            if (ruoliSelezionati.isEmpty()) {
                esito.setText("Seleziona almeno un ruolo.");
                return;
            }

            String lavoriAccettati = String.join(",", ruoliSelezionati);

            StatusAnimatore s = new StatusAnimatore();
            s.setAnimatoreId(animatore.getId());
            s.setAziendaId(animatore.getAziendaId());
            s.setLavoriAccettati(lavoriAccettati);
            s.setHaccp(haccp);

            try {
                boolean ok = statusDao.insertOrUpdate(s);
                if (ok) {
                    esito.setText("Status ruoli aggiornato!");
                } else {
                    esito.setText("Errore nell'aggiornamento ruoli.");
                }
            } catch (Exception e) {
                esito.setText("Errore: " + e.getMessage());
            }
        });

        stage.show();
    }
}