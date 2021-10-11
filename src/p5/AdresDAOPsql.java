package p5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    private Connection conn;

    public AdresDAOPsql(Connection conn) throws SQLException {
        this.conn = conn;
    }

    @Override
    public boolean save(Adres adres) throws SQLException {
        String saveQuery = "insert into adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) values (?, ?, ?, ?, ?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(saveQuery)) {
            ps.setInt(1, adres.getId());
            ps.setString(2, adres.getPostcode());
            ps.setString(3, adres.getHuisnummer());
            ps.setString(4, adres.getStraat());
            ps.setString(5, adres.getWoonplaats());
            ps.setInt(6, adres.getReiziger().getId());

            ps.execute();
            ps.close();
            return true;
        }catch (SQLException e){
            System.out.println("Error saving new adres");
            System.out.println(e);
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        String updateQuery = "UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?;";
        try(PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            ps.setString(1, adres.getPostcode());
            ps.setString(2, adres.getHuisnummer());
            ps.setString(3, adres.getStraat());
            ps.setString(4, adres.getWoonplaats());
            ps.setInt(5, adres.getReiziger().getId());
            ps.setInt(6, adres.getId());

            ps.execute();
            ps.close();
            return true;
        }catch (SQLException e){
            System.out.println("Error updating adres");
            System.out.println(e);
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        String deleteQuery = "DELETE FROM adres WHERE adres_id = ?";
        try(PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setInt(1, adres.getId());
            ps.execute();
            ps.close();

            ReizigerDAOPsql rdao = new ReizigerDAOPsql(Main.getConnection());
            adres.getReiziger().setAdres(null);
            rdao.delete(adres.getReiziger());
            return true;
        }catch (SQLException e){
            System.out.println("Error deleting adres");
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Adres findById(int id) throws SQLException {
        String findByIdQuery = "SELECT * FROM adres WHERE adres_id = ?";
        try(PreparedStatement ps = conn.prepareStatement(findByIdQuery)) {
            ps.setInt(1, id);
            ResultSet results = ps.executeQuery();
            if (results.next()){
                ReizigerDAOPsql rdao = new ReizigerDAOPsql(Main.getConnection());
                Adres adres = new Adres(id, results.getString("postcode"), results.getString("huisnummer"), results.getString("straat"), results.getString("woonplaats"), rdao.findById(results.getInt("reiziger_id")));
                ps.close();
                return adres;
            }
            else {
                return null;
            }
        }catch (SQLException e){
            System.out.println("Error trying to find adres by id");
            System.out.println(e);
            return null;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        String findByReizigerQuery = "SELECT * FROM adres WHERE reiziger_id = ?;";
        try(PreparedStatement ps = conn.prepareStatement(findByReizigerQuery)) {
            ps.setInt(1, reiziger.getId());
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                ReizigerDAOPsql rdao = new ReizigerDAOPsql(Main.getConnection());
                Adres adres = new Adres(results.getInt("adres_id"), results.getString("postcode"), results.getString("huisnummer"), results.getString("straat"), results.getString("woonplaats"), reiziger);
                ps.close();
                return adres;
            }else  {
                return null;
            }

        }catch (SQLException e){

            System.out.println("Error trying to find adres by reiziger, maybe reiziger doesn't have an adres yet");
            System.out.println(e);
            return null;
        }
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        String findAllQuery = "SELECT * FROM adres;";
        ArrayList<Adres> allAdressen = new ArrayList<>();
        try(PreparedStatement ps = conn.prepareStatement(findAllQuery)){
            ResultSet results = ps.executeQuery();
            while (results.next()){
                ReizigerDAOPsql rdao = new ReizigerDAOPsql(Main.getConnection());
                Adres adres = new Adres(results.getInt("adres_id"), results.getString("postcode"), results.getString("huisnummer"), results.getString("straat"), results.getString("woonplaats"), rdao.findById(results.getInt("reiziger_id")));
                allAdressen.add(adres);
            }
            ps.close();
            return allAdressen;
        }
        catch (SQLException e){
            System.out.println("Error getting all adressen");
            System.out.println(e);
            return null;
        }
    }
}