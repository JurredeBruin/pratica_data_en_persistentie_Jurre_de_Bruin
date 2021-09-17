package p4;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection Conn;
    private ReizigerDAOPsql rdao;


    public OVChipkaartDAOPsql(Connection conn) {
        this.Conn = conn;
    }
    public void connectRDAO(ReizigerDAOPsql rdao) {
        this.rdao=rdao;

    }
    public boolean save(OVChipkaart ov) {
        try {
//            Statement ctt = conn.createStatement();

            String query = "INSERT INTO ov_chipkaart(kaart_nummer,geldig_tot,klasse,saldo,reiziger_id) values(?,?,?,?,?)";
            PreparedStatement preparedStmt = Conn.prepareStatement(query);
            preparedStmt.setInt (1, ov.getKaart_nummer());
            preparedStmt.setDate (2, ov.getGeldig_tot());
            preparedStmt.setInt   (3, ov.getKlasse());
            preparedStmt.setDouble(4, ov.getSaldo());
            preparedStmt.setInt    (5, ov.getReiziger_id());


            // execute the preparedstatement
            preparedStmt.execute();



            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
    public boolean update(OVChipkaart ov) {
        try {
//            Statement ctt = conn.createStatement();

            String query = "Update ov_chipkaart set geldig_tot=?,klasse=?,saldo=?,reiziger_id=? where kaart_nummer=?";
            PreparedStatement preparedStmt = Conn.prepareStatement(query);
            preparedStmt.setDate (1, ov.getGeldig_tot());
            preparedStmt.setInt (2, ov.getKlasse());
            preparedStmt.setDouble   (3, ov.getSaldo());
            preparedStmt.setInt(4, ov.getReiziger_id());
            preparedStmt.setInt    (5, ov.getKaart_nummer());



            preparedStmt.execute();



            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    };

    public boolean delete(OVChipkaart ov) {
        try {

            String query = "Delete from ov_chipkaart where kaart_nummer=?";
            PreparedStatement preparedStmt = Conn.prepareStatement(query);
            preparedStmt.setInt (1, ov.getKaart_nummer());

            preparedStmt.execute();



            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        List<OVChipkaart> OVChipkaartArray = new ArrayList<>();
        try {
            String q = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
            PreparedStatement pst = this.Conn.prepareStatement(q);
            pst.setInt(1, reiziger.getId() );
            ResultSet rs = pst.executeQuery();

            while (rs.next() ) {
                OVChipkaart ovChipkaart = this.__retrieveResultset(rs,  reiziger);
                OVChipkaartArray.add(ovChipkaart);
            }
            rs.close();
            pst.close();

        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in findByReiziger(): " + err.getMessage() + " " + err.getStackTrace() );
        }

        return OVChipkaartArray;
    }

    public List<OVChipkaart> findall() {
        List<OVChipkaart> ovArray = new ArrayList<>();
        try {
            Statement st = this.Conn.createStatement();
            ResultSet rs = st.executeQuery("select * from ov_chipkaart");

            while ( rs.next() ) {
                String query = "Update ov_chipkaart set geldig_tot=?,klasse=?,saldo=?,reiziger_id=? where kaart_nummer=?";

                OVChipkaart ov = new OVChipkaart(
                        rs.getInt("kaart_nummer"),
                        rs.getDate("geldig_tot"),
                        rs.getInt("klasse"),
                        rs.getDouble("saldo"),
                        rs.getInt("reiziger_id"),
                        null
                );

                Reiziger reiziger = rdao.findByOVChipkaart(ov);
                ov.setReiziger(reiziger);

                ovArray.add(ov);
            }
            rs.close();
        } catch(Exception err) {
            System.err.println("AdresDAOsql geeft een error in findAll(): " + err.getMessage() );
        }
        return ovArray;
    }
    private OVChipkaart __addrelations(OVChipkaart ovChipkaart) {
        Reiziger reiziger = rdao.findByOVChipkaart(ovChipkaart);
        ovChipkaart.setReiziger(reiziger);

        return ovChipkaart;
    }

    private OVChipkaart __retrieveResultset(ResultSet rs, Reiziger reiziger)  {
        OVChipkaart ovChipkaart = null;
        try {
            ovChipkaart = new OVChipkaart(
                    rs.getInt("kaart_nummer"),
                    rs.getDate("geldig_tot"),
                    rs.getInt("klasse"),
                    rs.getDouble("saldo"),
                    rs.getInt("reiziger_id"),
                    reiziger
            );

            if (reiziger == null) {
                __addrelations(ovChipkaart);
            }
        } catch (Exception err) {
            System.err.println("OvchipkaartDAOsql geeft een error in __retrieveResultSet(): " + err.getMessage() + " " +  err.getStackTrace());
        }
        return ovChipkaart;
    }
}
