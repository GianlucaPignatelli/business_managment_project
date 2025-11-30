package animazioneazienda.dao.animatore;

import animazioneazienda.bean.animatore.OffertaLavoroBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OffertaLavoroDAO {
    private final Connection conn;
    public OffertaLavoroDAO(Connection conn) {
        this.conn = conn;
    }
    public List<OffertaLavoroBean> findByAnimatore(int aziendaId, int animatoreId) throws SQLException {
        List<OffertaLavoroBean> lista = new ArrayList<>();
        String query = "SELECT * FROM offerta_lavoro WHERE azienda_id=? AND animatore_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, aziendaId);
            stmt.setInt(2, animatoreId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OffertaLavoroBean o = new OffertaLavoroBean();
                o.setId(rs.getInt("id"));
                o.setAziendaId(rs.getInt("azienda_id"));
                o.setAnimatoreId(rs.getInt("animatore_id"));
                o.setDataEvento(rs.getDate("data_evento").toLocalDate());
                o.setOrarioInizio(rs.getTime("orario_inizio").toLocalTime());
                o.setOrarioFine(rs.getTime("orario_fine").toLocalTime());
                o.setDescrizione(rs.getString("descrizione"));
                o.setStato(rs.getString("stato"));
                lista.add(o);
            }
        }
        return lista;
    }

    public boolean aggiornaStato(int offertaId, String nuovoStato, int aziendaId, int animatoreId) throws SQLException {
        String query = "UPDATE offerta_lavoro SET stato=? WHERE id=? AND azienda_id=? AND animatore_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nuovoStato);
            stmt.setInt(2, offertaId);
            stmt.setInt(3, aziendaId);
            stmt.setInt(4, animatoreId);
            return stmt.executeUpdate() > 0;
        }
    }
}