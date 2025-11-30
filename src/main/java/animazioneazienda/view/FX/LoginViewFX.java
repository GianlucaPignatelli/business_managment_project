package animazioneazienda.view.FX;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.view.FX.animatore.AnimatoreMenuFX;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class LoginViewFX {
    private final Stage primaryStage;

    public LoginViewFX(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        VBox root = new VBox(25);
        root.setStyle("-fx-background-color: #181818; -fx-padding: 40; -fx-alignment: center;");
        root.setPrefWidth(900);
        root.setPrefHeight(600);

        Label titleLabel = new Label("Accedi al nostro software!");
        titleLabel.setFont(Font.font("Arial", 26));
        titleLabel.setTextFill(Color.web("#1CA9E2"));
        ImageView loginIcon = new ImageView(new Image(getClass().getResourceAsStream("/login.png")));
        loginIcon.setFitHeight(36); loginIcon.setFitWidth(36);
        HBox titleBox = new HBox(12, titleLabel, loginIcon);
        titleBox.setAlignment(Pos.CENTER);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(360);
        emailField.setPrefHeight(48);
        emailField.setFont(Font.font("Arial", FontWeight.NORMAL, 17));
        HBox emailBox = new HBox(emailField); emailBox.setAlignment(Pos.CENTER);

        PasswordField passwordField = new PasswordField();
        TextField passwordVisibleField = new TextField();
        passwordVisibleField.setManaged(false);
        passwordVisibleField.setVisible(false);
        passwordVisibleField.setMaxWidth(360);
        passwordVisibleField.setPrefHeight(48);
        passwordVisibleField.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        passwordField.setPrefHeight(48);
        passwordField.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        ImageView eyeImg = new ImageView(new Image(getClass().getResourceAsStream("/eye.png")));
        eyeImg.setFitHeight(32); eyeImg.setFitWidth(32);
        eyeImg.setPickOnBounds(true);

        StackPane pwStack = new StackPane(passwordField, passwordVisibleField, eyeImg);
        pwStack.setMaxWidth(360);
        pwStack.setPrefHeight(48);
        StackPane.setAlignment(eyeImg, Pos.CENTER_RIGHT);
        StackPane.setMargin(eyeImg, new Insets(0,12,0,0));
        passwordField.setStyle("-fx-padding: 0 54 0 0;");
        passwordVisibleField.setStyle("-fx-padding: 0 54 0 0;");

        eyeImg.setOnMouseClicked(e -> {
            boolean showing = passwordVisibleField.isVisible();
            if (!showing) {
                passwordVisibleField.setText(passwordField.getText());
                passwordVisibleField.setVisible(true);
                passwordVisibleField.setManaged(true);
                passwordField.setVisible(false);
                passwordField.setManaged(false);
            } else {
                passwordField.setText(passwordVisibleField.getText());
                passwordVisibleField.setVisible(false);
                passwordVisibleField.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
            }
        });

        Button loginBtn = new Button("Entra");
        loginBtn.setStyle("-fx-font-size: 19px; -fx-background-color: #1CA9E2; -fx-text-fill: #181818;");
        loginBtn.setMaxWidth(150);

        ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/left_arrow.png")));
        backIcon.setFitHeight(26); backIcon.setFitWidth(26);
        Button indietroBtn = new Button("", backIcon);
        indietroBtn.setStyle("-fx-background-color: transparent;");
        indietroBtn.setMinSize(40, 40);
        HBox boxIndietro = new HBox(indietroBtn);
        boxIndietro.setAlignment(Pos.CENTER);
        boxIndietro.setPadding(new Insets(18, 0, 0, 0));

        Label messageLabel = new Label("");
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        messageLabel.setTextFill(Color.web("#1CA9E2"));
        messageLabel.setAlignment(Pos.CENTER);

        root.getChildren().addAll(titleBox, emailBox, pwStack, boxIndietro, loginBtn, messageLabel);

        loginBtn.setOnAction(ev -> {
            String email = emailField.getText().trim();
            String password = passwordField.isVisible()
                    ? passwordField.getText().trim()
                    : passwordVisibleField.getText().trim();

            UtenteBean utente = EntryPointViewFX.loginController.doLoginReturnUtente(email, password);
            if (utente == null) {
                messageLabel.setText("Login fallito! Controlla le credenziali.");
                return;
            }
            switch (utente.getRuolo()) {
                case ANIMATORE:
                    AnimatoreMenuFX menuAnimFX = new AnimatoreMenuFX(primaryStage, utente);
                    menuAnimFX.show();
                    break;
                default:
                    messageLabel.setText("Ruolo non supportato!");
                    break;
            }
        });

        indietroBtn.setOnAction(ev -> {
            MainMenuFX mainMenu = new MainMenuFX(primaryStage);
            mainMenu.show();
        });

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }
}