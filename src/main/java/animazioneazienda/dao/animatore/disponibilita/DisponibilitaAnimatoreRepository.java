package animazioneazienda.dao.animatore.disponibilita;

import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.exception.DaoException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DisponibilitaAnimatoreRepository {
    List<DisponibilitaAnimatoreBean> findByAnimatore(int aziendaId, int animatoreId) throws DaoException;
    boolean inserisciDisponibilita(int aziendaId, int animatoreId, LocalDate data, LocalTime inizio, LocalTime fine, boolean tuttoIlGiorno) throws DaoException;
    boolean modificaDisponibilita(DisponibilitaAnimatoreBean bean, LocalDate nuovaData, LocalTime nuovoInizio, LocalTime nuovoFine, boolean tuttoIlGiorno) throws DaoException;
    boolean eliminaDisponibilita(DisponibilitaAnimatoreBean bean) throws DaoException;
}