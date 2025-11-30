package animazioneazienda.dao.animatore;

import animazioneazienda.bean.animatore.StatusAnimatoreBean;
import java.sql.*;

public class StatusAnimatoreDAO {
    private final Connection conn;

    public StatusAnimatoreDAO(Connection conn) {
        this.conn = conn;
    }

    public StatusAnimatoreBean findByAnimatore(int aziendaId, int animatoreId) throws SQLException {
        String query = "SELECT * FROM status_animatore WHERE azienda_id=? AND animatore_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, aziendaId);
            stmt.setInt(2, animatoreId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                StatusAnimatoreBean s = new StatusAnimatoreBean();
                s.setAnimatoreId(rs.getInt("animatore_id"));
                s.setAziendaId(rs.getInt("azienda_id"));
                s.setModelloAuto(rs.getString("modello_auto"));
                s.setDimensioneAuto(rs.getString("dimensione_auto"));
                s.setLavoriAccettati(rs.getString("lavori_accettati"));
                s.setStato(rs.getString("stato"));
                s.setHaccp(rs.getBoolean("haccp")); // FIX: recuperi il campo HACCP
                return s;
            } else {
                return null;
            }
        }
    }

    public boolean insertOrUpdate(StatusAnimatoreBean s) throws SQLException {
        // FISSA QUERY: aggiungi campo haccp
        String query = "REPLACE INTO status_animatore (animatore_id, azienda_id, modello_auto, dimensione_auto, lavori_accettati, stato, haccp) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, s.getAnimatoreId());
            stmt.setInt(2, s.getAziendaId());
            stmt.setString(3, s.getModelloAuto());
            stmt.setString(4, s.getDimensioneAuto());
            stmt.setString(5, s.getLavoriAccettati());
            stmt.setString(6, s.getStato());
            stmt.setBoolean(7, s.isHaccp()); // FIX: salva il campo HACCP
            return stmt.executeUpdate() > 0;
        }
    }
}