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


    public boolean save(Product pr) {
        try {
            String query = "INSERT INTO product(product_nummer,naam,beschrijving,prijs) values(?,?,?,?)";
            PreparedStatement preparedStmt = Conn.prepareStatement(query);
            preparedStmt.setInt (1, pr.getProduct_nummer());
            preparedStmt.setString (2, pr.getNaam());
            preparedStmt.setString   (3, pr.getBeschrijving());
            preparedStmt.setDouble(4, pr.getPrijs());

            preparedStmt.execute();

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
    public boolean update(Product pr) {
        try {
            String query = "Update product set naam=?,beschrijving=?,prijs=? where product_nummer=?";
            PreparedStatement preparedStmt = Conn.prepareStatement(query);
            preparedStmt.setString(1, pr.getNaam());
            preparedStmt.setString (2, pr.getBeschrijving());
            preparedStmt.setDouble   (3, pr.getPrijs());
            preparedStmt.setInt(4, pr.getProduct_nummer());

            preparedStmt.execute();

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    };

    public boolean delete(Product pr) {
        try {

            String query = "Delete from product where product_nummer=?";
            PreparedStatement preparedStmt = Conn.prepareStatement(query);
            preparedStmt.setInt (1, pr.getProduct_nummer());

            preparedStmt.execute();

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
    public List<Product> findall() {
        List<Product> prArray = new ArrayList<>();
        try {
            Statement st = this.Conn.createStatement();
            ResultSet rs = st.executeQuery("select * from product");

            while ( rs.next() ) {
//                String query = "Update ov_chipkaart set geldig_tot=?,klasse=?,saldo=?,reiziger_id=? where kaart_nummer=?";

                Product pr = new Product(
                        rs.getInt("product_nummer"),
                        rs.getString("naam"),
                        rs.getString("beschrijving"),
                        rs.getDouble("prijs")
                );

//                OVChipkaart ovChipkaart = ovdao.findByProduct(pr);
//                pr.setovchipkaart(ovChipkaart);

                prArray.add(pr);
            }
            rs.close();
        } catch(Exception err) {
            System.err.println("AdresDAOsql geeft een error in findAll(): " + err.getMessage() );
        }
        return prArray;
    }
    public ArrayList<Product> findByOvchipkaart(OVChipkaart ovchipkaart) {
        ArrayList<Product> ProductArray = new ArrayList<>();
        try {
            String querry = "SELECT product.product_nummer, naam, beschrijving, prijs FROM ov_chipkaart_product INNER JOIN product ON ov_chipkaart_product.product_nummer = product.product_nummer WHERE kaart_nummer = ?";
            PreparedStatement pst = this.Conn.prepareStatement(querry);
            pst.setInt(1, ovchipkaart.getKaart_nummer() );
            ResultSet rs = pst.executeQuery();

            while (rs.next() ) {
                Product product = this.retrieveResultset(rs,  ovchipkaart);
                ProductArray.add(product);
            }
            rs.close();
            pst.close();

        } catch(Exception err) {
            System.err.println("productDAOPsql geeft een error in findbyOvchipkaart(): " + err.getMessage());
        }

        return ProductArray;
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
    private Product retrieveResultset(ResultSet rs, OVChipkaart ovchipkaart) {
        Product product = null;
        try {
            product = new Product(
                    rs.getInt("product_nummer"),
                    rs.getString("naam"),
                    rs.getString("beschrijving"),
                    rs.getDouble("prijs")
            );

        } catch (Exception err) {
            System.err.println("OvchipkaartDAOsql geeft een error in __retrieveResultSet(): " + err.getMessage());
        }
        return product;
    }
}
