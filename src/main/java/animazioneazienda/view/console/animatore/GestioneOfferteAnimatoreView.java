package animazioneazienda.view.console.animatore;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.OffertaLavoroBean;
import animazioneazienda.dao.animatore.OffertaLavoroDAO;
import java.util.List;
import java.util.Scanner;

public class GestioneOfferteAnimatoreView {
    private final OffertaLavoroDAO offertaDAO;
    private final UtenteBean animatore;
    private final Scanner scanner = new Scanner(System.in);

    public GestioneOfferteAnimatoreView(OffertaLavoroDAO offertaDAO, UtenteBean animatore) {
        this.offertaDAO = offertaDAO;
        this.animatore = animatore;
    }

    public void gestisciOfferteLavoro() {
        try {
            List<OffertaLavoroBean> lista = offertaDAO.findByAnimatore(animatore.getAziendaId(), animatore.getId());
            System.out.println("\n--- OFFERTE LAVORO RICEVUTE ---");
            if (lista.isEmpty()) {
                System.out.println("Nessuna offerta ricevuta.");
                return;
            }
            for (OffertaLavoroBean o : lista) {
                System.out.println("ID Offerta: " + o.getId() +
                        " | Data evento: " + o.getDataEvento() +
                        " | Fascia: " + o.getOrarioInizio() + "-" + o.getOrarioFine() +
                        " | Descrizione: " + o.getDescrizione() +
                        " | Stato: " + o.getStato());
            }
            System.out.print("\nVuoi aggiornare lo stato di una offerta? (s/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                System.out.print("ID offerta da aggiornare: ");
                int id = Integer.parseInt(scanner.nextLine());
                OffertaLavoroBean selezionata = lista.stream().filter(o -> o.getId() == id).findFirst().orElse(null);
                if (selezionata == null) {
                    System.out.println("ID offerta non valido.");
                    return;
                }
                System.out.print("Nuovo stato (accettato/rifiutato): ");
                String nuovoStato = scanner.nextLine().trim().toLowerCase();
                if ("accettato".equals(nuovoStato) || "rifiutato".equals(nuovoStato)) {
                    boolean ok = offertaDAO.aggiornaStato(id, nuovoStato, animatore.getAziendaId(), animatore.getId());
                    if (ok) {
                        System.out.println("Stato offerta aggiornato!");
                    } else {
                        System.out.println("Errore nell'aggiornamento stato.");
                    }
                } else {
                    System.out.println("Stato non valido!");
                }
            }
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }
}