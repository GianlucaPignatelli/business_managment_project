package animazioneazienda.dao.animatore.status;

import animazioneazienda.bean.animatore.StatusAnimatoreBean;
import animazioneazienda.exception.DaoException;

public interface StatusAnimatoreRepository {
    StatusAnimatoreBean findByAnimatore(int aziendaId, int animatoreId) throws DaoException;
    boolean insertOrUpdate(StatusAnimatoreBean s) throws DaoException;
}