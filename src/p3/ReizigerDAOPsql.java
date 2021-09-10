package p3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAOsql adao;

    public ReizigerDAOPsql(Connection connection, AdresDAOsql adao) {
        this.conn = connection;
        this.adao=adao;
    }

    public boolean save(Reiziger reiziger) {
        try {
//            Statement ctt = conn.createStatement();

            String query = "INSERT INTO reiziger(reiziger_id,voorletters,tussenvoegsel,achternaam,geboortedatum) values(?,?,?,?,?)";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt (1, reiziger.getId());
            preparedStmt.setString (2, reiziger.getVoorletters());
            preparedStmt.setString   (3, reiziger.getTussenvoegsel());
            preparedStmt.setString(4, reiziger.getAchternaam());
            preparedStmt.setDate    (5, reiziger.getGeboortedatum());

            // execute the preparedstatement
            preparedStmt.execute();



            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean update(Reiziger reiziger) {
        try {
//            Statement ctt = conn.createStatement();

            String query = "Update reiziger set voorletters=?,tussenvoegsel=?,achternaam=?,geboortedatum=? where reiziger_id=?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, reiziger.getVoorletters());
            preparedStmt.setString   (2, reiziger.getTussenvoegsel());
            preparedStmt.setString(3, reiziger.getAchternaam());
            preparedStmt.setDate    (4, reiziger.getGeboortedatum());
            preparedStmt.setInt(5,reiziger.getId());

            preparedStmt.execute();



            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }    };

    public boolean delete(Reiziger reiziger) {
        try {
//            Statement ctt = conn.createStatement();

            String query = "Delete from reiziger where reiziger_id=?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt (1, reiziger.getId());

            preparedStmt.execute();



            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }


    public Reiziger findByid(int id) {

        try {
            String query = "SELECT * FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, id);
            ResultSet rs = preparedStmt.executeQuery();


            rs.next();
            int reiziger_id=rs.getInt("reiziger_id");
            Adres adres = adao.findByReizigerid(reiziger_id);
            return new Reiziger(

                    rs.getInt("reiziger_id"),
                    rs.getString("voorletters"),
                    rs.getString("tussenvoegsel"),
                    rs.getString("achternaam"),
                    rs.getDate("geboorteDatum"),
                    adres
            );

        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft error bij findbyid(): " + err.getMessage() );
            return new Reiziger(0, "", "", "", Date.valueOf("00-00-0000"), adao.findByReiziger(findByid(0)) );
        }

    };

    public List<Reiziger> findByGbdatum(String datum) {

        List<Reiziger> reizigers = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            String query = "SELECT * From reiziger WHERE geboortedatum=?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDate(1, Date.valueOf(datum));
            ResultSet rs = preparedStmt.executeQuery();
            while(rs.next()) {
                int reiziger_id=rs.getInt("reiziger_id");
                Adres adres = adao.findByReizigerid(reiziger_id);
                String tussenvoegsel="";
                if (rs.getString("tussenvoegsel") == null) {
                    tussenvoegsel = "";
                } else {
                    tussenvoegsel = rs.getString("tussenvoegsel");
                }
                int id = rs.getInt("reiziger_id");
                Date geboortedatum = rs.getDate("geboortedatum");
                String voornaam = rs.getString("voorletters");
                String achternaam = rs.getString("achternaam");
                reizigers.add(new Reiziger(id,voornaam,tussenvoegsel,achternaam,geboortedatum, adres));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return reizigers;
    }

    public List<Reiziger> findAll() {
        List<Reiziger> reizigers = new ArrayList<>();

        try {
            Statement st = conn.createStatement();
            String query = "SELECT * From reiziger";
            ResultSet rs=st.executeQuery(query);

            while(rs.next()) {
                int reiziger_id=rs.getInt("reiziger_id");
                Adres adres = adao.findByReizigerid(reiziger_id);
                String tussenvoegsel="";
                if (rs.getString("tussenvoegsel") == null) {
                    tussenvoegsel = "";
                } else {
                    tussenvoegsel = rs.getString("tussenvoegsel");
                }
                int id = rs.getInt("reiziger_id");
                Date geboortedatum = rs.getDate("geboortedatum");
                String voornaam = rs.getString("voorletters");
                String achternaam = rs.getString("achternaam");
                reizigers.add(new Reiziger(id,voornaam,tussenvoegsel,achternaam,geboortedatum, adres));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return reizigers;
    }
}
