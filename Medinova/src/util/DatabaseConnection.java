package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/medinova_db";
    private static final String USER = "root"; // Emri standard i XAMPP
    private static final String PASSWORD = ""; // Fjalëkalimi standard i XAMPP (bosh)

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver nuk u gjet. Sigurohuni që MySQL Connector/J është shtuar në projekt.");
            throw new SQLException("Probleme me shoferin e bazës së të dhënave.");
        }
    }
}