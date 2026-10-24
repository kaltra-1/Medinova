package controller;

import model.Mjek;
import util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MjekController {

    private Mjek creaMjekNgaResultSet(ResultSet rs) throws SQLException {
        
        int id = rs.getInt("id");
        String emer = rs.getString("emer");
        String mbiemer = rs.getString("mbiemer");
        String spec = rs.getString("specializimi");
        String tel = rs.getString("telefoni");
        String email = rs.getString("email");
        LocalDate dataFillimi = rs.getDate("data_fillimit_pune").toLocalDate();
        
        return new Mjek(id, emer, mbiemer, spec, tel, email, dataFillimi);
    }

    // 1. Krijimi / Shtimi i një Mjeku
    public boolean shtoMjek(Mjek m, String username, String password) {
        String sqlPerdorues = "INSERT INTO perdoruesit (username, password, roli) VALUES (?, ?, 'MJEK')";
        String sqlMjek = "INSERT INTO mjeket (emer, mbiemer, specializimi, telefoni, email, data_fillimit_pune, perdorues_id) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); 

            // 1️ Shto përdoruesin
            try (PreparedStatement pstUser = conn.prepareStatement(sqlPerdorues, Statement.RETURN_GENERATED_KEYS)) {
                pstUser.setString(1, username);
                pstUser.setString(2, password);
                pstUser.executeUpdate();

                ResultSet rs = pstUser.getGeneratedKeys();
                if (rs.next()) {
                    int perdoruesId = rs.getInt(1);

                    // 2️ Shto mjekun me perdorues_id
                    try (PreparedStatement pstMjek = conn.prepareStatement(sqlMjek)) {
                        pstMjek.setString(1, m.getEmer());
                        pstMjek.setString(2, m.getMbiemer());
                        pstMjek.setString(3, m.getSpecializimi());
                        pstMjek.setString(4, m.getTelefoni());
                        pstMjek.setString(5, m.getEmail());
                        pstMjek.setDate(6, Date.valueOf(m.getDataFillimitPune()));
                        pstMjek.setInt(7, perdoruesId);

                        pstMjek.executeUpdate();
                    }
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // 2. Leximi / Marrja e të gjithë Mjekëve
    public List<Mjek> merrTeGjitheMjeket() {
        List<Mjek> mjeket = new ArrayList<>();
        String sql = "SELECT * FROM mjeket ORDER BY emer, mbiemer";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                mjeket.add(creaMjekNgaResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Gabim gjatë marrjes së mjekëve: " + e.getMessage());
        }
        return mjeket;
    }
    
    /**
     * 3. Përditësimi i të dhënave të një Mjeku ekzistues.
     */
    public boolean perditesoMjek(Mjek m) {
        String sql = "UPDATE mjeket SET emer = ?, mbiemer = ?, specializimi = ?, telefoni = ?, email = ?, data_fillimit_pune = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, m.getEmer());
            pstmt.setString(2, m.getMbiemer());
            pstmt.setString(3, m.getSpecializimi());
            pstmt.setString(4, m.getTelefoni());
            pstmt.setString(5, m.getEmail());
            pstmt.setDate(6, Date.valueOf(m.getDataFillimitPune()));
            pstmt.setInt(7, m.getId()); 

            int rreshtaTeNdryshuar = pstmt.executeUpdate();
            return rreshtaTeNdryshuar > 0;

        } catch (SQLException e) {
            System.err.println("Gabim gjatë përditësimit të mjekut: " + e.getMessage());
            return false;
        }
    }

    /**
     * 4. Fshirja e një Mjeku bazuar në ID.
     */
    public boolean fshiMjek(int id) {
   
        String sql = "DELETE FROM mjeket WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rreshtaTeNdryshuar = pstmt.executeUpdate();
            return rreshtaTeNdryshuar > 0;

        } catch (SQLException e) {
            System.err.println("Gabim gjatë fshirjes së mjekut: " + e.getMessage());
            return false;
        }
    }
    
    public int merrMjekIdNgaPerdoruesId(int perdoruesId) {
        String query = "SELECT id FROM mjeket WHERE perdorues_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, perdoruesId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; 
    }


    public int getTodayAppointmentsByDoctor(int mjekId) {
        String sql = "SELECT COUNT(*) FROM takimet WHERE mjek_id = ? AND data_ora = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, mjekId);
            pst.setDate(2, Date.valueOf(LocalDate.now()));

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Gabim gjatë numërimit të takimeve të sotme: " + e.getMessage());
        }
        return 0;
    }


}