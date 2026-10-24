package model;

public class Perdorues {
    private int id;
    private String username;
    private String password;
    private String roli; // p.sh. "admin" ose "mjek"

    // Konstruktori i plotë
    public Perdorues(int id, String username, String password, String roli) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roli = roli;
    }

    // Konstruktori pa ID (p.sh. kur krijohet para insert në DB)
    public Perdorues(String username, String password, String roli) {
        this.username = username;
        this.password = password;
        this.roli = roli;
    }

    // Getters dhe Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoli() {
        return roli;
    }

    public void setRoli(String roli) {
        this.roli = roli;
    }

    @Override
    public String toString() {
        return "Perdorues{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", roli='" + roli + '\'' +
                '}';
    }
}
