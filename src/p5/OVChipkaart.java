package p5;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OVChipkaart {
    private int kaart_nummer=0;
    private Date geldig_tot;
    private int klasse=0;
    private double saldo=0;
    private int reiziger_id=0;
    private Reiziger reizigerObj;
    private ArrayList<Product> productlist;


    public OVChipkaart(int kaart_nummer, Date geldig_tot, int klasse, double saldo, int reiziger_id) {
        this.kaart_nummer=kaart_nummer;
        this.geldig_tot=geldig_tot;
        this.klasse=klasse;
        this.saldo=saldo;
        this.reiziger_id=reiziger_id;
    }

    public void setReiziger_id(int reiziger_id) {
        this.reiziger_id = reiziger_id;
    }

    public int getReiziger_id() {
        return reiziger_id;
    }

    public Date getGeldig_tot() {
        return geldig_tot;
    }

    public void setGeldig_tot(Date geldig_tot) {
        this.geldig_tot = geldig_tot;
    }

    public void setKaart_nummer(int kaart_nummer) {
        this.kaart_nummer = kaart_nummer;
    }

    public int getKaart_nummer() {
        return kaart_nummer;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setSaldo(double sald0) {
        this.saldo = sald0;
    }

    public double getSaldo() {
        return saldo;
    }
    public Reiziger getReiziger() {
        return reizigerObj;
    }
    public void setReiziger(Reiziger reizigerObj, boolean relationCalled) {
        this.reizigerObj = reizigerObj;

        if (!relationCalled) {
            ArrayList<OVChipkaart> ovChipkaartList = new ArrayList();
            ovChipkaartList.add(this);
            this.reizigerObj.setOvChipkaartList(ovChipkaartList, true);
        }
    }
    public ArrayList<Product> getProductList() {return productlist;}
    public void setProductList(ArrayList<Product> productList, boolean relationCalled) {
        this.productlist = productList;

        if (!relationCalled) {
            for (Product product : this.productlist) {
                ArrayList<OVChipkaart> ovChipkaartList = new ArrayList();
                ovChipkaartList.add(this);
                product.setOvChipkaartList(ovChipkaartList, true);
            }
        }
    }
    public String toString() {
        String string = "OvChipkaart{ ";

        string += internalGetInfo() + ", ";
        if (this.reizigerObj != null) {
            string += "Reiziger" + this.reizigerObj.getInfo();
        } else {
            string += "null";
        }

        string += " }";
        return string;
    }

    public String getInfo() {
        return  "{ " + this.internalGetInfo() + " }";
    }

    private String internalGetInfo() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String geldigTotStr = dateFormat.format(this.geldig_tot);

        String string = "";
        string += this.kaart_nummer  + ", ";
        string += geldigTotStr + ", ";
        string += this.klasse + ", ";
        string += this.saldo + ", ";
        string += this.reiziger_id;

        return string;
    }

}
