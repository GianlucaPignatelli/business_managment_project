package animazioneazienda.dao.animatore.disponibilita;

import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class VisualizzaDisponibilitaDAO {
    private final Connection conn;
    public VisualizzaDisponibilitaDAO(Connection conn) { this.conn = conn; }

    public List<DisponibilitaAnimatoreBean> trovaPerAnimatore(int aziendaId, int animatoreId) {
        List<DisponibilitaAnimatoreBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM disponibilita_animatore WHERE azienda_id = ? AND animatore_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, aziendaId);
            ps.setInt(2, animatoreId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DisponibilitaAnimatoreBean bean = new DisponibilitaAnimatoreBean();
                bean.setId(rs.getInt("id"));
                bean.setAziendaId(rs.getInt("azienda_id"));
                bean.setAnimatoreId(rs.getInt("animatore_id"));
                bean.setData(rs.getDate("data").toLocalDate());
                bean.setTuttoIlGiorno(rs.getBoolean("tutto_il_giorno"));
                Time oraInizio = rs.getTime("orario_inizio");
                Time oraFine = rs.getTime("orario_fine");
                bean.setOrarioInizio(oraInizio == null ? null : oraInizio.toLocalTime());
                bean.setOrarioFine(oraFine == null ? null : oraFine.toLocalTime());
                lista.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean esisteSovrapposizione(int aziendaId, int animatoreId, LocalDate data, LocalTime inizio, LocalTime fine, boolean tuttoIlGiorno, Integer excludeId) {
        List<DisponibilitaAnimatoreBean> lista = trovaPerAnimatore(aziendaId, animatoreId);
        for (DisponibilitaAnimatoreBean d : lista) {
            if (excludeId != null && d.getId() == excludeId) continue;
            if (d.getData().equals(data)) {
                if (d.isTuttoIlGiorno() || tuttoIlGiorno)
                    return true;
                if (d.getOrarioInizio() != null && d.getOrarioFine() != null && inizio != null && fine != null) {
                    if (!d.getOrarioFine().isBefore(inizio) && !fine.isBefore(d.getOrarioInizio())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean esisteSovrapposizione(int aziendaId, int animatoreId, LocalDate data, LocalTime inizio, LocalTime fine, boolean tuttoIlGiorno) {
        return esisteSovrapposizione(aziendaId, animatoreId, data, inizio, fine, tuttoIlGiorno, null);
    }
}