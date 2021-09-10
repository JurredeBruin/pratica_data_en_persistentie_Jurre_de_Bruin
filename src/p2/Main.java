package p2;

import java.sql.*;
import java.util.List;


public class Main {
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        Connection Conn = getConnection();
        ReizigerDAO rdao = new ReizigerDAOPsql(Conn);
        try {
            testReizigerDAO(rdao);
        }  catch(Exception err) {
            System.err.println("error in testReizigersDAO " + err.getMessage() );
        }

        closeConnection();
    }

    public static Connection getConnection() {
        try {
            String Url = "jdbc:postgresql://localhost:5432/ovchip";
            String User = "postgres";
            String Pass = "Glazenpod12";

            connection = DriverManager.getConnection(Url, User, Pass);
        } catch (Exception sqlex) {

        }
        return connection;
    }
    public static void closeConnection(){
        try {
            connection.close();
        } catch (Exception err) {
            System.err.println("error inside main.getConnection() " + err.getMessage() );
        }
    }
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // update een reiziger en persisteer deze in de database
        String gebdatum = "1981-03-14";
        System.out.println("[Test] ReizigerDAO.update() before:");
        List<Reiziger> reizigers1 = rdao.findAll();

        for (Reiziger r : reizigers1) {
            System.out.println(r);
        }
        Reiziger sietske2 = new Reiziger(77, "s", "", "Visser", java.sql.Date.valueOf(gebdatum));
        System.out.println("[Test] ReizigerDAO.update() after:");
        rdao.update(sietske2);
        List<Reiziger> reizigers2 = rdao.findAll();

        for (Reiziger r : reizigers2) {
            System.out.println(r);
        }
        System.out.println();

        // delete een reiziger aan en persisteer deze in de database
        String gebodatum = "2002-09-17";
        Reiziger sietske3 = new Reiziger(77, "s", "", "Visser", java.sql.Date.valueOf(gebodatum));
        System.out.print("[Test] delete by id");
        rdao.delete(sietske3);
        List<Reiziger> reizigers3=rdao.findAll();
        System.out.println(reizigers3.size() + " reizigers\n");

        //find by id
        Reiziger reiziger5 = rdao.findByid(5);
        System.out.println("[Test] ReizigerDAO.findbyid(5) geeft de volgende reizigers:");
        System.out.println(reiziger5);
        System.out.println();
        //find by geboortedatum
        List<Reiziger> reizigers4=rdao.findByGbdatum("2002-09-17");
        System.out.println("[Test] ReizigerDAO.findbyGbdatum(2002-09-17) geeft de volgende reizigers:");
        for (Reiziger r : reizigers4) {
            System.out.println(r);
        }
        System.out.println();

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
    }

}
