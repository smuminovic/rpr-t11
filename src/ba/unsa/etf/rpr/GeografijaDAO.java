package ba.unsa.etf.rpr;

import java.io.File;
import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;

public class GeografijaDAO implements DAO {
    private static GeografijaDAO instance = new GeografijaDAO();

    public static GeografijaDAO getInstance() {
        if(instance == null) instance = new GeografijaDAO();;
        return instance;
    }

    public static void removeInstance() {
        //File dbfile = new File("baza.db");
        //dbfile.delete();
        instance = null;
    }

    private Connection connection;

    private GeografijaDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:baza.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //if (gradovi() == null) {
            ubaciPocetneGradove();
        //}
    }

    private void ubaciPocetneGradove() {
        //Pariz
        Grad pariz = new Grad(1, "Pariz", 2229621, 1);
        Drzava francuska = new Drzava(1,"Francuska",1);
        francuska.setGlavniGrad(pariz);
        pariz.setDrzava(francuska);
        dodajGrad(pariz);
        dodajDrzavu(francuska);
        //Beč
        Grad bec = new Grad(3, "Beč", 1867582, 3);
        Drzava austrija = new Drzava(3, "Austrija", 3);
        austrija.setGlavniGrad(bec);
        bec.setDrzava(austrija);
        dodajGrad(bec);
        dodajDrzavu(austrija);
        // London
        Grad london = new Grad(2, "London", 8825000, 2);
        Drzava uk = new Drzava(2,"Velika Britanija", 2);
        uk.setGlavniGrad(london);
        london.setDrzava(uk);
        dodajGrad(london);
        dodajDrzavu(uk);
        // Manchester
        Grad manchester = new Grad(4, "Manchester", 545500,2);
        manchester.setDrzava(uk);
        dodajGrad(manchester);
        // Graz
        Grad graz = new Grad(5, "Graz", 280200, 3);
        graz.setDrzava(austrija);
        dodajGrad(graz);
    }

    @Override
    public Grad glavniGrad(String drzava) {
        String sql = "select id, naziv, broj_stanovnika, drzava from grad where drzava = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, drzava);
            ResultSet set = stmt.executeQuery();

            while ( set.next() ) {
                Grad g = new Grad();
                g.setId(set.getLong(1));
                g.setNaziv(set.getString(2));
                g.setBrojStanovnika(set.getInt(3));
                g.setDrzavaId(set.getLong(4));
                return g;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void obrisiDrzavu(String drzava) {
        String sql = "delete from drzava where naziv = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,drzava);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Grad> gradovi() {
        String sql = "select id, naziv, broj_stanovnika, drzava from grad";
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
                res.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public void dodajGrad(Grad grad) {
            String sql = "insert into grad(naziv, broj_stanovnika, drzava) values (?, ?, ?)";
            try {
                if (grad.getId() != -1) {
                    sql = "insert into grad(id, naziv, broj_stanovnika, drzava) values (?, ?, ?, ?)";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setLong(1, grad.getId());
                    stmt.setString(2, grad.getNaziv());
                    stmt.setInt(3, grad.getBrojStanovnika());
                    stmt.setLong(4, grad.getDrzavaId());
                    stmt.execute();
                }
                else {
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, grad.getNaziv());
                    stmt.setInt(2, grad.getBrojStanovnika());
                    stmt.setLong(3, grad.getDrzavaId());
                    stmt.execute();
                    grad.setId(lastInsertedId());
                }

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
        String sql = "insert into drzava (naziv, glavni_grad) values (?, ?)";
        try {
            if (drzava.getId() != -1) {
                sql = "insert into drzava (id, naziv, glavni_grad) values (?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setLong(1, drzava.getId());
                stmt.setString(2, drzava.getNaziv());
                stmt.setLong(3, drzava.getGradId());
                stmt.execute();
            }
            else {
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, drzava.getNaziv());
                stmt.setLong(2, drzava.getGradId());
                stmt.execute();
                drzava.setId(lastInsertedId());
            }
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
