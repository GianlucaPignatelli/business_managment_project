package animazioneazienda.view.console.animatore.disponibilita;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.dao.animatore.disponibilita.EliminaDisponibilitaDAO;
import animazioneazienda.dao.animatore.disponibilita.VisualizzaDisponibilitaDAO;
import java.util.List;
import java.util.Scanner;

public class EliminaDisponibilitaView {
    private final EliminaDisponibilitaDAO eliminaDisponibilitaDAO;
    private final VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO;
    private final UtenteBean animatore;
    private final Scanner scanner = new Scanner(System.in);

    public EliminaDisponibilitaView(
            EliminaDisponibilitaDAO eliminaDisponibilitaDAO,
            VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO,
            UtenteBean animatore
    ) {
        this.eliminaDisponibilitaDAO = eliminaDisponibilitaDAO;
        this.visualizzaDisponibilitaDAO = visualizzaDisponibilitaDAO;
        this.animatore = animatore;
    }

    public void rimuoviDisponibilita() {
        try {
            List<DisponibilitaAnimatoreBean> lista = visualizzaDisponibilitaDAO.trovaPerAnimatore(animatore.getAziendaId(), animatore.getId());
            if (lista.isEmpty()) {
                System.out.println("Nessuna disponibilità da rimuovere.");
                return;
            }
            System.out.println("Seleziona ID della disponibilità da rimuovere:");
            for (DisponibilitaAnimatoreBean d : lista) {
                System.out.print("ID: " + d.getId() + " | Data: " + d.getData());
                if (d.isTuttoIlGiorno()) {
                    System.out.println(" | Tutto il giorno");
                } else {
                    System.out.println(" | Dalle " + d.getOrarioInizio() + " alle " + d.getOrarioFine());
                }
            }
            System.out.print("ID da rimuovere: ");
            int id = Integer.parseInt(scanner.nextLine());

            boolean ok = eliminaDisponibilitaDAO.elimina(id, animatore.getAziendaId(), animatore.getId());
            if (ok) {
                System.out.println("Disponibilità rimossa!");
            } else {
                System.out.println("Errore durante la rimozione.");
            }
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }
}