package model;

import java.time.LocalDate;

public class Pacient {
    private int id;
    private String emer;
    private String mbiemer;
    private LocalDate datelindje;
    private String adresa;
    private String telefon;
    private String grupiGjakut;
    private int mjekId;

    // Konstruktor me të gjitha fushat
    public Pacient(int id, String emer, String mbiemer, LocalDate datelindje, String adresa, String telefon, String grupiGjakut, int mjekId) {
        this.id = id;
        this.emer = emer;
        this.mbiemer = mbiemer;
        this.datelindje = datelindje;
        this.adresa = adresa;
        this.telefon = telefon;
        this.grupiGjakut = grupiGjakut;
        this.mjekId = mjekId;
    }

    // Konstruktor pa mjekId (default 0)
    public Pacient(int id, String emer, String mbiemer, LocalDate datelindje, String adresa, String telefon, String grupiGjakut) {
        this(id, emer, mbiemer, datelindje, adresa, telefon, grupiGjakut, 0);
    }

    // Konstruktor bosh
    public Pacient() {}

    // Getters dhe Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmer() { return emer; }
    public void setEmer(String emer) { this.emer = emer; }

    public String getMbiemer() { return mbiemer; }
    public void setMbiemer(String mbiemer) { this.mbiemer = mbiemer; }

    public LocalDate getDatelindje() { return datelindje; }
    public void setDatelindje(LocalDate datelindje) { this.datelindje = datelindje; }

    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getGrupiGjakut() { return grupiGjakut; }
    public void setGrupiGjakut(String grupiGjakut) { this.grupiGjakut = grupiGjakut; }

    public int getMjekId() { return mjekId; }
    public void setMjekId(int mjekId) { this.mjekId = mjekId; }

    @Override
    public String toString() {
        return emer + " " + mbiemer + " | " + grupiGjakut + " | " + telefon;
    }
}
