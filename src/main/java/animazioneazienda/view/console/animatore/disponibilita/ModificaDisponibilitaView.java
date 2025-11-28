package animazioneazienda.view.console.animatore.disponibilita;

import animazioneazienda.bean.Utente;
import animazioneazienda.bean.animatore.DisponibilitaAnimatore;
import animazioneazienda.dao.animatore.DisponibilitaAnimatoreDAO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class ModificaDisponibilitaView {
    private final DisponibilitaAnimatoreDAO disponibilitaDAO;
    private final Utente animatore;
    private final Scanner scanner = new Scanner(System.in);

    public ModificaDisponibilitaView(DisponibilitaAnimatoreDAO disponibilitaDAO, Utente animatore) {
        this.disponibilitaDAO = disponibilitaDAO;
        this.animatore = animatore;
    }

    public void modificaDisponibilita() {
        try {
            List<DisponibilitaAnimatore> lista = disponibilitaDAO.findByAnimatore(animatore.getAziendaId(), animatore.getId());
            if (lista.isEmpty()) {
                System.out.println("Nessuna disponibilità da modificare.");
                return;
            }
            System.out.println("Seleziona ID della disponibilità da modificare:");
            for (DisponibilitaAnimatore d : lista) {
                System.out.print("ID: " + d.getId() + " | Data: " + d.getData());
                if (d.isTuttoIlGiorno()) {
                    System.out.println(" | Tutto il giorno");
                } else {
                    System.out.println(" | Dalle " + d.getOrarioInizio() + " alle " + d.getOrarioFine());
                }
            }
            System.out.print("ID da modificare: ");
            int id = Integer.parseInt(scanner.nextLine());

            DisponibilitaAnimatore old = lista.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
            if (old == null) {
                System.out.println("ID non valido.");
                return;
            }

            System.out.print("Nuova data (YYYY-MM-DD): ");
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

            // CONTROLLA SOVRAPPOSIZIONE SE cambi data/orario
            if ((!old.getData().equals(data) ||
                    old.isTuttoIlGiorno() != tuttoIlGiorno ||
                    (old.getOrarioInizio() != null && !old.getOrarioInizio().equals(inizio)) ||
                    (old.getOrarioFine() != null && !old.getOrarioFine().equals(fine))) &&
                    disponibilitaDAO.esisteDisponibilitaSovrapposta(animatore.getAziendaId(), animatore.getId(), data, inizio, fine, tuttoIlGiorno)) {
                System.out.println("Errore: Esiste già una disponibilità o una fascia sovrapposta per questa data!");
                return;
            }

            old.setData(data);
            old.setTuttoIlGiorno(tuttoIlGiorno);
            old.setOrarioInizio(inizio);
            old.setOrarioFine(fine);

            boolean ok = disponibilitaDAO.updateDisponibilita(old);
            if (ok) {
                System.out.println("Disponibilità aggiornata!");
            } else {
                System.out.println("Errore nell'aggiornamento.");
            }
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }
}