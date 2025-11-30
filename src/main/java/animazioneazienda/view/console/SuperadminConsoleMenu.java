package animazioneazienda.view.console;

import animazioneazienda.bean.UtenteBean;
import java.util.Scanner;

public class SuperadminConsoleMenu {
    public static void showMenu(UtenteBean admin, AziendaView aziendaView, Scanner scan) {
        while (true) {
            System.out.println("\n--- MENU SUPERADMIN (" + admin.getEmail() + ") ---");
            System.out.println("1. Registra nuova azienda");
            System.out.println("2. Logout");
            System.out.print("Scelta: ");
            String scelta = scan.nextLine();
            switch (scelta) {
                case "1":
                    aziendaView.registraAzienda();
                    break;
                case "2":
                    return;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
    }
}