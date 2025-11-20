package animazioneazienda.dao.animatore;

import animazioneazienda.bean.animatore.StatusAnimatore;
import java.sql.*;

public class StatusAnimatoreDAO {
    private final Connection conn;

    public StatusAnimatoreDAO(Connection conn) {
        this.conn = conn;
    }

    public StatusAnimatore findByAnimatore(int aziendaId, int animatoreId) throws SQLException {
        String query = "SELECT * FROM status_animatore WHERE azienda_id=? AND animatore_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, aziendaId);
            stmt.setInt(2, animatoreId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                StatusAnimatore s = new StatusAnimatore();
                s.setAnimatoreId(rs.getInt("animatore_id"));
                s.setAziendaId(rs.getInt("azienda_id"));
                s.setModelloAuto(rs.getString("modello_auto"));
                s.setDimensioneAuto(rs.getString("dimensione_auto"));
                s.setLavoriAccettati(rs.getString("lavori_accettati"));
                s.setStato(rs.getString("stato"));
                return s;
            } else {
                return null;
            }
        }
    }

    public boolean insertOrUpdate(StatusAnimatore s) throws SQLException {
        // Semplice UPSERT
        String query = "REPLACE INTO status_animatore (animatore_id, azienda_id, modello_auto, dimensione_auto, lavori_accettati, stato) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, s.getAnimatoreId());
            stmt.setInt(2, s.getAziendaId());
            stmt.setString(3, s.getModelloAuto());
            stmt.setString(4, s.getDimensioneAuto());
            stmt.setString(5, s.getLavoriAccettati());
            stmt.setString(6, s.getStato());
            return stmt.executeUpdate() > 0;
        }
    }
}