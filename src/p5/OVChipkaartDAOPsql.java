package p5;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection Conn;
    private ReizigerDAOPsql rdao;
    private ProductDAOPsql pdao;



    public OVChipkaartDAOPsql(Connection conn) {
        this.Conn = conn;
    }
    public void connectRDAO(ReizigerDAOPsql rdao) {
        this.rdao=rdao;

    }
    public void connectProductDAO(ProductDAOPsql pr) {
        this.pdao=pr;
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
                OVChipkaart ovChipkaart = this.retrieveResultset(rs,  reiziger);
                OVChipkaartArray.add(ovChipkaart);
            }
            rs.close();
            pst.close();

        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in findByReiziger(): " + err.getMessage());
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
                        rs.getInt("reiziger_id")
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

    public List<OVChipkaart> findByProduct(Product pr) {
        List<OVChipkaart> ovarray = new ArrayList<>();
        try {
            String q = "SELECT ov_chipkaart.kaart_nummer, geldig_tot, klasse, saldo, reiziger_id FROM ov_chipkaart_product INNER JOIN ov_chipkaart ON ov_chipkaart.kaart_nummer = ov_chipkaart_product.kaart_nummer WHERE ov_chipkaart_product.product_nummer = ?";
            PreparedStatement pst = this.Conn.prepareStatement(q);
            pst.setInt(1, pr.getProduct_nummer() );
            ResultSet rs = pst.executeQuery();

            while (rs.next() ) {
                OVChipkaart ovchip = this.retrieveResultset_product(rs);
                ovarray.add(ovchip);
            }
            rs.close();
            pst.close();

        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in findByReiziger(): " + err.getMessage());
        }

        return ovarray;    }

    private OVChipkaart retrieveResultset_product(ResultSet rs) {
        OVChipkaart ovChipkaart = null;
        try {
            ovChipkaart = new OVChipkaart(
                    rs.getInt("kaart_nummer"),
                    rs.getDate("geldig_tot"),
                    rs.getInt("klasse"),
                    rs.getDouble("saldo"),
                    rs.getInt("reiziger_id")
            );
        } catch (Exception err) {
            System.err.println("OvchipkaartDAOsql geeft een error in __retrieveResultSet(): " + err.getMessage());
        }
        return ovChipkaart;
    }
    private void addrelation(OVChipkaart ovChipkaart) {
        this.addrelationsProduct(ovChipkaart);
        this.addrelationsReiziger(ovChipkaart);
    }
    private OVChipkaart addrelationsReiziger(OVChipkaart ovChipkaart) {
        Reiziger reiziger = rdao.findByOVChipkaart(ovChipkaart);
        ovChipkaart.setReiziger(reiziger);

        return ovChipkaart;
    }
    private void addrelationsProduct(OVChipkaart ovChipkaart) {
        ovChipkaart.setProductList( pdao.findByOvchipkaart(ovChipkaart), false );
    }
    private OVChipkaart retrieveResultset(ResultSet rs, Product product)  {
        OVChipkaart ovChipkaart=  this.retrieveResultset(rs, false);
        if (product == null) {
            System.err.println("Product is null");
            this.addrelationsReiziger(ovChipkaart);
        }
        ArrayList<Product> productList = new ArrayList();
        productList.add(product);
        ovChipkaart.setProductList(productList, false);

        this.addrelationsReiziger(ovChipkaart);

        return ovChipkaart;
    }

    private OVChipkaart retrieveResultset(ResultSet rs, Reiziger reiziger)  {
        OVChipkaart ovChipkaart=  this.retrieveResultset(rs, false);
        if (reiziger == null) {
            System.err.println("reiziger is null");
            this.addrelationsReiziger(ovChipkaart);
        }
        this.addrelationsReiziger(ovChipkaart);

        return ovChipkaart;
    }
    private OVChipkaart retrieveResultset(ResultSet rs,  boolean withrel)  {
        OVChipkaart ovChipkaart = null;
        try {
            ovChipkaart = new OVChipkaart(
                    rs.getInt("kaart_nummer"),
                    rs.getDate("geldig_tot"),
                    rs.getInt("klasse"),
                    rs.getDouble("saldo"),
                    rs.getInt("reiziger_id")
            );
            if (withrel) {
                this.addrelation(ovChipkaart);
            }

        } catch (Exception err) {
            System.err.println("OvchipkaartDAOsql geeft een error in retrieveResultset(resultset rs, boolean withrel): " + err.getMessage());
        }
        return ovChipkaart;
    }
}
