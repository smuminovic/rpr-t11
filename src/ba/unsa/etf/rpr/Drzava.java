package ba.unsa.etf.rpr;

public class Drzava {
    private long id;
    private String naziv;
    private long gradId;
    private Grad glavniGrad;

    public Drzava (String naziv, long gradId) {
        this.naziv = naziv;
        this.gradId = gradId;
    }

    public Drzava() {}

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

    public long getGradId() {
        return gradId;
    }

    public void setGradId(long gradId) {
        this.gradId = gradId;
    }

    public Grad getGlavniGrad() {
        return glavniGrad;
    }

    public void setGlavniGrad(Grad grad) {
        this.glavniGrad = grad;
    }
}
