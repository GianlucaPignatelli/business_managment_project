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

    public List<DisponibilitaAnimatore> findByAnimatore(int aziendaId, int animatoreId) {
        String sql = "SELECT * FROM disponibilita_animatore WHERE azienda_id = ? AND animatore_id = ? ORDER BY data, orario_inizio";
        List<DisponibilitaAnimatore> lista = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
                d.setOrarioInizio(rs.getObject("orario_inizio") != null ? rs.getTime("orario_inizio").toLocalTime() : null);
                d.setOrarioFine(rs.getObject("orario_fine") != null ? rs.getTime("orario_fine").toLocalTime() : null);
                lista.add(d);
            }
        } catch (SQLException e) {
            System.out.println("Errore nel caricamento disponibilità: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Controlla se esiste una disponibilità sovrapposta per quella data.
     * - Non permette "tutto il giorno" e fasce orarie insieme nello stesso giorno.
     * - Non permette fasce orarie che si sovrappongono tra loro.
     */
    public boolean esisteDisponibilitaSovrapposta(int aziendaId, int animatoreId, LocalDate data, LocalTime inizio, LocalTime fine, boolean tuttoIlGiorno) {
        List<DisponibilitaAnimatore> lista = findByAnimatore(aziendaId, animatoreId);
        for (DisponibilitaAnimatore d : lista) {
            if (d.getData().equals(data)) {
                if (d.isTuttoIlGiorno() || tuttoIlGiorno)
                    return true; // una delle due è "tutto il giorno"
                if (d.getOrarioInizio() != null && d.getOrarioFine() != null && inizio != null && fine != null) {
                    // Sovrapposizione fasce orarie: A(inizio,fine) vs B(inizio,fine)
                    if (!d.getOrarioFine().isBefore(inizio) && !fine.isBefore(d.getOrarioInizio())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean insertDisponibilita(DisponibilitaAnimatore d) {
        String sql = "INSERT INTO disponibilita_animatore (azienda_id, animatore_id, data, tutto_il_giorno, orario_inizio, orario_fine) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, d.getAziendaId());
            stmt.setInt(2, d.getAnimatoreId());
            stmt.setDate(3, java.sql.Date.valueOf(d.getData()));
            stmt.setBoolean(4, d.isTuttoIlGiorno());
            if (d.getOrarioInizio() != null)
                stmt.setTime(5, Time.valueOf(d.getOrarioInizio()));
            else
                stmt.setNull(5, Types.TIME);
            if (d.getOrarioFine() != null)
                stmt.setTime(6, Time.valueOf(d.getOrarioFine()));
            else
                stmt.setNull(6, Types.TIME);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Errore nell'inserimento disponibilità: " + e.getMessage());
            return false;
        }
    }

    public boolean updateDisponibilita(DisponibilitaAnimatore d) {
        String sql = "UPDATE disponibilita_animatore SET data = ?, tutto_il_giorno = ?, orario_inizio = ?, orario_fine = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(d.getData()));
            stmt.setBoolean(2, d.isTuttoIlGiorno());
            if (d.getOrarioInizio() != null)
                stmt.setTime(3, Time.valueOf(d.getOrarioInizio()));
            else
                stmt.setNull(3, Types.TIME);
            if (d.getOrarioFine() != null)
                stmt.setTime(4, Time.valueOf(d.getOrarioFine()));
            else
                stmt.setNull(4, Types.TIME);
            stmt.setInt(5, d.getId());
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Errore nell'aggiornamento disponibilità: " + e.getMessage());
            return false;
        }
    }

    public boolean removeDisponibilita(int id, int aziendaId, int animatoreId) {
        String sql = "DELETE FROM disponibilita_animatore WHERE id = ? AND azienda_id = ? AND animatore_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, aziendaId);
            stmt.setInt(3, animatoreId);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Errore nella rimozione disponibilità: " + e.getMessage());
            return false;
        }
    }
}