package animazioneazienda.dao.animatore.status;

import animazioneazienda.bean.animatore.StatusAnimatoreBean;
import animazioneazienda.exception.DaoException;

public class StatusAnimatoreDemoRepository implements StatusAnimatoreRepository {
    @Override
    public StatusAnimatoreBean findByAnimatore(int aziendaId, int animatoreId) throws DaoException {
        StatusAnimatoreBean s = new StatusAnimatoreBean();
        s.setAnimatoreId(animatoreId);
        s.setAziendaId(aziendaId);
        s.setModelloAuto("DemoCar");
        s.setDimensioneAuto("Media");
        s.setLavoriAccettati("Aiutoanimatore,Operatore carretti");
        s.setStato("Disponibile");
        s.setHaccp(true);
        return s; // dati fake
    }

    @Override
    public boolean insertOrUpdate(StatusAnimatoreBean s) throws DaoException {
        return true; // non fa nulla, mock!
    }
}