package controller;

import model.Stok;
import util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StokController {
    
    private Stok creoStokNgaResultSet(ResultSet rs) throws SQLException {
        // Metodë ndihmëse për të kthyer një rresht DB në një objekt Stok
        int id = rs.getInt("id");
        String emerArtikulli = rs.getString("emer_artikulli");
        String njesia = rs.getString("njesia");
        int sasiaAktuale = rs.getInt("sasia_aktuale");
        int sasiaMinimale = rs.getInt("sasia_minimale");
        String vendndodhja = rs.getString("vendndodhja");
        LocalDate dataSkadences = rs.getDate("data_skadences").toLocalDate();
        
        return new Stok(id, emerArtikulli, njesia, sasiaAktuale, sasiaMinimale, vendndodhja, dataSkadences);
    }

    // 1. Krijimi / Shtimi i një Artikulli në Stok
    public boolean shtoArtikullStoku(Stok s) {
        String sql = "INSERT INTO stoku (emer_artikulli, njesia, sasia_aktuale, sasia_minimale, vendndodhja, data_skadences) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, s.getEmerArtikulli());
            pstmt.setString(2, s.getNjesia());
            pstmt.setInt(3, s.getSasiaAktuale());
            pstmt.setInt(4, s.getSasiaMinimale());
            pstmt.setString(5, s.getVendndodhja());
            pstmt.setDate(6, Date.valueOf(s.getDataSkadences()));

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Gabim gjatë shtimit të artikullit në stok: " + e.getMessage());
            return false;
        }
    }

    // 2. Leximi / Marrja e Stokut që është nën Nivelin e Alarmit
    public List<Stok> merrStokunNenAlarm() {
        List<Stok> artikujtNeAlarm = new ArrayList<>();
        // Query që zgjedh artikujt ku sasia aktuale është më e vogël se sasia minimale
        String sql = "SELECT * FROM stoku WHERE sasia_aktuale < sasia_minimale";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                artikujtNeAlarm.add(creoStokNgaResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Gabim gjatë marrjes së stokut nën alarm: " + e.getMessage());
        }
        return artikujtNeAlarm;
    }

    // 3. Përditësimi i Sasisë së Stokut
    public boolean perditesoSasineStokut(int artikullId, int sasiaERe) {
        String sql = "UPDATE stoku SET sasia_aktuale = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sasiaERe);
            pstmt.setInt(2, artikullId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Gabim gjatë përditësimit të sasisë: " + e.getMessage());
            return false;
        }
    }
    
 // 4. Përditësimi i të dhënave të një Artikulli
    public boolean perditesoArtikullStoku(Stok s) {
        String sql = "UPDATE stoku SET emer_artikulli = ?, njesia = ?, sasia_aktuale = ?, sasia_minimale = ?, vendndodhja = ?, data_skadences = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, s.getEmerArtikulli());
            pstmt.setString(2, s.getNjesia());
            pstmt.setInt(3, s.getSasiaAktuale());
            pstmt.setInt(4, s.getSasiaMinimale());
            pstmt.setString(5, s.getVendndodhja());
            pstmt.setDate(6, Date.valueOf(s.getDataSkadences()));
            pstmt.setInt(7, s.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Gabim gjatë përditësimit të artikullit: " + e.getMessage());
            return false;
        }
    }

    // 5. Fshirja e një Artikulli
    public boolean fshiArtikullStoku(int id) {
        String sql = "DELETE FROM stoku WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Gabim gjatë fshirjes së artikullit: " + e.getMessage());
            return false;
        }
    }

    public List<Stok> merrTeGjitheStokun() {
        List<Stok> lista = new ArrayList<>();
        String sql = "SELECT * FROM stoku";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
            	Stok s = new Stok(
            		    rs.getInt("id"),
            		    rs.getString("emer_artikulli"),  // <--- ndrysho nga "emer"
            		    rs.getString("njesia"),
            		    rs.getInt("sasia_aktuale"),
            		    rs.getInt("sasia_minimale"),
            		    rs.getString("vendndodhja"),
            		    rs.getDate("data_skadences").toLocalDate()
            		);

                lista.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}