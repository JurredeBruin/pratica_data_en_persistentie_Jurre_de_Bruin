package p3;

import p4.OVChipkaart;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Reiziger {
    private int Id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;
    private Adres adres;

    public Reiziger(int id, String vl, String tv, String an, Date gd,Adres ad) {
        this.Id=id;
        this.voorletters=vl;
        this.tussenvoegsel=tv;
        this.achternaam=an;
        this.geboortedatum=gd;
        this.adres=ad;

        if (this.adres != null) {
            this.adres.setReiziger(this);
        }
    }
    public String getName() {
        if (this.tussenvoegsel != "" && this.tussenvoegsel != null) {
            return this.voorletters + " " + this.tussenvoegsel + " " + this.achternaam;
        } else {
            return this.voorletters + " " + this.achternaam;
        }
    }

    public String toString() {
        String reizigerStr = "Reiziger{ ";

        reizigerStr += __internalGetInfo() + ", ";
        if (this.adres != null) {
            reizigerStr += "Adres" + this.adres.getInfo();
        } else {
            reizigerStr += ", null";
        }

        reizigerStr += " }";

        return reizigerStr;
    }

    public String getInfo() {
        return  "{ " + this.__internalGetInfo() + " }";
    }

    private String __internalGetInfo() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String gDatumStr = dateFormat.format(this.geboortedatum);

        String reizigerStr = "";
        reizigerStr += this.Id + ", ";
        reizigerStr += this.voorletters + ", ";
        reizigerStr += this.tussenvoegsel + ", ";
        reizigerStr += this.achternaam + ", ";
        reizigerStr += gDatumStr;
        return reizigerStr;
    }

    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

//    @Override
//    public String toString() {
//        return "Reiziger{" +
//                "Id=" + Id +
//                ", voorletters='" + voorletters + '\'' +
//                ", tussenvoegsel='" + tussenvoegsel + '\'' +
//                ", achternaam='" + achternaam + '\'' +
//                ", geboortedatum=" + geboortedatum +
//                 ",adres=("+
//                ", #'" + adres.getId() + '\'' +
//                ", '" + adres.getPostcode() + '\'' +
//                ", '" + adres.getHuisnummer() + ')'+'\'' +
////                ", achternaam='" + adres.getHuisnummer() + '\'' +
////                ", achternaam='" + adres.getStraat() + '\'' +
////                ", achternaam='" + adres.getWoonplaats() + '\'' +
//                '}';
//    }

    public void setAdres(Adres adresObj) {
        this.adres = adresObj;
    }

    public Adres getAdres() {
        return adres;
    }
}
