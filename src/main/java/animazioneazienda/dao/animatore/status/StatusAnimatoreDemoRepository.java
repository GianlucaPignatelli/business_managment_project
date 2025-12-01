package animazioneazienda.dao.animatore.status;

import animazioneazienda.bean.animatore.StatusAnimatoreBean;
import animazioneazienda.exception.DaoException;
/*
 * decommentare righe system.out per vedere demo
 */

public class StatusAnimatoreDemoRepository implements StatusAnimatoreRepository {

    public StatusAnimatoreDemoRepository() {
        //System.out.println(">>> [Demo] StatusAnimatoreDemoRepository istanziato");
    }

    @Override
    public StatusAnimatoreBean findByAnimatore(int aziendaId, int animatoreId) throws DaoException {
        //System.out.println(">>> [Demo] Chiamata findByAnimatore su StatusAnimatoreDemoRepository (animatoreId=" + animatoreId + ")");
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
        //System.out.println(">>> [Demo] insertOrUpdate chiamata su StatusAnimatoreDemoRepository (animatoreId=" + s.getAnimatoreId() + ")");
        return true; // non fa nulla, mock!
    }
}