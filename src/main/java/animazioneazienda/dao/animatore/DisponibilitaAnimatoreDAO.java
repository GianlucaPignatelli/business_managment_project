package animazioneazienda.dao.animatore;

import animazioneazienda.bean.animatore.DisponibilitaAnimatore;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DisponibilitaAnimatoreDAO {
    private final Connection conn;

    public DisponibilitaAnimatoreDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean insertDisponibilita(DisponibilitaAnimatore d) throws SQLException {
        String query = "INSERT INTO disponibilita_animatore (azienda_id, animatore_id, data, tutto_il_giorno, orario_inizio, orario_fine) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, d.getAziendaId());
            stmt.setInt(2, d.getAnimatoreId());
            stmt.setDate(3, Date.valueOf(d.getData()));
            stmt.setBoolean(4, d.isTuttoIlGiorno());
            stmt.setTime(5, d.isTuttoIlGiorno() ? null : Time.valueOf(d.getOrarioInizio()));
            stmt.setTime(6, d.isTuttoIlGiorno() ? null : Time.valueOf(d.getOrarioFine()));
            return stmt.executeUpdate() > 0;
        }
    }

    public List<DisponibilitaAnimatore> findByAnimatore(int aziendaId, int animatoreId) throws SQLException {
        List<DisponibilitaAnimatore> lista = new ArrayList<>();
        String query = "SELECT * FROM disponibilita_animatore WHERE azienda_id=? AND animatore_id=? ORDER BY data";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, aziendaId);
            stmt.setInt(2, animatoreId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DisponibilitaAnimatore d = new DisponibilitaAnimatore();
                d.setId(rs.getInt("id"));
                d.setAziendaId(rs.getInt("azienda_id"));
                d.setAnimatoreId(rs.getInt("animatore_id"));
                d.setData(rs.getDate("data").toLocalDate());
                d.setTuttoIlGiorno(rs.getBoolean("tutto_il_giorno"));
                if (d.isTuttoIlGiorno()) {
                    d.setOrarioInizio(null);
                    d.setOrarioFine(null);
                } else {
                    d.setOrarioInizio(rs.getTime("orario_inizio").toLocalTime());
                    d.setOrarioFine(rs.getTime("orario_fine").toLocalTime());
                }
                lista.add(d);
            }
        }
        return lista;
    }

    public boolean removeDisponibilita(int id, int aziendaId, int animatoreId) throws SQLException {
        String query = "DELETE FROM disponibilita_animatore WHERE id=? AND azienda_id=? AND animatore_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.setInt(2, aziendaId);
            stmt.setInt(3, animatoreId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateDisponibilita(DisponibilitaAnimatore d) throws SQLException {
        String query = "UPDATE disponibilita_animatore SET data=?, tutto_il_giorno=?, orario_inizio=?, orario_fine=? WHERE id=? AND azienda_id=? AND animatore_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(d.getData()));
            stmt.setBoolean(2, d.isTuttoIlGiorno());
            stmt.setTime(3, d.isTuttoIlGiorno() ? null : Time.valueOf(d.getOrarioInizio()));
            stmt.setTime(4, d.isTuttoIlGiorno() ? null : Time.valueOf(d.getOrarioFine()));
            stmt.setInt(5, d.getId());
            stmt.setInt(6, d.getAziendaId());
            stmt.setInt(7, d.getAnimatoreId());
            return stmt.executeUpdate() > 0;
        }
    }
}