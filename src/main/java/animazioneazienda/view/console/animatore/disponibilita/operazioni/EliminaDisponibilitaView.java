package animazioneazienda.view.console.animatore.disponibilita.operazioni;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.dao.animatore.disponibilita.DisponibilitaAnimatoreRepository;
import animazioneazienda.exception.DaoException;
import java.util.List;
import java.util.Scanner;

public class EliminaDisponibilitaView {
    private final DisponibilitaAnimatoreRepository disponibilitaRepository;
    private final UtenteBean animatore;
    private final Scanner scanner = new Scanner(System.in);

    public EliminaDisponibilitaView(DisponibilitaAnimatoreRepository disponibilitaRepository, UtenteBean animatore) {
        this.disponibilitaRepository = disponibilitaRepository;
        this.animatore = animatore;
    }

    public void rimuoviDisponibilita() {
        try {
            List<DisponibilitaAnimatoreBean> lista = disponibilitaRepository.findByAnimatore(animatore.getAziendaId(), animatore.getId());
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
            DisponibilitaAnimatoreBean bean = lista.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
            if (bean == null) {
                System.out.println("ID non valido.");
                return;
            }
            boolean ok = disponibilitaRepository.eliminaDisponibilita(bean);
            if (ok) {
                System.out.println("Disponibilità rimossa!");
            } else {
                System.out.println("Errore durante la rimozione.");
            }
        } catch (DaoException e) {
            System.out.println("Errore eliminazione disponibilità: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }
}