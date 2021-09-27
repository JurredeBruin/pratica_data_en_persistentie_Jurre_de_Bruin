package p4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAOsql adao;
    private OVChipkaartDAO ovdao;


    public ReizigerDAOPsql(Connection connection, AdresDAOsql adao,OVChipkaartDAOPsql ovdao) {
        this.conn = connection;
        this.adao=adao;
        this.ovdao=ovdao;
    }

    public boolean save(Reiziger reiziger) {
        try {

            String query = "INSERT INTO reiziger(reiziger_id,voorletters,tussenvoegsel,achternaam,geboortedatum) values(?,?,?,?,?)";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt (1, reiziger.getId());
            preparedStmt.setString (2, reiziger.getVoorletters());
            preparedStmt.setString   (3, reiziger.getTussenvoegsel());
            preparedStmt.setString(4, reiziger.getAchternaam());
            preparedStmt.setDate    (5, reiziger.getGeboortedatum());

            preparedStmt.execute();
            adao.save(reiziger.getAdres());
            if(reiziger.getOvChipkaartList()!=null){
                for(OVChipkaart ov:reiziger.getOvChipkaartList()){
                    ovdao.save(ov);
                }
            }


            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean update(Reiziger reiziger) {
        try {

            String query = "Update reiziger set voorletters=?,tussenvoegsel=?,achternaam=?,geboortedatum=? where reiziger_id=?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, reiziger.getVoorletters());
            preparedStmt.setString   (2, reiziger.getTussenvoegsel());
            preparedStmt.setString(3, reiziger.getAchternaam());
            preparedStmt.setDate    (4, reiziger.getGeboortedatum());
            preparedStmt.setInt(5,reiziger.getId());

            preparedStmt.execute();
            adao.update(reiziger.getAdres());
            if(reiziger.getOvChipkaartList()!=null){
                for(OVChipkaart ov:reiziger.getOvChipkaartList()){
                    ovdao.update(ov);
                }
            }
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }    };

    public boolean delete(Reiziger reiziger) {
        try {
            adao.delete(reiziger.getAdres());
            if(reiziger.getOvChipkaartList()!=null){
                for(OVChipkaart ov:reiziger.getOvChipkaartList()){
                    ovdao.delete(   ov);
                }
            }
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
        return this.findById(id, null);
    }

    public Reiziger findByAdres(Adres adres){
        int id = adres.getReiziger_id();
        Reiziger reiziger = this.findById(id, adres);

        return reiziger;
    }

    public Reiziger findByOVChipkaart(OVChipkaart ovChipkaart){
        int id = ovChipkaart.getReiziger_id();
        Reiziger reiziger = this.findById(id, null);

        return reiziger;
    }

    public List<Reiziger> findByGbdatum(String datum) {
        List<Reiziger> reizigersArray = new ArrayList<>();

        try {
            String q = "SELECT * FROM reiziger WHERE geboorteDatum = ?";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setDate(1, Date.valueOf(datum));
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Reiziger reiziger =  this.retrieveRs(rs, null);
                reizigersArray.add(reiziger);
            }

            pst.close();
            rs.close();

            return reizigersArray;
        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in findByGbDatum(): " + err.getMessage());
            return reizigersArray;
        }
    };

    public List<Reiziger> findAll() {
        List<Reiziger> reizigersArray = new ArrayList<>();

        try {
            Statement st = this.conn.createStatement();
            ResultSet rs = st.executeQuery("select * from reiziger");

            while (rs.next()) {
                Reiziger reiziger = this.retrieveRs(rs, null);
                reizigersArray.add(reiziger);
            }
            rs.close();
        } catch (Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in findAll(): " + err.getMessage());
        }

        return reizigersArray;
    };

    private Reiziger addRelations(Reiziger reiziger) {
        reiziger = this.addAdresRelation(reiziger);
        reiziger = this.addchipRelation(reiziger);

        return reiziger;
    }

    private Reiziger addAdresRelation(Reiziger reiziger) {
        Adres adres = adao.findByReiziger( reiziger );
        if (adres != null) {
            reiziger.setAdres(adres);
        }

        return reiziger;
    }

    private Reiziger addchipRelation(Reiziger reiziger) {
        List<OVChipkaart> ovChipkaarten = ovdao.findByReiziger( reiziger );
        if (!ovChipkaarten.isEmpty()) {
            reiziger.setOvChipkaartList(ovChipkaarten);
        }

        return reiziger;
    }

    private Reiziger findById(int id, Adres adres) {
        try {
            String q = "SELECT * FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setInt(1, id );
            ResultSet rs = pst.executeQuery();

            Reiziger reiziger = null;
            if ( rs.next() ) {
                reiziger =  this.retrieveRs(rs, adres);
            }

            pst.close();
            rs.close();
            return reiziger;


        } catch(Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in __findbyid(): " + err.getMessage());
            return null;
        }
    }

    private Reiziger retrieveRs(ResultSet rs, Adres adres)  {
        Reiziger reiziger = null;
        try {
            reiziger = new Reiziger(
                    rs.getInt("reiziger_id"),
                    rs.getString("voorletters"),
                    rs.getString("tussenvoegsel"),
                    rs.getString("achternaam"),
                    rs.getDate("geboorteDatum"),
                    null
            );

            if (adres == null) {
                this.addAdresRelation(reiziger);
            }
            this.addchipRelation(reiziger);

        } catch (Exception err) {
            System.err.println("ReizigersDAOsql geeft een error in __retrieveResultSet(): " + err.getMessage());
        }
        return reiziger;
    }
}
