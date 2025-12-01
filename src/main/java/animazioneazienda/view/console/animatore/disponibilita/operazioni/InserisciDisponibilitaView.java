package animazioneazienda.view.console.animatore.disponibilita.operazioni;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.dao.animatore.disponibilita.DisponibilitaAnimatoreRepository;
import animazioneazienda.exception.DaoException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class InserisciDisponibilitaView {
    private final DisponibilitaAnimatoreRepository disponibilitaRepository;
    private final UtenteBean animatore;
    private final Scanner scanner = new Scanner(System.in);

    public InserisciDisponibilitaView(DisponibilitaAnimatoreRepository disponibilitaRepository, UtenteBean animatore) {
        this.disponibilitaRepository = disponibilitaRepository;
        this.animatore = animatore;
    }

    public void aggiungiDisponibilita() {
        try {
            System.out.print("Data (YYYY-MM-DD): ");
            LocalDate data = LocalDate.parse(scanner.nextLine().trim());
            LocalDate oggi = LocalDate.now();
            if (data.isBefore(oggi)) {
                System.out.println("Errore: Non puoi inserire una disponibilità per una data passata!");
                return;
            }
            System.out.print("Disponibilità per tutto il giorno? (s/n): ");
            boolean tuttoIlGiorno = scanner.nextLine().trim().equalsIgnoreCase("s");
            LocalTime inizio = null;
            LocalTime fine = null;
            if (!tuttoIlGiorno) {
                System.out.print("Orario inizio (HH:MM): ");
                inizio = LocalTime.parse(scanner.nextLine().trim());
                System.out.print("Orario fine (HH:MM): ");
                fine = LocalTime.parse(scanner.nextLine().trim());
                if (!fine.isAfter(inizio)) {
                    System.out.println("Errore: L'orario di fine deve essere successivo all'orario di inizio!");
                    return;
                }
            }
            boolean ok = disponibilitaRepository.inserisciDisponibilita(
                    animatore.getAziendaId(),
                    animatore.getId(),
                    data,
                    inizio,
                    fine,
                    tuttoIlGiorno
            );
            if (ok) {
                System.out.println("Disponibilità aggiunta!");
            } else {
                System.out.println("Errore nell'aggiunta.");
            }
        } catch (DaoException e) {
            System.out.println("Errore inserimento disponibilità: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }
}