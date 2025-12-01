package animazioneazienda.dao.animatore.disponibilita;

import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.exception.DaoException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DisponibilitaAnimatoreCompositeRepository implements DisponibilitaAnimatoreRepository {
    private final List<DisponibilitaAnimatoreRepository> repositories;

    public DisponibilitaAnimatoreCompositeRepository(List<DisponibilitaAnimatoreRepository> repositories) {
        this.repositories = repositories;
    }

    @Override
    public List<DisponibilitaAnimatoreBean> findByAnimatore(int aziendaId, int animatoreId) throws DaoException {
        for (DisponibilitaAnimatoreRepository repo : repositories) {
            List<DisponibilitaAnimatoreBean> result = repo.findByAnimatore(aziendaId, animatoreId);
            if (result != null && !result.isEmpty())
                return result;
        }
        return List.of();
    }

    @Override
    public boolean inserisciDisponibilita(int aziendaId, int animatoreId, LocalDate data, LocalTime inizio, LocalTime fine, boolean tuttoIlGiorno) throws DaoException {
        boolean ok = true;
        for (DisponibilitaAnimatoreRepository repo : repositories) {
            ok = repo.inserisciDisponibilita(aziendaId, animatoreId, data, inizio, fine, tuttoIlGiorno) && ok;
        }
        return ok;
    }

    @Override
    public boolean modificaDisponibilita(DisponibilitaAnimatoreBean bean, LocalDate nuovaData, LocalTime nuovoInizio, LocalTime nuovoFine, boolean tuttoIlGiorno) throws DaoException {
        boolean ok = true;
        for (DisponibilitaAnimatoreRepository repo : repositories) {
            ok = repo.modificaDisponibilita(bean, nuovaData, nuovoInizio, nuovoFine, tuttoIlGiorno) && ok;
        }
        return ok;
    }

    @Override
    public boolean eliminaDisponibilita(DisponibilitaAnimatoreBean bean) throws DaoException {
        for (DisponibilitaAnimatoreRepository repo : repositories) {
            boolean ok = repo.eliminaDisponibilita(bean);
            if (ok) return true; // Se almeno uno la elimina, per la view va bene
        }
        return false; // Nessuno l'ha eliminata
    }
}