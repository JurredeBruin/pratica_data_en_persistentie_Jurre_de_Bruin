package p5;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public interface ReizigerDAO {
    public boolean save(Reiziger reiziger) throws SQLException;
    public boolean update(Reiziger reiziger) throws SQLException;
    public boolean delete(Reiziger reiziger) throws SQLException;
    public Reiziger findById(int id) throws SQLException;
    public Reiziger findByGbdatum(Date geboortedatum) throws SQLException;
    public ArrayList<Reiziger> findAll() throws SQLException;
}
