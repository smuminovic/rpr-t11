package ba.unsa.etf.rpr;

import java.io.File;
import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;

public class GeografijaDAO implements DAO {
    private static GeografijaDAO instance = new GeografijaDAO();

    public static GeografijaDAO getInstance() {
        if(instance == null) instance = new GeografijaDAO();
        return instance;
    }

    public static void removeInstance() {
        File dbfile = new File("baza.db");
        dbfile.delete();
        instance = null;
    }

    void obrisiTabele() throws SQLException {
        String sql = "DROP TABLE grad";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sql = "DROP TABLE drzava";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection connection;

    private GeografijaDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:baza.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //obrisi tabele
        try {
            obrisiTabele();
        } catch (SQLException e){
            e.printStackTrace();
        }
        //kreiraj tabele
        try {
            kreirajTabele();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void kreirajTabele() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS grad (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	naziv text NOT NULL UNIQUE,\n"
                + " broj_stanovnika integer,\n"
                + " drzava integer,\n"
                + "	FOREIGN KEY(drzava) REFERENCES drzave(id) ON DELETE CASCADE\n"
                + ");";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.execute();
        sql = "CREATE TABLE IF NOT EXISTS drzava (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	naziv text NOT NULL UNIQUE,\n"
                + " glavni_grad integer,\n"
                + "	FOREIGN KEY(glavni_grad) REFERENCES gradovi(id) ON DELETE CASCADE\n"
                + ");";
        stmt = connection.prepareStatement(sql);
        stmt.execute();
        ubaciPocetneGradove();
    }

    private void ubaciPocetneGradove() {
        //Pariz
        Grad pariz = new Grad( "Pariz", 2229621, 1);
        Drzava francuska = new Drzava("Francuska",1);
        francuska.setGlavniGrad(pariz);
        pariz.setDrzava(francuska);
        dodajGrad(pariz);
        dodajDrzavu(francuska);
        // London
        Grad london = new Grad( "London", 8825000, 2);
        Drzava uk = new Drzava("Velika Britanija", 2);
        uk.setGlavniGrad(london);
        london.setDrzava(uk);
        dodajGrad(london);
        dodajDrzavu(uk);
        //Beč
        Grad bec = new Grad( "Beč", 1867582, 3);
        Drzava austrija = new Drzava( "Austrija", 3);
        austrija.setGlavniGrad(bec);
        bec.setDrzava(austrija);
        dodajGrad(bec);
        dodajDrzavu(austrija);
        // Manchester
        Grad manchester = new Grad( "Manchester", 545500,2);
        manchester.setDrzava(uk);
        dodajGrad(manchester);
        // Graz
        Grad graz = new Grad( "Graz", 280200, 3);
        graz.setDrzava(austrija);
        dodajGrad(graz);
    }

    @Override
    public Grad glavniGrad(String drzava) {
        //ovo ispravit da daje glavni grad????
        String sql = "select id, naziv, glavni_grad from drzava where naziv = ?";
        try {
            long idGlavnogGrada;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, drzava);
            ResultSet set = stmt.executeQuery();
            Drzava d = new Drzava();
            while ( set.next() ) {
                d.setId(set.getLong(1));
                d.setNaziv(set.getString(2));
                d.setGradId(set.getLong(3));
            }
            ArrayList<Grad> gradovi = new ArrayList<>();
            gradovi = gradovi();
            for (Grad grd:gradovi) {
                if (d.getGradId() == grd.getId()) return grd;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void obrisiDrzavu(String drzava) {
        long idDrzava = 0;
        ArrayList<Drzava> drz = drzave();
        for (Drzava d:drz) {
            if (d.getNaziv().equals(drzava)) idDrzava = d.getId();
        }
        obrisiGrad(idDrzava);
        String sql = "delete from drzava where naziv = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,drzava);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void obrisiGrad(long id) {
        String sql = "delete from grad where drzava = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1,id);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Grad> gradovi() {
        ArrayList<Drzava> drzave = new ArrayList<>();
        drzave = drzave();
        String sql = "select id, naziv, broj_stanovnika, drzava from grad order by broj_stanovnika desc ";
        ArrayList<Grad> res = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet set = stmt.executeQuery();

            while ( set.next() ) {
                Grad g = new Grad();
                g.setId(set.getLong(1));
                g.setNaziv(set.getString(2));
                g.setBrojStanovnika(set.getInt(3));
                g.setDrzavaId(set.getLong(4));
                for (Drzava drz:drzave) {
                    if (drz.getId() == g.getDrzavaId()) g.setDrzava(drz);
                }
                res.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public ArrayList<Drzava> drzave() {
        String sql = "select id, naziv, glavni_grad from drzava";
        ArrayList<Drzava> res = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet set = stmt.executeQuery();

            while ( set.next() ) {
                Drzava d = new Drzava();
                d.setId(set.getLong(1));
                d.setNaziv(set.getString(2));
                d.setGradId(set.getLong(3));
                res.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public void dodajGrad(Grad grad) {
        ArrayList<Drzava> drzave = drzave();
        long drId = 0;
        if (drzave.size() == 0) {
            drId = drzave.size()+1;
        }
        else {
            drId = drzave.size();
        }
        for (Drzava d:drzave) {
            if (d.getNaziv().equals(grad.getDrzava().getNaziv())) {
                drId = d.getId();
            }
        }
        String sql = "insert into grad(naziv, broj_stanovnika, drzava) values (?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, grad.getNaziv());
            stmt.setInt(2, grad.getBrojStanovnika());
            stmt.setLong(3, drId); //grad.getDrzavaId()
            stmt.execute();
            grad.setId(lastInsertedId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private long lastInsertedId() {
        String sql = "select last_insert_rowid()";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void dodajDrzavu(Drzava drzava) {
        ArrayList<Grad> gradovi = gradovi();
        long grId = 0;
        if (gradovi.size() == 0) {
            grId = gradovi.size()+1;
        }
        else {
            grId = gradovi.size();
        }
        for (Grad g:gradovi) {
            if (g.getNaziv().equals(drzava.getGlavniGrad().getNaziv())) {
                grId = g.getId();
            }
        }
        String sql = "insert into drzava (naziv, glavni_grad) values (?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, drzava.getNaziv());
            stmt.setLong(2, grId); //drzava.getGradId()
            stmt.execute();
            drzava.setId(lastInsertedId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void izmijeniGrad(Grad grad) {
        String sql = "update grad set naziv = ?, broj_stanovnika = ?, drzava = ? where id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1 , grad.getNaziv());
            stmt.setInt(2,grad.getBrojStanovnika());
            stmt.setLong(3,grad.getDrzavaId());
            stmt.setLong(4,grad.getId());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Drzava nadjiDrzavu(String drzava) {
        String sql = "select id, naziv, glavni_grad from drzava where naziv = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, drzava);
            ResultSet set = stmt.executeQuery();

            while ( set.next() ) {
                Drzava d = new Drzava();
                d.setId(set.getLong(1));
                d.setNaziv(set.getString(2));
                d.setGradId(set.getLong(3));
                return d;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
