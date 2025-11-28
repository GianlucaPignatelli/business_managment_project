package animazioneazienda.dao.animatore;

import animazioneazienda.bean.animatore.StatusAnimatore;
import animazioneazienda.exception.DaoException;

public interface StatusAnimatoreRepository {
    StatusAnimatore findByAnimatore(int aziendaId, int animatoreId) throws DaoException;
    boolean insertOrUpdate(StatusAnimatore s) throws DaoException;
}