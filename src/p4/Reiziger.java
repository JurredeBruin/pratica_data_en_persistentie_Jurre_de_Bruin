package p4;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class Reiziger {
    private int Id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;
    private Adres adres;
    private List<OVChipkaart> OvChipkaartList;

    public Reiziger(int id, String vl, String tv, String an, Date gd, Adres ad) {
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
    public String getInfo() {
        return  "{ " + this.extrainfo() + " }";
    }


    public String toString() {
        String AdresObjString = "";

        if (this.adres != null) {
            AdresObjString += "Adres" + this.adres.getInfo();
        } else {
            AdresObjString += "null";
        }

        String OvChipkaartList = "";
        if (this.OvChipkaartList != null && !this.OvChipkaartList.isEmpty()) {
            OvChipkaartList += "chip kaarten lijst[";
            for (int i=0; i <this.OvChipkaartList.size(); i++) {
                if (i > 1) {
                    OvChipkaartList += ", ";
                }
                OvChipkaartList += " OvChipkaart" + this.OvChipkaartList.get(i).getInfo();
            }
            OvChipkaartList += " ]";
        } else {
            OvChipkaartList = "null";
        }

        String rs = "Reiziger{ ";
        rs += extrainfo() + ", ";
        rs += AdresObjString + ", ";
        rs += OvChipkaartList;
        rs += " }";

        return rs;
    }


    private String extrainfo() {
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

    public void setAdres(Adres adresObj) {
        this.adres = adresObj;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setOvChipkaartList(List<OVChipkaart> ovChipkaartList) { this.OvChipkaartList = ovChipkaartList;}
    public List<OVChipkaart> getOvChipkaartList() {return this.OvChipkaartList;}
}
