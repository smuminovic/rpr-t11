package ba.unsa.etf.rpr;

import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;

public class GeografijaDAO implements DAO {
    private static GeografijaDAO instance = new GeografijaDAO();

    public static GeografijaDAO getInstance() {
        return instance;
    }

    public  static void removeInstance() {
        instance = null;
    }

    private Connection connection;

    private GeografijaDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:baza.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    }

    @Override
    public void dodajDrzavu(Drzava drzava) {
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
        return null;
    }
}
