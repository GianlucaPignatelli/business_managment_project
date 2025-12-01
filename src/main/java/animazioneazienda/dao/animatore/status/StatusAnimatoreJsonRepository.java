package animazioneazienda.dao.animatore.status;

import animazioneazienda.bean.animatore.StatusAnimatoreBean;
import animazioneazienda.exception.DaoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class StatusAnimatoreJsonRepository implements StatusAnimatoreRepository {
    private final File jsonFile;
    private final ObjectMapper mapper = new ObjectMapper();
    private Map<String, StatusAnimatoreBean> data = new HashMap<>();

    public StatusAnimatoreJsonRepository(File jsonFile) throws DaoException {
        this.jsonFile = jsonFile;
        try {
            if (jsonFile.exists()) {
                data = mapper.readValue(jsonFile, new TypeReference<Map<String, StatusAnimatoreBean>>() {});
            }
        } catch (Exception e) {
            throw new DaoException("Errore JSON nell'inizializzazione", e);
        }
    }

    @Override
    public StatusAnimatoreBean findByAnimatore(int aziendaId, int animatoreId) throws DaoException {
        String key = aziendaId + "_" + animatoreId;
        return data.get(key);
    }

    @Override
    public boolean insertOrUpdate(StatusAnimatoreBean s) throws DaoException {
        String key = s.getAziendaId() + "_" + s.getAnimatoreId();
        data.put(key, s);
        try {
            mapper.writeValue(jsonFile, data);
            return true;
        } catch (Exception e) {
            throw new DaoException("Errore salvataggio JSON", e);
        }
    }
}