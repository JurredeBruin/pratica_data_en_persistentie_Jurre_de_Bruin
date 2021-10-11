package p5;

import java.sql.*;
import java.util.ArrayList;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAOPsql adao;
    private OVChipkaartDAOPsql ovdao;

    public ReizigerDAOPsql(Connection conn) throws SQLException{
        this.conn = conn;
        adao = new AdresDAOPsql(conn);
        ovdao = new OVChipkaartDAOPsql(conn);
    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        String saveQuery = "insert into reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) values (?, ?, ?, ?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(saveQuery)) {
            ps.setInt(1, reiziger.getId());
            ps.setString(2, reiziger.getVoorletters());
            ps.setString(3, reiziger.getTussenvoegsel());
            ps.setString(4, reiziger.getAchternaam());
            ps.setDate(5, new Date(reiziger.getGeboortedatum().getTime()));
            ps.execute();
            ps.close();
            // Save alle OV Chipkaarten van de reiziger
            for (OVChipkaart ovChipkaart : reiziger.getOVChipkaarten()){
                ovdao.save(ovChipkaart);
            }
            return true;
        }catch (SQLException e){
            System.out.println("Error saving new reiziger");
            System.out.println(e);
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        String updateQuery = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateQuery)){
            ps.setString(1, reiziger.getVoorletters());
            ps.setString(2, reiziger.getTussenvoegsel());
            ps.setString(3, reiziger.getAchternaam());
            ps.setDate(4, new Date(reiziger.getGeboortedatum().getTime()));
            ps.setInt(5, reiziger.getId());
            ps.execute();
            ps.close();
            // Update alle OV Chipkaarten van de reiziger
            for (OVChipkaart ovChipkaart : reiziger.getOVChipkaarten()){
                ovdao.update(ovChipkaart);
            }
            return true;
        }
        catch (SQLException e){
            System.out.println("Error updating reiziger");
            System.out.println(e);
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        String deleteQuery = "DELETE FROM reiziger WHERE reiziger_id = ?";
        try(PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setInt(1, reiziger.getId());
            ps.execute();
            ps.close();

            if (reiziger.getAdres()!=null) {
                adao.delete(reiziger.getAdres());
            }
            if(reiziger.getOVChipkaarten().size() != 0){
                for (OVChipkaart ovChipkaart : reiziger.getOVChipkaarten()){
                    ovdao.delete(ovChipkaart);
                }
            }
            return true;
        }catch (SQLException e){
            System.out.println("Error deleting reiziger");
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        String findByIdQuery = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        try(PreparedStatement ps = conn.prepareStatement(findByIdQuery)) {
            ps.setInt(1, id);
            ResultSet results = ps.executeQuery();
            results.next();
            Reiziger reiziger = new Reiziger(id, results.getString("voorletters"), results.getString("tussenvoegsel"), results.getString("achternaam"), new Date(results.getDate("geboortedatum").getTime()));
            Adres adres = adao.findByReiziger(reiziger);
            if (adres!=null){reiziger.setAdres(adres);}
            reiziger.setOVChipkaarten(ovdao.findByReiziger(reiziger));
            ps.close();
            return reiziger;
        }catch (SQLException e){
            System.out.println("Error trying to find reiziger by id");
            System.out.println(e);
            return null;
        }
    }

    @Override
    public Reiziger findByGbdatum(java.util.Date geboortedatum) throws SQLException {
        String findByGbQuery = "SELECT * FROM reiziger WHERE geboortedatum = ?";
        try(PreparedStatement ps = conn.prepareStatement(findByGbQuery)) {
            ps.setDate(1, new Date(geboortedatum.getTime()));
            ResultSet results = ps.executeQuery();
            results.next();
            Reiziger reiziger = new Reiziger(results.getInt("reiziger_id"), results.getString("voorletters"), results.getString("tussenvoegsel"), results.getString("achternaam"), new Date(results.getDate("geboortedatum").getTime()));
            Adres adres = adao.findByReiziger(reiziger);
            if (adres!=null){reiziger.setAdres(adres);}
            reiziger.setOVChipkaarten(ovdao.findByReiziger(reiziger));
            ps.close();
            return reiziger;
        }catch (SQLException e){
            System.out.println("Error trying to find reiziger by geboortedatum");
            System.out.println(e);;
            return null;
        }
    }

    @Override
    public ArrayList<Reiziger> findAll() throws SQLException {
        String findAllQuery = "SELECT * FROM reiziger;";
        ArrayList<Reiziger> allReizigers = new ArrayList<>();
        try(PreparedStatement ps = conn.prepareStatement(findAllQuery)){
            ResultSet results = ps.executeQuery();
            while (results.next()){
                Reiziger reiziger = new Reiziger(results.getInt("reiziger_id"), results.getString("voorletters"), results.getString("tussenvoegsel"), results.getString("achternaam"), new Date(results.getDate("geboortedatum").getTime()));
                Adres adres = adao.findByReiziger(reiziger);
                if (adres!=null){reiziger.setAdres(adres);}
                reiziger.setOVChipkaarten(ovdao.findByReiziger(reiziger));
                allReizigers.add(reiziger);
            }
            ps.close();
            return allReizigers;
        }
        catch (SQLException e){
            System.out.println("Error getting all reizigers");
            System.out.println(e);
            return null;
        }
    }
}
