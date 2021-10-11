package p5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDAOPsql implements ProductDAO {
    private Connection conn;
    private OVChipkaartDAOPsql ovdao;
    public static boolean saved = false;

    public ProductDAOPsql(Connection conn) throws SQLException {
        this.conn = conn;
    }

    @Override
    public boolean save(Product product) throws SQLException {

        String saveQuery = "insert into product (product_nummer, naam, beschrijving, prijs) values (?, ?, ?, ?)";

        try(PreparedStatement ps = conn.prepareStatement(saveQuery)) {
            ps.setInt(1, product.getProductNummer());
            ps.setString(2, product.getNaam());
            ps.setString(3, product.getBeschrijving());
            ps.setFloat(4, product.getPrijs());

            ps.execute();
            ps.close();
        }catch (SQLException e){
            return false;
        }

        ovdao = new OVChipkaartDAOPsql(conn);
        ArrayList<OVChipkaart> databaseChipkaarten = ovdao.findAll();

        for(int chipkaartId : product.getOvChipkaarten()){
            if(!databaseChipkaarten.contains(OVChipkaart.findById(chipkaartId))){
                ovdao.save(OVChipkaart.findById(chipkaartId));
            }
        }

        if(saved == false) {
            String saveTussenQuery = "insert into ov_chipkaart_product (kaart_nummer, product_nummer) values (?, ?);";

            for (Integer ovChipkaartId : product.getOvChipkaarten()) {
                try (PreparedStatement ps = conn.prepareStatement(saveTussenQuery)) {
                    ps.setInt(1, ovChipkaartId);
                    ps.setInt(2, product.getProductNummer());
                    ps.execute();
                    saved = true;
                    return true;
                } catch (SQLException e) {
                    System.out.println("Error saving new chipkaart-product object");
                    System.out.println(e);
                    return false;
                }
            }
        }
        else{saved = false;}
        return true;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        String updateQuery = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?;";

        try(PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            ps.setString(1, product.getNaam());
            ps.setString(2, product.getBeschrijving());
            ps.setFloat(3, product.getPrijs());
            ps.setInt(4, product.getProductNummer());

            // Prepared statement uitvoeren en closen.
            ps.execute();
            ps.close();
        }catch (SQLException e){
            System.out.println("Error updating Product");
            System.out.println(e);
        }
        ovdao = new OVChipkaartDAOPsql(conn);
        ArrayList<OVChipkaart> databaseChipkaarten = ovdao.findAll();
        for(int ovChipkaartId : product.getOvChipkaarten()){
            OVChipkaart ovChipkaart = OVChipkaart.findById(ovChipkaartId);
            ovdao.delete(ovChipkaart);
            ovdao.save(ovChipkaart);
        }
        return true;
    }

    @Override
    public boolean delete(Product product) {
        product.deleteProduct(product);

        String deleteQuery = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?; DELETE FROM product WHERE product_nummer = ?; ";
        try(PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setInt(1, product.getProductNummer());
            ps.setInt(2, product.getProductNummer());
            ps.execute();
            ps.close();
            return true;
        }catch (SQLException e){
            System.out.println("Error deleting product");
            System.out.println(e);
            return false;
        }


    }

    @Override
    public ArrayList<Product> findByOvChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        String findQuery = "SELECT * FROM product JOIN ov_chipkaart_product as ocp ON product.product_nummer = ocp.product_nummer WHERE ocp.kaart_nummer = ?;";
        String ovQuery = "SELECT ov_chipkaart.kaart_nummer FROM ov_chipkaart JOIN ov_chipkaart_product as ocp ON ov_chipkaart.kaart_nummer = ocp.kaart_nummer WHERE ocp.product_nummer = ?";

        ArrayList<Product> products = new ArrayList<>();
        ovdao = new OVChipkaartDAOPsql(conn);
        try(PreparedStatement ps = conn.prepareStatement(findQuery)){
            ps.setInt(1, ovChipkaart.getKaartNummer());
            ResultSet results = ps.executeQuery();
            while (results.next()){
                Product product = new Product(results.getInt("product_nummer"), results.getString("naam"), results.getString("beschrijving"), results.getFloat("prijs"));
                try(PreparedStatement ovps = conn.prepareStatement(ovQuery)) {
                    ovps.setInt(1, product.getProductNummer());
                    ResultSet ovResults = ovps.executeQuery();
                    while (ovResults.next()){
                        product.getOvChipkaarten().add(ovResults.getInt("kaart_nummer"));
                    }
                }
                products.add(product);
            }
            return products;
        }catch (SQLException e){
            System.out.println("Error getting all products by chipkaart");
            System.out.println(e);
            return null;
        }
    }

    @Override
    public ArrayList<Product> findAll() throws SQLException {
        String findQuery = "SELECT * FROM product;";
        String ovQuery = "SELECT ov_chipkaart.kaart_nummer FROM ov_chipkaart JOIN ov_chipkaart_product as ocp ON ov_chipkaart.kaart_nummer = ocp.kaart_nummer WHERE ocp.product_nummer = ?;";

        ArrayList<Product> products = new ArrayList<>();
        ovdao = new OVChipkaartDAOPsql(conn);
        try(PreparedStatement ps = conn.prepareStatement(findQuery)){
            ResultSet results = ps.executeQuery();
            while (results.next()){
                Product product = new Product(results.getInt("product_nummer"), results.getString("naam"), results.getString("beschrijving"), results.getFloat("prijs"));
                try(PreparedStatement ovps = conn.prepareStatement(ovQuery)) {
                    ovps.setInt(1, product.getProductNummer());
                    ResultSet ovResults = ovps.executeQuery();
                    while (ovResults.next()){
                        product.getOvChipkaarten().add(ovResults.getInt("kaart_nummer"));
                    }
                }
                products.add(product);
            }
            return products;
        }catch (SQLException e){
            System.out.println("Error getting all products");
            System.out.println(e);
            return null;
        }
    }
}