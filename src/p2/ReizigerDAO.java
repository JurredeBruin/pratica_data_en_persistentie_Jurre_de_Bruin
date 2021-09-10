package p2;

import java.util.List;

public interface ReizigerDAO {
    boolean save(Reiziger reiziger);
    boolean update(Reiziger reiziger);
    boolean delete(Reiziger reiziger);
    List<Reiziger> findAll();
    Reiziger findByid(int id);
    List<Reiziger> findByGbdatum(String datum);

}
