package animazioneazienda.view.console.animatore.disponibilita;

import animazioneazienda.bean.Utente;
import animazioneazienda.bean.animatore.DisponibilitaAnimatore;
import animazioneazienda.dao.animatore.DisponibilitaAnimatoreDAO;
import java.util.List;
import java.util.Scanner;

public class EliminaDisponibilitaView {
    private final DisponibilitaAnimatoreDAO disponibilitaDAO;
    private final Utente animatore;
    private final Scanner scanner = new Scanner(System.in);

    public EliminaDisponibilitaView(DisponibilitaAnimatoreDAO disponibilitaDAO, Utente animatore) {
        this.disponibilitaDAO = disponibilitaDAO;
        this.animatore = animatore;
    }

    public void rimuoviDisponibilita() {
        try {
            List<DisponibilitaAnimatore> lista = disponibilitaDAO.findByAnimatore(animatore.getAziendaId(), animatore.getId());
            if (lista.isEmpty()) {
                System.out.println("Nessuna disponibilità da rimuovere.");
                return;
            }
            System.out.println("Seleziona ID della disponibilità da rimuovere:");
            for (DisponibilitaAnimatore d : lista) {
                System.out.print("ID: " + d.getId() + " | Data: " + d.getData());
                if (d.isTuttoIlGiorno()) {
                    System.out.println(" | Tutto il giorno");
                } else {
                    System.out.println(" | Dalle " + d.getOrarioInizio() + " alle " + d.getOrarioFine());
                }
            }
            System.out.print("ID da rimuovere: ");
            int id = Integer.parseInt(scanner.nextLine());

            boolean ok = disponibilitaDAO.removeDisponibilita(id, animatore.getAziendaId(), animatore.getId());
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