package animazioneazienda;

import animazioneazienda.dao.UtenteDAO;
import animazioneazienda.dao.AziendaDAO;
import animazioneazienda.dao.animatore.DisponibilitaAnimatoreDAO;
import animazioneazienda.dao.animatore.StatusAnimatoreDAO;
import animazioneazienda.dao.animatore.OffertaLavoroDAO;
import animazioneazienda.controller.LoginController;
import animazioneazienda.bean.Utente;
import animazioneazienda.bean.Azienda;
import animazioneazienda.view.console.AziendaView;
import animazioneazienda.view.console.LoginView;
import animazioneazienda.view.console.SetupWizardView;
import animazioneazienda.view.console.SuperadminConsoleMenu;
import animazioneazienda.view.console.amministratore.AdminMenuConsoleView;
import animazioneazienda.view.console.animatore.AnimatoreMenuView;
import animazioneazienda.view.FX.EntryPointViewFX;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://localhost:3306/gestionale_animazione";
            String user = "root";
            String password = "280400";
            Connection conn = DriverManager.getConnection(url, user, password);

            AziendaDAO aziendaDAO = new AziendaDAO(conn);
            UtenteDAO utenteDAO = new UtenteDAO(conn);
            DisponibilitaAnimatoreDAO disponibilitaAnimatoreDAO = new DisponibilitaAnimatoreDAO(conn);
            StatusAnimatoreDAO statusAnimatoreDAO = new StatusAnimatoreDAO(conn);
            OffertaLavoroDAO offertaLavoroDAO = new OffertaLavoroDAO(conn);
            LoginController loginController = new LoginController(utenteDAO);

            if (aziendaDAO.contaAziende() == 0 || !loginController.esisteSuperadmin()) {
                SetupWizardView wizard = new SetupWizardView(aziendaDAO, utenteDAO);
                wizard.mostraWizard();
            }

            System.out.println("--- Gestionale Animazione ---");
            System.out.println("1 - Interfaccia Grafica (JavaFX)");
            System.out.println("2 - Riga di comando (console)");
            System.out.print("Scelta: ");
            Scanner scan = new Scanner(System.in);
            String scelta = scan.nextLine();

            if ("1".equals(scelta)) {
                // INIZIALIZZO CAMPI STATICI DELLA ENTRYPOINT FX
                EntryPointViewFX.aziendaDAO = aziendaDAO;
                EntryPointViewFX.utenteDAO = utenteDAO;
                EntryPointViewFX.loginController = loginController;
                EntryPointViewFX.disponibilitaAnimatoreDAO = disponibilitaAnimatoreDAO;
                EntryPointViewFX.statusAnimatoreDAO = statusAnimatoreDAO;
                EntryPointViewFX.offertaLavoroDAO = offertaLavoroDAO;
                javafx.application.Application.launch(EntryPointViewFX.class);
            } else if ("2".equals(scelta)) {
                AziendaView aziendaView = new AziendaView(aziendaDAO);
                LoginView loginView = new LoginView(loginController, aziendaDAO);

                while (true) {
                    System.out.println("\n--- MENU PRINCIPALE ---");
                    System.out.println("1. Login");
                    System.out.println("2. Registrati");
                    System.out.println("0. Esci");
                    System.out.print("Scelta: ");
                    String sceltaMenu = scan.nextLine();

                    switch (sceltaMenu) {
                        case "1": {
                            Utente utente = loginView.doLoginReturnUtente();
                            if (utente != null) {
                                String aziendaNome = "(azienda sconosciuta)";
                                try {
                                    if (utente.getAziendaNome() != null && !utente.getAziendaNome().isEmpty()) {
                                        aziendaNome = utente.getAziendaNome();
                                    } else {
                                        // Se per qualche motivo nel bean non c'è, prova a prenderlo tramite DAO
                                        Azienda azienda = aziendaDAO.findById(utente.getAziendaId());
                                        if (azienda != null) aziendaNome = azienda.getNome();
                                    }
                                } catch (Exception e) {
                                    aziendaNome = "(errore azienda)";
                                }

                                if (utente.getRuolo() == Utente.Ruolo.SUPERADMIN) {
                                    System.out.println("Login effettuato! Benvenuto " + utente.getRuolo() + " di " + aziendaNome);
                                    SuperadminConsoleMenu.showMenu(utente, aziendaView, scan);
                                } else if (utente.getRuolo() == Utente.Ruolo.AMMINISTRATORE) {
                                    System.out.println("Login effettuato! Benvenuto " + utente.getRuolo() + " di " + aziendaNome);
                                    AdminMenuConsoleView adminMenu = new AdminMenuConsoleView(aziendaDAO, utente);
                                    adminMenu.showMenu();
                                } else if (utente.getRuolo() == Utente.Ruolo.ANIMATORE) {
                                    System.out.println("Login effettuato! Benvenuto " + utente.getRuolo() + " di " + aziendaNome);
                                    AnimatoreMenuView animMenu = new AnimatoreMenuView(
                                            disponibilitaAnimatoreDAO,
                                            statusAnimatoreDAO,
                                            offertaLavoroDAO,
                                            utente
                                    );
                                    animMenu.showMenu();
                                }
                            }
                            break;
                        }
                        case "2":
                            loginView.doRegistrazione();
                            break;
                        case "0":
                            System.out.println("Uscita...");
                            conn.close();
                            return;
                        default:
                            System.out.println("Scelta non valida.");
                            break;
                    }
                }
            } else {
                System.out.println("Scelta non valida.");
            }

            conn.close();
        } catch (Exception e) {
            System.out.println("Errore di connessione al DB: " + e.getMessage());
        }
    }
}