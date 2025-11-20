package animazioneazienda.view.console.amministratore;

import animazioneazienda.dao.AziendaDAO;
import animazioneazienda.bean.Utente;

public class AdminMenuConsoleView {
    private final AziendaDAO aziendaDAO;
    private final Utente amministratore;

    public AdminMenuConsoleView(AziendaDAO aziendaDAO, Utente amministratore) {
        this.aziendaDAO = aziendaDAO;
        this.amministratore = amministratore;
    }

    public void showMenu() {
        System.out.println("\n--- MENU AMMINISTRATORE ---");
        System.out.println("Questa sezione sarà presto disponibile con le funzionalità di gestione magazzino, feste, inventario, ecc.");
        // Qui aggiungerai tutte le funzionalità nel prossimo sprint!
    }
}