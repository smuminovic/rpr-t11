package ba.unsa.etf.rpr;

import java.util.ArrayList;

public interface DAO {
    // CRUD
    //create
    void dodajGrad(Grad g);
    void dodajDrzavu(Drzava d);
    //read
    Grad glavniGrad(String drzava);
    Drzava nadjiDrzavu(String drzava);
    ArrayList<Grad> gradovi();
    //update
    void izmijeniGrad(Grad g);
    //delete
    void obrisiDrzavu(String drzava);
}
