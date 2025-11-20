package animazioneazienda.view.FX;

import animazioneazienda.dao.AziendaDAO;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistrazioneAziendaViewFX {
    private AziendaDAO aziendaDAO;

    public void setAziendaDAO(AziendaDAO dao) { this.aziendaDAO = dao; }

    public void start(Stage stage) {
        stage.setTitle("Registra Nuova Azienda");

        Label label = new Label("Nome azienda:");
        TextField nomeField = new TextField();
        Button btn = new Button("Registra");
        Label resultLabel = new Label();

        btn.setOnAction(e -> {
            String nome = nomeField.getText().trim();
            if (!nome.isEmpty() && aziendaDAO != null) {
                try {
                    int id = aziendaDAO.registraAzienda(nome);
                    if (id > 0) resultLabel.setText("Azienda registrata! ID: " + id);
                    else resultLabel.setText("Errore registrazione.");
                } catch (Exception ex) {
                    resultLabel.setText("Errore: " + ex.getMessage());
                }
            } else {
                resultLabel.setText("Nome azienda obbligatorio.");
            }
        });

        VBox box = new VBox(15, label, nomeField, btn, resultLabel);
        box.setPadding(new Insets(30));
        Scene scene = new Scene(box, 350, 200);
        stage.setScene(scene);
        stage.show();
    }
}