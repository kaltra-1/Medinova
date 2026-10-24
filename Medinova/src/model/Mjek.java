package model;

import java.time.LocalDate;

public class Mjek {
    private int id;
    private String emer;
    private String mbiemer;
    private String specializimi;
    private String telefoni;
    private String email;
    private LocalDate dataFillimitPune;

    public Mjek(int id, String emer, String mbiemer, String specializimi, String telefoni, 
    		String email, LocalDate dataFillimitPune) {
        this.id = id;
        this.emer = emer;
        this.mbiemer = mbiemer;
        this.specializimi = specializimi;
        this.telefoni = telefoni;
        this.email = email;
        this.dataFillimitPune = dataFillimitPune;
    }

    public Mjek() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmer() { return emer; }
    public void setEmer(String emer) { this.emer = emer; }

    public String getMbiemer() { return mbiemer; }
    public void setMbiemer(String mbiemer) { this.mbiemer = mbiemer; }

    public String getSpecializimi() { return specializimi; }
    public void setSpecializimi(String specializimi) { this.specializimi = specializimi; }

    public String getTelefoni() { return telefoni; }
    public void setTelefoni(String telefoni) { this.telefoni = telefoni; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDataFillimitPune() { return dataFillimitPune; }
    public void setDataFillimitPune(LocalDate dataFillimitPune) { this.dataFillimitPune = dataFillimitPune; }
}