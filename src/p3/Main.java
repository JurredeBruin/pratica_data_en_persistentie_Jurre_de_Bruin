package p3;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


public class Main {
    private static Connection connection;
    private static AdresDAOsql adao;
    private static ReizigerDAOPsql rdao;

    public static void main(String[] args) throws SQLException {
        Connection Conn = getConnection();
        adao = new AdresDAOsql(Conn);
        rdao = new ReizigerDAOPsql(Conn,adao);
        adao.connectRDAO(rdao);
        try {
            testReizigerDAO(rdao);
            testAdresDAO();
        }  catch(Exception err) {
            System.err.println("error in testReizigersDAO " + err.getMessage() );
        }

        closeConnection();
    }

    public static Connection getConnection() throws SQLException {
        if(connection==null){
            String Url = "jdbc:postgresql://localhost:5432/ovchip";
            String User = "postgres";
            String Pass = "Glazenpod12";

            connection = DriverManager.getConnection(Url, User, Pass);
        }
        return connection;
    }
    public static void closeConnection() throws SQLException {
        if(connection!=null){
            connection.close();
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
        AdresDAO adao = new AdresDAOsql(connection);
        Adres gideonstraat3 = new Adres(9, "8754kl", "786", "gideonstraat", "noorwegen",26,null);
        Reiziger sietske = new Reiziger(26, "S", "", "Boers", Date.valueOf(gbdatum),gideonstraat3);
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
        Reiziger sietske2 = new Reiziger(26, "s", "", "Visser", Date.valueOf(gebdatum),gideonstraat3);
        System.out.println("[Test] ReizigerDAO.update() after:");

        rdao.update(sietske2);
        List<Reiziger> reizigers2 = rdao.findAll();

        for (Reiziger r : reizigers2) {
            System.out.println(r);
        }
        System.out.println();

        // delete een reiziger aan en persisteer deze in de database
        String gebodatum = "2002-09-17";
        Reiziger sietske3 = new Reiziger(26, "s", "", "Visser", Date.valueOf(gebodatum),gideonstraat3);
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


        //extra voor adres test reiziger
//        String geboodatum = "1981-03-14";
//        Reiziger sietske9 = new Reiziger(33,"S", "van", "Boers", Date.valueOf(geboodatum),gideonstraat3);
//        rdao.save(sietske9);
//        rdao.findAll();
//        System.out.println(reizigers.size() + " reizigers\n");



        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
    }
    private static void testAdresDAO() throws SQLException {
        System.out.println("\n---------- Test Adres dao -------------");
        if (rdao.findByid(6) == null) {
            String gbdatum2 = "1981-03-14";
            Reiziger test = new Reiziger(6, "t", "est", " voor adres", Date.valueOf(gbdatum2), null);
            rdao.save(test);
        }
        // Haal alle adressen op uit de database
        List<Adres> adres = adao.findall();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adres) {
            System.out.println(a);
        }
        System.out.println();

        // Maak een nieuw adres aan en persisteer deze in de database

        Adres gideonstraat = new Adres(7, "8754kl", "786", "gideonstraat", "noorwegen",33,null);
        System.out.print("[Test] Eerst " + adres.size() + " adressen, na adao.save() ");
        adao.save(gideonstraat);
        adres = adao.findall();
        System.out.println(adres.size() + " adressen\n");

        //update adres


        System.out.println("[Test] AdresDAO.update() before:");
        List<Adres> adres1 = adao.findall();
        for (Adres r : adres1) {
            System.out.println(r);
        }
        System.out.println();
        gideonstraat.setWoonplaats("amerika");
        gideonstraat.setPostcode("newyork");
        adao.update(gideonstraat);
        List<Adres>adres2=adao.findall();
        for (Adres r : adres2) {
            System.out.println(r);
        }
        System.out.println();


        //getbyreiziger
        ReizigerDAO rdao = new ReizigerDAOPsql(connection, adao);
        Adres reizigadres = adao.findByReiziger( rdao.findByid(2) );
        System.out.println(reizigadres);
        System.out.println();


        //delete adres
        Adres gideonstraat3 = new Adres(7, "9986ol", "786", "gideonstraat", "noorwegen",33,null);
        System.out.print("[Test] delete by id ");
        adao.delete(gideonstraat3);
        List<Adres> adres4=adao.findall();
        System.out.println(adres4.size() + " adressen\n");


    }



    }
