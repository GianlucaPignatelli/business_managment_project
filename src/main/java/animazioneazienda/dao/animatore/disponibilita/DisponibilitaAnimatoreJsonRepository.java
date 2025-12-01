package animazioneazienda.dao.animatore.disponibilita;

import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.exception.DaoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class DisponibilitaAnimatoreJsonRepository implements DisponibilitaAnimatoreRepository {
    private final File jsonFile;
    private final ObjectMapper mapper = new ObjectMapper();
    private Map<Integer, DisponibilitaAnimatoreBean> data = new HashMap<>();

    public DisponibilitaAnimatoreJsonRepository(File jsonFile) throws DaoException {
        this.jsonFile = jsonFile;
        try {
            if (jsonFile.exists()) {
                List<DisponibilitaAnimatoreBean> list = mapper.readValue(jsonFile, new TypeReference<List<DisponibilitaAnimatoreBean>>() {});
                for (DisponibilitaAnimatoreBean b : list)
                    data.put(b.getId(), b);
            }
        } catch (Exception e) {
            throw new DaoException("Errore JSON inizializzazione", e);
        }
    }

    @Override
    public List<DisponibilitaAnimatoreBean> findByAnimatore(int aziendaId, int animatoreId) {
        List<DisponibilitaAnimatoreBean> result = new ArrayList<>();
        for (DisponibilitaAnimatoreBean b : data.values())
            if (b.getAziendaId() == aziendaId && b.getAnimatoreId() == animatoreId)
                result.add(b);
        return result;
    }

    @Override
    public boolean inserisciDisponibilita(int aziendaId, int animatoreId, LocalDate dataValue, LocalTime inizio, LocalTime fine, boolean tuttoIlGiorno) throws DaoException {
        int id = data.size() + 1;
        DisponibilitaAnimatoreBean bean = new DisponibilitaAnimatoreBean();
        bean.setId(id);
        bean.setAziendaId(aziendaId);
        bean.setAnimatoreId(animatoreId);
        bean.setData(dataValue);
        bean.setOrarioInizio(inizio);
        bean.setOrarioFine(fine);
        bean.setTuttoIlGiorno(tuttoIlGiorno);
        data.put(id, bean);
        try {
            mapper.writeValue(jsonFile, new ArrayList<>(data.values()));
        } catch (Exception e) {
            throw new DaoException("Errore salvataggio JSON", e);
        }
        return true;
    }

    @Override
    public boolean modificaDisponibilita(DisponibilitaAnimatoreBean bean, LocalDate nuovaData, LocalTime nuovoInizio, LocalTime nuovoFine, boolean tuttoIlGiorno) throws DaoException {
        bean.setData(nuovaData);
        bean.setOrarioInizio(nuovoInizio);
        bean.setOrarioFine(nuovoFine);
        bean.setTuttoIlGiorno(tuttoIlGiorno);
        data.put(bean.getId(), bean);
        try {
            mapper.writeValue(jsonFile, new ArrayList<>(data.values()));
        } catch (Exception e) {
            throw new DaoException("Errore salvataggio JSON", e);
        }
        return true;
    }

    @Override
    public boolean eliminaDisponibilita(DisponibilitaAnimatoreBean bean) throws DaoException {
        data.remove(bean.getId());
        try {
            mapper.writeValue(jsonFile, new ArrayList<>(data.values()));
        } catch (Exception e) {
            throw new DaoException("Errore salvataggio JSON", e);
        }
        return true;
    }
}