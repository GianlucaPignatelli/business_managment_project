package animazioneazienda.dao;

import animazioneazienda.bean.Utente;
import java.sql.*;

public class UtenteDAO {
    private final Connection conn;

    public UtenteDAO(Connection conn) {
        this.conn = conn;
    }

    // Inserisce un nuovo utente, ignora aziendaNome per DB
    public boolean insertUtente(Utente utente) throws SQLException {
        String sql = "INSERT INTO utenti (email, password, ruolo, azienda_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, utente.getEmail());
            stmt.setString(2, utente.getPassword());
            stmt.setString(3, utente.getRuolo().name());
            stmt.setInt(4, utente.getAziendaId());
            int eff = stmt.executeUpdate();
            return eff > 0;
        }
    }

    // Conta gli utenti con ruolo SUPERADMIN
    public int contaSuperadmin() throws SQLException {
        String sql = "SELECT COUNT(*) FROM utenti WHERE ruolo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Utente.Ruolo.SUPERADMIN.name());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    // Ricerca utente per email/password, popolando anche il nome azienda tramite JOIN
    public Utente findByEmailAndPassword(String email, String password) throws SQLException {
        String sql = "SELECT u.*, a.nome AS azienda_nome " +
                "FROM utenti u LEFT JOIN aziende a ON u.azienda_id = a.id " +
                "WHERE u.email = ? AND u.password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utente utente = new Utente();
                utente.setId(rs.getInt("id"));
                utente.setEmail(rs.getString("email"));
                utente.setPassword(rs.getString("password"));
                utente.setRuolo(Utente.Ruolo.valueOf(rs.getString("ruolo")));
                utente.setAziendaId(rs.getInt("azienda_id"));
                utente.setAziendaNome(rs.getString("azienda_nome"));
                return utente;
            }
        }
        return null;
    }
}
