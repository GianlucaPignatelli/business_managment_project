package animazioneazienda.controller;

import animazioneazienda.bean.UtenteBean;
import animazioneazienda.dao.UtenteDAO;

import java.sql.Date;

public class LoginController {
    private final UtenteDAO utenteDAO;

    public LoginController(UtenteDAO utenteDAO) {
        this.utenteDAO = utenteDAO;
    }

    public UtenteBean doLoginReturnUtente(String email, String password) {
        try {
            return utenteDAO.findByEmailAndPassword(email, password);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean esisteSuperadmin() {
        try {
            return utenteDAO.contaSuperadmin() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // REGISTRA UTENTE: tutti dati, incluso ruolo e aziendaId
    public boolean registraUtente(String nome, String cognome, Date dataNascita, String sesso,
                                  String email, String password, String nomeAzienda, UtenteBean.Ruolo ruolo, int aziendaId) {
        try {
            UtenteBean utente = new UtenteBean(email, password, ruolo, aziendaId, nomeAzienda, nome, cognome, sesso, dataNascita);
            return utenteDAO.insertUtente(utente);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}