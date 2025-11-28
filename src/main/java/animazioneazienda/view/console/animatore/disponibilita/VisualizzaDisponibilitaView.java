package animazioneazienda.view.console.animatore.disponibilita;

import animazioneazienda.bean.Utente;
import animazioneazienda.bean.animatore.DisponibilitaAnimatore;
import animazioneazienda.dao.animatore.DisponibilitaAnimatoreDAO;

import java.util.List;

public class VisualizzaDisponibilitaView {
    private final DisponibilitaAnimatoreDAO disponibilitaDAO;
    private final Utente animatore;

    public VisualizzaDisponibilitaView(DisponibilitaAnimatoreDAO disponibilitaDAO, Utente animatore) {
        this.disponibilitaDAO = disponibilitaDAO;
        this.animatore = animatore;
    }

    public void visualizzaDisponibilita() {
        try {
            List<DisponibilitaAnimatore> lista = disponibilitaDAO.findByAnimatore(animatore.getAziendaId(), animatore.getId());
            System.out.println("\n--- Disponibilità inserite ---");
            if (lista.isEmpty()) {
                System.out.println("Nessuna disponibilità inserita.");
            } else {
                for (DisponibilitaAnimatore d : lista) {
                    System.out.print("ID: " + d.getId() + " | Data: " + d.getData());
                    if (d.isTuttoIlGiorno()) {
                        System.out.println(" | Tutto il giorno");
                    } else {
                        System.out.println(" | Dalle " + d.getOrarioInizio() + " alle " + d.getOrarioFine());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Errore nella visualizzazione: " + e.getMessage());
        }
    }
}