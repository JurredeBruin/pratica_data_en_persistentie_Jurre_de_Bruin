package p5;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ProductDAO {
    public boolean save(Product product) throws SQLException;
    public boolean update(Product product) throws SQLException;
    public boolean delete(Product product);
    public ArrayList<Product> findByOvChipkaart(OVChipkaart ovChipkaart) throws SQLException;
    public ArrayList<Product> findAll() throws SQLException;

}
