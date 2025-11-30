package animazioneazienda.dao.animatore.disponibilita;

import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ModificaDisponibilitaDAO {
    private final Connection conn;
    public ModificaDisponibilitaDAO(Connection conn) { this.conn = conn; }

    public boolean modifica(DisponibilitaAnimatoreBean bean) {
        String sql = "UPDATE disponibilita_animatore SET data = ?, tutto_il_giorno = ?, orario_inizio = ?, orario_fine = ? WHERE id = ? AND azienda_id = ? AND animatore_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(bean.getData()));
            ps.setBoolean(2, bean.isTuttoIlGiorno());
            ps.setTime(3, bean.getOrarioInizio() != null ? java.sql.Time.valueOf(bean.getOrarioInizio()) : null);
            ps.setTime(4, bean.getOrarioFine() != null ? java.sql.Time.valueOf(bean.getOrarioFine()) : null);
            ps.setInt(5, bean.getId());
            ps.setInt(6, bean.getAziendaId());
            ps.setInt(7, bean.getAnimatoreId());
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}