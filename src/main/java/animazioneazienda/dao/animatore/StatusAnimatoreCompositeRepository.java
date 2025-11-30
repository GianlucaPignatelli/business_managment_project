package animazioneazienda.dao.animatore;

import animazioneazienda.bean.animatore.StatusAnimatoreBean;
import animazioneazienda.exception.DaoException;
import java.util.List;

public class StatusAnimatoreCompositeRepository implements StatusAnimatoreRepository {
    private final List<StatusAnimatoreRepository> repositories;

    public StatusAnimatoreCompositeRepository(List<StatusAnimatoreRepository> repositories) {
        this.repositories = repositories;
    }

    @Override
    public StatusAnimatoreBean findByAnimatore(int aziendaId, int animatoreId) throws DaoException {
        // Recupera da tutti, restituisce il primo valore NON null (priorità MySQL), oppure null se nessuno
        for (StatusAnimatoreRepository repo : repositories) {
            StatusAnimatoreBean result = repo.findByAnimatore(aziendaId, animatoreId);
            if (result != null) return result;
        }
        return null;
    }

    @Override
    public boolean insertOrUpdate(StatusAnimatoreBean s) throws DaoException {
        boolean ok = true;
        for (StatusAnimatoreRepository repo : repositories) {
            ok = repo.insertOrUpdate(s) && ok; // tutte devono riuscire!
        }
        return ok;
    }
}