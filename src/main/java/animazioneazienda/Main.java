package animazioneazienda;

import animazioneazienda.controller.AnimatoreController;
import animazioneazienda.controller.LoginController;
import animazioneazienda.dao.*;
import animazioneazienda.dao.animatore.*;
import animazioneazienda.bean.Utente;
import animazioneazienda.bean.Azienda;
import animazioneazienda.exception.DaoException;
import animazioneazienda.view.console.*;
import animazioneazienda.view.console.amministratore.AdminMenuConsoleView;
import animazioneazienda.view.console.animatore.AnimatoreMenuView;
import animazioneazienda.view.FX.EntryPointViewFX;
import animazioneazienda.view.console.animatore.disponibilita.GestioneDisponibilitaView;
import animazioneazienda.view.console.animatore.GestioneStatusAnimatoreView;
import animazioneazienda.view.console.animatore.GestioneOfferteAnimatoreView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // --- SETUP DB E PERSISTENZA JSON ---
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/gestionale_animazione";
            String user = "root";
            String password = "280400";
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println("Errore di connessione a MySQL: " + e.getMessage());
            return;
        }

        // Percorso locale del file JSON
        File jsonFile = new File("animatori.json");

        // --- ISTANZIA TUTTE LE DAO ---
        AziendaDAO aziendaDAO = new AziendaDAO(conn);
        UtenteDAO utenteDAO = new UtenteDAO(conn);
        DisponibilitaAnimatoreDAO disponibilitaAnimatoreDAO = new DisponibilitaAnimatoreDAO(conn);
        StatusAnimatoreDAO statusAnimatoreDAO = new StatusAnimatoreDAO(conn);
        OffertaLavoroDAO offertaLavoroDAO = new OffertaLavoroDAO(conn);
        LoginController loginController = new LoginController(utenteDAO);

        // --- REPOSITORY COMPOSITE: COESISTENZA MYSQL E JSON ---
        StatusAnimatoreRepository statusAnimatoreRepo;
        try {
            statusAnimatoreRepo = RepositoryFactory.getInstance()
                    .getDoublePersistenceStatusAnimatoreRepository(conn, jsonFile); // composite!
        } catch (DaoException de) {
            System.out.println("Errore nell'inizializzare la persistenza animatore: " + de.getMessage());
            try { conn.close(); } catch (Exception ex) {}
            return;
        }

        AnimatoreController animatoreController = new AnimatoreController(statusAnimatoreRepo);

        // --- WIZARD INIZIALE (se serve) ---
        try {
            if (aziendaDAO.contaAziende() == 0 || !loginController.esisteSuperadmin()) {
                SetupWizardView wizard = new SetupWizardView(aziendaDAO, utenteDAO);
                wizard.mostraWizard();
            }
        } catch (Exception e) {
            System.out.println("Errore nel wizard di setup: " + e.getMessage());
            try { conn.close(); } catch (Exception ex) {}
            return;
        }

        // --- SCELTA UTENTE: CONSOLE o GUI ---
        System.out.println("--- Gestionale Animazione ---");
        System.out.println("1 - Interfaccia Grafica (JavaFX)");
        System.out.println("2 - Riga di comando (console)");
        System.out.print("Scelta: ");
        String scelta = scan.nextLine();

        if ("1".equals(scelta)) {
            EntryPointViewFX.aziendaDAO = aziendaDAO;
            EntryPointViewFX.utenteDAO = utenteDAO;
            EntryPointViewFX.loginController = loginController;
            EntryPointViewFX.disponibilitaAnimatoreDAO = disponibilitaAnimatoreDAO;
            EntryPointViewFX.statusAnimatoreDAO = statusAnimatoreDAO;
            EntryPointViewFX.offertaLavoroDAO = offertaLavoroDAO;
            EntryPointViewFX.animatoreController = animatoreController;
            javafx.application.Application.launch(EntryPointViewFX.class);
        } else if ("2".equals(scelta)) {
            AziendaView aziendaView = new AziendaView(aziendaDAO);
            LoginView loginView = new LoginView(loginController);
            RegistrazioneView registrazioneView = new RegistrazioneView(loginController, aziendaDAO);

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
                                        offertaLavoroDAO,
                                        utente,
                                        animatoreController
                                );
                                animMenu.showMenu();
                            }
                        }
                        break;
                    }
                    case "2":
                        registrazioneView.doRegistrazione();
                        break;
                    case "0":
                        System.out.println("Uscita...");
                        try { conn.close(); } catch (Exception ex) {}
                        return;
                    default:
                        System.out.println("Scelta non valida.");
                        break;
                }
            }
        } else {
            System.out.println("Scelta non valida.");
        }

        try { conn.close(); } catch (Exception ex) {}
    }
}