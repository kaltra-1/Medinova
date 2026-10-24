package controller;

import model.Takim;
import util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TakimController {

    private Takim creaTakimNgaResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int pacientId = rs.getInt("pacient_id");
        int mjekId = rs.getInt("mjek_id");
        
        LocalDateTime dataOra = rs.getTimestamp("data_ora").toLocalDateTime();
        
        String arsyeja = rs.getString("arsyeja");
        String statusi = rs.getString("statusi");
        
        return new Takim(id, pacientId, mjekId, dataOra, arsyeja, statusi);
    }

    // 1. Krijimi / Shtimi i një Takimi
    public boolean shtoTakim(Takim t) {
        String sql = "INSERT INTO takimet (pacient_id, mjek_id, data_ora, arsyeja, statusi) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, t.getPacientId());
            pstmt.setInt(2, t.getMjekId());
            pstmt.setTimestamp(3, Timestamp.valueOf(t.getDataOra())); 
            pstmt.setString(4, t.getArsyeja());
            pstmt.setString(5, t.getStatusi());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Gabim gjatë shtimit të takimit: " + e.getMessage());
            return false;
        }
    }

    // 2. Leximi / Marrja e të gjitha Takimeve
    public List<Takim> merrTeGjithaTakimet() {
        List<Takim> takimet = new ArrayList<>();
        String sql = "SELECT * FROM takimet ORDER BY data_ora DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                takimet.add(creaTakimNgaResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Gabim gjatë marrjes së takimeve: " + e.getMessage());
        }
        return takimet;
    }

    // 3. Përditësimi i një Takimi
    public boolean perditesoTakim(Takim t) {
        String sql = "UPDATE takimet SET pacient_id = ?, mjek_id = ?, data_ora = ?, arsyeja = ?, statusi = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, t.getPacientId());
            pstmt.setInt(2, t.getMjekId());
            pstmt.setTimestamp(3, Timestamp.valueOf(t.getDataOra()));
            pstmt.setString(4, t.getArsyeja());
            pstmt.setString(5, t.getStatusi());
            pstmt.setInt(6, t.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Gabim gjatë përditësimit të takimit: " + e.getMessage());
            return false;
        }
    }

    // 4. Fshirja e një Takimi
    public boolean fshiTakim(int id) {
        String sql = "DELETE FROM takimet WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Gabim gjatë fshirjes së takimit: " + e.getMessage());
            return false;
        }
    }
    
    public List<Takim> merrTakimetNeDate(LocalDate data) {
        List<Takim> lista = new ArrayList<>();
        String sql = "SELECT * FROM takimet WHERE DATE(data_ora) = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(data));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Takim t = new Takim();
                t.setId(rs.getInt("id"));
                t.setPacientId(rs.getInt("pacient_id"));
                t.setMjekId(rs.getInt("mjek_id"));
                t.setDataOra(rs.getTimestamp("data_ora").toLocalDateTime());
                t.setArsyeja(rs.getString("arsyeja"));
                t.setStatusi(rs.getString("statusi"));
                lista.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}