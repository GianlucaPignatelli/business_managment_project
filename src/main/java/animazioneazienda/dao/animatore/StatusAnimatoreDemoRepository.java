package animazioneazienda.dao.animatore;

import animazioneazienda.bean.animatore.StatusAnimatore;
import animazioneazienda.exception.DaoException;

public class StatusAnimatoreDemoRepository implements StatusAnimatoreRepository {
    @Override
    public StatusAnimatore findByAnimatore(int aziendaId, int animatoreId) throws DaoException {
        StatusAnimatore s = new StatusAnimatore();
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
    public boolean insertOrUpdate(StatusAnimatore s) throws DaoException {
        return true; // non fa nulla, mock!
    }
}