package animazioneazienda.dao;

import animazioneazienda.bean.Utente;
import java.sql.*;

public class UtenteDAO {
    private final Connection conn;

    public UtenteDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean insertUtente(Utente utente) throws SQLException {
        String sql = "INSERT INTO utenti (email, password, ruolo, azienda_id, nome, cognome, sesso, data_nascita) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, utente.getEmail());
            stmt.setString(2, utente.getPassword());
            stmt.setString(3, utente.getRuolo().name());
            stmt.setInt(4, utente.getAziendaId()); // Usa 0 o null solo se la colonna lo consente!
            stmt.setString(5, utente.getNome());
            stmt.setString(6, utente.getCognome());
            stmt.setString(7, utente.getSesso());
            stmt.setDate(8, utente.getDataNascita());
            int eff = stmt.executeUpdate();
            return eff > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Ricerca utente per login e popola tutti i campi
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
                utente.setNome(rs.getString("nome"));
                utente.setCognome(rs.getString("cognome"));
                utente.setSesso(rs.getString("sesso"));
                utente.setDataNascita(rs.getDate("data_nascita"));
                return utente;
            }
        }
        return null;
    }

    // Conta quanti superadmin sono presenti a DB
    public int contaSuperadmin() throws SQLException {
        String sql = "SELECT COUNT(*) FROM utenti WHERE ruolo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Utente.Ruolo.SUPERADMIN.name());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}