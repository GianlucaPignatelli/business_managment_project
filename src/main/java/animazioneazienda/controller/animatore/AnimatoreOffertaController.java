package animazioneazienda.controller.animatore;

import animazioneazienda.bean.animatore.OffertaLavoroBean;
import animazioneazienda.dao.animatore.offerta.OffertaLavoroRepository;
import animazioneazienda.exception.DaoException;

import java.util.List;

public class AnimatoreOffertaController {
    private final OffertaLavoroRepository repository;

    public AnimatoreOffertaController(OffertaLavoroRepository repository) {
        this.repository = repository;
    }

    public List<OffertaLavoroBean> caricaOfferte(int aziendaId, int animatoreId) throws DaoException {
        return repository.findByAnimatore(aziendaId, animatoreId);
    }

    public boolean aggiornaStato(int offertaId, String nuovoStato, int aziendaId, int animatoreId) throws DaoException {
        return repository.aggiornaStato(offertaId, nuovoStato, aziendaId, animatoreId);
    }
}