package controller;

import model.Perdorues;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerdoruesController {

    private final String URL = "jdbc:mysql://localhost:3306/medinova_db";
    private final String USER = "root";
    private final String PASSWORD = "";

    public Perdorues login(String username, String password) {

        String query = "SELECT * FROM perdoruesit WHERE username = ? AND password = ?";
        Perdorues perdorues = null;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                perdorues = new Perdorues(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("roli")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return perdorues;
    }

    // ✅ SHTO PËRDORUES
    public boolean shtoPerdorues(Perdorues p) {
        String query = "INSERT INTO perdoruesit (username, password, roli) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, p.getUsername());
            ps.setString(2, p.getPassword());
            ps.setString(3, p.getRoli());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Perdorues> merrTeGjithePerdoruesit() {
        List<Perdorues> lista = new ArrayList<>();
        String query = "SELECT * FROM perdoruesit ORDER BY username";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Perdorues p = new Perdorues(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("roli")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean perditesoPerdorues(Perdorues p) {
        String sql = "UPDATE perdoruesit SET password=?, roli=? WHERE username=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, p.getPassword());
            pst.setString(2, p.getRoli());
            pst.setString(3, p.getUsername());

            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean fshiPerdorues(String username) {
        String sql = "DELETE FROM perdoruesit WHERE username=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, username);
            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
