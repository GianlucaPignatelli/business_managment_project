package animazioneazienda.dao.animatore.offerta;

import animazioneazienda.bean.animatore.OffertaLavoroBean;
import animazioneazienda.exception.DaoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.util.*;

public class OffertaLavoroJsonRepository implements OffertaLavoroRepository {
    private final File jsonFile;
    private final ObjectMapper mapper = new ObjectMapper();
    private Map<Integer, OffertaLavoroBean> data = new HashMap<>();

    public OffertaLavoroJsonRepository(File jsonFile) throws DaoException {
        this.jsonFile = jsonFile;
        try {
            if (jsonFile.exists()) {
                List<OffertaLavoroBean> list = mapper.readValue(jsonFile, new TypeReference<List<OffertaLavoroBean>>() {});
                for (OffertaLavoroBean o : list) {
                    data.put(o.getId(), o);
                }
            }
        } catch (Exception e) {
            throw new DaoException("Errore JSON nell'inizializzazione", e);
        }
    }

    @Override
    public List<OffertaLavoroBean> findByAnimatore(int aziendaId, int animatoreId) {
        List<OffertaLavoroBean> result = new ArrayList<>();
        for (OffertaLavoroBean o : data.values()) {
            if (o.getAziendaId() == aziendaId && o.getAnimatoreId() == animatoreId)
                result.add(o);
        }
        return result;
    }

    @Override
    public boolean aggiornaStato(int offertaId, String nuovoStato, int aziendaId, int animatoreId) throws DaoException {
        OffertaLavoroBean o = data.get(offertaId);
        if (o != null && o.getAziendaId() == aziendaId && o.getAnimatoreId() == animatoreId) {
            o.setStato(nuovoStato);
            try {
                mapper.writeValue(jsonFile, new ArrayList<>(data.values()));
            } catch (Exception e) {
                throw new DaoException("Errore salvataggio JSON", e);
            }
            return true;
        }
        return false;
    }
}