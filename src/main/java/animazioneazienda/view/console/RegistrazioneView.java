package animazioneazienda.view.console;

import animazioneazienda.controller.LoginController;
import animazioneazienda.bean.UtenteBean;
import animazioneazienda.dao.AziendaDAO;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

public class RegistrazioneView {
    private final LoginController loginController;
    private final AziendaDAO aziendaDAO;

    public RegistrazioneView(LoginController loginController, AziendaDAO aziendaDAO) {
        this.loginController = loginController;
        this.aziendaDAO = aziendaDAO;
    }

    public void doRegistrazione() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();

        System.out.print("Cognome: ");
        String cognome = scanner.nextLine().trim();

        System.out.print("Data di nascita (YYYY-MM-DD): ");
        String dataNascitaStr = scanner.nextLine().trim();
        Date dataNascita;
        try {
            dataNascita = Date.valueOf(dataNascitaStr);
        } catch (Exception e) {
            System.out.println("Formato data non valido!");
            return;
        }

        System.out.print("Sesso (Maschio / Femmina / Altro): ");
        String sessoInput = scanner.nextLine().trim().toLowerCase();
        String sesso;
        if (sessoInput.equals("maschio")) {
            sesso = "M";
        } else if (sessoInput.equals("femmina")) {
            sesso = "F";
        } else if (sessoInput.equals("altro")) {
            sesso = "A";
        } else {
            System.out.println("Valore sesso non valido! Scrivi solo: Maschio, Femmina o Altro.");
            return;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        System.out.print("Nome azienda (come registrato dal Superadmin): ");
        String nomeAzienda = scanner.nextLine().trim();

        int aziendaId;
        try {
            aziendaId = aziendaDAO.findIdByNome(nomeAzienda);
        } catch (SQLException e) {
            System.out.println("Errore DB durante la ricerca dell'azienda: " + e.getMessage());
            return;
        }
        if (aziendaId == -1) {
            System.out.println("Azienda non esistente! Chiedi al Superadmin di registrarla.");
            return;
        }

        System.out.print("Ruolo (AMMINISTRATORE/ANIMATORE): ");
        String ruoloStr = scanner.nextLine().trim().toUpperCase();

        UtenteBean.Ruolo ruolo = "AMMINISTRATORE".equals(ruoloStr)
                ? UtenteBean.Ruolo.AMMINISTRATORE
                : UtenteBean.Ruolo.ANIMATORE;

        boolean successo = loginController.registraUtente(
                nome,
                cognome,
                dataNascita,
                sesso,
                email,
                password,
                nomeAzienda,
                ruolo,
                aziendaId
        );

        if (successo) {
            System.out.println("Registrazione avvenuta con successo!");
        } else {
            System.out.println("Registrazione fallita: email già presente oppure errore dati.");
        }
    }
}