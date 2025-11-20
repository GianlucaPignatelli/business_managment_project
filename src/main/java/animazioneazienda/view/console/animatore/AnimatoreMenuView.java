package animazioneazienda.view.console.animatore;

import animazioneazienda.bean.Utente;
import animazioneazienda.bean.animatore.DisponibilitaAnimatore;
import animazioneazienda.bean.animatore.StatusAnimatore;
import animazioneazienda.bean.animatore.OffertaLavoro;
import animazioneazienda.dao.animatore.DisponibilitaAnimatoreDAO;
import animazioneazienda.dao.animatore.StatusAnimatoreDAO;
import animazioneazienda.dao.animatore.OffertaLavoroDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class AnimatoreMenuView {
    private final DisponibilitaAnimatoreDAO disponibilitaDAO;
    private final StatusAnimatoreDAO statusDAO;
    private final OffertaLavoroDAO offertaDAO;
    private final Utente animatore;
    private final Scanner scanner = new Scanner(System.in);

    public AnimatoreMenuView(
            DisponibilitaAnimatoreDAO disponibilitaDAO,
            StatusAnimatoreDAO statusDAO,
            OffertaLavoroDAO offertaDAO,
            Utente animatore
    ) {
        this.disponibilitaDAO = disponibilitaDAO;
        this.statusDAO = statusDAO;
        this.offertaDAO = offertaDAO;
        this.animatore = animatore;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- MENU ANIMATORE ---");
            System.out.println("1. Gestisci disponibilità calendario");
            System.out.println("2. Aggiorna profilo/status");
            System.out.println("3. Visualizza offerte di lavoro");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");
            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1":
                    sottoMenuDisponibilita();
                    break;
                case "2":
                    gestisciStatusAnimatore();
                    break;
                case "3":
                    gestisciOfferteLavoro();
                    break;
                case "0":
                    System.out.println("Uscita dal menu Animatore.");
                    return;
                default:
                    System.out.println("Scelta non valida!");
            }
        }
    }

    // --- GESTIONE DISPONIBILITÀ ---
    private void sottoMenuDisponibilita() {
        while (true) {
            System.out.println("\n--- GESTIONE DISPONIBILITÀ ---");
            System.out.println("1. Visualizza disponibilità");
            System.out.println("2. Aggiungi disponibilità");
            System.out.println("3. Modifica disponibilità");
            System.out.println("4. Rimuovi disponibilità");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");
            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1":
                    visualizzaDisponibilita();
                    break;
                case "2":
                    aggiungiDisponibilita();
                    break;
                case "3":
                    modificaDisponibilita();
                    break;
                case "4":
                    rimuoviDisponibilita();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Scelta non valida!");
            }
        }
    }

    private void visualizzaDisponibilita() {
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

    private void aggiungiDisponibilita() {
        try {
            System.out.print("Data (YYYY-MM-DD): ");
            LocalDate data = LocalDate.parse(scanner.nextLine().trim());

            // Verifica che la data NON sia nel passato
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

                // Verifica che l'orario di fine sia dopo l'orario di inizio
                if (!fine.isAfter(inizio)) {
                    System.out.println("Errore: L'orario di fine deve essere successivo all'orario di inizio!");
                    return;
                }
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

    private void modificaDisponibilita() {
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

            // Verifica che la data NON sia nel passato
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

                // Verifica che l'orario di fine sia dopo l'orario di inizio
                if (!fine.isAfter(inizio)) {
                    System.out.println("Errore: L'orario di fine deve essere successivo all'orario di inizio!");
                    return;
                }
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

    private void rimuoviDisponibilita() {
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

    // --- GESTIONE PROFILO/STATUS ANIMATORE ---
    private void gestisciStatusAnimatore() {
        try {
            StatusAnimatore currStatus = statusDAO.findByAnimatore(animatore.getAziendaId(), animatore.getId());
            System.out.println("\n--- IL TUO PROFILO ---");
            if (currStatus != null) {
                System.out.println("Modello auto: " + currStatus.getModelloAuto());
                System.out.println("Dimensione auto: " + currStatus.getDimensioneAuto());
                System.out.println("Tipologie lavori accettati: " + currStatus.getLavoriAccettati());
                System.out.println("Certificazione HACCP: " + (currStatus.isHaccp() ? "Sì" : "No"));
                System.out.println("Stato: " + currStatus.getStato());
            } else {
                System.out.println("Nessun profilo registrato!");
            }
            System.out.println("\n--- AGGIORNA DATI PROFILO ---");

            System.out.print("Modello auto: ");
            String modelloAuto = scanner.nextLine();

            System.out.print("Dimensione auto (piccola/media/grande/furgone): ");
            String dimensioneAuto = scanner.nextLine();

            String[] elencoRuoli = {
                    "Capoanimatore",
                    "Aiutoanimatore",
                    "Delivery",
                    "Operatore carretti"
            };
            System.out.println("Seleziona i ruoli desiderati (numeri separati da virgola):");
            for (int i = 0; i < elencoRuoli.length; i++) {
                System.out.printf("%d. %s%n", i + 1, elencoRuoli[i]);
            }
            System.out.print("Scelta: ");
            String input = scanner.nextLine();
            String[] scelte = input.split(",");
            List<String> ruoliSelezionati = new ArrayList<>();
            boolean haccp = false;
            for (String s : scelte) {
                try {
                    int idx = Integer.parseInt(s.trim()) - 1;
                    if (idx >= 0 && idx < elencoRuoli.length) {
                        String ruolo = elencoRuoli[idx];
                        ruoliSelezionati.add(ruolo);
                        if ("Operatore carretti".equals(ruolo)) {
                            System.out.print("Hai la certificazione HACCP? (s/n): ");
                            String val = scanner.nextLine().trim().toLowerCase();
                            haccp = val.equals("s");
                        }
                    }
                } catch (NumberFormatException ignored) {}
            }
            String lavoriAccettati = String.join(",", ruoliSelezionati);

            System.out.print("Stato (Disponibile/Non operativo): ");
            String stato = scanner.nextLine();

            StatusAnimatore s = new StatusAnimatore();
            s.setAnimatoreId(animatore.getId());
            s.setAziendaId(animatore.getAziendaId());
            s.setModelloAuto(modelloAuto);
            s.setDimensioneAuto(dimensioneAuto);
            s.setLavoriAccettati(lavoriAccettati);
            s.setStato(stato);
            s.setHaccp(haccp);

            boolean ok = statusDAO.insertOrUpdate(s);
            if (ok) {
                System.out.println("Profilo aggiornato!");
            } else {
                System.out.println("Errore nell'aggiornamento profilo.");
            }
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    // --- GESTIONE OFFERTE DI LAVORO ---
    private void gestisciOfferteLavoro() {
        try {
            List<OffertaLavoro> lista = offertaDAO.findByAnimatore(animatore.getAziendaId(), animatore.getId());
            System.out.println("\n--- OFFERTE LAVORO RICEVUTE ---");
            if (lista.isEmpty()) {
                System.out.println("Nessuna offerta ricevuta.");
                return;
            }
            for (OffertaLavoro o : lista) {
                System.out.println("ID Offerta: " + o.getId() +
                        " | Data evento: " + o.getDataEvento() +
                        " | Fascia: " + o.getOrarioInizio() + "-" + o.getOrarioFine() +
                        " | Descrizione: " + o.getDescrizione() +
                        " | Stato: " + o.getStato());
            }
            System.out.print("\nVuoi aggiornare lo stato di una offerta? (s/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                System.out.print("ID offerta da aggiornare: ");
                int id = Integer.parseInt(scanner.nextLine());
                OffertaLavoro selezionata = lista.stream().filter(o -> o.getId() == id).findFirst().orElse(null);
                if (selezionata == null) {
                    System.out.println("ID offerta non valido.");
                    return;
                }
                System.out.print("Nuovo stato (accettato/rifiutato): ");
                String nuovoStato = scanner.nextLine().trim().toLowerCase();
                if ("accettato".equals(nuovoStato) || "rifiutato".equals(nuovoStato)) {
                    boolean ok = offertaDAO.aggiornaStato(id, nuovoStato, animatore.getAziendaId(), animatore.getId());
                    if (ok) {
                        System.out.println("Stato offerta aggiornato!");
                    } else {
                        System.out.println("Errore nell'aggiornamento stato.");
                    }
                } else {
                    System.out.println("Stato non valido!");
                }
            }
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }
}