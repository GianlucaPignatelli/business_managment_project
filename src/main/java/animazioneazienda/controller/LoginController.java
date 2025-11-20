package animazioneazienda.controller;

import animazioneazienda.bean.Utente;
import animazioneazienda.dao.UtenteDAO;

public class LoginController {
    private final UtenteDAO utenteDAO;

    public LoginController(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    /**
     * Registra un nuovo utente (Superadmin, Animatore, Amministratore)
     * @return true se la registrazione è OK
     */
    public boolean registraUtente(String email, String password, Utente.Ruolo ruolo, int aziendaId) {
        if (utenteDAO == null) return false;
        if (email == null || password == null || ruolo == null || aziendaId <= 0) return false;
        try {
            Utente nuovo = new Utente();
            nuovo.setEmail(email);
            nuovo.setPassword(password);
            nuovo.setRuolo(ruolo);
            nuovo.setAziendaId(aziendaId);
            return utenteDAO.insertUtente(nuovo);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Login: restituisce l'utente trovato, null se dati invalidi.
     */
    public Utente doLoginReturnUtente(String email, String password) {
        try {
            Utente utente = utenteDAO.findByEmailAndPassword(email, password);
            return utente;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * True se esiste almeno 1 superadmin nel sistema
     */
    public boolean esisteSuperadmin() {
        try {
            return utenteDAO.contaSuperadmin() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}