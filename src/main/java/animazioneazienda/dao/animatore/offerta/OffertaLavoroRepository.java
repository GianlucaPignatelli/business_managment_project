package animazioneazienda.dao.animatore.offerta;

import animazioneazienda.bean.animatore.OffertaLavoroBean;
import animazioneazienda.exception.DaoException;
import java.util.List;

public interface OffertaLavoroRepository {
    List<OffertaLavoroBean> findByAnimatore(int aziendaId, int animatoreId) throws DaoException;
    boolean aggiornaStato(int offertaId, String nuovoStato, int aziendaId, int animatoreId) throws DaoException;
}