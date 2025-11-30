package animazioneazienda;

import animazioneazienda.controller.LoginController;
import animazioneazienda.controller.AnimatoreController;
import animazioneazienda.controller.animatore.CalendarioDisponibilitaAnimatoreController;

import animazioneazienda.dao.AziendaDAO;
import animazioneazienda.dao.RepositoryFactory;
import animazioneazienda.dao.UtenteDAO;
import animazioneazienda.dao.animatore.StatusAnimatoreRepository;
import animazioneazienda.dao.animatore.OffertaLavoroDAO;
import animazioneazienda.dao.animatore.disponibilita.VisualizzaDisponibilitaDAO;
import animazioneazienda.dao.animatore.disponibilita.InserisciDisponibilitaDAO;
import animazioneazienda.dao.animatore.disponibilita.ModificaDisponibilitaDAO;
import animazioneazienda.dao.animatore.disponibilita.EliminaDisponibilitaDAO;
import animazioneazienda.bean.AziendaBean;
import animazioneazienda.bean.UtenteBean;
import animazioneazienda.exception.DaoException;
import animazioneazienda.view.console.AziendaView;
import animazioneazienda.view.console.LoginView;
import animazioneazienda.view.console.RegistrazioneView;
import animazioneazienda.view.console.SetupWizardView;
import animazioneazienda.view.console.SuperadminConsoleMenu;
import animazioneazienda.view.console.amministratore.AdminMenuConsoleView;
import animazioneazienda.view.console.animatore.AnimatoreMenuView;
import animazioneazienda.view.FX.EntryPointViewFX;

import java.sql.Connection;
import java.sql.DriverManager;
import java.io.File;
import java.util.Scanner;

import static animazioneazienda.view.FX.EntryPointViewFX.statusAnimatoreDAO;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // --- SETUP DB ---
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

        // --- Percorso locale file JSON ---
        File jsonFile = new File("animatori.json");

        // --- ISTANZIA TUTTE LE DAO ---
        AziendaDAO aziendaDAO = new AziendaDAO(conn);
        UtenteDAO utenteDAO = new UtenteDAO(conn);

        VisualizzaDisponibilitaDAO visualizzaDisponibilitaDAO = new VisualizzaDisponibilitaDAO(conn);
        InserisciDisponibilitaDAO inserisciDisponibilitaDAO = new InserisciDisponibilitaDAO(conn);
        ModificaDisponibilitaDAO modificaDisponibilitaDAO = new ModificaDisponibilitaDAO(conn);
        EliminaDisponibilitaDAO eliminaDisponibilitaDAO = new EliminaDisponibilitaDAO(conn);

        OffertaLavoroDAO offertaLavoroDAO = new OffertaLavoroDAO(conn);

        LoginController loginController = new LoginController(utenteDAO);

        // --- REPOSITORY STATUS ANIMATORE (persistenza + composite) ---
        StatusAnimatoreRepository statusAnimatoreRepo;
        try {
            statusAnimatoreRepo = RepositoryFactory.getInstance()
                    .getDoublePersistenceStatusAnimatoreRepository(conn, jsonFile);
        } catch (DaoException de) {
            System.out.println("Errore nell'inizializzare la persistenza animatore: " + de.getMessage());
            try { conn.close(); } catch (Exception ex) {}
            return;
        }

        // --- VIEW PRINCIPALI ---
        AziendaView aziendaView = new AziendaView(aziendaDAO);
        LoginView loginView = new LoginView(loginController);
        RegistrazioneView registrazioneView = new RegistrazioneView(loginController, aziendaDAO);

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

        // --- SCELTA INTERFACCIA ---
        System.out.println("--- Gestionale Animazione ---");
        System.out.println("1 - Interfaccia Grafica (JavaFX)");
        System.out.println("2 - Riga di comando (console)");
        System.out.print("Scelta: ");
        String scelta = scan.nextLine();

        if ("1".equals(scelta)) {
            EntryPointViewFX.aziendaDAO = aziendaDAO;
            EntryPointViewFX.utenteDAO = utenteDAO;
            EntryPointViewFX.loginController = loginController;
            EntryPointViewFX.visualizzaDisponibilitaDAO = visualizzaDisponibilitaDAO;
            EntryPointViewFX.inserisciDisponibilitaDAO = inserisciDisponibilitaDAO;
            EntryPointViewFX.modificaDisponibilitaDAO = modificaDisponibilitaDAO;
            EntryPointViewFX.eliminaDisponibilitaDAO = eliminaDisponibilitaDAO;
            EntryPointViewFX.offertaLavoroDAO = offertaLavoroDAO;
            EntryPointViewFX.launch(EntryPointViewFX.class, args);
            return;
        } else if ("2".equals(scelta)) {
            while (true) {
                System.out.println("\n--- MENU PRINCIPALE ---");
                System.out.println("1. Login");
                System.out.println("2. Registrati");
                System.out.println("0. Esci");
                System.out.print("Scelta: ");
                String sceltaMenu = scan.nextLine();

                switch (sceltaMenu) {
                    case "1": {
                        UtenteBean utente = loginView.doLoginReturnUtente();
                        if (utente != null) {
                            String aziendaNome = "(azienda sconosciuta)";
                            try {
                                if (utente.getAziendaNome() != null && !utente.getAziendaNome().isEmpty()) {
                                    aziendaNome = utente.getAziendaNome();
                                } else {
                                    AziendaBean azienda = aziendaDAO.findById(utente.getAziendaId());
                                    if (azienda != null) aziendaNome = azienda.getNome();
                                }
                            } catch (Exception e) {
                                aziendaNome = "(errore azienda)";
                            }

                            if (utente.getRuolo() == UtenteBean.Ruolo.SUPERADMIN) {
                                System.out.println("Login effettuato! Benvenuto " + utente.getRuolo() + " di " + aziendaNome);
                                SuperadminConsoleMenu.showMenu(utente, aziendaView, scan);
                            } else if (utente.getRuolo() == UtenteBean.Ruolo.AMMINISTRATORE) {
                                System.out.println("Login effettuato! Benvenuto " + utente.getRuolo() + " di " + aziendaNome);
                                AdminMenuConsoleView adminMenu = new AdminMenuConsoleView(aziendaDAO, utente);
                                adminMenu.showMenu();
                            } else if (utente.getRuolo() == UtenteBean.Ruolo.ANIMATORE) {
                                System.out.println("Login effettuato! Benvenuto " + utente.getRuolo() + " di " + aziendaNome);

                                CalendarioDisponibilitaAnimatoreController calendarioController =
                                        new CalendarioDisponibilitaAnimatoreController(
                                                visualizzaDisponibilitaDAO,
                                                inserisciDisponibilitaDAO,
                                                modificaDisponibilitaDAO,
                                                eliminaDisponibilitaDAO,
                                                utente
                                        );

                                AnimatoreController animatoreController =
                                        new AnimatoreController(statusAnimatoreRepo);

                                AnimatoreMenuView animMenu = new AnimatoreMenuView(
                                        calendarioController,
                                        offertaLavoroDAO,
                                        animatoreController,
                                        utente
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