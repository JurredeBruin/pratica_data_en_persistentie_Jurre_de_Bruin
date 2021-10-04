package p5;

import java.util.ArrayList;
import java.util.List;

public interface ProductDAO {
    boolean save(Product product);
    boolean update(Product product);
    boolean delete(Product product);
    ArrayList<Product>findByOvchipkaart(OVChipkaart ovChipkaart);
    ArrayList<Product> findAll();

}
