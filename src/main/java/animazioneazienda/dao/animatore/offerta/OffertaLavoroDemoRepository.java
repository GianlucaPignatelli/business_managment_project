package animazioneazienda.dao.animatore.offerta;

import animazioneazienda.bean.animatore.OffertaLavoroBean;
import animazioneazienda.exception.DaoException;
import java.util.*;

public class OffertaLavoroDemoRepository implements OffertaLavoroRepository {
    private final List<OffertaLavoroBean> mockData = new ArrayList<>();

    public OffertaLavoroDemoRepository() {
        // Dati fake – puoi popolare qui
        // OffertaLavoroBean o = new OffertaLavoroBean();
        // o.setId(...), etc. mockData.add(o);
    }

    @Override
    public List<OffertaLavoroBean> findByAnimatore(int aziendaId, int animatoreId) throws DaoException {
        List<OffertaLavoroBean> result = new ArrayList<>();
        for (OffertaLavoroBean offerta : mockData) {
            if (offerta.getAziendaId() == aziendaId && offerta.getAnimatoreId() == animatoreId)
                result.add(offerta);
        }
        return result;
    }

    @Override
    public boolean aggiornaStato(int offertaId, String nuovoStato, int aziendaId, int animatoreId) throws DaoException {
        for (OffertaLavoroBean offerta : mockData) {
            if (offerta.getId() == offertaId && offerta.getAziendaId() == aziendaId && offerta.getAnimatoreId() == animatoreId) {
                offerta.setStato(nuovoStato);
                return true;
            }
        }
        // non trovato ma per demo puoi restituire true comunque
        return true;
    }
}