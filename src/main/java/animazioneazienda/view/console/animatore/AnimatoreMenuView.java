package animazioneazienda.view.console.animatore;

import animazioneazienda.bean.Utente;
import animazioneazienda.dao.animatore.DisponibilitaAnimatoreDAO;
import animazioneazienda.dao.animatore.OffertaLavoroDAO;
import animazioneazienda.controller.AnimatoreController;
import animazioneazienda.view.console.animatore.disponibilita.GestioneDisponibilitaView;

import java.util.Scanner;

public class AnimatoreMenuView {
    private final GestioneDisponibilitaView disponibilitaView;
    private final GestioneStatusAnimatoreView statusAnimatoreView;
    private final GestioneOfferteAnimatoreView offerteView;
    private final Scanner scanner = new Scanner(System.in);

    public AnimatoreMenuView(
            DisponibilitaAnimatoreDAO disponibilitaDAO,
            OffertaLavoroDAO offertaDAO,
            Utente animatore,
            AnimatoreController animatoreController
    ) {
        this.disponibilitaView = new GestioneDisponibilitaView(disponibilitaDAO, animatore);
        this.statusAnimatoreView = new GestioneStatusAnimatoreView(animatore, animatoreController);
        this.offerteView = new GestioneOfferteAnimatoreView(offertaDAO, animatore);
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
                    disponibilitaView.showSottoMenu();
                    break;
                case "2":
                    statusAnimatoreView.gestisciStatusAnimatore();
                    break;
                case "3":
                    offerteView.gestisciOfferteLavoro();
                    break;
                case "0":
                    System.out.println("Uscita dal menu Animatore.");
                    return;
                default:
                    System.out.println("Scelta non valida!");
            }
        }
    }
}