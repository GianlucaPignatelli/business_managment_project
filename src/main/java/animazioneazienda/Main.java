package animazioneazienda;

import animazioneazienda.controller.LoginController;
import animazioneazienda.controller.animatore.AnimatoreDisponibilitaController;
import animazioneazienda.controller.animatore.AnimatoreStatusController;
import animazioneazienda.controller.animatore.AnimatoreOffertaController;
import animazioneazienda.dao.AziendaDAO;
import animazioneazienda.dao.RepositoryFactory;
import animazioneazienda.dao.UtenteDAO;
import animazioneazienda.dao.animatore.status.StatusAnimatoreRepository;
import animazioneazienda.dao.animatore.status.StatusAnimatoreCompositeRepository;
import animazioneazienda.dao.animatore.status.StatusAnimatoreMySQLRepository;
import animazioneazienda.dao.animatore.status.StatusAnimatoreDemoRepository;
import animazioneazienda.dao.animatore.offerta.OffertaLavoroRepository;
import animazioneazienda.dao.animatore.offerta.OffertaLavoroCompositeRepository;
import animazioneazienda.dao.animatore.offerta.OffertaLavoroMySQLRepository;
import animazioneazienda.dao.animatore.offerta.OffertaLavoroDemoRepository;
import animazioneazienda.dao.animatore.disponibilita.DisponibilitaAnimatoreRepository;
import animazioneazienda.dao.animatore.disponibilita.DisponibilitaAnimatoreCompositeRepository;
import animazioneazienda.dao.animatore.disponibilita.DisponibilitaAnimatoreMySQLRepository;
import animazioneazienda.dao.animatore.disponibilita.DisponibilitaAnimatoreDemoRepository;
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
import java.util.Arrays;

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

        // --- DAO PRINCIPALI ---
        AziendaDAO aziendaDAO = new AziendaDAO(conn);
        UtenteDAO utenteDAO = new UtenteDAO(conn);

        // --- Disponibilità: repository composite GoF
        DisponibilitaAnimatoreRepository disponibilitaAnimatoreRepository = new DisponibilitaAnimatoreCompositeRepository(
                Arrays.asList(
                        new DisponibilitaAnimatoreMySQLRepository(conn),
                        new DisponibilitaAnimatoreDemoRepository()
                        // puoi aggiungere qui anche la versione JSON se serve
                ));

        // --- Offerte: repository composite GoF
        OffertaLavoroRepository offertaLavoroRepository = new OffertaLavoroCompositeRepository(Arrays.asList(
                new OffertaLavoroMySQLRepository(conn),
                new OffertaLavoroDemoRepository()
                // puoi aggiungere qui anche la versione JSON se serve
        ));

        // --- Status: repository composite GoF
        StatusAnimatoreRepository statusAnimatoreRepository = new StatusAnimatoreCompositeRepository(Arrays.asList(
                new StatusAnimatoreMySQLRepository(conn),
                new StatusAnimatoreDemoRepository()
                // puoi aggiungere qui anche la versione JSON se serve
        ));

        LoginController loginController = new LoginController(utenteDAO);

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
            EntryPointViewFX.disponibilitaAnimatoreRepository = disponibilitaAnimatoreRepository;
            EntryPointViewFX.offertaLavoroRepository = offertaLavoroRepository;
            EntryPointViewFX.statusAnimatoreRepository = statusAnimatoreRepository;
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

                                AnimatoreDisponibilitaController disponibilitaController =
                                        new AnimatoreDisponibilitaController(
                                                disponibilitaAnimatoreRepository,
                                                utente
                                        );

                                AnimatoreStatusController animatoreStatusController =
                                        new AnimatoreStatusController(statusAnimatoreRepository);

                                AnimatoreOffertaController animatoreOffertaController =
                                        new AnimatoreOffertaController(offertaLavoroRepository);

                                AnimatoreMenuView animMenu = new AnimatoreMenuView(
                                        disponibilitaController,
                                        animatoreOffertaController,
                                        animatoreStatusController,
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