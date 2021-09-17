package p4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOsql implements AdresDAO {
    private Connection conn;
    private ReizigerDAOPsql rdao;

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
            String q = "SELECT * FROM Adres WHERE reiziger_id = ?";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setInt(1, reiziger.getId() );
            ResultSet rs = pst.executeQuery();

            if (rs.next() ) {
                Adres adres = new Adres(
                        rs.getInt("adres_id"),
                        rs.getString("straat"),
                        rs.getString("huisnummer"),
                        rs.getString("postcode"),
                        rs.getString("woonplaats"),
                        rs.getInt("reiziger_id"),
                        reiziger
                );
                return adres;
            } else {
                return null;
            }

        } catch(Exception err) {
            System.err.println("AdresDAOsql geeft een error in findByReiziger(): " + err.getMessage() );
            return null;
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
                    rs.getInt("reiziger_id"),
                    null);


        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft error bij findbyid(): " + err.getMessage() );
            return new Adres(0, "", "", "", "",0 ,null);
        }

    }


    public List<Adres> findall() {
        List<Adres> adresArray = new ArrayList<>();
        try {
            Statement st = this.conn.createStatement();
            ResultSet rs = st.executeQuery("select * from adres");

            while ( rs.next() ) {
                Adres adres = new Adres(
                        rs.getInt("adres_id"),
                        rs.getString("postcode"),
                        rs.getString("huisnummer"),
                        rs.getString("straat"),
                        rs.getString("woonplaats"),
                        rs.getInt("reiziger_id"),
                        null
                );

                Reiziger reiziger = rdao.findByAdres(adres);
                adres.setReiziger(reiziger);

                adresArray.add(adres);
            }
            rs.close();
        } catch(Exception err) {
            System.err.println("AdresDAOsql geeft een error in findAll(): " + err.getMessage() );
        }
        return adresArray;
    }


    public void connectRDAO(ReizigerDAOPsql rdao) {
        this.rdao=rdao;

    }
}
