package animazioneazienda.dao;

import animazioneazienda.bean.Azienda;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AziendaDAO {
    private final Connection conn;

    public AziendaDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Azienda> getAll() throws SQLException {
        List<Azienda> aziende = new ArrayList<>();
        String query = "SELECT * FROM aziende";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Azienda a = new Azienda();
                a.setId(rs.getInt("id"));
                a.setNome(rs.getString("nome"));
                aziende.add(a);
            }
        }
        return aziende;
    }

    public Azienda findById(int id) throws SQLException {
        String query = "SELECT * FROM aziende WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Azienda a = new Azienda();
                a.setId(rs.getInt("id"));
                a.setNome(rs.getString("nome"));
                return a;
            }
        }
        return null;
    }

    public int contaAziende() throws SQLException {
        String query = "SELECT COUNT(*) FROM aziende";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int registraAzienda(String nomeAzienda) throws SQLException {
        String sql = "INSERT INTO aziende (nome) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nomeAzienda);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return -1;
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    return -1;
                }
            }
        }
    }

    public int findIdByNome(String nomeAzienda) throws SQLException {
        String sql = "SELECT id FROM aziende WHERE nome = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeAzienda);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return -1; // Azienda non trovata
    }
}