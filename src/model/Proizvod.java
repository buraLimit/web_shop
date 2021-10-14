package model;

public class Proizvod {
    private  int id;
    private String naziv, opis;
    private double cena;
    private int kategorija;
    private String src;
    private boolean vidljivost;

    public Proizvod(String naziv, String opis, double cena,String src,int kategorija, boolean vidljivost) {
        this.naziv = naziv;
        this.opis = opis;
        this.cena = cena;
        this.src = src;
        this.kategorija=kategorija;
        this.vidljivost = vidljivost;
    }

    public  int getId() {
        return id;
    }

    public  void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public int getKategorija() {
        return kategorija;
    }

    public void setKategorija(int kategorija) {
        this.kategorija = kategorija;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public boolean isVidljivost() {
        return vidljivost;
    }

    public void setVidljivost(boolean vidljivost) {
        this.vidljivost = vidljivost;
    }

    @Override
    public String toString() {
        return "Proizvod{" +
                "id=" + id +
                ", naziv='" + naziv + '\'' +
                ", opis='" + opis + '\'' +
                ", cena=" + cena +
                ", kategorija='" + kategorija + '\'' +
                '}';
    }
}
