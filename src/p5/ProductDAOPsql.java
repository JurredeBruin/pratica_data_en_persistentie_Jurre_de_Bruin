package p5;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {
    private Connection Conn;
    private OVChipkaartDAOPsql ovdao;

    public ProductDAOPsql(Connection conn,OVChipkaartDAOPsql ovdao) {
        this.Conn = conn;
        this.ovdao=ovdao;
    }


    public boolean save(Product product) {
        if ( this.__save(product) ) {
            this.ovdao.saveList(product.getOvChipkaartList());
        }
        return false;
    }

    private boolean __save(Product product) {
        try {
            String q = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = this.Conn.prepareStatement(q);
            pst.setInt(1, product.getProduct_nummer() );
            pst.setString(2, product.getNaam() );
            pst.setString(3, product.getBeschrijving() );
            pst.setDouble(4, product.getPrijs() );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("ProductDaopsql geeft een error in __save(): " + err.getMessage() );
            return false;
        }
    }

    public boolean saveList(ArrayList<Product> productArrayList) {
        try {
            if ( productArrayList == null || productArrayList.isEmpty() ) {
                throw new Exception("Product Arraylist is invalid");
            }

            for (Product product : productArrayList) {
                this.__save(product);
            }
            return true;
        } catch(Exception err) {
            System.err.println("ProductDaopsql geeft een error in saveList(): " + err.getMessage() );
            return false;
        }
    }

    private boolean __update(Product product) {
        try {
            String q = "UPDATE product SET naam=?, beschrijving=?, prijs=? WHERE product_nummer=?;";
            PreparedStatement pst = this.Conn.prepareStatement(q);
            pst.setString(1, product.getNaam() );
            pst.setString(2, product.getBeschrijving() );
            pst.setDouble(3, product.getPrijs() );
            pst.setInt(4, product.getProduct_nummer() );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("ProductDaopsql geeft een error in __update(): " + err.getMessage() );
            return false;
        }
    }



    public boolean update(Product product) {
        if ( this.__update(product) ) {
            return this.ovdao.updateList( product.getOvChipkaartList() );
        }
        return false;
    }

    public boolean updateList(ArrayList<Product> productArrayList) {
        try {
            if ( productArrayList == null || productArrayList.isEmpty() ) {
                throw new Exception("Product Arraylist is invalid");
            }

            for (Product product : productArrayList) {
                this.__update(product);
            }
            return true;
        } catch(Exception err) {
            System.err.println("ProductDaopsql geeft een error in updatelist(): " + err.getMessage() );
            return false;
        }
    }

    public boolean delete(Product product) {
        try {
            if (this.deleteLink(product)) {
                String q = "DELETE FROM product WHERE product_nummer=?";
                PreparedStatement pst = this.Conn.prepareStatement(q);
                pst.setInt(1, product.getProduct_nummer() );

                pst.execute();
                pst.close();
                return true;
            }
            throw new Exception("productLinks could not be deleted");
        } catch(Exception err) {
            System.err.println("ProductDaopsql geeft een error in delete(): " + err.getMessage() );
            return false;
        }
    }

    private boolean deleteLink(Product product) {
        try {
            String q = "DELETE FROM ov_chipkaart_product WHERE product_nummer=?";
            PreparedStatement pst = this.Conn.prepareStatement(q);
            pst.setInt(1, product.getProduct_nummer() );

            pst.execute();
            pst.close();
            return true;
        } catch(Exception err) {
            System.err.println("ProductDaopsql geeft een error in deletelink(): " + err.getMessage() );
            return false;
        }
    }

    public boolean deleteList(ArrayList<Product> productArrayList) {
        try {
            if ( productArrayList == null || productArrayList.isEmpty() ) {
                throw new Exception("Product Arraylist is invalid");
            }

            for (Product product : productArrayList) {
                this.delete(product);
            }
            return true;
        } catch(Exception err) {
            System.err.println("ProductDaopsql geeft een error in deletelist(): " + err.getMessage() );
            return false;
        }
    }

//    public List<Product> findall() {
//        List<Product> prArray = new ArrayList<>();
//        try {
//            Statement st = this.Conn.createStatement();
//            ResultSet rs = st.executeQuery("select * from product");
//
//            while ( rs.next() ) {
////                String query = "Update ov_chipkaart set geldig_tot=?,klasse=?,saldo=?,reiziger_id=? where kaart_nummer=?";
//
//                Product pr = new Product(
//                        rs.getInt("product_nummer"),
//                        rs.getString("naam"),
//                        rs.getString("beschrijving"),
//                        rs.getDouble("prijs")
//                );
//
////                OVChipkaart ovChipkaart = ovdao.findByProduct(pr);
////                pr.setovchipkaart(ovChipkaart);
//
//                prArray.add(pr);
//            }
//            rs.close();
//        } catch(Exception err) {
//            System.err.println("AdresDAOsql geeft een error in findAll(): " + err.getMessage() );
//        }
//        return prArray;
//    }
    public ArrayList<Product>findByOvchipkaart(OVChipkaart ovChipkaart) {
        ArrayList<Product> productArrayList = new ArrayList<>();
        try {
            String q = "SELECT product.product_nummer, naam, beschrijving, prijs FROM ov_chipkaart_product " +
                    "INNER JOIN product ON ov_chipkaart_product.product_nummer = product.product_nummer " +
                    "WHERE kaart_nummer = ?";
            PreparedStatement pst = this.Conn.prepareStatement(q);
            pst.setInt(1, ovChipkaart.getKaart_nummer() );
            ResultSet rs = pst.executeQuery();

            while (rs.next() ) {
                Product product = this.retrieveResultset(rs, ovChipkaart);
                productArrayList.add(product);
            }
            rs.close();
            pst.close();
        } catch(Exception err) {
            System.err.println("ProductDAOPsql geeft een error in findbyovChipkaart(): " + err.getMessage() );
        }

        return productArrayList;
    }
    public Product findById(int id) {
        Product product = null;
        try {
            String q = "SELECT product_nummer, naam, beschrijving, prijs FROM product WHERE product_nummer = ?";
            PreparedStatement pst = this.Conn.prepareStatement(q);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next() ) {
                product = this.retrieveResultset(rs, null);
            }
            rs.close();
            pst.close();

        } catch(Exception err) {
            System.err.println("productDAOPsql geeft een error in findbyID(): " + err.getMessage() );
        }
        return product;
    }
    public ArrayList<Product> findAll() {
        ArrayList<Product> productArrayList = new ArrayList<>();
        try {
            String q = "SELECT product_nummer, naam, beschrijving, prijs FROM product";
            PreparedStatement pst = this.Conn.prepareStatement(q);
            ResultSet rs = pst.executeQuery();

            while (rs.next() ) {
                Product product = this.retrieveResultset(rs, null);
                productArrayList.add(product);
            }
            rs.close();
            pst.close();
        } catch(Exception err) {
            System.err.println("productDAOPsql geeft een error in findall(): " + err.getMessage() );
        }

        return productArrayList;
    }
    private void __addRelations(Product product ) {
        ArrayList<OVChipkaart> ovChipkaartList = ovdao.findByProduct( product );
        product.setOvChipkaartList(ovChipkaartList, false);
    }
    private Product retrieveResultset(ResultSet rs, OVChipkaart ovchipkaart) {
        Product product = null;
        try {
            product = new Product(
                    rs.getInt("product_nummer"),
                    rs.getString("naam"),
                    rs.getString("beschrijving"),
                    rs.getDouble("prijs")
            );

            if (ovchipkaart == null) {
                __addRelations(product);
            } else {
                ArrayList<OVChipkaart> ovChipkaartList = new ArrayList();
                ovChipkaartList.add(ovchipkaart);
                product.setOvChipkaartList(ovChipkaartList, false);
            }

        } catch (Exception err) {
            System.err.println("OvchipkaartDAOsql geeft een error in __retrieveResultSet(): " + err.getMessage());
        }
        return product;
    }
}
