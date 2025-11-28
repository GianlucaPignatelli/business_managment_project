package animazioneazienda.view.console.animatore.disponibilita;

import animazioneazienda.bean.Utente;
import animazioneazienda.dao.animatore.DisponibilitaAnimatoreDAO;

import java.util.Scanner;

public class GestioneDisponibilitaView {
    private final VisualizzaDisponibilitaView visualizzaView;
    private final InserisciDisponibilitaView inserisciView;
    private final ModificaDisponibilitaView modificaView;
    private final EliminaDisponibilitaView eliminaView;
    private final Scanner scanner = new Scanner(System.in);

    public GestioneDisponibilitaView(DisponibilitaAnimatoreDAO disponibilitaDAO, Utente animatore) {
        this.visualizzaView = new VisualizzaDisponibilitaView(disponibilitaDAO, animatore);
        this.inserisciView = new InserisciDisponibilitaView(disponibilitaDAO, animatore);
        this.modificaView = new ModificaDisponibilitaView(disponibilitaDAO, animatore);
        this.eliminaView = new EliminaDisponibilitaView(disponibilitaDAO, animatore);
    }

    public void showSottoMenu() {
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
                    visualizzaView.visualizzaDisponibilita();
                    break;
                case "2":
                    inserisciView.aggiungiDisponibilita();
                    break;
                case "3":
                    modificaView.modificaDisponibilita();
                    break;
                case "4":
                    eliminaView.rimuoviDisponibilita();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Scelta non valida!");
            }
        }
    }
}