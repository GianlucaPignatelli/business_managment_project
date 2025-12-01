package animazioneazienda.controller.animatore;

import animazioneazienda.bean.animatore.StatusAnimatoreBean;
import animazioneazienda.dao.animatore.status.StatusAnimatoreRepository;
import animazioneazienda.exception.DaoException;

public class AnimatoreStatusController {
    private final StatusAnimatoreRepository repository;

    public AnimatoreStatusController(StatusAnimatoreRepository repository) {
        this.repository = repository;
    }

    public StatusAnimatoreBean caricaStatus(int aziendaId, int animatoreId) throws DaoException {
        return repository.findByAnimatore(aziendaId, animatoreId);
    }

    public boolean salvaOAggiornaStatus(StatusAnimatoreBean s) throws DaoException {
        return repository.insertOrUpdate(s);
    }
}