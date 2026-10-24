package model;

import java.time.LocalDateTime;

public class Takim {
    private int id;
    private int pacientId; // Lidhja me Pacientin
    private int mjekId;    // Lidhja me Mjekun
    private LocalDateTime dataOra;
    private String arsyeja;
    private String statusi; // p.sh. "Planifikuar", "Kryer", "Anuluar"

    // Konstruktor i plotë
    public Takim(int id, int pacientId, int mjekId, LocalDateTime dataOra, String arsyeja, String statusi) {
        this.id = id;
        this.pacientId = pacientId;
        this.mjekId = mjekId;
        this.dataOra = dataOra;
        this.arsyeja = arsyeja;
        this.statusi = statusi;
    }

    // Konstruktor për krijimin e ri (pa ID, e cila gjenerohet nga DB)
    public Takim(int pacientId, int mjekId, LocalDateTime dataOra, String arsyeja, String statusi) {
        this.pacientId = pacientId;
        this.mjekId = mjekId;
        this.dataOra = dataOra;
        this.arsyeja = arsyeja;
        this.statusi = statusi;
    }

    // Konstruktor bosh
    public Takim() {
    }

    // Getters dhe Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPacientId() { return pacientId; }
    public void setPacientId(int pacientId) { this.pacientId = pacientId; }

    public int getMjekId() { return mjekId; }
    public void setMjekId(int mjekId) { this.mjekId = mjekId; }

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public String getArsyeja() { return arsyeja; }
    public void setArsyeja(String arsyeja) { this.arsyeja = arsyeja; }

    public String getStatusi() { return statusi; }
    public void setStatusi(String statusi) { this.statusi = statusi; }
}