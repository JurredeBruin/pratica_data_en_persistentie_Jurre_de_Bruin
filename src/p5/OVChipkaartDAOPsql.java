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
    public void setReizigerDAO(ReizigerDAOPsql reizigerDAO) { this.rdao = reizigerDAO; }
    public void setProductDAO(ProductDAOPsql productDAO) { this.pdao = productDAO; }
//    public void connectRDAO(ReizigerDAOPsql rdao) {
//        this.rdao=rdao;
//
//    }
//    public void connectProductDAO(ProductDAOPsql pr) {
//        this.pdao=pr;
//    }
    public boolean save(OVChipkaart ovChipkaart) {
        if ( this.__save(ovChipkaart) ) {
            if ( pdao.saveList(ovChipkaart.getProductList()) ) {
                return rdao.save(ovChipkaart.getReiziger());
            }
        }
        return false;
    }

    private boolean __save(OVChipkaart ovChipkaart) {
        try {
            String q = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = this.Conn.prepareStatement(q);
            pst.setInt(1, ovChipkaart.getKaart_nummer() );
            pst.setDate(2,  new Date(ovChipkaart.getGeldig_tot().getTime() ) );
            pst.setInt(3,ovChipkaart.getKlasse() );
            pst.setDouble(4, ovChipkaart.getSaldo() );
            pst.setInt(5, ovChipkaart.getReiziger_id() );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in __save(): " + err.getMessage());
            return false;
        }
    }

    public boolean saveList(ArrayList<OVChipkaart> ovChipkaartList) {
        try {
            if ( ovChipkaartList == null || ovChipkaartList.isEmpty() ) {
                throw new Exception("OvChipkaart Arraylist is invalid");
            }

            for (OVChipkaart ovChipkaart : ovChipkaartList) {
                this.__save(ovChipkaart);
            }
            return true;
        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in saveList(): " + err.getMessage());
            return false;
        }
    }

    private boolean __update(OVChipkaart ovChipkaart) {
        try {
            String q = "UPDATE ov_chipkaart SET geldig_tot=?, klasse=?, saldo=? WHERE kaart_nummer=?;";
            PreparedStatement pst = this.Conn.prepareStatement(q);
            pst.setDate(1,  new Date(ovChipkaart.getGeldig_tot().getTime() ) );
            pst.setInt(2, ovChipkaart.getKlasse() );
            pst.setDouble(3, ovChipkaart.getSaldo() );
            pst.setInt(4, ovChipkaart.getKaart_nummer() );

            pst.execute();

            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in __update(): " + err.getMessage());
            return false;
        }
    }

    public boolean update(OVChipkaart ovChipkaart) {
        if ( this.__update(ovChipkaart) ) {
            if ( pdao.updateList(ovChipkaart.getProductList()) ) {
                return rdao.update(ovChipkaart.getReiziger());
            }
        }
        return false;
    }

    public boolean updateList(ArrayList<OVChipkaart> ovChipkaartArrayList) {
        try {
            if ( ovChipkaartArrayList == null || ovChipkaartArrayList.isEmpty() ) {
                throw new Exception("OvChipkaart Arraylist is invalid");
            }

            for (OVChipkaart ovChipkaart : ovChipkaartArrayList) {
                this.__update(ovChipkaart);
            }
            return true;
        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in updateList(): " + err.getMessage());
            return false;
        }
    }

    public boolean delete(OVChipkaart ovChipkaart) {
        try {
            if ( this.deleteLink(ovChipkaart) ) {
                String q = "DELETE FROM ov_chipkaart WHERE kaart_nummer=?;";
                PreparedStatement pst = this.Conn.prepareStatement(q);
                pst.setInt(1, ovChipkaart.getKaart_nummer() );
                pst.execute();

                pst.close();
                return true;
            }

            throw new Exception("OvChipkaart links could not be deleted");

        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in delete(): " + err.getMessage());
            return false;
        }
    }

    private boolean deleteLink(OVChipkaart ovChipkaart) {
        try {
            String q = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer=?";
            PreparedStatement pst = this.Conn.prepareStatement(q);
            pst.setInt(1, ovChipkaart.getKaart_nummer() );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in deletelink(): " + err.getMessage());
            return false;
        }
    }

    public boolean deleteList(ArrayList<OVChipkaart> ovChipkaartArrayList) {
        try {
            if ( ovChipkaartArrayList == null || ovChipkaartArrayList.isEmpty() ) {
                throw new Exception("OvChipkaart Arraylist is invalid");
            }

            for (OVChipkaart ovChipkaart : ovChipkaartArrayList) {
                this.delete(ovChipkaart);
            }
            return true;
        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in deletelist(): " + err.getMessage());
            return false;
        }
    }

    public ArrayList<OVChipkaart> findByReiziger(Reiziger reiziger) {
        ArrayList<OVChipkaart> OVChipkaartArray = new ArrayList<>();
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

    public ArrayList<OVChipkaart> findall() {
        ArrayList<OVChipkaart> OVChipkaartArray = new ArrayList<>();
        try {
            Statement st = this.Conn.createStatement();
            ResultSet rs = st.executeQuery("select * from ov_chipkaart");

            while (rs.next()) {
                OVChipkaart ovChipkaart = this.retrieveResultset(rs, true);
                OVChipkaartArray.add(ovChipkaart);
            }
            rs.close();

        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in findall(): " + err.getMessage());
        }

        return OVChipkaartArray;
    }
    public OVChipkaart findByKaartNummer(int id) {
        try {
            String q = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?";
            PreparedStatement pst = this.Conn.prepareStatement(q);
            pst.setInt(1, id );
            ResultSet rs = pst.executeQuery();

            OVChipkaart ovChipkaart = null;
            if (rs.next() ) {
                ovChipkaart = this.retrieveResultset(rs, true);
            }
            rs.close();
            pst.close();

            return ovChipkaart;

        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in findbykaartnmr(): " + err.getMessage());
            return null;
        }
    }

    public ArrayList<OVChipkaart> findByProduct(Product pr) {
        ArrayList<OVChipkaart> ovarray = new ArrayList<>();
        try {
            String q = "SELECT ov_chipkaart.kaart_nummer, geldig_tot, klasse, saldo, reiziger_id FROM ov_chipkaart_product INNER JOIN ov_chipkaart ON ov_chipkaart.kaart_nummer = ov_chipkaart_product.kaart_nummer WHERE ov_chipkaart_product.product_nummer = ?";
            PreparedStatement pst = this.Conn.prepareStatement(q);
            pst.setInt(1, pr.getProduct_nummer() );
            ResultSet rs = pst.executeQuery();

            while (rs.next() ) {
                OVChipkaart ovchip = this.retrieveResultset(rs , pr);
                ovarray.add(ovchip);
            }
            rs.close();
            pst.close();

        } catch(Exception err) {
            System.err.println("OVCHipkaartDAOPsql geeft een error in findByReiziger(): " + err.getMessage());
        }

        return ovarray;    }


    private void addrelation(OVChipkaart ovChipkaart) {
        this.addrelationsProduct(ovChipkaart);
        this.addrelationsReiziger(ovChipkaart);
    }
    private void addrelationsReiziger(OVChipkaart ovChipkaart) {
        Reiziger reiziger = rdao.findByOVChipkaart(ovChipkaart);
        ovChipkaart.setReiziger(reiziger, false);
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
