package animazioneazienda.view.console.animatore.status;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.StatusAnimatoreBean;
import animazioneazienda.controller.animatore.AnimatoreStatusController;
import animazioneazienda.exception.DaoException;
import java.util.*;

public class GestioneStatusAnimatoreView {
    private final UtenteBean animatore;
    private final AnimatoreStatusController animatoreStatusController;
    private final Scanner scanner = new Scanner(System.in);

    public GestioneStatusAnimatoreView(UtenteBean animatore, AnimatoreStatusController animatoreStatusController) {
        this.animatore = animatore;
        this.animatoreStatusController = animatoreStatusController;
    }

    public void gestisciStatusAnimatore() {
        try {
            StatusAnimatoreBean currStatus = animatoreStatusController.caricaStatus(animatore.getAziendaId(), animatore.getId());
            System.out.println("\n--- IL TUO PROFILO ---");
            if (currStatus != null) {
                System.out.println("Modello auto: " + currStatus.getModelloAuto());
                System.out.println("Dimensione auto: " + currStatus.getDimensioneAuto());
                System.out.println("Tipologie lavori accettati: " + currStatus.getLavoriAccettati());
                System.out.println("Certificazione HACCP: " + (currStatus.isHaccp() ? "Sì" : "No"));
                System.out.println("Stato: " + currStatus.getStato());
            } else {
                System.out.println("Nessun profilo registrato!");
            }
            System.out.println("\n--- AGGIORNA DATI PROFILO ---");

            System.out.print("Modello auto: ");
            String modelloAuto = scanner.nextLine();

            System.out.print("Dimensione auto (piccola/media/grande/furgone): ");
            String dimensioneAuto = scanner.nextLine();

            String[] elencoRuoli = {
                    "Capoanimatore",
                    "Aiutoanimatore",
                    "Delivery",
                    "Operatore carretti"
            };
            System.out.println("Seleziona i ruoli desiderati (numeri separati da virgola):");
            for (int i = 0; i < elencoRuoli.length; i++) {
                System.out.printf("%d. %s%n", i + 1, elencoRuoli[i]);
            }
            System.out.print("Scelta: ");
            String input = scanner.nextLine();
            String[] scelte = input.split(",");
            List<String> ruoliSelezionati = new ArrayList<>();
            boolean haccp = false;
            for (String s : scelte) {
                try {
                    int idx = Integer.parseInt(s.trim()) - 1;
                    if (idx >= 0 && idx < elencoRuoli.length) {
                        String ruolo = elencoRuoli[idx];
                        ruoliSelezionati.add(ruolo);
                        if ("Operatore carretti".equals(ruolo)) {
                            System.out.print("Hai la certificazione HACCP? (s/n): ");
                            String val = scanner.nextLine().trim().toLowerCase();
                            haccp = val.equals("s");
                        }
                    }
                } catch (NumberFormatException ignored) {}
            }
            String lavoriAccettati = String.join(",", ruoliSelezionati);

            System.out.print("Stato (Disponibile/Non operativo): ");
            String stato = scanner.nextLine();

            StatusAnimatoreBean s = new StatusAnimatoreBean();
            s.setAnimatoreId(animatore.getId());
            s.setAziendaId(animatore.getAziendaId());
            s.setModelloAuto(modelloAuto);
            s.setDimensioneAuto(dimensioneAuto);
            s.setLavoriAccettati(lavoriAccettati);
            s.setStato(stato);
            s.setHaccp(haccp);

            boolean ok = animatoreStatusController.salvaOAggiornaStatus(s);
            if (ok) {
                System.out.println("Profilo aggiornato!");
            } else {
                System.out.println("Errore nell'aggiornamento profilo.");
            }
        } catch (DaoException de) {
            System.out.println("Errore di persistenza: " + de.getMessage());
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }
}