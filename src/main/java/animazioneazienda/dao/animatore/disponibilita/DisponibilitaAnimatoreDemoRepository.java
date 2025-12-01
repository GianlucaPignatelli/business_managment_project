package animazioneazienda.dao.animatore.disponibilita;

import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.exception.DaoException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DisponibilitaAnimatoreDemoRepository implements DisponibilitaAnimatoreRepository {
    private final List<DisponibilitaAnimatoreBean> lista = new ArrayList<>();

    @Override
    public List<DisponibilitaAnimatoreBean> findByAnimatore(int aziendaId, int animatoreId) {
        List<DisponibilitaAnimatoreBean> result = new ArrayList<>();
        for (DisponibilitaAnimatoreBean bean : lista) {
            if (bean.getAziendaId() == aziendaId && bean.getAnimatoreId() == animatoreId)
                result.add(bean);
        }
        return result;
    }

    @Override
    public boolean inserisciDisponibilita(int aziendaId, int animatoreId, LocalDate data, LocalTime inizio, LocalTime fine, boolean tuttoIlGiorno) {
        DisponibilitaAnimatoreBean bean = new DisponibilitaAnimatoreBean();
        bean.setId(lista.size() + 1); // id fittizio
        bean.setAziendaId(aziendaId);
        bean.setAnimatoreId(animatoreId);
        bean.setData(data);
        bean.setOrarioInizio(inizio);
        bean.setOrarioFine(fine);
        bean.setTuttoIlGiorno(tuttoIlGiorno);
        lista.add(bean);
        return true;
    }

    @Override
    public boolean modificaDisponibilita(DisponibilitaAnimatoreBean bean, LocalDate nuovaData, LocalTime nuovoInizio, LocalTime nuovoFine, boolean tuttoIlGiorno) {
        bean.setData(nuovaData);
        bean.setOrarioInizio(nuovoInizio);
        bean.setOrarioFine(nuovoFine);
        bean.setTuttoIlGiorno(tuttoIlGiorno);
        return true;
    }

    @Override
    public boolean eliminaDisponibilita(DisponibilitaAnimatoreBean bean) {
        return lista.remove(bean);
    }
}