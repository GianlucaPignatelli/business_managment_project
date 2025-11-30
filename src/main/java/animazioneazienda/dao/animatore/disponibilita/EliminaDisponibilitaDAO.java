package animazioneazienda.dao.animatore.disponibilita;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EliminaDisponibilitaDAO {
    private final Connection conn;
    public EliminaDisponibilitaDAO(Connection conn) { this.conn = conn; }

    public boolean elimina(int disponibilitaId, int aziendaId, int animatoreId) {
        String sql = "DELETE FROM disponibilita_animatore WHERE id = ? AND azienda_id = ? AND animatore_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, disponibilitaId);
            ps.setInt(2, aziendaId);
            ps.setInt(3, animatoreId);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}