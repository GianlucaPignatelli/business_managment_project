package animazioneazienda.controller.animatore;

import animazioneazienda.bean.Utente;
import animazioneazienda.bean.animatore.DisponibilitaAnimatore;
import animazioneazienda.dao.animatore.DisponibilitaAnimatoreDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CalendarioDisponibilitaFXController {
    private final DisponibilitaAnimatoreDAO disponibilitaDAO;
    private final Utente utente;

    public CalendarioDisponibilitaFXController(DisponibilitaAnimatoreDAO disponibilitaDAO, Utente utente) {
        this.disponibilitaDAO = disponibilitaDAO;
        this.utente = utente;
    }

    /**
     * Legge tutte le disponibilità persistite dell’utente corrente.
     */
    public List<DisponibilitaAnimatore> caricaDisponibilita() {
        return disponibilitaDAO.findByAnimatore(utente.getAziendaId(), utente.getId());
    }

    /**
     * Business logic di inserimento: validazione data, orari, sovrapposizioni
     */
    public boolean inserisciDisponibilita(LocalDate giorno, LocalTime inizio, LocalTime fine, boolean tuttoIlGiorno) {
        // Validazione data
        if (giorno == null || giorno.isBefore(LocalDate.now()))
            return false;
        // Validazione orario (solo per fascia)
        if (!tuttoIlGiorno && (inizio == null || fine == null || !fine.isAfter(inizio)))
            return false;
        // Controllo sovrapposizione
        if (disponibilitaDAO.esisteDisponibilitaSovrapposta(utente.getAziendaId(), utente.getId(), giorno, inizio, fine, tuttoIlGiorno))
            return false;

        DisponibilitaAnimatore disp = new DisponibilitaAnimatore();
        disp.setAziendaId(utente.getAziendaId());
        disp.setAnimatoreId(utente.getId());
        disp.setData(giorno);
        disp.setTuttoIlGiorno(tuttoIlGiorno);
        disp.setOrarioInizio(inizio);
        disp.setOrarioFine(fine);

        return disponibilitaDAO.insertDisponibilita(disp);
    }

    /**
     * Modifica una disponibilità esistente: con validazione e controllo sovrapposizioni
     */
    public boolean modificaDisponibilita(DisponibilitaAnimatore disp, LocalDate giorno, LocalTime inizio, LocalTime fine, boolean tuttoIlGiorno) {
        // Validazione data
        if (giorno == null || giorno.isBefore(LocalDate.now()))
            return false;
        // Validazione orari
        if (!tuttoIlGiorno && (inizio == null || fine == null || !fine.isAfter(inizio)))
            return false;

        boolean cambiato = !disp.getData().equals(giorno) || disp.isTuttoIlGiorno() != tuttoIlGiorno ||
                (disp.getOrarioInizio() != null && !disp.getOrarioInizio().equals(inizio)) ||
                (disp.getOrarioFine() != null && !disp.getOrarioFine().equals(fine));

        if (cambiato &&
                disponibilitaDAO.esisteDisponibilitaSovrapposta(utente.getAziendaId(), utente.getId(), giorno, inizio, fine, tuttoIlGiorno))
            return false;

        disp.setData(giorno);
        disp.setTuttoIlGiorno(tuttoIlGiorno);
        disp.setOrarioInizio(inizio);
        disp.setOrarioFine(fine);

        return disponibilitaDAO.updateDisponibilita(disp);
    }

    /**
     * Elimina una disponibilità per ID, utente e azienda
     */
    public boolean eliminaDisponibilita(DisponibilitaAnimatore disp) {
        return disponibilitaDAO.removeDisponibilita(disp.getId(), utente.getAziendaId(), utente.getId());
    }

    /**
     * Verifica che l’utente sia effettivamente di ruolo Animatore
     */
    public boolean isAnimatore() {
        return utente != null && Utente.Ruolo.ANIMATORE.equals(utente.getRuolo());
    }
}