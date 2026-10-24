package controller;

import model.Pacient;
import util.DatabaseConnection;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PacientController {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Shto pacient
    public boolean shtoPacient(Pacient p) {
        String sql = "INSERT INTO pacientet (emer, mbiemer, datelindje, adresa, telefon, grupi_gjakut, mjek_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getEmer());
            pstmt.setString(2, p.getMbiemer());
            pstmt.setString(3, p.getDatelindje().format(DATE_FORMATTER));
            pstmt.setString(4, p.getAdresa());
            pstmt.setString(5, p.getTelefon());
            pstmt.setString(6, p.getGrupiGjakut());
            pstmt.setInt(7, p.getMjekId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gabim gjatë shtimit të pacientit: " + e.getMessage());
            return false;
        }
    }

    // Merr pacient sipas ID
    public Pacient merrPacientById(int id) {
        String sql = "SELECT * FROM pacientet WHERE id = ?";
        Pacient pacient = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    pacient = new Pacient(
                        rs.getInt("id"),
                        rs.getString("emer"),
                        rs.getString("mbiemer"),
                        rs.getDate("datelindje").toLocalDate(),
                        rs.getString("adresa"),
                        rs.getString("telefon"),
                        rs.getString("grupi_gjakut"),
                        rs.getInt("mjek_id")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Gabim gjatë marrjes së pacientit: " + e.getMessage());
        }
        return pacient;
    }

    // Përditëso pacient
    public boolean perditesoPacient(Pacient p) {
        String sql = "UPDATE pacientet SET emer = ?, mbiemer = ?, datelindje = ?, adresa = ?, telefon = ?, grupi_gjakut = ?, mjek_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getEmer());
            pstmt.setString(2, p.getMbiemer());
            pstmt.setString(3, p.getDatelindje().format(DATE_FORMATTER));
            pstmt.setString(4, p.getAdresa());
            pstmt.setString(5, p.getTelefon());
            pstmt.setString(6, p.getGrupiGjakut());
            pstmt.setInt(7, p.getMjekId());
            pstmt.setInt(8, p.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gabim gjatë përditësimit të pacientit: " + e.getMessage());
            return false;
        }
    }

    // Fshi pacient
    public boolean fshiPacient(int id) {
        String sql = "DELETE FROM pacientet WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gabim gjatë fshirjes së pacientit: " + e.getMessage());
            return false;
        }
    }

    // Merr të gjithë pacientët
    public List<Pacient> merrTeGjithePacientet() {
        List<Pacient> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientet";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Pacient p = new Pacient(
                    rs.getInt("id"),
                    rs.getString("emer"),
                    rs.getString("mbiemer"),
                    rs.getDate("datelindje").toLocalDate(),
                    rs.getString("adresa"),
                    rs.getString("telefon"),
                    rs.getString("grupi_gjakut"),
                    rs.getInt("mjek_id")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Merr pacientët e një mjeku të caktuar
    public List<Pacient> merrPacientetSipasMjekId(int mjekId) {
        List<Pacient> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientet WHERE mjek_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, mjekId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Pacient p = new Pacient(
                    rs.getInt("id"),
                    rs.getString("emer"),
                    rs.getString("mbiemer"),
                    rs.getDate("datelindje").toLocalDate(),
                    rs.getString("adresa"),
                    rs.getString("telefon"),
                    rs.getString("grupi_gjakut"),
                    rs.getInt("mjek_id")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public List<Pacient> merrPacientetSipasMjekut(int mjekId) {
        List<Pacient> pacientet = new ArrayList<>();
        String sql = "SELECT DISTINCT p.* FROM pacientet p " +
                     "JOIN takimet t ON p.id = t.pacient_id " +
                     "WHERE t.mjek_id = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, mjekId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Pacient p = new Pacient(
                    rs.getInt("id"),
                    rs.getString("emer"),
                    rs.getString("mbiemer"),
                    rs.getDate("datelindje").toLocalDate(),
                    rs.getString("adresa"),
                    rs.getString("telefon"),
                    rs.getString("grupi_gjakut")
                );
                pacientet.add(p);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return pacientet;
    }

}
