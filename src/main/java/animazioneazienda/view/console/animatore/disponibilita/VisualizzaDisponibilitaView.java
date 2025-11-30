package animazioneazienda.view.console.animatore.disponibilita;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.dao.animatore.disponibilita.VisualizzaDisponibilitaDAO;

import java.util.List;

public class VisualizzaDisponibilitaView {
    private final VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO;
    private final UtenteBean animatore;

    public VisualizzaDisponibilitaView(VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO, UtenteBean animatore) {
        this.visualizzaDisponibilitaDAO = visualizzaDisponibilitaDAO;
        this.animatore = animatore;
    }

    public void visualizzaDisponibilita() {
        try {
            List<DisponibilitaAnimatoreBean> lista = visualizzaDisponibilitaDAO.trovaPerAnimatore(animatore.getAziendaId(), animatore.getId());
            System.out.println("\n--- Disponibilità inserite ---");
            if (lista.isEmpty()) {
                System.out.println("Nessuna disponibilità inserita.");
            } else {
                for (DisponibilitaAnimatoreBean d : lista) {
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