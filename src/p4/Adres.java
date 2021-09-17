package p4;

public class Adres{
    private int id;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;
    private int reiziger_id;
    private Reiziger reisobj;

    public Adres (int id, String postcode, String huisnummer, String straat, String woonplaats, int reiziger_id, Reiziger reisobj) {
        this.id = id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
        this.reiziger_id = reiziger_id;
        this.reisobj = reisobj;
        if (reisobj != null) {
            this.reisobj.setAdres(this);
        }
    }

    private String __internalGetInfo() {
        String adresStr = "";
        adresStr += this.id + ", ";
        adresStr += this.straat + ", ";
        adresStr += this.huisnummer + ", ";
        adresStr += this.postcode + ", ";
        adresStr += this.woonplaats + ", ";
        adresStr += this.reiziger_id;

        return adresStr;
    }

    public String getInfo() {
        return  "{ " + this.__internalGetInfo() + " }";
    }

    public String toString() {
        String adresStr = "Adres{ ";
        adresStr += this.__internalGetInfo() + ", ";

        if (reisobj == null) {
            adresStr += "NULL";
        } else {
            adresStr += "Reiziger" + this.reisobj.getInfo();
        }

        adresStr += " }";

        return adresStr;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getStraat() {
        return straat;
    }
    public void setStraat(String straat) {
        this.straat = straat;
    }
    public String getHuisnummer() {
        return huisnummer;
    }
    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
    }
    public String getWoonplaats() {
        return woonplaats;
    }
    public void setWoonplaats(String woonplaats) {
        this.woonplaats = woonplaats;
    }
    public String getPostcode() {
        return postcode;
    }
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
    public int getReiziger_id() {
        return reiziger_id;
    }
    public void setReiziger_id(int reiziger_id) {
        this.reiziger_id = reiziger_id;
    }
    public Reiziger getReiziger() {
        return reisobj;
    }
    public void setReiziger(Reiziger reiziger) {
        this.reisobj = reiziger;
    }

}
