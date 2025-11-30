package animazioneazienda.view.console;

import animazioneazienda.controller.LoginController;
import animazioneazienda.bean.UtenteBean;
import java.util.Scanner;

public class LoginView {
    private final LoginController loginController;

    public LoginView(LoginController loginController) {
        this.loginController = loginController;
    }

    public UtenteBean doLoginReturnUtente() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        UtenteBean utente = loginController.doLoginReturnUtente(email, password);

        if (utente != null) {
            System.out.println("Benvenuto " + utente.getNome() + " " + utente.getCognome() + "!");
        } else {
            System.out.println("Login fallito! Credenziali errate o utente non trovato.");
        }
        return utente;
    }
}