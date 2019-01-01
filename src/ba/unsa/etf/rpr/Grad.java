package ba.unsa.etf.rpr;

public class Grad {

    private long id;
    private String naziv;
    private int brojStanovnika;
    private long drzavaId;
    private Drzava drzava;

    public Grad(String naziv, int brojStanovnika, long drzavaId) {
        this.naziv = naziv;
        this.brojStanovnika = brojStanovnika;
        this.drzavaId = drzavaId;
    }

    public Grad() {}

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

    public Drzava getDrzava() {
        return drzava;
    }

    public void setDrzava(Drzava drzava) {
        this.drzava = drzava;
    }
}
