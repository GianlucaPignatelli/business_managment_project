package animazioneazienda.dao.animatore.offerta;

import animazioneazienda.bean.animatore.OffertaLavoroBean;
import animazioneazienda.exception.DaoException;
import java.util.List;
import java.util.Collections;

public class OffertaLavoroCompositeRepository implements OffertaLavoroRepository {
    private final List<OffertaLavoroRepository> repositories;

    public OffertaLavoroCompositeRepository(List<OffertaLavoroRepository> repositories) {
        this.repositories = repositories;
    }

    @Override
    public List<OffertaLavoroBean> findByAnimatore(int aziendaId, int animatoreId) throws DaoException {
        for (OffertaLavoroRepository repo : repositories) {
            List<OffertaLavoroBean> result = repo.findByAnimatore(aziendaId, animatoreId);
            if (result != null && !result.isEmpty())
                return result;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean aggiornaStato(int offertaId, String nuovoStato, int aziendaId, int animatoreId) throws DaoException {
        boolean ok = true;
        for (OffertaLavoroRepository repo : repositories) {
            ok = repo.aggiornaStato(offertaId, nuovoStato, aziendaId, animatoreId) && ok;
        }
        return ok;
    }
}