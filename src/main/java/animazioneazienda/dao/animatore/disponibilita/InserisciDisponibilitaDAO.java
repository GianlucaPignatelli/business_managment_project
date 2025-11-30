package animazioneazienda.dao.animatore.disponibilita;

import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class InserisciDisponibilitaDAO {
    private final Connection conn;
    public InserisciDisponibilitaDAO(Connection conn) { this.conn = conn; }

    public boolean inserisci(DisponibilitaAnimatoreBean bean) {
        String sql = "INSERT INTO disponibilita_animatore (azienda_id, animatore_id, data, tutto_il_giorno, orario_inizio, orario_fine) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bean.getAziendaId());
            ps.setInt(2, bean.getAnimatoreId());
            ps.setDate(3, java.sql.Date.valueOf(bean.getData()));
            ps.setBoolean(4, bean.isTuttoIlGiorno());
            ps.setTime(5, bean.getOrarioInizio() != null ? java.sql.Time.valueOf(bean.getOrarioInizio()) : null);
            ps.setTime(6, bean.getOrarioFine() != null ? java.sql.Time.valueOf(bean.getOrarioFine()) : null);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}