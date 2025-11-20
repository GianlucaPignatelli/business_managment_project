package animazioneazienda.view.FX;

import animazioneazienda.bean.Utente;
import animazioneazienda.dao.AziendaDAO;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SuperadminMenuFX {
    public void start(Stage stage, Utente admin) {
        VBox box = new VBox(18);
        box.setStyle("-fx-padding: 36; -fx-alignment: center;");

        Label title = new Label("Superadmin Menu (" + (admin != null ? admin.getEmail() : "---") + ")");
        Button btnCreaAzienda = new Button("Registra nuova azienda");
        Button btnLogout = new Button("Logout");
        Label msgLabel = new Label();

        box.getChildren().addAll(title, btnCreaAzienda, btnLogout, msgLabel);

        btnCreaAzienda.setOnAction(ev -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Nuova azienda");
            dialog.setContentText("Nome azienda:");
            dialog.showAndWait().ifPresent(nome -> {
                try {
                    AziendaDAO aziendaDAO = FXContextHolder.aziendaDAO;
                    int id = aziendaDAO.registraAzienda(nome);
                    if (id > 0) {
                        msgLabel.setText("Azienda registrata con successo! (ID: " + id + ")");
                    } else {
                        msgLabel.setText("Errore registrazione azienda.");
                    }
                } catch (Exception e) {
                    msgLabel.setText("Errore: " + e.getMessage());
                }
            });
        });

        btnLogout.setOnAction(ev -> stage.close());

        stage.setScene(new Scene(box, 350, 230));
        stage.show();
    }
}