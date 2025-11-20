package animazioneazienda.view.console;

import animazioneazienda.dao.AziendaDAO;
import java.util.Scanner;

public class AziendaView {
    private final AziendaDAO aziendaDAO;
    private final Scanner scanner;

    public AziendaView(AziendaDAO aziendaDAO) {
        this.aziendaDAO = aziendaDAO;
        this.scanner = new Scanner(System.in);
    }

    public void registraAzienda() {
        System.out.print("Inserisci il nome della nuova azienda: ");
        String nome = scanner.nextLine();
        try {
            int id = aziendaDAO.registraAzienda(nome);
            if (id > 0) {
                System.out.println("Azienda registrata con successo (ID: " + id + ")");
            } else {
                System.out.println("Errore registrazione azienda.");
            }
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    // Funzione usata SOLO per mostrare le aziende disponibili all'utente durante login/registrazione
    public void contaAziende() {
        try {
            aziendaDAO.contaAziende();
        } catch (Exception e) {
            System.out.println("Errore visualizzazione aziende.");
        }
    }
}