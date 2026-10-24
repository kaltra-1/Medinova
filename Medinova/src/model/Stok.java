package model;

import java.time.LocalDate;

public class Stok {
    private int id;
    private String emerArtikulli;
    private String njesia; // p.sh. "Copë", "Kuti", "Ml"
    private int sasiaAktuale;
    private int sasiaMinimale; // Nivel alarmi
    private String vendndodhja;
    private LocalDate dataSkadences;

    // Konstruktor i plotë
    public Stok(int id, String emerArtikulli, String njesia, int sasiaAktuale, int sasiaMinimale, String vendndodhja, LocalDate dataSkadences) {
        this.id = id;
        this.emerArtikulli = emerArtikulli;
        this.njesia = njesia;
        this.sasiaAktuale = sasiaAktuale;
        this.sasiaMinimale = sasiaMinimale;
        this.vendndodhja = vendndodhja;
        this.dataSkadences = dataSkadences;
    }

    // Konstruktor bosh
    public Stok() {
    }

    // Getters dhe Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmerArtikulli() { return emerArtikulli; }
    public void setEmerArtikulli(String emerArtikulli) { this.emerArtikulli = emerArtikulli; }

    public String getNjesia() { return njesia; }
    public void setNjesia(String njesia) { this.njesia = njesia; }

    public int getSasiaAktuale() { return sasiaAktuale; }
    public void setSasiaAktuale(int sasiaAktuale) { this.sasiaAktuale = sasiaAktuale; }

    public int getSasiaMinimale() { return sasiaMinimale; }
    public void setSasiaMinimale(int sasiaMinimale) { this.sasiaMinimale = sasiaMinimale; }

    public String getVendndodhja() { return vendndodhja; }
    public void setVendndodhja(String vendndodhja) { this.vendndodhja = vendndodhja; }

    public LocalDate getDataSkadences() { return dataSkadences; }
    public void setDataSkadences(LocalDate dataSkadences) { this.dataSkadences = dataSkadences; }
    
    /**
     * Metodë ndihmëse për të kontrolluar nëse artikulli është në nivel alarmi
     */
    public boolean eshteNeNivelAlarmi() {
        return this.sasiaAktuale < this.sasiaMinimale;
    }
}