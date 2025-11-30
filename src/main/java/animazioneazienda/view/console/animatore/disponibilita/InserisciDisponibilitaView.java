package animazioneazienda.view.console.animatore.disponibilita;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.dao.animatore.disponibilita.InserisciDisponibilitaDAO;
import animazioneazienda.dao.animatore.disponibilita.VisualizzaDisponibilitaDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class InserisciDisponibilitaView {
    private final InserisciDisponibilitaDAO inserisciDisponibilitaDAO;
    private final VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO;
    private final UtenteBean animatore;
    private final Scanner scanner = new Scanner(System.in);

    public InserisciDisponibilitaView(
            InserisciDisponibilitaDAO inserisciDisponibilitaDAO,
            VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO,
            UtenteBean animatore
    ) {
        this.inserisciDisponibilitaDAO = inserisciDisponibilitaDAO;
        this.visualizzaDisponibilitaDAO = visualizzaDisponibilitaDAO;
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
            // CONTROLLO SOVRAPPOSIZIONE
            if (visualizzaDisponibilitaDAO.esisteSovrapposizione(animatore.getAziendaId(), animatore.getId(), data, inizio, fine, tuttoIlGiorno)) {
                System.out.println("Errore: Esiste già una disponibilità o una fascia sovrapposta per questa data!");
                return;
            }
            DisponibilitaAnimatoreBean d = new DisponibilitaAnimatoreBean();
            d.setAziendaId(animatore.getAziendaId());
            d.setAnimatoreId(animatore.getId());
            d.setData(data);
            d.setTuttoIlGiorno(tuttoIlGiorno);
            d.setOrarioInizio(inizio);
            d.setOrarioFine(fine);
            boolean ok = inserisciDisponibilitaDAO.inserisci(d);
            if (ok) {
                System.out.println("Disponibilità aggiunta!");
            } else {
                System.out.println("Errore nell'aggiunta.");
            }
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }
}