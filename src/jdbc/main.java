package jdbc;

import java.sql.*;
public class main {
    public static void main(String[] args) throws SQLException {
        try{
            String url = "jdbc:postgresql://localhost/ovchip?user=postgres&password=Glazenpod12";
            Connection conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement();
            String query = "SELECT * From reiziger";

            ResultSet rs=st.executeQuery(query);
            String voornaam="";
            String tussenvoegsel = "";
            String achternaam="";
            String id="";
            String geboortedatum="";

            while(rs.next()){
                if(rs.getString("tussenvoegsel")==null){
                    tussenvoegsel="";
                }else{
                    tussenvoegsel=rs.getString("tussenvoegsel");
                }
                id= rs.getString("reiziger_id");
                geboortedatum= rs.getString("geboortedatum");
                voornaam=rs.getString("voorletters");
                achternaam=rs.getString("achternaam");
                System.out.println("#"+id+": "+voornaam+" "+tussenvoegsel+" "+achternaam+" ("+geboortedatum+")");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
