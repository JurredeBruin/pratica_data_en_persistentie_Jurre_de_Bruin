package p3;

import java.sql.Date;
import java.sql.PreparedStatement;

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

    @Override
    public String toString() {
        return "Reiziger{" +
                "Id=" + Id +
                ", voorletters='" + voorletters + '\'' +
                ", tussenvoegsel='" + tussenvoegsel + '\'' +
                ", achternaam='" + achternaam + '\'' +
                ", geboortedatum=" + geboortedatum +
                 ",adres=("+
                ", achternaam='" + adres.getId() + '\'' +
                ", achternaam='" + adres.getPostcode() + '\'' +
                ", achternaam='" + adres.getHuisnummer() + '\'' +
                ", achternaam='" + adres.getHuisnummer() + '\'' +
                ", achternaam='" + adres.getStraat() + '\'' +
                ", achternaam='" + adres.getWoonplaats() + '\'' +
                '}';
    }
}
