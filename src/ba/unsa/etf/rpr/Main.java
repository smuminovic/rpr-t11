package ba.unsa.etf.rpr;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static String ispisiGradove() {
        String rez = "";
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO dao = GeografijaDAO.getInstance();
        ArrayList<Grad> gradovi = dao.gradovi();
        for (Grad g:gradovi) {
            rez += g.getNaziv() + " (" + g.getDrzava().getNaziv() + ") - " + g.getBrojStanovnika() + "\n";
        }
        return rez;
    }
    public static void main(String[] args) {
    }
}
