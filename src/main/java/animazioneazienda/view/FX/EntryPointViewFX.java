package animazioneazienda.view.FX;

import animazioneazienda.controller.AnimatoreController;
import animazioneazienda.controller.LoginController;
import animazioneazienda.dao.AziendaDAO;
import animazioneazienda.dao.UtenteDAO;
import animazioneazienda.dao.animatore.DisponibilitaAnimatoreDAO;
import animazioneazienda.dao.animatore.OffertaLavoroDAO;
import animazioneazienda.dao.animatore.StatusAnimatoreDAO;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class EntryPointViewFX extends Application {
    public static AziendaDAO aziendaDAO;
    public static UtenteDAO utenteDAO;
    public static LoginController loginController;
    public static DisponibilitaAnimatoreDAO disponibilitaAnimatoreDAO;
    public static StatusAnimatoreDAO statusAnimatoreDAO;
    public static OffertaLavoroDAO offertaLavoroDAO;
    public static AnimatoreController animatoreController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox root = new VBox(30);
        root.setStyle("-fx-background-color: #181818; -fx-alignment: center;");

        try {
            Image logo = new Image("logo.png");
            ImageView logoView = new ImageView(logo);
            logoView.setFitWidth(180);
            logoView.setPreserveRatio(true);
            root.getChildren().add(logoView);
        } catch (Exception ex) {}

        Label welcomeLabel = new Label("Benvenuto in Gestionale Animazione!");
        welcomeLabel.setFont(Font.font("Arial", 40));
        welcomeLabel.setTextFill(Color.web("#1CA9E2"));

        root.getChildren().add(welcomeLabel);

        Scene scene = new Scene(root, 900, 600);

        primaryStage.setTitle("Gestionale Animazione");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);

        // Forza massimizzazione all'avvio (opzionale, commenta se non vuoi)
        // primaryStage.setMaximized(true);

        primaryStage.centerOnScreen();
        primaryStage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> {
            showMainMenu(primaryStage);
        });
        pause.play();
    }

    // Metodo per tornare al menu principale da altre schermate
    public static void showMainMenu(Stage primaryStage) {
        MainMenuFX menuFX = new MainMenuFX(primaryStage);
        menuFX.show();
    }
}