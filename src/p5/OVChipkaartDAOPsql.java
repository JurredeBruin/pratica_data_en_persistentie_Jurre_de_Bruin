package p5;


import java.sql.*;
import java.util.ArrayList;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection conn;
    private ReizigerDAOPsql rdao;
    private ProductDAOPsql pdao;

    public OVChipkaartDAOPsql(Connection conn) throws SQLException {
        this.conn = conn;

    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        // Query om nieuwe OV Chipkaart op te slaan in database.
        String saveQuery = "insert into ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) values (?, ?, ?, ?, ?)";

        // Gebruik van prepared statement om makkelijk variabelen in de query te doen.
        try(PreparedStatement ps = conn.prepareStatement(saveQuery)) {
            ps.setInt(1, ovChipkaart.getKaartNummer());
            ps.setDate(2, new Date(ovChipkaart.getGeldigTot().getTime()));
            ps.setInt(3, ovChipkaart.getKlasse());
            ps.setFloat(4, ovChipkaart.getSaldo());
            ps.setInt(5, ovChipkaart.getReiziger().getId());

            // Prepared statement uitvoeren en closen.
            ps.execute();
            ps.close();
        }catch (SQLException e){
            return false;
        }

        pdao = new ProductDAOPsql(conn);
        ArrayList<Product> databaseProducten = pdao.findAll();

        for(Product product : ovChipkaart.getProducten()){
            if(!databaseProducten.contains(product)){
                pdao.save(product);
            }
        }

        if(ProductDAOPsql.saved == false) {
            String saveTussenQuery = "insert into ov_chipkaart_product (kaart_nummer, product_nummer) values (?, ?)";
            for (Product product : ovChipkaart.getProducten()) {
                try (PreparedStatement ps = conn.prepareStatement(saveTussenQuery)) {
                    ps.setInt(1, ovChipkaart.getKaartNummer());
                    ps.setInt(2, product.getProductNummer());
                    ps.execute();
                    ProductDAOPsql.saved = true;
                    return true;
                } catch (SQLException e) {
                    System.out.println("Error saving new chipkaart-product object");
                    System.out.println(e);
                    return false;
                }
            }
        }else{
            ProductDAOPsql.saved = false;}


        return true;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {
        // Query om een OV Chipkaart te updaten.
        String updateQuery = "UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? WHERE kaart_nummer = ?;";

        // Variabelen in de query stoppen.
        try(PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            ps.setDate(1, new Date(ovChipkaart.getGeldigTot().getTime()));
            ps.setInt(2, ovChipkaart.getKlasse());
            ps.setFloat(3, ovChipkaart.getSaldo());
            ps.setInt(4, ovChipkaart.getReiziger().getId());
            ps.setInt(5, ovChipkaart.getKaartNummer());

            // Prepared statement uitvoeren en closen.
            ps.execute();
            ps.close();
        }catch (SQLException e){
            System.out.println("Error updating OV Chipkaart");
            System.out.println(e);
        }

        pdao = new ProductDAOPsql(conn);
        ArrayList<Product> databaseProducten = pdao.findAll();

        for(Product product : ovChipkaart.getProducten()){
            pdao.delete(product);
            pdao.save(product);
        }

        return true;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        // De OV Chipkaart uit de lijst met chipkaarten van de reiziger halen en bij alle producten uit de lijst halen.
        ovChipkaart.deleteOvChipkaart(ovChipkaart);

        // Delete query maken en uitvoeren
        String deleteQuery = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?; DELETE FROM ov_chipkaart WHERE kaart_nummer = ?; ";
        try(PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setInt(1, ovChipkaart.getKaartNummer());
            ps.setInt(2, ovChipkaart.getKaartNummer());
            ps.execute();
            ps.close();
            return true;
        }catch (SQLException e){
            System.out.println("Error deleting OV Chipkaart");
            System.out.println(e);
            return false;
        }
    }

    @Override
    public OVChipkaart findById(int id) throws SQLException {
        // Maak de query en voer hem uit.
        String findByIdQuery = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?";
        try(PreparedStatement ps = conn.prepareStatement(findByIdQuery)) {
            rdao = new ReizigerDAOPsql(conn);
            ps.setInt(1, id);
            ResultSet results = ps.executeQuery();
            // Het aanmaken van het ovchipkaart object in een if zodat het niet fout gaat als er geen resultaat is.
            if (results.next()){
                // De database aanroepen om bijbehorende reiziger op te halen en daarna ovchipkaart object aanmaken.
                OVChipkaart ovChipkaart = new OVChipkaart(id, new Date(results.getDate("geldig_tot").getTime()), results.getInt("klasse"), results.getFloat("saldo"),  rdao.findById(results.getInt("reiziger_id")));
                ps.close();
                return ovChipkaart;
            }
            else {
                return null;
            }
        }catch (SQLException e){
            System.out.println("Error trying to find OV Chipkaart by id");
            System.out.println(e);
            return null;
        }
    }

    @Override
    public ArrayList<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        String findByReizigerQuery = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?;";
        // Lijst maken van chipkaarten om te vullen uit de database.
        ArrayList<OVChipkaart> chipkaarten = new ArrayList<OVChipkaart>();
        // Query uitvoeren en lijst invullen.
        try(PreparedStatement ps = conn.prepareStatement(findByReizigerQuery)) {
            rdao = new ReizigerDAOPsql(conn);
            ps.setInt(1, reiziger.getId());
            ResultSet results = ps.executeQuery();
            while(results.next()) {
                OVChipkaart ovChipkaart = new OVChipkaart(results.getInt("kaart_nummer"), new Date(results.getDate("geldig_tot").getTime()), results.getInt("klasse"), results.getFloat("saldo"), reiziger);
                chipkaarten.add(ovChipkaart);
            }
            ps.close();
            return chipkaarten;
        }catch (SQLException e){

            System.out.println("Error trying to find ov chipkaart by reiziger, maybe reiziger doesn't have an ov chipkaart yet");
            System.out.println(e);
            return null;
        }
    }

    @Override
    public ArrayList<OVChipkaart> findByProduct(Product product) throws SQLException {
        ArrayList<OVChipkaart> chipkaarten = new ArrayList<>();
        String findQuery = "SELECT * FROM ov_chipkaart JOIN ov_chipkaart_product as ocp ON ov_chipkaart.kaart_nummer = ocp.kaart_nummer WHERE ocp.product_nummer = ?;";
        String productQuery = "SELECT * FROM product JOIN ov_chipkaart_product as ocp ON product.product_nummer = ocp.product_nummer WHERE ocp.kaart_nummer = ?;";

        try(PreparedStatement ps = conn.prepareStatement(findQuery)){
            ps.setInt(1, product.getProductNummer());
            ResultSet results = ps.executeQuery();

            //Alle reizigers ophalen
            rdao = new ReizigerDAOPsql(conn);
            pdao = new ProductDAOPsql(conn);
            ArrayList<Reiziger> reizigers = rdao.findAll();
            while (results.next()){
                // Door reizigers zoeken naar de passende reiziger bij de OV chipkaart, op deze manier hoeft er maar 1 keer de database geroepen te worden voor reizigers in plaats van als ik rdao.findById zou doen.
                for(Reiziger reiziger : reizigers){
                    if(reiziger.getId() == results.getInt("reiziger_id")){
                        OVChipkaart ovChipkaart = new OVChipkaart(results.getInt("kaart_nummer"), new Date(results.getDate("geldig_tot").getTime()), results.getInt("klasse"), results.getFloat("saldo"), reiziger);
                        try(PreparedStatement pps = conn.prepareStatement(productQuery)) {
                            pps.setInt(1, ovChipkaart.getKaartNummer());
                            ResultSet ovResults = pps.executeQuery();
                            while (ovResults.next()){
                                pdao.findAll();
                                ovChipkaart.getProducten().add(Product.findById(ovResults.getInt("product_nummer")));
                            }
                        }
                        chipkaarten.add(ovChipkaart);
                    }
                }
            }
            ps.close();
            return chipkaarten;
        }
        catch (SQLException e){
            System.out.println("Error getting all chipkaarten");
            System.out.println(e);
            return null;
        }
    }

    @Override
    public ArrayList<OVChipkaart> findAll() throws SQLException {
        String findAllQuery = "SELECT * FROM ov_chipkaart;";
        String productQuery = "SELECT product.product_nummer FROM product JOIN ov_chipkaart_product as ocp ON product.product_nummer = ocp.product_nummer WHERE ocp.kaart_nummer = ?;";
        ArrayList<OVChipkaart> chipkaarten = new ArrayList<>();
        try(PreparedStatement ps = conn.prepareStatement(findAllQuery)){
            ResultSet results = ps.executeQuery();

            //Alle reizigers ophalen
            rdao = new ReizigerDAOPsql(conn);
            pdao = new ProductDAOPsql(conn);
            ArrayList<Reiziger> reizigers = rdao.findAll();
            while (results.next()){
                // Door reizigers zoeken naar de passende reiziger bij de OV chipkaart, op deze manier hoeft er maar 1 keer de database geroepen te worden voor reizigers in plaats van als ik rdao.findById zou doen.
                for(Reiziger reiziger : reizigers){
                    if(reiziger.getId() == results.getInt("reiziger_id")){
                        OVChipkaart ovChipkaart = new OVChipkaart(results.getInt("kaart_nummer"), new Date(results.getDate("geldig_tot").getTime()), results.getInt("klasse"), results.getFloat("saldo"), reiziger);
                        try(PreparedStatement peepis = conn.prepareStatement(productQuery)) {
                            peepis.setInt(1, ovChipkaart.getKaartNummer());
                            ResultSet ovResults = peepis.executeQuery();
                            while (ovResults.next()) {
                                pdao.findAll();
                                ovChipkaart.getProducten().add(Product.findById(ovResults.getInt("product_nummer")));
                            }

                        }
                        chipkaarten.add(ovChipkaart);
                    }
                }
            }
            ps.close();
            return chipkaarten;
        }
        catch (SQLException e){
            System.out.println("Error getting all chipkaarten");
            System.out.println(e);
            return null;
        }
    }
}