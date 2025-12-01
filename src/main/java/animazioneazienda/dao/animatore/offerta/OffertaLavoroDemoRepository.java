package animazioneazienda.dao.animatore.offerta;

import animazioneazienda.bean.animatore.OffertaLavoroBean;
import animazioneazienda.exception.DaoException;
import java.util.*;
/*
 * decommentare righe system.out per vedere demo
 */

public class OffertaLavoroDemoRepository implements OffertaLavoroRepository {
    private final List<OffertaLavoroBean> mockData = new ArrayList<>();

    public OffertaLavoroDemoRepository() {
        //System.out.println(">>> [Demo] OffertaLavoroDemoRepository istanziato");
        // Popolamento eventuale
    }

    @Override
    public List<OffertaLavoroBean> findByAnimatore(int aziendaId, int animatoreId) throws DaoException {
        //System.out.println(">>> [Demo] Chiamata findByAnimatore su OffertaLavoroDemoRepository (animatoreId=" + animatoreId + ")");
        List<OffertaLavoroBean> result = new ArrayList<>();
        for (OffertaLavoroBean offerta : mockData) {
            if (offerta.getAziendaId() == aziendaId && offerta.getAnimatoreId() == animatoreId)
                result.add(offerta);
        }
        return result;
    }

    @Override
    public boolean aggiornaStato(int offertaId, String nuovoStato, int aziendaId, int animatoreId) throws DaoException {
        //System.out.println(">>> [Demo] aggiornaStato chiamata su OffertaLavoroDemoRepository (offertaId=" + offertaId + ")");
        for (OffertaLavoroBean offerta : mockData) {
            if (offerta.getId() == offertaId && offerta.getAziendaId() == aziendaId && offerta.getAnimatoreId() == animatoreId) {
                offerta.setStato(nuovoStato);
                return true;
            }
        }
        return true; // sempre true per demo
    }
}