package p3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOsql implements AdresDAO{
    private Connection conn;
    private ReizigerDAO rdao;

    public AdresDAOsql(Connection connection) {
        this.conn = connection;
    }

    public boolean save(Adres adres) {
        try {
//            Statement ctt = conn.createStatement();

            String query = "INSERT INTO adres(adres_id,postcode,huisnummer,straat,woonplaats,reiziger_id) values(?,?,?,?,?,?)";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt (1, adres.getId());
            preparedStmt.setString (2, adres.getPostcode());
            preparedStmt.setString   (3, adres.getHuisnummer());
            preparedStmt.setString(4, adres.getStraat());
            preparedStmt.setString    (5, adres.getWoonplaats());
            preparedStmt.setInt    (6, adres.getReiziger_id());


            // execute the preparedstatement
            preparedStmt.execute();



            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean update(Adres adres) {
        try {
//            Statement ctt = conn.createStatement();

            String query = "Update adres set postcode=?,huisnummer=?,straat=?,woonplaats=?,reiziger_id=? where adres_id=?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, adres.getPostcode());
            preparedStmt.setString (2, adres.getHuisnummer());
            preparedStmt.setString   (3, adres.getStraat());
            preparedStmt.setString(4, adres.getWoonplaats());
            preparedStmt.setInt    (5, adres.getReiziger_id());
            preparedStmt.setInt    (6, adres.getId());



            preparedStmt.execute();



            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    };

    public boolean delete(Adres adres) {
        try {

            String query = "Delete from adres where adres_id=?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt (1, adres.getId());

            preparedStmt.execute();



            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        try {
            String query = "SELECT * FROM adres WHERE reiziger_id = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, reiziger.getId());
            ResultSet rs = preparedStmt.executeQuery();

            rs.next();
            return new Adres(

                    rs.getInt("adres_id"),
                    rs.getString("postcode"),
                    rs.getString("huisnummer"),
                    rs.getString("straat"),
                    rs.getString("woonplaats"),
                    rs.getInt("reiziger_id"));


        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft error bij findbyid(): " + err.getMessage() );
            return new Adres(0, "", "", "", "",0 );
        }

    }
    public Adres findByReizigerid(int id) {
        try {
            String query = "SELECT * FROM adres WHERE reiziger_id = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, id);
            ResultSet rs = preparedStmt.executeQuery();

            rs.next();
            return new Adres(

                    rs.getInt("adres_id"),
                    rs.getString("postcode"),
                    rs.getString("huisnummer"),
                    rs.getString("straat"),
                    rs.getString("woonplaats"),
                    rs.getInt("reiziger_id"));


        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft error bij findbyid(): " + err.getMessage() );
            return new Adres(0, "", "", "", "",0 );
        }

    }


    public List<Adres> findall() {
        List<Adres> adres = new ArrayList<>();

        try {
            Statement st = conn.createStatement();
            String query = "SELECT * From adres";
            ResultSet rs=st.executeQuery(query);
            while(rs.next()) {
                int id = rs.getInt("adres_id");
                String postcode = rs.getString("postcode");
                String huisnummer = rs.getString("huisnummer");
                String straat = rs.getString("straat");
                String woonplaats = rs.getString("woonplaats");
                int reizigerid = rs.getInt("reiziger_id");

                adres.add(new Adres(id,postcode,huisnummer,straat,woonplaats,reizigerid));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return adres;
    }

    @Override
    public void connectRDAO(ReizigerDAO rdao) {

    }
}
