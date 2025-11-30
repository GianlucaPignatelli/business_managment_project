package animazioneazienda.view.console.animatore;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.controller.animatore.CalendarioDisponibilitaAnimatoreController;
import animazioneazienda.controller.AnimatoreController;
import animazioneazienda.dao.animatore.OffertaLavoroDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class AnimatoreMenuView {
    private final CalendarioDisponibilitaAnimatoreController calendarioController;
    private final OffertaLavoroDAO offertaLavoroDAO;
    private final AnimatoreController animatoreController;
    private final UtenteBean animatore;
    private final Scanner scan;

    public AnimatoreMenuView(
            CalendarioDisponibilitaAnimatoreController calendarioController,
            OffertaLavoroDAO offertaLavoroDAO,
            AnimatoreController animatoreController,
            UtenteBean animatore
    ) {
        this.calendarioController = calendarioController;
        this.offertaLavoroDAO = offertaLavoroDAO;
        this.animatoreController = animatoreController;
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
                    GestioneOfferteAnimatoreView offerteView = new GestioneOfferteAnimatoreView(offertaLavoroDAO, animatore);
                    offerteView.gestisciOfferteLavoro();
                    break;
                case "3":
                    GestioneStatusAnimatoreView statusView = new GestioneStatusAnimatoreView(animatore, animatoreController);
                    statusView.gestisciStatusAnimatore();
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
        List<DisponibilitaAnimatoreBean> lista = calendarioController.ottieniDisponibilita();
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
            boolean res = calendarioController.inserisciDisponibilita(giorno, inizio, fine, tuttoIlGiorno);
            System.out.println(res ? "Disponibilità inserita!" : "Errore nell'inserimento.");
        } catch (Exception e) {
            System.out.println("Dati non validi: " + e.getMessage());
        }
    }

    private void modificaDisponibilita() {
        List<DisponibilitaAnimatoreBean> lista = calendarioController.ottieniDisponibilita();
        if (lista.isEmpty()) {
            System.out.println("Nessuna disponibilità da modificare.");
            return;
        }
        visualizzaDisponibilita();
        try {
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

            boolean res = calendarioController.modificaDisponibilita(bean, giorno, inizio, fine, tuttoIlGiorno);
            System.out.println(res ? "Modifica riuscita!" : "Errore: verifica sovrapposizioni e dati.");
        } catch (Exception e) {
            System.out.println("Errore nella selezione/modifica: " + e.getMessage());
        }
    }

    private void eliminaDisponibilita() {
        List<DisponibilitaAnimatoreBean> lista = calendarioController.ottieniDisponibilita();
        if (lista.isEmpty()) {
            System.out.println("Nessuna disponibilità da eliminare.");
            return;
        }
        visualizzaDisponibilita();
        try {
            System.out.print("Seleziona disponibilità da eliminare (numero): ");
            int idx = Integer.parseInt(scan.nextLine()) - 1;
            if (idx < 0 || idx >= lista.size()) throw new IndexOutOfBoundsException();
            DisponibilitaAnimatoreBean bean = lista.get(idx);

            boolean res = calendarioController.eliminaDisponibilita(bean);
            System.out.println(res ? "Eliminata correttamente!" : "Errore eliminazione.");
        } catch (Exception e) {
            System.out.println("Errore selezione/eliminazione: " + e.getMessage());
        }
    }
}