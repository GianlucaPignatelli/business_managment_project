package animazioneazienda.controller;

import animazioneazienda.bean.animatore.StatusAnimatoreBean;
import animazioneazienda.exception.DaoException;
import animazioneazienda.dao.animatore.StatusAnimatoreRepository;

public class AnimatoreController {
    private final StatusAnimatoreRepository repo;

    public AnimatoreController(StatusAnimatoreRepository repo) {
        this.repo = repo;
    }

    public StatusAnimatoreBean caricaStatus(int aziendaId, int userId) throws DaoException {
        return repo.findByAnimatore(aziendaId, userId);
    }

    public boolean salvaOAggiornaStatus(StatusAnimatoreBean status) throws DaoException {
        return repo.insertOrUpdate(status);
    }
}