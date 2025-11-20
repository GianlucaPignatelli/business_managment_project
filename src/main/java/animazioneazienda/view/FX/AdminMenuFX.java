package animazioneazienda.view.FX;

import animazioneazienda.bean.Utente;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminMenuFX {
    public void start(Stage stage, Utente utente) {
        VBox box = new VBox(20);
        box.setStyle("-fx-padding: 38; -fx-alignment: center;");
        Label msg = new Label("Benvenuto, " + utente.getRuolo() + " di " + utente.getAziendaId());
        box.getChildren().addAll(new Label("Menu Utente"), msg);
        stage.setScene(new Scene(box, 320, 150));
        stage.show();
    }
}