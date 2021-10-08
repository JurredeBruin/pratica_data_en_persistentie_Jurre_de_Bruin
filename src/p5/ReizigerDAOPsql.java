package p5;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAOsql adao;
    private OVChipkaartDAO ovdao;


    public ReizigerDAOPsql(Connection connection, AdresDAOsql adao, OVChipkaartDAOPsql ovdao) {
        this.conn = connection;
        this.adao = adao;
        this.ovdao = ovdao;
    }

    public boolean save(Reiziger reiziger) {
        try {
            if (reiziger.getOvChipkaartList() == null || reiziger.getOvChipkaartList().isEmpty()) {
                throw new Exception("update heeft geen valide OvChipkaart list object");

            } else if (reiziger.getAdres() == null) {
                throw new Exception("update heeft geen valide Adres object");

            } else {
                String q = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboorteDatum) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = this.conn.prepareStatement(q);
                pst.setInt(1, reiziger.getId());
                pst.setString(2, reiziger.getVoorletters());
                pst.setString(3, reiziger.getTussenvoegsel());
                pst.setString(4, reiziger.getAchternaam());
                pst.setDate(5, new Date(reiziger.getGeboortedatum().getTime()));

                pst.execute();

                this.adao.save(reiziger.getAdres());
                this.ovdao.saveList(reiziger.getOvChipkaartList());
                return true;
            }

        } catch (Exception err) {
            System.err.println("ReizigerDAOPsql geeft een error in save(): " + err.getMessage());
            return false;
        }
    }

    public boolean update(Reiziger reiziger) {
        try {
            if (reiziger.getOvChipkaartList() == null || reiziger.getOvChipkaartList().isEmpty()) {
                throw new Exception("update heeft geen OvChipkaart list object");

            } else if (reiziger.getAdres() == null) {
                throw new Exception("update heeft geen Adres object");

            } else {
                String q = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboorteDatum = ? WHERE reiziger_id=?";
                PreparedStatement pst = this.conn.prepareStatement(q);
                pst.setString(1, reiziger.getVoorletters());
                pst.setString(2, reiziger.getTussenvoegsel());
                pst.setString(3, reiziger.getAchternaam());
                pst.setDate(4, new Date(reiziger.getGeboortedatum().getTime()));
                pst.setInt(5, reiziger.getId());

                pst.execute();

                this.adao.update(reiziger.getAdres());
                this.ovdao.updateList(reiziger.getOvChipkaartList());
                return true;
            }

        } catch (Exception err) {
            System.err.println("ReizigerDAOPsql geeft een error in update(): " + err.getMessage());
            return false;
        }
    }

    public boolean delete(Reiziger reiziger) {
        try {
            if (reiziger.getOvChipkaartList() == null || reiziger.getOvChipkaartList().isEmpty()) {
                throw new Exception("Delete heeft geen valide ovArrayList object");

            } else if (reiziger.getAdres() == null) {
                throw new Exception("Delete heeft geen valide adres object");

            } else {
                this.adao.delete(reiziger.getAdres());
                this.ovdao.deleteList(reiziger.getOvChipkaartList());

                String q = "DELETE FROM reiziger WHERE reiziger_id = ?";
                PreparedStatement pst = this.conn.prepareStatement(q);
                pst.setInt(1, reiziger.getId());
                pst.execute();

                return true;
            }

        } catch (Exception err) {
            System.err.println("ReizigerDAOPsql geeft een error in delete(): " + err.getMessage());
            return false;
        }
    }

    public Reiziger findByid(int id) {
        return this.__findById(id, null);
    }

    public Reiziger findByAdres(Adres adres) {
        int id = adres.getReiziger_id();

        return this.__findById(id, adres);
    }

    public Reiziger findByOVChipkaart(OVChipkaart ovChipkaart) {
        int id = ovChipkaart.getReiziger_id();

        return this.__findById(id, null);
    }

    public ArrayList<Reiziger> findByGbdatum(String datum) {
        ArrayList<Reiziger> reizigersArray = new ArrayList<>();

        try {
            String q = "SELECT * FROM reiziger WHERE geboorteDatum = ?";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setDate(1, Date.valueOf(datum));
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Reiziger reiziger = this.__retrieveResultSet(rs, null);
                reizigersArray.add(reiziger);
            }

            pst.close();
            rs.close();

            return reizigersArray;
        } catch (Exception err) {
            System.err.println("ReizigerDAOPsql geeft een error in findbygeboortedatum(): " + err.getMessage());
            return reizigersArray;
        }
    }

    public ArrayList<Reiziger> findAll() {
        ArrayList<Reiziger> reizigersArray = new ArrayList<>();

        try {
            Statement st = this.conn.createStatement();
            ResultSet rs = st.executeQuery("select * from reiziger");

            while (rs.next()) {
                Reiziger reiziger = this.__retrieveResultSet(rs, null);
                reizigersArray.add(reiziger);
            }
            rs.close();
        } catch (Exception err) {
            System.err.println("ReizigerDAOPsql geeft een error in findall(): " + err.getMessage());
        }

        return reizigersArray;
    }

    private Reiziger __addRelations(Reiziger reiziger) {
        this.__addAdresRelation(reiziger);
        this.__addOvchipkaartRelation(reiziger);

        return reiziger;
    }

    private void __addAdresRelation(Reiziger reiziger) {
        Adres adres = adao.findByReiziger(reiziger);
        if (adres != null) {
            reiziger.setAdres(adres, false);
        }
    }

    private void __addOvchipkaartRelation(Reiziger reiziger) {
        ArrayList<OVChipkaart> ovChipkaarten = ovdao.findByReiziger(reiziger);
        if (!ovChipkaarten.isEmpty()) {
            reiziger.setOvChipkaartList(ovChipkaarten, false);
        }
    }

    private Reiziger __findById(int id, Adres adres) {
        try {
            String q = "SELECT * FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement pst = this.conn.prepareStatement(q);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            Reiziger reiziger = null;
            if (rs.next()) {
                reiziger = this.__retrieveResultSet(rs, adres);
            }

            pst.close();
            rs.close();
            return reiziger;


        } catch (Exception err) {
            System.err.println("ReizigerDAOPsql geeft een error in __findbyid(): " + err.getMessage());
            return null;
        }
    }

    private Reiziger __retrieveResultSet(ResultSet rs, Adres adres) {
        Reiziger reiziger = null;
        try {
            reiziger = new Reiziger(
                    rs.getInt("reiziger_id"),
                    rs.getString("voorletters"),
                    rs.getString("tussenvoegsel"),
                    rs.getString("achternaam"),
                    rs.getDate("geboorteDatum")
            );

            if (adres == null) {
                this.__addAdresRelation(reiziger);
            } else {
                reiziger.setAdres(adres, false);
            }
            this.__addOvchipkaartRelation(reiziger);

        } catch (Exception err) {
            System.err.println("ReizigerDAOPsql geeft een error in retrieveresultset(): " + err.getMessage());
        }
        return reiziger;
    }
}
