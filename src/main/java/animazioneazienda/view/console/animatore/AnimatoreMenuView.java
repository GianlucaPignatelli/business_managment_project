package animazioneazienda.view.console.animatore;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.controller.animatore.AnimatoreDisponibilitaController;
import animazioneazienda.controller.animatore.AnimatoreOffertaController;
import animazioneazienda.controller.animatore.AnimatoreStatusController;
import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.bean.animatore.OffertaLavoroBean;
import animazioneazienda.bean.animatore.StatusAnimatoreBean;
import animazioneazienda.exception.DaoException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class AnimatoreMenuView {
    private final AnimatoreDisponibilitaController disponibilitaController;
    private final AnimatoreOffertaController animatoreOffertaController;
    private final AnimatoreStatusController animatoreStatusController;
    private final UtenteBean animatore;
    private final Scanner scan;

    public AnimatoreMenuView(
            AnimatoreDisponibilitaController disponibilitaController,
            AnimatoreOffertaController animatoreOffertaController,
            AnimatoreStatusController animatoreStatusController,
            UtenteBean animatore
    ) {
        this.disponibilitaController = disponibilitaController;
        this.animatoreOffertaController = animatoreOffertaController;
        this.animatoreStatusController = animatoreStatusController;
        this.animatore = animatore;
        this.scan = new Scanner(System.in);
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- MENU ANIMATORE ---");
            if (animatore != null) {
                System.out.println("Animatore: " + animatore.getNome() + " " + animatore.getCognome());
            }
            System.out.println("1. Gestisci disponibilità");
            System.out.println("2. Visualizza offerte di lavoro");
            System.out.println("3. Visualizza/aggiorna status animatore");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");
            String scelta = scan.nextLine();

            switch (scelta) {
                case "1":
                    showDisponibilitaSubMenu();
                    break;
                case "2":
                    gestisciOfferteLavoro();
                    break;
                case "3":
                    gestisciStatusAnimatore();
                    break;
                case "0":
                    System.out.println("Uscita dal menu animatore...");
                    return;
                default:
                    System.out.println("Scelta non valida.");
                    break;
            }
        }
    }

    private void showDisponibilitaSubMenu() {
        while (true) {
            System.out.println("\n--- SOTTO MENU DISPONIBILITÀ ---");
            System.out.println("1. Visualizza disponibilità");
            System.out.println("2. Inserisci nuova disponibilità");
            System.out.println("3. Modifica disponibilità");
            System.out.println("4. Elimina disponibilità");
            System.out.println("0. Indietro");
            System.out.print("Scelta: ");
            String scelta = scan.nextLine();

            switch (scelta) {
                case "1":
                    visualizzaDisponibilita();
                    break;
                case "2":
                    inserisciDisponibilita();
                    break;
                case "3":
                    modificaDisponibilita();
                    break;
                case "4":
                    eliminaDisponibilita();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Scelta non valida.");
                    break;
            }
        }
    }

    private void visualizzaDisponibilita() {
        try {
            List<DisponibilitaAnimatoreBean> lista = disponibilitaController.visualizzaDisponibilita();
            if (lista.isEmpty()) {
                System.out.println("Nessuna disponibilità presente.");
            } else {
                for (int i = 0; i < lista.size(); i++) {
                    DisponibilitaAnimatoreBean bean = lista.get(i);
                    System.out.printf("%d - %s %s %s-%s [id:%d]\n",
                            i + 1,
                            bean.getData(),
                            bean.isTuttoIlGiorno() ? "(Tutto il giorno)" : "",
                            bean.getOrarioInizio() != null ? bean.getOrarioInizio() : "",
                            bean.getOrarioFine() != null ? bean.getOrarioFine() : "",
                            bean.getId()
                    );
                }
            }
        } catch (DaoException e) {
            System.out.println("Errore visualizzazione disponibilità: " + e.getMessage());
        }
    }

    private void inserisciDisponibilita() {
        try {
            System.out.print("Data (YYYY-MM-DD): ");
            LocalDate giorno = LocalDate.parse(scan.nextLine());
            System.out.print("Disponibile tutto il giorno? [s/n]: ");
            boolean tuttoIlGiorno = scan.nextLine().trim().equalsIgnoreCase("s");
            LocalTime inizio = null, fine = null;
            if (!tuttoIlGiorno) {
                System.out.print("Ora inizio (HH:mm): ");
                inizio = LocalTime.parse(scan.nextLine());
                System.out.print("Ora fine (HH:mm): ");
                fine = LocalTime.parse(scan.nextLine());
            }
            boolean res = disponibilitaController.inserisciDisponibilita(giorno, inizio, fine, tuttoIlGiorno);
            System.out.println(res ? "Disponibilità inserita!" : "Errore nell'inserimento.");
        } catch (Exception e) {
            System.out.println("Dati non validi: " + e.getMessage());
        }
    }

    private void modificaDisponibilita() {
        try {
            List<DisponibilitaAnimatoreBean> lista = disponibilitaController.visualizzaDisponibilita();
            if (lista.isEmpty()) {
                System.out.println("Nessuna disponibilità da modificare.");
                return;
            }
            visualizzaDisponibilita();
            System.out.print("Seleziona disponibilità da modificare (numero): ");
            int idx = Integer.parseInt(scan.nextLine()) - 1;
            if (idx < 0 || idx >= lista.size()) throw new IndexOutOfBoundsException();
            DisponibilitaAnimatoreBean bean = lista.get(idx);

            System.out.print("Nuova data (YYYY-MM-DD): ");
            LocalDate giorno = LocalDate.parse(scan.nextLine());
            System.out.print("Disponibile tutto il giorno? [s/n]: ");
            boolean tuttoIlGiorno = scan.nextLine().trim().equalsIgnoreCase("s");
            LocalTime inizio = null, fine = null;
            if (!tuttoIlGiorno) {
                System.out.print("Nuova ora inizio (HH:mm): ");
                inizio = LocalTime.parse(scan.nextLine());
                System.out.print("Nuova ora fine (HH:mm): ");
                fine = LocalTime.parse(scan.nextLine());
            }

            boolean res = disponibilitaController.modificaDisponibilita(bean, giorno, inizio, fine, tuttoIlGiorno);
            System.out.println(res ? "Modifica riuscita!" : "Errore: verifica sovrapposizioni e dati.");
        } catch (Exception e) {
            System.out.println("Errore nella selezione/modifica: " + e.getMessage());
        }
    }

    private void eliminaDisponibilita() {
        try {
            List<DisponibilitaAnimatoreBean> lista = disponibilitaController.visualizzaDisponibilita();
            if (lista.isEmpty()) {
                System.out.println("Nessuna disponibilità da eliminare.");
                return;
            }
            visualizzaDisponibilita();
            System.out.print("Seleziona disponibilità da eliminare (numero): ");
            int idx = Integer.parseInt(scan.nextLine()) - 1;
            if (idx < 0 || idx >= lista.size()) throw new IndexOutOfBoundsException();
            DisponibilitaAnimatoreBean bean = lista.get(idx);

            boolean res = disponibilitaController.eliminaDisponibilita(bean);
            System.out.println(res ? "Eliminata correttamente!" : "Errore eliminazione.");
        } catch (Exception e) {
            System.out.println("Errore selezione/eliminazione: " + e.getMessage());
        }
    }

    private void gestisciOfferteLavoro() {
        try {
            List<OffertaLavoroBean> offerte = animatoreOffertaController.caricaOfferte(animatore.getAziendaId(), animatore.getId());
            System.out.println("\n--- OFFERTE LAVORO RICEVUTE ---");
            if (offerte.isEmpty()) {
                System.out.println("Nessuna offerta ricevuta.");
                return;
            }
            for (OffertaLavoroBean o : offerte) {
                System.out.println("ID Offerta: " + o.getId() +
                        " | Data evento: " + o.getDataEvento() +
                        " | Fascia: " + o.getOrarioInizio() + "-" + o.getOrarioFine() +
                        " | Descrizione: " + o.getDescrizione() +
                        " | Stato: " + o.getStato());
            }
            System.out.print("\nVuoi aggiornare lo stato di una offerta? (s/n): ");
            if (scan.nextLine().trim().equalsIgnoreCase("s")) {
                System.out.print("ID offerta da aggiornare: ");
                int id = Integer.parseInt(scan.nextLine());
                OffertaLavoroBean selezionata = offerte.stream().filter(o -> o.getId() == id).findFirst().orElse(null);
                if (selezionata == null) {
                    System.out.println("ID offerta non valido.");
                    return;
                }
                System.out.print("Nuovo stato (accettato/rifiutato): ");
                String nuovoStato = scan.nextLine().trim().toLowerCase();
                if ("accettato".equals(nuovoStato) || "rifiutato".equals(nuovoStato)) {
                    boolean ok = animatoreOffertaController.aggiornaStato(id, nuovoStato, animatore.getAziendaId(), animatore.getId());
                    if (ok) {
                        System.out.println("Stato offerta aggiornato!");
                    } else {
                        System.out.println("Errore nell'aggiornamento stato.");
                    }
                } else {
                    System.out.println("Stato non valido!");
                }
            }
        } catch (DaoException | RuntimeException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private void gestisciStatusAnimatore() {
        try {
            StatusAnimatoreBean currStatus = animatoreStatusController.caricaStatus(animatore.getAziendaId(), animatore.getId());
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
            String modelloAuto = scan.nextLine();

            System.out.print("Dimensione auto (piccola/media/grande/furgone): ");
            String dimensioneAuto = scan.nextLine();

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
            String input = scan.nextLine();
            String[] scelte = input.split(",");
            StringBuilder ruoliSelezionati = new StringBuilder();
            boolean haccp = false;
            for (String s : scelte) {
                try {
                    int idx = Integer.parseInt(s.trim()) - 1;
                    if (idx >= 0 && idx < elencoRuoli.length) {
                        String ruolo = elencoRuoli[idx];
                        if (ruoliSelezionati.length() > 0) ruoliSelezionati.append(",");
                        ruoliSelezionati.append(ruolo);
                        if ("Operatore carretti".equals(ruolo)) {
                            System.out.print("Hai la certificazione HACCP? (s/n): ");
                            String val = scan.nextLine().trim().toLowerCase();
                            haccp = val.equals("s");
                        }
                    }
                } catch (NumberFormatException ignored) {}
            }
            System.out.print("Stato (Disponibile/Non operativo): ");
            String stato = scan.nextLine();

            StatusAnimatoreBean s = new StatusAnimatoreBean();
            s.setAnimatoreId(animatore.getId());
            s.setAziendaId(animatore.getAziendaId());
            s.setModelloAuto(modelloAuto);
            s.setDimensioneAuto(dimensioneAuto);
            s.setLavoriAccettati(ruoliSelezionati.toString());
            s.setStato(stato);
            s.setHaccp(haccp);

            boolean ok = animatoreStatusController.salvaOAggiornaStatus(s);
            if (ok) {
                System.out.println("Profilo aggiornato!");
            } else {
                System.out.println("Errore nell'aggiornamento profilo.");
            }
        } catch (DaoException | RuntimeException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }
}