package animazioneazienda.view.console;

import animazioneazienda.controller.LoginController;
import animazioneazienda.bean.Utente;
import animazioneazienda.dao.AziendaDAO;

import java.sql.SQLException;
import java.util.Scanner;

public class LoginView {
    private final LoginController loginController;
    private final AziendaDAO aziendaDAO;
    private final Scanner scanner = new Scanner(System.in);

    public LoginView(LoginController loginController, AziendaDAO aziendaDAO) {
        this.loginController = loginController;
        this.aziendaDAO = aziendaDAO;
    }

    // Validazione email: solo @gmail.com
    private boolean isEmailValid(String email) {
        return email != null && email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$");
    }

    // Restituisce l'utente loggato, oppure null
    public Utente doLoginReturnUtente() {
        System.out.println("--- LOGIN ---");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (!isEmailValid(email)) {
            System.out.println("Email non valida! Usa una email @gmail.com.");
            return null;
        }
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Utente utente = loginController.doLoginReturnUtente(email, password);
        if (utente != null) {
            return utente;
        } else {
            System.out.println("Login fallito: credenziali non corrette o utente inesistente.");
            return null;
        }
    }

    public void doRegistrazione() throws SQLException {
        System.out.println("--- REGISTRATI ---");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (!isEmailValid(email)) {
            System.out.println("Email non valida! Usa una email @gmail.com.");
            return;
        }
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Ruolo (AMMINISTRATORE / ANIMATORE): ");
        String ruoloScelto = scanner.nextLine().toUpperCase();
        Utente.Ruolo ruolo;
        if ("AMMINISTRATORE".equals(ruoloScelto)) {
            ruolo = Utente.Ruolo.AMMINISTRATORE;
        } else if ("ANIMATORE".equals(ruoloScelto)) {
            ruolo = Utente.Ruolo.ANIMATORE;
        } else {
            System.out.println("Ruolo non valido!");
            return;
        }

        System.out.print("Nome azienda (come registrato dal Superadmin): ");
        String nomeAzienda = scanner.nextLine().trim();

        int aziendaId = aziendaDAO.findIdByNome(nomeAzienda);
        if (aziendaId == -1) {
            System.out.println("Azienda non esistente! Chiedi al Superadmin di registrare la tua azienda.");
            return;
        }

        boolean successo = loginController.registraUtente(email, password, ruolo, aziendaId);
        if (successo) {
            System.out.println("Registrazione avvenuta con successo!");
        } else {
            System.out.println("Registrazione fallita: email già presente.");
        }
    }
}