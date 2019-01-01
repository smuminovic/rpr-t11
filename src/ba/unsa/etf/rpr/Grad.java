package ba.unsa.etf.rpr;

public class Grad {

    private long id;
    private String naziv;
    private int brojStanovnika;
    private long drzavaId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public long getDrzavaId() {
        return drzavaId;
    }

    public void setDrzavaId(long drzavaId) {
        this.drzavaId = drzavaId;
    }

    public int getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(int brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }
}
