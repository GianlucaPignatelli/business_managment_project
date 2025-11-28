package animazioneazienda.view.console.animatore.disponibilita;

import animazioneazienda.bean.Utente;
import animazioneazienda.bean.animatore.DisponibilitaAnimatore;
import animazioneazienda.dao.animatore.DisponibilitaAnimatoreDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class InserisciDisponibilitaView {
    private final DisponibilitaAnimatoreDAO disponibilitaDAO;
    private final Utente animatore;
    private final Scanner scanner = new Scanner(System.in);

    public InserisciDisponibilitaView(DisponibilitaAnimatoreDAO disponibilitaDAO, Utente animatore) {
        this.disponibilitaDAO = disponibilitaDAO;
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

            // CONTROLLO SOVRAPPOSIZIONE & DUPLICATI
            if (disponibilitaDAO.esisteDisponibilitaSovrapposta(animatore.getAziendaId(), animatore.getId(), data, inizio, fine, tuttoIlGiorno)) {
                System.out.println("Errore: Esiste già una disponibilità o una fascia sovrapposta per questa data!");
                return;
            }

            DisponibilitaAnimatore d = new DisponibilitaAnimatore();
            d.setAziendaId(animatore.getAziendaId());
            d.setAnimatoreId(animatore.getId());
            d.setData(data);
            d.setTuttoIlGiorno(tuttoIlGiorno);
            d.setOrarioInizio(inizio);
            d.setOrarioFine(fine);

            boolean ok = disponibilitaDAO.insertDisponibilita(d);
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