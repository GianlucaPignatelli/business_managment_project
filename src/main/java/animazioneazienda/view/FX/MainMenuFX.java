package animazioneazienda.view.FX;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainMenuFX {
    private final Stage primaryStage;

    public MainMenuFX(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        VBox root = new VBox(30);
        root.setStyle("-fx-background-color: #181818; -fx-alignment: center;");
        root.setPrefWidth(900);
        root.setPrefHeight(600);

        Label title = new Label("Effettua il login\noppure registrati");
        title.setFont(Font.font("Arial", 32));
        title.setTextFill(Color.web("#1CA9E2"));
        title.setAlignment(Pos.CENTER);

        ImageView loginIcon = new ImageView(new Image(getClass().getResourceAsStream("/login.png")));
        loginIcon.setFitHeight(38); loginIcon.setFitWidth(38);
        HBox titleBox = new HBox(12, title, loginIcon);
        titleBox.setAlignment(Pos.CENTER);

        Button btnLogin = new Button("Login");
        btnLogin.setStyle("-fx-font-size: 18px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");

        Button btnRegistrati = new Button("Registrazione");
        btnRegistrati.setStyle("-fx-font-size: 18px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");

        root.getChildren().addAll(titleBox, btnLogin, btnRegistrati);

        btnLogin.setOnAction(ev -> {
            LoginViewFX loginViewFX = new LoginViewFX(primaryStage);
            loginViewFX.show();
        });

        btnRegistrati.setOnAction(ev -> {
            RegistrazioneViewFX regViewFX = new RegistrazioneViewFX(primaryStage);
            regViewFX.show();
        });

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }
}