package animazioneazienda.dao.animatore.disponibilita;

import animazioneazienda.bean.animatore.DisponibilitaAnimatoreBean;
import animazioneazienda.exception.DaoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DisponibilitaAnimatoreMySQLRepository implements DisponibilitaAnimatoreRepository {
    private final Connection conn;

    public DisponibilitaAnimatoreMySQLRepository(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<DisponibilitaAnimatoreBean> findByAnimatore(int aziendaId, int animatoreId) throws DaoException {
        List<DisponibilitaAnimatoreBean> lista = new ArrayList<>();
        String query = "SELECT * FROM disponibilita_animatore WHERE azienda_id=? AND animatore_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, aziendaId);
            stmt.setInt(2, animatoreId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DisponibilitaAnimatoreBean bean = new DisponibilitaAnimatoreBean();
                bean.setId(rs.getInt("id"));
                bean.setAziendaId(rs.getInt("azienda_id"));
                bean.setAnimatoreId(rs.getInt("animatore_id"));
                bean.setData(rs.getDate("data").toLocalDate());
                bean.setOrarioInizio(rs.getTime("orario_inizio") != null ? rs.getTime("orario_inizio").toLocalTime() : null);
                bean.setOrarioFine(rs.getTime("orario_fine") != null ? rs.getTime("orario_fine").toLocalTime() : null);
                bean.setTuttoIlGiorno(rs.getBoolean("tutto_il_giorno"));
                lista.add(bean);
            }
        } catch (SQLException e) {
            throw new DaoException("Errore SQL in findByAnimatore", e);
        }
        return lista;
    }

    @Override
    public boolean inserisciDisponibilita(int aziendaId, int animatoreId, LocalDate data, LocalTime inizio, LocalTime fine, boolean tuttoIlGiorno) throws DaoException {
        String query = "INSERT INTO disponibilita_animatore (azienda_id, animatore_id, data, orario_inizio, orario_fine, tutto_il_giorno) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, aziendaId);
            stmt.setInt(2, animatoreId);
            stmt.setDate(3, java.sql.Date.valueOf(data));
            stmt.setTime(4, inizio != null ? java.sql.Time.valueOf(inizio) : null);
            stmt.setTime(5, fine != null ? java.sql.Time.valueOf(fine) : null);
            stmt.setBoolean(6, tuttoIlGiorno);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Errore SQL in inserisciDisponibilita", e);
        }
    }

    @Override
    public boolean modificaDisponibilita(DisponibilitaAnimatoreBean bean, LocalDate nuovaData, LocalTime nuovoInizio, LocalTime nuovoFine, boolean tuttoIlGiorno) throws DaoException {
        String query = "UPDATE disponibilita_animatore SET data=?, orario_inizio=?, orario_fine=?, tutto_il_giorno=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(nuovaData));
            stmt.setTime(2, nuovoInizio != null ? java.sql.Time.valueOf(nuovoInizio) : null);
            stmt.setTime(3, nuovoFine != null ? java.sql.Time.valueOf(nuovoFine) : null);
            stmt.setBoolean(4, tuttoIlGiorno);
            stmt.setInt(5, bean.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Errore SQL in modificaDisponibilita", e);
        }
    }

    @Override
    public boolean eliminaDisponibilita(DisponibilitaAnimatoreBean bean) throws DaoException {
        String query = "DELETE FROM disponibilita_animatore WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bean.getId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0; // OK solo se almeno una riga eliminata
        } catch (SQLException e) {
            throw new DaoException("Errore SQL in eliminaDisponibilita", e);
        }
    }
}