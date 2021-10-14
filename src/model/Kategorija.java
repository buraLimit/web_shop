package model;

public class Kategorija {
    private  int id;
    private String naziv;
    private boolean vidljivost;

    public Kategorija(String naziv, boolean vidljivost) {
        this.vidljivost=vidljivost;
        this.naziv = naziv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public boolean isVidljivost() {
        return vidljivost;
    }

    public void setVidljivost(boolean vidljivost) {
        this.vidljivost = vidljivost;
    }
}
