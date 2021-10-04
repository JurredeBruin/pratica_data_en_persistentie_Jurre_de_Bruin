package p5;

import java.util.ArrayList;

public class Product {
    private int product_nummer;
    private String naam;
    private String beschrijving;
    private double prijs;
    private ArrayList<OVChipkaart> ovlist;

    public Product(int product_nummer, String naam, String beschrijving, double prijs) {
        this.product_nummer=product_nummer;
        this.naam=naam;
        this.beschrijving=beschrijving;
        this.prijs=prijs;
    }

    public int getProduct_nummer() {
        return product_nummer;
    }
    public String getNaam(){
        return naam  ;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public void setProduct_nummer(int product_nummer) {
        this.product_nummer = product_nummer;
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_nummer=" + product_nummer +
                ", naam='" + naam + '\'' +
                ", beschrijving='" + beschrijving + '\'' +
                ", prijs=" + prijs +
                '}';
    }

    public ArrayList<OVChipkaart> getOvChipkaartList() { return ovlist; }
    public void setOvChipkaartList(ArrayList<OVChipkaart> ovChipkaartList, boolean relationCalled) {
        this.ovlist = ovChipkaartList;

        for (OVChipkaart ovChipkaart : this.ovlist) {
            ArrayList<Product> productList = new ArrayList();
            productList.add(this);
            ovChipkaart.setProductList(productList, true);
        }
    }
    }
