package animazioneazienda.view.console.animatore.disponibilita.operazioni;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.dao.animatore.disponibilita.DisponibilitaAnimatoreRepository;
import animazioneazienda.exception.DaoException;
import java.util.List;

public class VisualizzaDisponibilitaView {
    private final DisponibilitaAnimatoreRepository disponibilitaRepository;
    private final UtenteBean animatore;

    public VisualizzaDisponibilitaView(DisponibilitaAnimatoreRepository disponibilitaRepository, UtenteBean animatore) {
        this.disponibilitaRepository = disponibilitaRepository;
        this.animatore = animatore;
    }

    public void visualizzaDisponibilita() {
        try {
            List<DisponibilitaAnimatoreBean> lista = disponibilitaRepository.findByAnimatore(animatore.getAziendaId(), animatore.getId());
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
        } catch (DaoException e) {
            System.out.println("Errore nella visualizzazione: " + e.getMessage());
        }
    }
}