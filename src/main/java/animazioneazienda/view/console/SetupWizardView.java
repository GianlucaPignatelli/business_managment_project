package animazioneazienda.view.console;

import animazioneazienda.dao.AziendaDAO;
import animazioneazienda.dao.UtenteDAO;
import animazioneazienda.bean.Utente;
import java.sql.SQLException;
import java.util.Scanner;

public class SetupWizardView {
    private final AziendaDAO aziendaDAO;
    private final UtenteDAO utenteDAO;

    public SetupWizardView(AziendaDAO aziendaDAO, UtenteDAO utenteDAO) {
        this.aziendaDAO = aziendaDAO;
        this.utenteDAO = utenteDAO;
    }

    public void mostraWizard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n==== SETUP INIZIALE APPLICAZIONE ====");
        System.out.print("Inserisci il nome della prima azienda: ");
        String nomeAzienda = scanner.nextLine();
        try {
            int aziendaId = aziendaDAO.registraAzienda(nomeAzienda);
            if (aziendaId <= 0) throw new Exception("Errore creazione azienda");

            System.out.println("Azienda creata con successo (ID: " + aziendaId + ")");
            System.out.println("\n--- Registra il primo utente (SUPERADMIN) ---");
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String pw = scanner.nextLine();

            boolean res = false;
            try {
                res = utenteDAO.insertUtente(
                        new Utente(email, pw, Utente.Ruolo.SUPERADMIN, aziendaId, nomeAzienda)
                );
            } catch (SQLException ex) {
                System.out.println("Errore registrazione SUPERADMIN: " + ex.getMessage());
            }

            if (res) {
                System.out.println("SUPERADMIN creato! Ora puoi fare login/gestire le aziende.");
            } else {
                System.out.println("Errore registrazione SUPERADMIN!");
            }
        } catch (Exception e) {
            System.out.println("Errore setup iniziale: " + e.getMessage());
        }
    }
}